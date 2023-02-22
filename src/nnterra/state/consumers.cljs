(ns nnterra.state.consumers
  (:require [goog.crypt.base64 :as base64]
            [nnterra.state.events :as state.events]))

(defn decode-event
  []
  (when-let [encoded-event (:content @state.events/encoded)]
    (swap! state.events/decoded (fn [e]
                                  (assoc e :content (base64/decodeString encoded-event))))))

(defn load
  []
  (decode-event))