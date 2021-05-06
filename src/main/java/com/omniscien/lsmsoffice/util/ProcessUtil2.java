package com.omniscien.lsmsoffice.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.mozilla.universalchardet.UniversalDetector;
import org.w3c.dom.Document;

import com.omniscien.lsmsoffice.util.Base64Coder;
import com.omniscien.lsmsoffice.util.FontConfig;

import com.omniscien.lsmsoffice.util.FontConfig.LangPair;
import com.omniscien.lsmsoffice.util.FontConfig.Fonts;
import com.aspose.cells.CalcModeType;
import com.aspose.cells.Cell;
import com.aspose.cells.Cells;
import com.aspose.cells.Color;
import com.aspose.cells.FontSetting;
import com.aspose.cells.NumberCategoryType;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.email.EmlLoadOptions;
import com.aspose.email.MailMessage;
import com.aspose.pdf.ImageType;
import com.aspose.pdf.Page;
import com.aspose.pdf.PageCollection;
import com.aspose.pdf.XImageCollection;
import com.aspose.pdf.facades.PdfConverter;
import com.aspose.pdf.facades.PdfExtractor;
import com.aspose.slides.IParagraph;
import com.aspose.slides.IPortion;
import com.aspose.slides.ITextFrame;
import com.aspose.slides.Presentation;
import com.aspose.slides.SlideUtil;
import com.aspose.slides.internal.mw.avi;
import com.aspose.words.ControlChar;
import com.aspose.words.ListFormat;
import com.aspose.words.NumberStyle;
import com.aspose.words.Run;
import com.aspose.words.StyleCollection;
import com.glaforge.i18n.io.CharsetToolkit;
import com.google.gson.Gson;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import com.omniscien.lsmsoffice.model.ServletContextMock;
import com.omniscien.lsmsoffice.util.Registry;

import net.sf.okapi.common.Util;



public class ProcessUtil2 {
	//
	private ReadProp rp = null;
	private String resourcepath;
	String productid;
	String abbyyextension;
	String fontConfigPath;
	ServletContextMock app;
	Log4J oLog = null;
	Common oCommon = new Common();
	private PropertieseService propertieseService; 
	String pageName = "ProcessUtil.java";
	Registry reg = null;
	//
	List<String> extWordList = Arrays.asList(new String[]{
			"doc","docx","dot","dotx","docm","odt","ott","rtf"});	
	List<String> extCellList = Arrays.asList(new String[]{
			"xls","xlsx","xlsb","xlsm","xlt","xltx","xltm","xlsm","ods","csv","tsv"});	
	List<String> extEmailList = Arrays.asList(new String[]{
			"msg", "pst", "ost", "oft", "olm","eml", "emlx", "mbox"});	
	List<String> extSlideList = Arrays.asList(new String[]{
			"ppt","pot","pps", "pptx", "potx", "ppsx", "ppsm", "potm","otp","odp"});
	List<String> extOpenofficeList = Arrays.asList(new String[]{
			"odt","ott","ods","odp","otp","odg","otg"});
	List<String> abbyyExtToXliffList = Arrays.asList(new String[]{
			"pdf","bmp","gif","jpg","jpeg","pcx","png","tif","tiff"});
	
	List<Integer> chineseNumberList = Arrays.asList(new Integer[]{
			33, 35, 37, 39});
	List<Integer> countingNumberStyle = Arrays.asList(new Integer[]{
			10, 11, 17,  33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 44});
	List<Integer> commaUnicode = Arrays.asList(new Integer[]{
			12289, 65105, 65380});
	List<Integer> fullStopUnicode = Arrays.asList(new Integer[]{
			12290, 65294, 65377});
	List<String> firstStringSkip = Arrays.asList(new String[]{
			" ",".",",", ")", "}", "]"});
	List<String> LastStringSkip = Arrays.asList(new String[]{
			" ", "\"", "(", "{", "["});
	List<String> nonLanguage = Arrays.asList(new String[] {
			"ZH", "TH", "KO", "JA"});
	
	Map<String, Integer> mSaveFormatSlide = new HashMap<String, Integer>() {{
        put("ppt",  0);
        put("pot", 	20);
        put("pps", 	19);
        put("pptx", 3);
        put("potx", 10);
        put("ppsx", 4);
        put("ppsm", 9);
        put("potm", 11);
        put("otp", 	17);
        put("odp", 	6);
    }};
	//
    
    List<Integer> lSkipCellExtract = Arrays.asList(new Integer[]{
    		NumberCategoryType.DATE, NumberCategoryType.TIME});
	   
    Pattern pNumber = Pattern.compile("-?\\d+(\\.\\d+)?");
    
    //for check OCR Available
	private HashMap<Integer, JobInfo> jobsInprocess = new HashMap<Integer, JobInfo>();
	private Boolean pdfFeatureOption = false;
	private String abbyyLicenseNumber = "";
	//default wait interval 
	private Integer cpuCores = 1, defaultWaitInterval = 10;
	private String abbyyPath = "", abbyyGetInfoFileName = "", abbyyWaitInterval = "";
	private String jobsInProcessKey = "msows-jip";
	private Boolean isSharedCPUCoresMode = false;
	//
	private Thread tMonitorJobProcess = null;
	private boolean bStartThreadMonitorJobProcess = false;
	private Integer iTimeIntervalMonitor = 300000;//300 seconds = 5minutes
	private Integer iTimeToRemoveJobMinutes = 60;
	//
	
	private String sSpacialCharPattern = "([\\u0000\\u001E\\u000C\\u000E\\u001F\\u000B\\u0009])";
	
	


	public ProcessUtil2() {
//		System.out.println("HELLO!!");
		// TODO Auto-generated constructor stub
		
	}
	
	public ProcessUtil2(
			String _resourcespath, 
			String _abbyyextension, 
			ServletContextMock _app, 
			Log4J _oLog, 
			String _fontConfigPath, 
			String _abbyyPath, 
			String _abbyyGetInfoFileName, 
			String _abbyyWaitInterval, 
			Boolean _isSharedCPUCoresMode, 
			Integer timeToRemoveJobMinutes, 
			ReadProp rp
			) throws Exception {
		this.rp = rp;
		this.propertieseService = new PropertieseService();
		app = _app;
		resourcepath = _resourcespath;
		abbyyPath = _abbyyPath;
		abbyyGetInfoFileName = _abbyyGetInfoFileName;
		abbyyWaitInterval = _abbyyWaitInterval;
		isSharedCPUCoresMode = _isSharedCPUCoresMode;
		iTimeToRemoveJobMinutes = timeToRemoveJobMinutes;
		oLog = _oLog;
//		reg = new Registry(_app);
//		getABBYYInfo();
		abbyyextension = _abbyyextension;
		fontConfigPath = _fontConfigPath;
		activateAsposeLicense();
		
//		checkStartThreadMonitorJobProcess();
		
	}
	
	public class JobInfo {
		public String jobid = "";
		public Integer percent = 0;
		public Double filesize = 0.0;
		public Integer waitTime = 0;
		public String status = "";
		public Date startdate = null;
		public Date updatedate = null;
	}
	
	public void activateAsposeLicense() throws Exception {
		String setAsposeKey = "msows_setaspose";
		Boolean bSetAspose = false;
		if (app != null && app.getAttribute(setAsposeKey) != null) {
			bSetAspose = (Boolean)app.getAttribute(setAsposeKey);
		}
		if (!bSetAspose) {
			bSetAspose = setAsposeLicense();
			if (app != null)
				app.setAttribute(setAsposeKey, bSetAspose);
		}
	}
	
	public void getABBYYInfo() throws Exception {
		productid = getProductid();
		abbyyLicenseNumber = getABBYYLicense();
//		cpuCores = getCPUCores("");
		defaultWaitInterval = getABBYYTimeInterval();
	}
	
	public String getProductid() throws Exception {
		String sProductid = "", sAppKey = "msows_productid";
		String sPDFAppKey = "msows_pdfoption";
		try {
			if (app != null && app.getAttribute(sPDFAppKey) != null) {
				pdfFeatureOption = oCommon.ChkNullBooleanObj(app.getAttribute(sPDFAppKey));
			} else {
				//pdfFeatureOption = oCommon.ChkNullBooleanObj(reg.getLookupDataRegistry("LSETS", "PDFFeatureOption"));
				pdfFeatureOption = Boolean.parseBoolean(oCommon.ChkNullStrObj(propertieseService.getPropertiesValue("PDFFeatureOption")));
				if (app != null)
					app.setAttribute(sPDFAppKey, pdfFeatureOption);
			}
			
			//check option first
			if (pdfFeatureOption) {
				if (app != null && app.getAttribute(sAppKey) != null) {
					sProductid = app.getAttribute(sAppKey).toString();
				} else {
					//sProductid = reg.getLookupDataRegistry("LSETS", "ABBYYCustomerProductID");
					sProductid = propertieseService.getPropertiesValue("ABBYYCustomerProductID");
					if (sProductid == null)
						sProductid = "";
					if (app != null)
						app.setAttribute(sAppKey, sProductid);
				}
			}		

			oLog.WriteLog(pageName, "getProductid", "PDFFeatureOption=" + pdfFeatureOption.toString() + ", HaveProductId=" + (sProductid.trim().length() > 0? "yes": "no"), "", false);
			
		} catch (Exception e) {
			oLog.WriteLog(pageName, "getProductid", oLog.getStackTrace(e), "", true);
		}
		return sProductid;
	}
	
	public String getABBYYLicense() throws Exception {
		String sLicienseNo = "", sAppKey = "msows_abl";
		try {
			if (!pdfFeatureOption) {
				return "";
			}
			
			if (app != null && app.getAttribute(sAppKey) != null) {
				sLicienseNo = app.getAttribute(sAppKey).toString();
			} else {
				//sLicienseNo = reg.getLookupDataRegistry("LSETS", "ABBYYLicenseKey");
				sLicienseNo = propertieseService.getPropertiesValue("ABBYYLicenseKey");
				if (sLicienseNo == null)
					sLicienseNo = "";
				if (app != null)
					app.setAttribute(sAppKey, sLicienseNo);
			}

			oLog.WriteLog(pageName, "getProductid", "ABBYYLicenseNumber=" + sLicienseNo, "", false);
			
		} catch (Exception e) {
			oLog.WriteLog(pageName, "getABBYYLicense", oLog.getStackTrace(e), "", true);
		}
		return sLicienseNo;
	}
	
	private Integer getCPUCores(String jobId) throws Exception {
		String sAppKey = "msows_abbyycpu";
		Integer cpuCores = -1;
		try {
			if (oCommon.IsEmpty(abbyyLicenseNumber)) {
				return -1;
			}
			if (app != null && app.getAttribute(sAppKey) != null) {
				cpuCores = (Integer) app.getAttribute(sAppKey);
			}
			if (cpuCores < 1) {
				//call function get cpu cores
				try {
					String shellPath = oLog.combine(abbyyPath, abbyyGetInfoFileName);
					if (!oCommon.fileExists(shellPath))
						shellPath = oLog.combine(resourcepath + "abbyy", abbyyGetInfoFileName);
					if (!oCommon.fileExists(shellPath))
						throw new Exception(shellPath + " does not exist.");
					
					String command = shellPath + " " + abbyyLicenseNumber;					
					oLog.WriteLog(pageName, "getCPUCores", "executeshell=" + command, jobId, false);
					String output = "Copyright (c) 2018 ABBYY Production LLC. All rights reserved.\n" + 
							"Parameters of SWTR-1201-1007-0160-3084-9378\n" + 
							"    : \n" + 
							"     Description : \n" + 
							"       Functionality Subset : Runtime Professional\n" + 
							"       Protection Type : Software (File)\n" + 
							"       Serial Number : SWTR-1201-1007-0160-3084-9378\n" + 
							"       Expiration Date : 31 days left\n" + 
							"     Productivity : \n" + 
							"       CPU cores : 2\n" + 
							"       CPU cores per station, minimum : 1\n" + 
							"       Productivity limit : Unlimited\n" + 
							"     Volume : \n" + 
							"       Regular texts : \n" + 
							"         Quantity : Unlimited\n" + 
							"";//oCommon.executeShell(command, 2, 10, oLog);
					oLog.WriteLog(pageName, "getCPUCores", "output=" + output, jobId, false);
					Pattern pCPU = Pattern.compile("(CPU[ ]cores[:= ]{1,})(?<value>[0-9]+)", Pattern.CASE_INSENSITIVE);
					Matcher mCPU = pCPU.matcher(output);
					if (pCPU.matcher(output).find()) {
						String cpuNo = "";
		            	while (mCPU.find()) {
			            	cpuNo = oCommon.ChkNullStrObj(mCPU.group("value"));
			            	if (cpuNo.trim().length() > 0)
			            		break;
		            	}
			            if (cpuNo.trim().length() == 0)
			            	throw new Exception("Cannot get CPU Cores.");
			            cpuCores = oCommon.ChkNullIntObj(cpuNo);
					}
					if (app != null)
						app.setAttribute(sAppKey, cpuCores);
				} catch (Exception ex) {
					oLog.WriteLog(pageName, "getCPUCores", oLog.getStackTrace(ex), jobId, true);
					//set default
					cpuCores = 1;
				}
			}
			oLog.WriteLog(pageName, "getCPUCores", "cpuCores=" + cpuCores, jobId, false);
			
		} catch (Exception e) {
			oLog.WriteLog(pageName, "getCPUCores", oLog.getStackTrace(e), jobId, true);
		}
		return cpuCores;
	}
	
	public Integer getABBYYTimeInterval() throws Exception {
		String sAppKey = "msows_abtime";
		Integer time = 0;
		try {
			if (!pdfFeatureOption) {
				return 0;
			}
			
			if (app != null && app.getAttribute(sAppKey) != null) {
				time = (Integer) app.getAttribute(sAppKey);
			} else {
//				time = oCommon.ChkNullIntObj(reg.getLookupDataRegistry("LSETS", "ABBYYTimeInterval"));
				time = oCommon.ChkNullIntObj(propertieseService.getPropertiesValue("ABBYYTimeInterval"));
				if (app != null)
					app.setAttribute(sAppKey, time);
			}

			oLog.WriteLog(pageName, "getABBYYTimeInterval", "ABBYYTimeInterval=" + time, "", false);
			
		} catch (Exception e) {
			oLog.WriteLog(pageName, "getABBYYTimeInterval", oLog.getStackTrace(e), "", true);
		}
		return time;
	}
	
	private Boolean setAsposeLicense() throws Exception {
		Boolean bSetAspose = true;
		try {
			oLog.WriteLog(pageName, "setAsposeLicense", "", "", false);
			com.aspose.words.License licW = new com.aspose.words.License();
			licW.setLicense(IOUtils.toInputStream(loadAsposeLicense()));
			
			com.aspose.cells.License licC = new com.aspose.cells.License();
			licC.setLicense(IOUtils.toInputStream(loadAsposeLicense()));
			
			com.aspose.slides.License licS = new com.aspose.slides.License();
			licS.setLicense(IOUtils.toInputStream(loadAsposeLicense()));
			
			com.aspose.email.License licE = new com.aspose.email.License();
			licE.setLicense(IOUtils.toInputStream(loadAsposeLicense()));
	
//			com.aspose.diagram.License licD = new com.aspose.diagram.License();
//			licD.setLicense(IOUtils.toInputStream(loadAsposeLicense()));
				
			com.aspose.pdf.License licP = new com.aspose.pdf.License();
			licP.setLicense(IOUtils.toInputStream(loadAsposeLicense()));
			
		} catch (Exception e) {
			bSetAspose = false;
			oLog.WriteLog(pageName, "setAsposeLicense", oLog.getStackTrace(e), "", true);
		}
		return bSetAspose;
	}
	
private String loadAsposeLicense() {
		
		StringBuilder sbLicense = new StringBuilder();
//		sbLicense.append("<License>\r\n");
//		sbLicense.append("  <Data>\r\n");
//		sbLicense.append("    <LicensedTo>Asia Online Pte Ltd</LicensedTo>\r\n");
//		sbLicense.append("    <EmailTo>greg.binger@omniscien.com</EmailTo>\r\n");
//		sbLicense.append("    <LicenseType>Developer OEM</LicenseType>\r\n");
//		sbLicense.append("    <LicenseNote>Limited to 1 developer, unlimited physical locations</LicenseNote>\r\n");
//		sbLicense.append("    <OrderID>191014055551</OrderID>\r\n");
//		sbLicense.append("    <UserID>135027412</UserID>\r\n");
//		sbLicense.append("    <OEM>This is a redistributable license</OEM>\r\n");
//		sbLicense.append("    <Products>\r\n");
//		sbLicense.append("      <Product>Aspose.Total for Java</Product>\r\n");
//		sbLicense.append("    </Products>\r\n");
//		sbLicense.append("    <EditionType>Enterprise</EditionType>\r\n");
//		sbLicense.append("    <SerialNumber>cdd715c6-ce3b-442a-bac9-a7ee85c471c3</SerialNumber>\r\n");
//		sbLicense.append("    <SubscriptionExpiry>20201015</SubscriptionExpiry>\r\n");
//		sbLicense.append("    <LicenseVersion>3.0</LicenseVersion>\r\n");
//		sbLicense.append("    <LicenseInstructions>https://purchase.aspose.com/policies/use-license</LicenseInstructions>\r\n");
//		sbLicense.append("  </Data>\r\n");
//		sbLicense.append(" \r\n");
//		sbLicense.append("<Signature>bPoEFCdXTWM4HgjFmZ5xPqQgW7JLoLRda4vLgJMyshfqz6h7jwEoUGPzpT5NFdOhxncxc4Oq4jK4OkvMyTxHBBVwIUBFSoqb1lw9FIFa7reU+sK21AlHgg3zb3lrbfG2YvrOGvmEZVEqFPpWhlH8QrJkrOWYnn8UHsfb11eaCYs=</Signature>\r\n");
//		sbLicense.append("</License>\r\n");
		sbLicense.append(rp.getProp("License"));
		return sbLicense.toString();
	}

	private void checkStartThreadMonitorJobProcess() throws Exception {
		// check start thread insert/update DB
		if (app != null && app.getAttribute("msows_smjp") != null) {
			bStartThreadMonitorJobProcess = (Boolean) app.getAttribute("msows_smjp");
		}
		// oLog.WriteLog(pageName, "checkStartThreadMonitorJobProcess",
		// "bStartThreadMonitorJobProcess=" + bStartThreadMonitorJobProcess, "", false);
		if (!bStartThreadMonitorJobProcess) {
			boolean bFirstRequest = false;
			synchronized (app) {
				if (app.getAttribute("FirstRequest") == null) {
					app.setAttribute("FirstRequest", true);
					bFirstRequest = true;
				}
			}
			if (bFirstRequest)
				callStartThreadMonitorJobProcess();
		}
	}
	
	private void callStartThreadMonitorJobProcess() throws Exception {
		if (bStartThreadMonitorJobProcess == false) {
			bStartThreadMonitorJobProcess = true;
			if (app != null) {
				app.setAttribute("msows_smjp", bStartThreadMonitorJobProcess);
			}

			oLog.WriteLog(pageName, "callStartThreadMonitorJobProcess", "Start Thread Montior Job In Process.", "", false);
			startThreadMonitorJobProcess();
		}
	}
	
