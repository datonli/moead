package test.hdfsoperate;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

public class URLCat {

	static{
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		InputStream in = null;
		in = new URL("hdfs://192.168.1.137:9000/home/hadoop/test/words.txt").openStream();
		IOUtils.copyBytes(in, System.out, 4096, false);
		IOUtils.closeStream(in);
	}

}
