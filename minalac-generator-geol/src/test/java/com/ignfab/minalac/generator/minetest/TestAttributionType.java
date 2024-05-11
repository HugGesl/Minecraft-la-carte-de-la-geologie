package com.ignfab.minalac.generator.minetest;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.swing.data.JFileDataStoreChooser;

import com.ignfab.minalac.generator.AttributionType;
import com.ignfab.minalac.generator.SemanticType;
import com.ignfab.minalac.generator.VoxelType;
import com.ignfab.minalac.generator.VoxelWorld;
import java.util.*;

public class TestAttributionType {
    
	public static void main(String[] args) {
        try {
        	
        	AttributionType attributionType = new AttributionType();
        	
            File file = JFileDataStoreChooser.showOpenFile("shp", null);
            if (file == null) {
                System.out.println("Aucun fichier sélectionné.");
                return;
            }
            
            FileDataStore store = FileDataStoreFinder.getDataStore(file);
            SimpleFeatureSource featureSource = store.getFeatureSource();
            SimpleFeatureCollection feature = featureSource.getFeatures();
            
            CoordinateReferenceSystem CRS = featureSource.getSchema().getCoordinateReferenceSystem();
            ReferencedEnvelope envelope = new ReferencedEnvelope(878831, 879831, 6557127, 6558127, CRS);
            
            SimpleFeatureCollection filteredFeatures = attributionType.filterFeatures(feature, envelope);
            
            
            List<Integer> uniqueElements = attributionType.getCodeLeg(filteredFeatures);
            System.out.println(uniqueElements);
            
            Map<Integer, SemanticType> codeLegToSemanticType = attributionType.createCodeLegToSemanticType(uniqueElements);
            	
            System.out.println(codeLegToSemanticType);
            
         
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }}

