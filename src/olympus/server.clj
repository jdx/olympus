(ns olympus.server
  (:use compojure.core
        org.httpkit.server)
  (:require [clojure.java.io :as io]
            [noir.util.middleware :as middleware]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [olympus.controllers.instances :as instances-controller]))
(timbre/refer-timbre)

(defroutes app-routes
  (route/resources "/")
  (context "/api" []
           (context "/instances" [] instances-controller/instance-routes) )
  ;; TODO: don't read this file in every time in prod
  (GET "/*" [] (io/resource "public/index.html")))

(defn init [] (info "olympus started successfully"))

(defn destroy [] (info "olympus is shutting down..."))

(defn logger [handler]
  (fn [request]
    (let [resp (handler request)]
      (when-not (re-find #"^/(assets|favicon)" (:uri request))
        (info (:request-method request) (:uri request) (:params request) (:status resp)))
      resp)))

(def app
  (middleware/app-handler
    [app-routes]
    :middleware [logger]
    :formats [:json-kw :edn]))

(defn handler [request]
  (with-channel request channel
    (on-close channel (fn [status] (println "client close it" status)))
    (on-receive channel (fn [data] (info "Got message" data)))))
