(ns genetic.core
  (:require [genetic.impl :as impl])
  (:gen-class
    :main main))

;;(def population-size 0)
;;(def chromosome-length 0)
;;(def mutation-rate 0.007 )
;;(def crossover-rate 0.7)
;;(def max-generations 0)
;;(def target 42)
;;(def generation (atom 0))
(def state [])

(defn- step 
  "Perform one full step of our algorithm on data."
  []
  (when (not (empty state)))
  (loop [remaining @state
         new-state []]
    )
  )

(defn- set-initial-values
  [args]
  (let [m (zipmap [:ps :cl :mr :cr :target :mg] args)] 
    (do 
      (def population-size (get m :ps 100))
      (def chromosome-length (get m :cl 30))
      (def mutation-rate (get m :mr 0.007))
      (def crossover-rate (get m :cr 0.7))
      (def target (get m :target 0))
      (def max-generations (get :mg m 0))
      (def state (impl/initial-values population-size chromosome-length target)))))

(defn -main
  [& args]
  (let [a *command-line-args*]
    (if (= (first a) "help")
      (println "Usage:\n"
               "  lein run <population size> <chromosome length> <mutation rate> <crossover rate> <target value> [max generations]")
      (do
        (set-initial-values *command-line-args*)
        (println 
                 "Initializing with values:\n"
                 "  Population Size:" population-size "\n"
                 "  Chromosome Length:" chromosome-length "\n"
                 "  Mutation Rate:" mutation-rate "\n"
                 "  Crossover Rate:" crossover-rate "\n"
                 "  Max Generations:" max-generations (if (<= max-generations 0) " (Unlimited) " "") "\n"
                 "  Target Value:" target)
;;        (clojure.pprint/pprint state)
        ))))
