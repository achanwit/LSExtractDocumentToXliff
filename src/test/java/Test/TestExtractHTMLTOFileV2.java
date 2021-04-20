package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class TestExtractHTMLTOFileV2 {

	public TestExtractHTMLTOFileV2() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		long Begin = System.currentTimeMillis();
		Extract ms = new Extract();
		String jobId = "2";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/omniscien.html";
		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/omniscienV2.html.xlf";
		
		String format = "xliff2";
		
		String sourcelanguage = "EN";
		String targetlanguage = "DE";
		
		String output = ms.ExtarctXliffToFile(jobId, inputPath, outputPath, sourcelanguage, targetlanguage, format);
				
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
		System.out.println("output: "+output);
		System.exit(0);

	}

}
