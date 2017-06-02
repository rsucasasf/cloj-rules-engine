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
           :desc "Rule description: 'launch' action-B and action-C if #A greater than 10"}
  ;; RULE_3
  :RULE_3 {:cond "(> #C 10)"
           :actions ["action-D"]
           :desc "Rule description: 'launch' action-D if #C greater than 10"}
  ;; RULE_4
  :RULE_4 {:cond "(< #B 50)"
           :actions ["action-D" "action-B"]
           :desc "Rule description: 'launch' action-D if #B is less than 50"}
  ;; RULE_5
  :RULE_5 {:cond "(= (str #D) (str 50))"
           :actions ["action-E"]
           :desc "Rule description: 'launch' action-E if if #D string is equal to '50'"}
  ;; RULE_6
  :RULE_6 {:cond "(= #D \"goldenaxe\")"
           :actions ["action-F"]
           :desc "Rule description: 'launch' action-F if #D string is equal to 'goldenaxe'"}
  ;; RULE_7
  :RULE_7 {:cond "(= #D 'goldenaxe2')"
           :actions ["action-G"]
           :desc "Rule description: 'launch' action-G if #D string is equal to 'goldenaxe2'"}
  ;; RULE_8
  :RULE_8 {:cond "(> (sqrt #C) 10)"
           :actions ["action-H-sqrt"]
           :desc "Rule description: 'launch' action-H-sqrt if square root of #C is greater than 10"}
  ;; RULE_9
  :RULE_9 {:cond "(every? even? (list #A #B #C))"
           :actions ["action-I-even?"]
           :desc "Rule description: 'launch' action-I-even? if all elements from list are even"}
}
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
