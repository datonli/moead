package test;

import java.util.ArrayList;
import java.util.List;

public class arrayListCopy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<int[]> list1 = new ArrayList<int[]>();
		int[] a1 = new int[2];
		a1[0] = 1;
		a1[1] = 2;
		list1.add(a1);
		
		List<int[]> list2 = new ArrayList<int[]>(list1);
		System.out.println(list2.get(0)[0] + " " + list2.get(0)[1]); 
		list1.clear();
		System.out.println(list2.get(0)[0] + " " + list2.get(0)[1]); 
	}

}
