package algorithms.mapreduceclass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import algorithms.mo.ea.CMoChromosome;
import algorithms.mo.prolem.ZDT1;
import algorithms.moead.MOEAD;


public class CaculateZDT1MR {
	ZDT1 zdt1 = new ZDT1();
	
	public void caculate(MoeaData mData,int loopTime) throws IOException
	{
		HdfsOper hdfs = new HdfsOper();
		for (int i = 0; i < loopTime; i++) {
			List<CMoChromosome> chromosomes = new ArrayList<CMoChromosome>();
			List<int[]> neighbourTable = new ArrayList<int[]>();
			List<double[]> weights = new ArrayList<double[]>();
			BufferedReader br = new BufferedReader(hdfs.open("/moead/" + i + "/part-r-00000"));
			String line = new String();
			String str = "";
//			while ((line = br.readLine()) != null) {
//				mData.stringLine2ObjectEnd(chromosomes, neighbourTable,
//						weights, mData.neighboursize,line);
//			}
			if ((line = br.readLine()) != null) {
				mData.stringLine2ObjectEnd(chromosomes, neighbourTable,
						weights, mData.neighboursize,line);
			}
			br.close();
			for (int k = 0; k < chromosomes.size(); k++) {
				str += chromosomes.get(k).objectivesValue[0]
						+ " "
						+ chromosomes.get(k).objectivesValue[1]
						+ "\n";
			}
			mData.write2File("/home/hadoop/experiment/parallel_result/" + i + ".txt", str);
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		MoeaData mData = new MoeaData();
		HdfsOper hdfs = new HdfsOper();
		for (int i = 0; i < 100; i++) {
				List<CMoChromosome> chromosomes = new ArrayList<CMoChromosome>();
				List<int[]> neighbourTable = new ArrayList<int[]>();
				List<double[]> weights = new ArrayList<double[]>();
				
				BufferedReader br = new BufferedReader(hdfs.open("/moead/" + i + "/part-r-00000"));
				String line = new String();
				String str = "";
				if ((line = br.readLine()) != null) {
					mData.stringLine2ObjectEnd(chromosomes, neighbourTable,
							weights,30, line);
				}
				br.close();
				for (int k = 0; k < chromosomes.size(); k++) {
					str += chromosomes.get(k).objectivesValue[0]
							+ " "
							+ chromosomes.get(k).objectivesValue[1]
							+ "\n";
				}
				mData.write2File("/home/hadoop/experiment/parallel_result/" + i + ".txt", str);
			}
		}
}