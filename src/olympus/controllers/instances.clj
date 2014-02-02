(ns olympus.controllers.instances
  (:use compojure.core)
  (:require [olympus.docker :as docker]
            [olympus.instances :as instances]))

(defn create-instance []
  (println (instances/create))
  {:body "ok"})

(defn get-instances []
  (let [instances (instances/all)]
    {:body (for [i instances] {:id (i :instance-id)
                               :instance_type (i :instance-type)
                               :status (get-in i [:state :name])
                               :public_dns_name (i :public-dns-name)})}))

(defn get-instance [id]
  (let [instance (instances/by-id id)]
    {:body {:id (instance :instance-id)
            :docker-version (docker/version (instance :public-dns-name))}}))

(defn terminate-instance [id]
  (instances/terminate id))

(defroutes instance-routes
  (GET    "/"    []   (get-instances))
  (POST   "/"    []   (create-instance))
  (GET    "/:id" [id] (get-instance id))
  (DELETE "/:id" [id] (terminate-instance id)))
