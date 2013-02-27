package ge.zura.android.autofines.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SaveInput {
	
	private static FileWriter writer;

	public static void savePlateToFile(String input, File file){

//		File file = new File(Environment.getExternalStorageDirectory() + File.separator + "plateNumbers.txt");

			try {
				writer = new FileWriter(file, true);
				writer.append(input + "\n");
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
	}
	
	public static ArrayList<String> readFromFile(File file) throws IOException{
		
		ArrayList<String> list = new ArrayList<String>();
		BufferedReader reader = null;
		String line;
		
		try {
			InputStream in = new FileInputStream(file);
			InputStreamReader inReader = new InputStreamReader(in);
			reader = new BufferedReader(inReader);
			
			
			while((line = reader.readLine()) != null && !list.contains(line)) {
				list.add(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;		
	}

}
