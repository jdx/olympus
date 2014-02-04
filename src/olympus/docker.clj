(ns olympus.docker
  (:require [clojure.core.async :as async :refer (chan go >! <!!)]
            [taoensso.timbre :as timbre]
            [cheshire.core :refer (decode encode)]
            [org.httpkit.client :as http]
            [olympus.instances :as instances]))
(timbre/refer-timbre)

(def hermes-port 22021)

(def need-hermes-channel (chan))

(defn curl
  ([address method path] (curl address method path {}))
  ([address method path options]
   (if (= method :get)
     (let [{:keys [body error status]} @(http/get (format (str "http://" address ":%d" path) hermes-port) options)]
       (if error
         (do (timbre/error error) (go (>! need-hermes-channel address)))
         body))
     (let [{:keys [body error status]} @(http/post (format (str "http://" address ":%d" path) hermes-port) options)]
       (if error
         (do (timbre/error error) (go (>! need-hermes-channel address)))
         body)))))

(defn version [address]
  (let [resp (curl address :get "/version")]
    (get (decode resp) "Version")))

(defn load-hermes [address]
  (info "Provisioning hermes on" address)
  (instances/run-cmd address "docker run -d -p 22021:22021 -v /var/run/:/var/host_run dickeyxxx/hermes"))

(defn provision [address]
  (instances/open-port address hermes-port)
  (load-hermes address))

(defn containers [address]
  (decode (curl address :get "/containers/json")))

(defn add-container [address image]
  (curl address :post (str "/images/create?fromImage=" image))
  (let [body (encode {"Image" image})]
    (let [resp (decode (curl address :post "/containers/create" {:body body}))]
      (curl address :post (str "/containers/" (get resp "Id") "/start")))))

(go (while true
      (let [address (<!! need-hermes-channel)]
        (println (load-hermes address)))))

