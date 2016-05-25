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
      (or (-> decoded nil?)
          (-> decoded (> (first (last operators))))) nil
      ;; If > 9 then it's an operator
      (-> decoded (>= 2r1010)) (operators decoded)
      ;; Else it's just a number
      :else
      ;; We're adding .0 so we dont get Clojuire ratios in result of calculating our expressions
      (str decoded ".0"))))

(defn- operator?
  "Returns whether the character is an operator"
  [c]
  (some? (some #{c} (vals operators))))

(defn- to-expression
  "Converts a vector of strings to expression that we're going to feed to compiler later.
  Removes trailing operators and joins with whitespaces.
  We're also adding a '.0' to each number so we don't get Clojure ratios in result"
  [input]
  (let [s (clojure.string/join " " input)]
    (if (-> s last str operator?)
      (-> (subs s 0 (-> (count s) (- 1))) clojure.string/trim)
      s)))

(defn decode-chromosome
  "Decodes the a vector of bits into expression string. 
  Values that do not conform to 'number-operator-number-operator-number-e.t.c' order are dropped."
  [input]
  (loop [remaining input
         ;; flag that previous character was an operator. set to 'true' because sequence starts with a number
         was-operator true   
         decoded []]
    (let [current (decode-gene (take 4 remaining))
          is-operator (operator? current)
          left (drop 4 remaining)]
      (cond 
        ;; if we can't decode next char - return what we have and clean last operator
        (or (-> current nil?)) (to-expression decoded)
        ;; if symbol conforms to our expression - recur with new values
        (-> is-operator (not= was-operator)) (recur left is-operator (conj decoded current))
        :else 
        (recur left was-operator decoded)))))

(defn add-expression
  "Adds a decoded expression to a map with :chromosome key"
  [m]
  (assoc m :expression (decode-chromosome (:chromosome m))))
