package com.ignfab.minalac.generator;

import com.ignfab.minalac.Strategies.ContextND;
import com.ignfab.minalac.generator.minetest.MTVoxelWorld;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

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
import java.util.List;

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

    	if (args.length != 3) {
            System.out.println("There must be 3 arguments : directoryFullPath , dataUrl , Method ");}
    	else {
	
	         long startTime = System.currentTimeMillis();
	        
	         //Example : "/home/john/.minetest/worlds/map/"
	         String directoryFullPath = args[0];
	         String dataUrl = args[1];
	    
	
	    	// BBOX has to be a square.
	    	// Width and height have to be one-tenth of the side of the side length of the BBOX's square
	    	// Example "https://data.geopf.fr/wms-r/wms?LAYERS=RGEALTI-MNT_PYR-ZIP_FXX_LAMB93_WMS&FORMAT=image/x-bil;bits=32&SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&STYLES=&CRS=EPSG:2154&BBOX=595000,6335000,605000,6345000&WIDTH=1000&HEIGHT=1000"
	    	
	    	
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
	        double xmin = Double.parseDouble(bboxValues[0]);
	        double ymin = Double.parseDouble(bboxValues[1]);
	        double xmax = Double.parseDouble(bboxValues[2]);
	        double ymax = Double.parseDouble(bboxValues[3]);
	        System.out.println(xmax);
	
	
	        // Get width and height
	        int width = Integer.parseInt(params.get("WIDTH"));
	        int height = Integer.parseInt(params.get("HEIGHT"));
	        
	        System.out.println(height);
	        System.out.println("Done");
	
	
	    	// Load shapefile
	    	FileDataStore store = FileDataStoreFinder.getDataStore(file);
	    	SimpleFeatureSource featureSource = store.getFeatureSource();
	    	SimpleFeatureCollection collection = featureSource.getFeatures();
	    	
	    	CoordinateReferenceSystem CRS = featureSource.getSchema().getCoordinateReferenceSystem();
	    	
	    	// Create ReferencedEnvelope
	    	ReferencedEnvelope bounds = new ReferencedEnvelope(xmin, xmax, ymin, ymax, CRS);
	    	
	    	// Attribution Type sémantique 
	    	AttributionType attributionType = new AttributionType();
	    	SimpleFeatureCollection filteredFeatures = attributionType.filterFeatures(collection, bounds);
	    	
	    	List<Integer> uniqueElements = attributionType.getCodeLeg(filteredFeatures);
	    	Map<Integer, SemanticType> codeLegToSemanticType = attributionType.createCodeLegToSemanticType(uniqueElements);
	
	
	    	// Get attribute name
	    	String attributeName = "CODE_LEG";
	    	
	    	// Grid dimension
	    	Dimension gridDim = new Dimension(width, height);
	    	// Output name for raster data
	    	String output = "MapGeol";
	    	// Progress monitor
	    	NullProgressListener monitor = new NullProgressListener();
	
	    	// Convert vector data to raster
	    	GridCoverage2D sorted = VectorToRasterProcess.process(collection, attributeName, gridDim, bounds, output, monitor);
	
	    	// Create voxel world
	    	VoxelWorld worldMnt = new MTVoxelWorld();
	    	createWorldFromSHP(mntArray, worldMnt, sorted, codeLegToSemanticType);
	    	worldMnt.save(directoryFullPath);
	
	    	// Set spawn point
	        int midPoint = (int) (mntArray.length + Math.sqrt(mntArray.length)) / 2;
	        setStaticSpawnPoint(directoryFullPath, 0, (int) mntArray[midPoint] / 10 + 1, 0);
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
	}


    
    // fonction générant une carte Minetest à partir d'un fichier SHP des couches géologiques vecteurs harmonisées
    private static void createWorldFromSHP(float[] mntArray, VoxelWorld world, GridCoverage2D codeLegGrid, Map<Integer, SemanticType> codeLegToSemanticType) throws OutOfWorldException {

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
            
            
            // x et y équivalent entre la gridcoverage et le MNT
            int xgrid = i % worldLength;
            int ygrid = i / worldLength;
            int CodeLeg = iterator.getSample(xgrid, ygrid, 0);
            SemanticType BlockType = codeLegToSemanticType.get(CodeLeg);
            
           
            
            VoxelType BlockX; 
            // gestion des zones dont l'information géologique est nulle
            if (BlockType == null) {BlockX = stoneVT;}
            else {BlockX = world.getFactory().createVoxelType(BlockType);
            }
            
           
            BlockX.place(x, y, z);
            BlockX.place(x, (y - 1), z);
            BlockX.place(x, (y - 2), z);

        }
    }
    
    private static void createWorldFromMnt(float[] mntArray, VoxelWorld world) throws OutOfWorldException {
        int worldLength = (int) Math.sqrt(mntArray.length);

        int x, y, z;

        VoxelType grassVT = world.getFactory().createVoxelType(SemanticType.Grass);
        VoxelType stoneVT = world.getFactory().createVoxelType(SemanticType.Stone);
        VoxelType dirtVT = world.getFactory().createVoxelType(SemanticType.Dirt);

        for (int i = 0; i < mntArray.length; i++) {
            //Temporary translation so the player spawn at the center of the generated map (player info isn't generated yet on this version)
            x = i % worldLength - worldLength / 2;
            //Ratio between the side length of the BBOX and width/heigth length
            //In this example, we assume that the ratio given by the URL is always 10
            y = (int) mntArray[i];
            z = i / worldLength - worldLength / 2;

            grassVT.place(x, y, z);
            dirtVT.place(x, (y - 1), z);
            dirtVT.place(x, (y - 2), z);
            
            for (int y_stone = y - 3; y_stone > y - (3 + 10); y_stone--) {
                stoneVT.place(x, y_stone, z);
            }
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
    
public void createWorldFromCsv3D(String Fpath,VoxelWorld world, String directoryFullPath) throws OutOfWorldException, MapWriteException, IOException {
    	

    	VoxelType AINF = world.getFactory().createVoxelType(SemanticType.Blue);
        VoxelType CARB = world.getFactory().createVoxelType(SemanticType.Green);
        VoxelType ASUP = world.getFactory().createVoxelType(SemanticType.Cyan);
        VoxelType LATE = world.getFactory().createVoxelType(SemanticType.Orange);
        int E_Late;
        int E_ASUP;
        int E_CARB;
        int E_AINF;
        int y;
        int n = 0;
        int x;
        int z;
    	
        try (Reader reader = new FileReader(Fpath);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';').withIgnoreEmptyLines().withTrim())) {
        	  csvParser.iterator().next();
               for (CSVRecord csvRecord : csvParser) {
            	   x = n % 90;
            	   z = n / 90;
            	   System.out.println(n);    
            	   E_Late = csvRecord.get(5).isEmpty() ? 0 : (int) Double.parseDouble(csvRecord.get(5).replace(',', '.'));
                   E_ASUP = csvRecord.get(8).isEmpty() ? 0 : (int) Double.parseDouble(csvRecord.get(8).replace(',', '.'));
                   E_CARB = csvRecord.get(11).isEmpty() ? 0 : (int) Double.parseDouble(csvRecord.get(11).replace(',', '.'));
                   E_AINF = csvRecord.get(13).isEmpty() ? 0 : (int) Double.parseDouble(csvRecord.get(13).replace(',', '.'));
                   System.out.println(n);   
                
                y = 0;
                for (int i = 0; i < E_AINF; i++) {
                	AINF.place(x,i+y,z);
                }
                y = E_AINF;
               
                for (int i = 0; i < E_CARB; i++) {
                	CARB.place(x,i+y,z);
                }
                y = y + E_CARB;
                
                for (int i = 0; i < E_ASUP; i++) {
                	ASUP.place(x,i+y,z);
                }
                y = y + E_ASUP;
            
                for (int i = 0; i < E_Late; i++) {
                	LATE.place(x,i+y,z);
                }
                n = n+1;
                
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
   
   }

    
}