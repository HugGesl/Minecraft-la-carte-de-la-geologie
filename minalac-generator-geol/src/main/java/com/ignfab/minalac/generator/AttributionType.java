package com.ignfab.minalac.generator;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.*;


import org.geotools.api.data.FileDataStore;

import org.geotools.api.data.SimpleFeatureSource;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.api.feature.simple.SimpleFeature;


public class AttributionType {

		/*méthode : getCodeLeg(FileDataStore store) 
		 * objectif : permet d'attribuer au paramètre codeLeg, un sémantic Type,
		 * de manière aléatoire et équitable.*/
	
    public Set<Integer> getCodeLeg(FileDataStore store) throws Exception {
        // display a data store file chooser dialog for shapefiles
       
        SimpleFeatureSource featureSource = store.getFeatureSource();
        SimpleFeatureCollection featureCollection = featureSource.getFeatures();
        
        List<Integer> code_leg = new ArrayList<>();
        
        SimpleFeatureIterator iterator = featureCollection.features();
        
        
        try {
            while( iterator.hasNext() ){
                 SimpleFeature feature = iterator.next();
                 List<Object> attributes = feature.getAttributes();
                 if (attributes.size() > 4 && attributes.get(4) instanceof Integer) {
                     Integer fifthElement = (Integer) attributes.get(4);
                     code_leg.add(fifthElement);
                 }
              }
        }
        finally {
            iterator.close();
        }
        return new HashSet<>(code_leg);}
    
        public Map<Integer, SemanticType> createCodeLegToSemanticType(Set<Integer> uniqueElements) {
            // Calcul du nombre de paquets nécessaire pour couvrir toutes les valeurs de code_leg avec les éléments de SemanticType
            int packetsCount = (int) Math.ceil((double) uniqueElements.size() / SemanticType.values().length - 5);
            
            // Création du dictionnaire pour associer chaque élément de code_leg à son équivalent dans SemanticType
            Map<Integer, SemanticType> codeLegToSemanticType = new HashMap<>();
            
            // Index pour parcourir les éléments de SemanticType
            int semanticTypeIndex = 0;
            
            // Itération sur chaque élément unique de code_leg
            for (Integer codeLeg : uniqueElements) {
                // Associer l'élément de code_leg avec l'élément correspondant de SemanticType
            	if (semanticTypeIndex >= 5) {
                    codeLegToSemanticType.put(codeLeg, SemanticType.values()[semanticTypeIndex]);
                } else {
                    // Ignorer les cinq premiers éléments de SemanticType
                    codeLegToSemanticType.put(codeLeg, SemanticType.values()[semanticTypeIndex + 5]);
                }
                
                // Passer au paquet suivant si nécessaire
                if (++semanticTypeIndex >= SemanticType.values().length) {
                    // Si l'index dépasse la taille de l'énumération, revenir au début de l'énumération à partir du sixième élément
                    semanticTypeIndex = 5;
                }
                
            }
            
            return codeLegToSemanticType;
        }      
    }


