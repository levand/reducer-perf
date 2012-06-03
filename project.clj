(defproject reducer-perf "0.1.0-SNAPSHOT"
  :description "Test the performance of Clojure's new reducers"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[incanter "1.3.0"]
                 [criterium "0.2.1"]
                 [org.codehaus.jsr166-mirror/jsr166y "1.7.0"]
                 [org.clojure/clojure "1.5.0-master-SNAPSHOT"]]
  :jvm-opts ["-Xmx2g"]
  ;; :jvm-opts ["-Xmx48g"]
  :main reducer-perf.bench
  )