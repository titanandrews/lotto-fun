(ns lotto-fun.core
  (:gen-class))
  (use 'clojure.java.io)
  (require '[clojure.string :as str])

(defn process-csv
  [handle-line]
    (with-open [rdr (reader "NCELPowerball.csv")]
      (doseq [line (line-seq rdr)]
        (handle-line
          (drop 1
            (drop-last 2
              (str/split line #",")))))))

(defn clean-data
  [data]
  ; Drop the header,
  ; and drop lines from end. Do it this way in case format changes.
  (drop 1
    (filter (fn [item]
      (= (count item) 6 ))
      data)))

(defn get-winning-seqs
  []
  (def lines [] )
  (process-csv (fn [line]
    (def lines (concat lines [line]))))
  (clean-data lines))

(defn -main
  [& args]
  (println (get-winning-seqs)))
