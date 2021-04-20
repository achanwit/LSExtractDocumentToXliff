package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class A2_TestExtractPDFToParagraphTxt {

	public A2_TestExtractPDFToParagraphTxt() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		long Begin = System.currentTimeMillis();
		Extract ms = new Extract();
		String jobId = "52";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/input/03_pdf/sample.pdf";
		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/outputfile/03_pdf/sample.pdf.txt";
		
		String sourcelanguage = "EN";
		String targetlanguage = "DE";
		
		String output = ms.ExtractFileToParagraphText(jobId, inputPath, outputPath);

		
//		String output = ms.ExtarctXliffToFile(jobId, inputPath, outputPath, sourcelanguage, targetlanguage);
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
		System.out.println("output: "+output);
		System.exit(0);
	}

}
