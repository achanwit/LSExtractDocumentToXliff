package com.omniscien.lsmsoffice.process;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.ServletContext;

import org.apache.commons.codec.binary.Base64;

import com.omniscien.lsmsoffice.model.ServletContextMock;
import com.omniscien.lsmsoffice.util.Common;
import com.omniscien.lsmsoffice.util.Log4J;
import com.omniscien.lsmsoffice.util.ProcessUtil2;
import com.omniscien.lsmsoffice.util.ReadProp;

public class Merge {
	
	private static com.omniscien.lsmsoffice.util.Log4J oLog = null;
	private static Common oCommon = null;
	private static String sPageName = "merge.java";
	private ReadProp rp = new ReadProp();
	private ServletContextMock app = new ServletContextMock();

	public Merge() {
		// TODO Auto-generated constructor stub
	}
	
	public String Mergr(
			String jobid,
			String inputfilename,
			String xliffpath,
			String sourcelanguage,
			String targetlanguage,
			String sOutputPath
			) {
		String output = "";
		
		String xliffcontent = "";
		String service = "";
		
		output = Merge(jobid, service, inputfilename, xliffcontent, xliffpath, sourcelanguage, targetlanguage, sOutputPath);
		
		return output;
	}
	
	public String Merge(
			String jobid,
			String service, 
			String inputfilename, 
			String xliffcontent, 
			String xliffpath, 
			String sourcelanguage,
			String  targetlanguage,
			String sOutputPath
			) {
		String sOutput = "";
		
		
		
		if (oLog == null) {
			try {
				oLog = new Log4J(app);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			oLog.debugMode = true;
			oLog.setDebugPath(rp.getProp(com.omniscien.lsmsoffice.util.ConstantOfExtractDoc.LOG_PATH));
			oLog.log4JPropertyFile = rp.getProp(com.omniscien.lsmsoffice.util.ConstantOfExtractDoc.LOG_4J);
		}
		if (oCommon == null) {
			oCommon = new Common();
		}
		
		//decode base64
		if (Base64.isBase64(xliffcontent + ".")) {
			xliffcontent = new String(Base64.decodeBase64(xliffcontent));		
		}
		
		//call score
		ProcessUtil2 oProcess = null;
		try {
			oProcess =  new ProcessUtil2(
//				getResourcesPath(),
					rp.getProp(com.omniscien.lsmsoffice.util.ConstantOfExtractDoc.ResourcesPath),
//				getABBYYExtension(), 
					rp.getProp(com.omniscien.lsmsoffice.util.ConstantOfExtractDoc.ABBYYExtension),
					app, 
					oLog, 
//				getFontConfigPath(), 
					rp.getProp(com.omniscien.lsmsoffice.util.ConstantOfExtractDoc.FontConfigPath),
//				getABBYYPath(), 
					rp.getProp(com.omniscien.lsmsoffice.util.ConstantOfExtractDoc.ABBYYPath),
//				getABBYYGetInfoFileName(), 
					rp.getProp(com.omniscien.lsmsoffice.util.ConstantOfExtractDoc.ABBYYGetInfoFileName),
//				getABBYYWaitInterval(),
					rp.getProp(com.omniscien.lsmsoffice.util.ConstantOfExtractDoc.ABBYYWaitInterval),
//				getIsSharedCPUCoresMode(), 
					Boolean.valueOf(rp.getProp(com.omniscien.lsmsoffice.util.ConstantOfExtractDoc.IsSharedCPUCoresMode)),
//				getTimeToRemoveJobMinutes()
					Integer.parseInt(rp.getProp(com.omniscien.lsmsoffice.util.ConstantOfExtractDoc.TimeToRemoveJobMinutes))    
					);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			/*** Param 
			 * 	String jobID,
				String service,
				String inputFileName,
				String xliffInputConntent,
				String xliffInputPath,
				String sLangSource,
				String sLangTarget,
				String sOutputPath
			 */
			sOutput = oProcess.merge(jobid, service, inputfilename, xliffcontent, xliffpath, sourcelanguage, targetlanguage, sOutputPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sOutput;
		
	}

}
