(ns olympus.docker
  (:require [clojure.core.async :as async :refer (chan go >! <!!)]
            [taoensso.timbre :as timbre]
            [cheshire.core :refer (decode)]
            [org.httpkit.client :as http]
            [olympus.instances :as instances]))
(timbre/refer-timbre)

(def hermes-port 22021)

(def need-hermes-channel (chan))

(defn curl [address method path]
  (let [{:keys [body error status]} @(http/get (format (str "http://" address ":%d" path) hermes-port))]
    (if error
      (go (>! need-hermes-channel address))
      (decode body))))

(defn version [address]
  (let [resp (curl address "GET" "/version")]
    (get resp "Version")))

(defn load-hermes [address]
  (info "Provisioning hermes on" address)
  (instances/run-cmd address "docker run -d -p 22021:22021 -v /var/run/:/var/host_run dickeyxxx/hermes"))

(defn provision [address]
  (instances/open-port address hermes-port)
  (load-hermes address))

(go (while true
      (let [address (<!! need-hermes-channel)]
        (println (load-hermes address)))))

