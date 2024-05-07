package com.ignfab.minalac.generator;

import com.ignfab.minalac.Strategies.ContextND;
import com.ignfab.minalac.Strategies.Init2D;
import com.ignfab.minalac.generator.minetest.MTVoxelWorld;

import it.geosolutions.jaiext.iterators.RandomIterFactory;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.media.jai.iterator.RandomIter;

import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.util.NullProgressListener;
import org.geotools.geometry.Position2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.vector.VectorToRasterProcess;
import org.geotools.swing.data.JFileDataStoreChooser;

/**
 * This is a temporary class to have an idea of how the program works.
 * It generates a Minetest map which is a 3D rendering from a heightmap
 */
public class SampleImplementation {
    public static void main(String[] args) throws Exception {
        

         long startTime = System.currentTimeMillis();
         //Parent directory must exist
         //Example : "/home/john/.minetest/worlds/map/"
         String directoryFullPath = "C:/Users/Moi/Documents/PDI/minetest-5.8.0-win64/worlds/toto";


    	// The function createWorldFromUrl doesn't, for now, handle the parameters.

    	// BBOX has to be a square.
    	// Width and height have to be one-tenth of the side of the side length of the BBOX's square
    	// Example "https://data.geopf.fr/wms-r/wms?LAYERS=RGEALTI-MNT_PYR-ZIP_FXX_LAMB93_WMS&FORMAT=image/x-bil;bits=32&SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&STYLES=&CRS=EPSG:2154&BBOX=595000,6335000,605000,6345000&WIDTH=1000&HEIGHT=1000"
    	String dataUrl = "https://data.geopf.fr/wms-r/wms?LAYERS=RGEALTI-MNT_PYR-ZIP_FXX_LAMB93_WMS&FORMAT=image/x-bil;bits=32&SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&STYLES=&CRS=EPSG:2154&BBOX=886723,6543233,887723,6544233&WIDTH=1000&HEIGHT=1000";
    	
    	
    	float[] mntArray = getHeightMap(dataUrl);
    	
    	// Choose a shapefile
    	File file = JFileDataStoreChooser.showOpenFile("shp", null);
    	if (file == null) {
    	    System.out.println("Aucun fichier sélectionné.");
    	    return;
    	}
    	

    	 // Extract parameters from dataUrl
        Map<String, String> params = getParams(dataUrl);
        

        
        // Get bbox values
        String bboxStr = params.get("BBOX");
        String[] bboxValues = bboxStr.split(",");
        int xmin = Integer.parseInt(bboxValues[0]);
        int ymin = Integer.parseInt(bboxValues[1]);
        int xmax = Integer.parseInt(bboxValues[2]);
        int ymax = Integer.parseInt(bboxValues[3]);


        // Get width and height
        int width = Integer.parseInt(params.get("WIDTH"));
        int height = Integer.parseInt(params.get("HEIGHT"));
        
    	System.out.println(xmin +" "+ xmax +" "+ height);



    	// Load shapefile
    	FileDataStore store = FileDataStoreFinder.getDataStore(file);
    	SimpleFeatureSource featureSource = store.getFeatureSource();
    	SimpleFeatureCollection collection = featureSource.getFeatures();
    	CoordinateReferenceSystem CRS = featureSource.getSchema().getCoordinateReferenceSystem();
    	System.out.println("Shapefile load");

    	// Get unique elements
    	AttributionType attributionType = new AttributionType();
    	Set<Integer> uniqueElements = attributionType.getCodeLeg(store);
    	Map<Integer, SemanticType> codeLegToSemanticType = attributionType.createCodeLegToSemanticType(uniqueElements);


    	// Get attribute name
    	String attributeName = "CODE_LEG";
    	// Create ReferencedEnvelope
    	ReferencedEnvelope bounds = new ReferencedEnvelope(xmin, xmax, ymin, ymax, CRS);
    	// Grid dimension
    	Dimension gridDim = new Dimension(width, height);
    	// Output name for raster data
    	String output = "MapGeol";
    
    	// Progress monitor
    	NullProgressListener monitor = new NullProgressListener();

    	// Convert vector data to raster
    	GridCoverage2D sorted = VectorToRasterProcess.process(collection, attributeName, gridDim, bounds, output, monitor);
    	RenderedImage grid = sorted.getRenderedImage();
    	
    	 System.out.println("Rasterisation complete");
    	 
    	// Create voxel world
    	VoxelWorld worldMnt = new MTVoxelWorld();
    	createWorldFromMnt(mntArray, worldMnt, sorted, codeLegToSemanticType);
    	worldMnt.save(directoryFullPath);

    	// Set spawn point
        int midPoint = (int) (mntArray.length + Math.sqrt(mntArray.length)) / 2;
        setStaticSpawnPoint(directoryFullPath, 0, (int) mntArray[midPoint] / 10 + 1, 0);
        
        //Create minimap
        MinimapPicture mp = new MinimapPicture(xmin, ymin, xmax, ymax, width, height, "2154" , "png");
        mp.saveImage("../output.png");
        
        
        long endTime = System.currentTimeMillis();
     // Calculer la durée écoulée en millisecondes
        long elapsedTime = endTime - startTime;

        // Convertir la durée écoulée en heures, minutes et secondes
        long hours = elapsedTime / (1000 * 60 * 60);
        long minutes = (elapsedTime % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = ((elapsedTime % (1000 * 60 * 60)) % (1000 * 60)) / 1000;

        // Afficher le temps écoulé
        System.out.println("Temps écoulé: " + hours + " heures, " + minutes + " minutes, " + seconds + " secondes");
    }

    

    private static void createWorldFromMnt(float[] mntArray, VoxelWorld world, GridCoverage2D codeLegGrid, Map<Integer, SemanticType> codeLegToSemanticType) throws OutOfWorldException {

        int worldLength = (int) Math.sqrt(mntArray.length);
        

        int x, y, z;
        VoxelType stoneVT = world.getFactory().createVoxelType(SemanticType.Stone);

        Rectangle bounds = new Rectangle(0,0,1000,1000);
        RandomIter iterator = RandomIterFactory.create(codeLegGrid.getRenderedImage(), bounds, true, true);

        System.out.println("Map en cours de creation");
        for (int i = 0; i < mntArray.length; i++) {
            //Temporary translation so the player spawn at the center of the generated map (player info isn't generated yet on this version)
            x = i % worldLength - worldLength / 2;
            //Ratio between the side length of the BBOX and width/heigth length
            //In this example, we assume that the ratio given by the URL is always 10
            y = (int) mntArray[i];
            z = i / worldLength - worldLength / 2;
            
            
            int xgrid = i % worldLength;
            int ygrid = i / worldLength;
            int CodeLeg = iterator.getSample(xgrid, ygrid, 0);
            SemanticType BlockType = codeLegToSemanticType.get(CodeLeg);
            
           
            VoxelType BlockX; 
            if (BlockType == null) {BlockX = stoneVT;}
            else {BlockX = world.getFactory().createVoxelType(BlockType);
            }
            
           
            BlockX.place(x, y, z);
            BlockX.place(x, (y - 1), z);
            BlockX.place(x, (y - 2), z);

        }
    }
    

    

    private static float[] getHeightMap(String urlString) throws MalformedURLException {
        float[] mntArray;
        URL url = new URL(urlString);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n;
            byte[] buffer = new byte[1];
            while (-1 != (n = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, n);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] byteArray = outputStream.toByteArray();
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN);

        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        mntArray = new float[floatBuffer.capacity()];
        floatBuffer.get(mntArray);

        return mntArray;
    }

    private static void setStaticSpawnPoint(String directoryFullPath, int x, int y, int z) throws IOException {
        directoryFullPath = directoryFullPath.endsWith("/") ? directoryFullPath : directoryFullPath + "/";
        System.out.println(directoryFullPath);
        File dir = new File(directoryFullPath + "worldmods/ign_spawn/");
        if (dir.mkdirs()) {
            File luaScript = new File(dir.getAbsolutePath() + "/init.lua");
            luaScript.createNewFile();

            FileWriter fileWriter = new FileWriter(luaScript);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("minetest.setting_set(\"static_spawnpoint\", \"" + x + ", " + y + ", " + z + "\")");
            printWriter.close();
        }
    }
    
    private static Map<String, String> getParams(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);
        String query = url.getQuery();
        String[] params = query.split("&");

        Map<String, String> map = new HashMap<>();

        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }
}