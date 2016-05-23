(ns genetic.impl
  (:require [clojure.pprint :refer [cl-format]]
            [infix.macros :refer [from-string]]))

(defn crossover 
  "Perform a crossover operation on two parent chromosomes on crossover-point.
  Returns a vector of 2 children."
  [parent1 parent2 crossover-point]
  (when (and (= (count parent1) (count parent2))
             (<= (inc crossover-point) (count parent1)))
    (loop [i 0
           p1 parent1 
           p2 parent2 
           c1 [] 
           c2 []]
      (let [[p1g & p1rest] p1
            [p2g & p2rest] p2]
        (cond (>= i (count parent1)) (vector c1 c2)
              (<= i crossover-point) (recur (inc i) p1rest p2rest (conj c1 p1g) (conj c2 p2g))
              :else                  (recur (inc i) p1rest p2rest (conj c1 p2g) (conj c2 p1g)))))))

(defn mutate
  "Mutates a random gene with some probability."
  [chromosome prob]
  (if (> prob (rand))
    (let [i (rand-int (count chromosome))]
      (assoc chromosome i (#(if (= % 1) 0 1) (nth chromosome i))))
    chromosome))

(load "impl_encoding")
(load "impl_math")
