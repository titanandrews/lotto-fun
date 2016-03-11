(ns lotto-fun.core
  (:gen-class))
  (use 'clojure.java.io)
  (require '[clojure.string :as str])
  (require '[clojure.java.io :as io])

(defn- process-csv
  [handle-line filename]
    (with-open [rdr (reader filename)]
      (doseq [line (line-seq rdr)]
        (handle-line
          (drop 1
            (drop-last 2
              (str/split line #",")))))))

(defn- clean-data
  [data]
  ; Drop the header,
  ; and drop lines from end. Do it this way in case format changes.
  (drop 1
    (filter (fn [item]
      (= (count item) 6 ))
      data)))

(defn- copy-uri-to-file
  [uri file]
  (with-open [in (io/input-stream uri)
              out (io/output-stream file)]
    (io/copy in out)))

(defn get-winning-seqs
  "Returns sequences of past winning numbers from the Powerball CSV file
  downloaded from the internet."
  []
  (println "Downloading latest winning sequences...")
  (copy-uri-to-file "http://www.nc-educationlottery.org/powerball_download.aspx"
    "NCELPowerball.csv")
  (def lines [] )
  (process-csv (fn [line]
    (def lines (concat lines [line]))) "NCELPowerball.csv")
  (clean-data lines))

(defn- keep-track
  [num num-map]
  (update num-map num (fn [num]
    (if (nil? num) 1 (inc num)))))

(defn count-winning-numbers-excluding-powerball
  "Returns a list of pairs of the numbers paired with how many times
  it was picked, sorted by most picks first."
  [seqs]
  ; TODO How do I make this a local var???
  (def num-map nil)
  (doseq [ seq seqs ]
    (doseq [ num (drop-last seq) ]
      (def num-map (keep-track num num-map))))
  (sort-by val > num-map))

(defn count-powerballs-only
  "Returns a list of pairs of only the powerball numbers paired with how many times
  it was picked, sorted by most picks first."
  [seqs]
  ; TODO How do I make this a local var???
  (def num-map nil)
  (doseq [ seq seqs ]
    (doseq [ num (drop 5 seq) ]
      (def num-map (keep-track num num-map))))
  (sort-by val > num-map))

(defn -main
  [& args]
  (let [all-nums (get-winning-seqs)]
    (let [winning-nums (count-winning-numbers-excluding-powerball all-nums)]
      (println "Top 20 numbers")
      (println "Number\tTimes picked")
      (doseq [ num-pair (take 20 winning-nums) ]
        (println num-pair)))

    (let [winning-nums (count-powerballs-only all-nums)]
      (println "Top 20 Powerball numbers")
      (println "Number\tTimes picked")
      (doseq [ num-pair (take 20 winning-nums) ]
        (println num-pair)))))

    ; TODO More interesting stats coming soon!

