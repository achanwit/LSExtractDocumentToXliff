package Test;

import com.omniscien.lsmsoffice.process.Merge;

public class TestMergeDoc {

	public TestMergeDoc() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		long Begin = System.currentTimeMillis();
		
		/** Start: Code Test **/
		
		//Variable
		String jobId = "1";
//		String inputfilename = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/input/01_doc/file-sample_500kB.doc";
		String inputfilename = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/merge/Test/20180318_Leadbelly_German_EIO_Annex_A_Proseuctor_powers_CPS_DE_DE.docx";
		String xliffpath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/merge/Test/20180318_Leadbelly_German_EIO_Annex_A_court_powers_CPS_DE_DE.out.docx.xlf";
		String sourcelanguage = "DE";
		String targetlanguage = "EN";
		String sOutputPath = "/home/chanwit/Documents/LSExtractDocument/A.PreStudy/merge/Test/result/output.doc";
		
		//New Object Merge
//		String jobid,
//		String inputfilename,
//		String xliffpath,
//		String sourcelanguage,
//		String targetlanguage,
//		String sOutputPath
		Merge merge = new Merge();
		
		String mergeResult = merge.Mergr(jobId, inputfilename, xliffpath, sourcelanguage, targetlanguage, sOutputPath);
		
		
		
		/** End: Code Test **/
		long End = System.currentTimeMillis();
		long Total = End-Begin;

		System.out.println("Total Time Process: "+Total);
		System.exit(0);
	}

}
