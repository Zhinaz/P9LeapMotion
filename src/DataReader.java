import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataReader {
	ArrayList<double[]> data = new ArrayList<double[]>();

	public DataReader(String filename) {
		File file = new File(filename);
		Scanner scan = null;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String numLine = scan.nextLine();
		int numberOfFeatures = numLine.split(",").length;
		String[] line = numLine.split(",");
		double[] temp = new double[numberOfFeatures];
		
		int badLine = 0;
		
		// Now read until we run out of lines
		while (scan.hasNext()) {
			temp = new double[numberOfFeatures];
			for (int i = 0; i < numberOfFeatures; i++) {
				//System.out.println(badLine + "     " + line.length + "     " + line[i]);
				double val = Double.parseDouble(line[i]);
				temp[i] = val;

			}
			data.add(temp);
			line = scan.nextLine().split(",");
			badLine++;
		}
		scan.close();
	}

	public ArrayList<double[]> getParsedData() {
		return data;
	}
}