	private void startThreadMonitorJobProcess() {
		// every xx seconds will delete job in process
		if (tMonitorJobProcess == null) {
			tMonitorJobProcess = new Thread("msof_monitorjobprocess") {
				public void run() {
					if (iTimeIntervalMonitor <= 0)
						iTimeIntervalMonitor = 300000;// 5 minutes
					while (true) {
						try {
							if ((jobsInprocess == null || jobsInprocess.size() == 0)
									&& app != null && app.getAttribute(jobsInProcessKey) != null) {
								jobsInprocess = (HashMap<Integer, JobInfo>) app.getAttribute(jobsInProcessKey);
							}
							if (jobsInprocess != null && jobsInprocess.size() > 0) {
								HashMap<Integer, JobInfo> hmJobsInProcess = CopyHashMap(jobsInprocess);
								//List<Integer> alJobsRemove = new ArrayList<Integer>();
								//Boolean bRemove = false;
								try {
									//check startdate and update date
									for (Iterator<Map.Entry<Integer, JobInfo>> it = hmJobsInProcess.entrySet().iterator(); it.hasNext();) {
										Map.Entry<Integer, JobInfo> entry = it.next();
										Integer jobId = entry.getKey();
										JobInfo jobInfo = entry.getValue();
										//
										if (jobInfo.startdate == null)
											continue;

										SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
										Calendar cal = Calendar.getInstance(TimeZone.getDefault());
										Date startDate = dateFormat.parse(dateFormat.format(jobInfo.startdate));
										Date endDate = dateFormat.parse(dateFormat.format(cal.getTime()));
										if (jobInfo.updatedate != null) {
											startDate = dateFormat.parse(dateFormat.format(jobInfo.updatedate));
										}
										//compare start and updatedate
										//int diff = endDate.compareTo(startDate);
										long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
										long diffInMinutes = (diffInMillies / (1000 * 60)) % 60; 
										if(diffInMinutes > iTimeToRemoveJobMinutes) {
											//if (!alJobsRemove.contains(jobId))
											//	alJobsRemove.add(jobId);
											if (jobsInprocess != null && jobsInprocess.containsKey(jobId)) {
												jobsInprocess.remove(jobId);
												oLog.WriteLog(pageName, "startThreadMonitorJobProcess", "removed", jobId.toString(), false);
												if (app != null)
													app.setAttribute(jobsInProcessKey, jobsInprocess);
											}
										}
									}
									//bRemove = true;
								} catch (Exception e) {
									oLog.WriteLog(pageName, "startThreadMonitorJobProcess", "remove job error=" + oLog.getStackTrace(e), "", true);
								} finally {
									//if (bRemove && alJobsRemove.size() > 0) {
									//	app.setAttribute(jobsInProcessKey, jobsInprocess);
									//}
								}
							}
						} catch (Exception ex) {
							if (!oLog.getStackTrace(ex).contains("ConcurrentModificationException"))
								oLog.WriteLog(pageName, "startThreadMonitorJobProcess", oLog.getStackTrace(ex), "", true);
						}
						// sleep
						try {
							Thread.sleep(iTimeIntervalMonitor);
						} catch (InterruptedException e) { }

					}

				}
			};
			tMonitorJobProcess.start();
		}
	}
	
	private HashMap<Integer, JobInfo> CopyHashMap(HashMap<Integer, JobInfo> hmSource) {
		HashMap<Integer, JobInfo>hmTarget = new HashMap<Integer, JobInfo>();
		try {
			HashMap<Integer, JobInfo> hmTemp = new HashMap<Integer, JobInfo>(hmSource);
			hmTemp.keySet().removeAll(hmTarget.keySet());
			hmTarget.putAll(hmTemp);
		} catch (Exception e) {
		}
		return hmTarget;
	}
	
	public String extract(
			String jobID,
			String service,
			String inputFileName,
			String outputFilename,
			String inputContent,
			String inputPath,
			String outputPath,
			String sLangSource,
			String sLangTarget,
			String format,
			boolean bMerge,
			AtomicReference<String> sDocxContent) throws Exception
			{
				String output = "";
				
				try {
					File workingFile = null;
					String workingPath = "";
					
					//Input is file
					if (inputPath.length() > 0) {

						File inputFile = new File(inputPath);
						if (inputFile.exists())
						{
							workingPath = oLog.debugPath + "/temp/" + service + File.separator + jobID + File.separator + inputFileName;
							workingFile = new File(workingPath);
							if (!workingFile.getParentFile().exists()) workingFile.getParentFile().mkdirs();
							if (bMerge && workingFile.exists())
							{
								File extractFile = new File(workingPath+".extract");
								if (!extractFile.exists())
								{
									FileUtils.moveFile(workingFile, extractFile);
									oLog.WriteLog(pageName, "extract", "backup input extract file", jobID, false);
								}
							}
							FileUtils.copyFile(inputFile,workingFile);
						}
						else
						{
							String message = "input file doesn't exist";
							oLog.WriteLog(pageName, "extract", message, jobID, true);
							throw new Exception(message);
						}
					
					}
					//Input is filesteam
					else 
					{
						workingPath = oLog.debugPath + "/temp/" + service + File.separator + jobID + File.separator + inputFileName;
						workingFile = new File(workingPath);
						if (!workingFile.getParentFile().exists()) workingFile.getParentFile().mkdirs();
						byte[] bData = Base64Coder.decode(inputContent);
						FileUtils.writeByteArrayToFile(workingFile, bData);
					}
					oLog.WriteLog(pageName, "extract", "working path=" + workingPath, jobID, false);
					
					//list of file extension that will need to ocr first (pdf,bmp,gif,jpg,jpeg,pcx,png,tif,tiff)
					if (abbyyextension.length() > 0) {
						abbyyExtToXliffList = Arrays.asList(abbyyextension.split(",")); 
					}
					
					//check file extension
					String ext = FilenameUtils.getExtension(workingFile.getPath()).toLowerCase();
					if (format.length() == 0)
					{
						//set default extract format
						if (extEmailList.contains(ext)) { 
							format = "html";
						}else if (extWordList.contains(ext) 
								|| extCellList.contains(ext) 
								|| extSlideList.contains(ext) 
								|| abbyyExtToXliffList.contains(ext)) {
							format = "xliff";
						}
					}
					
					try {
						//extract to html
						if (format.equals("html")) {
							
							output = extractToHtml( workingFile, inputFileName,ext, jobID);
						
						}
						//extract to text
						else if (format.equals("text")) 
						{
							
							output = extractToText( workingFile, inputFileName,ext, jobID);
						}
						else if (format.equals("xliff")) 
						{
							if (abbyyExtToXliffList.contains(ext) && bMerge==false) //extract to docx first
							{					
//								if (productid != null && productid.length() > 0)
//								{
									/*
									boolean bAvailable = false;
									synchronized (app) {
										HashMap<String, Object> hmOut = checkOCRAvailable(jobID);
										if (oCommon.ChkNullBooleanObj(hmOut.get("isavailable"))) {
											bAvailable = true;
										} else {
											throw new Exception("OCR is not available.");
										}
									}
									if (bAvailable) {
										//convert pdf to docx by ABBYY
										convertToDocxByABBYY(workingFile, sLangSource, sLangTarget, sDocxContent,jobID);								
									}
									*/
									//convert pdf to docx by ABBYY
//									convertToDocxByABBYY(workingFile, sLangSource, sLangTarget, sDocxContent,jobID);
//								}
//								else
//								{
									if (ext.equals("pdf"))
									{
										//try convert by aspose
										String originalPath  = workingFile.getPath();
										String currentPath = workingFile.getPath() + ".docx";
										
										com.aspose.pdf.Document doc = new com.aspose.pdf.Document(originalPath);
										doc.save(currentPath, com.aspose.pdf.SaveFormat.DocX);
										
										byte[] bData = FileUtils.readFileToByteArray(new File(currentPath));
										sDocxContent = new AtomicReference<String>();
										sDocxContent.set(new String(Base64Coder.encode(bData)));	
									}else {
										throw new Exception("This conversation not support yet.");	
									}
//								}
															
								//udpate workingFile
								workingFile = new File(workingPath + ".docx");
								
								//extract process
								output = extractToXliffAspose( workingFile, inputFileName,ext, sLangSource ,  sLangTarget, jobID, outputPath);			
							} 
							else if (extWordList.contains(ext) 
									|| (abbyyExtToXliffList.contains(ext) && bMerge==true))  //case pdf use docx to merge (output is docx)
							{
									//extract process
									output = extractToXliffAspose(workingFile, inputFileName,ext, sLangSource ,  sLangTarget, jobID, outputPath);				
							}
							else if (extCellList.contains(ext))
							{
								try {
									
									//extract process
//									output = extractToXliffAkapi(workingFile, inputFileName,ext, sLangSource ,  sLangTarget, jobID);		
									//20200402: Ramoslee: Change to use Aspose.
									output = extractToXliffAsposeExcell(workingFile, inputFileName,ext, sLangSource ,  sLangTarget, jobID, outputPath);		
									
								} catch (Exception e) {
									
									//try convert to new format
									if(ext.equals("tsv")) {
										String csvFilePath = workingFile.getPath().replace(".tsv", ".csv");
										String tsvFilePath = workingFile.getPath();
										convertTSVToCSVFile(csvFilePath, tsvFilePath);
										workingFile = new File(csvFilePath);
										
										
									}else {
										convertToNewFormatForExtract(workingFile ,  jobID ,  ext);							
									}
									oLog.WriteLog(pageName, "extract", "retry extract again input=" + workingPath, jobID, false);			
									
									//extract process
									//output = extractToXliffAkapi(workingFile, inputFileName,ext, sLangSource ,  sLangTarget, jobID);		
									//20200402: Ramoslee: Change to use Aspose.
									output = extractToXliffAsposeExcell(workingFile, inputFileName,ext, sLangSource ,  sLangTarget, jobID, outputPath);		
								}	
							/*20200331: Ramoslee : Change to use ASPOSE to extract xliff*/
							}else if (extSlideList.contains(ext)) {
								output = extractToXliffAsposeSlide(workingFile, inputFileName, ext, sLangSource, sLangTarget, jobID, outputPath);
							}else if(extEmailList.contains(ext)) {
								output = extractToXliffAsposeEmail( workingFile, inputFileName,ext, sLangSource ,  sLangTarget, jobID, outputPath);
//								System.out.println("");
							}
							/*Ende 20200331: Ramoslee : Change to use ASPOSE to extract xliff*/
							
						}else if(format.equals("xliff2")) {
							if (abbyyExtToXliffList.contains(ext) && bMerge==false) //extract to docx first
							{
								if (ext.equals("pdf"))
								{
									//try convert by aspose
									String originalPath  = workingFile.getPath();
									String currentPath = workingFile.getPath() + ".docx";
									
									com.aspose.pdf.Document doc = new com.aspose.pdf.Document(originalPath);
									doc.save(currentPath, com.aspose.pdf.SaveFormat.DocX);
									
									byte[] bData = FileUtils.readFileToByteArray(new File(currentPath));
									sDocxContent = new AtomicReference<String>();
									sDocxContent.set(new String(Base64Coder.encode(bData)));	
								}else {
									throw new Exception("This conversation not support yet.");	
								}
//							}
														
							//udpate workingFile
							workingFile = new File(workingPath + ".docx");
							
							//extract process
							output = extractToXliffV2Aspose( workingFile, inputFileName,ext, sLangSource ,  sLangTarget, jobID, outputPath);	
							
							}else if (extWordList.contains(ext) 
									|| (abbyyExtToXliffList.contains(ext) && bMerge==true))  //case pdf use docx to merge (output is docx)
							{
								//extract process
								
								output = extractToXliffV2Aspose(workingFile, inputFileName,ext, sLangSource ,  sLangTarget, jobID, outputPath);
							}else if (extCellList.contains(ext)){
								try {
									output = extractToXliffAsposeExcellV2(workingFile, inputFileName,ext, sLangSource ,  sLangTarget, jobID, outputPath);		
								} catch (Exception e) {
									
									if(ext.equals("tsv")) {
										String csvFilePath = workingFile.getPath().replace(".tsv", ".csv");
										String tsvFilePath = workingFile.getPath();
										convertTSVToCSVFile(csvFilePath, tsvFilePath);
										workingFile = new File(csvFilePath);
										
										
									}else {
										convertToNewFormatForExtract(workingFile ,  jobID ,  ext);							
									}							
									
									oLog.WriteLog(pageName, "extract", "retry extract again input=" + workingPath, jobID, false);			
									
									
									output = extractToXliffAsposeExcellV2(workingFile, inputFileName,ext, sLangSource ,  sLangTarget, jobID, outputPath);		
								}	
							
							}else if (extSlideList.contains(ext)) {
								output = extractToXliffAsposeSlideV2(workingFile, inputFileName, ext, sLangSource, sLangTarget, jobID, outputPath);
							}
						}else if(format.equals("txt")) {

							if (abbyyExtToXliffList.contains(ext) && bMerge==false) //extract to docx first
							{
								if (ext.equals("pdf"))
								{
									//try convert by aspose
									String originalPath  = workingFile.getPath();
									String currentPath = workingFile.getPath() + ".docx";
									
									com.aspose.pdf.Document doc = new com.aspose.pdf.Document(originalPath);
									doc.save(currentPath, com.aspose.pdf.SaveFormat.DocX);
									
									byte[] bData = FileUtils.readFileToByteArray(new File(currentPath));
									sDocxContent = new AtomicReference<String>();
									sDocxContent.set(new String(Base64Coder.encode(bData)));	
								}else {
									throw new Exception("This conversation not support yet.");	
								}
//							}
														
							//udpate workingFile
							workingFile = new File(workingPath + ".docx");
							
							/*** extract process
							File workingFile,
							String inputFileName,
							String ext,
							String sLangSource , 
							String sLangTarget,
							String jobID, 
							String outputPath ***/
							output = extractToTxtAspose(workingFile, inputFileName, ext, sLangSource, sLangTarget, jobID, outputPath);
//							output = extractToXliffV2Aspose( workingFile, inputFileName,ext, sLangSource ,  sLangTarget, jobID, outputPath);	
							
							}else if (extWordList.contains(ext) 
									|| (abbyyExtToXliffList.contains(ext) && bMerge==true))  //case pdf use docx to merge (output is docx)
							{
								//extract process
								
								output = extractToTxtAspose(workingFile, inputFileName,ext, sLangSource ,  sLangTarget, jobID, outputPath);
							}else if (extCellList.contains(ext)){
								try {
									output = extractToXliffAsposeExcellV2(workingFile, inputFileName,ext, sLangSource ,  sLangTarget, jobID, outputPath);		
								} catch (Exception e) {
									
									if(ext.equals("tsv")) {
										String csvFilePath = workingFile.getPath().replace(".tsv", ".csv");
										String tsvFilePath = workingFile.getPath();
										convertTSVToCSVFile(csvFilePath, tsvFilePath);
										workingFile = new File(csvFilePath);
										
										
									}else {
										convertToNewFormatForExtract(workingFile ,  jobID ,  ext);							
									}							
									
									oLog.WriteLog(pageName, "extract", "retry extract again input=" + workingPath, jobID, false);			
									
									
									output = extractToXliffAsposeExcellV2(workingFile, inputFileName,ext, sLangSource ,  sLangTarget, jobID, outputPath);		
								}	
							
							}else if (extSlideList.contains(ext)) {
								output = extractToXliffAsposeSlideV2(workingFile, inputFileName, ext, sLangSource, sLangTarget, jobID, outputPath);
							}
						
						}
						
					}catch (Exception e1) {
						throw e1;
					}
					
				}
				catch ( Exception e ) {
					oLog.WriteLog(pageName, "extract",  oLog.getStackTrace(e), jobID, true);
					throw e;
				}
				
				return output;
			}
	
	private void convertTSVToCSVFile(String csvFilePath, String tsvFilePath) throws IOException {
		 StringTokenizer tokenizer;
		 try (BufferedReader br = new BufferedReader(new FileReader(tsvFilePath));
				 PrintWriter writer = new PrintWriter(new FileWriter(csvFilePath));) {
			 
			 int i = 0;
	            for (String line; (line = br.readLine()) != null; ) {
	                i++;
	                if (i % 10000 == 0) {
	                    System.out.println("Processed: " + i);

	                }
	                tokenizer = new StringTokenizer(line, "\t");

	                String csvLine = "";
	                String token;
	                while (tokenizer.hasMoreTokens()) {
	                    token = tokenizer.nextToken().replaceAll("\"", "'");
	                    csvLine += "\"" + token + "\",";
	                }

	                if (csvLine.endsWith(",")) {
	                    csvLine = csvLine.substring(0, csvLine.length() - 1);
	                }

	                writer.write(csvLine + System.getProperty("line.separator"));

	            }
			 
		 }
		
	}

	private String extractToHtml(File workingFile,String inputFileName,String ext,String jobID) //Aspose used
			throws Exception {

		
		String root = workingFile.getParent();
		String htmlPath = root + File.separator + "pack1"+File.separator+"work"+File.separator + inputFileName +".html"; 
		File htmlFile = new File(htmlPath);
		if (!htmlFile.getParentFile().exists()) htmlFile.getParentFile().mkdirs();
		
		if (extSlideList.contains(ext))
		{
			//extract slide to html
			com.aspose.slides.Presentation presentation = new com.aspose.slides.Presentation(workingFile.getPath());
			presentation.save(htmlPath,com.aspose.slides.SaveFormat.Html);
		}
		else if (extWordList.contains(ext) || abbyyExtToXliffList.contains(ext))
		{
			//extract word to html
			com.aspose.words.Document doc = new com.aspose.words.Document(workingFile.getPath());
			com.aspose.words.HtmlSaveOptions options= new com.aspose.words.HtmlSaveOptions();
			options.setExportImagesAsBase64(true);
			doc.save(htmlPath,options);
			//doc.save(htmlPath,com.aspose.words.SaveFormat.HTML);
		}
		else if (extEmailList.contains(ext))
		{
			//extract email to html
			com.aspose.email.MailMessage message = com.aspose.email.MailMessage.load(workingFile.getPath());
			message.save(htmlPath,com.aspose.email.SaveOptions.getDefaultHtml());
			
		}
		else if (extCellList.contains(ext))
		{
			//extract cell to html
			com.aspose.cells.Workbook workbook = new com.aspose.cells.Workbook(workingFile.getPath());
			com.aspose.cells.HtmlSaveOptions options= new com.aspose.cells.HtmlSaveOptions();
			options.setExportImagesAsBase64(true);
			workbook.save(htmlPath,options);
			//workbook.save(htmlPath,com.aspose.cells.SaveFormat.HTML);
		}
		
		try {
			java.io.File fileIn = new java.io.File(htmlPath);
			String sEncoding = GetEncoding(fileIn.toString());
			
			if ( !StandardCharsets.UTF_8.toString().equalsIgnoreCase(sEncoding) ) {
				FileUtils.copyFile(fileIn, new File(htmlPath + "_temp.html"));
				java.io.File source = new File(htmlPath + "_temp.html");
				EncodingConvert(source.toString(), fileIn.toString(), sEncoding, StandardCharsets.UTF_8.toString());
				source.deleteOnExit();
			}
		}catch (Exception e) {
			// TODO: handle exception
			oLog.WriteLog(pageName, "extractToHtml", "output=" + htmlPath + " Encoding :: error=" + e.getMessage(), jobID, false);
		}
		
		
		
		
		oLog.WriteLog(pageName, "extract", "output=" + htmlPath, jobID, false);
		
		return FileUtils.readFileToString(new File(htmlPath), "UTF-8");
			
	}
	
