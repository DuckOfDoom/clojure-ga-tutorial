(ns genetic.core
  (:require [genetic.impl :as impl]
            [clojure.pprint :as p])
  (:gen-class
    :main main))

(defn- to-map
  [c]
  (assoc {} :chromosome c :expression "UNKNOWN" :fitness "UNKNOWN"))

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
      (def max-generations (get :mg m 100))
      (def generation 0)
      (def state (impl/initial-values population-size chromosome-length target)))))

(defn- perform-crossover
  "Performs crossover on two chromosomes.
  Takes and returns a vector of maps."
  [c]
  (if (>= crossover-rate (rand)) 
    (let [c1 (:chromosome (first c))
          c2 (:chromosome (second c))] 
      (mapv to-map (impl/crossover c1 c2 (rand-int chromosome-length)))) 
    c))

(defn- perform-mutation
  "Performs mutation on two chromosomes
  Takes and return a vector of maps"
  [c]
  (mapv #(to-map (impl/mutate (:chromosome %) mutation-rate)) c))

(defn- calculate-fitness
  "Takes a map with chromosome and fills fithess and expression"
  [c-map]
  (let [c (:chromosome c-map)
        expr (impl/decode-chromosome c)
        fitness (impl/calculate-fitness (impl/calculate-expression expr) target)]
    (assoc c-map :chromosome c :expression expr :fitness fitness)))

(defn- step
  "Perform a single generation step of our algorithm on defined values."
  []
  (loop [old-generation state
         new-generation []]
    (if (empty? old-generation) 
      (def state new-generation)
      (let [selected (impl/select old-generation 2)
            c1 (-> selected first :chromosome)
            c2 (-> selected second :chromosome)
            new-chromosomes (-> selected 
                            perform-crossover
                            perform-mutation)]
        (recur (filterv #(and (not= (:chromosome %) c1) (not= (:chromosome %) c2)) old-generation)
               (into new-generation (mapv calculate-fitness new-chromosomes)))))))

(defn- find-best-solution
  "Find the best solution in our current state"
  []
  (apply max-key :fitness state))

(defn- dump-state
  []
  (let [s (slurp "state.txt")]
    (spit "state.txt" (str s "\n Gen #" generation "\n" (with-out-str (p/pprint state)))) ))

(defn- clear-dump
  []
  (spit "state.txt" ""))

(defn -main
  [& args]
  (let [a *command-line-args*]
    (if (= (first a) "help")
      (println "Usage:\n"
               "  lein run <population size> <chromosome length> <mutation rate> <crossover rate> <target value> [max generations]"
               "  lein run <target value>")) 
    (do
;;      (clear-dump)
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
;;        (dump-state)
        (step)
        (def generation (inc generation))
        (let [bs (find-best-solution)]
          (println "Generation #" generation "\n" "Best Solution: " bs)
          (when (< generation max-generations)
            (recur))))
      )))
