(ns olympus.controllers.containers
  (:use compojure.core)
  (:require [olympus.docker :as docker]
            [olympus.instances :as instances]))

(defn get-containers [id]
  (let [address (:public-dns-name (instances/by-id id))]
    (let [containers (docker/containers address)]
      {:body (for [c containers] c)})))

(defroutes container-routes
  (GET     "/"    [id]   (get-containers id)))
