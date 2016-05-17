(ns genetic.impl)

(defn random-chromosome
  "Produces a random vector of bits"
  [n]
  (apply vector
         (take n (repeatedly #(rand-int 2)))))

(defn init
  "Returns a map with all the initial chromosomes. Chromosome count is n, chromosome length is l"
  [n l]
  (loop [n l]))
