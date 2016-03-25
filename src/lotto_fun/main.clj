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

(defn least-winners [seqs]
  (let [winning-nums (count-winning-numbers-excluding-powerball seqs)]
    (println "Least 10 numbers")
    (println "Number\tTimes picked")
    (doseq [ num-pair (take-last 10 winning-nums) ]
      (println num-pair))))

(defn top-powerballs [seqs]
  (let [winning-nums (count-powerballs-only seqs)]
    (println "Top 10 Powerball numbers")
    (println "Number\tTimes picked")
    (doseq [ num-pair (take 10 winning-nums) ]
      (println num-pair))))

(defn least-powerballs [seqs]
  (let [winning-nums (count-powerballs-only seqs)]
    (println "Least 10 Powerball numbers")
    (println "Number\tTimes picked")
    (doseq [ num-pair (take-last 10 winning-nums) ]
      (println num-pair))))

(defn exit []
  (println "Bye for now!")
  (System/exit 0))

(defn help []
  (println "Commands are:")
  (println "(h)elp    --- displays this help")
  (println "(t)op     --- displays top 10 most winning numbers")
  (println "(l)east   --- displays 10 least winning numbers")
  (println "(p)ower   --- displays top 10 most winning powerballs")
  (println "lp(o)wer  --- displays 10 least winning powerballs")
  (println "(q)uit    --- exits the program"))

(defn- format-input [input]
  (str/lower-case (str/trim input)))

(defn process-input [seqs]
  (println "Enter command: (type help for list of commands)")
  (print "?")
  (flush)
  (loop [line (read-line)]
    (let [input (format-input line)]
      (if (or (= "q" input) (= "quit" input))
        (exit))
      (if (or (= "t" input) (= "top" input))
        (top-winners seqs))
      (if (or (= "l" input) (= "least" input))
        (least-winners seqs))
      (if (or (= "p" input) (= "power" input))
        (top-powerballs seqs))
      (if (or (= "o" input) (= "lpower" input))
        (least-powerballs seqs))
      (if (or (= "h" input) (= "help" input))
        (help))
      (print "?")
      (flush)
      (recur (read-line)))))

(defn -main [& args]
  (println "Loading winning Powerball sequences...")
  (process-input (get-winning-seqs)))

  ; TODO More interesting stats coming soon!
