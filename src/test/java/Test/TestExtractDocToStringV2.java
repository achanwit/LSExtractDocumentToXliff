package Test;

import com.omniscien.lsmsoffice.process.Extract;

public class TestExtractDocToStringV2 {

	public static void main(String[] args) {
		long Begin = System.currentTimeMillis();

		Extract ms = new Extract();
		String jobId = "2";
		String inputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/file-sample_500kB.doc";
		
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
