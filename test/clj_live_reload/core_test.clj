(ns clj-live-reload.core-test
  (:require [clojure.test :refer :all]
            [cheshire.core :refer :all]
            [ring.adapter.jetty9 :as jetty ]
            [clj-live-reload.core :refer :all]
            [clj-live-reload.core :as core]))

(def sent-string (atom nil))

(deftest clj-live-reload
  (with-redefs [jetty/send! (fn [_ msg] (reset! sent-string msg))]

    (reset! sent-string nil)

    (testing "ignores a non-hello message"
      ((ws-handler :on-text) :ws "{}")
      (is (nil? @sent-string)))
      
    (testing "sends a hello response on hello"
      ((ws-handler :on-text) :ws (generate-string {"command" "hello"}))
      (is (= core/hello-response (parse-string @sent-string))))
      
    (testing "removes a socket on close"
      (with-redefs [core/sockets (atom #{:ws})]
        ((ws-handler :on-close) :ws :status :reason)
        (is (empty? @core/sockets))))
      
    (testing "adds a socket on connect"
      (with-redefs [core/sockets (atom #{})]
        ((ws-handler :on-connect) :ws)
        (is (= #{:ws} @core/sockets))))
      
    (testing "sends a reload command on change"
      (with-redefs [core/sockets (atom [:ws])]
        (core/on-change :change "myfile")
        (is (= {"command" "reload" "path" "myfile" "liveCSS" true} 
               (parse-string @sent-string)))))
      
    (testing "Serves up livereload.js on request"
      (is (= 39120 (count (:body (core/app nil))))))))
