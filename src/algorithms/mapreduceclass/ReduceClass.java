package algorithms.mapreduceclass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import algorithms.mo.ea.CMoChromosome;
import algorithms.moead.MOEAD;

public class ReduceClass extends Reducer<Text, Text, NullWritable, Text> {
	private Text result = new Text();

	// private IntWritable result = new IntWritable();
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		String sum = "";
		// int sum = 0;
		List<CMoChromosome> chromosomes = new ArrayList<CMoChromosome>();
		List<int[]> neighbourTable = new ArrayList<int[]>();
		List<double[]> weights = new ArrayList<double[]>();
		MoeaData mData = new MoeaData();
		List<String> strArray = new ArrayList<String>();
		double[] idealpoint = new double[2];
		idealpoint[0] = idealpoint[1] = 10.0;
		
		int count = 0;
		
		// mData.popsize = 300;
		for (Text val : values) {
			if (key.toString() != "1111111111") {
				strArray.add(val.toString());
				mData.stringLine2Object(chromosomes, neighbourTable, weights, val.toString());
			} else {
				// 处理一些杂带的数据,CR,F等等
			}
			// sum ++;
			// sum = sum + val + ",";
		}

		List<CMoChromosome> oChromosomes = new ArrayList<CMoChromosome>(
				chromosomes);
		// 按照fitnessValue值由大到小排序
		Collections.sort(oChromosomes, new SortByFitnessValue());

//		System.out.println("");

//		System.out.println(chromosomes.get(0).fitnessValue + " "
//				+ chromosomes.get(1).fitnessValue + " "
//				+ chromosomes.get(2).fitnessValue);

//		System.out.println("");

//		System.out.println(oChromosomes.get(0).fitnessValue + " "
//				+ oChromosomes.get(1).fitnessValue + " "
//				+ oChromosomes.get(2).fitnessValue);
		
		int index = 0;
		for (int i = 0; i < chromosomes.size(); i++)
			if (oChromosomes.get(0).fitnessValue == chromosomes.get(i).fitnessValue)
				index = i;
		// 此处是错误的，因为strArray没有进行排序
		sum = strArray.get(index);

		MOEAD moead = new MOEAD();
		System.out.println("oCH.value is : \n" + oChromosomes.get(0).objectivesValue[0] + " " + oChromosomes.get(0).objectivesValue[1] );
		moead.updateReference(idealpoint, oChromosomes.get(0));
		System.out.println("idealpoint is :\n" + idealpoint[0] + " " + idealpoint[1]);
		result.set(sum);
		// result.set(sum);
		// 需要对outputformat进行重写，使得在输出时将“\t”变为“,”
		// 2014-7-15
		NullWritable nullw = null;
		// context.write(key, result);
		context.write(nullw, result);
	}

	class SortByFitnessValue implements Comparator<CMoChromosome> {
		public int compare(CMoChromosome o1, CMoChromosome o2) {
			if (o1.fitnessValue > o2.fitnessValue)
				return -1;
			return 0;
		}
	}
}
