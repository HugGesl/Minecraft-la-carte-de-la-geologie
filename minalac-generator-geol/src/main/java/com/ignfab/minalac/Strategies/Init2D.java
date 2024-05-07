package com.ignfab.minalac.Strategies;

import com.ignfab.minalac.generator.AttributionType;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.simple.SimpleFeatureCollection;

import com.ignfab.minalac.generator.SemanticType;

public class Init2D implements StrategyND{
	
	
	private void buildMap(String Fname) throws Exception {

		File file = new File(Fname);
		FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();
        SimpleFeatureCollection collection = featureSource.getFeatures();
        CoordinateReferenceSystem CRS = featureSource.getSchema().getCoordinateReferenceSystem();
        
        AttributionType attributionType = new AttributionType();
        List<Integer> uniqueElements = attributionType.getCodeLeg(collection);
        Map<Integer, SemanticType> codeLegToSemanticType = attributionType.createCodeLegToSemanticType(uniqueElements);
       
        
	}

}
