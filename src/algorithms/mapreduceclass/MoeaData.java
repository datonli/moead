package algorithms.mapreduceclass;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Progressable;

import algorithms.mo.IMultiObjectiveProblem;
import algorithms.mo.ea.CMoChromosome;
import algorithms.mo.ea.MoPopulation;
import algorithms.mo.prolem.ZDT1;

public class MoeaData {
	public List<CMoChromosome> chromosomes = new ArrayList<CMoChromosome>();
	public List<int[]> neighbourTable = new ArrayList<int[]>();
	public MoPopulation mainpop;
	public int neighboursize;
	public double[] idealpoint = new double[2];
	public List<double[]> weights = new ArrayList<double[]>();
	public int popsize;
	
	// 好像problem的可以先不管，直接在要用的map里面写定ZDT1这个problem来进行生成
	public IMultiObjectiveProblem problem;

	private double F;
	private double CR;

	public MoeaData() {
	}

	public MoeaData(List<int[]> neighbourTable, double[] idealpoint,
			List<double[]> weights, MoPopulation mainpop, ZDT1 problem,
			int neighboursize, int popsize, double F, double CR) {
		this.neighbourTable = neighbourTable;
		this.CR = CR;
		this.F = F;
		this.mainpop = mainpop;
		this.idealpoint = idealpoint;
		this.neighboursize = neighboursize;
		this.problem = problem;
		this.weights = weights;
		this.popsize = popsize;

	}
	
	public void setChromosomes(List<CMoChromosome> chromosomes)
	{
		this.chromosomes = chromosomes;
	}
	
	/*
	public MoeaData(List<int[]> neighbourTable, double[] idealpoint,
			List<double[]> weights, List<CMoChromosome> chromosomes,
			ZDT1 problem, int neighboursize, int popsize, double F, double CR) {
		this.neighbourTable = neighbourTable;
		this.CR = CR;
		this.F = F;
		this.chromosomes = chromosomes;
		this.idealpoint = idealpoint;
		this.neighboursize = neighboursize;
		this.problem = problem;
		this.weights = weights;
		this.popsize = popsize;
		// this.mainpop.chromosomes = chromosomes;
	}
*/
	public String toStringLine(int i, List<CMoChromosome> chromosomes,
			List<int[]> neighbourTable, List<double[]> weights) {
		String str = "";
		for (int n = 0; n < neighbourTable.get(i).length; n++) {
			str += neighbourTable.get(i)[n] + ",";
		}
		str += chromosomes.get(i).toString() + "," + weights.get(i)[0] + ","
				+ weights.get(i)[1] + " ";
		return str;
	}

	public void stringLine2Object(List<CMoChromosome> chromosomes,
			List<int[]> neighbourTable, List<double[]> weights, String str) {
		String[] info = this.stringSplitLine(str);
		int k = 0;
		int[] neighbourTableIntArray = new int[neighboursize];
		for (int i = 0; i < neighboursize; i++, k++) {
			neighbourTableIntArray[i] = Integer.parseInt(info[i]);
		}
		neighbourTable.add(neighbourTableIntArray);
		CMoChromosome chromosome = new CMoChromosome();
		chromosome.objectivesValue = new double[2];
		chromosome.objectivesEI = new double[2];
		chromosome.estimatedObjectiveDevitation = new double[2];
		chromosome.estimatedObjectiveValue = new double[2];

		chromosome.fitnessValue = Double.parseDouble(info[k]);
		chromosome.objectivesValue[0] = Double.parseDouble(info[k + 1]);
		chromosome.objectivesValue[1] = Double.parseDouble(info[k + 2]);
		chromosome.objectivesEI[0] = Double.parseDouble(info[k + 3]);
		chromosome.objectivesEI[1] = Double.parseDouble(info[k + 4]);
		chromosome.estimatedObjectiveDevitation[0] = Double
				.parseDouble(info[k + 5]);
		chromosome.estimatedObjectiveDevitation[1] = Double
				.parseDouble(info[k + 6]);
		chromosome.estimatedObjectiveValue[0] = Double.parseDouble(info[k + 7]);
		chromosome.estimatedObjectiveValue[1] = Double.parseDouble(info[k + 8]);

		chromosome.realGenes = new double[info.length - k - 11];
		for (int l = 0; l < info.length - k - 11; l++)
			chromosome.realGenes[l] = Double.parseDouble(info[l + k + 9]);
		chromosomes.add(chromosome);

		double[] weight = new double[2];
		weight[0] = Double.parseDouble(info[info.length - 2]);
		weight[1] = Double.parseDouble(info[info.length - 1]);
		
		weights.add(weight);
	}