	public String GetEncoding(String FilePath) throws Exception
	{
	  String winningEncoding = "";
	  int maxLine = 1000;
	  
	  File file = new File(FilePath);
	  java.io.FileInputStream fis =null;
	  ByteArrayOutputStream output = null;
	  if (!file.exists())
			throw new Exception("Input file doesn't exits.");
	  try {						
		    // Load input data
		    long count = 0;
		    int n = 0, EOF = -1;
		    byte[] buffer = new byte[4096];
			 
			fis = new java.io.FileInputStream(FilePath);
		    output = new ByteArrayOutputStream();
		    //while ((EOF != (n = fis.read(buffer))) && (count <= Integer.MAX_VALUE)) {
		    while ((EOF != (n = fis.read(buffer))) && (count <= maxLine)) {
		        output.write(buffer, 0, n);
		        count ++;
		    }
		    
		    byte[] data = output.toByteArray();

		    // Detect encoding
		    Map<String, int[]> encodingsScores = new HashMap<>();

		    // * GuessEncoding
		    updateEncodingsScores(encodingsScores, new CharsetToolkit(data).guessEncoding().displayName());

		    // * ICU4j
		    CharsetDetector charsetDetector = new CharsetDetector();
		    charsetDetector.setText(data);
		    charsetDetector.enableInputFilter(true);
		    CharsetMatch cm = charsetDetector.detect();
		    if (cm != null) {
		        updateEncodingsScores(encodingsScores, cm.getName());
		    }

		    // * juniversalchardset
		    UniversalDetector universalDetector = new UniversalDetector(null);
		    universalDetector.handleData(data, 0, data.length);
		    universalDetector.dataEnd();
		    String encodingName = universalDetector.getDetectedCharset();
		    if (encodingName != null) {
		        updateEncodingsScores(encodingsScores, encodingName);
		    }

		    // Find winning encoding
		    Map.Entry<String, int[]> maxEntry = null;
		    for (Map.Entry<String, int[]> e : encodingsScores.entrySet()) {
		        if (maxEntry == null || (e.getValue()[0] > maxEntry.getValue()[0])) {
		            maxEntry = e;
		        }
		    }

		     winningEncoding = maxEntry.getKey();

		} catch (Exception e) {
			throw e;
		}finally {
			if (null != output)
				output.close();
			if (null != fis)
				fis.close();
		}
	  //System.out.println(winningEncoding);
	    return winningEncoding;
	}
	
	private void updateEncodingsScores(Map<String, int[]> encodingsScores, String encoding) {
	    String encodingName = encoding.toLowerCase();
	    int[] encodingScore = encodingsScores.get(encodingName);

	    if (encodingScore == null) {
	        encodingsScores.put(encodingName, new int[] { 1 });
	    } else {
	        encodingScore[0]++;
	    }
	}
	
	public void EncodingConvert(String FilePath, String OutputFilePath, String SourceEncoding, String TargetEncoding) throws Exception
	{
		BufferedReader brInput = null;	
		BufferedWriter bwOutput = null;
		try {	
		    Reader rdInput = new InputStreamReader(new FileInputStream(FilePath),SourceEncoding);		       			
	        brInput = new BufferedReader(rdInput);	
	        java.io.File oFile = new java.io.File(OutputFilePath);
			if (!oFile.getParentFile().exists())
				oFile.getParentFile().mkdirs();
	        Writer wOutput = new OutputStreamWriter(new FileOutputStream(OutputFilePath), TargetEncoding);
	        bwOutput = new BufferedWriter(wOutput);		    
	        String inputLine; 
	        int iLine = 0;
	        while ( (inputLine = brInput.readLine()) != null) {      	
	        	if (iLine == 0)
	        	{
	        		//bwOutput.write("\ufeff");
	        		bwOutput.write(RemoveBOM(inputLine) + GetNewline());
	        	}
	        	else
		        	bwOutput.write(inputLine  + GetNewline());
	        	iLine++;
	        }	    

		} catch (Exception e) {
			throw e;
		}
		finally
		{
			if (brInput != null)
				brInput.close();
			if (bwOutput != null)
				bwOutput.close();
		}
	}
	
	public String RemoveBOM(String text) {
		String newStr = "";
		try {
			byte[] bArray = text.getBytes();
			if (bArray[0] == -17) {
				byte[] newArray = new byte[bArray.length - 3];
				System.arraycopy(bArray, 3, newArray, 0, bArray.length - 3);
				newStr = new String(newArray);
			} else {
				newStr = text;
			}
		} catch (Exception e) {
			
		}
		return newStr;
	}
	
	public String GetNewline(){
		 
		String os = System.getProperty("os.name").toLowerCase();
		//windows
	    boolean isWidow = (os.indexOf( "win" ) >= 0); 
	    if (isWidow)
	    	return "\r\n";
	    else
	    	return "\n";
	}
	
	private String extractToText(File workingFile,String inputFileName,String ext,String jobID)
			throws Exception {
		
		String root = workingFile.getParent();
		String txtPath = root + File.separator + "pack1"+File.separator+"work"+File.separator + inputFileName +".txt"; 
		File txtFile = new File(txtPath);
		if (!txtFile.getParentFile().exists()) txtFile.getParentFile().mkdirs();
		
		if (extSlideList.contains(ext))
		{
			//extract slide to text
			com.aspose.slides.Presentation pres;
			com.aspose.slides.ISlideCollection slides;

			try ( Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(txtPath, false), StandardCharsets.UTF_8 )) ) {
				pres = new com.aspose.slides.Presentation(workingFile.getPath());
				slides = pres.getSlides();

			    for (int i = 0; i < slides.size(); i++) {
			    	com.aspose.slides.ISlide slide = slides.get_Item(i);

			    	for (int j = 0; j < slide.getShapes().size(); j++) {
			    		com.aspose.slides.IShape shape = slide.getShapes().get_Item(j);
			    		if(shape instanceof com.aspose.slides.AutoShape){
			    			if (((com.aspose.slides.IAutoShape)shape).getTextFrame() != null) {
			    				writer.write(slidesExtractFonts(((com.aspose.slides.IAutoShape) shape).getTextFrame()) );
			    			}
			    		} else if(shape instanceof com.aspose.slides.LegacyDiagram) {
			    			com.aspose.slides.LegacyDiagram legacy=(com.aspose.slides.LegacyDiagram)shape;
			    			com.aspose.slides.ISmartArt smart=legacy.convertToSmartArt();
			    			for(com.aspose.slides.ISmartArtNode node:smart.getAllNodes()){
			    				if(node.getTextFrame()!=null){
			    					writer.write(slidesExtractFonts(node.getTextFrame()));
			    				}
			    			}
			    		} else if(shape instanceof com.aspose.slides.SmartArt) {
			    			com.aspose.slides.ISmartArt smart=(com.aspose.slides.ISmartArt)shape;
			    			for(com.aspose.slides.ISmartArtNode node:smart.getAllNodes()){
			    				if(node.getTextFrame()!=null){
			    					writer.write(slidesExtractFonts(node.getTextFrame()));
			    				}
			    			}
			    		} else if (shape instanceof com.aspose.slides.Table) {
			    			com.aspose.slides.ITable table=(com.aspose.slides.ITable)shape;
			    			for(int u=0;u<table.getRows().size();u++){
			    				for(int v=0;v<table.getColumns().size();v++){
			    					com.aspose.slides.ICell cell=table.get_Item(v, u);
			    					if(cell.getTextFrame()!=null ){
				    					writer.write(slidesExtractFonts(cell.getTextFrame()));
			    					}
			    				}
			    			}
			    		}
			    	}
			    }
			} catch (Exception e) {
				throw e;
			} finally {
				slides = null; pres = null;
			}
		}
		else if (extWordList.contains(ext) || abbyyExtToXliffList.contains(ext) )
		{
			//extract word to text
			com.aspose.words.Document doc = new com.aspose.words.Document(workingFile.getPath());
			doc.save(txtPath,com.aspose.words.SaveFormat.TEXT);

		}
		else if (extEmailList.contains(ext))
		{
			//extract email to text			
			com.aspose.email.MailMessage message;
			try ( Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(txtPath, false), StandardCharsets.UTF_8 )) ) {
				message = com.aspose.email.MailMessage.load(workingFile.getPath());
				writer.write("From: " + message.getFrom()+"\n");
				writer.write("To: " + message.getTo()+ "\n");
				writer.write("Cc: " + message.getCc()+ "\n");
				writer.write("Subject: "+ message.getSubject()+ "\n");
				writer.write("Date: "+ message.getDate()+ "\n");
				writer.write("Body: " + "\n" + message.getHtmlBodyText());
			} catch (Exception e) {
				throw e;
			} finally {
				message = null;
			}
		}
		else if (extCellList.contains(ext))
		{
			//extract cell to text
			com.aspose.cells.Workbook workbook;
			try ( Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(txtPath, false), StandardCharsets.UTF_8 )) ) {
				workbook = new com.aspose.cells.Workbook(workingFile.getPath());
				// Text save options. You can use any type of separator
				com.aspose.cells.TxtSaveOptions opts = new com.aspose.cells.TxtSaveOptions();
				opts.setSeparator('\t');
				// Copy each worksheet data in text format inside workbook data array
				for (int idx = 0; idx < workbook.getWorksheets().getCount(); idx++) {
					byte[] workbookData = new byte[0];// 0-byte array
					// Save the active worksheet into text format
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					workbook.getWorksheets().setActiveSheetIndex(idx);
					workbook.save(bout, opts);

					// Save the worksheet data into sheet data array
					byte[] sheetData = bout.toByteArray();

					// Combine this worksheet data into workbook data array
					byte[] combinedArray = new byte[workbookData.length + sheetData.length];
					System.arraycopy(workbookData, 0, combinedArray, 0, workbookData.length);
					System.arraycopy(sheetData, 0, combinedArray, workbookData.length, sheetData.length);

					workbookData = combinedArray;
					writer.write(new String(workbookData, StandardCharsets.UTF_8));
				}
			} catch (Exception e) {
				throw e;
			} finally {
				workbook=null; 
			}
		}
		oLog.WriteLog(pageName, "extract", "output=" + txtPath, jobID, false);
		
		return FileUtils.readFileToString(new File(txtPath), "UTF-8");	
	}
	
	private String slidesExtractFonts(com.aspose.slides.ITextFrame tf2) {
		StringBuilder sbFrame = new StringBuilder();
		for (int k = 0; k < tf2.getParagraphs().getCount(); k++) {
			com.aspose.slides.IParagraph paragraph = tf2.getParagraphs().get_Item(k);
			List paragraphs = new ArrayList();
			for (int n = 0; n < paragraph.getPortions().getCount(); n++) {
				com.aspose.slides.IPortion portion = paragraph.getPortions().get_Item(n);
				com.aspose.slides.IPortionFormat pformat=portion.getPortionFormat();
				paragraphs.add(portion.getText());
			}
			sbFrame.append(String.join("", paragraphs )).append("\n");
		}
		return sbFrame.toString();
	}
	
	private void convertToDocxByABBYY(File workingFile, String sLangSource, String sLangTarget, AtomicReference<String> sDocxContent, String jobid) throws Exception
	{
		String originalPath  = workingFile.getPath();
		String currentPath = workingFile.getPath() + ".docx";

		//insert job in memory for check ocr available
		//InsertJobInprocess(jobid, 0, oCommon.calculateFileSize(workingFile.length(), "kb"), "queued");
		synchronized (app) {
			PDFConverter oPDFConverter = new PDFConverter();
			try {
				oLog.WriteLog(pageName, "extract", "abbyy: start convert pdf to docx input=" + originalPath, jobid, false);
				UpdateJobInprocess(jobid, 1, oCommon.calculateFileSize(workingFile.length(), "kb"), "processing");
				oPDFConverter.Run(originalPath, currentPath, productid, sLangSource, sLangTarget, isSharedCPUCoresMode, oLog);
				oLog.WriteLog(pageName, "extract", "abbyy: end convert output=" + currentPath, jobid, false);
				
			} catch (Exception e) {
				oLog.WriteLog(pageName, "extract", oLog.getStackTrace(e), jobid, true);
				if (e.getMessage().contains("has access restrictions"))
				{
					oLog.WriteLog(pageName, "extract", "try to unlock restrictions", jobid, true);
					oCommon.RunLinuxCommand(resourcepath + "/pdfutilities/pdfunlock.sh " + originalPath);
					String unlockPath = originalPath.replace(".original", "_unlocked.original");
					if (new File(unlockPath).exists())
					{	
						oLog.WriteLog(pageName, "extract", "unlock restrictions completed", jobid, true);
						FileUtils.moveFile(new File(originalPath),new File(originalPath + ".restrictions"));	
						FileUtils.moveFile(new File(unlockPath),new File(originalPath));	
						try {
							oPDFConverter.Run(originalPath, currentPath, productid,sLangSource, sLangTarget, isSharedCPUCoresMode, oLog);
						} catch (Exception e2) {
							throw e;
						}
					}
					else
					{
						throw new Exception("Unlock the input file that has access restrictions failed.");
					}
				}
				if (e.getMessage().contains("password-protected"))
				{					
					throw new Exception("The input file is password-protected.");
				}
				else 
					throw e;
			} finally {
				//remove job in process
				RemoveJobInprocess(jobid);
			}
		}					

		byte[] bData = FileUtils.readFileToByteArray(new File(currentPath));
		sDocxContent.set(new String(Base64Coder.encode(bData)));	
	}
	
	private String extractToXliffV2Aspose(File workingFile,String inputFileName,String ext,
			String sLangSource , String sLangTarget,
			String jobID, String outputPath) throws Exception {
		String output = "";
		
		try {
			//create working folder
			String root = workingFile.getParent();
			String xliffPath = root + File.separator + "pack1"+File.separator+"work"+File.separator + inputFileName +".xlf"; 
			File xliffFile = new File(xliffPath);
			if (!xliffFile.getParentFile().exists()) xliffFile.getParentFile().mkdirs();		

			oLog.WriteLog(pageName, "extract", "start extract", jobID, false);
			
			if (extWordList.contains(ext) || abbyyExtToXliffList.contains(ext)) {
				com.aspose.words.Document doc = null;
				com.aspose.words.NodeCollection paragraphs = null;
				com.aspose.words.NodeCollection runs = null;	
				
				try ( Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xliffFile, false), StandardCharsets.UTF_8 )) ) {

					writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
							+ "<xliff version=\"2.0\" xmlns=\"urn:oasis:names:tc:xliff:document:2.0\" srcLang=\""+sLangSource+"\" trgLang=\""+sLangTarget+"\">\n"
//							+ "<file original=\""+inputFileName+"\" id=\""+jobID+"\">\n"
							+ "<file id=\""+inputFileName+"\">\n"
							);
					
					doc = new com.aspose.words.Document(workingFile.getPath());
				    // Retrieve all paragraphs in the document.
					paragraphs = doc.getChildNodes(com.aspose.words.NodeType.PARAGRAPH, true);
				    // Iterate through all paragraphs
					int paragraphID = 1;
					for (com.aspose.words.Paragraph para : (Iterable<com.aspose.words.Paragraph>) paragraphs) {
						if (para.getRuns().getCount() > 0) {
							com.aspose.words.Run prevRun = null;
							// ArrayList<com.aspose.words.Node> arNodeRemove = new ArrayList<com.aspose.words.Node>();
							StringBuilder sbParagraph = new StringBuilder("");
					        // Check all runs in the paragraph for page breaks and remove them.
						   int xID = 1;
						   runs = para.getChildNodes(com.aspose.words.NodeType.RUN, true);
						   for (com.aspose.words.Run run : (Iterable<com.aspose.words.Run>) runs) {	
							   if (!checkIgnoreField(para,run) && !run.isDeleteRevision()) {
								   String sText = run.getText();
								   if (prevRun==null) {
									   sbParagraph.append(""+CleanExtractRunText(sText));
								   }else if(IsRunSameFontStyle(prevRun.getFont(), run.getFont(),paragraphID) && !IsForceXTag(sText,paragraphID)) {
									   sbParagraph.append(""+CleanExtractRunText(sText));
								   }else {
									   String sLast = getLastIndex(sbParagraph.toString());
										  String sFirst = getFirstIndex(sText);
										  if (!isNumeric(sLast) && !isNumeric(sFirst)) {
												 sbParagraph.append("<x id=\""+xID+"\"/>");
												 xID++;
										  }
										 sbParagraph.append(CleanExtractRunText(sText));
								   }
							   }else {
								   if (null != run.getText() && run.getText().toUpperCase().indexOf("REF") >= 0 && sbParagraph.toString().length() > 0) {
									   sbParagraph.append("<x id=\""+xID+"\"/>");
									   xID++;
								   }else if (null != run.getText() && (run.getText().indexOf("PAGE") > 0 || run.getText().indexOf("NUMPAGES") > 0)
											&& sbParagraph.toString().length() > 0) {
										sbParagraph.append("<x id=\"" + xID + "\"/>");
										xID++;
								}
								   
							   }
							   prevRun = run;
						   }
						   if (sbParagraph.toString().length() > 0){
							 //System.out.println(sbParagraph.toString());
								String sSource = sbParagraph.toString();
								sSource = removeTagXDateMain(sSource, sLangSource, jobID);
								writer.write("<unit id=\"pid"+paragraphID+"\">\n"
//										+ "<notes>\n"
//										+ "<note category=\"\"></note>\n"
//										+ "<note category=\"\"></note>\n"
//										+ "</notes>\n"
										+ "<segment>\n<source xml:lang=\""+sLangSource+"\">"+sSource+"</source>\n"
//										+ "<target></target>\n"
										+ "</segment>\n"
										
										+ "</unit>\n");
						   }
						   paragraphID++;	
						   
						}
					}
					
					
					writer.write("</file>\n</xliff>");
				} catch (Exception e) {
					throw e;
				}
				finally {
					//save update document
					if (doc != null) {
						doc.save(workingFile.getPath());	
					}
					//
					doc = null; 
					paragraphs = null;
				}
			}

			//read output xliff file
			
