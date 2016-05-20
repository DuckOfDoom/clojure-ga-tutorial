(ns genetic.impl-utils-test
  (:require [clojure.test :refer :all]
            [genetic.impl :refer :all]))

(deftest random-chromosome-test
  (testing  
    "Produces random chromosome"
    (is (-> (count (random-chromosome 5)) (= 5)))))

(deftest calculate-expression-test
  (testing 
    "Calculates expression from a string"
    (is (-> (calculate-expression "50.0 + 35.0 / 10.0 * 13.0 + 2.0") (= 97.5)))))

(deftest calculate-fitness-test
  (testing 
    "Calculates fitness"
    (is (-> (calculate-fitness 17 15) (= 0.5)))
    (is (-> (calculate-fitness 32 34) (= 0.5)))  
    (is (-> (calculate-fitness 15 15) (Double/isInfinite)))))
    
