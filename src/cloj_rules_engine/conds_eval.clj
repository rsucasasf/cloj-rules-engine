(ns cloj-rules-engine.conds-eval)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; FUNCTION:uuid
(defn- uuid [] "Generates random UUID" (.toString (java.util.UUID/randomUUID)))

;; FUNCTION: replace-map
;; Given an input string and a hash-map, returns a new string with all keys in map found in input replaced with the value of the key
(defn- replace-map "Returns a new string with all keys in map 'm' found in input string 's' replaced with the value of the key"
  [s m]
  (clojure.string/replace s
              (re-pattern (apply str (interpose "|" (map #(java.util.regex.Pattern/quote %) (keys m))))) m))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PUBLIC FUNCTIONS:

;; FUNCTION: gen-conds-map
(defn gen-conds-map ""
  [rules]
  (into {}
    (for [x (keys rules)]
      {(keyword (uuid)) (str "(when " (get-in rules [x :cond]) " " x "))")})))

;; FUNCTION: eval-conditions
(defn eval-conditions ""
  [rules m-values]
  (remove nil?
    (for [[k v] rules]
      (eval (read-string (replace-map v m-values))))))
