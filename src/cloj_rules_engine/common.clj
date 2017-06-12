(ns cloj-rules-engine.common
  (:require [cloj-rules-engine.logs :as logs]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; FUNCTION: read-content-from-abs-path
(defn- read-content-from-abs-path "read file content from absolute path"
  [path-file]
  (try (read-string (slurp path-file))
    (catch Exception e
      (do (logs/log-exception e) nil))))

;; FUNCTION: get-resource
(defn- get-resource "get file or nil if error"
  [path]
  (try
    (when path
      (-> (Thread/currentThread) .getContextClassLoader (.getResource path)))
    (catch Exception e
      (do (logs/log-exception e) nil))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; FUNCTION: read-content
;; Examples: (read-content "rules.clj") (read-content "C://ABSOLUTE_PATH/rules.clj")
(defn read-content "read file content"
  [path]
  (let [fpath (get-resource path)]
    (if-not (nil? fpath)
      (read-string (slurp fpath))
      (read-content-from-abs-path path))))

;; CLASS OBJECTS FUNCTIONS (JAVA INTEROP)

;; FUNCTION:set-field
;; function to safely set the field content
(defn set-field "function to safely set the field content"
  [this key value]
  (swap! (.state this) into {key value}))

;; FUNCTION:get-field
;; function to safely get the field content
(defn get-field "function to safely get the field content"
  [this key]
  (@(.state this) key))

;; MAPS

;; FUNCTION: add-new-attr-to-map
;; Example: (add-new-attr-to-map {:id1 {:val 1} :id2 {:val 3}} :xy "wo")
(defn add-new-attr-to-map "Creates a copy an initial map and adds a new attribute to all elements of this copy"
  [initial-map new-attr attr-value]
  (loop [m-res {}
         m-ini initial-map]
    (if (= 0 (count m-ini))
      m-res
      (recur
        (assoc m-res (first (first m-ini)) (assoc (second (first m-ini)) new-attr attr-value))
        (rest m-ini)))))

;; FUNCTION: update-value-in-map
;; Example: (update-value-in-map (gen-initial-map-with-fired-to-false {:id1 {:val 1} :id2 {:val 3}}) :id1 :fired true)
(defn update-value-in-map "Creates a copy an initial map and updates a value"
  [initial-map key-to-update sub-key-to-update new-value]
  (loop [m-res {}
         m-ini initial-map]
    (if (= 0 (count m-ini))
      m-res
      (if (= (first (first m-ini)) key-to-update)
        (recur
          (assoc m-res (first (first m-ini)) (assoc (second (first m-ini)) sub-key-to-update new-value))
          (rest m-ini))
        (recur
          (assoc m-res (first (first m-ini)) (second (first m-ini)))
          (rest m-ini))))))

;; FUNCTION: parse-m
(defn parse-m "Transform map -m- numeric values into string values"
  [m]
  (into {}
    (for [[k v] m]
      (if (number? v)
        {k (str v)}
        {k v}))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; FUNCTION:
;; returns true / false for chances: Example 50% = (chances1 0.5)
(defn- result-chances "Gets the result of chances 'n', where 0 <= n <= 1"
  [n]
  (if (< (rand 1) n) true false))

;; FUNCTION:get-actions-eval
(defn get-action-chances [m]
  (loop [mtmp m]
    (if (= 0 (count mtmp))
      nil
      (if (result-chances (val (first (first mtmp))))
        (key (first (first mtmp)))
        (recur (rest mtmp))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; MULTIMETHOD FUNCTION: valid?
;; Multimethod used to validate maps contents.
(defmulti valid? (fn [[k v]] (class v)))

;; FUNCTION: is-valid?
(defn- is-valid? "Checks if 'f' returns true or false"
  [value f]
  (let [res-valid? (f)]
    (do
      (logs/log-debug ">> value= " value ", valid?=" res-valid?)
      (when-not res-valid?
        (logs/log-warning "[!] value= " value ", valid?=" res-valid?))
      res-valid?)))

;; check java.lang.Long values: not allowed
(defmethod valid? java.lang.Long [[_ value]] (do (logs/log-warning "[!] value is Numeric: " value) false))

;; check java.lang.Double values: not allowed
(defmethod valid? java.lang.Double [[_ value]] (do (logs/log-warning "[!] value is Numeric: " value) false))

;; check string values: not empty allowed
(defmethod valid? java.lang.String [[_ value]] (is-valid? value #(not (empty? value))))

;; check string values: not empty allowed
(defmethod valid? clojure.lang.PersistentVector [[_ value]] true)

;;
(defmethod valid? nil [_] (do (logs/log-warning "[!] value is nil") false))

;;  not valid for rest cases
(defmethod valid? :default [_] (do (logs/log-debug "value: -default- false") false))
