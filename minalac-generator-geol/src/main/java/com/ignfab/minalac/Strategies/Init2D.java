package com.ignfab.minalac.Strategies;

import com.ignfab.minalac.generator.AttributionType;
import java.io.File;
import java.util.Map;
import java.util.Set;

import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;

import com.ignfab.minalac.generator.SemanticType;

public class Init2D implements StrategyND{
	
	
	private void buildMap(String Fname) throws Exception {

		File file = new File(Fname);
		FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();
        CoordinateReferenceSystem CRS = featureSource.getSchema().getCoordinateReferenceSystem();
        
        AttributionType attributionType = new AttributionType();
        Set<Integer> uniqueElements = attributionType.getCodeLeg(store);
        Map<Integer, SemanticType> codeLegToSemanticType = attributionType.createCodeLegToSemanticType(uniqueElements);
       
        
	}

}
