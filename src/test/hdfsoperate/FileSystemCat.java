package test.hdfsoperate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class FileSystemCat {

	public static void main(String[] args) throws IOException {
		String uri = "hdfs://192.168.1.137:9000/home/hadoop/test/words.txt";
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		InputStream in = null;
		in = fs.open(new Path(uri));
		IOUtils.copyBytes(in, System.out, 4096, false);
		IOUtils.closeStream(in);
	}

}
