(ns olympus.directives.select-on-click
  (:use-macros [purnam.angular :only [def.directive]]))
  
(def.directive olympus.selectOnClick []
  (fn [$scope element attrs]
    (element.bind "click" (fn [] (this.select)))))

