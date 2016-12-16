(ns adventure.core
  (:require [clojure.core.match :refer [match]]
            [clojure.string :as str])
  (:refer-clojure :exclude [take])
  (:gen-class))

(def goblin-map
  {:goblin1 {:strength 5
             :num 1
             :name "goblin"}
   :goblin2 {:strength 10
             :num 8
             :name "goblin"}
   :goblin3 {:strength 10
             :num 2
             :name "goblin"}
   :goblin4 {:strength 15
             :num 7
             :msg "The goblin's soul is sucked into your orb!"
             :name "goblin"}
   :goblin5 {:strength 20
             :num 3
             :msg "Its soul is sucked into the orb!"
             :name "goblin"}
   :goblin6 {:strength 20
             :num 6
             :msg "Its soul is sucked into the orb!"
             :name "goblin"}
   :goblin7 {:strength 35
             :num 4
             :name "massive goblin"}
   :goblin-cheif {:strength 50
             :num 2
             :msg "The cheif falls, shouting 'The master will destroy you!'."
             :name "GOBLIN CHEIF"}
   :goblin-master {:strength 75
             :num 5
             :msg "As the goblin falls, the space around you becomes to crumble, and everything goes dark...\n"
            :name "MASTER GOBLIN"}})

(def obj-map
  {:key1 {:name "Key 1"
          :desc "a key. It's labelled 'Key #1'."
          :type :key}
   :key2 {:name "Key 2"
          :desc "a key. It's labelled 'Key #2'."
          :type :key}
   :key3 {:name "Key 3"
          :desc "a key. It's labelled 'Key #3'."
          :type :key}
   :key4 {:name "Key 4"
          :desc "a key. It's labelled 'Key #4'."
          :type :key}
   :key5 {:name "Key 5"
          :desc "a key. It's labelled 'Key #5'."
          :type :key}
   :key6 {:name "Key 6"
          :desc "a key. It's labelled 'Key #6'."
          :type :key}
   :sword {:name "Sword"
           :desc "a sword. This should help you fight the goblins!"}
   :orb {:name "Mysterious Orb"
         :desc "a mysterious orb. I wonder what it does?"
         :type :orb}
   :note1 {:name "Note 1"
           :desc "a note. It's from Mattox! It reads 'Be wary as you go up, there's danger on the second floor. -Mattox'."
           :type :note}
   :note2 {:name "Note 2"
           :desc "another note. It reads 'You've made it this far, but the house only gets more treacherous on the next floor. Be careful! -Mattox'."
           :type :note}
   :note3 {:name "Note 3"
           :desc "a note. It reads 'You can only go through this portal if you can harvest three goblin souls with your orb. I'm counting on you! -Mattox'."
           :type :note}
   :health-potion {:name "Empty potion bottle"
                   :desc "a health potion. It restores 50 health points!"
                   :type :potion}

   :health-potion2 {:name "Empty potion bottle"
                   :desc "a health potion. It restores 50 health points!"
                   :type :potion}
  })

