(ns lotto-fun.main
  (:gen-class))
  (use 'lotto-fun.core)
  (require '[clojure.string :as str])

(defn top-winners [seqs]
  (let [winning-nums (count-winning-numbers-excluding-powerball seqs)]
    (println "Top 10 numbers")
    (println "Number\tTimes picked")
    (doseq [ num-pair (take 10 winning-nums) ]
      (println num-pair))))

(defn top-powerballs [seqs]
  (let [winning-nums (count-powerballs-only seqs)]
    (println "Top 10 Powerball numbers")
    (println "Number\tTimes picked")
    (doseq [ num-pair (take 10 winning-nums) ]
      (println num-pair))))

(defn exit []
  (println "Bye for now!")
  (System/exit 0))

(defn help []
  (println "Commands are:")
  (println "(h)elp  --- displays this help")
  (println "(t)op   --- displays top 10 most winning numbers")
  (println "(p)ower --- displays top 10 most winning powerballs")
  (println "(q)uit  --- exits the program"))

(defn process-input [seqs]
  (println "Enter command: (type help for list of commands)")
  (print "?")
  (flush)
  (loop [input (read-line)]
    (let [lower-input (str/lower-case input)]
      (if (or (= "q" lower-input) (= "quit" lower-input))
        (exit))
      (if (or (= "t" lower-input) (= "top" lower-input))
        (top-winners seqs))
      (if (or (= "p" lower-input) (= "power" lower-input))
        (top-powerballs seqs))
      (if (or (= "h" lower-input) (= "help" lower-input))
        (help))
      (print "?")
      (flush)
      (recur (read-line)))))


(defn -main [& args]
  (println "Loading winning Powerball sequences...")
  (process-input (get-winning-seqs)))

  ; TODO More interesting stats coming soon!

