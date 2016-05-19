(in-ns 'genetic.impl)

(def operators
  "Returns map of operator encodings. Numbers are encoded as common binary numbers while operators occupy range from 10 to 15 in binary."
  {2r1010 "+"
   2r1011 "-"
   2r1100 "/"
   2r1101 "*"})

(defn binarify
  "Represents a base-10 number in base-2"
  [bin]
  (Integer/toString bin 2))

(defn int-to-bits
  "Converts integer to vector of bits"
  [i]
  (read-string (str "[" (clojure.string/join " " (binarify i)) "]")))

(defn bits-to-int
  "Converts vector of bits to integer."
  [v]
  (let [bits (clojure.string/join v)]
    (read-string (str "2r" bits))))

(defn decode-gene
  "Decodes a sequence of 4 bits into something we can use. Returns nil if can't be decoded."
  [bits]
  (let [decoded (if (-> (count bits) (not= 4))
                  nil
                  (bits-to-int bits))]
    (cond 
      ;; Can't decode something that is greater than our table
      (or (-> decoded nil?) (-> (decoded (> (first (last operators)))))) nil
      ;; If > 9 then it's an operator
      (-> (decoded (>= 2r1010))) (operators decoded)
      ;; Else it's just a number
      :else
      (str decoded))))

(defn operator?
  "Returns whether the character is an operator"
  [i]
  (and (>= i 2r1010) (<= i 2r1101)))

(defn decode-chromosome
  "Decodes the a vector of bits into expression string. 
  Values that do not conform to 'number-operator-number-operator-number-e.t.c' order are dropped."
  [input]
  (loop [remaining input
         decoded []
          ;; flag that previous character was an operator. set to 'true' because sequence starts with a number
         was-operator true]  
    (let [current (decode-gene (take 4 input))
          is-operator (operator? current)]
      (cond 
        ;; if we can't decode next char - returning what we have
        (nil? current) decoded
        ;; if symbol conforms to our expression - recur with new values
        (not= is-operator was-operator) (recur (drop 4 input) (conj decoded current) is-operator) 
        :else
        (recur (drop 4 input) decoded was-operator)))))
