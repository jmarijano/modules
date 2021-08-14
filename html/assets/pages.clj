(ns <<ns-name>>web.routes.pages
  (:require
    [<<ns-name>>.web.middleware.exception :as exception]
    [<<ns-name>>.web.pages.layout :as layout]
    [integrant.core :as ig]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.parameters :as parameters]
    [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]))

(defn wrap-site-defaults []
  (let [error-page (layout/error-page
                     {:status 403
                      :title "Invalid anti-forgery token"})]
    #(wrap-anti-forgery % {:error-response error-page}))))

(defn home [request]
  (layout/render request "home.html"))

;; Routes
(defn page-routes [base-path]
  [base-path
   ["/" {:get home}]])

(defn route-data
  [{:keys [template-opts]}]
  {:swagger    {:id ::api}
   :middleware [;; Ring site defaults
                (wrap-site-defaults)
                ;; query-params & form-params
                parameters/parameters-middleware
                ;; encoding response body
                muuntaja/format-response-middleware                
                ;; exception handling
                exception/wrap-exception]})

(derive :reitit.routes/pages :reitit/routes)

(defmethod ig/init-key :reitit.routes/pages
  [_ {:keys [base-path]
      :or   {base-path ""}
      :as   opts}]
  (layout/init-custom-tags!)
  ["" (route-data opts) (page-routes base-path)])

