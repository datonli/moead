package test;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class FileOperate {

	public static void main(String[] args) throws IOException,
			InterruptedException, ClassNotFoundException {

		init();

		Configuration conf = new Configuration();

		Job job = new Job(conf, "word count");
		job.setJarByClass(FileOperate.class);

		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		/* set the path of input and output */
		FileInputFormat.addInputPath(job, new Path("hdfs://192.168.1.137:9000/home/hadoop/test/words.txt"));
		FileOutputFormat.setOutputPath(job, new Path("hdfs://192.168.1.137:9000/home/hadoop/test/out"));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

	public static class TokenizerMapper extends
			Mapper<Object, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {

			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				word.set(itr.nextToken());
				context.write(word, one);
			}
		}
	}

	public static class IntSumReducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {

			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}

	public static void init() throws IOException {

		/* copy local file to hdfs */
		Configuration config = new Configuration();
		FileSystem hdfs = null;
		String srcFile = "/home/hadoop/Desktop/words.txt";
		String dstFile = "hdfs:///home/hadoop/test/words.txt";
		hdfs = FileSystem.get(config);
		Path srcPath = new Path(srcFile);
		Path dstPath = new Path(dstFile);
		hdfs.copyFromLocalFile(srcPath, dstPath);

		String fileName = "hdfs://192.168.1.137:9000/home/hadoop/test/words.txt";
		Path path = new Path(fileName);
		FileStatus fileStatus = null;

		fileStatus = hdfs.getFileStatus(path);
		System.out.println(fileStatus.getBlockSize());

		FileSystem fs = FileSystem.get(config);
		DistributedFileSystem hdfs1 = (DistributedFileSystem) fs;
		DatanodeInfo[] dataNodeStats = hdfs1.getDataNodeStats();

		/* create a file on hdfs */
		Path Outputpath = new Path("hdfs://192.168.1.137:9000/home/hadoop/test/listOfDatanode");
		FSDataOutputStream outputStream = hdfs.create(Outputpath);

		String[] names = new String[dataNodeStats.length];
		for (int i = 0; i < dataNodeStats.length; i++) {
			names[i] = dataNodeStats[i].getHostName();/* get the list of datanodes */
			System.out.println(names[i]);
			/* write the list of datanodes to file on hdfs */
			outputStream.write(names[i].getBytes(), 0, names[i].length());
		}
	}

}
