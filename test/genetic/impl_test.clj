(ns genetic.impl-test
  (:require [clojure.test :refer :all]
            [genetic.impl :refer :all]))

(deftest crossover-test
  (testing "Performs a crossover operation."
    (is (-> (crossover [1 0 1 0] [0 1 0 1] 1) (= [[1 0 0 1] [0 1 1 0]])))
    (is (-> (crossover [1 1] [0 0] 0) (= [[1 0] [0 1]])))
    ;; Doesn't crossover at all
    (is (-> (crossover [1 1] [0 0] 1) (= [[1 1] [0 0]]))))
  (testing "Returns nil for incorrect values."
    (is (-> (crossover [1 1] [1 1 0] 5) nil?))
    (is (-> (crossover [1 0 1] [1 1 0] 3) nil?))))

(deftest mutate-test
  (testing "Mutates!"
    (is (#(or (= % [1 1]) (= % [0 0])) (mutate [1 0] 1))))
  (testing "Doesnt mutate with 0 probability"
    (is (-> (mutate [1 0 1] 0) (= [1 0 1])))))
