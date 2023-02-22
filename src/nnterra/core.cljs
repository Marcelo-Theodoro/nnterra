(ns nnterra.core
  (:require [nnterra.logic.path :as logic.path]
            [nnterra.nostr.event :as nostr.event]
            [reagent.dom]
            [reagent.core]
            [cljs.reader :as reader]
            [nnterra.state.events :as state.events]
            [nnterra.state.consumers :as state.consumers]))
;; If `debug` true, it'll bypass all the requesting part and only show the content of `mocked-debug-content`
;; Useful for testing before publishing
(def debug false)
(def mocked-debug-content (str [:div {:style {:background-color "#EED9F4"
                                              :height "100%"
                                              :width "100%"
                                              :position "absolute"
                                              :top  0
                                              :left 0
                                              :text-align "center"}}
                                [:p]
                                [:h1 "The page you are seeing is stored on Nostr."]
                                [:p "For more information, take a look in "
                                 [:a {:href "https://github.com/marcelo-Theodoro/nnterra/"} "nnterra"]
                                 "!"]]))

;; Handler

(defn handle-note
  [note]
  (let [content (.-content note)]
    (swap! state.events/encoded
           #(assoc % :content content))))

;; Views

(defn execute-note []
  (if debug
    (reader/read-string mocked-debug-content)

    (let [qs       (js/URLSearchParams. js/window.location.search)
          event-id (some-> (.get qs "event") logic.path/note-hex-id)   ;; It assumes an NIP-19 event-id.
          relay    (first (.getAll qs "relay"))                        ;; Currently, we can handle only one relay.
          ]

      (assert event-id "No valid event-id provided")
      (assert relay "No relay provided")

      (state.consumers/load)
      (nostr.event/retrieve! event-id relay handle-note)

      (if (empty? @state.events/decoded)
        [:div [:p "Loading..."]]                              ;; Event is not ready. Default Loading screen.
        (reader/read-string (:content @state.events/decoded)) ;; Event is read. Render the content.
        ))))


;; Initialize app

(defn mount-root []
  (reagent.dom/render [execute-note] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))

