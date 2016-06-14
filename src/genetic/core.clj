(ns genetic.core
  (:require [genetic.impl :as impl]
            [clojure.pprint :as p])
  (:gen-class
    :main main))

(def population-size 100)
(def chromosome-length 100)
(def mutation-rate 0.007)
(def crossover-rate 0.7)
(def target 0)
(def max-generations 0)

(defn- to-map
  [c]
  (assoc {} :chromosome c :expression "UNKNOWN" :fitness "UNKNOWN"))

(defn- set-initial-values
  [args]
  (let [m 
        (cond (<= 1 (count args) 6) (zipmap [:target-value :chromosome-length :population-size :crossover-rate :mutation-rate :max-generations] (map read-string args))
              :else (throw (Exception. "Invalid args count. See 'help' for available options."))) ]
    (do 
      (def population-size (get m :population-size population-size))
      (when (odd? population-size) (throw (Exception. "Population Size should be an odd number")))
      (def chromosome-length (get m :chromosome-length chromosome-length))
      (def mutation-rate (get m :mutation-rate mutation-rate))
      (def crossover-rate (get m :crossover-rate crossover-rate))
      (def target (get m :target-value target))
      (def max-generations (get m :max-generations max-generations))
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
            [c1 c2] selected
            new-chromosomes (-> selected 
                                perform-crossover
                                perform-mutation)]
        (recur (impl/remove-parents old-generation c1 c2)
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
    (if (or (empty? a) (= (first a) "help"))
      (println "Usage (using leiningen):\n"
               "  lein run <target value> [chromosome length] [population size] [crossover rate] [mutation rate] [max generations]")      
      (do
        ;;      (clear-dump)
        (set-initial-values a)
        (println 
          "Initialized with values:\n"
          "  Target Value:" target "\n"
          "  Chromosome Length:" chromosome-length "\n"
          "  Population Size:" population-size "\n"
          "  Crossover Rate:" crossover-rate "\n"
          "  Mutation Rate:" mutation-rate "\n"
          "  Max Generations:" max-generations (if (<= max-generations 0) " (Unlimited) " "") "\n"
          "Press Enter to continue...")
        (read-line)
        (loop 
          []
          ;;        (dump-state)
          (step)
;;          (println (count state))
          (def generation (inc generation))
          (let [bs (find-best-solution)]
            (cond
              (or 
                ;; Max Generations reached if max-generations stated
                (and (> max-generations 0) (>= generation max-generations))
                ;; Found solution with infinite fitness - perfect match
                (>= (:fitness bs) Double/POSITIVE_INFINITY))
              (println 
                (format "Algorithm finished on %d-th generation." generation) "\n"
                "Best found solution is: " (:expression bs) " = " (impl/calculate-expression (:expression bs)) "\n"
                "With fitness: " (:fitness bs))
              :else 
              (do 
                (println "Generation #" generation "\n"
                         "Best Solution: " (:fitness bs) "=> '" (:expression bs) "' \n" 
                         "Average Fitness: " (impl/calculate-average-fitness state))
                (recur))
              )))
        ))))
