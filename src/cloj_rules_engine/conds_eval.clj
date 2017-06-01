(ns cloj-rules-engine.conds-eval
  (:require [clojure.set :as clojure.set]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; FUNCTION:uuid
(defn- uuid [] "Generates random UUID" (.toString (java.util.UUID/randomUUID)))

;; FUNCTION: replace-map
;; Given an input string and a hash-map, returns a new string with all keys in map found in input replaced with the value of the key
(defn- replace-map "Returns a new string with all keys in map 'm' found in input string 's' replaced with the value of the key"
  [txt replacement-map]
  (clojure.string/replace ;; (replace txt match replacement)
    txt
    (re-pattern (apply str (interpose "|" (map #(java.util.regex.Pattern/quote %) (keys replacement-map)))))
    replacement-map))

;; FUNCTION: get-params-keys-cond-map
(defn- get-map-params-cond-map-keys
  "Gets a list of all the params / variables defined in the rules conditions (from *conds-map) => (\"#A\" \"#C\")
  and then creates a map that has these params as keys, and a list of all the rules (*conds-map keys)
  where these keys appear => {\"#A\" (:6f23fsdf234234 :b4fr2134234) ...}"
  [conds-map]
  (into {}
    (for [x (distinct (apply concat (for [[k v] conds-map] (re-seq #"\#[A-Za-z][A-Za-z0-9]*" v))))]
      {x (remove nil? (for [[k v] conds-map]
                        (when-not (nil? (re-find (re-pattern (str x " ")) v)) k)))})))

;; FUNCTION: get-keys-to-remove
(defn- get-keys-to-remove
  "Gets the map resulting from executing get-map-params-cond-map-keys, and check which params are not defined in *values-map.
  It returns a map with only the entries not defined in *values-map."
  [params-keys-cond-map values-map]
  (let [not-found-keys (apply list (clojure.set/difference (into #{} (keys params-keys-cond-map)) (into #{} (keys values-map))))]
    (if (= 0 (count not-found-keys))
      {}
      (loop [m-result         {}
             m-vars-cond-map  params-keys-cond-map]
        (if (= 0 (count m-vars-cond-map))
          m-result
          (if (some #(= (first (first m-vars-cond-map)) %) not-found-keys)
            (recur (assoc m-result (first (first m-vars-cond-map)) (second (first m-vars-cond-map)))
                   (rest m-vars-cond-map))
            (recur m-result (rest m-vars-cond-map))))))))

;; FUNCTION: get-current-conds-map
(defn- create-current-conds-map
  "Creates a conditions map with only the params present in *values-map"
  [conds-map keys-to-remove]
  (loop [m-result   conds-map
         l          (distinct (apply concat (for [[k v] keys-to-remove] v)))]
    (if (= 0 (count l))
      m-result
      (recur (dissoc m-result (first l)) (next l)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PUBLIC FUNCTIONS:

;; FUNCTION: gen-conds-map
(defn gen-conds-map "Generates a conditions map, with a random key and the clojure condition that will
                    be executed (after replacing params)"
  [rules]
  (into {}
    (for [x (keys rules)]
      {(keyword (uuid)) (str "(when " (get-in rules [x :cond]) " " x ")")})))

;; FUNCTION: eval-conditions
(defn eval-conditions "Evaluates conditions"
  [rules m-values]
  (remove nil?
    (for [[k v] rules]
      (eval
        (read-string
          (replace-map v m-values))))))

;; FUNCTION: get-current-conds-map
(defn get-current-conds-map "Returns a conditions map ready for the current facts (and removing entries of missing facts / params)"
  [conds-map values-map]
  (create-current-conds-map conds-map (get-keys-to-remove (get-map-params-cond-map-keys conds-map) values-map)))
