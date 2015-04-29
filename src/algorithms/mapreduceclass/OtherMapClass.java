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
	public static long timeRecord = 0;
	long weightVectorNum;

	Text weightVector = new Text();
	Text indivInfo = new Text();

	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		long time=System.currentTimeMillis();
		String str = value.toString();
		MoeaData mData = new MoeaData();
		mData.str2MoeaData(str);

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

		for (int k = 0; k < mData.popsize; k++) {
			indivInfo.set(mData.toStringLine(k, mData.chromosomes, mData.neighbourTable, mData.weights));
			// chromosome.set()
			weightVector.set(mData.weights.get(k)[0] + ","
					+ mData.weights.get(k)[1]);
			context.write(weightVector, indivInfo);
		}
		timeRecord += System.currentTimeMillis() - time;
		System.out.println("mapper timeRecord is : " + timeRecord);
	}
}
