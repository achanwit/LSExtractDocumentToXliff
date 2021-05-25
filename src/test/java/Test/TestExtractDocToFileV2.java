package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class TestExtractDocToFileV2 {

	public TestExtractDocToFileV2() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		long Begin = System.currentTimeMillis();
		Extract extract = new Extract();
		extract.propertiesSetting("ExtractDocConfig.properties");
		String jobId = "1";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/input/01_doc/file-sample_500kB.docx";
		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/outputfile/01_doc/file-sample_500kB_v2.0.doc.xlf";
		
		String format = "xliff2";
		
		String sourcelanguage = "EN";
		String targetlanguage = "DE";
		

		extract.ExtarctXliffToFile(jobId, inputPath, outputPath, sourcelanguage, targetlanguage, format);
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);

		System.exit(0);
	}

}
