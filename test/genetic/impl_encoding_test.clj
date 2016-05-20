(ns genetic.impl-encoding-test
  (:require [clojure.test :refer :all]
            [genetic.impl :refer :all]))

(deftest base-conversion
  (testing "Conversion from base-10 to string in base-2"
    (is (= "1011" (binarify 11)))) )

(deftest int-to-bits-test
  (testing "Converts integer to vector of bits"
    (is (-> (int-to-bits 14) (= [1 1 1 0])))))

(deftest bits-to-int-test
  (testing "Converts vector of bits to int"
    (is (-> (bits-to-int [1 1 1 0]) (= 14)))) )

(deftest decode-gene-test
  "Trying to decode a single gene"
  (testing "Decodes gene of 4 bits into a number."
    (is (-> (decode-gene [0 1 0 1]) (= "5.0")))) 
  (testing "Decodes gene of 4 bits into an operator sign."
    (is (-> (decode-gene [1 1 0 1]) (= "*")))) 
  (testing "Returns nil if not enough bits"
    (is (-> (decode-gene [0 1 1]) nil?))
    (is (-> (decode-gene '()) nil?)))
  (is (-> (decode-gene []) nil?)) 
  (testing "Returns nil if number is too large"
    (is (-> (decode-gene [1 1 1 1]) nil?)))) 

(deftest decode-chromosome-test
  "Trying to decode the whole chromosome"
  (testing "Decoding a single gene"
    (is (-> (decode-chromosome [0 1 0 1]) (= "5.0")))
    (is (-> (decode-chromosome [0 1 0 1 0 1]) (= "5.0"))))
  (testing "Decoding multiple genes"
    (is (-> (decode-chromosome [0 1 0 1  1 0 1 0  0 0 1 0]) (= "5.0 + 2.0"))))
  (testing "Sequence shouldn't end with operator" 
    (is (-> (decode-chromosome [0 1 0 1  1 0 1 0  0 1]) (= "5.0"))))) 
  (testing "Multiple operators or multiple numbers should be dropped"
    (is (-> (decode-chromosome [0 1 0 1  0 1 0 1  1 0 1 0 1 0 1 0  0 0 1 0]) (= "5.0 + 2.0"))))
