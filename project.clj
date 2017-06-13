(defproject cloj-rules-engine "0.1.2-SNAPSHOT"
  :description "a very simple rules engine written in Clojure"
  :url "https://github.com/rsucasasf/cloj-rules-engine"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]            ; Eclipse Public License - Version 1.0  https://clojure.org/
                 [org.clojure/tools.logging "0.3.1"]      ; Eclipse Public License - Version 1.0  https://github.com/clojure/tools.logging
                 [log4j/log4j "1.2.17"                    ; Apache License, Version 2.0           http://logging.apache.org/log4j/1.2/
                  :exclusions [javax.mail/mail
                               javax.jms/jms
                               com.sun.jdmk/jmxtools
                               com.sun.jmx/jmxri]]
                 [org.clojure/math.numeric-tower "0.0.4"] ; Eclipse Public License - Version 1.0  https://github.com/clojure/math.numeric-tower/
                 [org.clojure/data.json "0.2.6"]          ; Eclipse Public License - Version 1.0  https://github.com/clojure/data.json
                 [proto-repl "0.3.1"]]                    ; MIT                                   https://github.com/jasongilman/proto-repl
  :target-path "target/%s"
  :plugins [[camechis/deploy-uberjar "0.3.0"]
            [lein-cloverage "1.0.7-SNAPSHOT"]]            ; MIT                                   https://github.com/codecov/example-clojure
  :profiles {
    :uberjar {:aot :all}
    :dev {:resource-paths ["resources"]
    ;; jvm configuration
    :jvm-opts ["-Xmx256M"]}

  :lein-release {:deploy-via :clojars :build-uberjar true}


  ;; deploy-repositories
  ;:deploy-repositories {"releases" {:url "https://oss.sonatype.org/service/local/staging/deploy/maven2/" :creds :gpg}
  ;                      "snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots/" :creds :gpg}}
  ;; maven central requirements
  :scm {:url "git@github.com/rsucasasf/cloj-rules-engine.git"}
  :pom-addition [:developers [:developer
                              [:name "Roi Sucasas"]
                              [:url "https://github.com/rsucasasf"]
                              [:email "rsucasasf@hotmail.com"]
                              [:timezone "1"]]]})
