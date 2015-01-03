package algorithms.mapreduceclass;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.mapred.JobConf;

public class HdfsOper {

	    //HDFS address
	    private static final String HDFS = "hdfs://localhost:9000/";
	    //hdfs path
	    private String hdfsPath;
	    //Hadoop configure
	    private Configuration conf;
	    
	    public HdfsOper(Configuration conf) {
	        this(HDFS, conf);
	    }

	    public HdfsOper(String hdfs, Configuration conf) {
	        this.hdfsPath = hdfs;
	        this.conf = conf;
	    }

	    //load the configure files
	    public static JobConf config(){
	        JobConf conf = new JobConf(HdfsOper.class);
	        conf.setJobName("HdfsDAO");
	        conf.addResource("classpath:/hadoop/core-site.xml");
	        conf.addResource("classpath:/hadoop/hdfs-site.xml");
	        conf.addResource("classpath:/hadoop/mapred-site.xml");
	        return conf;
	    }

	    
	    /**
	     * hdfs api implements
	     * including :
	     * cat,ls,mkdirs,rm,copyFile,download,createFile
	     */
	    
	    public void cat(String remoteFile) throws IOException {
	        Path path = new Path(remoteFile);
	        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
	        FSDataInputStream fsdis = null;
	        System.out.println("cat: " + remoteFile);
	        try {  
	            fsdis =fs.open(path);
	            IOUtils.copyBytes(fsdis, System.out, 4096, false);  
	          } finally {  
	            IOUtils.closeStream(fsdis);
	            fs.close();
	          }
	    }

	    public void ls(String folder) throws IOException {
	        Path path = new Path(folder);
	        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
	        FileStatus[] list = fs.listStatus(path);
	        System.out.println("ls: " + folder);
	        System.out.println("==========================================================");
	        for (FileStatus f : list) {
	            System.out.printf("name: %s, folder: %s, size: %d\n", f.getPath(), f.isDir(), f.getLen());
	        }
	        System.out.println("==========================================================");
	        fs.close();
	    }
	    
	    public void mkdirs(String folder) throws IOException {
	        Path path = new Path(folder);
	        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
	        if (!fs.exists(path)) {
	            fs.mkdirs(path);
	            System.out.println("Create: " + folder);
	        }
	        fs.close();
	    }
	    
	    public void rm(String folder) throws IOException {
	        Path path = new Path(folder);
	        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
	        fs.deleteOnExit(path);
	        System.out.println("Delete: " + folder);
	        fs.close();
	    }
	    
	    public void copyFile(String local, String remote) throws IOException {
	        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
	        fs.copyFromLocalFile(new Path(local), new Path(remote));
	        System.out.println("copy from: " + local + " to " + remote);
	        fs.close();
	    }
	    
	    public void download(String remote, String local) throws IOException {
	        Path path = new Path(remote);
	        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
	        fs.copyToLocalFile(path, new Path(local));
	        System.out.println("download: from" + remote + " to " + local);
	        fs.close();
	    }
	    
	    public void createFile(String file, String content) throws IOException {
	        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
	        byte[] buff = content.getBytes();
	        FSDataOutputStream os = null;
	        try {
	            os = fs.create(new Path(file));
	            os.write(buff, 0, buff.length);
	            System.out.println("Create: " + file);
	        } finally {
	            if (os != null)
	                os.close();
	        }
	        fs.close();
	    }
	    
	    
	    //test use cases
	    public static void main(String[] args) throws IOException {
	        JobConf conf = config();
	        HdfsOper hdfs = new HdfsOper(conf);
	        hdfs.ls("/");
	        hdfs.ls("/user/root/input/");
	        hdfs.cat("/user/root/input/test1.txt");
	    } 
}
