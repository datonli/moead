package algorithms.mo.ea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import algorithms.mo.DominationDeterminer;

public class MoPopulation {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	public List<MoChromosome> chromosomes;

	// public List<CMoChromosome> oChromosomes;
	// private MoeaGenotype genotype;

	public MoChromosome getChromosome(int a_index) {
		return chromosomes.get(a_index);
	}

	public MoChromosome setChromosome(int a_index, MoChromosome chromosome) {
		return chromosomes.set(a_index, chromosome);
	}

	public void addChromosome(MoChromosome mo) {
		this.chromosomes.add(mo);
	}

	MoPopulation(MoeaGenotype genotype1, int size) {
		// this.genotype = genotype1;
		this.chromosomes = new ArrayList<MoChromosome>(size);
		for (int i = 0; i < size; i++) {
			MoChromosome chromosome = genotype1.createChromosome();
			this.chromosomes.add(chromosome);
		}
	}

	public int size() {
		return chromosomes.size();
	}

	public MoChromosome removeChromosome(int i) {
		return this.chromosomes.remove(i);
	}

	public void writeToFile(String filename) {
		writeToFile(this.chromosomes, filename);
	}

	public void writeToStream(OutputStream stream) {
		writeToStream(this.chromosomes, stream);
	}

	public static MoPopulation readFrom(String readFromFile) {
		// todo
		return null;
	}

	public static void writeToFile(Collection<MoChromosome> col, String filename) {
		try {
			FileOutputStream stream = new FileOutputStream(filename);
			writeToStream(col, stream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void writeToStream(Collection<MoChromosome> col,
			OutputStream stream) {
		PrintStream printer = new PrintStream(stream);
		for (MoChromosome ind : col) {
			printer.println(ind.vectorString());
		}
	}

	public String toString() {
		String str = "";
		for (int i = 0; i < this.chromosomes.size(); i++) {
			str += this.chromosomes.get(i).toString() + ",";
		}
		return str;
	}

	// public void writeToFile(String filename) throws IOException {
	// writeToFile(this.chromosomes, filename);
	// }
	//
	// public void writeToStream(List<MoChromosome> chromosomes2,
	// OutputStream stream) {
	// writeToStream(this.chromosomes, stream);
	// }
	//
	// public static MoPopulation readFrom(String readFromFile) { // todo
	// return null;
	// }
	//
	// // 2014-5-27 将创建的Collection<MoChromosome>序列化写入到本地文件中 然后通过反序列化读取
	//
	// public static void writeToFile(List<MoChromosome> col, String filename)
	// throws IOException {
	// try {
	// FileOutputStream fos = new FileOutputStream(filename);
	// ObjectOutputStream oos = new ObjectOutputStream(fos);
	//
	// oos.writeObject(col);
	// oos.close();
	// fos.close();
	// // FileOutputStream stream = new FileOutputStream(filename);
	// // DataOutputStream dos = new DataOutputStream(stream);
	// // dos.writeBytes(col.toString());
	// // writeToStream(col, stream);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// }
	// }

	// public static void writeToStream(Collection<MoChromosome> col,
	// OutputStream stream) {
	// PrintStream printer = new PrintStream(stream);
	// for (MoChromosome ind : col) {
	// printer.println(ind.vectorString());
	// }
	// }

	/*
	 * public void writeToFile(String filename) throws IOException {
	 * writeToFile(this.chromosomes, filename); }
	 * 
	 * public static void writeToFile(Collection<MoChromosome> col, String
	 * filename) throws IOException { try { FileOutputStream fos = new
	 * FileOutputStream(filename); ObjectOutputStream oos = new
	 * ObjectOutputStream(fos); oos.writeObject(col); } catch
	 * (FileNotFoundException e) { e.printStackTrace(); } }
	 */

	public Collection<MoChromosome> readFromFile(String filename)
			throws IOException, ClassNotFoundException { // Collection<MoChromosome>
															// col ;
		FileInputStream stream = new FileInputStream(filename);

		// DataInputStream in = new DataInputStream(stream);
		// in.readByte();
		ObjectInputStream ois = new ObjectInputStream(stream);
		List<MoChromosome> col = new ArrayList<MoChromosome>();
		col = (ArrayList<MoChromosome>) ois.readObject();
		Iterator<MoChromosome> it = col.iterator();
		while (it.hasNext()) {
			CMoChromosome moCh = (CMoChromosome) it.next();
			System.out.println(moCh.realGenes[0]);
			// System.out.println(stu.getId() + " " + stu.getName());
		}
		ois.close();
		stream.close();
		return null;
	}

	/*
	 * public static Collection<MoChromosome> readFromFile(String filename) {
	 * List<MoChromosome> col = new ArrayList<MoChromosome>(); try {
	 * FileInputStream stream = new FileInputStream(filename); col =
	 * readFromStream(stream); } catch (FileNotFoundException e) {
	 * e.printStackTrace(); } return col; }
	 * 
	 * private static List<MoChromosome> readFromStream(FileInputStream stream)
	 * { List<MoChromosome> col = new ArrayList<MoChromosome>(); try {
	 * ObjectInputStream ois = new ObjectInputStream(stream); int size = 30;
	 * for(int i = 0; i < size; i ++){ try { MoChromosome reMoChromosome
	 * =(MoChromosome) ois.readObject(); col.add(reMoChromosome);
	 * System.out.println
	 * ("getFitnessValue = "+reMoChromosome.getFitnessValue()); } catch
	 * (ClassNotFoundException e) { e.printStackTrace(); } } } catch
	 * (IOException e) { e.printStackTrace(); }
	 * 
	 * return col; }
	 */
	public static Collection<MoChromosome> NonDominationSelection(
			List<MoChromosome> pop) {
		List<MoChromosome> result = new ArrayList<MoChromosome>();
		if (pop.size() == 0)
			return result;
		result.add(pop.get(0));

		int counter = 1;
		int size = pop.size();

		out: while (counter < size) {
			int jj = 0;
			MoChromosome chromosome2 = pop.get(counter);
			int resultsize = result.size();
			boolean remove[] = new boolean[resultsize];

			while (jj < resultsize) {
				MoChromosome chromosome1 = result.get(jj);
				if (DominationDeterminer.dominate(chromosome2.objectivesValue,
						chromosome1.objectivesValue, true)) {
					remove[jj] = true;
					// result.remove(jj);
				} else if (DominationDeterminer.dominate(
						chromosome1.objectivesValue,
						chromosome2.objectivesValue, true)) {
					counter++;
					break out;
				}
				jj++;
			}

			for (int i = remove.length - 1; i >= 0; i--) {
				if (remove[i])
					result.remove(i);
			}

			result.add(chromosome2);
			counter++;
		}

		return result;
	}

	public static Collection<MoChromosome> NonDominationSelection(
			MoPopulation pop) {
		return NonDominationSelection(pop.chromosomes);
	}

	/**
	 * Compute the minimal distance from a MoChromosome to a set of MoChromosome
	 * in the parameter space.
	 * 
	 * @param pop
	 * @param p
	 * @return
	 */
	public static double minParameterDistance(List<MoChromosome> pop,
			MoChromosome p) {
		double distance = Double.MAX_VALUE;
		Iterator<MoChromosome> iterator = pop.iterator();
		while (iterator.hasNext()) {
			MoChromosome next = iterator.next();
			double parameterDistance = p.parameterDistance(next);
			if (parameterDistance < distance)
				distance = parameterDistance;
		}
		return distance;
	}
}
