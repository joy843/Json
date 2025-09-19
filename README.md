# JSON – Librairie personnalisée en Java

Projet réalisé dans le cadre du cours **INF1035 – Concepts avancés en programmation orientée objet**.  
L’objectif était de développer une **librairie JSON** permettant de **sérialiser et désérialiser** des objets Java, sans dépendre de bibliothèques externes.

## Contexte

Ce projet avait pour but de comprendre la **réflexion en Java** et de manipuler des concepts plus avancés comme :

- La **réflexion** (reflection API) pour explorer dynamiquement les champs d’objets.  
- Les **annotations personnalisées** (`@Alias`, `@Ignored`) afin de contrôler la sérialisation.  
- L’utilisation de différentes **structures de données** (listes, objets imbriqués, collections).  

 Honnêtement, ce n’était pas un projet simple : il fallait comprendre comment fonctionne vraiment un objet en mémoire, comment l’explorer dynamiquement et comment traduire tout ça en texte JSON valide.  

## Fonctionnalités

- Sérialisation d’un objet Java en chaîne JSON.  
- Désérialisation d’une chaîne JSON en objet Java.  
- Gestion des champs simples, objets imbriqués, et collections.  
- Support d’annotations :  
  - `@Alias("nom")` → change le nom du champ dans le JSON.  
  - `@Ignored` → ignore un champ lors de la sérialisation.
