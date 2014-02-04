(ns olympus.test.docker
  (:use clojure.test
        midje.sweet
        org.httpkit.fake
        olympus.docker)
  (:require [olympus.instances :as instances]))

(deftest docker
  (fact "loads hermes"
        (provision "localhost") => true
        (provided (load-hermes "localhost") => true)
        (provided (instances/open-port "localhost" 22021) => true))
  (fact "gets docker version"
        (version "localhost") => "0.7.6"
        (provided (curl "localhost" :get "/version") => "{\"Version\":\"0.7.6\"}"))
  (with-fake-http ["http://localhost:22021/version" "{\"Version\":\"0.7.6\"}"]
    (fact "gets docker version"
          (curl "localhost" :get "/version") => "{\"Version\":\"0.7.6\"}")))