//			if (!validateXML(output))
//				oLog.WriteLog(pageName, "extract", "INVALID XML output=" + xliffPath, jobID, false);
			
			oLog.WriteLog(pageName, "extract", "end extract output=" + xliffPath, jobID, false);
			
			if(outputPath.length()<1) {
				
				output = FileUtils.readFileToString(new File(xliffPath));
			}else {
				if(new File(outputPath).exists()) {
					deleteFile(outputPath);
				}
				Path temp = Files.move(Paths.get(xliffPath), Paths.get(outputPath));
				output = "Generate Xliff file: \""+outputPath+ "\" finished.";
			}
			
			
		} catch (Exception e) {
			throw e;
		}
		
		return output;
	}
	
	private void deleteFile(String filePath) {
		File file = new File(filePath); 
		file.delete();	
	}

	private String extractToXliffAsposeEmail(File workingFile,String inputFileName,String ext,
			String sLangSource , String sLangTarget,
			String jobID, String outputPath) throws Exception {
		String output = "";
		try {
			//create working folder
			String root = workingFile.getParent();
			String xliffPath = root + File.separator + "pack1"+File.separator+"work"+File.separator + inputFileName +".xlf"; 
			File xliffFile = new File(xliffPath);
			
			if (!xliffFile.getParentFile().exists()) {
				xliffFile.getParentFile().mkdirs();		
			}

			oLog.WriteLog(pageName, "extract", "start extract", jobID, false);
			
			if(extEmailList.contains(ext) || abbyyExtToXliffList.contains(ext)) {

				System.out.println("workingFile: "+workingFile.getPath());
				MailMessage mail = null;
				
				mail = MailMessage.load(workingFile.getPath());
				
				
				//mail = MailMessage.load(inputFileName)
				System.out.println("Email Content: "+mail.getBody());
			}
			//read output xliff file
			
//			if (!validateXML(output))
//				oLog.WriteLog(pageName, "extract", "INVALID XML output=" + xliffPath, jobID, false);
			
			oLog.WriteLog(pageName, "extract", "end extract output=" + xliffPath, jobID, false);
			
//			File outputFile = new File(outputPath);
			
//			FileUtils.copyFile(workingFile,outputFile);
//			String workingFilePath = workingFile.getPath();
			if(outputPath.length() <1) {
				output = FileUtils.readFileToString(new File(xliffPath));
				
			}else {
				if(new File(outputPath).exists()) {
					deleteFile(outputPath);
				}
				Path temp = Files.move(Paths.get(xliffPath), Paths.get(outputPath));
				output = "Generate Xliff file: \""+outputPath+ "\" finished.";
			}
		} catch (Exception e) {
			throw e;
		}
		
		
		return output;
		
	}
	
	private String extractToTxtAspose(
			File workingFile,
			String inputFileName,
			String ext,
			String sLangSource , 
			String sLangTarget,
			String jobID, 
			String outputPath
			) throws Exception {
		String output = "";
		
		try {
			//create working folder
			String root = workingFile.getParent();
			String xliffPath = root + File.separator + "pack1"+File.separator+"work"+File.separator + inputFileName +".txt"; 
			File xliffFile = new File(xliffPath);
			if (!xliffFile.getParentFile().exists()) xliffFile.getParentFile().mkdirs();		

			oLog.WriteLog(pageName, "extract", "start extract", jobID, false);
			if (extWordList.contains(ext) || abbyyExtToXliffList.contains(ext)){
				com.aspose.words.Document doc = null;
				com.aspose.words.NodeCollection paragraphs = null;
				com.aspose.words.NodeCollection runs = null;
				
				try ( Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xliffFile, false), StandardCharsets.UTF_8 )) ) {
					doc = new com.aspose.words.Document(workingFile.getPath());
				    // Retrieve all paragraphs in the document.
					paragraphs = doc.getChildNodes(com.aspose.words.NodeType.PARAGRAPH, true);
				    // Iterate through all paragraphs
					int paragraphID = 1;
					for (com.aspose.words.Paragraph para : (Iterable<com.aspose.words.Paragraph>) paragraphs) {
						if (para.getRuns().getCount() > 0){
							com.aspose.words.Run prevRun = null;
							// ArrayList<com.aspose.words.Node> arNodeRemove = new ArrayList<com.aspose.words.Node>();
							StringBuilder sbParagraph = new StringBuilder("");
					        // Check all runs in the paragraph for page breaks and remove them.
						   int xID = 1;
						   runs = para.getChildNodes(com.aspose.words.NodeType.RUN, true);
						   
						   for (com.aspose.words.Run run : (Iterable<com.aspose.words.Run>) runs) {	
							   if (!checkIgnoreField(para,run) && !run.isDeleteRevision()) {
								   String sText = run.getText();
								   if (prevRun==null)
								   {
									   //System.out.println("----------paragraphID=" + paragraphID + "------------");
									   //System.out.println("START\t" + sText);
									   sbParagraph.append(""+CleanExtractRunText(sText));
								   }
								   else if (IsRunSameFontStyle(prevRun.getFont(), run.getFont(),paragraphID) && !IsForceXTag(sText,paragraphID))
								   {
									   sbParagraph.append(""+CleanExtractRunText(sText));						   
									  // System.out.println("SAME\t" + sText);
								   }
								   else
								   {
//									  sbParagraph.append("<x id=\""+xID+"\"/>" +CleanExtractRunText(sText));
//									  //System.out.println("DIFF\t" + sText);
//									  xID++;
									  
									  String sLast = getLastIndex(sbParagraph.toString());
									  String sFirst = getFirstIndex(sText);
									  if (!isNumeric(sLast) && !isNumeric(sFirst)) {
											 sbParagraph.append("<x id=\""+xID+"\"/>");
											 xID++;
									  }
									 sbParagraph.append(CleanExtractRunText(sText));
								   }
							   }else
							   {
								   //System.out.println("IGNORE\t" + run.getText());
								   // Ramoslee 202008: Fix issue 44 for reference field.
								   if (null != run.getText() && run.getText().toUpperCase().indexOf("REF") >= 0 && sbParagraph.toString().length() > 0) {
									   sbParagraph.append("<x id=\""+xID+"\"/>");
									   xID++;
								   }
								   // End: Ramoslee 202008: Fix issue 44 for reference field.
								   // Ramoslee 20201020: Fix Page @Page to @Numpages.
								   else if (null != run.getText() && (run.getText().indexOf("PAGE") > 0 || run.getText().indexOf("NUMPAGES") > 0)
											&& sbParagraph.toString().length() > 0) {
										sbParagraph.append("<x id=\"" + xID + "\"/>");
										xID++;
									}
								   // End: 20201020
							   }
							   prevRun = run;
						   }
						   if (sbParagraph.toString().length() > 0)
							{
								//System.out.println(sbParagraph.toString());
								String sSource = sbParagraph.toString();
								sSource = removeTagXDateMain(sSource, sLangSource, jobID);
						        writer.write("<p id=\"pid" + paragraphID + "\">"+ sSource + "</p>\n");		
						        //+ "<target xml:lang=\"" + sLangTarget.toUpperCase() + "\">" + sbParagraph.toString() + "</target></trans-unit>\n");
							}
					    	paragraphID++;
						}
					}
				} catch (Exception e) {
					throw e;
				}
				finally {
					//save update document
					if (doc != null) {
						doc.save(workingFile.getPath());	
					}
					//
					doc = null; 
					paragraphs = null;
				}
					
			}			
			
			//read output xliff file
			
//			if (!validateXML(output)) {
//				oLog.WriteLog(pageName, "extract", "INVALID XML output=" + xliffPath, jobID, false);
//			}
			oLog.WriteLog(pageName, "extract", "end extract output=" + xliffPath, jobID, false);
			
//			File outputFile = new File(outputPath);
			
//			FileUtils.copyFile(workingFile,outputFile);
//			String workingFilePath = workingFile.getPath();
			if(outputPath.length() <1) {
				output = FileUtils.readFileToString(new File(xliffPath));
				
			}else {
				if(new File(outputPath).exists()) {
					deleteFile(outputPath);
				}
				Path temp = Files.move(Paths.get(xliffPath), Paths.get(outputPath));
				output = "Extract file as paragraph finished, position at: \""+outputPath+ "\" .";
			}
		} catch (Exception e) {
			throw e;
		}
		
		
		return output;
	}
	
	private String extractToXliffAspose(File workingFile,String inputFileName,String ext,
			String sLangSource , String sLangTarget,
			String jobID, String outputPath) throws Exception {
		String output = "";
		try {
			
			//create working folder
			String root = workingFile.getParent();
			String xliffPath = root + File.separator + "pack1"+File.separator+"work"+File.separator + inputFileName +".xlf"; 
			File xliffFile = new File(xliffPath);
			if (!xliffFile.getParentFile().exists()) xliffFile.getParentFile().mkdirs();		

			oLog.WriteLog(pageName, "extract", "start extract", jobID, false);
			
			if (extWordList.contains(ext) || abbyyExtToXliffList.contains(ext))
			{
				com.aspose.words.Document doc = null;
				com.aspose.words.NodeCollection paragraphs = null;
				com.aspose.words.NodeCollection runs = null;				
				
				try ( Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xliffFile, false), StandardCharsets.UTF_8 )) ) {
					
					writer.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n<xliff version=\"1.2\" xmlns=\"urn:oasis:names:tc:xliff:document:1.2\">\n"
					+"<file original=\""+StringEscapeUtils.escapeXml(inputFileName)+"\" tool=\"Omniscien Technologies\" source-language=\"" + sLangSource 
					+ "\" target-language=\"" + sLangTarget + "\" datatype=\"x-unknown\">\n<body>\n");
					
					doc = new com.aspose.words.Document(workingFile.getPath());
				    // Retrieve all paragraphs in the document.
					paragraphs = doc.getChildNodes(com.aspose.words.NodeType.PARAGRAPH, true);
				    // Iterate through all paragraphs
					int paragraphID = 1;
				    for (com.aspose.words.Paragraph para : (Iterable<com.aspose.words.Paragraph>) paragraphs) {
				    	if (para.getRuns().getCount() > 0)
				    	{
							com.aspose.words.Run prevRun = null;
							// ArrayList<com.aspose.words.Node> arNodeRemove = new ArrayList<com.aspose.words.Node>();
							StringBuilder sbParagraph = new StringBuilder("");
					        // Check all runs in the paragraph for page breaks and remove them.
						   int xID = 1;
						   runs = para.getChildNodes(com.aspose.words.NodeType.RUN, true);
						   for (com.aspose.words.Run run : (Iterable<com.aspose.words.Run>) runs) {	
							   if (!checkIgnoreField(para,run) && !run.isDeleteRevision()) // Ramoslee 20200310: Take only text was not deleted.
							   {
								   String sText = run.getText();
								   if (prevRun==null)
								   {
									   //System.out.println("----------paragraphID=" + paragraphID + "------------");
									   //System.out.println("START\t" + sText);
									   sbParagraph.append(""+CleanExtractRunText(sText));
								   }
								   else if (IsRunSameFontStyle(prevRun.getFont(), run.getFont(),paragraphID) && !IsForceXTag(sText,paragraphID))
								   {
									   sbParagraph.append(""+CleanExtractRunText(sText));						   
									  // System.out.println("SAME\t" + sText);
								   }
								   else
								   {
//									  sbParagraph.append("<x id=\""+xID+"\"/>" +CleanExtractRunText(sText));
//									  //System.out.println("DIFF\t" + sText);
//									  xID++;
									  
									  String sLast = getLastIndex(sbParagraph.toString());
									  String sFirst = getFirstIndex(sText);
									  if (!isNumeric(sLast) && !isNumeric(sFirst)) {
											 sbParagraph.append("<x id=\""+xID+"\"/>");
											 xID++;
									  }
									 sbParagraph.append(CleanExtractRunText(sText));
								   }
							   }
							   else
							   {
								   //System.out.println("IGNORE\t" + run.getText());
								   // Ramoslee 202008: Fix issue 44 for reference field.
								   if (null != run.getText() && run.getText().toUpperCase().indexOf("REF") >= 0 && sbParagraph.toString().length() > 0) {
									   sbParagraph.append("<x id=\""+xID+"\"/>");
									   xID++;
								   }
								   // End: Ramoslee 202008: Fix issue 44 for reference field.
								   // Ramoslee 20201020: Fix Page @Page to @Numpages.
								   else if (null != run.getText() && (run.getText().indexOf("PAGE") > 0 || run.getText().indexOf("NUMPAGES") > 0)
											&& sbParagraph.toString().length() > 0) {
										sbParagraph.append("<x id=\"" + xID + "\"/>");
										xID++;
									}
								   // End: 20201020
							   }
							   
							   /*Ramoslee 20200316: Fix issue for the content change after translation.*/
							   prevRun = run;
					       }
						    
							if (sbParagraph.toString().length() > 0)
							{
								//System.out.println(sbParagraph.toString());
								String sSource = sbParagraph.toString();
								sSource = removeTagXDateMain(sSource, sLangSource, jobID);
						        writer.write("<trans-unit id=\"pid" + paragraphID + "\"><source xml:lang=\"" + sLangSource.toUpperCase() + "\">" + sSource + "</source></trans-unit>\n");		
						        //+ "<target xml:lang=\"" + sLangTarget.toUpperCase() + "\">" + sbParagraph.toString() + "</target></trans-unit>\n");
							}
					    	paragraphID++;									        
				    	}
				    }
					writer.write("</body>\n</file>\n</xliff>");
				} catch (Exception e) {
					throw e;
				}
				finally {
					//save update document
					if (doc != null) {
						doc.save(workingFile.getPath());	
					}
					//
					doc = null; 
					paragraphs = null;
				}
			}			
			
			//read output xliff file
			
//			if (!validateXML(output))
//				oLog.WriteLog(pageName, "extract", "INVALID XML output=" + xliffPath, jobID, false);
			
			oLog.WriteLog(pageName, "extract", "end extract output=" + xliffPath, jobID, false);
			
//			File outputFile = new File(outputPath);
			
