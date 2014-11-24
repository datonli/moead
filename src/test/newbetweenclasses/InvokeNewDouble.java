package test.newbetweenclasses;

import test.mrfileinput.NewDouble;

public class InvokeNewDouble {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NewDouble nd = new NewDouble();
		nd.init();
		System.out.println(nd.d.get(0)[0] +" " + nd.num);
	}

}
