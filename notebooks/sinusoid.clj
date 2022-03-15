^{:nextjournal.clerk/visibility #{:hide-ns}}
(ns notebooks.sinusoid
  (:require [nextjournal.clerk :as clerk]
            [sinusoid.ratio :refer [ratio fractions-of-pi]]))

;; ## Solve sinusoidal equations

;; Adapted from: https://www.khanacademy.org/math/trigonometry/trig-equations-and-identities/xfefa5515:sinusoidal-equations/e/solve-advanced-sinusoidal-equations

;; Derive one or more expressions that together represent all solutions to the equation. 
;; Assume $n$ is any integer.

(def mode :rad)

^{:nextjournal.clerk/visibility #{:hide}}
(case mode
  :deg (clerk/html "Your answer should be in degrees rounded to the nearest hundredth.")
  :rad (clerk/html "Your answer should be in radians."))

(defn round [n d]
  (double (/ (Math/round (* d n)) d)))

(def pi Math/PI)

(defn deg [rad]
  (round (/ rad (/ pi 180)) 100))

(def sinusoid
  {:amplitude -4
   :trig-fn "\\cos"
   :period 5
   :x-shift 1
   :val 1})

^{:nextjournal.clerk/visibility #{:hide}}
(let [{:keys [amplitude trig-fn period x-shift val]} sinusoid]
  (clerk/tex (str amplitude trig-fn "(" period "x)" 
                 (when (pos? x-shift) "+")
                  x-shift "=" val)))

;; Trig identities

^{:nextjournal.clerk/visibility #{:hide}}
(clerk/tex 
 (case (:trig-fn sinusoid)
   "\\cos" "\\cos(\\theta)=\\cos(-\\theta)"
   "\\sin" "\\sin(\\theta)=\\sin(180\\degree-\\theta)"))

^{:nextjournal.clerk/visibility #{:hide}}
(clerk/tex
 (case (:trig-fn sinusoid)
   "\\cos" "\\cos(\\theta)=\\cos(\\theta+360\\degree)"
   "\\sin" "\\sin(\\theta)=\\sin(\\theta+360\\degree)"))

^{:nextjournal.clerk/visibility #{:hide}}
(defn inverse-trig-fn [n]
  (case (:trig-fn sinusoid)
    "\\cos" (Math/acos n)
    "\\sin" (Math/asin n)))

;; Isolate the sinusoidal function

^{:nextjournal.clerk/visibility #{:hide}}
(let [{:keys [amplitude trig-fn period x-shift val]} sinusoid]
  (clerk/tex
   (str "\\begin{aligned}"
        amplitude trig-fn "(" period "x)"
       (when (pos? x-shift) "+")
        x-shift "&=" val "\\\\\\\\ "
        amplitude trig-fn "(" period "x)&=" (- val x-shift) "\\\\\\\\ "
        trig-fn "(" period "x)&=" (ratio (/ (- val x-shift) amplitude))
        "\\end{aligned}")))

;; Use the inverse trig function to find one value of `(* period x)`

^{:nextjournal.clerk/visibility #{:hide}}
(let [{:keys [amplitude trig-fn x-shift val]} sinusoid
      v (inverse-trig-fn (/ (- val x-shift) amplitude))]
  (clerk/tex
   (str trig-fn "^{-1}\\left(" (ratio (/ (- val x-shift) amplitude)) "\\right)="
        (case mode
          :deg (str (deg v) "^\\circ")
          :rad (or (get fractions-of-pi v) (round v 1000))))))

;; Now we can use the first identity to find the second solution within the interval:

^{:nextjournal.clerk/visibility #{:hide}}
(let [{:keys [amplitude trig-fn x-shift val]} sinusoid
      v (inverse-trig-fn (/ (- val x-shift) amplitude))]
  (clerk/tex
   (case mode 
     :deg (case trig-fn
            "\\cos" (str "-" (deg v) " ^\\circ")
            "\\sin" (str "-180^\\circ-" (deg v) " ^\\circ=" (- -180 (deg v))))
     :rad (case trig-fn
            "\\cos" (str "-" (or (get fractions-of-pi v) (round v 1000)))
            "\\sin" (str "\\pi-" (round v 1000) "=" (round (- pi v) 1000))))))

;; Using the second identity, we can find all the solutions to our equation 
;; from the two angles we found above. 
;; The first solution gives us the following:

^{:nextjournal.clerk/visibility #{:hide}}
(let [{:keys [amplitude trig-fn period x-shift val]} sinusoid
      v (inverse-trig-fn (/ (- val x-shift) amplitude))]
  (clerk/tex
   (str "\\begin{aligned}" period "x&="
        (case mode
          :deg (str (deg v) "^\\circ+n\\cdot360^\\circ \\\\\\\\x&=\\dfrac{" 
                    (deg v) "^\\circ+n\\cdot360^\\circ}{" period "}="
                    (round (/ (deg v) period) 1000)
                    "^\\circ+n\\cdot" (/ 360 period) "^\\circ")
          :rad (str (round v 1000) "+n\\cdot2\\pi \\\\\\\\x&=\\dfrac{"
                    (round v 1000) "+n\\cdot2\\pi}{" period "}="
                    (round (/ v period) 1000) "+n\\cdot\\dfrac{2\\pi}{" period "}"))
        "\\end{aligned}")))

;; Similarly, the second solution gives us the following:

^{:nextjournal.clerk/visibility #{:hide}}
(let [{:keys [amplitude trig-fn period x-shift val]} sinusoid
      v (inverse-trig-fn (/ (- val x-shift) amplitude))]
(clerk/tex
 (str "\\begin{aligned}" period "x&="
      (case trig-fn 
        "\\cos"
        (case mode
          :deg (str "-" (deg v) "^\\circ+n\\cdot360^\\circ \\\\\\\\x&=\\dfrac{"
                    "-" (deg v) "^\\circ+n\\cdot360^\\circ}{" period "}="
                    "-" (round (/ (deg v) period) 1000) "^\\circ+n\\cdot" (/ 360 period) "^\\circ")
          :rad (str "-" (round v 1000) "+n\\cdot2\\pi \\\\\\\\x&=\\dfrac{"
                    "-" (round v 1000) "+n\\cdot2\\pi}{" period "}="
                    "-"  (round (/ v period) 1000) "+n\\cdot\\dfrac{2\\pi}{" period "}"))
        "\\sin"
        (case mode
          :deg (str (- -180 (deg v)) "^\\circ+n\\cdot360^\\circ \\\\\\\\x&=\\dfrac{"
                    (- -180 (deg v)) "^\\circ+n\\cdot360^\\circ}{" period "}="
                    (/ (- -180 (deg v)) period) "^\\circ+n\\cdot" (/ 360 period) "^\\circ")
          :rad (str (round (- pi v) 1000) "+n\\cdot2\\pi \\\\\\\\x&=\\dfrac{"
                    (round (- pi v) 1000) "+n\\cdot2\\pi}{" period "}="
                    (round (/ (- pi v) period) 1000) "+n\\cdot\\dfrac{2\\pi}{" period "}")))
      "\\end{aligned}")))
