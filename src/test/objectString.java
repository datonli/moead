package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class objectString {
	public static void main(String args[]) throws IOException
	{
		myPerson me = new myPerson();
		me.height = 178;
		me.weight = 135;
		System.out.println(me.toString());
		FileOutputStream fos = new FileOutputStream("/home/hadoop/moead-experiment/myPerson.txt");
		DataOutputStream dos = new DataOutputStream(fos);
		dos.writeUTF(me.height + " " + me.weight + "\n");
		dos.close();
		fos.close();
		
		FileInputStream fis = new FileInputStream("/home/hadoop/moead-experiment/myPerson.txt");
		DataInputStream dis = new DataInputStream(fis);
		System.out.println(dis.readUTF());
		fis.close();
		dis.close();
		
	}
}

class myPerson{
	int height;
	int weight;
}