	public String[] stringSplitLine(String str) {
		String[] numArray;
		int n = 0, m = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ',')
				n++;
		}
		numArray = str.trim().split(",");
		return numArray;
	}

	// 先后顺序为：neighbourTable mainpop weights CR F neighboursize idealpoint
	public String toString() {
		String str = "";
		for (int num = 0; num < this.popsize ; num++) {
//		for (int num = 0; num < this.neighbourTable.size(); num++) {
			for (int n = 0; n < this.neighbourTable.get(num).length; n++) {
				str += this.neighbourTable.get(num)[n] + ",";
			}
			if (this.mainpop != null)
				str += this.mainpop.chromosomes.get(num).toString() + ","
//				str += this.mainpop.toString() + ","
						+ this.weights.get(num)[0] + ","
						+ this.weights.get(num)[1] + " ";
			else
			{
				str += this.chromosomes.get(num).toString() + ","
						+ this.weights.get(num)[0] + ","
						+ this.weights.get(num)[1] + " ";
			}
		}

		str += "_" + this.CR + "," + this.F + "," + this.popsize + ","
				+ this.neighboursize + ",";
		for (int num = 0; num < this.idealpoint.length; num++) {
			str += this.idealpoint[num] + ",";
		}

		return str;
	}

	public boolean write2File(String filename, int time) throws IOException {
		DataOutputStream dataOutputStream = new DataOutputStream(
				new FileOutputStream(filename));
		for (int count = 0; count < time; count++) {
			dataOutputStream.writeBytes(this.toString());
			dataOutputStream.writeBytes("\n");
		}
		dataOutputStream.close();
		return true;
	}
	
	public boolean write2HdfsFile(String filename, int time) throws IOException {
		HdfsOper hdfs = new HdfsOper();
		String moeadPopu = "";
		for (int count = 0; count < time; count++) {
			moeadPopu += this.toString() + "\n";
		}
		hdfs.createFile(filename,moeadPopu);
		return true;
	}

	public boolean write2File(String filename, String str) throws IOException {
		DataOutputStream dataOutputStream = new DataOutputStream(
				new FileOutputStream(filename));
			dataOutputStream.writeBytes(str);
			dataOutputStream.writeBytes("\n");
		dataOutputStream.close();
		return true;
	}
	