(def house-map
  {:entrance {:desc "There is one door in front of you (south). "
           :title "at the entrance"
           :dir {:south :room3}
           :floor 1}

   :room2 {:desc "There are no doors besides the one behind you (east). "
           :title "in room 2"
           :dir {:east :room3}
           :floor 1
           :contents :key1}

   :room3 {:desc "The room is empty. There are doors to the north, east, and west. "
              :title "in room 3"
              :dir {:north :entrance
                    :west :room2
                    :east :room4}
              :floor 1}

   :room4 {:desc "The room is dark, but there is some light coming from above. There is a door to the west. "
           :title "in room 4"
           :dir {:west :room3
               :up :room5}
           :floor 1
           :contents :note1
           :lock :key1}

   :room5 {:desc "There is a ladder going downwards, and doors to all sides. You hear a low growl to the east. "
           :title "in room 5"
           :dir {:down :room4
                 :north :room6
                 :west :room7
                 :east :room8
                 :south :room9}
           :floor 2
           :lock :note1}

   :room6 {:desc "The only door is the one behind you (south). "
           :title "in room 6"
           :dir {:south :room5}
           :floor 2
           :contents :key6
           :lock :key5}

   :room7 {:desc "The only door is behind you (east). "
           :title "in room 7"
           :dir {:east :room5}
           :floor 2
           :contents :key2}

   :room8 {:desc "You're in a foul-smelling room. There are doors to the east, west, and south. "
           :goblin-desc "You're in a foul-smelling room. You look around and see... A GOBLIN! Coming straight for you! "
           :title "in room 8"
           :dir {:west :room5
                 :south :room10
                 :east :room11}
           :goblin :goblin1
           :floor 2}

   :room9  {:desc "The only door is behind you (north). "
           :title "in room 9"
           :dir {:north :room5}
           :floor 2
           :contents :sword
           :lock :key2}

   :room10  {:desc "The only door is behind you (north). "
           :title "in room 10"
           :dir {:north :room8}
           :floor 2
           :contents :key3}

   :room11  {:desc "There is a door to the west and a scary-looking ladder going upwards. "
           :title "in room 11"
           :dir {:west :room8
                 :up :room12}
           :floor 2
           :contents :note2
           :lock :key3}

   :room12 {:desc "There is a horrendous stench all around you. There are doors to every side, and a ladder going down. The door to the west has a strange glow emitting from it. "
            :title "in room 12"
            :dir {:down :room11
                  :west :room13
                  :north :room14
                  :east :room15
                  :south :death}
            :floor 3
            :lock :note2}

   :room13 {:desc "There is a room to the east. "
            :title "in room 13"
            :dir {:east :room12}
            :floor 3
            :contents :orb
            :lock :key6}

   :room14 {:desc "There is a door behind you (south). "
            :title "in room 14"
            :dir {:south :room12}
            :floor 3
            :contents :key5}

   :room15 {:desc "There are rooms to the north, east, and west. "
            :goblin-desc "You look in front of you and see... A GOBLIN! This one's bigger than the last one. "
            :title "in room 15"
            :dir {:west :room12
                  :north :room16
                  :east :room17
                  }
            :goblin :goblin2
            :floor 3}

   :room16 {:desc "There is one door to the south"
            :goblin-desc "In front of you there's... ANOTHER LARGE GOBLIN! "
            :title "in room 16"
            :dir {:south :room15}
            :goblin :goblin3
            :floor 3
            :contents :health-potion}

   :room17 {:desc "There is a strange portal above you, and doors to the east and west. "
            :title "in room 17"
            :dir {:up :gr1
                  :east :room18
                  :west :room15}
            :floor 3
            :contents :note3
            :lock :orb}

   :room18 {:desc "There are doors to the north, south, and west. "
            :goblin-desc "There is a goblin in front of you, even larger than the last! "
            :title "in room 18"
            :dir {:north :room19
                  :south :room20
                  :west :room17}
            :goblin :goblin4
            :floor 3
            :lock :note3}

   :room19 {:desc "There is one door to the south. "
            :goblin-desc "There is a huge goblin in front of you! "
            :title "in room 19"
            :dir {:south :room18}
            :goblin :goblin5
            :floor 3}

   :room20 {:desc "There is one door to the north. "
            :goblin-desc "There is a huge goblin in front of you! "
            :title "in room 20"
            :dir {:north :room18}
            :goblin :goblin6
            :floor 3}

   :gr1 {:desc "It is an alternate dimension of some sort. The only path is north. "
         :title "in a strange place"
         :dir {:north :gr2}
         :floor -1
         :lock :portal}

   :gr2 {:desc "It is an alternate dimension of some sort. The only path is north. "
         :goblin-desc "Over you looms a massive goblin! "
         :title "in a strange place"
         :dir {:north :gr3}
         :floor -1
         :goblin :goblin7}

    :gr3 {:desc "It is an alternate dimension of some sort. The only path is north. "
         :goblin-desc "You look up, and see an enormous goblin. He says 'I am the GOBLIN CHEIF, there's no way you can get past me!' "
         :title "in a strange place"
         :dir {:north :gr4}
         :floor -1
         :goblin :goblin-cheif}

    :gr4 {:desc "You're alone, but can feel an incredibly evil presence to the north. "
         :title "in a strange place"
         :dir {:north :gr5}
         :floor -1
         :contents :health-potion2}

    :gr5 {:desc "It is an alternate dimension of some sort. "
         :goblin-desc "Over you looms a goblin taller than the sears tower! He says 'I am the MASTER GOBLIN. You've caused me a lot of trouble human, but make no mistake. Dr. Mattox is mine! Try to take him from me and it will be your end!' "
         :title "in a strange place"
         :dir {}
         :floor -1
         :goblin :goblin-master}

    :last-room {:desc "There is one door to the east, and one to the west. "
                :title "in some room in the house"
                :dir {:east :end
                      :west :death}
                :floor 4}
   })

