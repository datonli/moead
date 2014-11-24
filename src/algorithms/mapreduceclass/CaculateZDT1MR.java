package algorithms.mapreduceclass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import algorithms.mo.ea.CMoChromosome;
import algorithms.mo.prolem.ZDT1;


public class CaculateZDT1MR {
	ZDT1 zdt1 = new ZDT1();
	

	public static void main(String[] args) throws IOException {
		MoeaData mData = new MoeaData();
		String out = "/home/hadoop/Desktop/MyJobDir/moead_MR/";
		for (int i = 0; i < 100; i++) {
				List<CMoChromosome> chromosomes = new ArrayList<CMoChromosome>();
				List<int[]> neighbourTable = new ArrayList<int[]>();
				List<double[]> weights = new ArrayList<double[]>();

				FileReader fr = new FileReader(out + i + "/part-r-00000");
				BufferedReader br = new BufferedReader(fr);
				String line = new String();
				String str = "";
				while ((line = br.readLine()) != null) {
					mData.stringLine2Object(chromosomes, neighbourTable,
							weights, line);
				}
				br.close();
				fr.close();
				
				for (int k = 0; k < chromosomes.size(); k++) {
					str += chromosomes.get(k).objectivesValue[0]
							+ " "
							+ chromosomes.get(k).objectivesValue[1]
							+ "\n";
				}
				mData.write2File("/home/hadoop/Desktop/MOP_ZDT1/" + i + ".txt", str);
			}
		}
}