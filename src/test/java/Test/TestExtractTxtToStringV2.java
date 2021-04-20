package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class TestExtractTxtToStringV2 {

	public TestExtractTxtToStringV2() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		long Begin = System.currentTimeMillis();

		Extract ms = new Extract();
		String jobId = "252";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/input/07_txt/sample3.txt";
//		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/file-sample_500kB_v1.2.doc.xlf";
//		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/file-sample_500kB_v2.0.doc.xlf";
		
//		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/report.xlsx";
//		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/output_report.xlsx.xlf";
		
		String sourcelanguage = "EN";
		String targetlanguage = "DE";
		String format = "xliff2";
		
		String output = ms.ExtractXlifToString(
				jobId, 
				inputPath, 
				sourcelanguage, 
				targetlanguage,
				format
				);
		//System.out.println("Output: "+output);
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
		System.out.println("output: "+output);
		System.exit(0);

	}
}
