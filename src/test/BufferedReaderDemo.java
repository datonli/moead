package test;

import java.io.*;  

class BufferedReaderDemo  
{  
  
    public static void main(String[] args) throws IOException  
    {  
        // 字符输入流  
        InputStreamReader inputReader = new InputStreamReader(System.in);  
        BufferedReader bufReader = new BufferedReader(inputReader); // 为输入加上缓冲功能  
          
        // 文件字符输出流  
        FileWriter fileWriter = new FileWriter("output.txt");  
        BufferedWriter bufWriter = new BufferedWriter(fileWriter); // 为输出流加上缓冲功能  
        String input = null;  
          
        // 每次从标准输入中读入一行字符，直到quit结束  
        while(!(input = bufReader.readLine()).equals("quit"))  
        {  
            bufWriter.write(input);  
            bufWriter.newLine(); // 输出与OS相关的换行符  
              
        }  
          
        // 关闭流  
        bufReader.close();  
        bufWriter.close();  
  
    }  
  
}  