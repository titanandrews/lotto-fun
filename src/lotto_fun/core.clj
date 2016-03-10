(ns lotto-fun.core
  (:gen-class))
  (use 'clojure.java.io)
  (require '[clojure.string :as str])
  (require '[clojure.java.io :as io])

(defn process-csv
  [handle-line filename]
    (with-open [rdr (reader filename)]
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

(defn copy-uri-to-file
  [uri file]
  (with-open [in (io/input-stream uri)
              out (io/output-stream file)]
    (io/copy in out)))

(defn get-winning-seqs
  []
  (println "Downloading latest winning sequences...")
  (copy-uri-to-file "http://www.nc-educationlottery.org/powerball_download.aspx"
    "NCELPowerball.csv")
  (def lines [] )
  (process-csv (fn [line]
    (def lines (concat lines [line]))) "NCELPowerball.csv")
  (clean-data lines))

(defn -main
  [& args]
  (println (get-winning-seqs)))
