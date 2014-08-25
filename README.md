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
1.	Pr�sentation	3
2.	Compatibilit�s	4
3.	Principes de Fonctionnement	5
4.	Proc�dure d�installation	6


Pr�sentation
===
Telosys Monitoring analyse le temps d�ex�cution des requ�tes HTTP sur un serveur Apache Tomcat.
L�objectif est de d�tecter les requ�tes dont la dur�e d�ex�cution d�passe un seuil d�fini et de lister ces requ�tes via un rapport accessible depuis l�application d�ploy�e.

Recommandations
===
Pour garantir le fonctionnement des serveurs et des applications analys�es, il est n�cessaire d��tablir et de valider la proc�dure d�installation et de configuration du filtre Telosys Monitoring sur un environnement de qualification ou de pr�-production et non directement sur l�environnement de production.

Il est n�cessaire que l��quipe en charge de l�exploitation des environnements de production ait la connaissance technique des impacts de l�outil Telosys Monitoring sur le fonctionnement des serveurs et des applications analys�es par Telosys Monitoring. La connaissance de la proc�dure d�installation et de ses recommandations est �galement obligatoire.

Le chapitre ``` Compatibilit�s ``` indique les produits compatibles avec Telosys Monitoring.

Le chapitre ``` Principes de fonctionnement ``` d�crit le fonctionnement interne de Telosys Monitoring et ses impacts sur le fonctionnement du serveur et de l�application analys�s.

Le chapitre ``` Proc�dure d�installation ``` liste les �tapes obligatoires de l�installation de Telosys Monitoring sur l�environnement cible ainsi que les recommandations.

Compatibilit�s
===

Les impl�mentations et les versions support�es de Java sont :
* Impl�mentation : Oracle Java : 1.6 et +

Les serveurs support�s sont :
* Apache Tomcat : 6.x, 7.x

Principes de Fonctionnement
===
Telosys Monitoring est techniquement un filtre Java EE qui n�impose aucune modification du code de l�application.

Cependant, il est n�cessaire que le JAR de Telosys Monitoring soit pr�sent dans le Classpath de cette application et que le fichier ``` web.xml ``` soit modifi� pour que le filtre Telosys Monitoring soit pris en compte.

Ceci n�cessite que le serveur soit arr�t� et red�marr� pour que l�ajout du filtre Telosys Monitoring soit pris en compte correctement.

Proc�dure d�installation
===
Les �tapes suivantes d�crivent une proc�dure d�installation ``` standard ``` de Telosys Monitoring. Cette proc�dure d�installation doit �tre adapt�e et test�e sur votre environnement cible.

Voici les �tapes d�une installation standard :

1. Arr�t du serveur
---
Le serveur doit �tre arr�t� afin de proc�der � l�ajout et � la configuration du filtre Telosys Monitoring.
Actions	R�sultats
Arr�ter le serveur Apache Tomcat	Le serveur Apache Tomcat est arr�t�

2. Ajout du fichier JAR Telosys Monitoring
---
Le JAR telosys-monitoring.jar doit �tre ajout� dans le classpath du serveur Apache Tomcat.
Actions	R�sultats
Ajouter le fichier telosys-monitoring.jar dans le r�pertoire des librairies du serveur Apache Tomcat (voir les lignes ci-dessous)	Le JAR telosys-monitoring.jar sera charg� lors d�un futur d�marrage du serveur Apache Tomcat 
* Ajouter le fichier telosys-monitoring.jar dans le r�pertoire des librairies du serveur Apache Tomcat. Ce r�pertoire est situ� � la racine du r�pertoire du serveur Apache Tomcat :
  * Apache Tomcat 7.x : [tomcat_home]/lib
, avec [tomcat_home] : le r�pertoire racine du serveur Apache Tomcat
 

3. Ajout et configuration du filtre Telosys Monitoring
---
Le filtre Telosys Monitoring doit �tre ajout� dans l�application Java EE analys� en ajoutant les lignes suivantes dans le fichier web.xml de l�application.
Actions	R�sultats
Ajouter la configuration du filtre Telosys Monitoring au d�but du fichier web.xml de l�application � analyser (voir les lignes ci-dessous)	Le filtre Telosys Monitoring sera charg�  lors d�un futur d�marrage de l�application par le serveur Apache Tomcat 
* Ajouter les lignes suivantes dans le fichier web.xml de l�application � analyser :
  * Ces lignes doivent se situer juste apr�s la balise ouvrante <web-app> et avant les d�clarations des autres filtres Java EE de l�application via les balises <filter> et <filter-mapping>.
  
