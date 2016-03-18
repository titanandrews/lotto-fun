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
  (let [lines (atom [])]
  (process-csv (fn [line]
    (reset! lines (concat (deref lines) [line]))) "NCELPowerball.csv")
  (clean-data (deref lines))))

(defn- keep-track
  [num num-map]
  (update num-map num (fn [num]
    (if (nil? num) 1 (inc num)))))

(defn count-winning-numbers-excluding-powerball
  "Returns a list of pairs of the numbers paired with how many times
  it was picked, sorted by most picks first."
  [seqs]
  (let [num-map (atom {})]
  (doseq [ seq seqs ]
    (doseq [ num (drop-last seq) ]
      (reset! num-map (keep-track num (deref num-map)))))
  (sort-by val > (deref num-map))))

(defn count-powerballs-only
  "Returns a list of pairs of only the powerball numbers paired with how many times
  it was picked, sorted by most picks first."
  [seqs]
  (let [num-map (atom {})]
  (doseq [ seq seqs ]
    (doseq [ num (drop 5 seq) ]
      (reset! num-map (keep-track num (deref num-map)))))
  (sort-by val > (deref num-map))))


