#! /usr/bin/bash

clear

for message in "Merci aux développeurs de Mozilla" "Merci surtout à tous les développeurs qui s'intéressent à la sécurité" "Merci à tous ceux qui font des erreurs parce que c'est comme ça qu'on apprend et sinon les consultants sécurité n'auraient pas de travail ;)" "Merci à tous" "A bientôt"
do
  toilet -w 146 -k -f big "$message" | lolcat -a -d 7 -s 20
done
