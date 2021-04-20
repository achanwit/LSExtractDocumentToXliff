package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class TestExtractPPTToFileV1 {

	public TestExtractPPTToFileV1() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		
		long Begin = System.currentTimeMillis();
		Extract ms = new Extract();
		String jobId = "1";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/samplepptx.pptx";
		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/samplepptx.pptx.xlf";
		
		String sourcelanguage = "EN";
		String targetlanguage = "DE";
		
		String output = ms.ExtarctXliffToFile(jobId, inputPath, outputPath, sourcelanguage, targetlanguage);
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
		System.out.println("output: "+output);
		System.exit(0);
	}

}
