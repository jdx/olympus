(ns olympus.controllers.instances
  (:use [purnam.cljs :only [aget-in aset-in]])
  (:use-macros [purnam.js :only [!]]
               [purnam.angular :only [def.controller]])
  (:require olympus.app
            olympus.services.instances))

(def.controller olympus.InstancesCtrl [$scope InstanceSvc]
  (! $scope.status "Create")
  (! $scope.refresh #(-> (InstanceSvc.list)
                                    (.success (fn [res]
                                                (! $scope.servers res)))))
  (! $scope.ping #(-> (InstanceSvc.ping %)
                      (.success (fn [res] (js/console.log res)))))
  ($scope.refresh)
  (! $scope.create #(-> (InstanceSvc.create)
                        (.success (fn [res] ($scope.refresh)
                                            (! $scope.status "Booting...")))))
  (! $scope.terminate #(-> (InstanceSvc.terminate %)
                           (.success (fn [res] ($scope.refresh))))))
