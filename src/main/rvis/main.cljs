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
;; simple echarts example
;; https://echarts.apache.org/en/tutorial.html#Get%20Started%20with%20ECharts%20in%205%20minutes

;; the atom to hold the options
(defonce chart-options (reagent/atom ""))

;; create the chart component and lifecycle methods
;; including a function to .init the component
;; and to run .setOption to load the chart options passed as props from an atom
;; ---------------------------------------
(defn echart-inner []
  (reagent/create-class {:display-name "echart-component"
                         :component-did-mount (fn [comp]
                                                (let [chart (echarts/init (reagent-dom/dom-node comp))
                                                      options (clj->js (reagent/props comp))]
                                                  (.setOption chart options)))
                         :reagent-render (fn [comp] [:div  {:style {:width "600px" :height "300px"}}])}))

;; @config is a map so it gets passed as props and used with reagent/props in echart-inner.
;; Non-props values can be accessed via (reagent/argv comp)
(defn echart-outer [config]
  [echart-inner @config])
;; ---------------------------------------

;; ALTERNATIVELY: the options can be passed as a map throuhg an argument to echart-inner
;; which doesn't seem so magical as props
;; ---------------------------------------
; (defn echart-inner [chart-options]
;   (reagent/create-class {:display-name "echart-component"
;                          :component-did-mount (fn [comp]
;                                                 (let [chart (echarts/init (reagent-dom/dom-node comp))
;                                                       options (clj->js chart-options)]
;                                                   (.setOption chart options)))
;                          :reagent-render (fn [comp] [:div  {:style {:width "600px" :height "300px"}}])}))

;; chart-options is a map here,
;; so it gets passed as an argument required by echart-inner
;; and doesn't need derefed
; (defn echart-outer [config]
;   [echart-inner config])
;; ---------------------------------------

; chart options examples that should work
(def pie-chart
  {:title {:text "Pie Chart"}
   :series [{:name "pie"
             :type "pie"
             :data [{:value 400 :name "A"}
                    {:value 335 :name "B"}]}]})

(def bar-chart {:title   {:text "Bar Chart"}
                :tooltip {}
                :legend  {:data ["Sales"]}
                :xAxis   {:data ["aik" "amidi" "chiffon shirt" "pants" "heels" "socks"]}
                :yAxis   {}
                :series  [{:name "Sales"
                           :type "bar"
                           :data [5, 20, 36, 10, 20, 30]}]})

(def line-chart {:title {:text "Line Chart"}
                 :xAxis {:type "category" :data ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]}
                 :yAxis {:type "value"}
                 :series [{:type "line"
                           :data [820, 932, 901, 934, 1290, 1330, 1320]}]})

(reset! chart-options pie-chart)

(defn app []
  [:div
   [:h1 "Hello!"]
   [:div
    [:h2 "Build echarts"]
    [:div [echart-outer chart-options]]]
    ; [:div [echart-outer pie-chart]]]
   [:div
    [:h2 "Markdown to HTML Converter"]
    [:div (pr-str @markdown)]
    [:textarea
      {:on-change #(reset! markdown (-> % .-target .-value))
        :value @markdown}]
    [:div (md->html @markdown)]]])

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
