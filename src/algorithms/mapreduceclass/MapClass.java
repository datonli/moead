package algorithms.mapreduceclass;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import algorithms.mo.ea.CMoChromosome;


public class MapClass extends Mapper<Object, Text, Text, Text> {


	
	
	String chromesomeString = new String();
	long weightVectorNum ;
	
	Text weightVector = new Text();
	Text chromesome = new Text();
	
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		
		CMoChromosome iChromesome = new CMoChromosome();
		double[] weights = new double[2];
		
		chromesomeString = value.toString();
		String info[] = stringSplit(chromesomeString);
		
		iChromesome.realGenes = new double[info.length - 3];
		iChromesome.fitnessValue = Double.parseDouble(info[0]);
		weights[0] = Double.parseDouble(info[info.length-2]);
		weights[1] = Double.parseDouble(info[info.length-1]);
		for(int i = 0;  i < info.length - 3; i ++)
			iChromesome.realGenes[i] = Double.parseDouble(info[i+1]);
		
		
		String weightVectorString = weights[0] + "," + weights[1];
		chromesomeString = "";
		for(int i = 0; i < iChromesome.realGenes.length; i ++)
			chromesomeString = chromesomeString + "," + iChromesome.realGenes[i];
		
		
//		weightVectorNum = Long.parseLong(key.toString());
		
//		weightVector.set( weightVectorNum);
		
		chromesome.set(chromesomeString);
		weightVector.set(weightVectorString);
		context.write(weightVector, chromesome);
		
	}
	
	private String[] stringSplit(String str)
	{
//		String str = "{0.08218825376743849,3.757855421277097E-4,2.869341891856144E-6,0.0020485414638808054,6.243090755902699E-4,4.594265161742133E-4,0.0,2.732823810369905E-4,0.0011590656966958223,4.2818123661894137E-4,7.703224883792988E-5,4.788870204666882E-5,2.942768006691259E-4,3.9122461647754416E-4,1.5764756243880047E-4,0.0,2.2370693216623087E-4,1.316947258583687E-5,1.3655398129035817E-4,5.8193244278313855E-5,4.535704265202578E-4,0.001791791249116657,6.812752747120833E-4,3.5458065742900996E-5,5.553535082614043E-4,4.161965987930608E-4,6.422278597241913E-5,3.54893384907453E-5,8.704258591667111E-4,6.345007345795682E-4},0.0,0.8966666666666666,0.10333333333333333";
		String[] numArray ;
		int n = 0;
		for(int i = 0; i < str.length(); i ++)
		{
			if(str.charAt(i) == ',')
				n ++;
		}
		numArray = str.trim().split(",");
//		for(int i = 0; i < numArray.length; i ++)
//			System.out.println(numArray[i]);
		
		return numArray;
	}
}
