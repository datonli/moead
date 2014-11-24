package test;

import java.io.*;  

public class IODemo  
{  
  
    public static void main(String[] args) throws Exception  
    {  
        FileInputStream fis = new FileInputStream("./PrintStream.txt");  
        // 为文件输入流加上字符处理功能  
        InputStreamReader isr = new InputStreamReader(fis);  
          
        // 从文件中读取字符  	
        int ch = 0;  
        while((ch = isr.read()) != -1)  
        {  
            System.out.print((char)ch);  
        }  
        System.out.println();  
          
        // 关闭流  
        isr.close();  
    }  
  
}  