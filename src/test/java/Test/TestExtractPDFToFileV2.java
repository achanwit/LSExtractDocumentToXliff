package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class TestExtractPDFToFileV2 {

	public TestExtractPDFToFileV2() {
		// TODO Auto-generated constructor stub
	}
	
public static void main(String[] args) {
		
		long Begin = System.currentTimeMillis();
		Extract ms = new Extract();
		String jobId = "5";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/sample.pdf";
		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/sampleV2.pdf.xlf";
		
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
