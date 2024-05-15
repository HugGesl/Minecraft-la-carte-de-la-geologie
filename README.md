# Utiliser minalac-generator

## 🛠️ Installer Maven
 Téléchargez Apache Maven en .zip via ce lien
[Download Maven](https://maven.apache.org/download.cgi)

----------

Lancez cette commande pour déziper le dossier et glissez le dans répertoire de votre choix (sauf /Programmesx86)
```bash
unzip apache-maven-3.9.6-bin.zip
```

----------
Pour le reste des manips, suivez cette vidéo 
[Youtube](https://www.youtube.com/watch?v=km3tLti4TCM)
        
        
        
# <p align="center">Génerer une carte</p>
  
Placez vous dans le répertoire /minanalac-generator et lancez cette commande en l'adaptant.

```bash
mvn compile exec:java  -Dexec.mainClass="com.ignfab.minalac.generator.SampleImplementation"  -Dexec.args="directoryFullPath dataUrl method SHPpath"
```
Les différents arguments sont : 
- directoryFullPath : le chemin vers le dossier où vous voulez enregistrez la carte
- dataUrl : le chemin vers le modèle CSV 3D (method = From3D) ou l'URL vers les données MNT de l'IGN
- method : méthode de production de la carte à utiliser FromSHP, FromMNT ou From3D
- SHPpath : Dans le cas ou method = From3D, écrire le chemin vers le fichier SHP à utiliser,sinon écrire null

Lien vers les données certes géologiques vectorisées harmonisées de la BRGM : https://www.geocatalogue.fr/geonetwork/srv/fre/catalog.search#/metadata/94636790-8615-11dc-9e02-0050568151b7

Un exemple de requête vers les données MNT de l'IGN : 

```bash
https://data.geopf.fr/wms-r/wms?LAYERS=RGEALTI-MNT_PYR-ZIP_FXX_LAMB93_WMS&FORMAT=image/x-bil;bits=32&SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&STYLES=&CRS=EPSG:2154&BBOX=970595,6498094,980595,6508094&WIDTH=1000&HEIGHT=1000"
```

----------
# <p align="center">Charger Geotools</p>
Si vous avez télechargé Maven.

Créez un projet Maven dans Eclipse puis suivez le tuto Eclipse de [Geotools](https://docs.geotools.org/latest/userguide/tutorial/quickstart/eclipse.html)


# <p>Problèmes potentiels</p>
  
*Cannot access central (https://repo.maven.apache.org/maven2) in offline mode* 
 
 **Solution** : run `mvn install` dans votre projet maven




        
    
