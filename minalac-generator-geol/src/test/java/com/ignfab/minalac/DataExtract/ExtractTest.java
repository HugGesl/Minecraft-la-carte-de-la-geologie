package com.ignfab.minalac.DataExtract;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.style.Style;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.swing.JMapFrame;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;


public class ExtractTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String fname = "C:\\Users\\jboli\\Bureau\\ENSG\\projet\\PID\\fluxexemples\\GEO050K_HARM_003\\GEO050K_HARM_003_S_FGEOL_2154.shp";
		File file = new File(fname);
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();
		CoordinateReferenceSystem CRS = featureSource.getSchema().getCoordinateReferenceSystem();
		CreatorBBOX bbox = new CreatorBBOX(704662,705662,6584242,6585242,CRS);
		System.out.println("Done");
	
       

	}

}
