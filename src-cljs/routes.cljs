(ns olympus.routes
  (:use [purnam.cljs :only [aget-in aset-in]])
  (:require olympus.app)
  (:use-macros [purnam.js :only [obj]]
               [purnam.angular :only [def.config]]))

(def.config olympus [$locationProvider $routeProvider]
  (doto $locationProvider (.html5Mode true))
  (doto $routeProvider
    (.when "/instances"    (obj :templateUrl "/pages/home.html"))
    (.when "/instances/:id" (obj :templateUrl "/pages/instance.html"))
    (.otherwise   (obj :redirectTo  "/instances"))))
