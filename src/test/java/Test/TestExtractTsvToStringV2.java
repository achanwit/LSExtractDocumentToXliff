package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class TestExtractTsvToStringV2 {

	public TestExtractTsvToStringV2() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		
		long Begin = System.currentTimeMillis();
		Extract ms = new Extract();
		String jobId = "1";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/input/09_tsv/Sample.tsv";
//		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/outputfile/08_csv/addresses_V1.csv.xlf";
		
		String sourcelanguage = "EN";
		String targetlanguage = "DE";
		
		String format = "xliff2";
		
		String output = ms.ExtractXlifToString(jobId, inputPath, sourcelanguage, targetlanguage, format);
		
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
		System.out.println("output: "+output);
		System.exit(0);
	}

}
