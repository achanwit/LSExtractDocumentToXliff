package com.omniscien.lsmsoffice.process;

import java.util.concurrent.atomic.AtomicReference;


import org.apache.commons.codec.binary.Base64;

import com.omniscien.lsmsoffice.model.ServletContextMock;
import com.omniscien.lsmsoffice.util.Common;
import com.omniscien.lsmsoffice.util.Log4J;
import com.omniscien.lsmsoffice.util.ProcessUtil;
import com.omniscien.lsmsoffice.util.ProcessUtil2;
import com.omniscien.lsmsoffice.util.ReadProp;


public class Extract{
//public class MSOffice{
	
	private static com.omniscien.lsmsoffice.util.Log4J oLog = null;
	private static Common oCommon = null;
	private static String sPageName = "extract.java";
	private ReadProp rp = null;
	//private static MSOffice instance = new MSOffice();

	public Extract() {
		
	}
	
	public void propertiesSetting(String filePath) {
		rp = new ReadProp(filePath);
		
		
}
	public boolean ExtractImageFromPDF(String inputODFFilePath, String outputPath, String prefixOfImageFile) {
		boolean extarctStatus = false;
		ServletContextMock app = new ServletContextMock();
		ProcessUtil2 oProcess = new ProcessUtil2();
		if (oLog == null) {
			oLog = new Log4J(app, rp);
//			oLog.debugMode = getDebugMode();
			oLog.debugMode = true;
//			oLog.setDebugPath(getDebugPath());
			oLog.setDebugPath(rp.getProp(com.omniscien.lsmsoffice.util.Constant.LOG_PATH));
//			oLog.log4JPropertyFile = getLog4JPath();
			oLog.log4JPropertyFile = rp.getProp(com.omniscien.lsmsoffice.util.Constant.LOG_4J);
		}
		if (oCommon == null) {
			oCommon = new Common();
		}
		try {
			oProcess = new ProcessUtil2(

					 rp.getProp(com.omniscien.lsmsoffice.util.Constant.ResourcesPath),
//				util.ConstantOfExtractDoc.ResourcesPath,
					 rp.getProp(com.omniscien.lsmsoffice.util.Constant.ABBYYExtension),
//				util.ConstantOfExtractDoc.ABBYYExtension,
					app,
					oLog,
					rp.getProp(com.omniscien.lsmsoffice.util.Constant.FontConfigPath),
//				util.ConstantOfExtractDoc.FontConfigPath,
					rp.getProp(com.omniscien.lsmsoffice.util.Constant.ABBYYPath),
//				util.ConstantOfExtractDoc.ABBYYPath,
					rp.getProp(com.omniscien.lsmsoffice.util.Constant.ABBYYGetInfoFileName),
//				util.ConstantOfExtractDoc.ABBYYGetInfoFileName,
					rp.getProp(com.omniscien.lsmsoffice.util.Constant.ABBYYWaitInterval),
//				util.ConstantOfExtractDoc.ABBYYWaitInterval,
					false,
					60,
					rp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		extarctStatus = oProcess.extractOnlyImageFromPDF(inputODFFilePath, outputPath, prefixOfImageFile);
		return extarctStatus;
			
		
	}
	
	public String ExtractXlifToString(String jobid, String inputpath, String  sourcelanguage, String  targetlanguage) {
		String sOutput = "";
		
		String format = "xliff";
		
		sOutput = ExtractXlifToString(jobid, inputpath, sourcelanguage, targetlanguage, format);
		
		return sOutput;
	}
	
	public String ExtractXlifToString(String jobid, String inputpath, String  sourcelanguage, String  targetlanguage, String format) {
		String sOutput = "";
		
		String outputpath = "";
		
		sOutput = Extract(jobid, inputpath, outputpath, sourcelanguage, targetlanguage, format);
		
		return sOutput;
	}
	
	public void ExtarctXliffToFile(String jobid, String inputpath, String outputpath, String  sourcelanguage, String  targetlanguage) {
		String sOutput = "";
		String format = "xliff";
		
		ExtarctXliffToFile(jobid, inputpath, outputpath, sourcelanguage, targetlanguage, format);
		
	
	}
	
	public void ExtractFileToParagraphText(String jobid, String inputpath, String outputpath) {
		String sOutput = "";
		String format = "txt";
		String sourcelanguage = "";
		String targetlanguage = "";
		
		Extract(jobid, inputpath, outputpath, sourcelanguage, targetlanguage, format);
		
		
	}
	
	
	
	public void ExtarctXliffToFile(String jobid, String inputpath, String outputpath, String  sourcelanguage, String  targetlanguage, String format) {
		String sOutput = "";
		
		sOutput = Extract(jobid, inputpath, outputpath, sourcelanguage, targetlanguage, format);
		
		
	}
	
	
	
	private String Extract(String jobid, String inputpath, String outputpath, String  sourcelanguage, String  targetlanguage, String format) {
		String sOutput = "";
		
		String service = "";
		
		String inputfilename = getFileNameFromPath(inputpath);
		String outputfilename = "";
		
		String fileType = getFileType(inputfilename);
		fileType.toLowerCase();
		
		if(fileType.equals("html") || fileType.equals("txt")) {
			inputfilename = inputfilename+".doc";
		}
		
		if(!outputpath.equals("")) {
			outputfilename = getFileNameFromPath(outputpath);
		}
				
		String inputcontent = "";
		
		ServletContextMock app = new ServletContextMock();
		
		boolean bMerge = false;
		
		AtomicReference<String> sDocxContent = null;
		
		try {
			sOutput = Extract(
					jobid,
					service,
					inputfilename,
					outputfilename,
					inputcontent,
					inputpath,
					outputpath,
					sourcelanguage,
					targetlanguage,
					format,
					app,
					bMerge,
					sDocxContent
					
					);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sOutput;
	}
	
	private String getFileType(String inputfilename) {
		String[] inputArr = inputfilename.split("\\.");
		return inputArr[inputArr.length-1];
	}

	private String getFileNameFromPath(String inputpath) {
		// TODO Auto-generated method stub
		String[] inputpathArr = inputpath.split("/");
		return  inputpathArr[inputpathArr.length-1];
	}

	private String Extract(
			//1 jobid
			String jobid,
			//2 service
			String service,
			//3 inputfilename
			String  inputfilename,
			String outputfilename,
			//4 inputcontent
			String  inputcontent,
			//5 inputpath
			String  inputpath,
			String outputpath,
			//6 sourcelanguage
			String  sourcelanguage,
			//7 targetlanguage
			String  targetlanguage,
			//8 format
			String format,
			//9 app
			ServletContextMock app,
			//10 bMerge
			boolean bMerge,
			//11 sDocxContent
			AtomicReference<String> sDocxContent) throws Exception {
		
		String sOutput = "";
		
		if (oLog == null) {
			oLog = new Log4J(app, rp);
//			oLog.debugMode = getDebugMode();
			oLog.debugMode = true;
//			oLog.setDebugPath(getDebugPath());
			oLog.setDebugPath(rp.getProp(com.omniscien.lsmsoffice.util.Constant.LOG_PATH));
//			oLog.log4JPropertyFile = getLog4JPath();
			oLog.log4JPropertyFile = rp.getProp(com.omniscien.lsmsoffice.util.Constant.LOG_4J);
		}
		if (oCommon == null) {
			oCommon = new Common();
		}
		
		oLog.WriteLog(sPageName, bMerge?"merge":"extract", "parameters inputcontent=" + oCommon.trimLog(inputcontent),jobid, false);
		oLog.WriteLog(sPageName, bMerge?"merge":"extract", "parameters inputfilename=" + inputfilename,jobid, false);
		oLog.WriteLog(sPageName, bMerge?"merge":"extract", "parameters inputpath=" + inputpath,jobid, false);
		oLog.WriteLog(sPageName, bMerge?"merge":"extract", "parameters service=" + service,jobid, false);
		oLog.WriteLog(sPageName, bMerge?"merge":"extract", "parameters sourcelanguage=" + sourcelanguage,jobid, false);
		oLog.WriteLog(sPageName, bMerge?"merge":"extract", "parameters targetlanguage=" + targetlanguage,jobid, false);
		oLog.WriteLog(sPageName, bMerge?"merge":"extract", "parameters format=" + format,jobid, false);
		
		//decode base64
		if (Base64.isBase64(inputcontent + ".")) {
			inputcontent = new String(Base64.decodeBase64(inputcontent));
		}
		
		//call extract to xliff
		ProcessUtil2 oProcess = new ProcessUtil2();
		
		 oProcess = new ProcessUtil2(

				 rp.getProp(com.omniscien.lsmsoffice.util.Constant.ResourcesPath),
//				util.ConstantOfExtractDoc.ResourcesPath,
				 rp.getProp(com.omniscien.lsmsoffice.util.Constant.ABBYYExtension),
//				util.ConstantOfExtractDoc.ABBYYExtension,
				app,
				oLog,
				rp.getProp(com.omniscien.lsmsoffice.util.Constant.FontConfigPath),
//				util.ConstantOfExtractDoc.FontConfigPath,
				rp.getProp(com.omniscien.lsmsoffice.util.Constant.ABBYYPath),
//				util.ConstantOfExtractDoc.ABBYYPath,
				rp.getProp(com.omniscien.lsmsoffice.util.Constant.ABBYYGetInfoFileName),
//				util.ConstantOfExtractDoc.ABBYYGetInfoFileName,
				rp.getProp(com.omniscien.lsmsoffice.util.Constant.ABBYYWaitInterval),
//				util.ConstantOfExtractDoc.ABBYYWaitInterval,
				false,
				60,
				rp

						);
		sOutput = oProcess.extract(jobid, service, inputfilename,outputfilename, inputcontent, inputpath, outputpath, sourcelanguage, targetlanguage, format, bMerge, sDocxContent);
//				
//		
		
		return sOutput;
	}
		

}
