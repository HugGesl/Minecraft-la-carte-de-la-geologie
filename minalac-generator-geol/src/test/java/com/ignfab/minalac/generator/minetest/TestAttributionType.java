package com.ignfab.minalac.generator.minetest;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
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
            
            Set<Integer> uniqueElements = attributionType.getCodeLeg(store);
            System.out.println(uniqueElements);
            Map<Integer, SemanticType> codeLegToSemanticType = attributionType.createCodeLegToSemanticType(uniqueElements);
            	
            System.out.println(codeLegToSemanticType);
            // Print the dictionary
            for (Map.Entry<Integer, SemanticType> entry : codeLegToSemanticType.entrySet()) {
                System.out.println("Code Leg: " + entry.getKey() + " -> Semantic Type: " + entry.getValue());
                
            }
         
            int id = 200	;
            System.out.println( codeLegToSemanticType.get(id));
            int id2 = 0	;
            System.out.println( codeLegToSemanticType.get(id2));
            SemanticType ST = codeLegToSemanticType.get(id);
            VoxelWorld world = new MTVoxelWorld();
            VoxelType BlockX = world.getFactory().createVoxelType(ST);
            System.out.println(BlockX);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
