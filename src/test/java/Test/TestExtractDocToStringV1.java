package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class TestExtractDocToStringV1 {

	public TestExtractDocToStringV1() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long Begin = System.currentTimeMillis();

		Extract ms = new Extract();
		String jobId = "1";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/file-sample_500kB.doc";
//		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/file-sample_500kB_v1.2.doc.xlf";
//		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/file-sample_500kB_v2.0.doc.xlf";
		
//		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/report.xlsx";
//		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/output_report.xlsx.xlf";
		
		String sourcelanguage = "EN";
		String targetlanguage = "DE";
//		String format = "xliff";
		
		String output = ms.ExtractXlifToString(
				jobId, 
				inputPath, 
				sourcelanguage, 
				targetlanguage
				);
		//System.out.println("Output: "+output);
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
		System.out.println("output: "+output);
		System.exit(0);
	}

}
