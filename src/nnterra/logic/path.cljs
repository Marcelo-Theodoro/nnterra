(ns nnterra.logic.path
  (:require ["nostr-tools" :as nostr-tools]))

(defn note-hex-id
  [nip19-note-id]
  (-> nip19-note-id
      nostr-tools/nip19.decode
      .-data))
