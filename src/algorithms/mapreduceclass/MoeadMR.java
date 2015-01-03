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

		impl.setMultiObjectiveProblem(problem);
		impl.setNumObjectives(problem.getObjectiveSpaceDimension());
		impl.setNumParameters(problem.getParameterSpaceDimension());
		impl.initialize();


		String in = "hdfs://localhost:9000/user/root/input/moead.txt";
		String out = "hdfs://localhost:9000/user/root/input/";
		MoeaData mData = new MoeaData(impl.neighbourTable, impl.idealpoint,
				impl.weights, impl.mainpop, ZDT1.getInstance(30),
				impl.neighboursize, impl.popsize, impl.F, impl.CR);

		Configuration conf = new Configuration();

		int time = 4;
		mData.write2HdfsFile(in, time);
		impl.mainpop = null;
		HdfsOper hdfs = new HdfsOper();
		for (int i = 0; i < 100; i++) {
			//hdfs.mkdir("/user/root/input/" + i + "/");
			if (i > 0) {
				List<CMoChromosome> chromosomes = new ArrayList<CMoChromosome>();
				List<int[]> neighbourTable = new ArrayList<int[]>();
				List<double[]> weights = new ArrayList<double[]>();
				//FileReader fr = new FileReader(out + (i - 1) + "/part-r-00000");
				BufferedReader br = new BufferedReader(hdfs.open("/user/root/input/" + (i - 1) + "/part-r-00000"));
				String line = new String();
				while ((line = br.readLine()) != null) {
					mData.stringLine2Object(chromosomes, neighbourTable,
							weights, line);
					impl.updateReference(impl.idealpoint,
							chromosomes.get(chromosomes.size() - 1));
				}
				br.close();
				//fr.close();


				impl.neighbourTable = new ArrayList<int[]>(neighbourTable);
				impl.weights = new ArrayList<double[]>(weights);
				mData = new MoeaData(impl.neighbourTable, impl.idealpoint,
						impl.weights,
						new ArrayList<CMoChromosome>(chromosomes),
						ZDT1.getInstance(30), impl.neighboursize, impl.popsize,
						impl.F, impl.CR);

				mData.write2HdfsFile(in, time);
			}

			Job job = new Job(conf, "moead" + i);
			job.setJarByClass(MoeadMR.class);
			job.setMapperClass(OtherMapClass.class);
//			job.setCombinerClass(ReduceClass.class);
			job.setReducerClass(ReduceClass.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			FileInputFormat.addInputPath(job, new Path(in));
			FileOutputFormat.setOutputPath(job, new Path(out + i + "/"));
			System.out.println("End the " + i + "th job");
			job.waitForCompletion(true);

		}

	}
}
