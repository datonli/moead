package test.dataout;

import java.io.DataOutput;
import java.io.DataOutputStream;

import org.apache.hadoop.io.Text;

public class String2Text {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Text t = new Text();
		t.set("hello");
		System.out.println(t);
		System.out.println(t.toString());
		DataOutput dot = new DataOutputStream(null);
		
//		t.write(out);
	}

}
