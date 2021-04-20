package com.omniscien.lsmsoffice.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;


public class Common {
	public Common() {
		
	}
	
	public String trimLog(String content)
	{	
		String out = content.split("\n")[0].substring(0,  content.split("\n")[0].length() > 20 ? 20 : content.split("\n")[0].length());
		if (out.length() == 20) out += "...";
		return out;
	}
	public String normalizeLineFeed(String content)
	{
		return content.replace("\r\n", "\n").replace("\r", "\n");
	}
	public int GetWordCount(String text)
	{
		return CleanAllTag(text).split("\\s+").length;
	}
	   //
    private String CleanAllTag(String contentText)
    {
    	contentText = ReplaceByPattern(contentText, "<.*?>", "", Pattern.DOTALL);
    	contentText = ReplaceByPattern(contentText, "</.*?>", "", Pattern.DOTALL);
    	
    	//unescape more than 1 time to make sure 
    	contentText = StringEscapeUtils.unescapeHtml3(contentText);
    	contentText = StringEscapeUtils.unescapeHtml3(contentText);
    	contentText = StringEscapeUtils.unescapeHtml3(contentText);
    	contentText = StringEscapeUtils.unescapeHtml3(contentText);
    	//
    	
    	contentText = ReplaceByPattern(contentText, ">\\s+<", "><", Pattern.DOTALL);
    	return contentText.replaceAll("\\s+", " ").trim();
    }
    public  String ReplaceByPattern(String input, String pattern, String replacement, int regexOptions)
    {
        String resultString = "";
        Pattern regex = Pattern.compile(pattern, regexOptions);
        Matcher regexMatcher = regex.matcher(input);
        try
        {
            resultString = regexMatcher.replaceAll(replacement);
        }
        catch(IllegalArgumentException ex) { }
        catch(IndexOutOfBoundsException ex) { }
        return resultString;
    }

