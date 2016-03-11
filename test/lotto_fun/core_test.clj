(ns lotto-fun.core-test
  (:require [clojure.test :refer :all]
            [lotto-fun.core :refer :all]))

(deftest test-count-winning-numbers-excluding-powerball
  (testing "should count numbers"
    (def counts (count-winning-numbers-excluding-powerball [["1" "2" "3" "4" "5" "6"]
                                                            ["1" "2" "7" "8" "9" "10"]
                                                            ["9" "1" "12" "13" "14" "15"]]))
    (is (= counts [["1" 3] ["9" 2] ["2" 2] ["3" 1] ["4" 1] ["8" 1]
                   ["14" 1] ["7" 1] ["5" 1] ["12" 1] ["13" 1]]))))


(deftest test-count-powerballs-only
  (testing "should count only powerball numbers"
    (def counts (count-powerballs-only [["1" "2" "3" "4" "5" "6"]
                                        ["1" "2" "7" "8" "9" "10"]
                                        ["9" "1" "12" "13" "14" "15"]
                                        ["9" "1" "12" "13" "14" "15"]]))
    (is (= counts [["15" 2] ["6" 1] ["10" 1]]))))
