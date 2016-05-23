(in-ns 'genetic.impl)

(defn random-chromosome
  "Produces a random sequence of bits"
  [n]
  (take n (repeatedly #(rand-int 2))))

(defn initial-values
  "Returns a vector of maps with all the initial chromosomes and fitnesses."
  [num-chromosomes chromosome-length default-fitness]
  (repeatedly num-chromosomes #(hash-map :body (apply vector (random-chromosome chromosome-length))
                                         :fitness default-fitness)))

(defn calculate-expression
  "Calculate the result of our expression in string form.
  Converts infix to valid Clojure code."
  [infix-expresson]
  ((from-string infix-expresson)))

(defn calculate-fitness
  "Calculate fitness for our chromosome. 
  Can return Infinity if the value is a perfect fit"
  [value target-value]
  (/ 1.0 (Math/abs (- target-value value))))
