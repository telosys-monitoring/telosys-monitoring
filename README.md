Telosys Monitoring is a Java EE filter to detect the longest requests on your application.

Documentation
---

Please read the documentation :
- [telosys-monitoring-doc-fr.pdf](telosys-monitoring-doc-fr.pdf)
- [telosys-monitoring-doc-fr.doc](telosys-monitoring-doc-fr.doc)

Contact
---

Contact us at : [telosysteam@gmail.com](telosysteam@gmail.com)

Telosys  - Monitoring
Sommaire
1.	Présentation	3
2.	Compatibilités	4
3.	Principes de Fonctionnement	5
4.	Procédure d’installation	6


Présentation
===
Telosys Monitoring analyse le temps d’exécution des requêtes HTTP sur un serveur Apache Tomcat.
L’objectif est de détecter les requêtes dont la durée d’exécution dépasse un seuil défini et de lister ces requêtes via un rapport accessible depuis l’application déployée.

Recommandations
===
Pour garantir le fonctionnement des serveurs et des applications analysées, il est nécessaire d’établir et de valider la procédure d’installation et de configuration du filtre Telosys Monitoring sur un environnement de qualification ou de pré-production et non directement sur l’environnement de production.

Il est nécessaire que l’équipe en charge de l’exploitation des environnements de production ait la connaissance technique des impacts de l’outil Telosys Monitoring sur le fonctionnement des serveurs et des applications analysées par Telosys Monitoring. La connaissance de la procédure d’installation et de ses recommandations est également obligatoire.

Le chapitre ``` Compatibilités ``` indique les produits compatibles avec Telosys Monitoring.

Le chapitre ``` Principes de fonctionnement ``` décrit le fonctionnement interne de Telosys Monitoring et ses impacts sur le fonctionnement du serveur et de l’application analysés.

Le chapitre ``` Procédure d’installation ``` liste les étapes obligatoires de l’installation de Telosys Monitoring sur l’environnement cible ainsi que les recommandations.

Compatibilités
===

Les implémentations et les versions supportées de Java sont :
* Implémentation : Oracle Java : 1.6 et +

Les serveurs supportés sont :
* Apache Tomcat : 6.x, 7.x

Principes de Fonctionnement
===
Telosys Monitoring est techniquement un filtre Java EE qui n’impose aucune modification du code de l’application.

Cependant, il est nécessaire que le JAR de Telosys Monitoring soit présent dans le Classpath de cette application et que le fichier ``` web.xml ``` soit modifié pour que le filtre Telosys Monitoring soit pris en compte.

Ceci nécessite que le serveur soit arrêté et redémarré pour que l’ajout du filtre Telosys Monitoring soit pris en compte correctement.

Procédure d’installation
===
Les étapes suivantes décrivent une procédure d’installation ``` standard ``` de Telosys Monitoring. Cette procédure d’installation doit être adaptée et testée sur votre environnement cible.

Voici les étapes d’une installation standard :

1. Arrêt du serveur
---
Le serveur doit être arrêté afin de procéder à l’ajout et à la configuration du filtre Telosys Monitoring.
Actions	Résultats
Arrêter le serveur Apache Tomcat	Le serveur Apache Tomcat est arrêté

2. Ajout du fichier JAR Telosys Monitoring
---
Le JAR telosys-monitoring.jar doit être ajouté dans le classpath du serveur Apache Tomcat.
Actions	Résultats
Ajouter le fichier telosys-monitoring.jar dans le répertoire des librairies du serveur Apache Tomcat (voir les lignes ci-dessous)	Le JAR telosys-monitoring.jar sera chargé lors d’un futur démarrage du serveur Apache Tomcat 
* Ajouter le fichier telosys-monitoring.jar dans le répertoire des librairies du serveur Apache Tomcat. Ce répertoire est situé à la racine du répertoire du serveur Apache Tomcat :
  * Apache Tomcat 7.x : [tomcat_home]/lib
, avec [tomcat_home] : le répertoire racine du serveur Apache Tomcat
 

3. Ajout et configuration du filtre Telosys Monitoring
---
Le filtre Telosys Monitoring doit être ajouté dans l’application Java EE analysé en ajoutant les lignes suivantes dans le fichier web.xml de l’application.
Actions	Résultats
Ajouter la configuration du filtre Telosys Monitoring au début du fichier web.xml de l’application à analyser (voir les lignes ci-dessous)	Le filtre Telosys Monitoring sera chargé  lors d’un futur démarrage de l’application par le serveur Apache Tomcat 
* Ajouter les lignes suivantes dans le fichier web.xml de l’application à analyser :
  * Ces lignes doivent se situer juste après la balise ouvrante <web-app> et avant les déclarations des autres filtres Java EE de l’application via les balises <filter> et <filter-mapping>.
  
