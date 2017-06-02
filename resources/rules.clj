;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; RULES DEFINITION FILE:
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
{
  ;; RULE_1
  :RULE_1 {:cond "(and (< #A 10) (> #B 50))"
           :actions ["action-A"]
           :desc "Rule description: 'launch' action-A if 'a' is lower than 10 and if 'b' is greater than 50"}
  ;; RULE_2
  :RULE_2 {:cond "(> #A 10)"
           :actions ["action-B" "action-C"]
           :desc "Rule description: 'launch' action-B and action-C if ..."}
  ;; RULE_3
  :RULE_3 {:cond "(> #C 10)"
           :actions ["action-D"]
           :desc "Rule description: 'launch' action-D if ..."}
  ;; RULE_4
  :RULE_4 {:cond "(< #B 50)"
           :actions ["action-D" "action-B"]
           :desc "Rule description: 'launch' action-D if ..."}
  ;; RULE_5
  :RULE_5 {:cond "(= (str #D) (str 50))"
           :actions ["action-E"]
           :desc "Rule description: 'launch' action-E if ..."}
  ;; RULE_6
  :RULE_6 {:cond "(= #D \"goldenaxe\")"
           :actions ["action-F"]
           :desc "Rule description: 'launch' action-F if ..."}
  ;; RULE_7
  :RULE_7 {:cond "(= #D 'goldenaxe2')"
           :actions ["action-G"]
           :desc "Rule description: 'launch' action-G if ..."}
}
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
