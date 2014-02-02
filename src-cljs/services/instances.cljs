(ns olympus.services.instances
  (:use [purnam.cljs :only [aget-in aset-in]])
  (:use-macros [purnam.js :only [obj]]
               [purnam.angular :only [def.service]])
  (:require olympus.app))

(def.service olympus.InstanceSvc [$http $q]
  (obj :list      #($http.get  "/api/instances")
       :create    #($http.post "/api/instances")
       :info      #($http.get    (str "/api/instances/" %))
       :ping      #($http.get    (str "/api/instances/" %))
       :terminate #($http.delete (str "/api/instances/" %))))

