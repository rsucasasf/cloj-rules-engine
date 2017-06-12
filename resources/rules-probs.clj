;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; RULES DEFINITION FILE:
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
{
  ;; RULE_1
  :RULE_1 {:cond "(and (< #A 10) (> #B 50))"
           :actions [{"action-A" 1}]
           :desc "Rule description: 'launch' action-A if 'a' is lower than 10 and if 'b' is greater than 50"}
  ;; RULE_2
  :RULE_2 {:cond "(> #A 10)"
           :actions [{"action-B" 0.5} {"action-C" 0.5}]
           :desc "Rule description: 'launch' action-B and action-C if #A greater than 10"}
}
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