//	pay your attention:
//	localSrc is local filesystem's file , and dst is the hdfs's file. 
	public boolean FileCopy2hdfs(String localSrc,String dst) throws IOException{
		InputStream in = new BufferedInputStream(new FileInputStream(localSrc));
		
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dst), conf);
		OutputStream out = fs.create(new Path(dst),new Progressable(){
			public void progress()
			{
				System.out.print(".");
			}
		});
		
		IOUtils.copyBytes(in, out, 4096, true);
		return true;
	}
	
	private String[][] stringSplit(String str) {
		String[] numArray;
		int n = 0, m = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ' ')
				n++;
		}
		numArray = str.trim().split(" ");
		for (int i = 0; i < numArray[0].length(); i++) {
			if (numArray[0].charAt(i) == ',')
				m++;
		}
		String[][] a = new String[n][m + 1];
		for (int i = 0; i < n; i++)
			a[i] = numArray[i].trim().split(",");
		return a;
	}

	private String[] string_Split(String str, char splitChar) {

		int n = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == splitChar)
				n++;
		}
		String[] a = new String[n + 1];
		for (int i = 0; i < n; i++)
			a = str.trim().split(splitChar + "");
		return a;
	}

	// 先后顺序为：neighbourTable mainpop weights____ CR F popsize neighboursize
	// idealpoint
	public void str2MoeaData(String str) {
		String[] strSplit = string_Split(str, '_');
		String[][] info = stringSplit(strSplit[0]);
		String[] strTailSplit = string_Split(strSplit[1], ',');
		this.CR = Double.parseDouble(strTailSplit[0]);
		this.F = Double.parseDouble(strTailSplit[1]);
		this.popsize = Integer.parseInt(strTailSplit[2]);
		this.neighboursize = Integer.parseInt(strTailSplit[3]);
		this.idealpoint[0] = Double.parseDouble(strTailSplit[4]);
		this.idealpoint[1] = Double.parseDouble(strTailSplit[5]);

		// this.mainpop.chromosomes = new ArrayList<MoChromosome>();

		// String[] chromesomeString = new String[info.length];
		for (int i = 0; i < info.length; i++) {
			// chromesomeString = new String[info.length];
			int[] neighbourTableIntArray = new int[this.neighboursize];
			for (int j = 0; j < this.neighboursize; j++) {
				neighbourTableIntArray[j] = Integer.parseInt(info[i][j]);
			}
			neighbourTable.add(neighbourTableIntArray);

			// this.mainpop.getChromosome(i).fitnessValue = Double
			// .parseDouble(info[i][this.popsize]);

			CMoChromosome chromosome = new CMoChromosome();
			chromosome.objectivesValue = new double[2];
			chromosome.objectivesEI = new double[2];
			chromosome.estimatedObjectiveDevitation = new double[2];
			chromosome.estimatedObjectiveValue = new double[2];
			chromosome.fitnessValue = Double
					.parseDouble(info[i][this.neighboursize]);
			chromosome.objectivesValue[0] = Double
					.parseDouble(info[i][this.neighboursize + 1]);
			chromosome.objectivesValue[1] = Double
					.parseDouble(info[i][this.neighboursize + 2]);
			chromosome.objectivesEI[0] = Double
					.parseDouble(info[i][this.neighboursize + 3]);
			chromosome.objectivesEI[1] = Double
					.parseDouble(info[i][this.neighboursize + 4]);
			chromosome.estimatedObjectiveDevitation[0] = Double
					.parseDouble(info[i][this.neighboursize + 5]);
			chromosome.estimatedObjectiveDevitation[1] = Double
					.parseDouble(info[i][this.neighboursize + 6]);
			chromosome.estimatedObjectiveValue[0] = Double
					.parseDouble(info[i][this.neighboursize + 7]);
			chromosome.estimatedObjectiveValue[1] = Double
					.parseDouble(info[i][this.neighboursize + 8]);

			chromosome.realGenes = new double[info[i].length
					- this.neighboursize - 11];
			for (int j = 0; j < info[i].length - this.neighboursize - 11; j++)
				chromosome.realGenes[j] = Double.parseDouble(info[i][j
						+ this.neighboursize + 9]);
			this.chromosomes.add(chromosome);

			double[] weight = new double[2];

			weight[0] = Double.parseDouble(info[i][info[i].length - 2]);
			weight[1] = Double.parseDouble(info[i][info[i].length - 1]);

			this.weights.add(weight);
		}
	}

	public List<int[]> getNeighbourTable() {
		return neighbourTable;
	}

	public void setNeighbourTable(List<int[]> neighbourTable) {
		this.neighbourTable = neighbourTable;
	}

	public MoPopulation getMainpop() {
		return mainpop;
	}

	public void setMainpop(MoPopulation mainpop) {
		this.mainpop = mainpop;
	}

	public int getNeighboursize() {
		return neighboursize;
	}

	public void setNeighboursize(int neighboursize) {
		this.neighboursize = neighboursize;
	}

	public double[] getIdealpoint() {
		return idealpoint;
	}

	public void setIdealpoint(double[] idealpoint) {
		this.idealpoint = idealpoint;
	}

	public List<double[]> getWeights() {
		return weights;
	}

	public void setWeights(List<double[]> weights) {
		this.weights = weights;
	}

	public IMultiObjectiveProblem getProblem() {
		return problem;
	}

	public void setProblem(IMultiObjectiveProblem problem) {
		this.problem = problem;
	}

	public double getF() {
		return F;
	}

	public void setF(double f) {
		F = f;
	}

	public double getCR() {
		return CR;
	}

	public void setCR(double cR) {
		CR = cR;
	}

}
