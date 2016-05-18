(ns genetic.impl-encoding-test
  (:require [clojure.test :refer :all]
            [genetic.impl :refer :all]))

(deftest base-conversion
  (testing "Conversion from base-10 to string in base-2"
    (is (= "1011" (binarify 11)))) )

(deftest int-to-bits-test
  (testing "Converts integer to vector of bits"
    (is (-> (int-to-bits 14) (= [1 1 1 0]))))
  (testing "Returns nil when fails."
    (is (-> (int-to-bits ) (= nil)))))
