package algorithms.mapreduceclass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import algorithms.mo.IMultiObjectiveProblem;
import algorithms.mo.ea.CMoChromosome;
import algorithms.mo.prolem.ZDT1;
import algorithms.moead.MOEAD;

public class MoeadMR {
	public static void main(String[] args) throws Exception {

		System.out.println("Test Created");
		MOEAD impl = new MOEAD();

		IMultiObjectiveProblem problem = ZDT1.getInstance(30);
		// IMultiObjectiveProblem problem = ZDT2.getInstance(30);
		System.out.println("Test Solving Started for: " + problem.getName());

		impl.popsize = 300;
		impl.neighboursize = 30;
		impl.TotalItrNum = 300;

		// impl.neighbourTable = new ArrayList<int[]>();
		// impl.solve(problem);
		impl.setMultiObjectiveProblem(problem);
		impl.setNumObjectives(problem.getObjectiveSpaceDimension());
		impl.setNumParameters(problem.getParameterSpaceDimension());
		impl.initialize();

		// System.out.println(impl.mainpop.chromosomes.get(0).toString());
		// System.out.println(impl.mainpop.chromosomes.size());
		// System.chromosomes.out.println(impl.mainpop.chromosomes.get(0).toString());
		// System.out.println(impl.neighbourTable.isEmpty());
		// System.out.println(impl.weights.isEmpty());

		// DataOutputStream dataOutputStream = new DataOutputStream(
		// new FileOutputStream(
		// "/home/hadoop/Desktop/MyJobDir/moead_MR/DataStream.txt"));
		// for (int time = 0; time < 3; time++) {
		// for (int num = 0; num < impl.mainpop.chromosomes.size(); num++) {
		// dataOutputStream.writeBytes(impl.mainpop.chromosomes.get(num)
		// .toString()
		// + ","
		// + impl.weights.get(num)[0]
		// + ","
		// + impl.weights.get(num)[1] + " ");
		// }
		// dataOutputStream.writeBytes("\n");
		// }
		// dataOutputStream.close();

		String in = "hdfs://localhost:9000/user/root/input/moead.txt";
		String out = "hdfs:////localhost:9000/user/root/input/";
//		String in = "/home/hadoop/Desktop/moead_MR/moead.txt";
//		String out = "/home/hadoop/Desktop/moead_MR/";
		MoeaData mData = new MoeaData(impl.neighbourTable, impl.idealpoint,
				impl.weights, impl.mainpop, ZDT1.getInstance(30),
				impl.neighboursize, impl.popsize, impl.F, impl.CR);

		Configuration conf = new Configuration();
		// String[] otherArgs = new GenericOptionsParser(conf, args)
		// .getRemainingArgs();
		// if (otherArgs.length != 2) {
		// System.err.println("Usage: moead <in> <out>");
		// System.exit(2);
		// }

		int time = 4;
		
		String localSrc = "/home/hadoop/Desktop/moead.txt";
//		String localSrc = "hdfs:////localhost:9000/user/root/moead.txt";
		mData.write2File(localSrc, time);
		mData.FileCopy2hdfs(localSrc, in);
		
		
		impl.mainpop = null;
		for (int i = 0; i < 100; i++) {
			if (i > 0) {
				List<CMoChromosome> chromosomes = new ArrayList<CMoChromosome>();
				List<int[]> neighbourTable = new ArrayList<int[]>();
				List<double[]> weights = new ArrayList<double[]>();

				FileReader fr = new FileReader(out + (i - 1) + "/part-r-00000");
				BufferedReader br = new BufferedReader(fr);
				String line = new String();
				// System.out.println("objectivesValue[0]                         objectivesValue[1] ");
				while ((line = br.readLine()) != null) {
					mData.stringLine2Object(chromosomes, neighbourTable,
							weights, line);
					impl.updateReference(impl.idealpoint,
							chromosomes.get(chromosomes.size() - 1));
					// System.out.println(chromosomes.get(chromosomes.size()-1).objectivesValue[0]
					// + " " +
					// chromosomes.get(chromosomes.size()-1).objectivesValue[1]);

				}
				br.close();
				fr.close();

//				String str = "";
//				for (int k = 0; k < chromosomes.size(); k++) {
//					str += chromosomes.get(chromosomes.size() - 1).objectivesValue[0]
//							+ " "
//							+ chromosomes.get(chromosomes.size() - 1).objectivesValue[1]
//							+ "\n";
//				}
//				mData.write2File("/home/hadoop/Desktop/" + "ZDT1" + (i-1), str);

				impl.neighbourTable = new ArrayList<int[]>(neighbourTable);
				impl.weights = new ArrayList<double[]>(weights);
				mData = new MoeaData(impl.neighbourTable, impl.idealpoint,
						impl.weights,
						new ArrayList<CMoChromosome>(chromosomes),
						ZDT1.getInstance(30), impl.neighboursize, impl.popsize,
						impl.F, impl.CR);

				// mData = new MoeaData(impl.neighbourTable, impl.idealpoint,
				// impl.weights, impl.mainpop, ZDT1.getInstance(30),
				// impl.neighboursize, impl.popsize, impl.F, impl.CR);
				// System.out.println("\nrealGenes.length is " +
				// chromosomes.get(0).realGenes.length);
				
				mData.write2File(localSrc, time);
				mData.FileCopy2hdfs(localSrc, in);
			}

			Job job = new Job(conf, "moead" + i);
			job.setJarByClass(MoeadMR.class);
			job.setMapperClass(OtherMapClass.class);
//			job.setCombinerClass(ReduceClass.class);
			job.setReducerClass(ReduceClass.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			FileInputFormat.addInputPath(job, new Path(in));
			FileOutputFormat.setOutputPath(job, new Path(out + i));
			System.out.println("End the " + i + "th job");
			job.waitForCompletion(true);

		}

	}
}
