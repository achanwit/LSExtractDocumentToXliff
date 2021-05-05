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
	private ReadProp rp=null;
	private ServletContextMock app = new ServletContextMock();

	public Merge() {
		// TODO Auto-generated constructor stub
	}
	
	public void propertiesSetting(String filePath) {
		rp = new ReadProp(filePath);
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
				oLog = new Log4J(app, rp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			oLog.debugMode = true;
			oLog.setDebugPath(rp.getProp(com.omniscien.lsmsoffice.util.Constant.LOG_PATH));
			oLog.log4JPropertyFile = rp.getProp(com.omniscien.lsmsoffice.util.Constant.LOG_4J);
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
					rp.getProp(com.omniscien.lsmsoffice.util.Constant.ResourcesPath),
//				getABBYYExtension(), 
					rp.getProp(com.omniscien.lsmsoffice.util.Constant.ABBYYExtension),
					app, 
					oLog, 
//				getFontConfigPath(), 
					rp.getProp(com.omniscien.lsmsoffice.util.Constant.FontConfigPath),
//				getABBYYPath(), 
					rp.getProp(com.omniscien.lsmsoffice.util.Constant.ABBYYPath),
//				getABBYYGetInfoFileName(), 
					rp.getProp(com.omniscien.lsmsoffice.util.Constant.ABBYYGetInfoFileName),
//				getABBYYWaitInterval(),
					rp.getProp(com.omniscien.lsmsoffice.util.Constant.ABBYYWaitInterval),
//				getIsSharedCPUCoresMode(), 
					Boolean.valueOf(rp.getProp(com.omniscien.lsmsoffice.util.Constant.IsSharedCPUCoresMode)),
//				getTimeToRemoveJobMinutes()
					Integer.parseInt(rp.getProp(com.omniscien.lsmsoffice.util.Constant.TimeToRemoveJobMinutes)),
					rp
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
