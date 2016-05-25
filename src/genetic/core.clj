(ns genetic.core
  (:require [genetic.impl :as impl]
            [clojure.pprint :as p])
  (:gen-class
    :main main))

(defn- set-initial-values
  [args]
  (let [m (zipmap [:ps :cl :mr :cr :target :mg] args)] 
    (do 
      (def population-size (get m :ps 100))
      (when (odd? population-size) ( throw (Exception. "Population Size should be an odd number")))
      (def chromosome-length (get m :cl 30))
      (def mutation-rate (get m :mr 0.007))
      (def crossover-rate (get m :cr 0.7))
      (def target (get m :target 0))
      (def max-generations (get :mg m 10))
      (def generation 0)
      (def state (impl/initial-values population-size chromosome-length target)))))

(defn- perform-crossover
  "Performs crossover on two chromosomes"
  [c]
  (if (>= crossover-rate (rand)) 
    (let [c1 (:chromosome (first c))
          c2 (:chromosome (second c))] 
      (impl/crossover c1 c2 (rand-int chromosome-length))) 
    c))

(defn- perform-mutation
  "Performs mutation on two chromosomes"
  [c]
  (mapv #(impl/mutate % mutation-rate) c))

(defn- recalculate-fitness
  "Takes chromosome and produces a map with fitness and expression"
  [c]
  (let [expr (impl/decode-chromosome c)
        fitness (impl/calculate-fitness (impl/calculate-expression expr) target)]
    (assoc {} :chromosome c :expression expr :fitness fitness)))

(defn- step
  "Perform a single generation step of our algorithm on defined values."
  []
  (loop [old-generation state
         new-generation []]
    ;;    (p/pprint old-generation)
    (if (empty? old-generation) 
      (def state new-generation)
      ;; selection
      (let [selected (impl/select old-generation 2)
            c1 (-> selected first :chromosome)
            c2 (-> selected second :chromosome)
            new-c (-> selected 
                      perform-crossover
                      perform-mutation)]
        (recur (filterv #(and (not= (:chromosome %) c1) (not= (:chromosome %) c2)) old-generation)
               (conj new-generation (recalculate-fitness new-c)))))))

(defn- find-best-solution
  "Find the best solution in our current state"
  []
  (apply max-key :fitness state))

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
        (loop 
          []
          (step)
          (p/pprint state)
          (def generation (inc generation))
          (let [bs (find-best-solution)]
            (println "Generation #" generation "\n" "Best Solution: " bs)
            (when (< generation max-generations)
              (recur))))
        ))))
