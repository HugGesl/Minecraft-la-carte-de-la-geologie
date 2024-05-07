package com.ignfab.minalac.generator;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.*;

import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Envelope;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.geometry.jts.JTS;
import org.geotools.feature.DefaultFeatureCollection;

public class AttributionType {

		/*méthode : getCodeLeg(FileDataStore store) 
		 * objectif : permet d'attribuer au paramètre codeLeg, un sémantic Type,
		 * de manière aléatoire et équitable.*/
	
   
	public SimpleFeatureCollection filterFeatures(SimpleFeatureCollection features, ReferencedEnvelope envelope) {
        // Filtrer les fonctionnalités basées sur l'enveloppe
        SimpleFeatureIterator iterator = features.features();
        try {
            // Créer une collection pour stocker les fonctionnalités filtrées
            DefaultFeatureCollection filteredFeatures = new DefaultFeatureCollection();
            Geometry envelopeGeometry = JTS.toGeometry(envelope);
            
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();

                // Vérifier si la géométrie de la fonctionnalité est à l'intérieur de l'enveloppe
                if (geometry.intersects(envelopeGeometry)) {
                    filteredFeatures.add(feature);
                }
            }

            return filteredFeatures;
        } finally {
            iterator.close();
        }
    }
	
	
	public List<Integer> getCodeLeg(SimpleFeatureCollection features) throws Exception {
        // display a data store file chooser dialog for shapefiles
       
      
        
        List<Integer> code_leg = new ArrayList<>();
        
        SimpleFeatureIterator iterator = features.features();
        
        
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
        Collections.sort(code_leg);
        return code_leg;}
	
    
        public Map<Integer, SemanticType> createCodeLegToSemanticType(List<Integer> uniqueElements) {
        	// Obtenez l'ensemble des types sémantiques de couleur
            Set<SemanticType> colorSemanticTypes = SemanticType.getColorSemanticTypes();

            // Création du dictionnaire pour associer chaque élément de code_leg à son équivalent dans SemanticType
            Map<Integer, SemanticType> codeLegToSemanticType = new HashMap<>();

            // Créer un itérateur sur l'ensemble des types sémantiques de couleur
            Iterator<SemanticType> iterator = colorSemanticTypes.iterator();

            // Itération sur chaque élément unique de code_leg
            for (Integer codeLeg : uniqueElements) {
                // Si l'itérateur a atteint la fin de l'ensemble, revenir au début
                if (!iterator.hasNext()) {
                    iterator = colorSemanticTypes.iterator();
                }
                // Associer l'élément de code_leg avec l'élément suivant de l'ensemble des types sémantiques de couleur
                codeLegToSemanticType.put(codeLeg, iterator.next());
            }

            return codeLegToSemanticType;
        }     
    }


