package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import algorithms.mapreduceclass.MoeaData;
import algorithms.mo.ea.CMoChromosome;

public class ReadFileLine {
	

	
	public static void main(String[] args) throws IOException {
		
		List<CMoChromosome> chromosomes = new ArrayList<CMoChromosome>();
		List<int[]> neighbourTable = new ArrayList<int[]>();
		List<double[]> weights = new ArrayList<double[]>();
		
		FileReader fr = new FileReader("/home/hadoop/Desktop/MyJobDir/moead_MR/out/part-r-00000");
		BufferedReader br =new BufferedReader(fr);
		String line = new String();
//		String[] a = null;
		MoeaData mData = new MoeaData();
		while((line = br.readLine()) != null)
		{
			mData.stringLine2Object(chromosomes, neighbourTable, weights, line);
			System.out.println("fitnessValue is :\n" + chromosomes.get(chromosomes.size() - 1).fitnessValue);
		}
		br.close();
		fr.close();
	}

}
