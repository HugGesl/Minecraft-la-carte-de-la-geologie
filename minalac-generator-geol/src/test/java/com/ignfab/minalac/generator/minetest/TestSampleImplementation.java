package com.ignfab.minalac.generator.minetest;
import com.ignfab.minalac.generator.VoxelWorld;
import com.ignfab.minalac.generator.SampleImplementation;

public class TestSampleImplementation {
	public static void main(String[] args) throws Exception {
		String Fpath = "C:\\Users\\jboli\\Bureau\\ENSG\\projet\\PID\\fluxexemples\\MODELE_3D_CSV.csv";
		String directoryFullPath = "C:\\Users\\jboli\\Bureau\\installer\\minetest-5.8.0-win64\\minetest-5.8.0-win64\\worlds\\MapFrom3D";
		VoxelWorld worldGeo3D = new MTVoxelWorld();
		SampleImplementation test = new SampleImplementation();
		test.createWorldFromCsv3D(Fpath,worldGeo3D);
		
	}
}
