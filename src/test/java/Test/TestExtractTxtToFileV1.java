package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class TestExtractTxtToFileV1 {

	public TestExtractTxtToFileV1() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		long Begin = System.currentTimeMillis();

		Extract ms = new Extract();
		String jobId = "253";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/input/07_txt/sample3.txt";
		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/outputfile/07_txt/sample3_V1.txt.xlf";
//		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/file-sample_500kB_v2.0.doc.xlf";
		
//		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/report.xlsx";
//		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/output_report.xlsx.xlf";
		
		String sourcelanguage = "EN";
		String targetlanguage = "DE";
		String format = "xliff";
		
		String output = ms.ExtarctXliffToFile(jobId, inputPath, outputPath, sourcelanguage, targetlanguage);
		//System.out.println("Output: "+output);
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
		System.out.println("output: "+output);
		System.exit(0);

	}

}
