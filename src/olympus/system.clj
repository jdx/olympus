(ns olympus.system
  (:use org.httpkit.server)
  (:require [olympus.server :as server]))

(defn create-system [] {:port 4000 :websocket-port 9090})

(defn start [system]
  (let [async-server (run-server server/handler {:port (:websocket-port system) :join? false})]
    (let [server (run-server server/app {:port (:port system) :join? false})]
      (into system {:server server :async-server async-server}))))

(defn stop [system]
  (when (:server system) ((:server system)))
  (when (:async-server system) ((:async-server system)))
  (dissoc system :server))
