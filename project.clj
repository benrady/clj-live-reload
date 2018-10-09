(defproject clj-live-reload "0.1.0"
  :description "Live Reload socket static files"
  :url "github.com/benrady/clj-live-reload"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev 
             {:plugins [[com.jakemccrary/lein-test-refresh "0.18.0"]]
              :dependencies [[org.clojure/clojure "1.8.0"]
                             [clojure-future-spec "1.9.0-alpha13"]
                             [pjstadig/humane-test-output "0.8.1"]]
              :injections [(require 'pjstadig.humane-test-output) (pjstadig.humane-test-output/activate!)]}}
  :dependencies [[info.sunng/ring-jetty9-adapter "0.12.0"]
                 [cheshire "5.8.1"]
                 [clojure-watch "0.1.14"]]
  :main clj-live-reload.core
  :test-refresh {:growl false
                 :notify-on-success false
                 :quiet true
                 :stack-trace-depth 25
                 :changes-only false
                 :watch-dirs ["src" "test"]
                 :refresh-dirs ["src" "test"]})
