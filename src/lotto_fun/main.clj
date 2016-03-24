(ns lotto-fun.main
  (:gen-class))
  (use 'lotto-fun.core)

(defn -main [& args]
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

