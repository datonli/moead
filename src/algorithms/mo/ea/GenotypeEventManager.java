package algorithms.mo.ea;

import java.util.ArrayList;
import java.util.List;

import algorithms.moead.Subproblem;


public class GenotypeEventManager {
	public List<IGenotypeListener> listeners = new ArrayList<IGenotypeListener>();

	public void fireGenerationBegin(List<Subproblem> pop, int generation) {
		for (IGenotypeListener listener : listeners) {
			listener.generationBegin(pop, generation);
		}
	};

	public void fireGenerationEnd(List<Subproblem> pop, int generation) {
		for (IGenotypeListener listener : listeners) {
			listener.generationEnd(pop, generation);
		}
	}

	public void fireInitFinished(List<Subproblem> subproblems) {
		for (IGenotypeListener listener : listeners) {
			listener.initFinish(subproblems);
		}
	};
}
