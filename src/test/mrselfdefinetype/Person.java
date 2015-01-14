package test.mrselfdefinetype;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

//
import org.apache.hadoop.io.WritableComparable;
//
public class Person implements WritableComparable<Person>{

	@Override
	public void readFields(DataInput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int compareTo(Person o) {
		// TODO Auto-generated method stub
		return 0;
	}
//	int id;
//	String name;
//	int age;
//	String city;
//	static{
//		
//	}
//	public static final String P_AGE = "ID";
////	public int hashCode(){}
////	public boolean equals(Object obj){}
//	
}
