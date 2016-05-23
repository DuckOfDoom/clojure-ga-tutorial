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
    (is (-> (mutate [1 0] 1) (= [0 1]))))
  (testing "Doesnt mutate with 0 probability"
    (is (-> (mutate [1 0 1] 0) (= [1 0 1])))))

(deftest select-test
  (testing "Selects single"
    (is (-> (:name (select [{:name "derp" :fitness 1}
                            {:name "herp" :fitness 0}])) (= "derp")))
    (is (-> (:name (select [{:name "herp" :fitness 0}])) (= "herp")))
    (is (let [name (:name (select [{:name "derp" :fitness 1} {:name "herp" :fitness 0} {:name "whoa" :fitness 3}]))]
          (or (= name "derp") (= name "whoa")))))
  (testing "Selects multiple"
    (is (-> (select [{:chromosome [1 0 1] :fitness 1} 
                     {:chromosome [1 1 1] :fitness 0}
                     {:chromosome [0 1 0]  :fitness 3}] 2) (= "derp")))))
