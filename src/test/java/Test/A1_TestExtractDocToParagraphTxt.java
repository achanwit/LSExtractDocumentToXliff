package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class A1_TestExtractDocToParagraphTxt {

	public A1_TestExtractDocToParagraphTxt() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		long Begin = System.currentTimeMillis();
		Extract ms = new Extract();
		ms.propertiesSetting("ExtractDocConfig.properties");
		String jobId = "1";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/input/10_msg/msg/sample.msg";
		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/outputfileParagraph/10_msg/sample.msg.txt";

		
//		String output = ms.ExtractFileToParagraphText(jobId, inputPath, outputPath);
		ms.ExtractFileToParagraphText(jobId, inputPath, outputPath);

		
//		String output = ms.ExtarctXliffToFile(jobId, inputPath, outputPath, sourcelanguage, targetlanguage);
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
//		System.out.println("output: "+output);
		System.exit(0);
	}

}
