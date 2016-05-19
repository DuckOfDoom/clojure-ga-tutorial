(defproject genetic "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main genetic.core
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [seesaw "1.4.5"]
  ;; Plugin for humate test output: https://github.com/pjstadig/humane-test-output
                 [pjstadig/humane-test-output "0.8.0"]]
  ;; Activates the above mentioned plugin
  :injections [(require 'pjstadig.humane-test-output)
               (pjstadig.humane-test-output/activate!)
               (println "Humane Output Activated!")] )
