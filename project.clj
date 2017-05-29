(defproject cloj-rules-engine "0.1.1"
  :description "Rules engine written in Clojure"
  :url "https://github.com/rsucasasf/cloj-rules-engine"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]         ; Eclipse Public License - Version 1.0
                 [org.clojure/tools.logging "0.3.1"]   ; Eclipse Public License - Version 1.0  https://github.com/clojure/tools.logging
                 [log4j/log4j "1.2.17"                 ; Apache License, Version 2.0           http://logging.apache.org/log4j/1.2/
                  :exclusions [javax.mail/mail
                               javax.jms/jms
                               com.sun.jdmk/jmxtools
                               com.sun.jmx/jmxri]]
                 [proto-repl "0.3.1"]]                  ; MIT 
  :target-path "target/%s"
  :profiles {
    :uberjar {:aot :all}
    :dev {:resource-paths ["resources"]
    ;; jvm configuration
    :jvm-opts ["-Xmx256M"]}
  ;; maven central requirements
  :scm {:url "git@github.com/rsucasasf/cloj-rules-engine.git"}
  :pom-addition [:developers [:developer
                              [:name ""]
                              [:url ""]
                              [:email ""]
                              [:timezone "-5"]]]})
