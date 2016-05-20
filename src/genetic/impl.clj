(ns genetic.impl
  (:require [clojure.pprint :refer [cl-format]]))

(defn random-chromosome
  "Produces a random sequence of bits"
  [n]
  (take n (repeatedly #(rand-int 2))))

(defn init
  "Returns a vector of maps with all the initial chromosomes and fitnesses."
  [num-chromosomes chromosome-length default-fitness]
  (repeatedly num-chromosomes #(hash-map :body (apply vector (random-chromosome chromosome-length))
                                         :fitness default-fitness)))

(load "impl_encoding")
