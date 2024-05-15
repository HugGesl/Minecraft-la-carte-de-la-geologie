package com.ignfab.minalac.generator;

import java.awt.image.BufferedImage;
import java.io.File;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import javax.imageio.ImageIO;

import java.net.URL;

class MinimapPicture {
	
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;
	private int width;
	private int height;
	private String epsg;
	private String format;
	
	public MinimapPicture(double minX, double minY, double maxX, double maxY, int width, int height, String epsg, String format) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.width = width;
        this.height = height;
        this.epsg = epsg;
        this.format = format;
    }
	
	//requête pour récupérer la vignette
	private static String buildGeologieRequest(double minX, double minY, double maxX, double maxY, int width, int height, String epsg, String format) {
        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append("https://geoservices.brgm.fr/geologie?");
        requestBuilder.append("SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&TRANSPARENT=TRUE&LAYERS=GEOLOGIE&");

        try {

            String bbox = URLEncoder.encode(minX + "," + minY + "," + maxX + "," + maxY, "UTF-8");
            requestBuilder.append("BBOX=").append(bbox).append("&");


            requestBuilder.append("WIDTH=").append(width).append("&");
            requestBuilder.append("HEIGHT=").append(height).append("&");


            requestBuilder.append("SRS=EPSG%3A").append(epsg).append("&");
            

            requestBuilder.append("FORMAT=image%2F").append(format);
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return requestBuilder.toString();
    }
	
	//sauvegarde l'image
	public void saveImage(String filePath) throws IOException {
        String requestUrl = buildGeologieRequest(minX, minY, maxX, maxY, width, height, epsg, format);

        URL url = new URL(requestUrl);
        BufferedImage image = ImageIO.read(url);


        File outputFile = new File(filePath + "/vignette_map." + format);
        outputFile.getParentFile().mkdirs();

        ImageIO.write(image, format, outputFile);
    }

}

