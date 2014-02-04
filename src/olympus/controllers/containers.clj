(ns olympus.controllers.containers
  (:use compojure.core)
  (:require [olympus.docker :as docker]
            [taoensso.timbre :as timbre]
            [olympus.instances :as instances]))
(timbre/refer-timbre)

(defn address [id]
  (:public-dns-name (instances/by-id id)))

(defn get-containers [id]
  (let [containers (docker/containers (address id))]
    {:body (for [c containers] c)}))

(defn add-container [id image]
  (info (docker/add-container (address id) image))
  {:body "OK"})

(defroutes container-routes
  (GET     "/"    [id]   (get-containers id))
  (POST    "/"    [id]   (add-container id "dickeyxxx/redis")))
