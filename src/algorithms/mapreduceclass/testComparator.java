package algorithms.mapreduceclass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


class Student
{
	public String name;
	public int age;
	
	public Student(String name,int age)
	{
		this.name = name;
		this.age = age;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
}

class SortByAge implements Comparator<Student>
{
	public int compare(Student o1,Student o2)
	{
		int age1 = o1.getAge();
		int age2 = o2.getAge();
		if(age1 < age2)
			return -1;
		return 0;
	}
}

class SortByName implements Comparator<Student>
{
	public int compare(Student o1,Student o2)
	{
//		Student c1 = (Student)o1;
//		Student c2 = (Student)o2;
		return o1.getName().compareTo(o2.getName());
	}
}


public class testComparator {
	public static void main(String[] args) {
		Student a = new Student("Lee",11);
		Student b = new Student("Yang",10);
		Student c = new Student("Gou",1);
		List<Student> stuList = new ArrayList<Student>();
		stuList.add(a);
		stuList.add(b);
		stuList.add(c);
		
		System.out.println(stuList.get(0).age + " " + stuList.get(0).name);
		System.out.println(stuList.get(1).age + " " + stuList.get(1).name);
		System.out.println(stuList.get(2).age + " " + stuList.get(2).name);
		
		System.out.println("");

		
		Collections.sort(stuList, new SortByName());
		System.out.println(stuList.get(0).age + " " + stuList.get(0).name);
		System.out.println(stuList.get(1).age + " " + stuList.get(1).name);
		System.out.println(stuList.get(2).age + " " + stuList.get(2).name);
		System.out.println("");
		
		Collections.sort(stuList, new SortByAge());
		System.out.println(stuList.get(0).age + " " + stuList.get(0).name);
		System.out.println(stuList.get(1).age + " " + stuList.get(1).name);
		System.out.println(stuList.get(2).age + " " + stuList.get(2).name);
		
	
		return;
	}
	
	

}
