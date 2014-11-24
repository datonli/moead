package test.mrfileinput;

import java.util.ArrayList;
import java.util.List;

public class NewDouble {
	public List<double[]> d ;
	public int num;
	
	
	public void init()
	{
		init1();
		
		num = 1;
	}


	private void init1() {
		d  = new ArrayList<double[]>();
		double[] id = new double[2];
		id[0] = 0.1;
		id[1] = 0.2;
		d.add(id);
	}
	
}