Lignes � ajouter dans le fichier web.xml	Description / Obligatoire / Valeur par d�faut
<filter>
  <filter-name>Monitor</filter-name>
  <filter-class>
    org.telosys.webtools.monitoring.RequestsMonitor
  </filter-class>	Obligatoire : Oui 

  <init-param>
    <param-name>reporting</param-name>
    <param-value>/monitor</param-value>
  </init-param>	Chemin d�acc�s dans l�URL pour acc�der � la page de rapport de Telosys Monitoring. Attention: ce chemin ne doit pas entrer en conflit avec les URI de l�application analys�e
Obligatoire : Non
Par d�faut : /monitor
  <init-param>
    <param-name>duration</param-name>
    <param-value>1000</param-value>
  </init-param>	Seuil en millisecondes de d�tection des requ�tes longues 
Obligatoire : Non
Par d�faut : 1000
  <init-param>
    <param-name>logsize</param-name>
    <param-value>100</param-value>
  </init-param>	Nombre de requ�tes longues stock�es dans la log 
Obligatoire : Non
Par d�faut : 100
  <init-param>
    <param-name>toptensize</param-name>
    <param-value>10</param-value>
  </init-param>	Nombre de requ�tes les plus longues class�es par dur�e stock�es dans la log
Obligatoire : Non
Par d�faut : 10
  <init-param>
    <param-name>longestsize</param-name>
    <param-value>10</param-value>
  </init-param>	Nombre de requ�tes les plus longues class�es par URL stock�es dans la log
Obligatoire : Non
Par d�faut : 10
  <init-param>
    <param-name>activated</param-name>
    <param-value>true</param-value>
  </init-param>	Activer le monitoring au d�marrage de l�application
Obligatoire: Non
Par d�faut: true (monitoring activ�)
Valeurs possibles :
  -   false : monitoring d�sactiv�
  -   true : monitoring activ�
</filter>
<filter-mapping>
  <filter-name>Monitor</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>	Obligatoire : Oui

Recommandations :
* Il est conseill� de ne pas stocker trop de requ�tes dans les logs afin de ne pas surcharger la m�moire du serveur.
* Il est conseill� de d�finir un seuil de d�tection suffisamment grand pour �viter qu�un trop grand nombre de requ�tes ne soient trait�es par le filtre Telosys Monitoring.
* Il faut v�rifier que le chemin d�acc�s � la page de rapport du monitoring n�entre pas en conflit avec l�une des pages et URI de l�application analys�e.
  * Exemple: Si le param�tre ``` reporting ``` a la valeur ``` /monitor ``` dans le fichier ``` web.xml ``` et que l�URL racine de l�application est ``` http://www.application.com/appli ```, alors l�URL d�acc�s � la page de rapport sera : ``` http://www.application.com/appli/monitor ```.
4. D�marrage du serveur
---
Le serveur doit �tre arr�t� afin de proc�der � l�ajout et � la configuration du filtre Telosys Monitoring.
Actions	R�sultats
D�marrer le serveur Apache Tomcat	Le serveur Apache Tomcat est d�marr�.

5. V�rification du fonctionnement de Telosys Monitoring
---
Il est n�cessaire de v�rifier que le filtre Telosys Monitoring a �t� charg� et initialis� correctement.
Actions	R�sultats
Ouvrir le fichier de log du serveur Apache Tomcat ou de l�application	La ligne suivante doit appara�tre dans la log du serveur ou de l�application analys�e :
```[TRACE] : MONITOR INITIALIZED.```

V�rifier qu�aucune erreur ou exception li�e au filtre Telosys Monitoring n�est pr�sent dans la log.

6. Acc�s � la page Telosys Monitoring
---
La page de rapport Telosys Monitoring est pr�sent�e dans le chapitre suivant.

5. Page de rapport Telosys Monitoring
---

La page de rapport pr�sente les requ�tes les plus longues et permet de modifier le param�trage du monitoring.
URL d�acc�s

L�acc�s � la page de rapport de Telosys Monitoring s�effectue via l�URL racine de l�application suivi du chemin sp�cifi� via le param�tre ``` reporting ``` d�fini dans le fichier web.xml.

*Exemple* :
* Si le param�tre ``` reporting ``` a la valeur ``` /monitor ``` dans le fichier ``` web.xml ``` et que l�URL racine de l�application est ``` http://www.application.com/appli ```, alors l�URL d�acc�s � la page de rapport sera : ``` http://www.application.com/appli/monitor ```.

Page de rapport
---

La page de rapport se pr�sente de la mani�re suivante � l��tat initial :
 
Cette page est compos�e d�une barre d�actions, de l�indicateur ``` d�marr� / arr�t� ``` du filtre de monitoring et affiche les informations sur le syst�me, la configuration, l��tat du monitoring ainsi que les logs des requ�tes.

Informations sur le syst�me
---

Les informations suivantes sont affich�es :
* ``` IP address ``` : Adresse IP de la machine
* ``` Hostname ``` : Le nom de la machine
* ``` Java ``` : La version de Java
* ``` OS ``` : La version du syst�me d�exploitation

Configuration
---

