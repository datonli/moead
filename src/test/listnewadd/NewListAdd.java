package test.listnewadd;

import java.util.ArrayList;
import java.util.List;

public class NewListAdd {
	List<int[]> lists = new ArrayList<int[]>();
	public static void main(String[] args) {
		NewListAdd nla = new NewListAdd();
		nla.newAdd();
		System.out.println(nla.lists.get(0)[1]);
	}
	private  void newAdd() {
		int[] a = new int[2];
		a[1] = 1;
		a[0] = 0;
		this.lists.add(a);
	}

}