//			FileUtils.copyFile(workingFile,outputFile);
//			String workingFilePath = workingFile.getPath();
			if(outputPath.length() <1) {
				output = FileUtils.readFileToString(new File(xliffPath));
				
			}else {
				if(new File(outputPath).exists()) {
					deleteFile(outputPath);
				}
				Path temp = Files.move(Paths.get(xliffPath), Paths.get(outputPath));
				output = "Generate Xliff file: \""+outputPath+ "\" finished.";
			}
		} catch (Exception e) {
			throw e;
		}
		return output;
	}
	
	public String mergeFromXliffAsposeExcell(String jobID,File workingFile,String xliffPath,String sLangSource,String sLangTarget,String outputPath)  throws Exception {
		String output = "";
		Workbook workbook = null;
		oLog.WriteLog(pageName, "mergeFromXliffAsposeExcell",  "Start merge output=" + outputPath, jobID, false);

		try {
			String xliffTranslated = FileUtils.readFileToString(new File(xliffPath), "UTF-8");
			xliffTranslated = CleanMergeRunText(xliffTranslated);
			//create working folder
			workbook = new Workbook(workingFile.getPath()); 
			int iNumberSheet = workbook.getWorksheets().getCount();
			for (int i = 0; i < iNumberSheet; i++) {
			
				Worksheet sheet = workbook.getWorksheets().get(i);
				Cells cells = sheet.getCells();
				Iterator iterator = cells.iterator();
				StringBuilder sbCellText = new StringBuilder("");
				while(iterator.hasNext())
				{
					Cell cell = (Cell)iterator.next();
					String sCellText = cell.getStringValue();
					String sCellTextNoFormat = cell.getStringValueWithoutFormat();
					String[] sArrCellText = getTranslatedCellText(xliffTranslated, sLangTarget, i+1, cell.getName());
					int k = 0;
					if (null != cell.getCharacters()) {
						FontSetting[] arrOriFontSettings = cell.getCharacters();
						String sPrevText = "";
						String sPrevAllText = "";
						List<Integer> iIndexList = new ArrayList();
						for (int j = 0; j < arrOriFontSettings.length; j++ ) {
							
							if (null != sArrCellText && sArrCellText.length > 0 && k < sArrCellText.length){
									String sTargetText = replaceSpacialChar3(sArrCellText[k]);
									if (nonLanguage.contains(sLangSource)) {
									   if (null != sPrevText && sPrevText.length() > 0 && null != sTargetText && sTargetText.length() > 0)  {
										   String lastStr = sPrevText.substring(sPrevText.length()-1);
										   String firstStr = sTargetText.substring(0,1);
										   if (!LastStringSkip.contains(lastStr) && !firstStringSkip.contains(firstStr)){
											   sTargetText = " " + sTargetText;
										   }
									   }
								    }
								    sPrevText = sTargetText;
									iIndexList.add(sTargetText.length());
									sPrevAllText = sPrevAllText + sTargetText;
									k++;
							}else {
								iIndexList.add(0);
							}
						}
						cell.putValue(sPrevAllText);
						int iSumAllIndex = 0;
						for (int l = 0; l < arrOriFontSettings.length; l++ ) {
							FontSetting fontSetting = arrOriFontSettings[l];
							if (l > 0) {
								iSumAllIndex = iSumAllIndex + iIndexList.get(l-1);
							}
							if (iIndexList.get(l) > 0) {
								
								Color color = new Color();
								color.fromArgb(fontSetting.getFont().getColor().toArgb());
								cell.characters(iSumAllIndex, iIndexList.get(l)).getFont().setColor(color);
								cell.characters(iSumAllIndex, iIndexList.get(l)).getFont().setScriptOffset(fontSetting.getFont().getScriptOffset());
								cell.characters(iSumAllIndex, iIndexList.get(l)).getFont().setArgbColor(fontSetting.getFont().getArgbColor());
								cell.characters(iSumAllIndex, iIndexList.get(l)).getFont().setBold(fontSetting.getFont().isBold());
								cell.characters(iSumAllIndex, iIndexList.get(l)).getFont().setCapsType(fontSetting.getFont().getCapsType());
								cell.characters(iSumAllIndex, iIndexList.get(l)).getFont().setCharset(fontSetting.getFont().getCharset());
								cell.characters(iSumAllIndex, iIndexList.get(l)).getFont().setItalic(fontSetting.getFont().isItalic());
								cell.characters(iSumAllIndex, iIndexList.get(l)).getFont().setNormalizeHeights(fontSetting.getFont().isNormalizeHeights());
								cell.characters(iSumAllIndex, iIndexList.get(l)).getFont().setSize(fontSetting.getFont().getSize());
								cell.characters(iSumAllIndex, iIndexList.get(l)).getFont().setStrikeout(fontSetting.getFont().isStrikeout());
								cell.characters(iSumAllIndex, iIndexList.get(l)).getFont().setStrikeType(fontSetting.getFont().getStrikeType());
								cell.characters(iSumAllIndex, iIndexList.get(l)).getFont().setSubscript(fontSetting.getFont().isSubscript());
								cell.characters(iSumAllIndex, iIndexList.get(l)).getFont().setSuperscript(fontSetting.getFont().isSuperscript());
//								cell.characters(iSumAllIndex, iIndexList.get(l)).getFont().setThemeColor(fontSetting.getFont().getThemeColor());
								cell.characters(iSumAllIndex, iIndexList.get(l)).getFont().setUnderline(fontSetting.getFont().getUnderline());
							}
						}
					}else {
						if (null == cell.getFormula() && null != sCellText && sCellText.length() > 0 && !lSkipCellExtract.contains(cell.getNumberCategoryType()) && !(isNumeric(sCellText) || isNumeric(sCellTextNoFormat))) {
							if (null != sArrCellText && sArrCellText.length > 0 && k < sArrCellText.length) {
								String targetText = replaceSpacialChar3(sArrCellText[k]);
								cell.putValue(targetText);
							}
						}
					}
				}
			}
	
			File fFile = new File(outputPath);
			if (!fFile.exists()) {
				fFile.mkdirs();
			}
		    //save output file after update translate output
			workbook.getSettings().setCalcMode(CalcModeType.AUTOMATIC);
			workbook.getSettings().setRecalculateBeforeSave( true);
			workbook.getSettings().setReCalculateOnOpen(true);
		    workbook.save(outputPath);
		    workbook = null;
			
			oLog.WriteLog(pageName, "mergeFromXliffAsposeExcell",  "end merge output=" + outputPath, jobID, false);
			byte[] bData = FileUtils.readFileToByteArray(new File(outputPath));
			output = new String(Base64Coder.encode(bData));
			
		} catch (Exception e) {
			throw e;
		}
		
		return output;
	}
	
	private void UpdateJobInprocess(String jobId, Integer percent, Double fileSize, String status) {
		try {
			if ((jobsInprocess == null || jobsInprocess.size() == 0) && app != null && app.getAttribute(jobsInProcessKey) != null) {
				jobsInprocess = (HashMap<Integer, JobInfo>) app.getAttribute(jobsInProcessKey);
			}
			//
			if (jobsInprocess == null) {
				jobsInprocess = new HashMap<Integer, JobInfo>();
			}
			//
			Integer iJobId = Integer.valueOf(jobId);
			JobInfo jobInfo = new JobInfo();
			if (jobsInprocess.containsKey(iJobId)) {
				jobInfo = jobsInprocess.get(iJobId);
				jobInfo.percent = percent;
				jobInfo.status = status;
				jobInfo.filesize = fileSize;
				jobInfo.waitTime = CalculateWaitInteralTime(fileSize);
				jobInfo.updatedate = new Date();
				oLog.WriteLog(pageName, "UpdateJobInprocess", "abbyy: waittime=" + jobInfo.waitTime.toString() + " percent=" + jobInfo.percent.toString(), jobId, false);
			} else {
				jobInfo.jobid = jobId;
				jobInfo.status = status;
				jobInfo.percent = percent;
				jobInfo.filesize = fileSize;
				jobInfo.waitTime = CalculateWaitInteralTime(fileSize);
				jobInfo.startdate = new Date();
			}
			setJobsInprocess(jobId, jobInfo, false);
			
		} catch (Exception e) {
			oLog.writeError(pageName + ":UpdateJobInprocess", jobId, e);
		}
	}
	
	private void RemoveJobInprocess(String jobId) {
		try {
			setJobsInprocess(jobId, null, true);
			oLog.WriteLog(pageName, "RemoveJobInprocess", "abbyy: Removed", jobId, false);
		} catch (Exception e) {
			oLog.writeError(pageName + ":RemoveJobInprocess", jobId, e);
		}
	}
	
	private void setJobsInprocess(String jobId, JobInfo jobInfo, Boolean removeJob) {
		try {
			if ((jobsInprocess == null || jobsInprocess.size() == 0) && app != null && app.getAttribute(jobsInProcessKey) != null) {
				jobsInprocess = (HashMap<Integer, JobInfo>) app.getAttribute(jobsInProcessKey);
			}
			//
			if (jobsInprocess == null) {
				jobsInprocess = new HashMap<Integer, JobInfo>();
			}
			
			//oLog.WriteLog(pageName, "setJobsInprocess", "abbyy: start size=" + jobsInprocess.size(), jobId, false);
			
			//delete
			Integer iJobId = Integer.valueOf(jobId);
			synchronized (app) {
				if (removeJob && jobsInprocess.containsKey(iJobId)) {
					jobsInprocess.remove(iJobId);
				} else {
					//insert/update
					jobsInprocess.put(iJobId, jobInfo);
				}
				
				if (app != null) {
					app.setAttribute(jobsInProcessKey, jobsInprocess);
				}
			}
			
			//oLog.WriteLog(pageName, "setJobsInprocess", "abbyy: size=" + jobsInprocess.size(), jobId, false);
		} catch (Exception e) {
			throw e;
		}
	}
	
	boolean IsRunSameFontStyle(com.aspose.words.Font prevFont,com.aspose.words.Font currentFont,int paragraphID)
	{
		if (paragraphID==0)
		{
			if (prevFont.getAllCaps() != currentFont.getAllCaps()) System.out.println("getAllCaps");
			if (prevFont.getSmallCaps() != currentFont.getSmallCaps()) System.out.println("getSmallCaps");
			if (prevFont.getBold() != currentFont.getBold()) System.out.println("getBold");
			if (!prevFont.getBorder().equals(currentFont.getBorder())) System.out.println("getBorder");
			if (prevFont.getComplexScript() != currentFont.getComplexScript()) System.out.println("getComplexScript");
			if (prevFont.getDoubleStrikeThrough() != currentFont.getDoubleStrikeThrough()) System.out.println("getDoubleStrikeThrough");
			if (prevFont.getEmboss() != currentFont.getEmboss()) System.out.println("getEmboss");
			if (prevFont.getEngrave() != currentFont.getEngrave()) System.out.println("getEngrave");
			if (prevFont.getHidden() != currentFont.getHidden()) System.out.println("getHidden");
			if (!prevFont.getHighlightColor().equals(currentFont.getHighlightColor())) System.out.println("getHighlightColor");
			if (prevFont.getItalic() != currentFont.getItalic()) System.out.println("getItalic");
			if (!prevFont.getName().equals(currentFont.getName())) System.out.println("getName");
			if (prevFont.getOutline() != currentFont.getOutline()) System.out.println("getOutline");
			if (prevFont.getPosition() != currentFont.getPosition()) System.out.println("getPosition");
			if (prevFont.getScaling() != currentFont.getScaling()) System.out.println("getScaling");
			if (!prevFont.getShading().equals(currentFont.getShading())) System.out.println("getShading");
			if (prevFont.getShadow() != currentFont.getShadow()) System.out.println("getShadow");
			if (prevFont.getSize() != currentFont.getSize())System.out.println("getSize");
			if (prevFont.getStrikeThrough() != currentFont.getStrikeThrough()) System.out.println("getStrikeThrough");
			if (!prevFont.getStyleName().equals(currentFont.getStyleName())) System.out.println("getStyleName");
			if (prevFont.getSubscript() != currentFont.getSubscript()) System.out.println("getSubscript");
			if (prevFont.getSuperscript() != currentFont.getSuperscript()) System.out.println("getSuperscript");
			if (prevFont.getTextEffect() != currentFont.getTextEffect()) System.out.println("getTextEffect");
			if (prevFont.getUnderline() != currentFont.getUnderline()) System.out.println("getUnderline");
			if (prevFont.getUnderlineColor() != currentFont.getUnderlineColor()) System.out.println("getUnderlineColor");
		}
		if ((prevFont.getAllCaps() == currentFont.getAllCaps()) && 
				(prevFont.getSmallCaps() == currentFont.getSmallCaps()) && 
				(prevFont.getBold() == currentFont.getBold()) && 
				(prevFont.getBorder().equals(currentFont.getBorder())) && 
				(prevFont.getColor().equals(currentFont.getColor())) && 
				(prevFont.getComplexScript() == currentFont.getComplexScript()) && 
				(prevFont.getDoubleStrikeThrough() == currentFont.getDoubleStrikeThrough()) && 
				(prevFont.getEmboss() == currentFont.getEmboss()) && 
				(prevFont.getEngrave() == currentFont.getEngrave()) && 
				(prevFont.getHidden() == currentFont.getHidden()) && 
				(prevFont.getHighlightColor().equals(currentFont.getHighlightColor())) && 
				(prevFont.getItalic() == currentFont.getItalic()) && 
				(prevFont.getName().equals(currentFont.getName())) && 
				(prevFont.getOutline() == currentFont.getOutline()) && 
				(prevFont.getPosition() == currentFont.getPosition()) && 
				// Fix BITS issue : the scaling of characters is different and it impact to generate tag <x> in xliff.
				//(prevFont.getScaling() == currentFont.getScaling()) && 
				(prevFont.getShading().equals(currentFont.getShading())) && 
				(prevFont.getShadow() == currentFont.getShadow()) && 
				(prevFont.getSize() == currentFont.getSize()) && 
				(prevFont.getStrikeThrough() == currentFont.getStrikeThrough()) && 
				(prevFont.getStyleName().equals(currentFont.getStyleName())) && 
				(prevFont.getSubscript() == currentFont.getSubscript()) && 
				(prevFont.getSuperscript() == currentFont.getSuperscript()) && 
				(prevFont.getTextEffect() == currentFont.getTextEffect()) && 
				(prevFont.getUnderline() == currentFont.getUnderline()) && 
				(prevFont.getUnderlineColor() == currentFont.getUnderlineColor()))
			return true;
		else
			return false;
	}
	
	private boolean checkIgnoreField(com.aspose.words.Paragraph para,com.aspose.words.Run run)
	{

		 boolean isInside = false;
		com.aspose.words.FieldCollection fields = para.getRange().getFields();
		for (com.aspose.words.Field field : fields) {
			com.aspose.words.Node currentNode = field.getStart();
			 while (currentNode != field.getEnd() && !isInside)
			 {
				 if (currentNode.getNodeType() == com.aspose.words.NodeType.RUN)
				 {
					 if (currentNode.equals(run))
					 {
						 isInside = true;
						 break;
					 }
				 }
				 com.aspose.words.Node nextNode = currentNode.nextPreOrder(currentNode.getDocument());
				 currentNode = nextNode;
			 }
		}
		return isInside;
	}
	
	private String CleanExtractRunText(String sText)
	{
		sText = sText.replace("&", "&amp;").replace("\"", "&quot;").replace("'", "&apos;").replace("<", "&lt;").replace(">", "&gt;");
		sText = replaceSpecialCharWihtKey(sText);
		return sText;
		/*Ramoslee 20200312: Post all special char to xlif.*/
//		return sText.replace("&", "&amp;").replace("\"", "&quot;").replace("'", "&apos;").replace("<", "&lt;").replace(">", "&gt;");
	}
	
	private String replaceSpecialCharWihtKey(String sText)
	{
		String outputStr = sText;
		List<Integer> indexSpChrList = new ArrayList();
		List<Integer> SpChrList = new ArrayList();
		Pattern pSpacialChar = Pattern.compile(sSpacialCharPattern,
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		
		Matcher mSpacialChar = pSpacialChar.matcher(sText);
		String sSpacialChar = "";
		int i = 0;
		int tempindex = 0;
		int count = 0;
		String tempEncrypt = "";
		while (mSpacialChar.find()) {
			int spChrIdex = mSpacialChar.start(1);
			sSpacialChar = mSpacialChar.group(1) == null  ? "" : mSpacialChar.group(1);
			indexSpChrList.add(spChrIdex);
			SpChrList.add((int) sSpacialChar.charAt(0));
		}
		
		
		if (!indexSpChrList.isEmpty()) {
			
			int startIndex = 0;
			int size = indexSpChrList.size();
			
			if(size == 1) {
				tempEncrypt = "U" + SpChrList.get(0);
				outputStr = outputStr.substring(0, (indexSpChrList.get(startIndex)) +  tempindex -(i > 0? startIndex :0 ) ) + "<x id=\"SPC" + i + "\" encrypt=\"" + tempEncrypt  +"\"/>" + outputStr.substring((indexSpChrList.get(startIndex)+tempindex-(i > 0? startIndex :0 ))+1);

			}else {
				for (int j = 0; j < size -1; j++) {
					count++;
					if (indexSpChrList.get(j) != indexSpChrList.get(j+1) -1 || ((j+1) == size -1) && indexSpChrList.get(j) == indexSpChrList.get(j+1) -1) {
						tempEncrypt = "";
						
						int endIdex = indexSpChrList.get(j);
						if (j+1 == size-1 && indexSpChrList.get(j) == indexSpChrList.get(j+1) -1) {
							endIdex = indexSpChrList.get(j+1);
						}
						int endK = ((j+1) == size -1? j+1:j);
						for (int k = startIndex; k <= endK; k++) {
							tempEncrypt = tempEncrypt + "U" + SpChrList.get(k);
						}
						outputStr = outputStr.substring(0, (indexSpChrList.get(startIndex)) +  tempindex -(i > 0? startIndex :0 ) ) + "<x id=\"SPC" + i + "\" encrypt=\"" + tempEncrypt  +"\"/>" + outputStr.substring((endIdex+tempindex-(i > 0? startIndex :0 ))+1);
						tempindex = tempindex + 24 + (i + "").length() + tempEncrypt.length();
						startIndex = j+1; //- (tempindex == 0?0:1)  //- (i == 0? 0:(count-i))
						i++;
						count=0;
					}
					
					if (((j+1) == size -1) && indexSpChrList.get(j) != indexSpChrList.get(j+1) -1){
						tempEncrypt = "U" + SpChrList.get(startIndex);
						outputStr = outputStr.substring(0, (indexSpChrList.get(startIndex)) +  tempindex -(i > 0? startIndex :0 ) ) + "<x id=\"SPC" + i + "\" encrypt=\"" + tempEncrypt  +"\"/>" + outputStr.substring((indexSpChrList.get(startIndex)+tempindex-(i > 0? startIndex :0 ))+1);

					}
				}
				
				
			}
		}
		return outputStr;
	}
	
	private String getLastIndex(String sSource) {
    	String sLast = "";
    	if (null != sSource) {
	    	for (int i = sSource.length() -1; i >= 0; i--) {
	    		sLast = Character.toString(sSource.charAt(i));
				if (!sLast.equals(" ")) {
					break;
				}
			}
    	}
    	return sLast;
    }
	
	 private String getFirstIndex(String sSource) {
	    	String sFirst = "";
	    	if (null != sSource) {
		    	for (int i = 0; i < sSource.length(); i ++) {
		    		sFirst = Character.toString(sSource.charAt(i));
					if (!sFirst.equals(" ")) {
						break;
					}
				}
	    	}
	    	return sFirst;
	    }
	 
	 public boolean isNumeric(String strNum) {
		    if (strNum == null) {
		        return false; 
		    }
		    return pNumber.matcher(strNum).matches();
		}

		boolean IsForceXTag(String sText,int paragraphID)
		{
			if (sText.contains("\t"))
				return true;
			else
				return false;
		}
		
		private String removeTagXDateMain(String sSource, String sLangSource, String jobID) {
			
			String sTarget = sSource;
			try {
				if ("ZH".equalsIgnoreCase(sLangSource)) {
					String sYearMonthDate = "([1-9１-９一二三四五六七八九]{1}[0-9０-９零一二三四五六七八九]{3})(<x[ ]id=\"[0-9]{1,}\"/>)(年)(<x[ ]id=\"[0-9]{1,}\"/>)(([0０]?[1-9１-９])|([1１][0-2０-２])|(([一二三四五六七八九十])|(十一)|(十二)))(<x[ ]id=\"[0-9]{1,}\"/>)(月)(<x[ ]id=\"[0-9]{1,}\"/>)(([012０１２]?[1-9１-９])|([3３][0-1０-１]|[12１２][0０])|(([一二三四五六七八九])|(十[一二三四五六七八九]?)|(二十[一二三四五六七八九]?)|(三十[一]?)))(<x[ ]id=\"[0-9]{1,}\"/>)(日)";
					String sDateMonthYear = "(([012０１２]?[1-9１-９])|([3３][0-1０-１]|[12１２][0０])|(([一二三四五六七八九])|(十[一二三四五六七八九]?)|(二十[一二三四五六七八九]?)|(三十[一]?)))(<x[ ]id=\"[0-9]{1,}\"/>)(日)(<x[ ]id=\"[0-9]{1,}\"/>)(([0０]?[1-9１-９])|([1１][0-2０-２])|(([一二三四五六七八九十])|(十一)|(十二)))(<x[ ]id=\"[0-9]{1,}\"/>)(月)(<x[ ]id=\"[0-9]{1,}\"/>)([1-9１-９一二三四五六七八九]{1}[0-9０-９零一二三四五六七八九]{3})(<x[ ]id=\"[0-9]{1,}\"/>)(年)";
					String sMonthDateYear = "(([0０]?[1-9１-９])|([1１][0-2０-２])|(([一二三四五六七八九十])|(十一)|(十二)))(<x[ ]id=\"[0-9]{1,}\"/>)(月)(<x[ ]id=\"[0-9]{1,}\"/>)(([012０１２]?[1-9１-９])|([3３][0-1０-１]|[12１２][0０])|(([一二三四五六七八九])|(十[一二三四五六七八九]?)|(二十[一二三四五六七八九]?)|(三十[一]?)))(<x[ ]id=\"[0-9]{1,}\"/>)(日)(<x[ ]id=\"[0-9]{1,}\"/>)([1-9１-９一二三四五六七八九]{1}[0-9０-９零一二三四五六七八九]{3})(<x[ ]id=\"[0-9]{1,}\"/>)(年)";
					String sYearMonth 	  = "([1-9１-９一二三四五六七八九]{1}[0-9０-９零一二三四五六七八九]{3})(<x[ ]id=\"[0-9]{1,}\"/>)(年)(<x[ ]id=\"[0-9]{1,}\"/>)(([0０]?[1-9１-９])|([1１][0-2０-２])|(([一二三四五六七八九十])|(十一)|(十二)))(<x[ ]id=\"[0-9]{1,}\"/>)(月)";
					String sMonthDate	  = "(([0０]?[1-9１-９])|([1１][0-2０-２])|(([一二三四五六七八九十])|(十一)|(十二)))(<x[ ]id=\"[0-9]{1,}\"/>)(月)(<x[ ]id=\"[0-9]{1,}\"/>)(([012０１２]?[1-9１-９])|([3３][0-1０-１]|[12１２][0０])|(([一二三四五六七八九])|(十[一二三四五六七八九]?)|(二十[一二三四五六七八九]?)|(三十[一]?)))(<x[ ]id=\"[0-9]{1,}\"/>)(日)";
					String sYear   		  = "([1-9１-９一二三四五六七八九]{1}[0-9０-９零一二三四五六七八九]{3})(<x[ ]id=\"[0-9]{1,}\"/>)(年)";
					String sMonth		  = "(([0０]?[1-9１-９])|([1１][0-2０-２])|(([一二三四五六七八九十])|(十一)|(十二)))(<x[ ]id=\"[0-9]{1,}\"/>)(月)";
					String sDate	  	  = "(([012０１２]?[1-9１-９])|([3３][0-1０-１]|[12１２][0０])|(([一二三四五六七八九])|(十[一二三四五六七八九]?)|(二十[一二三四五六七八九]?)|(三十[一]?)))(<x[ ]id=\"[0-9]{1,}\"/>)(日)";
					
					if (null != sTarget && sTarget.trim().length() > 0) {
						sTarget = removeTagXDate(sTarget, sYearMonthDate);
						
						sTarget = removeTagXDate(sTarget, sDateMonthYear);
						
						sTarget = removeTagXDate(sTarget, sMonthDateYear);
						
						sTarget = removeTagXDate(sTarget, sYearMonth);
						
						sTarget = removeTagXDate(sTarget, sMonthDate);
						
						sTarget = removeTagXDate(sTarget, sYear);
						
						sTarget = removeTagXDate(sTarget, sMonth);
						
						sTarget = removeTagXDate(sTarget, sDate);
					}
				}else if ("EN".equalsIgnoreCase(sLangSource)) {
					String sMonthDateYear = "(?<month>(January|February|March|April|May|June|July|August|September|October|November|December)|((Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)([.]{1})?))[ ]?(<x[ ]id=\\\"[0-9]{1,}\\\"/>)[ ]?((?<date>[012]?[1-9]|3[0-1]|[12]0)(st|nd|rd|th)?)[ ]?(<x[ ]id=\\\"[0-9]{1,}\\\"/>)[ ]?(?<year>[0-9]{4})";
					String sYearDateMonth = "(?<year>[0-9]{4})[ ]?(<x[ ]id=\\\"[0-9]{1,}\\\"/>)[ ]?((?<date>[012]?[1-9]|3[0-1]|[12]0)(st|nd|rd|th)?)[ ]?(<x[ ]id=\\\"[0-9]{1,}\\\"/>)[ ](?<month>(January|February|March|April|May|June|July|August|September|October|November|December)|((Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)([.]{0,1})))";
					String sYearMonth 	  = "(?<year>[0-9]{4})([ \\-/,]{0,2})[ ]?(<x[ ]id=\\\"[0-9]{1,}\\\"/>)[ ]?(?<month>(January|February|March|April|May|June|July|August|September|October|November|December)|((Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)([.]{1})))";
					String sMonthYear 	  = "(?<year>[0-9]{4})([ \\-/,]{0,2})[ ]?(<x[ ]id=\\\"[0-9]{1,}\\\"/>)[ ]?(?<month>(January|February|March|April|May|June|July|August|September|October|November|December)|((Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)([.]{1})))";
					String sMonthDate	  = "(?<month>(January|February|March|April|May|June|July|August|September|October|November|December)|((Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)([.]{1})?))[ ]?(<x[ ]id=\\\"[0-9]{1,}\\\"/>)[ ]?((?<date>[012]?[1-9]|3[0-1]|[12]0)(st|nd|rd|th)?)";
					String sDateMonth	  = "((?<date>[012]?[1-9]|3[0-1]|[12]0)(st|nd|rd|th)?)[ ]?(<x[ ]id=\\\"[0-9]{1,}\\\"/>)[ ](?<month>(January|February|March|April|May|June|July|August|September|October|November|December)|((Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)([.]{0,1})))";
		
					if (null != sTarget && sTarget.trim().length() > 0) {
						sTarget = removeTagXDate(sTarget, sMonthDateYear);
						
						sTarget = removeTagXDate(sTarget, sYearDateMonth);
						
						sTarget = removeTagXDate(sTarget, sYearMonth);
						
						sTarget = removeTagXDate(sTarget, sMonthYear);
						
						sTarget = removeTagXDate(sTarget, sMonthDate);
						
						sTarget = removeTagXDate(sTarget, sDateMonth);
						
					}
				}
			}catch (Exception e) {
				sTarget = sSource;
				// TODO: handle exceptiont
				oLog.WriteLog(pageName, "removeTagXDateMain",  e.getMessage(), jobID, true);
			}
			return sTarget;
		}
		
		private String removeTagXDate(String sSource, String sPattern ) {
			String sTarget = sSource;
			String sDateTemp = "";
			Pattern pTemp = Pattern.compile(sPattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher mTU = pTemp.matcher(sTarget);
			while (mTU.find()) {
				sDateTemp = mTU.group(0);
				sTarget = sTarget.replaceAll(sDateTemp, sDateTemp.replaceAll("<x[ ]id=\"(\\d+)\"\\/>", ""));
				mTU = null;
				mTU = pTemp.matcher(sTarget);
			}
//			System.out.println("sTarget ::" + sTarget);

			return sTarget;
		}
		
		private boolean validateXML(String xmlData) {

			boolean bValid = false;
			try {

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				ByteArrayInputStream bis = new ByteArrayInputStream(xmlData.getBytes());
				Document doc = db.parse(bis);

				bValid = true;

			} catch (Exception ex) {
				// instance document is invalid!			
			}
			return bValid;
		}
		
		private String CleanMergeRunText(String sText)
		{
			return sText.replace("&amp;","&").replace("&quot;","\"").replace("&apos;","'").replace("&lt;","<").replace("&gt;",">");
		}
		
		private String[] getTranslatedCellText(String xliffInputConntent ,String targetLanguage, int iSheetNumber, String sCellName)
		{
			Pattern pTU = Pattern.compile("<trans-unit[ ]id=\"SHEET"+iSheetNumber+"-"+sCellName+"\"[ ][^<>]*>(.+?)</trans-unit>",
					Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
//			Pattern pTarget = Pattern.compile("<source xml:lang=\"" + targetLanguage + "\"[^><]*?>(.+?)<\\/source>",
			Pattern pTarget = Pattern.compile("<target xml:lang=\"" + targetLanguage + "\"[^><]*?>(.+?)<\\/target>",
					Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			
			Matcher mTU = pTU.matcher(xliffInputConntent);
			String sTU = "",sTarget = "";
			if (mTU.find()) {
				// All content in TU
				sTU = mTU.group(1) == null  ? "" : mTU.group(1);
//				System.out.println("sTu " + sTU);
				if (sTU.length() > 0)
				{
					Matcher mTarget = pTarget.matcher(sTU);
					if (mTarget.find()) {
						sTarget = mTarget.group(1) == null ? "" : mTarget.group(1);
					}				
				}
			}
//			System.out.println("sTarget :::" + sTarget);
			return StringEscapeUtils.unescapeXml(sTarget).split("<x[ ]id=\"(\\d+)\"\\/>");
		}
		
		private String replaceSpacialChar3(String targetSText) {
			
			int j =0;
			String tempStr = "<x id=\"SPC" + j + "\" encrypt=\"";
			while(targetSText.indexOf(tempStr) >=0) {
				
//					System.out.println("tempStr" + tempStr);
					int indexspchr = targetSText.indexOf(tempStr);
//					System.out.println("indexspchr " + indexspchr);
					char unicode = 0;
					String specialChr = "";
					if (indexspchr >= 0) {
						String unicodes = targetSText.substring(indexspchr + tempStr.length(), targetSText.indexOf("\"", indexspchr + tempStr.length()));
						String[] unicodeArray = unicodes.split("U");
						String tempStrFull = tempStr + unicodes + "\"/>";
//						System.out.println("tempStrFull : " + tempStr + unicodes + "\"/>");
//						System.out.println("tempStrFull2 : " +tempStrFull2);
//						System.out.println("tempStrFull3 : " +tempStrFull2 + tempStrFull);
//						System.out.println("tempstr replace: " + targetSText.replace(tempStrFull2 + tempStrFull, tempStrFull2));
						
						if (null != unicodeArray && unicodeArray.length > 0) {
							for (int l = 0; l < unicodeArray.length; l++) {
								String unicodeStr = unicodeArray[l];
								if (unicodeStr.length() > 0) {
									specialChr = specialChr + (char)(Integer.parseInt(unicodeStr));
								}
							}
						}
						
						targetSText = targetSText.replaceAll(tempStrFull, specialChr);
					}
					

					if (targetSText.indexOf(tempStr) < 0) {
						j++;
						tempStr = "<x id=\"SPC" + j + "\" encrypt=\"";
					}
			}
			
			tempStr = "<x id=\"SPC";
			
			if (targetSText.indexOf(tempStr) >= 0) {
				cleanTagX(targetSText);
			}

			return targetSText;
		}
		
		private String cleanTagX(String targetSText) {
			
			String tempStr = "<x id=\"SPC";
			String endTagX = "\" encrypt=\"";
			
			while(targetSText.indexOf(tempStr) >=0) {
				
				int indexspchr = targetSText.indexOf(tempStr);
				int indexsEnd = targetSText.indexOf(endTagX);
				String specialChr = "";
				if (indexspchr >= 0 && indexsEnd > 0) {
					String tempStrFull = targetSText.substring(indexspchr, targetSText.indexOf("\"", indexsEnd + 11)) + "\"/>";
					String unicodes = tempStrFull.substring(tempStrFull.indexOf("U"), tempStrFull.lastIndexOf("\""));
					String[] unicodeArray = unicodes.split("U");
					if (null != unicodeArray && unicodeArray.length > 0) {
						int k = 0;
						for (int l = 0; l < unicodeArray.length; l++) {
							String unicodeStr = unicodeArray[l];
							if (unicodeStr.length() > 0) {
								specialChr = specialChr + (char)(Integer.parseInt(unicodeStr));
								k++;
							}
						}
					}
					
					targetSText = targetSText.replaceAll(tempStrFull, specialChr);
		
				}
			}
			
			
			return targetSText;
		}
		
		private Integer CalculateWaitInteralTime(Double fileSize) {
			Integer iUsedTime = defaultWaitInterval;
			Integer itime = iUsedTime;
			try {
				if (fileSize > 0) {
					//200kb ~ 20 seconds
					Double dFileSize = oCommon.ChkNullDoubleObj(abbyyWaitInterval.split("[|]")[0]);
					iUsedTime = oCommon.ChkNullIntObj(abbyyWaitInterval.split("[|]")[1]);
					itime = (int) Math.round((fileSize * iUsedTime) / dFileSize); 
				}
				
			} catch (Exception e) {
				itime = iUsedTime;
			}
			return itime;
		}
		
		public String extractToXliffAsposeExcellV2(File workingFile,String inputFileName,String ext,
				String sLangSource , String sLangTarget,
				String jobID, String outputPath) throws Exception {
			
			String output = "";
			oLog.WriteLog(pageName, "extractToXliffAsposeExcell", "start extract", jobID, false);
			Workbook workbook = null;
			try {
				//create working folder
				String root = workingFile.getParent();
				String xliffPath = root + File.separator + "pack1"+File.separator+"work"+File.separator + inputFileName +".xlf"; 
				File xliffFile = new File(xliffPath);
				if (!xliffFile.getParentFile().exists()) {
					xliffFile.getParentFile().mkdirs();
				}
				
				if (extCellList.contains(ext.toLowerCase())){
					try ( Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xliffFile, false), StandardCharsets.UTF_8 )) ) {
						writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
								+ "<xliff version=\"2.0\" xmlns=\"urn:oasis:names:tc:xliff:document:2.0\" srcLang=\""+sLangSource+"\" trgLang=\""+sLangTarget+"\">\n"
//								+ "<file original=\""+inputFileName+"\" id=\""+jobID+"\">\n"
								+ "<file id=\""+inputFileName+"\">\n"
								);
						
						workbook = new Workbook(workingFile.getPath());
						
						int iNumberSheet = workbook.getWorksheets().getCount();
						StringBuilder sbSheetText = new StringBuilder("");
						for (int i = 0; i < iNumberSheet; i++) {
							Worksheet sheet = workbook.getWorksheets().get(i);
							String sSheetName = sheet.getName();
							Cells cells = sheet.getCells();
							Iterator iterator = cells.iterator();
							int countIndex = 1;
							StringBuilder sbCellText = new StringBuilder("");
							while(iterator.hasNext()) {
								Cell cell = (Cell)iterator.next();
								String sCellText = cell.getStringValue();
								String sCellTextNoFormat = cell.getStringValueWithoutFormat();
								if (null != cell.getCharacters()) {
									for (int j = 0; j < cell.getCharacters().length; j++ ) {
										FontSetting fontSetting = cell.getCharacters()[j];
										int iStart = fontSetting.getStartIndex();
										int iLenght = fontSetting.getLength();
										if (sbCellText.toString().equals("")) {
											sbCellText.append(CleanExtractRunText(sCellText.substring(iStart, iStart+iLenght)));
										}else {
											sbCellText.append("<x id=\"" + (j+0) + "\"/>");
											sbCellText.append(CleanExtractRunText(sCellText.substring(iStart, iStart+iLenght)));
										}
									}
								}else {
									if (null == cell.getFormula() && null != sCellText && sCellText.length() > 0 && !lSkipCellExtract.contains(cell.getNumberCategoryType()) && !(isNumeric(sCellText) || isNumeric(sCellTextNoFormat))) {
										sbCellText.append(CleanExtractRunText(sCellText));
									}
								}
								String sTemp = sbCellText.toString();
								if (null != sTemp && !sTemp.equals("")) {
									sbSheetText.append("<unit id=\"SHEET"+ (i+1)+ "-" + cell.getName() + "\" resname=\"" +StringEscapeUtils.escapeXml(sSheetName)+ "!"+ cell.getName() +"\" xml:space=\"preserve\">\n"); 
									sbSheetText.append("<segment>");
									sbSheetText.append("<source xml:lang=\"" + sLangSource +"\">");
									sTemp = removeTagXDateMain(sTemp, sLangSource, jobID);
									sbSheetText.append(sTemp);
									sbSheetText.append("</source>\n");
									sbSheetText.append("</segment>");
									sbSheetText.append("</unit>\n");
								}
								countIndex ++;
								sbCellText = new StringBuilder("");
							}
							writer.write(sbSheetText.toString());
							sbSheetText = new StringBuilder("");
						}
						writer.write("</file>\n</xliff>");
					} catch (Exception e) {
						throw e;
					}
					finally {
						//save update document
//						System.out.println("workingFile.getPath() ::::::" + workingFile.getPath());
//						System.out.println("xliffFile.getPath() ::::::" + xliffFile.getPath());
						workbook.save(workingFile.getPath());
						workbook = null;
					}
				}
				
				output = FileUtils.readFileToString(new File(xliffPath));
//				if (!validateXML(output))
//					oLog.WriteLog(pageName, "extractToXliffAsposeExcell", "INVALID XML output=" + xliffPath, jobID, false);
				
				oLog.WriteLog(pageName, "extractToXliffAsposeExcell", "end extract output=" + xliffPath, jobID, false);
				
				if(outputPath.length()<1) {
					
					output = FileUtils.readFileToString(new File(xliffPath));
				}else {
					if(new File(outputPath).exists()) {
						deleteFile(outputPath);
					}
					Path temp = Files.move(Paths.get(xliffPath), Paths.get(outputPath));
					output = "Generate Xliff file: \""+outputPath+ "\" finished.";
				}
				
			} catch (Exception e) {
				throw e;
			}
			oLog.WriteLog(pageName, "extractToXliffAsposeExcell", "End extract", jobID, false);
			
			
			return output;
		}
		
		public String extractToXliffAsposeExcell(File workingFile,String inputFileName,String ext,
				String sLangSource , String sLangTarget,
				String jobID, String outputPath) throws Exception {
			String output = "";
			oLog.WriteLog(pageName, "extractToXliffAsposeExcell", "start extract", jobID, false);
			Workbook workbook = null;
			try {
				
				//create working folder
				String root = workingFile.getParent();
				String xliffPath = root + File.separator + "pack1"+File.separator+"work"+File.separator + inputFileName +".xlf"; 
				File xliffFile = new File(xliffPath);
				if (!xliffFile.getParentFile().exists()) xliffFile.getParentFile().mkdirs();		
				
				if (extCellList.contains(ext.toLowerCase()))
				{
					try ( Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xliffFile, false), StandardCharsets.UTF_8 )) ) {
						
						writer.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n<xliff version=\"1.2\" xmlns=\"urn:oasis:names:tc:xliff:document:1.2\">\n");
						writer.write("<file original=\"xl/" +StringEscapeUtils.escapeXml(inputFileName)+".xml\" source-language=\""+ sLangSource+"\" target-language=\""+sLangTarget+"\" datatype=\"x-undefined\">\n"); 
						writer.write("<body>");
						
						workbook = new Workbook(workingFile.getPath()); 
			
						int iNumberSheet = workbook.getWorksheets().getCount();
						StringBuilder sbSheetText = new StringBuilder("");
						for (int i = 0; i < iNumberSheet; i++) {
						
							Worksheet sheet = workbook.getWorksheets().get(i);
							String sSheetName = sheet.getName();
							Cells cells = sheet.getCells();
							Iterator iterator = cells.iterator();
							int countIndex = 1;
							StringBuilder sbCellText = new StringBuilder("");
							while(iterator.hasNext())
							{
								Cell cell = (Cell)iterator.next();
								String sCellText = cell.getStringValue();
								String sCellTextNoFormat = cell.getStringValueWithoutFormat();
								if (null != cell.getCharacters()) {
									for (int j = 0; j < cell.getCharacters().length; j++ ) {
										FontSetting fontSetting = cell.getCharacters()[j];
										int iStart = fontSetting.getStartIndex();
										int iLenght = fontSetting.getLength();
										if (sbCellText.toString().equals("")) {
											sbCellText.append(CleanExtractRunText(sCellText.substring(iStart, iStart+iLenght)));
										}else {
											sbCellText.append("<x id=\"" + (j+0) + "\"/>");
											sbCellText.append(CleanExtractRunText(sCellText.substring(iStart, iStart+iLenght)));
										}
									}
								}else {
									if (null == cell.getFormula() && null != sCellText && sCellText.length() > 0 && !lSkipCellExtract.contains(cell.getNumberCategoryType()) && !(isNumeric(sCellText) || isNumeric(sCellTextNoFormat))) {
										sbCellText.append(CleanExtractRunText(sCellText));
									}

								}
								String sTemp = sbCellText.toString();
								if (null != sTemp && !sTemp.equals("")) {
									sbSheetText.append("<trans-unit id=\"SHEET"+ (i+1)+ "-" + cell.getName() + "\" resname=\"" +StringEscapeUtils.escapeXml(sSheetName)+ "!"+ cell.getName() +"\" xml:space=\"preserve\">"); 
									sbSheetText.append("<source xml:lang=\"" + sLangSource +"\">");
									sTemp = removeTagXDateMain(sTemp, sLangSource, jobID);
									sbSheetText.append(sTemp);
									sbSheetText.append("</source>");
									sbSheetText.append("</trans-unit>\n");
								}
								countIndex ++;
								sbCellText = new StringBuilder("");
									
							}
							writer.write(sbSheetText.toString());
							sbSheetText = new StringBuilder("");
						}
				
						writer.write("</body>\n</file>\n</xliff>");
					} catch (Exception e) {
						throw e;
					}
					finally {
						//save update document
//						System.out.println("workingFile.getPath() ::::::" + workingFile.getPath());
//						System.out.println("xliffFile.getPath() ::::::" + xliffFile.getPath());
						workbook.save(workingFile.getPath());
						workbook = null;
					}
				}
				
				output = FileUtils.readFileToString(new File(xliffPath));