Lignes à ajouter dans le fichier web.xml	Description / Obligatoire / Valeur par défaut
<filter>
  <filter-name>Monitor</filter-name>
  <filter-class>
    org.telosys.webtools.monitoring.RequestsMonitor
  </filter-class>	Obligatoire : Oui 

  <init-param>
    <param-name>reporting</param-name>
    <param-value>/monitor</param-value>
  </init-param>	Chemin d’accès dans l’URL pour accéder à la page de rapport de Telosys Monitoring. Attention: ce chemin ne doit pas entrer en conflit avec les URI de l’application analysée
Obligatoire : Non
Par défaut : /monitor
  <init-param>
    <param-name>duration</param-name>
    <param-value>1000</param-value>
  </init-param>	Seuil en millisecondes de détection des requêtes longues 
Obligatoire : Non
Par défaut : 1000
  <init-param>
    <param-name>logsize</param-name>
    <param-value>100</param-value>
  </init-param>	Nombre de requêtes longues stockées dans la log 
Obligatoire : Non
Par défaut : 100
  <init-param>
    <param-name>toptensize</param-name>
    <param-value>10</param-value>
  </init-param>	Nombre de requêtes les plus longues classées par durée stockées dans la log
Obligatoire : Non
Par défaut : 10
  <init-param>
    <param-name>longestsize</param-name>
    <param-value>10</param-value>
  </init-param>	Nombre de requêtes les plus longues classées par URL stockées dans la log
Obligatoire : Non
Par défaut : 10
  <init-param>
    <param-name>activated</param-name>
    <param-value>true</param-value>
  </init-param>	Activer le monitoring au démarrage de l’application
Obligatoire: Non
Par défaut: true (monitoring activé)
Valeurs possibles :
  -   false : monitoring désactivé
  -   true : monitoring activé
</filter>
<filter-mapping>
  <filter-name>Monitor</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>	Obligatoire : Oui

Recommandations :
* Il est conseillé de ne pas stocker trop de requêtes dans les logs afin de ne pas surcharger la mémoire du serveur.
* Il est conseillé de définir un seuil de détection suffisamment grand pour éviter qu’un trop grand nombre de requêtes ne soient traitées par le filtre Telosys Monitoring.
* Il faut vérifier que le chemin d’accès à la page de rapport du monitoring n’entre pas en conflit avec l’une des pages et URI de l’application analysée.
  * Exemple: Si le paramètre ``` reporting ``` a la valeur ``` /monitor ``` dans le fichier ``` web.xml ``` et que l’URL racine de l’application est ``` http://www.application.com/appli ```, alors l’URL d’accès à la page de rapport sera : ``` http://www.application.com/appli/monitor ```.
4. Démarrage du serveur
---
Le serveur doit être arrêté afin de procéder à l’ajout et à la configuration du filtre Telosys Monitoring.
Actions	Résultats
Démarrer le serveur Apache Tomcat	Le serveur Apache Tomcat est démarré.

5. Vérification du fonctionnement de Telosys Monitoring
---
Il est nécessaire de vérifier que le filtre Telosys Monitoring a été chargé et initialisé correctement.
Actions	Résultats
Ouvrir le fichier de log du serveur Apache Tomcat ou de l’application	La ligne suivante doit apparaître dans la log du serveur ou de l’application analysée :
```[TRACE] : MONITOR INITIALIZED.```

Vérifier qu’aucune erreur ou exception liée au filtre Telosys Monitoring n’est présent dans la log.

6. Accès à la page Telosys Monitoring
---
La page de rapport Telosys Monitoring est présentée dans le chapitre suivant.

5. Page de rapport Telosys Monitoring
---

La page de rapport présente les requêtes les plus longues et permet de modifier le paramétrage du monitoring.
URL d’accès

L’accès à la page de rapport de Telosys Monitoring s’effectue via l’URL racine de l’application suivi du chemin spécifié via le paramètre ``` reporting ``` défini dans le fichier web.xml.

*Exemple* :
* Si le paramètre ``` reporting ``` a la valeur ``` /monitor ``` dans le fichier ``` web.xml ``` et que l’URL racine de l’application est ``` http://www.application.com/appli ```, alors l’URL d’accès à la page de rapport sera : ``` http://www.application.com/appli/monitor ```.

Page de rapport
---

La page de rapport se présente de la manière suivante à l’état initial :
 
Cette page est composée d’une barre d’actions, de l’indicateur ``` démarré / arrêté ``` du filtre de monitoring et affiche les informations sur le système, la configuration, l’état du monitoring ainsi que les logs des requêtes.

Informations sur le système
---

