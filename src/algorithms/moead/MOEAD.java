package algorithms.moead;

import static java.lang.Math.abs;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import algorithms.Sorting;
import algorithms.mo.IMultiObjectiveProblem;
import algorithms.mo.ea.CMoChromosome;
import algorithms.mo.ea.MoChromosome;
import algorithms.mo.ea.MoPopulation;
import algorithms.mo.ea.MoeaGenotype;
import algorithms.mo.prolem.ZDT1;

public class MOEAD extends MoeaGenotype {
	public int TotalItrNum;

	public double[] idealpoint;

	public MoPopulation mainpop;

	public List<double[]> weights;
	public List<int[]> neighbourTable;

	public List<double[]> oWeights;
	public List<int[]> oNeighbourTable = new ArrayList<int[]>();

	public int[] array;

	// parameters.
	public int popsize;

	public int neighboursize;

	public double F = 0.5;
	public double CR = 1;

	public MoChromosome GeneticOPDE(List<int[]> neighbourTable,
			int neighboursize, List<CMoChromosome> chromosomes, int i) {
		int k, l, m;
		do
			k = neighbourTable.get(i)[this.randomData.nextInt(0,
					neighboursize - 1)];
		while (k == i);
		do
			l = neighbourTable.get(i)[this.randomData.nextInt(0,
					neighboursize - 1)];
		while (l == k || l == i);
		do
			m = neighbourTable.get(i)[this.randomData.nextInt(0,
					neighboursize - 1)];
		while (m == l || m == k || m == i);

		CMoChromosome chromosome1 = chromosomes.get(k);
		CMoChromosome chromosome2 = chromosomes.get(l);
		CMoChromosome chromosome3 = chromosomes.get(m);

		// generic operation crossover and mutation.
		CMoChromosome offSpring = (CMoChromosome) this.createChromosome();
		CMoChromosome current = chromosomes.get(i);
		int D = offSpring.parDimension;
		double jrandom = Math.floor(Math.random() * D);

		for (int index = 0; index < D; index++) {
			double value = 0;
			if (Math.random() < CR || index == jrandom)
				value = chromosome1.realGenes[index]
						+ F
						* (chromosome2.realGenes[index] - chromosome3.realGenes[index]);
			else
				value = current.realGenes[index];

			// REPAIR.
			double high = 1;
			double low = 0;
			if (value > high)
				value = high;
			else if (value < low)
				value = low;

			offSpring.realGenes[index] = value;
		}

		offSpring
				.mutate(this.getRandomGenerator(), 1d / offSpring.parDimension);
		return offSpring;
	}

	protected MoChromosome GeneticOPDE(int i) {
		int k, l, m;
		do
			k = neighbourTable.get(i)[this.randomData.nextInt(0,
					neighboursize - 1)];
		while (k == i);
		do
			l = neighbourTable.get(i)[this.randomData.nextInt(0,
					neighboursize - 1)];
		while (l == k || l == i);
		do
			m = neighbourTable.get(i)[this.randomData.nextInt(0,
					neighboursize - 1)];
		while (m == l || m == k || m == i);

		CMoChromosome chromosome1 = (CMoChromosome) this.mainpop
				.getChromosome(k);
		CMoChromosome chromosome2 = (CMoChromosome) this.mainpop
				.getChromosome(l);
		CMoChromosome chromosome3 = (CMoChromosome) this.mainpop
				.getChromosome(m);

		// generic operation crossover and mutation.
		CMoChromosome offSpring = (CMoChromosome) this.createChromosome();
		CMoChromosome current = (CMoChromosome) this.mainpop.getChromosome(i);
		int D = offSpring.parDimension;
		double jrandom = Math.floor(Math.random() * D);

		for (int index = 0; index < D; index++) {
			double value = 0;
			if (Math.random() < CR || index == jrandom)
				value = chromosome1.realGenes[index]
						+ F
						* (chromosome2.realGenes[index] - chromosome3.realGenes[index]);
			else
				value = current.realGenes[index];

			// REPAIR.
			double high = 1;
			double low = 0;
			if (value > high)
				value = high;
			else if (value < low)
				value = low;

			offSpring.realGenes[index] = value;
		}

		offSpring
				.mutate(this.getRandomGenerator(), 1d / offSpring.parDimension);
		return offSpring;
	}

