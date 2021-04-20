package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class TestExtractDocToFileV1 {

	public TestExtractDocToFileV1() {

	}

	public static void main(String[] args) {
		
		long Begin = System.currentTimeMillis();
		Extract ms = new Extract();
		String jobId = "1";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/file-sample_500kB.doc";
		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/file-sample_500kB_v1.2.doc.xlf";
		
		String sourcelanguage = "EN";
		String targetlanguage = "DE";
		
		String output = ms.ExtarctXliffToFile(jobId, inputPath, outputPath, sourcelanguage, targetlanguage);
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
		System.out.println("output: "+output);
		System.exit(0);
	}

}
