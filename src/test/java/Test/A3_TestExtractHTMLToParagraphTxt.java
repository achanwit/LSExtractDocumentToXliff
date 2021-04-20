package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class A3_TestExtractHTMLToParagraphTxt {

	public A3_TestExtractHTMLToParagraphTxt() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		long Begin = System.currentTimeMillis();
		Extract ms = new Extract();
		String jobId = "1";
		String inputPath = "/home/chanwit/Documents/01_HMRC_WorkFlow/Test/etc.html";
		String outputPath = "/home/chanwit/Documents/01_HMRC_WorkFlow/Test/etc.html.txt";
		
	
		
		String output = ms.ExtractFileToParagraphText(jobId, inputPath, outputPath);

		
//		String output = ms.ExtarctXliffToFile(jobId, inputPath, outputPath, sourcelanguage, targetlanguage);
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
		System.out.println("output: "+output);
		System.exit(0);
	}
}
