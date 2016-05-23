(ns genetic.core
  (:require [genetic.impl :as impl])
  (:gen-class
    :main main))

(def population-size (atom 0))
(def chromosome-length (atom 0))
(def mutation-rate (atom 0))
(def crossover-rate (atom 0))
(def iteration-count (atom 0))
(def state (atom []))

(defn- step 
  "Perform one full step of our algorithm on data."
  []
  (when (not (empty state)))
  (loop [remaining @state
         new-state []]
    )
  )

(defn -main
  [& args]
  (loop [x 0]
    (do 
      (println x)
      (recur (inc x)))))
