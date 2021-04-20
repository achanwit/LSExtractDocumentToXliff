package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class TestExtractHTMLTOString {

	public TestExtractHTMLTOString() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		long Begin = System.currentTimeMillis();
		Extract ms = new Extract();
		String jobId = "2";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/omniscien.html";
//		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/file-sample_500kB_v2.0.doc.xlf";
		
		String format = "xliff";
		
		String sourcelanguage = "EN";
		String targetlanguage = "DE";
		
		String output = ms.ExtractXlifToString(jobId, inputPath, sourcelanguage, targetlanguage);
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
		System.out.println("output: "+output);
		System.exit(0);

	}

}
