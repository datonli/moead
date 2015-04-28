package algorithms.mapreduceclass;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

/**
 * MyInputFormat.java is an self-using class which inherit from FileInputFormat,
 * due to read our own format input file,I write it for much more performance. 
 * I was used with "NLineInputFormat.java" to read my own format file which split the population 
 * each line.The NLineInputFormat works well but it return a key/value just like offset/population.
 * It's considered lead to a slower and bad performance.
 * I'd to rewrite it cause the urgent require of higher running speed.
 * So that's it.MyInputFormat would work like:
 * input : a file need to split,which corresponded to a map.
 * output: key is a individual vector and value is their real value.
 *   
 * 
 * @author Daniel Li
 * @date 2015-4-24
 */

public class MyInputFormat extends FileInputFormat<LongWritable, Text>{
	
	public RecordReader<LongWritable, Text> createRecordReader(
		      InputSplit genericSplit, TaskAttemptContext context) 
		      throws IOException {
		return null;
	}
	
	public List<InputSplit> getSplits(JobContext job)
			  throws IOException {
		return null;
	}
	
}
