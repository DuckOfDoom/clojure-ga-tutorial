(ns genetic.ui
  (:require [seesaw.core :refer :all]
            [seesaw.swingx :refer [hyperlink]]
            [seesaw.border :refer :all])) 

(defn make-ui
  []
  (frame 
    :title "Genetic Algorithm Example" 
    :size [1000 :by 1000]
    :content (border-panel
               :border 5 :hgap 5 :vgap 5
               :north  (hyperlink :text "Link to example" :uri "http://www.ai-junkie.com/ga/intro/gat1.html")
               :center (canvas :id :canvas 
                               :background :black
                               :border (line-border :thickness 2 :color :black))
               :east   (slider :id :size 
                               :min 8 
                               :max 256 
                               :value 32 
                               :orientation :vertical
                               :tip "Adjust grid size")
               :south  (toolbar :floatable? false 
                                :items [(label :id :link
                                               :tip "Open cell file library in browser"
                                               :text "Cell file library"
                                               :foreground :blue
                                               :cursor :hand)
                                        :separator
                                        "Period (ms) " (spinner 
                                                         :id :period 
                                                         :model (spinner-model 250 :from 50 :to 1000 :by 25))]))))

(defn show-ui
  [frame]
  (-> frame show!))
