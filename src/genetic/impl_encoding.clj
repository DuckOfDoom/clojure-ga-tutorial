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
  "Decodes a vector of 4 bits into something we can use. Returns nil if can't be decoded."
  [bits]
  (let [decoded (bits-to-int bits)]
    (if (or 
          (-> (count bits) (not= 4))
          (-> decoded (> 2r1101)))
      nil
      decoded)))

(defn decode-chromosome
  "Decodes a string of bits into an string containing expression. 
  Bits should conform to order 'symbol-operator-symbol-e.t.c' or they will be dropped"
  [bits]
  (loop [remaining bits
         converted []]
    (if (-> (count remaining) (<= 4))
      ()))
  )

