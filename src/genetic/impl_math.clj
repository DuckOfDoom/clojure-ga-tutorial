(in-ns 'genetic.impl)

(defn random-chromosome
  "Produces a random sequence of bits"
  [n]
  (take n (repeatedly #(rand-int 2))))

(defn calculate-fitness
  "Calculate fitness for our chromosome"
  [value target-value]
  ;; Let's say that chromosomes that give us < 1 answer are not fit at all
  (if (and (>= 0 value) (>= 0 target-value))
    (/ 1.0 (Math/abs (- value target-value)))
    0))

(defn calculate-expression
  "Calculate the result of our expression in string form.
  Converts infix to valid Clojure code."
  [infix-expresson]
  (let [expr (try (from-string infix-expresson)
                  (catch Exception e (str "Caught Exception: " (.getMessage e)) nil))]
    (if (-> expr nil?)
      Double/POSITIVE_INFINITY
      (expr))))