//				if (!validateXML(output))
//					oLog.WriteLog(pageName, "extractToXliffAsposeExcell", "INVALID XML output=" + xliffPath, jobID, false);
				
				oLog.WriteLog(pageName, "extractToXliffAsposeExcell", "end extract output=" + xliffPath, jobID, false);
				
				if(outputPath.length()<1) {
					
					output = FileUtils.readFileToString(new File(xliffPath));
				}else {
					if(new File(outputPath).exists()) {
						deleteFile(outputPath);
					}
					Path temp = Files.move(Paths.get(xliffPath), Paths.get(outputPath));
					output = "Generate Xliff file: \""+outputPath+ "\" finished.";
				}
				
			} catch (Exception e) {
				throw e;
			}
			oLog.WriteLog(pageName, "extractToXliffAsposeExcell", "End extract", jobID, false);

			
			return output;
		}
		
		private void convertToNewFormatForExtract(File workingFile , String jobID , String ext) throws Exception
		{
			String originalPath = workingFile.getPath()+".original";
			File originalFile = new File(originalPath);
			if (originalFile.exists()) originalFile.delete();
			String currentPath = workingFile.getPath();
			FileUtils.moveFile(new File(currentPath),new File(originalPath));							
			oLog.WriteLog(pageName, "extract", "convert input to new MSOffice version input=" + originalPath, jobID, false);		
					
			if (extWordList.contains(ext))
			{
				//convert to docx
				com.aspose.words.Document doc = new com.aspose.words.Document(originalPath);
				doc.save(currentPath, com.aspose.words.SaveFormat.DOCX);
			}
			else if (extCellList.contains(ext))
			{
				//convert to xlsx
				com.aspose.cells.Workbook workbook = new com.aspose.cells.Workbook(originalPath);
				workbook.save(currentPath,com.aspose.cells.SaveFormat.XLSX);
			}
			else if (extSlideList.contains(ext))
			{

				//convert to pptx
				com.aspose.slides.Presentation presentation = new com.aspose.slides.Presentation(originalPath);
				presentation.save(currentPath,com.aspose.slides.SaveFormat.Pptx);
			}
			else
				throw new Exception("This conversation not support yet.");
		}
		
		public String extractToXliffAsposeSlideV2(File workingFile,String inputFileName,String ext,
				String sLangSource , String sLangTarget,
				String jobID, String outputPath) throws Exception {
			String output = "";
			oLog.WriteLog(pageName, "extractToXliffAsposeSlide", "start extract", jobID, false);
			try {
				//create working folder
				String root = workingFile.getParent();
				String xliffPath = root + File.separator + "pack1"+File.separator+"work"+File.separator + inputFileName +".xlf"; 
				File xliffFile = new File(xliffPath);
				if (!xliffFile.getParentFile().exists()) {
					xliffFile.getParentFile().mkdirs();		
				}
				
				Presentation pptxPresentation = null;
				
				if (extSlideList.contains(ext.toLowerCase())) {
					try ( Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xliffFile, false), StandardCharsets.UTF_8 )) ) {
						writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
								+ "<xliff version=\"2.0\" xmlns=\"urn:oasis:names:tc:xliff:document:2.0\" srcLang=\""+sLangSource+"\" trgLang=\""+sLangTarget+"\">\n"
						+ "<documentfile original=\""+StringEscapeUtils.escapeXml(inputFileName)+"\" tool=\"Omniscien Technologies\">\n" );
						pptxPresentation = new Presentation(workingFile.getPath());
						int sizeSlide = pptxPresentation.getSlides().size();
						int countIndex = 0;
						StringBuilder sbSlide = new StringBuilder("");
						for (int i = 0; i < sizeSlide ; i++) {
							sbSlide.append("<file id=\"ppt/slides/slide" + (i+1) + ".xml\">\n"); 
//							sbSlide.append("<body>");
							//Get an Array of TextFrameEx objects from the first slide
							ITextFrame[] textFramesSlideOne = SlideUtil.getAllTextBoxes(pptxPresentation.getSlides().get_Item(i));
							countIndex = 1;
							//Loop through the Array of TextFrames
							for (int j = 0; j < textFramesSlideOne.length; j++) {
								StringBuilder sbTextFrame = new StringBuilder("");
								//Loop through paragraphs in current TextFrame
								for(IParagraph para : textFramesSlideOne[j].getParagraphs()) {
									//Loop through portions in the current Paragraph
									int k = 1;
									for(IPortion port : para.getPortions()){
										if (sbTextFrame.toString().equals("")) {
											sbTextFrame.append(CleanExtractRunText(port.getText()));
										}else {
											
											String sLast = getLastIndex(sbTextFrame.toString());
											String sFirst = getFirstIndex(port.getText());
											if (!isNumeric(sLast) && !isNumeric(sFirst)) {
												sbTextFrame.append("<x id=\"" + k+ "\"/>");
												k++;
											}
											sbTextFrame.append(CleanExtractRunText(port.getText()));
											
										}
									}
									String sTemp = sbTextFrame.toString();
									if (null != sTemp && !sTemp.equals("")) {
										sbSlide.append("<unit id=\"SLIDE"+ (i+1)+ "-tu" + countIndex + "\" xml:space=\"preserve\">\n"); 
										sbSlide.append("<segment>\n");
										sbSlide.append("<source xml:lang=\"" + sLangSource +"\">");
										sTemp = removeTagXDateMain(sTemp, sLangSource, jobID);
										sbSlide.append(sTemp);
										sbSlide.append("</source>\n");
										sbSlide.append("</segment>\n");
										sbSlide.append("</unit>\n");
									}
									countIndex ++;
									sbTextFrame = new StringBuilder("");
								}
							}
//							sbSlide.append("</body>");
							sbSlide.append("</file>\n");
							writer.write(sbSlide.toString());
							sbSlide = new StringBuilder("");
						}
						writer.write("</documentfile>\n");
						writer.write("</xliff>");
					} catch (Exception e) {
						throw e;
					}
					finally {
						//save update document
						pptxPresentation.save(workingFile.getPath(), mSaveFormatSlide.get(ext.toLowerCase()));
						pptxPresentation = null;
					}
				}
				//read output xliff file
				output = FileUtils.readFileToString(new File(xliffPath));
