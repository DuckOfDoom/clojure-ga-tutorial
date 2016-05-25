(ns genetic.impl-math-test
  (:require [clojure.test :refer :all]
            [genetic.impl :refer :all]))

(deftest random-chromosome-test
  (testing  
    "Produces random chromosome"
    (is (-> (count (random-chromosome 5)) (= 5)))))

(deftest calculate-expression-test
  (testing 
    "Calculates expression from a string."
    (is (-> (calculate-expression "50.0 + 35.0 / 10.0 * 13.0 + 2.0") (= 97.5))))
  (testing 
    "Returns Infinity when can't parse expression."
    (is (-> (calculate-expression "") Double/isInfinite)))
  (testing 
    "Returns Infinity when Division By Zero exception occurs."
    (is (-> (calculate-expression "15.0 / 0.0") Double/isInfinite))))

(deftest calculate-fitness-test
  (testing 
    "Calculates fitness"
    (is (-> (calculate-fitness 17 15) (= 0.5)))
    (is (-> (calculate-fitness 32 34) (= 0.5)))
    (is (-> (calculate-fitness 0 12) (= (/ 1 12.0))))
    (is (-> (calculate-fitness -3 12) (= (/ 1 15.0))))
            ))
