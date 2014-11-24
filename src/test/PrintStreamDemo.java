package test;

import java.io.*;  

public class PrintStreamDemo  
{  
    public static void main(String[] args) throws Exception  
    {  
        // 使用OutputStream对象输出  
        FileOutputStream fos = new FileOutputStream("./OutputStream.txt");  
        fos.write(1); // 写入整型数据1  
        fos.close();  
          
        // 使用PrintStream对象输出  
        PrintStream ps = new PrintStream(new FileOutputStream("./PrintStream.txt"));  
        ps.println(2);  // 写入整型  
        ps.println(3.14); // 写入double型  
        ps.println(new String("Hello World!")); // 写入String类型  

        ps.close();  
  
    }  
  
}  
