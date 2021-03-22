package Test;

import extractDocument.MSOffice;

public class devTest {

	public devTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long Begin = System.currentTimeMillis();

		MSOffice ms = new MSOffice();
		String jobId = "45";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/file-sample_500kB.doc";
		String sourcelanguage = "DE";
		String targetlanguage = "EN";
		String format = "xliff";
		String output = ms.Extract(jobId, inputPath, sourcelanguage, targetlanguage, format);
		//System.out.println("Output: "+output);
		long End = System.currentTimeMillis();
		long Total = End-Begin;
		
		System.out.println("Total Time Process: "+Total);
	}

}
