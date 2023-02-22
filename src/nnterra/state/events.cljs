(ns nnterra.state.events
  (:require [reagent.dom]
            [reagent.core]))

(def decoded (reagent.core/atom {}))
(def encoded (reagent.core/atom {}))
