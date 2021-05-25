package com.omniscien.lsmsoffice.process;

import java.util.UUID;
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
	
	
	
	public ReadProp getRp() {
		return rp;
	}



	public void setRp(ReadProp rp) {
		this.rp = rp;
	}



	/*** Common generate id ***/
	public String generateID() {
		String idStr = new String();
		idStr = UUID.randomUUID().toString();
		return idStr;
	}
	
	public void propertiesSetting(String filePath) {
		rp = new ReadProp(filePath);
		
		
	}
	
	public boolean ExtractImageFromPDF(String inputODFFilePath, String outputPath) throws Exception {
		String jobid = generateID();
		String prefixOfImageFile = "";
		
		return ExtractImageFromPDF(jobid, inputODFFilePath, outputPath, prefixOfImageFile);
	}
	
	public boolean ExtractImageFromPDF(String inputODFFilePath, String outputPath, String prefixOfImageFile) throws Exception {
		String jobid = generateID();		
		return ExtractImageFromPDF(jobid, inputODFFilePath, outputPath, prefixOfImageFile);
	}
	
	public String ExtractImageFromFile(String InputFilePath, String OutputDirectoryPath ) throws Exception {
		String result = "";
		int ImageType = 1;
		String PrefixImageFileName = "";
		result = ExtractImageFromFile(InputFilePath, OutputDirectoryPath, ImageType, PrefixImageFileName);
		return result;
	}
	
	public String ExtractImageFromFile(String InputFilePath, String OutputDirectoryPath, int ImageType ) throws Exception {
		String result = "";
		String PrefixImageFileName = "";
		result = ExtractImageFromFile(InputFilePath, OutputDirectoryPath, ImageType, PrefixImageFileName);
		return result;
	}
	
	public String  ExtractImageFromFile(String InputFilePath, String OutputDirectoryPath, int ImageType, String PrefixImageFileName) throws Exception {
		String result = "";
		
		//Prepare jod id
		String jobid = generateID();
		

		//Prepare Image Type
		String imageTypeName = null;
		if(ImageType == 1) {
			imageTypeName = ".jpg";
		}else if(ImageType == 2) {
			imageTypeName = ".png";
		}else if(ImageType == 3) {
			imageTypeName = ".tiff";
		}else if(ImageType == 4) {
			imageTypeName = ".bmp";
		}else if(ImageType == 5) {
			imageTypeName = ".gif";
		}else {
			return "Mistake ImageType input";
		}
		String status = ExtractImageFromFile(jobid, InputFilePath, OutputDirectoryPath, imageTypeName, PrefixImageFileName);
		return result;
	}
	
	private String ExtractImageFromFile(String jobid, String inputFilePath, String outputDirectoryPath,
			String imageTypeName, String prefixImageFileName) throws Exception {
		String result = null;
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
			throw e;
		}
		result = oProcess.extractOnlyImageFromFile(inputFilePath, outputDirectoryPath, imageTypeName, prefixImageFileName);
		return result;
	}

	public boolean ExtractImageFromPDF(String jobid, String inputODFFilePath, String outputPath, String prefixOfImageFile) throws Exception {
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
			throw e;
		}
		extarctStatus = oProcess.extractOnlyImageFromPDF(inputODFFilePath, outputPath, prefixOfImageFile);
		return extarctStatus;
			
		
	}
	
	public String ExtractXlifToString(String jobid, String inputpath, String  sourcelanguage, String  targetlanguage) throws Exception {
		String sOutput = "";
		
		String format = "xliff";
		
		sOutput = ExtractXlifToString(jobid, inputpath, sourcelanguage, targetlanguage, format);
		
		return sOutput;
	}
	
	public String ExtractXlifToString(String jobid, String inputpath, String  sourcelanguage, String  targetlanguage, String format) throws Exception {
		String sOutput = "";
		
		String outputpath = "";
		
		sOutput = Extract(jobid, inputpath, outputpath, sourcelanguage, targetlanguage, format);
		
		return sOutput;
	}
	
	public void ExtarctXliffToFile(String jobid, String inputpath, String outputpath, String  sourcelanguage, String  targetlanguage) throws Exception {
		String sOutput = "";
		String format = "xliff";
		
		ExtarctXliffToFile(jobid, inputpath, outputpath, sourcelanguage, targetlanguage, format);
		
	
	}
	
	public void ExtractFileToParagraphText(String jobid, String inputpath, String outputpath) throws Exception {
		String sOutput = "";
		String format = "txt";
		String sourcelanguage = "";
		String targetlanguage = "";
		
		Extract(jobid, inputpath, outputpath, sourcelanguage, targetlanguage, format);
		
		
	}
	
	
	
	public void ExtarctXliffToFile(String jobid, String inputpath, String outputpath, String  sourcelanguage, String  targetlanguage, String format) throws Exception {
		String sOutput = "";
		
		sOutput = Extract(jobid, inputpath, outputpath, sourcelanguage, targetlanguage, format);
		
		
	}
	
	public String Extratc(String InputFilePath, String OutputFilePath, String SourceLanguage, String TargetLanguage, int XliffVersion) throws Exception {
		//Prepare Job id
		String jobid = generateID();
		
		//Prepare XliffVersion
		String format = null;
		if(XliffVersion == 1) {
			format = "xliff";
		}else if(XliffVersion == 2) {
			format = "xliff2";
		}else {
			return "Mistake XliffVersion input";
		}
		
		//Prepare SourceLanguage
		if(SourceLanguage.length() == 2) {
			SourceLanguage = SourceLanguage.toLowerCase();
		}
		if(SourceLanguage.length() > 2) {
			SourceLanguage = SourceLanguage.substring(0, 2).toLowerCase();
		}
		if(SourceLanguage.length() < 2 || SourceLanguage == null) {
			return "Mistake SourceLanguage input";
		}
		
		//Prepare TargetLanguage
		if(TargetLanguage.length() == 2) {
			TargetLanguage = TargetLanguage.toLowerCase();
		}
		if(TargetLanguage.length() > 2) {
			TargetLanguage = TargetLanguage.substring(0, 2).toLowerCase();
		}
		if(TargetLanguage.length() < 2 || TargetLanguage == null) {
			return "Mistake TargetLanguage input";
		}
		
		//Process Extract
		String result = Extract(jobid, InputFilePath, OutputFilePath, SourceLanguage, TargetLanguage, format);
		
		return result;
	}
	
	
	
	private String Extract(String jobid, String inputpath, String outputpath, String  sourcelanguage, String  targetlanguage, String format) throws Exception {
		
		String sOutput = "";
		
		String service = "";
		
		String inputfilename = getFileNameFromPath(inputpath);
		String outputfilename = "";
		
		String fileType = getFileType(inputfilename);
		fileType.toLowerCase();
		
		if(fileType.equals("html") || fileType.equals("txt")) {
			inputfilename = inputfilename+".doc";
		}
		
		/*** PDF ->TXT ***/
		if(fileType.equals("pdf")){
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
			throw e;
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