	// protected MoChromosome GeneticOP(int i) {
	// int k = neighbourTable.get(i)[this.randomData.nextInt(0,
	// neighboursize - 1)];
	// int l = neighbourTable.get(i)[this.randomData.nextInt(0,
	// neighboursize - 1)];
	//
	// MoChromosome chromosome1 = this.mainpop.getChromosome(k);
	// MoChromosome chromosome2 = this.mainpop.getChromosome(l);
	//
	// // generic operation crossover and mutation.
	// MoChromosome offSpring1 = this.createChromosome();
	// MoChromosome offSpring = this.createChromosome();
	//
	// GeneticOperators.realCrossover2(chromosome1, chromosome2, offSpring1,
	// offSpring, this.getMultiObjectiveProblem().getDomain());
	//
	// GeneticOperators.realMutation2(offSpring, 1d / this
	// .getMultiObjectiveProblem().getParameterSpaceDimension(), this
	// .getMultiObjectiveProblem().getDomain());
	//
	// // update the archive;
	// // updateArchive(offSpring);
	// this.destroyChromosome(offSpring1);
	// return offSpring;
	// }

	public void updateNeighbours(List<double[]> weights, double[] idealpoint,
			List<CMoChromosome> chromosomes, List<int[]> neighbourTable,
			int neighboursize, MoChromosome offSpring, int i) {
		for (int j = 0; j < neighboursize; j++) {
			int weightindex = neighbourTable.get(i)[j];
			// MoChromosome sol = chromosomes.get(weightindex);
			MoChromosome sol = chromosomes.get(weightindex);
			double d = updateCretia(weights, idealpoint, weightindex, offSpring);
			double e = updateCretia(weights, idealpoint, weightindex, sol);
			if (d < e)
			{
				chromosomes.get(weightindex).fitnessValue = e;
//				System.out.println(sol.fitnessValue + " ");
				offSpring.copyTo(sol);
//				return e;
			}
			else
			{
				chromosomes.get(weightindex).fitnessValue = d;
//				return d;
			}
		}
	}

	public void updateNeighbours(int i, MoChromosome offSpring) {
		for (int j = 0; j < neighboursize; j++) {
			int weightindex = neighbourTable.get(i)[j];
			MoChromosome sol = mainpop.getChromosome(weightindex);
			double d = updateCretia(weightindex, offSpring);
			double e = updateCretia(weightindex, sol);
			if (d < e)
				offSpring.copyTo(sol);
		}
	}

	protected double updateCretia(List<double[]> weights, double[] idealpoint,
			int problemIndex, MoChromosome chrom) {
		double[] ds = weights.get(problemIndex);
		return this.techScalarObj(idealpoint, ds, chrom);
		// return this.wsScalarObj(ds, chrom);
	}

	protected double updateCretia(int problemIndex, MoChromosome chrom) {
		double[] ds = this.weights.get(problemIndex);
		return this.techScalarObj(ds, chrom);
		// return this.wsScalarObj(ds, chrom);
	}

	public void updateReference(double[] idealpoint, MoChromosome indiv) {
		// update the idealpoint.
		for (int j = 0; j < indiv.objectivesValue.length; j++)
			if (indiv.objectivesValue[j] < idealpoint[j])
				idealpoint[j] = indiv.objectivesValue[j];
	}

	public void updateReference(MoChromosome indiv) {
		// update the idealpoint.
		for (int j = 0; j < indiv.objectivesValue.length; j++)
			if (indiv.objectivesValue[j] < idealpoint[j])
				idealpoint[j] = indiv.objectivesValue[j];
	}

