(ns lotto-fun.core
  (:gen-class))
  (use 'clojure.java.io)
  (require '[clojure.string :as str])

(defn process-csv
  [handler]
  (with-open [rdr (reader "NCELPowerball.csv")]
    (doseq [line (line-seq rdr)]
      (handler
        (drop 1
          (drop-last
            (str/split line #",")))))))

(defn print-it
  [line]
  (println line))

(defn -main
  [& args]
  (process-csv print-it))
