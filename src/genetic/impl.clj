(ns genetic.impl
  (:require [clojure.pprint :refer [cl-format]]))

(defn random-chromosome
  "Produces a random vector of bits"
  [n]
  (apply vector
         (take n (repeatedly #(rand-int 2)))))

(defn init
  "Returns a map with all the initial chromosomes. N is chomosome count, L is chromosome length"
  [n l]
  (loop [n l]))

(load "impl_encoding")
