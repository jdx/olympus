(defproject olympus "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [lib-noir "0.8.0"]
                 [compojure "1.1.6"]
                 [ring-server "0.3.1"]
                 [com.taoensso/timbre "3.0.0"]
                 [org.clojure/clojurescript "0.0-2156"]
                 [amazonica "0.2.3"]
                 [clj-ssh "0.5.7"]
                 [http-kit "2.1.16"]
                 [im.chit/purnam "0.1.8"]]
  :cljsbuild {:builds [{:source-paths ["src-cljs"]
              :compiler {:output-dir "resources/public/assets/js"
                         :output-to  "resources/public/assets/js/site.js"
                         :source-map "resources/public/assets/js/site.js.map"
                         :optimizations :whitespace}}]}
  :plugins [[lein-cljsbuild "1.0.2"]]
  :ring {:handler olympus.server/app
         :init    olympus.server/init
         :destroy olympus.server/destroy}
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[midje "1.6.0"]
                                  [http-kit.fake "0.2.1"]
                                  [org.clojure/tools.namespace "0.2.4"]]
                   :env {:dev true}}})