	protected double techScalarObj(double[] idealpoint, double[] namda,
			MoChromosome var) {
		double max_fun = -1 * Double.MAX_VALUE;
		// for (int n = 0; n < this.getNumObjectives(); n++) {
		for (int n = 0; n < 2; n++) {
			double diff = abs(var.objectivesValue[n] - idealpoint[n]);
			double feval;
			if (namda[n] == 0)
				feval = 0.00001 * diff;
			else
				feval = diff * namda[n];
			if (feval > max_fun)
				max_fun = feval;
		}
		return max_fun;
	}

	// 切比雪夫方法
	protected double techScalarObj(double[] namda, MoChromosome var) {
		double max_fun = -1 * Double.MAX_VALUE;
		for (int n = 0; n < getNumObjectives(); n++) {
			double diff = abs(var.objectivesValue[n] - idealpoint[n]);
			double feval;
			if (namda[n] == 0)
				feval = 0.00001 * diff;
			else
				feval = diff * namda[n];
			if (feval > max_fun)
				max_fun = feval;
		}
		return max_fun;
	}

	protected double wsScalarObj(double[] namda, MoChromosome var) {
		double sum = 0;
		for (int n = 0; n < getNumObjectives(); n++) {
			sum += (namda[n]) * var.objectivesValue[n];
		}
		return sum;
	}

	protected double pbiScalarObj(double[] namda, MoChromosome var) {
		return 0;
	}

	protected void improve(int i, MoChromosome offSpring) {
	}

	protected boolean terminated() {
		// condition on the iteration.
		return (this.ItrCounter > this.TotalItrNum);

		// condition on the evaluation.
		// return (this.evaluationCounter > this.TotalEvaluation);
	}

	public void initialize() {
		// loadConfiguration();
		idealpoint = new double[getNumObjectives()];
		for (int i = 0; i < getNumObjectives(); i++) {
			// min_indiv[i] = this.createChromosome();
			idealpoint[i] = 1.0e+30;
			// evaluate(min_indiv[i]);
		}

		// initialize the weights;
		initWeight(this.popsize);
		initNeighbour();
		moreInitialize();
	}

	public void moreInitialize() {
		mainpop = this.createPopulation(popsize);
		evaluate(mainpop);
		for (int i = 0; i < mainpop.size(); i++)
			updateReference(mainpop.getChromosome(i));
	};

	// protected void loadConfiguration() {
	// MoeaConfiguration configuration = this.getConfiguration();
	// this.popsize = configuration.getIntegerParameter(
	// MoeaConfiguration.POPULATION_SIZE, 100);
	// this.neighboursize = configuration.getIntegerParameter(
	// TSMOEAConfiguration.NEIGHBOUR_SIZE, 20);
	// this.TotalItrNum = configuration.getIntegerParameter(
	// MoeaConfiguration.GENERATION_NUMBER, 500);
	// }

	// 注意这里：相邻集是对每一个向量都有一个相邻集的。所以全部的相邻集合应该是一个二维的相邻矩阵。
	public void initNeighbour() {
		neighbourTable = new ArrayList<int[]>(popsize);

		double[][] distancematrix = new double[popsize][popsize];
		for (int i = 0; i < popsize; i++) {
			distancematrix[i][i] = 0;
			for (int j = i + 1; j < popsize; j++) {
				distancematrix[i][j] = distance(weights.get(i), weights.get(j));
				distancematrix[j][i] = distancematrix[i][j];
			}
		}

		for (int i = 0; i < popsize; i++) {
			int[] index = Sorting.sorting(distancematrix[i]);
			// int[] array = new int[neighboursize];
			array = new int[neighboursize];
			System.arraycopy(index, 0, array, 0, neighboursize);
			neighbourTable.add(array);
		}
	}

	public void initWeight(int m) {
		this.weights = new ArrayList<double[]>();
		for (int i = 0; i <= m; i++) {
			if (getNumObjectives() == 2) {
				double[] weight = new double[2];
				weight[0] = i / (double) m;
				weight[1] = (m - i) / (double) m;
				this.weights.add(weight);
			} else if (getNumObjectives() == 3) {
				for (int j = 0; j <= m; j++) {
					if (i + j <= m) {
						int k = m - i - j;
						double[] weight = new double[3];
						weight[0] = i / (double) m;
						weight[1] = j / (double) m;
						weight[2] = k / (double) m;
						this.weights.add(weight);
					}
				}
			}
		}
		this.popsize = this.weights.size();
	}