	public String round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		//System.out.println(value);
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);

		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(bd.doubleValue());
	}
	
	public String getDateString(Calendar cal)
	{	
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
		if (cal != null)
			return format1.format(cal.getTime());
		return "";
	}
	
	
	public String getModelPath(String sModelPath, String sModelName, String sToolType) {
		if (sModelPath == null || sModelPath.trim().length() == 0) {
			String sLocalPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			File fPath = new File(sLocalPath);
			if (fPath.isFile()) {
				sLocalPath = sLocalPath.substring(0, sLocalPath.lastIndexOf("/"));
			} else if (!fPath.exists())
				fPath.mkdirs();	
			
			sLocalPath = sLocalPath.endsWith("/")? sLocalPath : sLocalPath + "/";			
			sModelPath = sLocalPath + "resources/languageid/" + sToolType + "/" + sModelName;
			return sModelPath;
		}
		else
			return sModelPath + sModelName;
	}
	
	public Boolean fileExists(String pathFile) throws Exception {
		Boolean bExist = false;
		try {
			if (isWindows())
				pathFile = pathFile.replace("/", "\\");
			else
				pathFile = pathFile.replace("\\", "/");

			File file = new File(pathFile);
			if (file.isFile() && file.exists()) {
				bExist = true;
			}
		} catch (Exception ex) { }
		return bExist;
	}
	
	public boolean isWindows(){
		 
		String os = System.getProperty("os.name").toLowerCase();
		//windows
	    return (os.indexOf( "win" ) >= 0); 
 
	}
	
	public String ChkNullStrObj(Object obj) {
		try {
			if (obj == null)
				return "";
			else
				return obj.toString();
		} catch (Exception ex) {
			return "";
		}
	}

	public int ChkNullIntObj(Object obj) {
		try {
			if (obj == null)
				return 0;
			else if (obj.toString().trim().length() == 0)
				return 0;
			else
				return Integer.parseInt(obj.toString());

		} catch (Exception e) {
			return 0;
		}
	}

	public Boolean ChkNullBooleanObj(Object obj) {
		try {
			if (obj == null)
				return false;
			else {
				if (obj.toString().equals("1"))
					return true;
				else
					return Boolean.valueOf(obj.toString());
			}
			
		} catch (Exception e) {
			return false;
		}
	}
	
	public long ChkNullLongObj(Object obj) {
		try {
			if (obj == null)
				return 0;
			else if (obj.equals(""))
				return 0;
			else
				return Long.parseLong(obj.toString());
		} catch (Exception e) {
			return 0;
		}
	}
	
	public double ChkNullDoubleObj(Object obj) {
		try {
			if (obj == null)
				return 0;
			else if (obj.equals(""))
				return 0;
			else
				return Double.parseDouble(obj.toString());
		} catch (Exception e) {
			return 0;
		}
	}
	
	public boolean isBool(String input) {
		Boolean bIsBool = false;
		if (input != null && (input.trim().equalsIgnoreCase("true") || input.trim().equals("1"))) {
			bIsBool = true;
		} else if (input != null && (input.trim().equalsIgnoreCase("false") || input.trim().equals("0"))) {
			bIsBool = true;
		}
		return bIsBool;
	}
	
	public boolean isNumber(String input) {
		Pattern p = Pattern.compile("^[0-9]+$");
		Matcher m = p.matcher(input);
		boolean isnumber = false;
		if (m.find()) {
			isnumber = true;
		}
		return isnumber;
	}

	public boolean IsEmpty(Object object) {
		if (object == null)
			return true;

		if (object.toString().trim().length() == 0)
			return true;

		return false;
	}
	
	public String cleanXliff(String output)
	{
		//clean junk tag
		/**/
		output = cleanNotUseXliffTag(output,"(<file\\s+original=\"docProps/core[^>]*>.*?</file>)");
		output = cleanNotUseXliffTag(output,"(<file\\s+original=\"word/styles[^>]*>.*?</file>)");
		output = cleanNotUseXliffTag(output,"(<file\\s+original=\"word/settings[^>]*>.*?</file>)");
		output = cleanNotUseXliffTag(output,"(<file\\s+original=\"ppt/slideMasters[^>]*>.*?</file>)");
		output = cleanNotUseXliffTag(output,"(<file\\s+original=\"ppt/slideLayouts[^>]*>.*?</file>)");
		output = cleanNotUseXliffTag(output,"(<file\\s+original=\"word/header[^>]*>.*?</file>)");
		output = cleanNotUseXliffTag(output,"(<file\\s+original=\"word/footer[^>]*>.*?</file>)");
		output = cleanNotUseXliffTag(output,"(<file\\s+original=\"word/endnotes[^>]*>.*?</file>)");
		output = cleanNotUseXliffTag(output,"(<file\\s+original=\"word/footnotes[^>]*>.*?</file>)");
		
		//replace leading number
		output = cleanLeadingNumber(output);
		
		//fixed <g id="1">Sorry - just one <g id="2">&lt;squeak></g> more &lt;<g id="3">squeak></g> spot &lt;<g id="4">squeak</g>> -- oh!<x id="5"/>
		output = output.replaceAll("(&lt;)(<g[ ]+id=\"[^><]*\">)", "$2$1");
		//fixed 
		//before <x id="1"/><g id="2">(a)<x id="3"/></g><g id="4">B</g><g id="5">ack-to-Back Facilities<g id="6"><x id="7"/>(USD77,000,000.-)</g></g>
		//after <x id="1"/><g id="2">(a)<x id="3"/></g><g id="4">B</g><g id="5">ack-to-Back Facilities<g id="6"><x id="7"/>(USD77,000,000.-)</g></g>
		output = output.replaceAll("(<g[ ]id=\\\"\\d\\\">)([A-Z]{1})(<\\/g><g[ ]id=\\\"\\d\\\">)([a-zA-Z]{1})", "$1$3$2$4");		
		output = output.replaceAll("(<\\/g>)(\\>)", "$2$1");
		output = output.replaceAll("[\\n]+", "\n");
		return output;
	}
	
	private String cleanLeadingNumber(String output)
	{
		/*
		<trans-unit id="NFDBB2FA9-tu8" xml:space="preserve">
		<source xml:lang="zh"><g id="1">(6) <x id="2"/>Please refer to the Supplemental Provisions.</g></source>
		<target xml:lang="en"><g id="1">(6) <x id="2"/>Please refer to the Supplemental Provisions.</g></target>
		</trans-unit>
		 */
		Pattern p = Pattern.compile("<trans-unit[^>]*>(.*?)</trans-unit>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(output);
		while (m.find()) {
			String tu = m.group();
			String tuOut = m.group();
			
			Pattern ps = Pattern.compile("(<source[^>]*>)(.*?)(</source>)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			Matcher ms = ps.matcher(tu);
			while (ms.find()) {
				String souce = ms.group();
				String sOut = ms.group(1) + repareLeadingNumber(ms.group(2)) + ms.group(3);
				tuOut = tuOut.replace(souce, sOut);
			}

			Pattern pt = Pattern.compile("(<target[^>]*>)(.*?)(</target>)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			Matcher mt = pt.matcher(tu);	
			while (mt.find()) {
				String target = mt.group();
				String tOut = mt.group(1) + repareLeadingNumber(mt.group(2)) + mt.group(3);
				tuOut = tuOut.replace(target, tOut);
			}
			
			output = output.replace(tu, tuOut);
		}
		return output;
	}
	
	private String sPunctPattern = "(^|[ \"'\\t(]\\s*)[\\&|\\~|\\'|\\*|\\,|\\ã€�|\\.|\\Â·|\\ã€‚|\\â€¢|\\â€¦|\\:|\\;|\\^|\\/|\\-|\\–|\\-|\\־|\\‑|\\‒|\\﹣|\\－|\\‐|\\\\|\\!|\\Â¡|\\?|\\Â¿|\\Â¦|\\-|\\Â­|\\â€¾|\\â€“|\\â€”|\\Â¬|\\â€ |\\â€¡|\\Â§|\\Â¶|\\(|\\)|\\+|\\#|\\$|\\%|\\^|\\&|\\*|\\[|\\]|\\{|\\}|\\<|\\>|\\,|\\\"]+($|[ \"'\\t(]\\s*)";
	private String repareLeadingNumber(String output)
	{
		String[] arOutput = output.split("<x id=");
		if (arOutput[0].trim().length() > 0)
		{
			String check = arOutput[0].replaceAll("<g[ ]+id=\"[^><]*\">","").replaceAll("<\\/g>", "")
					.replaceAll("\\d+","").replaceAll("\\p{Punct}","").replaceAll(sPunctPattern,"").replaceAll("\\s+","");
			String sOutput0New = ""; 
			
			if (check.length() == 0)
			{
				//<g id="1">9</g><g id="2">.<g id="3">1
				String sLeading = arOutput[0].replaceAll("<g[ ]+id=\"[^><]*\">","").replaceAll("<\\/g>", "");
				String[] arOutput0 = arOutput[0].replaceAll("(>)([^><].*?)(<)", "$1$3").replaceAll("(>)([^><].*?)$", "$1").replaceAll("^([^><].*?)(<)", "$2").split("\">");
				if (arOutput0.length > 1)
				{
					boolean bAppendLeading = false;
					for (int i = 0; i < arOutput0.length; i++) {
						if (bAppendLeading == false && arOutput0[i].startsWith("<g"))
						{
								sOutput0New += arOutput0[i] + "\">" + sLeading;
								bAppendLeading = true;
						}
						else 
						{
							if (!arOutput0[i].endsWith(">"))
								sOutput0New += arOutput0[i] + "\">";
							else
								sOutput0New += arOutput0[i];
						}
					}
					output = output.replace(arOutput[0], sOutput0New);
					
				}
			}
		}
		return output;
	}
	
	private String cleanNotUseXliffTag(String input , String regSource)
	{
		String removeContent = "";
		Pattern pSource = Pattern.compile(regSource, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		Matcher mSource = pSource.matcher(input);
		while (mSource.find()) {
			removeContent = mSource.group(1);
			input = input.replace(removeContent+"\n","");
		}
		return input;
	}

	public String cleanHtml(String html)
	{
		html = html.replaceAll("<!--\\[if[^>]*\\]>[^!]*<!\\[endif\\]-->", "");
		html = html.replaceAll("(<!\\[if[^>]*\\]>)([^!]*)(<!\\[endif\\]>)", "$2");
		html = html.replaceAll("<!--\\[if[^>]*\\]>[^!]*<!\\[endif\\]", "");
		html = html.replace("<o:p>   </o:p>", "<br/>");
		html = html.replace("<o:p></o:p>", "").replace(" ", " ");
		html = html.replace(" ", " ");
		return html;
	}	
 
	public enum Type {XLSX, XLS, DOC, DOCX,PPT, PPTX , VSD,VSDX} 
	public Type referenceToType(String abbyyextension ,String reference,boolean bForceX) {
        Type type = null;
        String referenceInLowerCase = reference.toLowerCase();

        if (!bForceX)
        {
        	if (referenceInLowerCase.endsWith(".doc") 
	        		|| referenceInLowerCase.equals("application/msword")) {
	            type = Type.DOC;
	        } else if (referenceInLowerCase.endsWith(".docx")
	        		|| isABBYYExtension(abbyyextension, referenceInLowerCase)
	        		|| referenceInLowerCase.endsWith(".dot") || referenceInLowerCase.endsWith(".dotx") 
	        		|| referenceInLowerCase.endsWith(".odt") || referenceInLowerCase.endsWith(".ott") 
	        		|| referenceInLowerCase.endsWith(".rtf")
	        		|| referenceInLowerCase.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
	            type = Type.DOCX;
	        } else if (referenceInLowerCase.endsWith(".xls") 
	        		|| referenceInLowerCase.equals("application/vnd.ms-excel")) {
	            type = Type.XLS;
	        } else if (referenceInLowerCase.endsWith(".xlsx") 
	        		|| referenceInLowerCase.endsWith(".xlt") || referenceInLowerCase.endsWith(".xltx") 
	        		|| referenceInLowerCase.endsWith(".ods") || referenceInLowerCase.endsWith(".ots") 
	        		|| referenceInLowerCase.endsWith(".csv")
	        		|| referenceInLowerCase.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
	            type = Type.XLSX;
	        } else if (referenceInLowerCase.endsWith(".ppt") 
	        		|| referenceInLowerCase.equals("application/vnd.ms-powerpoint")) {
	            type = Type.PPT;
	        } else if (referenceInLowerCase.endsWith(".pptx") 
	        		|| referenceInLowerCase.endsWith(".pot") || referenceInLowerCase.endsWith(".potx") 
	        		|| referenceInLowerCase.endsWith(".odp") || referenceInLowerCase.endsWith(".otp") 
	        		|| referenceInLowerCase.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")) {
	            type = Type.PPTX;
	        } else if (referenceInLowerCase.endsWith(".vsd") 
	        		|| referenceInLowerCase.equals("application/vnd.visio")) {
	            type = Type.VSD;
	        } else if (referenceInLowerCase.endsWith(".vsdx") 
	        		|| referenceInLowerCase.endsWith(".vstx") || referenceInLowerCase.endsWith(".vst") 
	        		|| referenceInLowerCase.endsWith(".odg") || referenceInLowerCase.endsWith(".otg") 
	        		|| referenceInLowerCase.equals("application/vnd.visio2013")) {
	            type = Type.VSDX;
	        }
        }
        else
        {
        	  if (referenceInLowerCase.endsWith(".doc")  || referenceInLowerCase.endsWith(".docx") 
		        		|| isABBYYExtension(abbyyextension, referenceInLowerCase)
		        		|| referenceInLowerCase.endsWith(".dot") || referenceInLowerCase.endsWith(".dotx") 
		        		|| referenceInLowerCase.endsWith(".odt") || referenceInLowerCase.endsWith(".ott") 
		        		|| referenceInLowerCase.endsWith(".rtf")
		        		|| referenceInLowerCase.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
		            type = Type.DOCX;
		        } else if (referenceInLowerCase.endsWith(".xls") || referenceInLowerCase.endsWith(".xlsx") 
		        		|| referenceInLowerCase.endsWith(".xlt") || referenceInLowerCase.endsWith(".xltx") 
		        		|| referenceInLowerCase.endsWith(".ods") || referenceInLowerCase.endsWith(".ots") 
		        		|| referenceInLowerCase.endsWith(".csv")
		        		|| referenceInLowerCase.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
		            type = Type.XLSX;
		        } else if (referenceInLowerCase.endsWith(".ppt") || referenceInLowerCase.endsWith(".pptx") 
		        		|| referenceInLowerCase.endsWith(".pot") || referenceInLowerCase.endsWith(".potx") 
		        		|| referenceInLowerCase.endsWith(".odp") || referenceInLowerCase.endsWith(".otp") 
		        		|| referenceInLowerCase.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")) {
		            type = Type.PPTX;
		        } else if (referenceInLowerCase.endsWith(".vsd") || referenceInLowerCase.endsWith(".vsdx") 
		        		|| referenceInLowerCase.endsWith(".vstx") || referenceInLowerCase.endsWith(".vst") 
		        		|| referenceInLowerCase.endsWith(".odg") || referenceInLowerCase.endsWith(".otg") 
		        		|| referenceInLowerCase.equals("application/vnd.visio2013")) {
		            type = Type.VSDX;
		        }
        }

        if (type == null)
            throw new RuntimeException("There's no a valid reference to detect the type of the input stream.");

        return type;
    }
	private boolean isABBYYExtension(String abbyyextension,String referenceInLowerCase)
	{
		boolean bReturn = false;
		List<String> abbyyExtToXliffList = Arrays.asList(abbyyextension.split(","));
		for (int i = 0; i < abbyyExtToXliffList.size(); i++) {
			if (referenceInLowerCase.endsWith("." + abbyyExtToXliffList.get(i)))
			{
				bReturn = true;
				break;
			}
		}
		return bReturn;
	}
	public String RunLinuxCommand(String sCmd) throws Exception {
		StringBuffer sb = new StringBuffer();
        InputStreamReader sbReader = null;
        BufferedReader buffReader = null;
        
        try {
	        Process proc = Runtime.getRuntime().exec(sCmd);
	        sbReader = new InputStreamReader(proc.getInputStream());
	        buffReader = new BufferedReader(sbReader);
	        
	        String line;
	        while ((line = buffReader.readLine()) != null) {
	            sb.append(line).append("\n");
	        }
	        
	        try {
	        	proc.waitFor();
	        } catch (InterruptedException ex) {
	        }
	        
        } catch (Exception e) {
        	throw e;
        }
        return sb.toString();
	}
	private String loadAsposeLicense() {
		ReadProp rp = new ReadProp();
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
	
	private void setAsposeLicense() throws Exception {
		com.aspose.words.License licW = new com.aspose.words.License();
		licW.setLicense(IOUtils.toInputStream(loadAsposeLicense()));
		/*
		com.aspose.cells.License licC = new com.aspose.cells.License();
		licC.setLicense(IOUtils.toInputStream(loadAsposeLicense()));
		
		com.aspose.slides.License licS = new com.aspose.slides.License();
		licS.setLicense(IOUtils.toInputStream(loadAsposeLicense()));
		*/
	}
	
	public String ResetApplication(javax.servlet.ServletContext application_) throws Exception {
		String sOutput = "";
		try {
			if (application_ == null)
				return "";

			Enumeration<?> e = application_.getAttributeNames();
			while (e.hasMoreElements()) {
				String sKeyName = (String) e.nextElement();

				if (!sKeyName.contains("apache") && !sKeyName.contains("javax")) {
					if (application_.getAttribute(sKeyName) != null) {
						application_.removeAttribute(sKeyName);
					}
				}
			}			
			sOutput = "Reset Application Success";			

		} catch (Exception ex) {
			sOutput = ex.getMessage();
		}
		return sOutput;
	}
	
	public String executeShell(String command, Integer maxRetryError, Integer sleepInterval, Log4J oLog) throws Exception {
		StringBuilder sbOut = new StringBuilder();
		try {
			boolean bStopCheck = false;
	        int iRetryError = 1;
	        int iMaxRetryExecute = maxRetryError;
	        int iTimeWaitExecute = sleepInterval;
	        while (!bStopCheck)
	        {
	            bStopCheck = true;
	            ///--------------------------------------------------------------
	            Process proc = null;
	            proc = Runtime.getRuntime().exec(command);
				int iDone = proc.waitFor();
				
				// Read any errors from the attempted command
				java.io.BufferedReader stdError = new java.io.BufferedReader(new java.io.InputStreamReader(proc.getErrorStream()));
				StringBuilder sbError = new StringBuilder();
				String sProcessError = null;
				while ((sProcessError = stdError.readLine()) != null) {
					sbError.append(sProcessError + '\n');
				}

				if (sbError.length() == 0) {
					//read output stream
					java.io.BufferedReader stdOut = new java.io.BufferedReader(new java.io.InputStreamReader(proc.getInputStream()));
					String sProcessOut = null;
					while ((sProcessOut = stdOut.readLine()) != null) {
						sbOut.append(sProcessOut + '\n');
					}
				}

				if (iDone == 0)
					proc.destroy();

	            //write log finish
	            if (sbError.length() > 0) {
	            	//throw new Exception(sbError.toString().trim());
	                iRetryError++;
	                if (iRetryError > iMaxRetryExecute) {
	                    bStopCheck = true;
	                    throw new Exception("Execute fail: " + sbError.toString() );                
	                 } else {
	         			oLog.WriteDebugLogs("Common.java", "Execute fail: " + sbError.toString() + " waiting retry=" + iRetryError, "", true);
	                }
	                
	                if (!bStopCheck)
	                    Thread.sleep(iTimeWaitExecute);
	            }
	            ///--------------------------------------------------------------
	           

	        }
		} catch (Exception e) {
			throw e;
		}
		return sbOut.toString().trim();
	}
	
	public Double getFileSize(File file, String unit) {
		if (unit.equalsIgnoreCase("mb"))
			return (double) file.length() / (1024 * 1024);
		else if (unit.equalsIgnoreCase("kb"))
			return (double) file.length() / 1024;
		else if (unit.equalsIgnoreCase("bytes"))
			return (double) file.length();
		else
			return 0.0;
	}
	
	public Double calculateFileSize(long fileSize, String unit) {
		if (unit.equalsIgnoreCase("mb"))
			return (double) fileSize / (1024 * 1024);
		else if (unit.equalsIgnoreCase("kb"))
			return (double) fileSize / 1024;
		else if (unit.equalsIgnoreCase("bytes"))
			return (double) fileSize;
		else
			return 0.0;
	}
	
	public String convertObjectToString(Object object) {
		String result = "";
		try {
			if (object != null) {
				JSONObject json = new JSONObject(object);
				result = json.toString();
			}
		}catch(Exception e) {
			result = "";
		}
		return result;
	}
	
}
