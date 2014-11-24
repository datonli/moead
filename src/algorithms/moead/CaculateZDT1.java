package algorithms.moead;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import algorithms.mapreduceclass.MoeaData;

public class CaculateZDT1 {
	public static void main(String[] args) throws IOException {
		MoeaData mData = new MoeaData();
		CaculateZDT1 cz = new CaculateZDT1();
		FileReader fr = new FileReader("/home/hadoop/moead-experiment/moead_ZDT1_30.txt");
		BufferedReader br = new BufferedReader(fr);
		String line = new String();
		List<double[]> ovs = new ArrayList<double[]>();
		String str = "";
		String[][] info = null;
		while ((line = br.readLine()) != null) {
			double[] t = new double[2];
			info = cz.stringSplit(line);
			t[0] =  Double.parseDouble(info[1][0]);
			t[1] =  Double.parseDouble(info[1][1]);
			ovs.add(t);
		}
		br.close();
		fr.close();
		
		for (int k = 0; k < ovs.size(); k++) {
			str += ovs.get(k)[0]
					+ " "
					+ ovs.get(k)[1]
					+ "\n";
		}
		mData.write2File("/home/hadoop/Desktop/MOP_ZDT1/moead.txt", str);
	}
	private String[][] stringSplit(String str) {
		String[] numArray;
		int n = 0, m = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ';')
				n++;
		}
		numArray = str.trim().split(";");
		for (int i = 0; i < numArray[0].length(); i++) {
			if (numArray[0].charAt(i) == ',')
				m++;
		}
		String[][] a = new String[n][m + 1];
		for (int i = 0; i < n; i++)
			a[i] = numArray[i].trim().split(",");
		return a;
	}
}
