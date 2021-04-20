package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class TestExtractHTMLTOFileV1 {

	public TestExtractHTMLTOFileV1() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		long Begin = System.currentTimeMillis();
		Extract ms = new Extract();
		String jobId = "2";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/input/02_html/JISZ4001_1999.html";
		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/outputfile/02_html/JISZ4001_1999.html.xlf";
		
		String format = "xliff";
		
		String sourcelanguage = "JA";
		String targetlanguage = "EN";
		
		String output = ms.ExtarctXliffToFile(jobId, inputPath, outputPath, sourcelanguage, targetlanguage);
				
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
		System.out.println("output: "+output);
		System.exit(0);

	}

}
