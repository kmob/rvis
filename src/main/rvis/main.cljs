(ns rvis.main
	(:require [reagent.core :as reagent]
		  [reagent.dom :as reagent-dom]
		  ["showdown" :as showdown]))

;; install showdown using npm "npm install --save showdown"
;; which adds showdown to package.json
;; THEN require package loading from node as a string 
;;  ["showdown" :as showdown]
;; look at showdown examples for JS version of the function
;; and convert to a clojure function

;;Markdown to HTML
;;var showdown  = require('showdown'),
;;    converter = new showdown.Converter(),
;;    text      = '# hello, markdown!',
;;    html      = converter.makeHtml(text);

(defonce markdown (reagent/atom ""))

(defonce showdown-converter (showdown/Converter.))

(defn md->html [md]
	(.makeHtml showdown-converter md))

(defn app [] 
  [:div 
	[:h1 "Hello!"]
	[:textarea
	 {:on-change #(reset! markdown (-> % .-target .-value))
	  :value @markdown}]
 	[:div (md->html @markdown)]])

(defn mount! []
	(reagent-dom/render [app]
		(.getElementById js/document "app")))


(defn main! [] 
	(println "Welcome to the app!")
	(mount!))

(defn reload! []
	(println "Reloaded!")
	(mount!))
