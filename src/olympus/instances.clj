(ns olympus.instances
  (:use clj-ssh.ssh
        amazonica.core
        amazonica.aws.ec2))

(defn all [] (->> (describe-instances) :reservations (mapcat :instances)))

(defn create []
  (run-instances :image-id "ami-a1506cc8" :min-count 1 :max-count 1 :instance-type "t1.micro" :key-name "dickeyxxx"))

(defn terminate [id]
  (terminate-instances :instance-ids [id]))

(defn by-id [id]
  (first (filter #(= (% :instance-id) id) (all))))

(defn my-agent []
  (let [agent (ssh-agent {})]
    (add-identity agent {:private-key-path "~/.ssh/id_rsa"})
    agent))

(defn open-port [address port]
  (println "TODO: open port"))

(defn- ssh-session [address]
  (session (my-agent) address {:username "core" :strict-host-key-checking :no}))

(defn run-cmd [address cmd]
  (let [session (ssh-session address)]
    (with-connection session
      (ssh session {:cmd cmd}))))
