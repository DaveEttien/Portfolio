#  Gestion de Machines Virtuelles (C)

Projet réalisé dans le cadre du cours de systèmes d’exploitation, visant à implémenter un système de gestion de machines virtuelles capable d’exécuter des instructions LC-3.

---

## À propos

Ce projet met en pratique des concepts fondamentaux en programmation bas niveau, notamment la gestion mémoire, la manipulation de pointeurs et l’exécution d’instructions machine.

---

##  Objectif

Développer un système capable de :
- gérer des machines virtuelles (VM)
- charger et exécuter des programmes LC-3
- traiter des fichiers de transactions
- simuler un environnement d’exécution bas niveau

---

##  Fonctionnalités

- Création et gestion de machines virtuelles
- Allocation dynamique de mémoire
- Exécution d’instructions LC-3 :
  - ADD, AND, NOT
  - LD, ST, LDR, STR
  - BR, JMP, JSR
  - LEA, TRAP
- Lecture et exécution de fichiers de transactions
- Vérification de la validité des opérations

---

##  Problèmes rencontrés

Lors du développement, plusieurs erreurs critiques ont été identifiées :

- Segmentation fault lors des accès mémoire :contentReference[oaicite:0]{index=0}  
- Instructions LC-3 non reconnues (“OP inconnu”) :contentReference[oaicite:1]{index=1}  
- Comportements imprévisibles lors des transactions :contentReference[oaicite:2]{index=2}  

---

##  Solutions apportées

###  Gestion mémoire sécurisée
- Vérification du retour de `malloc`
- Blocage de l’exécution si allocation échoue

###  Validation des pointeurs
- Vérification systématique avant accès mémoire
- Gestion des erreurs pour éviter les crashes

###  Débogage avancé
- Ajout de logs pour :
  - création des VM
  - exécution des instructions
  - accès mémoire

###  Correction du décodeur LC-3
- Ajout de tous les opcodes standards :
  - BR, ADD, LD, ST, JSR, AND, LDR, STR, NOT, LDI, STI, JMP, LEA, TRAP :contentReference[oaicite:3]{index=3}  

###  Validation des transactions
- Vérification de la cohérence des fichiers
- Obligation de créer une VM avant exécution

---

##  Technologies utilisées

- Langage C
- Gestion mémoire (`malloc`, `free`)
- Manipulation de pointeurs
- Architecture bas niveau
- Simulation d’instructions LC-3

---

## Structure du projet
Projet 2/
│
├── src/ # Code source
├── texte/ # Fichiers de transactions
├── Programme/ #Fichiers olc3
├── Makefile # Compilation
├── Doc/ # Fichier pdf(rapport)

