(ns olympus.controllers.instance
  (:use [purnam.cljs :only [aget-in aset-in]])
  (:use-macros [purnam.js :only [!]]
               [purnam.angular :only [def.controller]])
  (:require olympus.app))

(def.controller olympus.InstanceCtrl [$scope $routeParams InstanceSvc]
  (! $scope.add (fn [] (InstanceSvc.add-container $routeParams.id)))
  (-> (InstanceSvc.containers $routeParams.id)
      (.success (fn [res]
                  (! $scope.containers res))))
  (-> (InstanceSvc.info $routeParams.id)
      (.success (fn [res]
                  (! $scope.instance res)))))
