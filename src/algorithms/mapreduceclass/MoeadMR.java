package algorithms.mapreduceclass;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import algorithms.mo.IMultiObjectiveProblem;
import algorithms.mo.ea.CMoChromosome;
import algorithms.mo.prolem.ZDT1;
import algorithms.moead.MOEAD;

public class MoeadMR {
	public static void main(String[] args) throws Exception {
		long startTime=System.currentTimeMillis();
		System.out.println("Test Created");
		MOEAD impl = new MOEAD();
		HdfsOper hdfs = new HdfsOper();
		hdfs.mkdir("/moead/");
		IMultiObjectiveProblem problem = ZDT1.getInstance(30);
		
		// IMultiObjectiveProblem problem = ZDT2.getInstance(30);
		System.out.println("Test Solving Started for: " + problem.getName());

		impl.popsize = 100;
		impl.neighboursize = 30;
		impl.TotalItrNum = 40;
		
		//the number of time match the slave number will be better
		int time = 4;
		int loopTime = impl.TotalItrNum / time;
		impl.setMultiObjectiveProblem(problem);
		impl.setNumObjectives(problem.getObjectiveSpaceDimension());
		impl.setNumParameters(problem.getParameterSpaceDimension());
		
		impl.initialize();

		String in = "hdfs://localhost:9000/moead/moead.txt";
		String out = "hdfs://localhost:9000/moead/";
		
		long newMD_Time=System.currentTimeMillis();
		MoeaData mData = new MoeaData(impl.neighbourTable, impl.idealpoint,
				impl.weights, impl.mainpop, ZDT1.getInstance(30),
				impl.neighboursize, impl.popsize, impl.F, impl.CR);
		newMD_Time = System.currentTimeMillis() - newMD_Time;
		System.out.println("newMD_Time time is : "+ newMD_Time +"ms");
		Configuration conf = new Configuration();
		
		long writeHF_Time=System.currentTimeMillis();
		mData.write2HdfsFile(in, time);
		writeHF_Time = System.currentTimeMillis() - writeHF_Time;
		System.out.println("writeHF_Time time is : "+ writeHF_Time +"ms");
		long midTime=System.currentTimeMillis();
		System.out.println("initialize time : " + (midTime - startTime));
		impl.mainpop = null;
		
		List<CMoChromosome> chromosomes = new ArrayList<CMoChromosome>();
		List<int[]> neighbourTable = new ArrayList<int[]>();
		List<double[]> weights = new ArrayList<double[]>();
		long[] writeHF_Time_Array = new long[loopTime];
		writeHF_Time_Array[0] = writeHF_Time;
		for (int i = 0; i < loopTime; i++) {
			//hdfs.mkdir("/user/root/input/" + i + "/");
			if (i >= 1) {
				chromosomes.clear();
				neighbourTable.clear();
				weights.clear();
				//FileReader fr = new FileReader(out + (i - 1) + "/part-r-00000");
				BufferedReader br = new BufferedReader(hdfs.open("/moead/" + (i - 1) + "/part-r-00000"));
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
//				mData.setChromosomes(new ArrayList<CMoChromosome>(chromosomes));
				
				mData = new MoeaData(impl.neighbourTable, impl.idealpoint,
						impl.weights,
						new ArrayList<CMoChromosome>(chromosomes),
						ZDT1.getInstance(30), impl.neighboursize, impl.popsize,
						impl.F, impl.CR);
				writeHF_Time = System.currentTimeMillis();
				mData.write2HdfsFile(in, time);
				writeHF_Time = System.currentTimeMillis() - writeHF_Time;
				writeHF_Time_Array[i] = writeHF_Time;
				
			}
//			mapreduce.input.lineinputformat.linespermap
//			conf.setInt("mapreduce.input.lineinputformat.linespermap",1);
			Job job = new Job(conf, "moead");
//			NLineInputFormat.setNumLinesPerSplit(job,1);
			System.out.println("split per map is : " + NLineInputFormat.getNumLinesPerSplit(job));
			
			job.setJarByClass(MoeadMR.class);
			job.setInputFormatClass(NLineInputFormat.class);
			
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
		long endTime=System.currentTimeMillis();
		System.out.println("running time is : "+(endTime-startTime)+"ms");
		long sum = 0;
		for(int t = 0; t < writeHF_Time_Array.length; t ++)
			sum += writeHF_Time_Array[t];
		System.out.println("total write hf time is : "+ sum +" ms"); 
		
		CaculateZDT1MR caculateResult = new CaculateZDT1MR();
		caculateResult.caculate(mData,  loopTime);
	}
}
