(ns lotto-fun.core
  (:gen-class))
  (use 'clojure.java.io)
  (require '[clojure.string :as str])
  (require '[clojure.java.io :as io])

(defn- process-csv [handle-line filename]
    (with-open [rdr (reader filename)]
      (doseq [line (line-seq rdr)]
        (handle-line
          (drop 1
            (drop-last 2
              (str/split line #",")))))))

(defn coll->ints [seqs]
  "Convert collection of list of strings to collection of list of integers"
  (letfn [(to-num [s] (if (integer? s) s (java.lang.Integer/parseInt s)))]
    (map #(map to-num %1) seqs)))

(defn- clean-data [data]
  ; Drop the header,
  ; and drop lines from end. Do it this way in case format changes.
  ; Strip the quotes.
  (letfn [(strip-quotes [s] (str/replace s "\"" ""))]
    (let [stripped (map #(map strip-quotes %1) data)]
      (coll->ints (drop 1
        (filter #(= (count %) 6 ) stripped))))))

(defn- copy-uri-to-file [uri file]
  (with-open [in (io/input-stream uri)
              out (io/output-stream file)]
    (io/copy in out)))

(defn get-winning-seqs []
  "Returns sequences of past winning numbers from the Powerball CSV file
   downloaded from the internet."
  (copy-uri-to-file "http://www.nc-educationlottery.org/powerball_download.aspx"
    "NCELPowerball.csv")
  (let [lines (atom [])]
    (process-csv #(reset! lines (concat @lines [%])) "NCELPowerball.csv")
    (clean-data @lines)))

(defn- keep-track [num num-map]
  (let [key (str num)]
    (update num-map key #(if (nil? %) 1 (inc %)))))

(defn drop-powerball [s]
  (drop-last s))

(defn count-winning-numbers-excluding-powerball [winning-seqs]
  "Returns a list of pairs of the numbers paired with how many times
   it was picked, sorted by most picks first."
  (let [num-map (atom {})]
    (doseq [ row winning-seqs ]
      (doseq [ num (drop-powerball row) ]
        (reset! num-map (keep-track num @num-map))))
    (coll->ints (sort-by val > @num-map))))

(defn make-str-pairs [winning-seq]
  (letfn [(make [head v pairs]
    (when (seq v)
      (doseq [num (drop 1 v)]
        (let [pair-str (str/join "-" (sort [head num]))]
        (reset! pairs (concat @pairs [pair-str]))))
      (let [tail (drop 1 v)]
      (if (seq tail)
        (recur (nth tail 0) tail pairs)
        @pairs))))]
    (make (nth winning-seq 0) winning-seq (atom []))))

(defn count-pairs [seqs]
  "Returns a list of repeating pairs of numbers, with how many times"
  "the pair was picked, sorted by most picks first."
  (let [pair-map (atom {})]
    (doseq [ row seqs ]
      (let [nums (drop-powerball row)]
        (doseq [ pair (make-str-pairs nums) ]
          (reset! pair-map (keep-track pair @pair-map)))))
  (sort-by val > @pair-map)))

(defn filter-pairs [pairs n]
  "Filter pairs containing number n"
  (filter #(=(nth (str/split (nth % 0) #"-") 0) (str n))  pairs))

(defn count-powerballs-only [winning-seqs]
  "Returns a list of pairs of only the powerball numbers paired with how many times
   it was picked, sorted by most picks first."
  (let [num-map (atom {})]
    (doseq [ row winning-seqs ]
      (doseq [ num (drop 5 row) ]
        (reset! num-map (keep-track num @num-map))))
    (coll->ints (sort-by val > @num-map))))
