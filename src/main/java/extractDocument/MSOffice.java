package extractDocument;

import java.util.concurrent.atomic.AtomicReference;


import org.apache.commons.codec.binary.Base64;

import util.ProcessUtil;

import util.Common;
import util.Log4J;
import model.ServletContextMock;


public class MSOffice extends MSOfficeBase {
	
	private static util.Log4J oLog = null;
	private static Common oCommon = null;
	private static String sPageName = "MSOffice.java";
	//private static MSOffice instance = new MSOffice();

	public MSOffice() {
		
	}
	
	public String Extract(String jobid, String inputpath, String  sourcelanguage, String  targetlanguage, String format) {
		String sOutput = "";
		
		String service = "";
		
		String inputfilename = getInputFileName(inputpath);
		
		String inputcontent = "";
		
		ServletContextMock app = new ServletContextMock();
		
		boolean bMerge = false;
		
		AtomicReference<String> sDocxContent = null;
		
		try {
			sOutput = Extract(
					jobid,
					service,
					inputfilename,
					inputcontent,
					inputpath,
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
	
	private String getInputFileName(String inputpath) {
		// TODO Auto-generated method stub
		String[] inputpathArr = inputpath.split("/");
		return  inputpathArr[inputpathArr.length-1];
	}

	public String Extract(
			//1 jobid
			String jobid,
			//2 service
			String service,
			//3 inputfilename
			String  inputfilename,
			//4 inputcontent
			String  inputcontent,
			//5 inputpath
			String  inputpath,
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
			oLog = new Log4J(app);
			oLog.debugMode = getDebugMode();
			oLog.setDebugPath(getDebugPath());
			oLog.log4JPropertyFile = getLog4JPath();
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
		ProcessUtil oProcess = new ProcessUtil(
						getResourcesPath(), 
						getABBYYExtension(), 
						app, 
						oLog, 
						getFontConfigPath(), 
						getABBYYPath(), 
						getABBYYGetInfoFileName(), 
						getABBYYWaitInterval(), 
						getIsSharedCPUCoresMode(), 
						getTimeToRemoveJobMinutes()
						);
		sOutput = oProcess.extract(jobid, service, inputfilename, inputcontent, inputpath, sourcelanguage, targetlanguage, format, bMerge, sDocxContent);
				
		
		
		return sOutput;
	}
		

}
