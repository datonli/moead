package algorithms.mapreduceclass;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import algorithms.mo.IMultiObjectiveProblem;
import algorithms.mo.ea.CMoChromosome;
import algorithms.mo.ea.MoChromosome;
import algorithms.mo.prolem.ZDT1;
import algorithms.moead.MOEAD;

public class OtherMapClass extends Mapper<Object, Text, Text, Text> {

	long weightVectorNum;

	Text weightVector = new Text();
	Text indivInfo = new Text();

	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		// CMoChromosome iChromesome = new CMoChromosome();
		// double[] weights = new double[2];

		String str = value.toString();
		// String info[][] = stringSplit(str);

		MoeaData mData = new MoeaData();
		mData.str2MoeaData(str);

		// 7-16 做个循环，把每个chromosome都添加进去数组或者list里面
		// CMoChromosome[] iChromesome = new CMoChromosome[info.length];
		// String[] chromesomeString = new String[info.length];
		//
		// // 需要对这个CMoChromosome对象数组逐个进行初始化，不然会出现空指针异常错误！！
		// for (int k = 0; k < info.length; k++)
		// iChromesome[k] = new CMoChromosome();
		// String[] weightVectorString = new String[info.length];
		// for (int j = 0; j < info.length; j++) {
		// iChromesome[j].realGenes = new double[info[j].length - 3];
		// iChromesome[j].fitnessValue = Double.parseDouble(info[j][0]);
		// weights[0] = Double.parseDouble(info[j][info[j].length - 2]);
		// weights[1] = Double.parseDouble(info[j][info[j].length - 1]);
		// for (int i = 0; i < info[j].length - 3; i++)
		// iChromesome[j].realGenes[i] = Double
		// .parseDouble(info[j][i + 1]);
		// weightVectorString[j] = weights[0] + "," + weights[1];
		// chromesomeString[j] = iChromesome[j].fitnessValue + "";
		// for (int i = 0; i < iChromesome[j].realGenes.length; i++)
		// chromesomeString[j] = chromesomeString[j] + ","
		// + iChromesome[j].realGenes[i];
		// }

		// for (int i = 0; i < info.length; i++) {
		// evolveNewInd(i);
		// }

		// weightVectorNum = Long.parseLong(key.toString());
		// weightVector.set( weightVectorNum);

		MOEAD impl = new MOEAD();
		IMultiObjectiveProblem problem = ZDT1.getInstance(30);
		impl.setMultiObjectiveProblem(problem);
		impl.setNumObjectives(problem.getObjectiveSpaceDimension());
		impl.setNumParameters(problem.getParameterSpaceDimension());
		// impl.initialize();
		MoChromosome offSpring = new CMoChromosome() ;
		int internalTime = 10;
		for(int t = 0 ; t < internalTime ; t ++){
			for (int i = 0; i < mData.popsize; i++) {
				offSpring = impl.GeneticOPDE(mData.neighbourTable,
						mData.neighboursize, mData.chromosomes, i);
				impl.evaluate(offSpring);
				impl.updateNeighbours(mData.weights, mData.idealpoint,
						mData.chromosomes, mData.neighbourTable,
						mData.neighboursize, offSpring, i);
	//			System.out.print(offSpring.fitnessValue + " " );
	//			System.out.println("First idealpoint is :\n" + mData.idealpoint[0] + " " + mData.idealpoint[1]);
				impl.updateReference(mData.idealpoint, offSpring);
	//			System.out.println("then idealpoint is :\n" + mData.idealpoint[0] + " " + mData.idealpoint[1]);
				impl.destroyChromosome(offSpring);
			}
		}
		//System.out.println(offSpring.parDimension);

		// 7-16 目前面临的问题是怎么将map输出多个key/value对，而不是只产生一个输出结果！！！
		// 解决办法：不停往context写就可以了！！

		//System.out.println(mData.popsize);
		//for (int k = 0; k < mData.popsize; k++)
			//System.out.print(mData.chromosomes.get(k).fitnessValue + " " ) ;
		//System.out.println("");
		//System.out.println("idealpoint is:");
		//System.out.println(mData.idealpoint[0] + " " + mData.idealpoint[1]);
		for (int k = 0; k < mData.popsize; k++) {
			indivInfo.set(mData.toStringLine(k, mData.chromosomes, mData.neighbourTable, mData.weights));
			// chromosome.set()
			weightVector.set(mData.weights.get(k)[0] + ","
					+ mData.weights.get(k)[1]);
			context.write(weightVector, indivInfo);
		}
	}
	// n行m列
	// 一行代表一组weightVector和chromesome.realGenes
	// 在一行上面排列的顺序是：fitnessValue + realGenes + weightVector
	// private String[][] stringSplit(String str) {
	// String[] numArray;
	// int n = 0, m = 0;
	// for (int i = 0; i < str.length(); i++) {
	// if (str.charAt(i) == ' ')
	// n++;
	// }
	// numArray = str.trim().split(" ");
	// for (int i = 0; i < numArray[0].length(); i++) {
	// if (numArray[0].charAt(i) == ',')
	// m++;
	// }
	// String[][] a = new String[n][m + 1];
	// for (int i = 0; i < n; i++)
	// a[i] = numArray[i].trim().split(",");
	// return a;
	// }
}
