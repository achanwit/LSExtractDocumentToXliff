package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class A4_TestExtractTextToParagraphTxt {

	public A4_TestExtractTextToParagraphTxt() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		long Begin = System.currentTimeMillis();
		Extract ms = new Extract();
		String jobId = "1";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/input/07_txt/sample3.txt";
		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/outputfile/07_txt/sample3.txt.txt";
		
	
		
		String output = ms.ExtractFileToParagraphText(jobId, inputPath, outputPath);

		
//		String output = ms.ExtarctXliffToFile(jobId, inputPath, outputPath, sourcelanguage, targetlanguage);
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
		System.out.println("output: "+output);
		System.exit(0);
	}

}
