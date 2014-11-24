package algorithms.mo.ea;

import algorithms.mo.IMultiObjectiveProblem;


public abstract class AbstractMOP implements IMultiObjectiveProblem {
	protected String name;
	protected double[][] domain;
	protected double[][] range;
	protected double[] idealpoint;
	protected int objDimension;
	protected int parDimension;

	public String getName() {
		return name;
	}

	public double[] getIdealPoint() {
		return idealpoint;
	}

	public int getObjectiveSpaceDimension() {
		return objDimension;
	}

	public int getParameterSpaceDimension() {
		return parDimension;
	}

	public double[][] getRange() {
		return range;
	}

	public double[][] getDomain() {
		return domain;
	}
	
	protected abstract void init();
	
	protected abstract MoChromosome createMoChromosomeInstance();

}
