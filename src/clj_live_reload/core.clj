(ns clj-live-reload.core
  (:gen-class)
  (:require [cheshire.core :refer :all]
            [ring.adapter.jetty9 :refer [run-jetty send!]]
            [clojure-watch.core :refer [start-watch]]))

(def hello-response {"command" "hello"
                     "serverName" "clj-live-reload"
                     "protocols" ["http://livereload.com/protocols/official-7"]})

(def sockets (atom #{}))

(defn- on-ws-text [ws text-message]
  (when (= "hello" (get (parse-string text-message) "command"))
    (send! ws (generate-string hello-response))))

(def ws-handler {:on-connect (fn [ws] 
                               (swap! sockets conj ws))
                 :on-close (fn [ws status-code reason] 
                             (swap! sockets disj ws))
                 :on-text on-ws-text})

(defn app [req] 
  {:body (slurp (clojure.java.io/resource "livereload.js")) 
   :status 200})

(defn on-change [event filename]
  (doseq [ws @sockets]
    (send! ws (generate-string {:command "reload" 
                                :path filename 
                                :liveCSS true }))))

(defn- create-server []
  (run-jetty app {:port 35729 
                  :websockets {"/livereload" ws-handler} 
                  :allow-null-path-info true})) 

(defn start-server
  "Watches a directory for changes and publishes them to a live-reload socket"
  [watch-path]
  (future (create-server))
  (start-watch [{:path watch-path
                 :event-types [:create :modify :delete]
                 :callback on-change
                 :options {:recursive true}}]))

(defn -main [path]
  (start-server path))
