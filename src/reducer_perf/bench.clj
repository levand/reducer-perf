(ns reducer-perf.bench
  (:require
   [clojure.java.io :as io]
   [criterium.core :as c]
   [clojure.core.reducers :as r]))

(defn mymax
  (^double [] 0.0)
  (^double [^double n] n)
  (^double [^double a ^double b] (max a b)))

(defn good-enough?
  [^double n ^double g]
  (< (Math/abs (- (* g g) n)) 0.00001))

(defn ffn
  [^double arg]
  (< arg 0.5))

;; Deliberately not hinted so as to be slightly more cpu-intensive
(defn sqrt
  ([n] (sqrt n 0.1))
  ([n g]
     (if (good-enough? n g)
       g
       (recur n (/ (+ g (/ n g))
                   2)))))

(defn run-n
  [n]
  (let [v (vec (repeatedly n rand))]
    (c/with-progress-reporting
      {:vanilla-reduce (c/quick-benchmark (reduce mymax v))
       :reducer-reduce (c/quick-benchmark (r/reduce mymax v))
       :reducer-fold (c/quick-benchmark (r/reduce mymax v))
       :vanilla-mapreduce-lite (c/quick-benchmark (reduce mymax (map inc v)))
       :reducer-mapreduce-lite (c/quick-benchmark (r/reduce mymax (r/map inc v)))
       :reducer-mapfold-lite (c/quick-benchmark (r/fold mymax (r/map inc v)))
       :vanilla-mapreduce (c/quick-benchmark (reduce mymax (map sqrt v)))
       :reducer-mapreduce (c/quick-benchmark (r/reduce mymax (r/map sqrt v)))
       :reducer-mapfold (c/quick-benchmark (r/fold mymax (r/map sqrt v)))
       :vanilla-filtermapreduce (c/quick-benchmark (reduce mymax (map sqrt (filter ffn v))))
       :reducer-filtermapreduce (c/quick-benchmark (r/reduce mymax (r/map sqrt (r/filter ffn v))))
       :reducer-filtermapfold (c/quick-benchmark (r/fold mymax (r/map sqrt (r/filter ffn v))))
       })))

(defn -main
  [name cores]
  (let [results (into {} (map (fn [n] [n (run-n n)])
                              ;; [10000 1000000 100000000 1000000000]
                              [1000 100000]
                              ))
        f (io/file (str "benchmarks/" name "/" cores))
        p (.getParentFile f)]
    (.mkdirs p)
    (spit f (assoc results
              :name name
              :cores cores))))




