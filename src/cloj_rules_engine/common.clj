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
;; PUBLIC FUNCTIONS:

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
