(ns genetic.impl
  (:require [clojure.pprint :refer [cl-format]]
            [infix.macros :refer [from-string]]))

(load "impl_encoding")
(load "impl_math")

(defn initial-values
  "Returns a vector of maps with all the initial chromosomes and fitnesses."
  [num-chromosomes chromosome-length target-value]
  (repeatedly num-chromosomes #(let [chromosome (apply vector (random-chromosome chromosome-length))
                                     fitness (calculate-fitness (-> chromosome decode-chromosome calculate-expression) target-value)]
                                 (hash-map :chromosome chromosome
                                           :expression (decode-chromosome chromosome)
                                           :fitness fitness))))
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
  [chromosome mutation-rate]
  (apply vector (map #(if (< (rand) mutation-rate) (if (= % 1) 0 1) (identity %)) chromosome)))

(defn select
  "Selects the most fit chromosome using roulette wheel selection.
  Accepts a vector of maps with :fitness keys, 
  returns a vector of maps or a single map."
  ([chromosomes]
   (let [weights (apply vector (map #(:fitness %) chromosomes))
         sum-weights (reduce + weights)
         value (rand sum-weights)]
     (loop [i 0
            sum (first weights)]
       (if (<= value sum) (get chromosomes i)
         (recur (inc i) (+ sum (get weights (inc i))))))))
  ([chromosomes n]
   (loop [i 0 
          current chromosomes
          selected []]
     (if (>= i n) 
       selected
       (let [x (select current)
             xs (filterv #(not= (:chromosome x) (:chromosome %)) current)]
         (recur (inc i) xs (conj selected x)))))))
