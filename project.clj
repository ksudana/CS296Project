(defproject adventure "Rescuing-Mattox-1.0"
  :description "A text-based adventure game written in Clojure."
  :url "https://github.com/ksudana/CS296Project"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/core.match "0.3.0-alpha4"]]
  :main ^:skip-aot adventure.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
