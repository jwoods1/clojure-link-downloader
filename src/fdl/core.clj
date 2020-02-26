(ns fdl.core
  (:require [clojure.string :as str]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(def root "F:/TV Series/")

(defn get-file [path] (with-open [reader (io/reader path)]
           (doall
            (csv/read-csv reader :seperator \|))))
(def bb (get-file "./bigbang.csv"))


(defn copy-uri [uri file] 
  (with-open [in (io/input-stream uri) out (io/output-stream file)]
    (io/copy in out)))

(defn dir-make [dir] (io/make-parents dir) dir)

(defn set-dir [path] (let [ a (str/split path #"\.") 
                           r (first a) 
                           s (rest a) 
                           season (first s)
                           name (str/split season #"")] 
                       (str  r "/" (nth name 0) (nth name 1)  (nth name 2)"/" (nth name 3) (nth name 4) (nth name 5) ".mkv" )))

(defn prep [file] (let [ path (set-dir (first file)) 
                        link(rest file)] {:path path :link link}))


(defn freshFile [file] (let [folder (first file)
                             season (first (rest file))
                             name (first (rest (rest file)))
                             link (last file)]
                         {:path (str root (str/trim folder) "/" (str/trim season) "/" (str/trim season) (str/trim name)) :link link}))
(comment
  (def movies  (map freshFile bb))
  (def m (first movies))
  (count movies)
  (dir-make (:path  (first movies)))
  (:path m)
  (copy-uri (:link m) (:path m))
  (pmap #(->>  (:path %)
               (dir-make)
               (copy-uri (str/trim (:link %)))) movies)
  (def f (io/file "Friends"))

  (def fs (file-seq f))
  (filter #(.isFile %) fs)
  
  )