La configuration contient les param�tres suivants :
* ``` Duration threshold ``` : il s�agit du seuil de la dur�e d�ex�cution � partir de laquelle les requ�tes sont captur�es dans la log
* ``` Log in memory size ``` : il s�agit du nombre des derni�res requ�tes conserv�es dont la dur�e d�ex�cution � d�passer le seuil ``` Duration threshold ```
* ``` Top requests by time ``` : le nombre de requ�tes conserv�es dont les temps d�ex�cution sont les plus longues. Ces requ�tes sont class�es par le temps d�ex�cution, ce qui veut dire que l�on peut avoir plusieurs fois la m�me requ�te dans cette liste.
* ``` Top requests by URL ``` : le nombre de requ�tes conserv�es dont les temps d�ex�cution sont les plus longues. Ces requ�tes sont class�es par URL, ce qui veut dire que nous n�aurons pas deux fois la m�me requ�te dans cette liste, et nous ne verrons que le temps d�ex�cution le plus long pour cette requ�te.

Etat du monitoring
---

L��tat du monitoring pr�sente :
* ``` Initialization data/time ``` : Date et heure lors du premier d�marrage du filtre et donc de l�application
* ``` Total requests count ``` : Nombre total de requ�tes re�ues par l�application
* ``` Long time requests count ``` : Nombre total de requ�tes dont le temps d�ex�cution a d�pass� le seuil ``` Duration threshold ```
Logs des requ�tes
La page de rapport pr�sente trois logs de requ�tes :
* ``` Log in memory size ``` : affiche les derni�res requ�tes conserv�es dont la dur�e d�ex�cution � d�passer le seuil ``` Duration threshold ```
* ``` Top requests by time ``` : pr�sente les requ�tes dont les temps d�ex�cution sont les plus longues. Ces requ�tes sont class�es selon le temps d�ex�cution, ce qui veut dire que l�on peut avoir plusieurs fois la m�me requ�te dans cette liste.
* ``` Top requests by URL ``` : affiche les requ�tes dont les temps d�ex�cution sont les plus longues. Ces requ�tes sont class�es par URL, ce qui veut dire que nous n�aurons pas deux fois la m�me requ�te dans cette liste, et nous ne verrons que le temps d�ex�cution le plus long pour cette requ�te.
Actions utilisateur
Il est possible de modifier le param�trage du monitoring sans avoir besoin de red�marrer l�application.
La barre d�actions utilisateur est situ�e sous le titre ``` Telosys Monitoring ``` :
 
Voici les commandes possibles :
* ``` Refresh ``` : effectue un rafra�chissement de la page
* ``` Liste d�roulante ``` + ``` Champ texte ``` + ``` Modify ``` : pour modifier un param�tre de la configuration du monitoring
  * il faut s�lectionner le param�tre de configuration � modifier, puis � sp�cifier la nouvelle valeur dans le champ texte et enfin � cliquer sur le bouton ``` Modify ``` pour appliquer la nouvelle valeur pour ce param�tre de configuration.
  * Voici les param�tres de configuration qui peuvent �tre modifi�s :
    * ``` Duration threshold ``` : il s�agit du seuil de la dur�e d�ex�cution � partir de laquelle les requ�tes sont captur�es dans la log
    * ``` Log in memory size ``` : il s�agit du nombre des derni�res requ�tes conserv�es dont la dur�e d�ex�cution � d�passer le seuil ``` Duration threshold ```
    * ``` Top requests by time ``` : le nombre de requ�tes conserv�es dont les temps d�ex�cution sont les plus longues. Ces requ�tes sont class�es par le temps d�ex�cution, ce qui veut dire que l�on peut avoir plusieurs fois la m�me requ�te dans cette liste.
    * ``` Top requests by URL ``` : le nombre de requ�tes conserv�es dont les temps d�ex�cution sont les plus longues. Ces requ�tes sont class�es par URL, ce qui veut dire que nous n�aurons pas deux fois la m�me requ�te dans cette liste, et nous ne verrons que le temps d�ex�cution le plus long pour cette requ�te.
* ``` Clear logs ``` : vider les logs de requ�tes
* ``` Reset ``` : r�initialiser les param�tres de configuration � leurs valeurs initiales telles que sp�cifi� dans le fichier ``` web.xml ``` de l�application analys�e. Les logs de requ�tes sont �galement vid�es
* ``` Stop ``` / ``` Start ``` : respectivement active et d�sactive le monitoring sur les requ�tes. 
  * Si le monitoring est d�sactiv�, le mot ``` [Stopped] ``` appara�t en haut de la page. Ceci indique que les requ�tes ne sont plus monitor�es pour diminuer la charge du serveur.
 
  * Si le monitoring est activ�, le mot ``` [Started] ``` appara�t en haut de la page. Ceci indique que les requ�tes sont monitor�es et les logs de requ�tes sont de nouveau mises � jour. 
 
