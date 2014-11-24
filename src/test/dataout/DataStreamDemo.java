package test.dataout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.hadoop.io.Text;

public class DataStreamDemo {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		Member[] members = { new Member("Justin", 90), new Member("momor", 95),
				new Member("Bush", 88) };
		
		Text t = new Text();
		t.set("hello");
		System.out.println(t);
		System.out.println(t.toString());
		
		try {
			
			
			DataOutputStream dataOutputStream = new DataOutputStream(
					new FileOutputStream(
							"/home/hadoop/moead-experiment/DataStream.txt"));

			for (Member member : members) {
//				t.set(member.toString());
				dataOutputStream.writeBytes(member.toString() + "\n");
//				// 写入UTF字符串
//				dataOutputStream.writeUTF(member.getName());
//				// 写入int数据
//				dataOutputStream.writeInt(member.getAge());
			}

			// 所有数据至目的地
			dataOutputStream.flush();
			// 关闭流
			dataOutputStream.close();

		
			
			DataInputStream dataInputStream = new DataInputStream(
					new FileInputStream(
							"/home/hadoop/moead-experiment/DataStream.txt"));
			String readLineResult = dataInputStream.readLine();
			String name = readLineResult.toString().substring(0,readLineResult.toString().length()-3 );
			String score = readLineResult.toString().substring(readLineResult.toString().length()-2,readLineResult.toString().length() );
			System.out.println(name);
			System.out.println(score);
			int num = Integer.parseInt(score);
			System.out.println(num);
//			// 读出数据并还原为对象
//			for (int i = 0; i < members.length; i++) {
//				// 读出UTF字符串
//				String name = dataInputStream.readUTF();
//				// 读出int数据
//				int score = dataInputStream.readInt();
//				members[i] = new Member(name, score);
//			}
//
//			// 关闭流
			dataInputStream.close();
//
//			// 显示还原后的数据
//			for (Member member : members) {
//				System.out
//						.printf("%s\t%d%n", member.getName(), member.getAge());
//			}
//			
			
			
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
