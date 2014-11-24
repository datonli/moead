package test.dataout;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;


public class DataOutMR {

	public static class MapperClass extends
			Mapper<Object, Text, Text, Text> {

//		private IntWritable score = new IntWritable();
		private Text name = new Text();
		private Text score = new Text();
		
		private String nameString = new String();
		private String scoreString = new String();
		
		
		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			nameString = value.toString().substring(0,value.toString().length()-3 );
			scoreString = value.toString().substring(value.toString().length()-2, value.toString().length());
			name.set(nameString);
			score.set(scoreString);
//			score.set(Integer.parseInt(scoreString));
			context.write(name, score);
		}
	}

	public static class ReducerClass extends
			Reducer<Text, Text, Text, Text> {
//		private IntWritable result = new IntWritable();
		private Text result = new Text();
		public void reduce(Text key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {
//			int sum = 0;
			String sum = "";
			for(Text val : values)
			{
//				sum ++;
				sum += val+" ";
			}
			result.set(sum);
			context.write(key, result);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: wordcount <in> <out>");
			System.exit(2);
		}
		Job job = new Job(conf, "filter");
		job.setJarByClass(DataOutMR.class);
		job.setMapperClass(MapperClass.class);
//		job.setCombinerClass(ReducerClass.class);
		job.setReducerClass(ReducerClass.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