//				if (!validateXML(output))
//					oLog.WriteLog(pageName, "extractToXliffAsposeSlide", "INVALID XML output=" + xliffPath, jobID, false);
				
				oLog.WriteLog(pageName, "extractToXliffAsposeSlide", "end extract output=" + xliffPath, jobID, false);
				
				if(outputPath.length() <1) {
					output = FileUtils.readFileToString(new File(xliffPath));
					
				}else {
					if(new File(outputPath).exists()) {
						deleteFile(outputPath);
					}
					Path temp = Files.move(Paths.get(xliffPath), Paths.get(outputPath));
					output = "Generate Xliff file: \""+outputPath+ "\" finished.";
				}
			} catch (Exception e) {
				throw e;
			}
			
			return output;
		}
		
		public String extractToXliffAsposeSlide(File workingFile,String inputFileName,String ext,
				String sLangSource , String sLangTarget,
				String jobID, String outputPath) throws Exception {
			String output = "";
			oLog.WriteLog(pageName, "extractToXliffAsposeSlide", "start extract", jobID, false);
			try {
				
				//create working folder
				String root = workingFile.getParent();
				String xliffPath = root + File.separator + "pack1"+File.separator+"work"+File.separator + inputFileName +".xlf"; 
				File xliffFile = new File(xliffPath);
				if (!xliffFile.getParentFile().exists()) xliffFile.getParentFile().mkdirs();		
				
				Presentation pptxPresentation = null;
				if (extSlideList.contains(ext.toLowerCase()))
				{
					
					try ( Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xliffFile, false), StandardCharsets.UTF_8 )) ) {
						
						writer.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n<xliff version=\"1.2\" xmlns=\"urn:oasis:names:tc:xliff:document:1.2\">\n"
						+"<documentfile original=\""+StringEscapeUtils.escapeXml(inputFileName)+"\" tool=\"Omniscien Technologies\" source-language=\"" + sLangSource 
						+ "\" target-language=\"" + sLangTarget + "\" datatype=\"x-unknown\">\n"
								+ "");
						
						
						pptxPresentation = new Presentation(workingFile.getPath());
			
						int sizeSlide = pptxPresentation.getSlides().size();
						int countIndex = 0;
						StringBuilder sbSlide = new StringBuilder("");
						for (int i = 0; i < sizeSlide ; i++) {
							sbSlide.append("<file original=\"ppt/slides/slide" + (i+1) + ".xml\" source-language=\""+ sLangSource + "\" target-language=\"" + sLangTarget + "\" datatype=\"x-undefined\">"); 
							sbSlide.append("<body>");
							//Get an Array of TextFrameEx objects from the first slide
							ITextFrame[] textFramesSlideOne = SlideUtil.getAllTextBoxes(pptxPresentation.getSlides().get_Item(i));
							countIndex = 1;
							//Loop through the Array of TextFrames
							for (int j = 0; j < textFramesSlideOne.length; j++) {
								StringBuilder sbTextFrame = new StringBuilder("");
								//Loop through paragraphs in current TextFrame
								for(IParagraph para : textFramesSlideOne[j].getParagraphs()) {
									
									//Loop through portions in the current Paragraph
									int k = 1;
									for(IPortion port : para.getPortions())
									{
										if (sbTextFrame.toString().equals("")) {
											sbTextFrame.append(CleanExtractRunText(port.getText()));
										}else {
											
											String sLast = getLastIndex(sbTextFrame.toString());
											String sFirst = getFirstIndex(port.getText());
											if (!isNumeric(sLast) && !isNumeric(sFirst)) {
												sbTextFrame.append("<x id=\"" + k+ "\"/>");
												k++;
											}
											sbTextFrame.append(CleanExtractRunText(port.getText()));
											
										}
										
									}
									
									String sTemp = sbTextFrame.toString();
									if (null != sTemp && !sTemp.equals("")) {
										sbSlide.append("<trans-unit id=\"SLIDE"+ (i+1)+ "-tu" + countIndex + "\" xml:space=\"preserve\">"); 
										sbSlide.append("<source xml:lang=\"" + sLangSource +"\">");
										sTemp = removeTagXDateMain(sTemp, sLangSource, jobID);
										sbSlide.append(sTemp);
										sbSlide.append("</source>");
										sbSlide.append("</trans-unit>");
									}
									countIndex ++;
									sbTextFrame = new StringBuilder("");
								}
							}
							sbSlide.append("</body>");
							sbSlide.append("</file>\n");
							writer.write(sbSlide.toString());
							sbSlide = new StringBuilder("");
							
						}
				
						writer.write("</documentfile>\n</xliff>");
					} catch (Exception e) {
						throw e;
					}
					finally {
						//save update document
						pptxPresentation.save(workingFile.getPath(), mSaveFormatSlide.get(ext.toLowerCase()));
						pptxPresentation = null;
					}
				}
				
				//read output xliff file
				output = FileUtils.readFileToString(new File(xliffPath));
