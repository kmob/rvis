(ns rvis.main
  (:require [reagent.core :as reagent]
       [reagent.dom :as reagent-dom]
       ["showdown" :as showdown]
       ["echarts" :as echarts]))

;; SHOWDOWN
;; install showdown using npm "npm install --save showdown"
;; require - ["showdown" :as showdown]
;; simple showdown example of markdown to html
;; var showdown  = require('showdown'),
;;    converter = new showdown.Converter(),
;;    text      = '# hello, markdown!',
;;    html      = converter.makeHtml(text);
;; the dom object to hold the markdown
(defonce markdown (reagent/atom ""))
;; load the showdown converter
(defonce showdown-converter (showdown/Converter.))
;; a function to run .makeHtml on the markdown passed in
(defn md->html [md]
  (.makeHtml showdown-converter md))

;; ECHARTS
;; npm install --save echarts
;; require as ["echarts" :as echarts]
;; echarts documentation
;; https://echarts.apache.org/en/api.html#echarts.init
;; https://echarts.apache.org/en/api.html#echartsInstance.setOption
;; simple echarts examples
;; https://github.com/a0x/echarts_samples
;; https://stackoverflow.com/questions/27930030/echarts-from-baidu
;; the dom object to hold the options
(defonce chart-options (reagent/atom ""))
;; load the echarts/init.
(defonce init-echarts (echarts/init.))
;; a function to run .setOption on the chart options passed in
(defn build-chart [options]
  (.setOption init-echarts (clj->js(options))))

; chart options examples that should work
(def pie_chart
  {:title {:text "Pie Chart"}
   :series [{:name "pie"
             :type "pie"
             :data [{:value 400 :name "A"}
                    {:value 335 :name "B"}]}]})

(def bar_chart {:title   {:text "Bar Chart"}
                :tooltip {}
                :legend  {:data ["Sales"]}
                :xAxis   {:data ["aik" "amidi" "chiffon shirt" "pants" "heels" "socks"]}
                :yAxis   {}
                :series  [{:name "Sales"
                           :type "bar"
                           :data [5, 20, 36, 10, 20, 30]}]})

(def line_chart {:title {:text "Line Chart"}
                 :xAxis {:type "category" :data ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]}
                 :yAxis {:type "value"}
                 :series [{:type "line"
                           :data [820, 932, 901, 934, 1290, 1330, 1320]}]})

(reset! chart-options pie_chart)

(defn app []
  [:div
   [:h1 "Hello!"]
   [:div
    [:h2 "Markdown to HTML Converter"]
    [:div (pr-str @markdown)]
    [:textarea
      {:on-change #(reset! markdown (-> % .-target .-value))
        :value @markdown}]
    [:div (md->html @markdown)]]
   [:div
    [:h2 "Graph"]
    [:div (pr-str @chart-options)]
    [:div (build-chart @chart-options)]]])

;; MOUNT AND MAIN FUNCTIONS
(defn mount! []
  (reagent-dom/render [app]
     (.getElementById js/document "app")))

(defn main! []
  (println "Welcome to the app!")
  (mount!))

(defn reload! []
  (println "Reloaded!")
  (mount!))
