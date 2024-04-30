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
mvn compile exec:java  -Dexec.mainClass="com.ignfab.minalac.generator.SampleImplementation"  -Dexec.args="RACINE\minetest-5.8.0-win64\worlds\NOM_MAP  https://data.geopf.fr/wms-r/wms?LAYERS=RGEALTI-MNT_PYR-ZIP_FXX_LAMB93_WMS&FORMAT=image/x-bil;bits=32&SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&STYLES=&CRS=EPSG:2154&BBOX=970595,6498094,980595,6508094&WIDTH=1000&HEIGHT=1000"
```
----------
# <p align="center">Charger Geotools</p>
Si vous avez télechargé Maven.

Créez un projet Maven dans Eclipse puis suivez le tuto Eclipse de [Geotools](https://docs.geotools.org/latest/userguide/tutorial/quickstart/eclipse.html)


# <p>Problèmes potentiels</p>
  
*Cannot access central (https://repo.maven.apache.org/maven2) in offline mode* 
 
 **Solution** : run `mvn install` dans votre projet maven




        
    
