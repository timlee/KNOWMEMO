package log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileLog {
	String fName = "data/data/com.knowmemo/databases/Log.txt";
	BufferedWriter bw = null;
	String oldMsg = "";
	
	public FileLog(){
		File LogFile = new File(fName);
		try{
			if(!LogFile.exists())LogFile.createNewFile();
			BufferedReader br = new BufferedReader(new FileReader(fName));
			String sLine = "";
			while ((sLine = br.readLine()) != null) {
				oldMsg += sLine + "\r\n";
			}
			bw = new BufferedWriter(new FileWriter(fName));
		}catch(IOException er){}
	}
	
	public void writeData(String msg) throws IOException{
		bw.write(msg + oldMsg);
	}
	
	void close()throws IOException{
		bw.flush();
		bw.close();
	}
}
