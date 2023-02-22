(ns nnterra.nostr.event
  (:require ["nostr-tools" :as nostr-tools]))

(defn retrieve!
  [event-id relay-url event-handler]
  (let [relay   (nostr-tools/relayInit. relay-url)]
    (-> (.connect relay)
        (.then (fn []
                 (let [sub (.sub relay  (clj->js [{:ids [event-id]}]))]
                   (.on sub "event" event-handler)
                   (.on sub "eose" (fn []
                                     (.unsub sub)
                                     (.close relay)))))))))
