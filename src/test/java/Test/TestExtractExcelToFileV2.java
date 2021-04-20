package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class TestExtractExcelToFileV2 {

	public TestExtractExcelToFileV2() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		
		long Begin = System.currentTimeMillis();
		Extract ms = new Extract();
		String jobId = "1";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/input/04_xlsx/report.xlsx";
		String outputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/outputfile/04_xlsx/report.xlsx_V2.xlf";
		
		String sourcelanguage = "EN";
		String targetlanguage = "DE";
		
		String format = "xliff2";
		
		String output = ms.ExtarctXliffToFile(jobId, inputPath, outputPath, sourcelanguage, targetlanguage, format);
		
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
		System.out.println("output: "+output);
		System.exit(0);
	}

}
