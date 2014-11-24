package test;

import java.io.*;  

class FileReaderDemo  
{  
  
    public static void main(String[] args) throws Exception  
    {  
        FileReader fileReader = new FileReader("PrintStream.txt");  
        FileWriter fileWriter = new FileWriter("output.txt");  
          
        int ch = 0;  
        char[] wrapChar = new char[]{'\r','\n'};  
        while((ch = fileReader.read()) != -1)  
        {  
            if(ch == '\n') // 如果是换行符  
            {  
                // 写入 \r\n 换行符   
                fileWriter.write(wrapChar);  
            }  
            else  
            {  
                fileWriter.write(ch);  
            }  
        }  
          
        // 关闭流  
        fileReader.close();  
        fileWriter.close();  
    }  
  
}  