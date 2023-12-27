(ns <<ns-name>>.web.views.home
    (:require
      [<<ns-name>>.web.domino :as domino]
      [<<ns-name>>.web.htmx :refer [page-htmx]]
      [<<ns-name>>.web.views.disp.home :as disp.home]
      [simpleui.core :as simpleui :refer [defcomponent]]))

(defcomponent ^:endpoint bmi-form [{:keys [session] :as req} ^:double height ^:double weight]
  (cond
   height (domino/transact session :height height)
   weight (domino/transact session :weight weight)
   :else (disp.home/form
          (domino/select session :height)
          (domino/select session :weight)
          (domino/select session :bmi))))

(defn ui-routes [base-path]
  (simpleui/make-routes
   base-path
   (fn [req]
     (page-htmx
      (bmi-form req)))))