Les informations suivantes sont affichées :
* ``` IP address ``` : Adresse IP de la machine
* ``` Hostname ``` : Le nom de la machine
* ``` Java ``` : La version de Java
* ``` OS ``` : La version du système d’exploitation

Configuration
---

La configuration contient les paramètres suivants :
* ``` Duration threshold ``` : il s’agit du seuil de la durée d’exécution à partir de laquelle les requêtes sont capturées dans la log
* ``` Log in memory size ``` : il s’agit du nombre des dernières requêtes conservées dont la durée d’exécution à dépasser le seuil ``` Duration threshold ```
* ``` Top requests by time ``` : le nombre de requêtes conservées dont les temps d’exécution sont les plus longues. Ces requêtes sont classées par le temps d’exécution, ce qui veut dire que l’on peut avoir plusieurs fois la même requête dans cette liste.
* ``` Top requests by URL ``` : le nombre de requêtes conservées dont les temps d’exécution sont les plus longues. Ces requêtes sont classées par URL, ce qui veut dire que nous n’aurons pas deux fois la même requête dans cette liste, et nous ne verrons que le temps d’exécution le plus long pour cette requête.

Etat du monitoring
---

L’état du monitoring présente :
* ``` Initialization data/time ``` : Date et heure lors du premier démarrage du filtre et donc de l’application
* ``` Total requests count ``` : Nombre total de requêtes reçues par l’application
* ``` Long time requests count ``` : Nombre total de requêtes dont le temps d’exécution a dépassé le seuil ``` Duration threshold ```
Logs des requêtes
La page de rapport présente trois logs de requêtes :
* ``` Log in memory size ``` : affiche les dernières requêtes conservées dont la durée d’exécution à dépasser le seuil ``` Duration threshold ```
* ``` Top requests by time ``` : présente les requêtes dont les temps d’exécution sont les plus longues. Ces requêtes sont classées selon le temps d’exécution, ce qui veut dire que l’on peut avoir plusieurs fois la même requête dans cette liste.
* ``` Top requests by URL ``` : affiche les requêtes dont les temps d’exécution sont les plus longues. Ces requêtes sont classées par URL, ce qui veut dire que nous n’aurons pas deux fois la même requête dans cette liste, et nous ne verrons que le temps d’exécution le plus long pour cette requête.
Actions utilisateur
Il est possible de modifier le paramétrage du monitoring sans avoir besoin de redémarrer l’application.
La barre d’actions utilisateur est située sous le titre ``` Telosys Monitoring ``` :
 
Voici les commandes possibles :
* ``` Refresh ``` : effectue un rafraîchissement de la page
* ``` Liste déroulante ``` + ``` Champ texte ``` + ``` Modify ``` : pour modifier un paramètre de la configuration du monitoring
  * il faut sélectionner le paramètre de configuration à modifier, puis à spécifier la nouvelle valeur dans le champ texte et enfin à cliquer sur le bouton ``` Modify ``` pour appliquer la nouvelle valeur pour ce paramètre de configuration.
  * Voici les paramètres de configuration qui peuvent être modifiés :
    * ``` Duration threshold ``` : il s’agit du seuil de la durée d’exécution à partir de laquelle les requêtes sont capturées dans la log
    * ``` Log in memory size ``` : il s’agit du nombre des dernières requêtes conservées dont la durée d’exécution à dépasser le seuil ``` Duration threshold ```
    * ``` Top requests by time ``` : le nombre de requêtes conservées dont les temps d’exécution sont les plus longues. Ces requêtes sont classées par le temps d’exécution, ce qui veut dire que l’on peut avoir plusieurs fois la même requête dans cette liste.
    * ``` Top requests by URL ``` : le nombre de requêtes conservées dont les temps d’exécution sont les plus longues. Ces requêtes sont classées par URL, ce qui veut dire que nous n’aurons pas deux fois la même requête dans cette liste, et nous ne verrons que le temps d’exécution le plus long pour cette requête.
* ``` Clear logs ``` : vider les logs de requêtes
* ``` Reset ``` : réinitialiser les paramètres de configuration à leurs valeurs initiales telles que spécifié dans le fichier ``` web.xml ``` de l’application analysée. Les logs de requêtes sont également vidées
* ``` Stop ``` / ``` Start ``` : respectivement active et désactive le monitoring sur les requêtes. 
  * Si le monitoring est désactivé, le mot ``` [Stopped] ``` apparaît en haut de la page. Ceci indique que les requêtes ne sont plus monitorées pour diminuer la charge du serveur.
 
  * Si le monitoring est activé, le mot ``` [Started] ``` apparaît en haut de la page. Ceci indique que les requêtes sont monitorées et les logs de requêtes sont de nouveau mises à jour. 
 
