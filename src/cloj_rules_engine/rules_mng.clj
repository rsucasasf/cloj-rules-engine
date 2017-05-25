(ns cloj-rules-engine.rules-mng
  "Rules-engine functions"
  (:require [cloj-rules-engine.conds-eval :as conds-eval]
            [cloj-rules-engine.logs :as logs]
            [cloj-rules-engine.common :as common]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; rules map
(def ^:private *rules-map (atom {}))

;; conds-map: created from *rules-map
(def ^:private *conds-map (atom {}))

;; values-map
(def ^:private *values-map (atom {}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PUBLIC FUNCTIONS:

;; FUNCTION: initialize
;; 'rules-file': rules file
(defn initialize "Intializes rules-map"
  [rules-file]
  (do
    (reset! *rules-map (common/read-content rules-file))
    (reset! *conds-map (conds-eval/gen-conds-map @*rules-map))
    true))

;; FUNCTION: update-map-values
(defn update-map-values "Updates values-map content"
  [m]
  (reset! *values-map m))

;; FUNCTION: get-rules-actions
(defn get-rules-actions "Returns an ArrayList of Strings, where each of the items is an action identifier"
  []
  (try
    (java.util.ArrayList.
      (remove nil?
        (distinct
          (apply concat (for [x (conds-eval/eval-conditions @*conds-map @*values-map)] (get-in @*rules-map [x :actions]))))))
    (catch Exception e
      (do (logs/log-exception e) nil))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;(initialize "rules.clj")

;(update-map-values {"#A" "14" "#B" "13" "#C" "13"})

;(deref *rules-map)
;(deref *values-map)
;(deref *conds-map)

;(get-rules-actions)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; 1. take '*conds-map' and delete all entries with params not included in '*values-map'


;; get all keys included in '*conds-map'
;(distinct (apply concat (for [[k v] @*conds-map] (re-seq #"\#[A-Za-z][A-Za-z0-9]*" v))))

;; create map [params keys] <-> list of ['*conds-map' keys]
;(into {}
;  (for [x (distinct (apply concat (for [[k v] @*conds-map] (re-seq #"\#[A-Za-z][A-Za-z0-9]*" v))))]
;    {x (remove nil? (for [[k v] @*conds-map]
;                      (when-not (nil? (re-find (re-pattern (str x " ")) v)) k)))}))

;(def params-keys-cond-map
;  (into {}
;    (for [x (distinct (apply concat (for [[k v] @*conds-map] (re-seq #"\#[A-Za-z][A-Za-z0-9]*" v))))]
;      {x (remove nil? (for [[k v] @*conds-map]
;                        (when-not (nil? (re-find (re-pattern (str x " ")) v)) k)))})))
;
;(for [[k v] params-keys-cond-map] v)
;(keys params-keys;-cond-map)

;; get all vars-cond-map keys not present in *values-map
;(update-map-values {"#A" "14"}) ; "#A" "14" "#B" "13" "#C" "13"

;(keys @*values-ma;p)
;
;(second params-keys-cond-map)

;(def keys-to-remove
;  (let [not-found-keys (apply list (clojure.set/difference (into #{} (keys params-keys-cond-map)) (into #{} (keys @*values-map))))]
;    (if (= 0 (count not-found-keys))
;      params-keys-cond-map
;      (loop [m-result         {}
;             m-vars-cond-map  params-keys-cond-map]
;        (if (= 0 (count m-vars-cond-map))
;          m-result
;          (if (some #(= (first (first m-vars-cond-map)) %) not-found-keys)
;            (recur (assoc m-result (first (first m-vars-cond-map)) (second (first m-vars-cond-map)))
;                   (rest m-vars-cond-map))
;            (recur m-result (rest m-vars-cond-map)))))))
;)
;
;
;(distinct (apply concat (for [[k v] keys-to-remove] v)))
;
;
;
;
;(loop [m-result   @*conds-map
;       l          (distinct (apply concat (for [[k v] keys-to-remove] v)))]
;  (if (=; 0 (count l))
;    m-result
;    (recur (dissoc m-result (first l)) (next l))))



;; FUNCTION: get-params-keys-cond-map
(defn- get-params-keys-cond-map ""
  []
  (into {}
    (for [x (distinct (apply concat (for [[k v] @*conds-map] (re-seq #"\#[A-Za-z][A-Za-z0-9]*" v))))]
      {x (remove nil? (for [[k v] @*conds-map]
                        (when-not (nil? (re-find (re-pattern (str x " ")) v)) k)))})))

;; FUNCTION: get-keys-to-remove
(defn- get-keys-to-remove ""
  [params-keys-cond-map]
  (let [not-found-keys (apply list (clojure.set/difference (into #{} (keys params-keys-cond-map)) (into #{} (keys @*values-map))))]
    (if (= 0 (count not-found-keys))
      params-keys-cond-map
      (loop [m-result         {}
             m-vars-cond-map  params-keys-cond-map]
        (if (= 0 (count m-vars-cond-map))
          m-result
          (if (some #(= (first (first m-vars-cond-map)) %) not-found-keys)
            (recur (assoc m-result (first (first m-vars-cond-map)) (second (first m-vars-cond-map)))
                   (rest m-vars-cond-map))
            (recur m-result (rest m-vars-cond-map))))))))


;; FUNCTION: get-current-conds-map
(defn get-current-conds-map ""
  [keys-to-remove]
  (loop [m-result   @*conds-map
         l          (distinct (apply concat (for [[k v] keys-to-remove] v)))]
    (if (= 0 (count l))
      m-result
      (recur (dissoc m-result (first l)) (next l)))))