	protected void doSolve() {
		initialize();
		while (!terminated()) {
			/*
			 * MapReduce实现的地方。 实现思路是： 1）mapper: input
			 * (key=wightVector,value=chromosmoe.realGenes) output
			 * (key=wightVector,value=chromosmoe.realGenes)
			 * 
			 * 进行一次完整的Update(i:1->N)
			 * 
			 * 2)reducer/combiner: input
			 * (key=wightVector,value=list(chromosmoe.realGenes))
			 * (其中：list(chromosmoe
			 * .realGenes)是将mapper的输出中相同的wightVector集合起來進行按照评估公式由优到差进行排序) output
			 * (key=wightVector,value=chromosmoe.realGenes)
			 * 
			 * 将不同进化得到的种群结果进行整合得到新一代种群。
			 */

			// 此处是对每个个体进行对比进化的地方！！！ 7-16
			for (int i = 0; i < popsize; i++) {
				evolveNewInd(i);
			}
			ItrCounter++;
			System.out.println("---------------Iteration " + ItrCounter
					+ "-----------------");
		}
	}

	public void reset() {
	}

	public void evolveNewInd(int i) {
		MoChromosome offSpring = GeneticOPDE(i);
		// MoChromosome offSpring = GeneticOP(i);
		improve(i, offSpring);

		this.evaluate(offSpring);

		// update neighbours.

		updateNeighbours(i, offSpring);
		updateReference(offSpring);

		// Always remember to destory the chromosome.

		this.destroyChromosome(offSpring);
	}

	public static void main(String[] args) throws ClassNotFoundException,
			IOException {
		System.out.println("Test Created");
		MOEAD impl = new MOEAD();

		IMultiObjectiveProblem problem = ZDT1.getInstance(30);
		// IMultiObjectiveProblem problem = ZDT2.getInstance(30);
		System.out.println("Test Solving Started for: " + problem.getName());

		impl.popsize = 300;
		impl.neighboursize = 30;
		impl.TotalItrNum = 400;

		// impl.TotalItrNum = 10;
		impl.solve(problem);

		String filename = "/home/hadoop/moead-experiment/moead_"
				+ problem.getName() + ".txt";
		impl.mainpop.writeToFile(filename);
		// impl.mainpop.readFromFile(filename);

		DataOutputStream dataOutputStream = new DataOutputStream(
				new FileOutputStream(
						"/home/hadoop/Desktop/MyJobDir/moead_MR/DataStream.txt"));

		// Text t = new Text();
		for (int time = 0; time < 2; time++) {
			for (int num = 0; num < impl.mainpop.chromosomes.size(); num++) {
				dataOutputStream.writeBytes(impl.mainpop.chromosomes.get(num)
						.toString()
						+ ","
						+ impl.weights.get(num)[0]
						+ ","
						+ impl.weights.get(num)[1] + " ");
			}
			dataOutputStream.writeBytes("\n");
		}
		// System.out.println(impl.mainpop.chromosomes.get(0).fitnessValue);
		// System.out.println(t.toString());

		// for (int num = 0; num < impl.weights.size(); num++)
		// dataOutputStream.writeBytes(impl.weights.get(num)[0] + " "
		// + impl.weights.get(num)[1]+"\n");
		dataOutputStream.close();

		System.out
				.println(impl.weights.size()
						+ " "
						+ impl.mainpop.chromosomes.size()
						+ " "
						+ impl.idealpoint.length
						+ " "
						+ impl.neighbourTable.size()
						+ " "
						+ impl.neighbourTable.get(0).length
						+ " "
						+ impl.mainpop.getChromosome(0).objectivesEI.length
						+ " "
						+ impl.mainpop.getChromosome(0).estimatedObjectiveDevitation.length
						+ " "
						+ impl.mainpop.getChromosome(0).estimatedObjectiveValue.length
						+ " "
						+ impl.idealpoint.length);

		System.out.println("Test Solving End");
	}
}
