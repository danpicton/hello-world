(ns hello-world.core
    (:require [sablono.core :as sab]))

(defonce app-state (atom {:likes 0}))

(defn like-this
  [data]
  (sab/html [:div
              {:style {:font-family "Arial, Helvetica, sans-serif"}}
              [:h1  "popularity at " (:likes @data)]
             [:div
              [:a {:href "#"
                   :onClick #(swap! data update-in [:likes] inc)}
               "Thumbs up"]]
             [:div
              [:svg
               {:style {:border "3px solid black"
                        :width 200
                        :height 100}}
               [:rect {
                       :x 60
                       :y 60
                       :height 50
                       :width 50
                       :fill "blue"
                       :border "1px solid black"}]]]
             [:div
              [:canvas
               {:id "canvy-canvas"
                :width 400
                :height 400}]]]))


(defn render!
  []
  (.render js/ReactDOM
           (like-this app-state)
           (.getElementById js/document "app")))

(add-watch app-state :on-change (fn [_ _ _ _] (render!)))

(render!)
(def canvas (.getElementById js/document "canvy-canvas"))

(defn draw-pixel! [canvas x y color]
 (let [ctx (.getContext canvas "2d")
       scale 2]
   (set! (.-fillStyle ctx) color)
   (.fillRect ctx (* scale x) (* scale y) scale scale)))

(defn reset-canvas! [canvas]
 (let [ctx (.getContext canvas "2d")]
   (set! (.-fillStyle ctx) "white")
   (.fillRect ctx 0 0 (.-width canvas) (.-height canvas))))

(defn draw-bw-wallpaper! [canvas a b side]
  (let [points 200]
    (dotimes [i points]
      (dotimes [j points]
        (let [x (+ a (* i  (/ side points)))
              y (+ b (* j  (/ side points)))
              c (int (+ (* x x) (* y y)))]
          (if (odd? c)
            (draw-pixel! canvas i j "magenta")
            (draw-pixel! canvas i j "cyan")))))))


(draw-bw-wallpaper! canvas 5 5 9)
;(reset-canvas! canvas)
;(enable-console-print!)

;(println "This text is printed from src/hello-world/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

;(defonce app-state (atom {:text "Hello world!"}))


;(defn on-js-reload [])
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
