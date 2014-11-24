package test;


import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.ObjectInputStream;  
import java.io.ObjectOutputStream;  
import java.io.Serializable;  
  
public class streamReadWrite {  
    public static void main(String[] args) throws Exception {  
        Person person = new Person(20, "hello", 3.45);  
        Person person2 = new Person(30, "world", 4.45);  
        Person person3 = new Person(40, "welcome", 5.45);  
  
        FileOutputStream fos = new FileOutputStream("person.txt");  
        ObjectOutputStream oos = new ObjectOutputStream(fos);  
  
        oos.writeObject(person);  
        oos.writeObject(person2);  
        oos.writeObject(person3);  
  
        oos.close();  
  
        FileInputStream fis = new FileInputStream("person.txt");  
        ObjectInputStream ois = new ObjectInputStream(fis);  
        Person resPerson = (Person) ois.readObject();  
        System.out.println(resPerson.getAge() + "," + resPerson.getName() + ","  
                + resPerson.getHeight());  
        Person resPerson2 = (Person) ois.readObject();  
        System.out.println(resPerson2.getAge() + "," + resPerson2.getName()  
                + "," + resPerson2.getHeight());  
        Person resPerson3 = (Person) ois.readObject();  
        System.out.println(resPerson3.getAge() + "," + resPerson3.getName()  
                + "," + resPerson3.getHeight());  
ois.close();  
    }  
}  
  
@SuppressWarnings("serial")
class Person implements Serializable {  
    int age;  
    transient String name;  
    double height;  
  
    public Person(int age, String name, double height) {  
        this.age = age;  
        this.name = name;  
        this.height = height;  
    }  
  
    public int getAge() {  
        return age;  
    }  
  
    public void setAge(int age) {  
        this.age = age;  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
  
    public double getHeight() {  
        return height;  
    }  
  
    public void setHeight(double height) {  
        this.height = height;  
    }  
  
}  
