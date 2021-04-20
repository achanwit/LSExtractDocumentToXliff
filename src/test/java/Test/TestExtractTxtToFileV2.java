package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class TestExtractTxtToFileV2 {

	public TestExtractTxtToFileV2() {
		// TODO Auto-generated constructor stub
	}

public static void main(String[] args) {
		
		long Begin = System.currentTimeMillis();
		Extract ms = new Extract();
		String jobId = "254";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/input/07_txt/sample3.txt";
		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/outputfile/07_txt/sample3_V2.txt.xlf";
		
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
