package textPage;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class FileToString {
	StringBuilder s = new StringBuilder();
	ArrayList<String> fileArr = new ArrayList<>();
	String fileName = "";
	// inputs to build string
	FileToString (StringBuilder sBld, ArrayList<String> arr, String str) {
		s = sBld;
		fileArr = arr;
		fileName = str;
	}
	// import file and append contents onto string
	public StringBuilder getString() {
		FileImporter fileIn = new FileImporter(fileName);
		try { // try to grab file and append contents to string
			fileArr = fileIn.getFile();
			for (int i = 0; i < fileArr.size(); i++) {
				s.append(fileArr.get(i).toString());
			}
		} catch (FileNotFoundException e) { // file not found
			e.printStackTrace();
		}
		return s;
	}
}
