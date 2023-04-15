# nnterra
Nostr means “Notes and Other Stuff Transmitted by Relays”. This project is about the “Other Stuff”.

Take a look: https://nnterra.web.app/?event=note1zud03a8kh32xuxklv6jg7l0zml75k5fmx47d4lqygqg4wgc6d5wqznwr2h&relay=wss://nos.lol


----------------------------------
Project status: Halted.

The project was able to render simple web applications and had good feedback.

Learnings:
-  hiccup is not popular. If we use a more popular format, we can leverage external tools.
-  Support of multiple relays is a must-have.

Challenges:
- How to handle web applications that need multiple files instead of inline js and css?
- How to update an application?

Open questions:
- Could we leverage a browser to render the content, just adding the capability of fetching data from Nostr?
- What should be the format?

Next steps:
I'm available to help anyone interested in moving forward with this project.  Given no one is interested, I'll be back to this project after finishing the other project I'm working.

You can read some of the suggestions in https://github.com/nostr-protocol/nips/issues/298#issuecomment-1464814141.


----------------------------------

## What
`nnterra` is an experiment about another kind of client for Nostr. Instead of a client handling notes, followers, and likes, it can render web apps.
You can think of it as a web browser that instead of requesting the source code of a page to a centralized server, it’ll make the request to an `N` number of relays.

It works by consuming an event with the source code of the web app from the relays. Currently, it can only consume. You'll have to use another client to publish.

That said, the project is just a proof of concept, without a lot of features and with questionable design decisions.
If proven as useful, in the future it should grow to something more robust, like a proper web browser, or a browser extension.

## How does it works

### High-level view:
1. You produce a message to a Nostr relay with content that can be rendered in the [hiccup](https://github.com/weavejester/hiccup) format.
2. With the event-id and relay list, you make a call to `nnterra`: `https://nnterra.web.app/?event=note1zud03a8kh32xuxklv6jg7l0zml75k5fmx47d4lqygqg4wgc6d5wqznwr2h&relay=wss://nos.lol`
3. `nnterra` will request to relays the content of the event
4. Once one relay answers, it’ll render the content for you

### Implementation (detailed) view:
The current implementation was based on “what is the easiest way to validate it?” so don’t assume that anything in the code should be considered a rule.
If you want to test by yourself, you should:
1. Create a page in `hiccup-like` format
    1. You can use standard HTML and use a tool like [html2hiccup](https://html2hiccup.dev)
2. Encode it to `base64`
3. Publish on some relay
4. Get the `relay-url` and `event-id`
5. Call `nnterra` with that information: `https://nnterra.web.app/?event={event-id}&relay={relay-url}`
6. (optional) Keep the browser console open because the project doesn’t handle errors yet

## Known limitations
* We don’t have a client to send the event. You must use something like `noscl` to publish it.
* We don’t have defined a kind id for that yet.
    * Using the default `text_note` event type is horrible, I know. I'll find a way to use a custom event kind asap.
* We are using hiccup-like content. It’s good for some context, but using plain and simple HTML would be better.
* We can handle only one relay for now. Should be easy to request from more than one relay.
* We should require and validate the pubkey from the owner of the event in the URL to avoid problems with rogue relays.
* We don't know how to handle deploy of a new version of an application.
* `href` is doable, but a lot of manual work is required.
* Finally, I don't know if this is useful at all.

## Why
Publishing your web application to multiple relays can increase the availability and the censorship resistance factor.
We could have entire Nostr clients, marketplaces and other useful tools depending only on Nostr relays.

## Open questions
* With event-id and relay list only it can be vulnerable to rogue relays. Maybe adding a pubkey to the request? A sig?
* What kind of language should be the standard? HTML + js?
    * It seems like a good idea because we can leverage the same engine that exists in browsers today.
* Could cause an impact on the relay?
    * The size of the event will be bigger than a standard note. But the number of messages of this kind should be a lot less.
    * Relays could charge an extra to publish this kind of event.
* How to handle deploy of new versions?
 

# Help wanted!
PLEASE, let me know what you think:
* What could be better
* What would not work
* What are your concerns

### You can either:
* Post a note with the #nnterra tag
* Send me a message on Nostr npub184jvqdrlyu34y28yd5p9a786jwnjrsrd9tr9nl24n52lzwmk4deqkkzed7
* Open a GitHub issue


----------------------------------


### Development mode
```
npm install
npx shadow-cljs watch app
```
start a ClojureScript REPL
```
npx shadow-cljs browser-repl
```
### Building for production

```
npx shadow-cljs release app
```
