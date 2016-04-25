(ns lotto-fun.core-test
  (:require [clojure.test :refer :all]
            [lotto-fun.core :refer :all]))

(deftest test-count-winning-numbers-excluding-powerball
  (testing "should count numbers"
    (let [counts
      (count-winning-numbers-excluding-powerball [["1" "2" "3" "4" "5" "6"]
                                                  ["1" "2" "7" "8" "9" "10"]
                                                  ["9" "1" "12" "13" "14" "15"]])]
      (is (= counts [["1" 3] ["9" 2] ["2" 2] ["3" 1] ["4" 1] ["8" 1]
                     ["14" 1] ["7" 1] ["5" 1] ["12" 1] ["13" 1]])))))

(deftest test-count-powerballs-only
  (testing "should count only powerball numbers"
    (let [counts
      (count-powerballs-only [["1" "2" "3" "4" "5" "6"]
                              ["1" "2" "7" "8" "9" "10"]
                              ["9" "1" "12" "13" "14" "15"]
                              ["9" "1" "12" "13" "14" "15"]])]
      (is (= counts [["15" 2] ["6" 1] ["10" 1]])))))

(deftest test-drop-powerball
  (testing "should drop the last number in sequence"
    (let [seq
      (drop-powerball [1 2 3 4 5 6])]
      (is (= seq [1 2 3 4 5])))))

(deftest test-make-str-pairs
  (testing "should make pairs from list of numbers"
    (let [pairs
      (make-str-pairs ["1" "2" "3" "4"])]
      (is (= pairs ["1-2" "1-3" "1-4" "2-3", "2-4", "3-4"]))))
  (testing "should order each pair"
    (let [pairs
      (make-str-pairs ["4" "3" "2" "1"])]
      (is (= pairs ["3-4" "2-4" "1-4" "2-3", "1-3", "1-2"])))))

(deftest test-count-pairs
  (testing "should return winning pairs with counts, powerball is never counted, in order of counts largest to smallest"
    (let [combinations
      (count-pairs [["1" "2" "3" "4" "5" "6"]
                    ["1" "7" "2" "8" "9" "10"]
                    ["9" "1" "2" "13" "14" "15"]])]
      (is (= combinations [["1-2" 3] ["2-9" 2] ["1-9" 2] ["13-2" 1] ["13-14" 1] ["2-4" 1] ["2-8" 1] ["14-2" 1] ["1-14" 1]
        ["2-7" 1] ["1-5" 1] ["1-4" 1] ["1-8" 1] ["7-9" 1] ["2-5" 1] ["13-9" 1] ["3-4" 1] ["8-9" 1]
        ["14-9" 1] ["7-8" 1] ["1-7" 1] ["3-5" 1] ["1-3" 1] ["4-5" 1] ["2-3" 1] ["1-13" 1]])))))

(deftest test-filter-pairs
  (testing "should return filtered pairs"
    (let [filtered
      (filter-pairs [["1-2" 3] ["2-9" 2] ["1-9" 2] ["13-2" 1]] "1")]
      (is (= filtered [["1-2" 3] ["1-9" 2]])))))



