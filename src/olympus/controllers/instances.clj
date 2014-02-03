(ns olympus.controllers.instances
  (:use compojure.core)
  (:require [olympus.docker :as docker]
            [olympus.instances :as instances]
            [olympus.controllers.containers :as containers-controller]))

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
  (let [i (instances/by-id id)]
    {:body {:id (i :instance-id)
            :public-dns-name (i :public-dns-name)
            :docker-version (docker/version (i :public-dns-name))}}))

(defn terminate-instance [id]
  (instances/terminate id))

(defroutes instance-routes
  (GET     "/"    []   (get-instances))
  (POST    "/"    []   (create-instance))
  (GET     "/:id" [id] (get-instance id))
  (DELETE  "/:id" [id] (terminate-instance id))
  (context "/:id/containers" [] containers-controller/container-routes))
