(ns genetic.core
  (:require [genetic.ui :as ui])
  (:gen-class
    :main main))

(defn start 
  []
  (-> (ui/make-ui) ui/show-ui)) 

(defn -main
  [& args]
  (start))
