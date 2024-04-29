package com.ignfab.minalac.DataExtract;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Dimension;

import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.style.Style;
import org.geotools.api.util.InternationalString;
import org.geotools.api.util.ProgressListener;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.util.NullProgressListener;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.process.Process;
import org.geotools.process.ProcessException;
import org.geotools.process.vector.VectorToRasterProcess;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.swing.JMapFrame;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

public class ExtractTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String fname = "C:\\Users\\Moi\\Documents\\PDI\\harmo\\GEO050K_HARM_001_S_FGEOL_2154.shp";
		File file = new File(fname);
		FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();
        SimpleFeatureCollection collection = featureSource.getFeatures();
        CoordinateReferenceSystem CRS = featureSource.getSchema().getCoordinateReferenceSystem();
        // Obtenez le nom de l'attribut
        String attributeName = "CODE_LEG";

        // Créer une enveloppe référencée avec les coordonnées et CRS fournis
        ReferencedEnvelope bounds = new ReferencedEnvelope(704662, 705662, 6584242, 6585242, CRS);
        
        // Dimension de la grille
        Dimension gridDim = new Dimension(1000, 1000);
        
        // Chemin de sortie pour les données raster
        String output = "test";
        
        // Surveillant de progression 
        NullProgressListener monitor = new NullProgressListener();

        System.out.println(attributeName);




        try {
        	GridCoverage2D sorted = VectorToRasterProcess.process(collection,attributeName, gridDim , bounds, output , monitor);
        	System.out.println(sorted.getRenderedImage());
        	System.out.println("Done");

        } catch (ProcessException e) {
            System.err.println("Error rasterizing vector data: " + e.getMessage());
        }
    }

}
