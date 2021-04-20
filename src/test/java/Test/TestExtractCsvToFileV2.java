package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class TestExtractCsvToFileV2 {

	public TestExtractCsvToFileV2() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		
		long Begin = System.currentTimeMillis();
		Extract ms = new Extract();
		String jobId = "1";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/input/08_csv/addresses.csv";
		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/outputfile/08_csv/addresses_V2.csv.xlf";
		
		String sourcelanguage = "EN";
		String targetlanguage = "DE";
		
		String format = "xliff2";
		
		String output = ms.ExtarctXliffToFile(jobId, inputPath, outputPath, sourcelanguage, targetlanguage, format);
		
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
		System.out.println("output: "+output);
		System.exit(0);
	}

}
