package com.ignfab.minalac.generator;

import com.ignfab.minalac.Strategies.ContextND;
import com.ignfab.minalac.Strategies.Init2D;
import com.ignfab.minalac.generator.minetest.MTVoxelWorld;

import it.geosolutions.jaiext.iterators.RandomIterFactory;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Map;
import java.util.Set;

import javax.media.jai.iterator.RandomIter;

import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.util.NullProgressListener;
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
            String directoryFullPath = "C:\\Users\\vmn\\Documents\\ENSG\\2023-2024\\PDI\\minetest-5.8.0-win64\\minetest-5.8.0-win64\\worlds\\minecraft";

            //The function createWorldFromUrl doesn't, for now, handle the parameters.
            //BBOX has to be a square.
            //Width and height have to be one-tenth of the side of the side length of the BBOX's square

            //Example "https://data.geopf.fr/wms-r/wms?LAYERS=RGEALTI-MNT_PYR-ZIP_FXX_LAMB93_WMS&FORMAT=image/x-bil;bits=32&SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&STYLES=&CRS=EPSG:2154&BBOX=595000,6335000,605000,6345000&WIDTH=1000&HEIGHT=1000"
            String dataUrl = "https://data.geopf.fr/wms-r/wms?LAYERS=RGEALTI-MNT_PYR-ZIP_FXX_LAMB93_WMS&FORMAT=image/x-bil;bits=32&SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&STYLES=&CRS=EPSG:2154&BBOX=878831,6557127,880831,6559127&WIDTH=2000&HEIGHT=2000";
            File file = JFileDataStoreChooser.showOpenFile("shp", null);
            if (file == null) {
                System.out.println("Aucun fichier sélectionné.");
                return;
            }
            float[] mntArray = getHeightMap(dataUrl);
            
            
    		FileDataStore store = FileDataStoreFinder.getDataStore(file);
            SimpleFeatureSource featureSource = store.getFeatureSource();
            SimpleFeatureCollection collection = featureSource.getFeatures();
            CoordinateReferenceSystem CRS = featureSource.getSchema().getCoordinateReferenceSystem();
            AttributionType attributionType = new AttributionType();
            Set<Integer> uniqueElements = attributionType.getCodeLeg(store);
            
            Map<Integer, SemanticType> codeLegToSemanticType = attributionType.createCodeLegToSemanticType(uniqueElements);
            
            // Obtenez le nom de l'attribut
            String attributeName = "CODE_LEG";

            // Créer une enveloppe référencée avec les coordonnées et CRS fournis
            ReferencedEnvelope bounds = new ReferencedEnvelope(878831, 880831, 6557127, 6559127, CRS);
            
            // Dimension de la grille
            Dimension gridDim = new Dimension(2000, 2000);
            
            // Chemin de sortie pour les données raster
            String output = "test";
            
            // Surveillant de progression 
            NullProgressListener monitor = new NullProgressListener();

            



            
	    	GridCoverage2D sorted = VectorToRasterProcess.process(collection,attributeName, gridDim , bounds, output , monitor);
	    	System.out.println(sorted.getRenderedImage());
	    	System.out.println("Done");

            
           
            VoxelWorld worldMnt = new MTVoxelWorld();
            createWorldFromMnt(mntArray, worldMnt,sorted, CRS, codeLegToSemanticType); 
            
            worldMnt.save(directoryFullPath);

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
    

    private static void createWorldFromMnt(float[] mntArray, VoxelWorld world, GridCoverage2D CodeLegGrid,CoordinateReferenceSystem CRS, Map<Integer, SemanticType> codeLegToSemanticType) throws OutOfWorldException {
        int worldLength = (int) Math.sqrt(mntArray.length);
        
        System.out.println(worldLength);
        
        int x, y, z;

        VoxelType grassVT = world.getFactory().createVoxelType(SemanticType.Grass);
        VoxelType stoneVT = world.getFactory().createVoxelType(SemanticType.Stone);
        VoxelType dirtVT = world.getFactory().createVoxelType(SemanticType.Dirt);
        
        Rectangle bounds = new Rectangle(0,0,2000,2000);
        RandomIter iterator = RandomIterFactory.create(CodeLegGrid.getRenderedImage(), bounds, true, true);
      
        for (int i = 0; i < mntArray.length; i++) {
            //Temporary translation so the player spawn at the center of the generated map (player info isn't generated yet on this version)
            x = i % worldLength - worldLength / 2;
            //Ratio between the side length of the BBOX and width/heigth length
            //In this example, we assume that the ratio given by the URL is always 10
            y = (int) mntArray[i];
            z = i / worldLength - worldLength / 2;
            
            if (i%123456 == 0) {
            	//afin de connaître l'altitude où se placer quand on arrive sur minetest.
            	System.out.println(x+" "+y+" "+z);
            }
            
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

            for (int y_bis = y - 3; y_bis > y - (3 + 10); y_bis--) {
            	BlockX.place(x, y_bis, z);
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
}