(def adventurer
  {:location :entrance
   :inventory #{}
   :moves 0
   :health 100
   :goblins-killed #{}
   :seen #{}})


(defn goblin? [player]
  (let [location (player :location)]
        (if (-> house-map location :goblin)
          (nil? ((-> house-map location :goblin) (player :goblins-killed)))
          nil)))

(defn status [player]
  (let [location (player :location)]
    (cond (= location :end) (do (println "Congrats! You've found Mattox!") (System/exit 0))
          (= location :death) (do (println "Wrong way! You've fallen to your death!") (System/exit 0))
          (= (player :health) 0) (do (println "You're out of health. You have perished!") (System/exit 0))
          (= (- 72 (player :moves)) 0) (do (println "You've ran out of time, Mattox is gone!") (System/exit 0))
          :else
          (do (print (str "You are " (-> house-map location :title) ". "))
          (when-not ((player :seen) location)
            (if (goblin? player) (print (-> house-map location :goblin-desc))
            (print (-> house-map location :desc))))
          (update-in player [:seen] #(conj % location))))))

(defn to-keywords [commands]
  (mapv keyword (str/split commands #"[.,?! ]+")))

(defn notInInv? [player obj]
  (nil? (obj (player :inventory))))

(defn tock [player]
  (update-in player [:moves] inc))

(defn portal? [player]
  (let [inv (player :goblins-killed)]
    (and (:goblin5 inv) (:goblin6 inv))))

(defn go [dir player]
  (let [location (player :location)
        dest (->> house-map location :dir dir)]
    (cond (nil? dest) (do (println "You can't go that way.") player)
          (dest (player :seen)) (tock (assoc-in player [:location] dest))
          (goblin? player) (do (println "There is a goblin blocking your way!") player)
          :else
          (let [lockObj (-> house-map dest :lock)]
          (cond (nil? lockObj) (tock (assoc-in player [:location] dest))
            (= lockObj :portal) (if (portal? player) (do (println "You are violently sucked in by the portal!") (tock (assoc-in player [:location] dest)))
                                                     (do (println "You are unable to go thorugh the portal.") player))
            :else
            (let [obj-type (-> obj-map lockObj :type)]
              (cond (and (= obj-type :key) (notInInv? player lockObj)) (do (println (str "The door is locked! It can only be opened with " (-> obj-map lockObj :name) ".")) player)
                (= obj-type :key) (do (println (str "Door unlocked with " (-> obj-map lockObj :name) ".")) (tock (assoc-in player [:location] dest)))
                (and (= obj-type :note) (notInInv? player lockObj)) (do (println "There is a note!") player)
                (and (= obj-type :orb) (notInInv? player lockObj)) (do (println "'You can't proceed without the orb!' says an ominous voice from above.") player)
                :else (tock (assoc-in player [:location] dest)))))))))


(defn search [player]
  (let [location (player :location)
        obj (-> house-map location :contents)]
    (cond (nil? obj) (do (println "Nothing was found.") player)
          (notInInv? player obj) (do (println "You've found an object!") player)
          :else
          (do (println "You've already searched this room.") player))))

(defn getPlayer [player obj]
  (if (= (-> obj-map obj :type) :potion)
      (update-in player [:health] + 50)
       player))


(defn take [player]
  (let [location (player :location)
        obj (-> house-map location :contents)]
  (cond (nil? obj) (do (println "No objects to take.") player)
        (notInInv? player obj)
          (do (println (str "You picked up " (-> obj-map obj :desc)))
          (update-in (getPlayer player obj) [:inventory] #(conj % obj)))
        :else
        (do (println "No objects to take.") player))))

(defn displayitems [player]
   (let [items (player :inventory)]
     (if (empty? items) (do (println "You don't have anything.") player)
         (do (println "\nItems: ")
         (loop [it items]
           (if (empty? it) (do (print "\n") player)
               (do (let [item (first it)] (println (-> obj-map item :name))) (recur (rest it)))))))))

(defn getNum [numStr]
  (cond (= numStr "0") 0
        (= numStr "1") 1
        (= numStr "2") 2
        (= numStr "3") 3
        (= numStr "4") 4
        (= numStr "5") 5
        (= numStr "6") 6
        (= numStr "7") 7
        (= numStr "8") 8
        (= numStr "9") 9
        :else -1))

(defn win? [plin gnum target]
  (let [pnum (getNum plin)]
  (cond
    (= plin "cheat") true
      (not= pnum -1) (< (Math/abs (- target pnum)) (Math/abs (- target gnum)))
      :else (do (println "Invalid Input.") nil))))

(defn do-fighting [player]
  (println "You've engaged in combat with the goblin! Pick a number from 0-9.")
  (let [location (player :location) goblin (-> house-map location :goblin) target (rand-int 10) playernum (read-line)]
    (if (win? playernum (-> goblin-map goblin :num) target)
          (do (println (str "You've defeated the " (-> goblin-map goblin :name) "!"))
              (if (-> goblin-map goblin :msg) (println (-> goblin-map goblin :msg)))
              (if (= goblin :goblin-master) (assoc-in player [:location] :last-room) (update-in player [:goblins-killed] #(conj % goblin))))
          (do (println (str "The goblin won! You lost " (-> goblin-map goblin :strength) " health points."))
              (update-in player [:health] - (min (player :health) (-> goblin-map goblin :strength)))))))

(defn fight [player]
  (if (nil? (:sword (player :inventory))) (do (println "You have nothing to fight with!") player)
        (do-fighting player)))

(defn yell [player]
  (let [location (player :location)
        floor (-> house-map location :floor)]
  (cond (= floor 1)
        (do (println "You hear a faint shout from far above.") player)
        (= floor 2)
        (do (println "You can just barely make out the voice of Dr. Mattox shouting from above.") player)
        (= floor 3)
        (do (println "You can clearly hear Dr. Mattox shouting from the next floor.") player)
        (= floor 4)
        (do (println "You can hear Dr. Mattox shouting loudly from one of the adjacent rooms, but you don't know which one!") player)
        :else
        (do (println "No response.") player))))

(defn respond [player command]
  (match command
         [:look] (update-in player [:seen] #(disj % (-> player :location)))
         [:n] (go :north player)
         [:s] (go :south player)
         [:e] (go :east player)
         [:w] (go :west player)
         [:u] (go :up player)
         [:d] (go :down player)
         [:search] (search player)
         [:take] (take player)
         [:items] (displayitems player)
         [:fight] (if (goblin? player) (fight player) (do (println "No one to fight.") player))
         [:yell] (yell player)
         [:time] (do (println (str "Time Left: " (- 72 (player :moves)) " hours.")) player)
         [:health] (do (println (str "Health: " (player :health))) player)
         [:help] (do (println  "\nCommands:\nn - Go north\ns - Go south\ne - Go east\nw - Go west\nu - Go up\nd - Go down\nlook - Look around\nsearch - Search room for objects\ntake - Add object in room to inventory\nitems - List contents of inventory\nfight - Fight goblin\nhealth - Get amount of health\ntime - Get time remaining\nyell - Shout Dr. Mattox's name\nquit - Quit the game\n") player)
         [:quit] (System/exit 0)
         _ (do (println "I don't understand you.")
               player)
         ))

(defn -main
  "Main method."
  [& args]
  (println "\nRESCUING DR. MATTOX \n\nDr. Mattox has been kidnapped by a goblins! He's been hidden somewhere in the house, and it's your job to find him. (Type 'help' for a list of commands.)\n")
  (loop [local-map house-map
         local-obj-map obj-map
         local-enemy-map goblin-map
         local-player adventurer]
    (let [pl (status local-player)
          _  (println "What do you want to do?")
          command (read-line)]
      (recur local-map local-obj-map local-enemy-map (respond pl (to-keywords command))))))
