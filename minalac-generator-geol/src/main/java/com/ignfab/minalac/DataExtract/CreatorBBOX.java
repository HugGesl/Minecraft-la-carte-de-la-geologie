package com.ignfab.minalac.DataExtract;

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.ReferencedEnvelope;

public class CreatorBBOX {
	 ReferencedEnvelope envelope;
	 
	 public CreatorBBOX(double minX,double minY,double maxX,double maxY, CoordinateReferenceSystem crs) {
		 ReferencedEnvelope envelope = new ReferencedEnvelope(minX, maxX, minY, maxY, crs);
		 this.envelope =  envelope;
	 }
	 private BBOX GenerateFilter() {
		 FilterFactory ff = org.geotools.factory.CommonFactoryFinder.getFilterFactory();
	     BBOX bboxFilter = ff.bbox(ff.property(""), envelope);
	     return bboxFilter;	
	  
	 }
	 ReferencedEnvelope GetENV() {
		 return this.envelope;
	 }
}
