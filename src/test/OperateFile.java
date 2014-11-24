package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/*public class OperateFile {

 *//**
 * @param args
 */
/*
 * public static void main(String[] args) {
 * 
 * }
 * 
 * }
 */

public class OperateFile implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "resource", "rawtypes" })
	public static void main(String[] args) throws Exception {
		OperateFile test = new OperateFile();
		Set<Student> num = new HashSet<Student>();
		num.add(test.new Student(1, "a"));
		num.add(test.new Student(2, "b"));
		num.add(test.new Student(3, "c"));
		FileOutputStream fos = new FileOutputStream("/home/hadoop/git/Git_workspace-new/moead/src/test/test.txt");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
//		oos.w
		oos.writeObject(num);

		FileInputStream fis = new FileInputStream("/home/hadoop/git/Git_workspace-new/moead/src/test/test.txt");
		ObjectInputStream ois = new ObjectInputStream(fis);
		Set<?> num2 = new HashSet();
		num2 = (Set<?>) ois.readObject();
		Iterator<?> it = num2.iterator();
		while (it.hasNext()) {
			Student stu = (Student) it.next();
			System.out.println(stu.getId() + " " + stu.getName());
		}
	}

	public class Student implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int id;
		private String name;

		public Student(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}