//				if (!validateXML(output))
//					oLog.WriteLog(pageName, "extractToXliffAsposeSlide", "INVALID XML output=" + xliffPath, jobID, false);
				
				oLog.WriteLog(pageName, "extractToXliffAsposeSlide", "end extract output=" + xliffPath, jobID, false);
				
				if(outputPath.length() <1) {
					output = FileUtils.readFileToString(new File(xliffPath));
					
				}else {
					if(new File(outputPath).exists()) {
						deleteFile(outputPath);
					}
					Path temp = Files.move(Paths.get(xliffPath), Paths.get(outputPath));
					output = "Generate Xliff file: \""+outputPath+ "\" finished.";
				}
				
			} catch (Exception e) {
				throw e;
			}
			
			return output;
		}
		
		public String merge(
				String jobID,
				String service,
				String inputFilePath,
				String xliffInputConntent,
				String xliffInputPath,
				String sLangSource,
				String sLangTarget,
				String sOutputPath
				) throws Exception {
			String output = "";
			
			try {
				
				File workingFile = null;
				
				String workingPath =  inputFilePath;
				
				workingFile = new File(workingPath);
				String root = workingFile.getParent();
				
				//get extension
				String ext = FilenameUtils.getExtension(workingFile.getPath()).toLowerCase();
				
				//get working xliff path
				String xliffPath = xliffInputPath;
				
				if (new File(xliffPath).exists()){
					String originalPath = xliffPath;
					File oriFile = new File(originalPath);
				}
				
				//get output msoffice path
				String outputPath = sOutputPath;
				
				if (xliffInputPath.length() > 0) {
					File xliffInputFile = new File(xliffInputPath);
				}else {
					if (extWordList.contains(ext) || abbyyExtToXliffList.contains(ext)) {
						FileUtils.writeStringToFile(new File(xliffPath), xliffInputConntent.replace("LSPIPELS","|"),"UTF-8");
					}else{
						FileUtils.writeStringToFile(new File(xliffPath), xliffInputConntent.toString(),"UTF-8");
					}
				}
				
				oLog.WriteLog(pageName, "merge", "working path=" + workingFile.getPath(), jobID, false);
				
				if (extWordList.contains(ext) || abbyyExtToXliffList.contains(ext)){		

					//udpate workingFile
					if (abbyyExtToXliffList.contains(ext))
					{
						workingFile = new File(workingPath + ".docx");
						outputPath = outputPath + ".docx";
					}
					
					output = mergeFromXliffAspose( jobID, workingFile, xliffPath, sLangSource, sLangTarget, outputPath);
					sOutputPath = outputPath;
				}else if (extCellList.contains(ext)){
					//20200402: Ramoslee: Change to use Aspose.
					output = mergeFromXliffAsposeExcell(jobID, workingFile, xliffPath, sLangSource, sLangTarget, outputPath);
					sOutputPath = outputPath;
				}
				else if (extSlideList.contains(ext)){
//					output = mergeFromXliffAkapi(jobID, workingFile, xliffPath, sLangSource, sLangTarget, outputPath,ext);
					output = mergeFromXliffAsposeSlide(jobID, workingFile, xliffPath, sLangSource, sLangTarget, outputPath, ext);
					sOutputPath = outputPath;
				}

				
			}catch ( Exception e ) {
				oLog.WriteLog(pageName, "merge",  e.getMessage(), jobID, true);
				throw e;
			}
			
			return output;
		}
		
		private String getFileNameFromFullPath(String inputFilePath) {
			String[] inputSplitArr = inputFilePath.split("/");
			int lengthOfInputSplitArr = inputSplitArr.length;
			
			return inputSplitArr[lengthOfInputSplitArr-1];
		}

		public String mergeFromXliffAspose(String jobID,File workingFile,String xliffPath,String sLangSource,String sLangTarget,String outputPath) throws Exception {
			String output = "";
			try {
					

				oLog.WriteLog(pageName, "merge", "start merge", jobID, false);
				String defaultFont = "";
				
				/*Start Ramoslee 20200312: Fix 19: Font Style.*/
				FontConfig fontConfig = getFontConfigFile(fontConfigPath);
				 LangPair firstPriority = null;
				 LangPair secondPriority = null;
				 String font = "";
				if (null != fontConfig) {
					HashMap<String,  LangPair> hashMap = getFontConfigByTargetLanguage(fontConfig, sLangSource, sLangTarget);
		            firstPriority = hashMap.get(sLangSource+"-"+sLangTarget);
		            secondPriority = hashMap.get(sLangTarget);
		            
		            
		            if(null == firstPriority && null != secondPriority) {
		            	defaultFont = getFont(secondPriority.getFonts(), "*");
		            }
				}
				/* End Ramoslee 20200312: Fix 19: Font Style.*/
					String xliffTranslated = FileUtils.readFileToString(new File(xliffPath), "UTF-8");
					
					com.aspose.words.Document doc = new com.aspose.words.Document(workingFile.getPath());
				    // Retrieve all paragraphs in the document.
					com.aspose.words.NodeCollection paragraphs = doc.getChildNodes(com.aspose.words.NodeType.PARAGRAPH, true);
				    // Iterate through all paragraphs
					int paragraphID = 1;
				    for (com.aspose.words.Paragraph para : (Iterable<com.aspose.words.Paragraph>) paragraphs) {
				    	if (para.getRuns().getCount() > 0)
				    	{
				    		
				    		/*Start :20200309 Ramoslee Fix Bullet Issue*/
						   if (para.isListItem()) {
							   
							   ListFormat format = para.getListFormat();
							   
							   /*Set number Style. REF: https://apireference.aspose.com/java/words/com.aspose.words/NumberStyle */
							   int k = format.getListLevelNumber();
							   for (int l = 0; l <= k ; l++) {
								   format.setListLevelNumber(l);
								   Integer numberStyle = format.getListLevel().getNumberStyle();
								   if ("EN".equalsIgnoreCase(sLangTarget)) {
									   if (countingNumberStyle.contains(numberStyle)) {
										   para.getListFormat().getListLevel().setNumberStyle(0); // Arabic Numbering 
									   }
									   
								   // If the bullet point is I, II, III, we should translate as 一、二、三, vice versa.
								   }else if ("ZH".equalsIgnoreCase(sLangTarget)) {
									   if (NumberStyle.UPPERCASE_ROMAN == numberStyle) { // I, II, III, ...
										   para.getListFormat().getListLevel().setNumberStyle(NumberStyle.TRAD_CHIN_NUM_3); // 一、二、三, ... Taiwanese counting thousand
									   }
								   }
							   }
							   
							   /* Replace Comma and Full Stop in number format*/
							   if ("EN".equalsIgnoreCase(sLangTarget)) {
								   String numberFormat = format.getListLevel().getNumberFormat();
								   String tempNumberFormat = "";
								   for (int l = 0; l < numberFormat.length(); l++) {
									   char chr = numberFormat.charAt(l);
									   if (commaUnicode.contains((int) chr)) {
										   chr = ',';
									   }else if (fullStopUnicode.contains((int)chr)) {
										   chr = '.';
									   }
									   
									   tempNumberFormat = tempNumberFormat + chr;
								   }
								   format.getListLevel().setNumberFormat(tempNumberFormat);
								   
							  // If the bullet point is I, II, III, we should translate as 一、二、三, vice versa.
							   }else if ("ZH".equalsIgnoreCase(sLangTarget)) { 
									   String numberFormat = format.getListLevel().getNumberFormat();
									   String tempNumberFormat = "";
									   for (int l = 0; l < numberFormat.length(); l++) {
										   char chr = numberFormat.charAt(l);
										   if ( 44 == (int) chr) { // 44 (,) comma
											   chr = (char) 12289; // 12289 (、) IDEOGRAPHIC COMMA
										   }
										   
										   tempNumberFormat = tempNumberFormat + chr;
									   }
									   format.getListLevel().setNumberFormat(tempNumberFormat);
								}
			   
						   }
						   /*End :20200309 Ramoslee Fix Bullet Issue*/
				    		
				    		//Get translated output paragraph
							String[] sTranslatedParagraph = getTranslatedParagraph(xliffTranslated,sLangTarget,paragraphID);
							String[] sTranslatedParagraphSource = getTranslatedParagraphFromSource(xliffTranslated,sLangSource,paragraphID);	

							
				    		com.aspose.words.Run prevRun = null;
					        // Check all runs in the paragraph for page breaks and remove them.
						   int xIndex = 0;
						   String prevText = "";
						   font = "";
						   com.aspose.words.NodeCollection runs = para.getChildNodes(com.aspose.words.NodeType.RUN, true);
						   // 20200403: Ramoslee : Fix text missing if font is deffirent with previous text.
						   com.aspose.words.NodeCollection originalRun = para.getChildNodes(com.aspose.words.NodeType.RUN, true);
						   List<com.aspose.words.Run> originalRunList = new ArrayList();
						   for (com.aspose.words.Run run : (Iterable<com.aspose.words.Run>) originalRun) {	
							   originalRunList.add(run);
						   }
						   // End: 20200403: Ramoslee
						   int j = 0;
						   String sTextConcat = "";
						   boolean bGetTarget = true;
						   boolean bPageBreak = false;
						   for (com.aspose.words.Run run : (Iterable<com.aspose.words.Run>) runs) {	
//							   com.aspose.words.Run runBeforeChange = (Run) run.deepClone(true);
							   // 20200403: Ramoslee : Fix text missing if font is deffirent with previous text.
							   com.aspose.words.Run runBeforeChange = null;
							   runBeforeChange = (Run) originalRunList.get(j);
							   // End: 20200403: Ramoslee
							   bPageBreak = false;
							   if (!checkIgnoreField(para,run) && !run.isDeleteRevision()) // Ramoslee 20200310: Take only text was not deleted.
							   {
								   // 20201020: Ramoslee: Fix page break issue.
								   if (run.getText().contains(ControlChar.PAGE_BREAK)) {
									   bPageBreak = true;
									}
								   // End 20201020
								   String sText = run.getText();
								   sText = sText.replaceAll("\\u00a0", " ");
								   sTextConcat = sTextConcat + sText;
								   String sSourceText = "";
								   if (sTranslatedParagraphSource.length > xIndex) {
									   sSourceText = sTranslatedParagraphSource[xIndex].replaceAll("\\u00a0", " ").replaceAll("LSEOSDLS", "");
								   }
								   sSourceText = replaceSpacialChar3(CleanMergeRunText(sSourceText));
								   if (prevRun==null)
								   {
									   
									   //System.out.println("-----------------paragraphID=" + paragraphID + "-----------------");
									   //System.out.println("START\t" + sText);
									   //System.out.println("START TX\t" + getSpacialChar(sText) + CleanMergeRunText(sTranslatedParagraph[xIndex]));
									   if (sTranslatedParagraph.length > xIndex) {
//										   run.setText(getSpacialChar(sText) + CleanMergeRunText(sTranslatedParagraph[xIndex]));
										   /*Ramoslee 20200312 : Fix Tab issue: the <x id="SPC..." will be replaced with original special character*/
//										   run.setText(repleceSpacialChar(sText, CleanMergeRunText(sTranslatedParagraph[xIndex])));
//										   run.setText(replaceSpacialChar3(CleanMergeRunText(sTranslatedParagraph[xIndex])));
										   String targetText = replaceSpacialChar3(CleanMergeRunText(sTranslatedParagraph[xIndex]));
										   prevText = targetText;
									   		run.setText(targetText);
									   		bGetTarget = false;
									   }else  run.setText("");
								   }else {
									   if (sTranslatedParagraph.length > xIndex) {
										  
										  
										   if (bGetTarget && sSourceText.trim().indexOf(sText.trim()) == 0 && sText.trim().length() > 0) {
											   String targetText = replaceSpacialChar3(CleanMergeRunText(sTranslatedParagraph[xIndex]));
											   if (nonLanguage.contains(sLangSource)) {
												   if (null != prevText && prevText.length() > 0 && null != targetText && targetText.length() > 0)  {
													   String lastStr = prevText.substring(prevText.length()-1);
													   String firstStr = targetText.substring(0,1);
													   if (!LastStringSkip.contains(lastStr) && !firstStringSkip.contains(firstStr)){
														   targetText = " " + targetText;
													   }
												   }
											   }
											   prevText = targetText;
											   run.setText(targetText);
											   bGetTarget = false;
//											   xIndex++;
										   }else if (bGetTarget && sSourceText.equals(sText)) {
											   String targetText = replaceSpacialChar3(CleanMergeRunText(sTranslatedParagraph[xIndex]));
											   if (nonLanguage.contains(sLangSource)) {
												   if (null != prevText && prevText.length() > 0 && null != targetText && targetText.length() > 0)  {
													   String lastStr = prevText.substring(prevText.length()-1);
													   String firstStr = targetText.substring(0,1);
													   if (!LastStringSkip.contains(lastStr) && !firstStringSkip.contains(firstStr)){
														   targetText = " " + targetText;
													   }
												   }
											   }
											   prevText = targetText;
											   run.setText(targetText);
											   bGetTarget = false;
//											   xIndex++;
										   } else {
											   run.setText("");
										   }
									   }else  run.setText("");
								   }
								   
								   if (sTextConcat.equals(sSourceText)){
									   xIndex++;
									   sTextConcat = "";
									   bGetTarget = true;
								   }
//								   /*Ramoslee 20200312: Content is missing*/
//								   else if (IsRunSameFontStyle(prevRun.getFont(), run.getFont(),paragraphID) && !IsForceXTag(sText,paragraphID))
//								   {			
//									   //System.out.println("SAME\t" + sText);
//									   run.setText("");
//									   xIndex--;
//								   }
//								   else
//								   {
//									   //System.out.println("DIFF\t" + sText);
//									   //System.out.println("DIFF TX\t" + getSpacialChar(sText) + CleanMergeRunText(sTranslatedParagraph[xIndex]));
//									   if (sTranslatedParagraph.length > xIndex) {
////										   run.setText(getSpacialChar(sText) + CleanMergeRunText(sTranslatedParagraph[xIndex]));
//										   /*Ramoslee 20200312 : 
//										    * 1. Fix Tab issue: the <x id="SPC..." will be replaced with original special character
//										    * 2. Fix add " " if previous text is difference style*/
//										   String targetText = replaceSpacialChar3(CleanMergeRunText(sTranslatedParagraph[xIndex]));
//										   if (nonLanguage.contains(sLangSource)) {
//											   if (null != prevText && prevText.length() > 0 && null != targetText && targetText.length() > 0)  {
//												   String lastStr = prevText.substring(prevText.length()-1);
//												   String firstStr = targetText.substring(0,1);
//												   if (!LastStringSkip.contains(lastStr) && !firstStringSkip.contains(firstStr)){
//													   targetText = " " + targetText;
//												   }
//											   }
//										   }
//										   prevText = targetText;
//										   run.setText(targetText);
//									   }else  
//										   run.setText("");
//								   }
								   
									/*Start Ramoslee 20200312: Fix 19: Font Style. One Paragraph One Fonts*/
								   if ((null != firstPriority || null != secondPriority) && (null == font || font.length() <= 0) && (prevText.length() > 0)) {
									   if (null != firstPriority) {
										   font = getFont(firstPriority.getFonts(), runBeforeChange.getFont().getName()); 
									   }else if (null != defaultFont && defaultFont.length() > 0){
										   font = defaultFont;
									   }
								   }
								   if (null != font && font.length() > 0) {
										   run.getFont().setName(font);
									   }
								   /*End Ramoslee 20200312: Fix 19: Font Style.*/
						    	   
								   // 20201020 : Ramoslee : Fix page break missing.
								   if (bPageBreak)
										run.setText(run.getText().concat(ControlChar.PAGE_BREAK));
								   // End 20201010
							   }
							   else
							   {
								// Ramoslee 202008: Fix issue 44 for reference field.
								   if (null != run.getText() && run.getText().toUpperCase().indexOf("REF") >= 0 
										   && sTranslatedParagraphSource.length > xIndex && sTranslatedParagraphSource[xIndex].length() == 0) {
									   xIndex++;
									   sTextConcat = "";
									   bGetTarget = true;
								   }
								   // End: Ramoslee 202008: Fix issue 44 for reference field.
								   //System.out.println("IGNORE\t" + sText);
							   }
							   /*Ramoslee 20200316: Fix issue for the content change after translation.*/
							   prevRun = runBeforeChange;
							   j++;
					       }

						   paragraphID++;						        
				    	}
				    }
				    
				    // Call the method below to update the TOC
				    // To skip the Infinite loop detected from ASPOSE.doc.updateFields();
				    try {
					    // Call the method below to update the TOC
						doc.updateFields();
					}catch(Exception e) {
						oLog.WriteLog(pageName, "merge", oLog.getStackTrace(e), jobID, true);
					}
				    
				    //save output file after update translate output
				    doc.save(outputPath);

				//read output from msoffice file
				oLog.WriteLog(pageName, "merge",  "end merge output=" + outputPath, jobID, false);
				byte[] bData = FileUtils.readFileToByteArray(new File(outputPath));
				output = new String(Base64Coder.encode(bData));
				
			}
			catch ( Exception e ) {
				oLog.WriteLog(pageName, "merge",  e.getMessage(), jobID, true);
				throw e;
			}
			return output;
		}
		
		private FontConfig getFontConfigFile(String configPath) {
			
			FontConfig fontConfig = null;
			try {
				if (null != configPath) {
					Reader reader = new FileReader(configPath);
//					System.out.println("Ramoslee Test: " + reader.read());
					fontConfig = new FontConfig();
		            Gson gson = new Gson();
		            fontConfig = gson.fromJson(reader, FontConfig.class);
				}else {
					oLog.WriteLog(pageName, "getFontConfigFile", "configPath IS NULL.", "", true);
				}
				
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				oLog.WriteLog(pageName, "getFontConfigFile",  e.getMessage(), "", true);
//				System.out.println("getFontConfigFile : " + e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				oLog.WriteLog(pageName, "getFontConfigFile",  e.getMessage(), "", true);
			} catch (Exception e) {
				oLog.WriteLog(pageName, "getFontConfigFile",  e.getMessage(), "", true);
			}
			
			return fontConfig;
		}
		
		private HashMap<String, LangPair> getFontConfigByTargetLanguage(FontConfig fontConfig, String lgSource, String lgTarget){
			HashMap<String,  LangPair> output = new HashMap<String,  LangPair>();
			String key = lgSource + "-" +lgTarget;
			
			List< LangPair> fonConfigList = fontConfig.getFontConfig();
			
			 LangPair fontConfigs = fonConfigList.stream().filter(x -> key.equalsIgnoreCase(x.langPair)).findFirst().orElse(null);
			 LangPair fontConfigsTg = fonConfigList.stream().filter(x -> lgTarget.equalsIgnoreCase(x.langPair)).findFirst().orElse(null);
			
			output.put(key, fontConfigs);
			output.put(lgTarget, fontConfigsTg);
			return output;
		}
		
		private String getFont(List< Fonts> sFontList, String sFont) {
			String outputFont = "";
			
			 Fonts fonts = sFontList.stream().filter( x -> sFont.equalsIgnoreCase(x.getSource())).findFirst().orElse(null);
			if (null == fonts) {
				fonts = sFontList.stream().filter( x -> "*".equalsIgnoreCase(x.getSource())).findFirst().orElse(null);
			}
			
			if (null != fonts) {
				outputFont = fonts.getTarget();
			}
			
			return outputFont;
		}
		
		private String[] getTranslatedParagraph(String xliffInputConntent ,String targetLanguage, int paragraphID)
		{
			Pattern pTU = Pattern.compile("<trans-unit[ ]id=\"pid"+paragraphID+"\"[^<>]*>(.+?)<\\/trans-unit>",
					Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Pattern pTarget = Pattern.compile("<target xml:lang=\"" + targetLanguage + "\"[^><]*?>(.+?)<\\/target>",
					Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			
			Matcher mTU = pTU.matcher(xliffInputConntent);
			String sTU = "",sTarget = "";
			while (mTU.find()) {
				// All content in TU
				sTU = mTU.group(1) == null  ? "" : mTU.group(1);
				if (sTU.length() > 0)
				{
					Matcher mTarget = pTarget.matcher(sTU);
					while (mTarget.find()) {
						sTarget = mTarget.group(1) == null ? "" : mTarget.group(1);
						break;
					}				
				}
				break;
			}
			return StringEscapeUtils.unescapeXml(sTarget).split("<x[ ]id=\"(\\d+)\"\\/>");
		}
		
		private String[] getTranslatedParagraphFromSource(String xliffInputConntent ,String sSourceLang, int paragraphID)
		{
			Pattern pTU = Pattern.compile("<trans-unit[ ]id=\"pid"+paragraphID+"\"[^<>]*>(.+?)<\\/trans-unit>",
					Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Pattern pTarget = Pattern.compile("<source xml:lang=\"" + sSourceLang + "\"[^><]*?>(.+?)<\\/source>",
//			Pattern pTarget = Pattern.compile("<target xml:lang=\"" + targetLanguage + "\"[^><]*?>(.+?)<\\/target>",
					Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			
			Matcher mTU = pTU.matcher(xliffInputConntent);
			String sTU = "",sTarget = "";
			while (mTU.find()) {
				// All content in TU
				sTU = mTU.group(1) == null  ? "" : mTU.group(1);
				if (sTU.length() > 0)
				{
					Matcher mTarget = pTarget.matcher(sTU);
					while (mTarget.find()) {
						sTarget = mTarget.group(1) == null ? "" : mTarget.group(1);
						break;
					}				
				}
				break;
			}
			return StringEscapeUtils.unescapeXml(sTarget).split("<x[ ]id=\"(\\d+)\"\\/>");
		}
		
		public String mergeFromXliffAsposeSlide(String jobID,File workingFile,String xliffPath,String sLangSource,String sLangTarget,String outputPath, String ext)  throws Exception {
			String output = "";
			
			oLog.WriteLog(pageName, "mergeFromXliffAsposeSlide", "Start Merge", jobID, false);
			try {
				
				String xliffTranslated = FileUtils.readFileToString(new File(xliffPath), "UTF-8");
				xliffTranslated = CleanMergeRunText(xliffTranslated);
				//create working folder
				String root = workingFile.getParent();
				Presentation pptxPresentation = null;
				pptxPresentation = new Presentation(workingFile.getPath());
			
				int sizeSlide = pptxPresentation.getSlides().size();
				int countIndex = 0;
				
				for (int i = 0; i < sizeSlide ; i++) {
					String sSlideText = getTranslatedSlide(xliffTranslated, i+1);
					//Get an Array of TextFrameEx objects from the first slide
					ITextFrame[] textFramesSlideOne = SlideUtil.getAllTextBoxes(pptxPresentation.getSlides().get_Item(i));
					countIndex = 1;
					if (null != sSlideText && sSlideText.trim().length() > 0) {
						sSlideText = CleanMergeRunText(sSlideText);
						//Loop through the Array of TextFrames
						for (int j = 0; j < textFramesSlideOne.length; j++) {
							//Loop through paragraphs in current TextFrame
							for(IParagraph para : textFramesSlideOne[j].getParagraphs()) {

								String[] sArrPortionText  = getTranslatedParagraphSlide(sSlideText, sLangTarget, i+1, countIndex);
								String[] sArrPortionTextSource  = getTranslatedParagraphSlideSource(sSlideText, sLangSource, i+1, countIndex);
								
								//Loop through portions in the current Paragraph
								int k = 0;
								IPortion prevPort = null;
								String prevText = "";
								String sTextConcat = "";
								boolean bGetTarget = true;
								for(IPortion port : para.getPortions())
								{
									//Display text in the current portion
									prevPort = port;
									String sText = port.getText();
									sText = sText.replaceAll("\\u00a0", " ");
								    sTextConcat = sTextConcat + sText;
								    String sSourceText = "";
								    if (sArrPortionTextSource.length > k) {
								    	sSourceText = sArrPortionTextSource[k].replaceAll("\\u00a0", " ").replaceAll("LSEOSDLS", "");
								    }
								    sSourceText = replaceSpacialChar3(CleanMergeRunText(sSourceText));
								    
									if (null != sArrPortionText && sArrPortionText.length > 0 && k <  sArrPortionText.length) {
										
										if (bGetTarget && (sSourceText.trim().indexOf(sText.trim()) == 0 && sText.trim().length() > 0) 
												|| (sSourceText.equals(sText))) {
											String targetText = replaceSpacialChar3(sArrPortionText[k]);
											if (nonLanguage.contains(sLangSource)) {
											   if (null != prevText && prevText.length() > 0 && null != targetText && targetText.length() > 0)  {
												   String lastStr = prevText.substring(prevText.length()-1);
												   String firstStr = targetText.substring(0,1);
												   if (!LastStringSkip.contains(lastStr) && !firstStringSkip.contains(firstStr)){
													   targetText = " " + targetText;
												   }
											   }
										    }
										    prevText = targetText;
											port.setText(targetText);
											bGetTarget = false;
										}else {
											port.setText("");
										}
									}else {
										port.setText("");
									}
									
									if (sTextConcat.equals(sSourceText)){
										   k++;
										   sTextConcat = "";
										   bGetTarget = true;
									   }
								}
								
								countIndex ++;
							
							}
						}
					}
				
				}
			    
				File fFile = new File(outputPath);
				if (!fFile.exists()) {
					fFile.mkdirs();
				}
			    //save output file after update translate output
			    pptxPresentation.save(outputPath, mSaveFormatSlide.get(ext.toLowerCase()));
				
//				oLog.WriteLog(pageName, "merge",  "end merge output=" + outputPath, jobID, false);
				byte[] bData = FileUtils.readFileToByteArray(new File(outputPath));
				output = new String(Base64Coder.encode(bData));
				
			} catch (Exception e) {
				throw e;
			}
			
			oLog.WriteLog(pageName, "mergeFromXliffAsposeSlide", "End Merge", jobID, false);

			return output;
		}
		
		private String getTranslatedSlide(String xliffInputConntent, int iSlide)
		{
			Pattern pFileSlide = Pattern.compile("<file[ ]original=\"ppt/slides/slide"+ iSlide + ".xml\"[^<>]*><body>(.+?)</body></file>",
					Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			
			Matcher mTU = pFileSlide.matcher(xliffInputConntent);
			String sTarget = "";
			if (mTU.find()) {
				// All content in TU
				sTarget = mTU.group(1) == null  ? "" : mTU.group(1);
				//System.out.println("mTU.group(1) :::" + i + ":::" + sTarget);
			}
			return sTarget;
		}
		
		private String[] getTranslatedParagraphSlide(String xliffInputConntent ,String targetLanguage, int iSlide, int paragraphID)
		{
			Pattern pTU = Pattern.compile("<trans-unit[ ]id=\"SLIDE"+iSlide+"-tu"+paragraphID+"\"[ ]xml:space=\"preserve\"[^<>]*>(.+?)</trans-unit>",
					Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
//			Pattern pTarget = Pattern.compile("<source xml:lang=\"" + targetLanguage + "\"[^><]*?>(.+?)<\\/source>",
			Pattern pTarget = Pattern.compile("<target xml:lang=\"" + targetLanguage + "\"[^><]*?>(.+?)<\\/target>",
					Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			
			Matcher mTU = pTU.matcher(xliffInputConntent);
			String sTU = "",sTarget = "";
			if (mTU.find()) {
				// All content in TU
				sTU = mTU.group(1) == null  ? "" : mTU.group(1);
				if (sTU.length() > 0)
				{
					Matcher mTarget = pTarget.matcher(sTU);
					if (mTarget.find()) {
						sTarget = mTarget.group(1) == null ? "" : mTarget.group(1);
					}				
				}
			}
//			sTarget = sTarget.replaceAll("<\\/g>", "");

			return StringEscapeUtils.unescapeXml(sTarget).split("<x[ ]id=\"(\\d+)\"\\/>");
		}
		
		private String[] getTranslatedParagraphSlideSource(String xliffInputConntent ,String targetLanguage, int iSlide, int paragraphID)
		{
			Pattern pTU = Pattern.compile("<trans-unit[ ]id=\"SLIDE"+iSlide+"-tu"+paragraphID+"\"[ ]xml:space=\"preserve\"[^<>]*>(.+?)</trans-unit>",
					Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Pattern pTarget = Pattern.compile("<source xml:lang=\"" + targetLanguage + "\"[^><]*?>(.+?)<\\/source>",
//			Pattern pTarget = Pattern.compile("<target xml:lang=\"" + targetLanguage + "\"[^><]*?>(.+?)<\\/target>",
					Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			
			Matcher mTU = pTU.matcher(xliffInputConntent);
			String sTU = "",sTarget = "";
			if (mTU.find()) {
				// All content in TU
				sTU = mTU.group(1) == null  ? "" : mTU.group(1);
				if (sTU.length() > 0)
				{
					Matcher mTarget = pTarget.matcher(sTU);
					if (mTarget.find()) {
						sTarget = mTarget.group(1) == null ? "" : mTarget.group(1);
					}				
				}
			}
			return StringEscapeUtils.unescapeXml(sTarget).split("<x[ ]id=\"(\\d+)\"\\/>");
			
		}
		
		public void extractImageFromPDFAllPage(String inputODFFilePath,String outputFilePath,String prefixOfImageFile) {
			PdfConverter objConverter = new PdfConverter();
			objConverter.bindPdf(inputODFFilePath);
			
			// initialize the converting process
			objConverter.doConvert();
			int i = 1;
			
			while (objConverter.hasNextImage()) {
//				objConverter.getNextImage(i + ".jpg", ImageType.getJpeg());
				objConverter.getNextImage(outputFilePath+"/"+prefixOfImageFile+i+".jpg", ImageType.getJpeg());
				i++;
			}
			objConverter.close();
			
		}
		
		public boolean extractOnlyImageFromPDF(String inputODFFilePath,String outputFilePath,String prefixOfImageFile) {
			boolean extractStatus = false;
			PdfExtractor pdfExtractor = new PdfExtractor();
			pdfExtractor.bindPdf(inputODFFilePath);
			
			// Extract all the images
			int i = 1;
			pdfExtractor.extractImage();
			while (pdfExtractor.hasNextImage()) {				
				extractStatus = pdfExtractor.getNextImage(outputFilePath+"/"+prefixOfImageFile+i+".jpg", ImageType.getJpeg());
				i++;
			}
			
			return extractStatus;
			
		}
}
