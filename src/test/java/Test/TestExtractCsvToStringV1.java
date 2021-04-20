package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class TestExtractCsvToStringV1 {

	public TestExtractCsvToStringV1() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		
		long Begin = System.currentTimeMillis();
		Extract ms = new Extract();
		String jobId = "1";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/input/08_csv/addresses.csv";
//		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/file-sample_500kB_v1.2.doc.xlf";
		
		String sourcelanguage = "EN";
		String targetlanguage = "DE";
		
//		String format = "xliff2";
		
		String output = ms.ExtractXlifToString(jobId, inputPath, sourcelanguage, targetlanguage);
		
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
		System.out.println("output: "+output);
		System.exit(0);
	}

}
