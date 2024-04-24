package com.ignfab.minalac.generator.minetest;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.swing.data.JFileDataStoreChooser;

import com.ignfab.minalac.generator.AttributionType;
import com.ignfab.minalac.generator.SemanticType;

public class TestAttributionType {
    
	public static void main(String[] args) {
        try {
            File file = JFileDataStoreChooser.showOpenFile("shp", null);
            if (file == null) {
                System.out.println("Aucun fichier sélectionné.");
                return;
            }

            FileDataStore store = FileDataStoreFinder.getDataStore(file);
            Set<Integer> uniqueElements = AttributionType.getCodeLeg(store);
            
            Map<Integer, SemanticType> codeLegToSemanticType = AttributionType.createCodeLegToSemanticType(uniqueElements);
            	
            System.out.println(codeLegToSemanticType);
            // Print the dictionary
            for (Map.Entry<Integer, SemanticType> entry : codeLegToSemanticType.entrySet()) {
                System.out.println("Code Leg: " + entry.getKey() + " -> Semantic Type: " + entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
