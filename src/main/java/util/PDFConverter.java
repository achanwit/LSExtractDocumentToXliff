package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

import com.abbyy.FREngine.Engine;
import com.abbyy.FREngine.FileExportFormatEnum;
import com.abbyy.FREngine.IDocumentProcessingParams;
import com.abbyy.FREngine.IEngine;
import com.abbyy.FREngine.IFRDocument;
import com.abbyy.FREngine.IObjectsExtractionParams;
import com.abbyy.FREngine.IPageAnalysisParams;
import com.abbyy.FREngine.IPageProcessingParams;
import com.abbyy.FREngine.IRecognizerParams;

public class PDFConverter {
	String pageName = "PDFConverter";
	private IEngine engine = null;
	Log4J oLog = null;

	public PDFConverter() {

	}

	public void Run(String input, String output, String productid, String sourceLang, String targetLang, Boolean isSharedCPUCoresMode, Log4J oLog4J)
			throws Exception {
		try {
			if (oLog == null)
				oLog = oLog4J;
			// Load ABBYY FineReader Engine
			loadEngine(productid, isSharedCPUCoresMode);

			// Process with ABBYY FineReader Engine
			processWithEngine(input, output, sourceLang, targetLang);

		} catch (Exception ex) {
			//ex.printStackTrace();
			displayMessage("Run:Error=" + ex.getMessage());
			throw ex;
		} finally {
			// Unload ABBYY FineReader Engine
			unloadEngine();
		}
	}

	private void loadEngine(String productid, Boolean isSharedCPUCoresMode) throws Exception {

		displayMessage("Initialize engine...");
		String GetDllFolder = GetDllFolder();
		String GetLicensePath = GetLicensePath();
		String GetLicensePassword = GetLicensePassword();
		
		//displayMessage("productid=" + productid + " GetDllFolder=" + GetDllFolder + " GetLicensePath=" + GetLicensePath + " GetLicensePassword=" + GetLicensePassword);
		try {
			engine = Engine.InitializeEngine(GetDllFolder, productid, GetLicensePath, GetLicensePassword, "", "", isSharedCPUCoresMode);
		} catch (Exception e) {
			oLog.WriteLog(pageName, "loadEngine", "Cannot Initialize Engine. Error=" + oLog.getStackTrace(e), "", true);
			throw e;
		}
		//displayMessage("InitializeEngine: Done");

		displayMessage("Loading predefined profile...");
		engine.LoadPredefinedProfile("DocumentConversion_Accuracy");

	}

	private void unloadEngine() throws Exception {
		try {
			displayMessage("Deinitializing Engine...");
			if (engine != null) {
	            // Unload ABBYY FineReader Engine
				engine = null;
				/*
				System.gc();
	            System.runFinalization();
				Engine.Unload();
				*/
				Engine.DeinitializeEngine();
			}
		} catch (Exception e) {
			System.out.println("unloadEngine:error=" + e.toString());
		}
	}

	private void processWithEngine(String input, String output, String sourceLang, String targetLang) throws Exception {

		// Create document
		displayMessage("Create document...");
		IFRDocument document = engine.CreateFRDocument();

		try {
			// set language paramter
			IDocumentProcessingParams documentparams = engine.CreateDocumentProcessingParams();
			IPageProcessingParams pageprocessingparams = engine.CreatePageProcessingParams();
			IRecognizerParams recognizegparams = engine.CreateRecognizerParams();

			IPageAnalysisParams pageAnalysisParams = documentparams.getPageProcessingParams().getPageAnalysisParams();
			pageAnalysisParams.setDetectPictures(true);
			
			//tabParams.setDetectTables(false); 
			//tabParams.setEnableTextExtractionMode(true); 
			//pageAnalysisParams.setAggressiveTableDetection(true);
			//tabParams.DetectTables=true;
			
			//IRecognizerParams recognizerParams = pageprocessingparams.getRecognizerParams(); 
			//recognizerParams.setLowResolutionMode(true);
			//set speed recognize
			recognizegparams.setBalancedMode(true);
			
			IObjectsExtractionParams objparams = pageprocessingparams.getObjectsExtractionParams(); 
			objparams.setDetectTextOnPictures(true);
			//objparams.setRemoveGarbage(true); 
			//objparams.setEnableAggressiveTextExtraction(true); 

			Hashtable<String, String> hashLanguages = getLanguages();
			String langauges = hashLanguages.get("EN");

			if (sourceLang.length() == 0 && targetLang.length() == 0) {
				for (String key : hashLanguages.keySet()) {
					if (!key.equals("EN"))
						langauges += "," + hashLanguages.get(key);
				}
			} else {
				String[] arLanguage = sourceLang.split(",");
				for (int i = 0; i < arLanguage.length; i++) {
					if (arLanguage[i].length() > 0
							&& hashLanguages.containsKey(arLanguage[i].substring(0, 2).toUpperCase()))
						if (!arLanguage[i].substring(0, 2).toUpperCase().equals("EN"))
							langauges += "," + hashLanguages.get(arLanguage[i].substring(0, 2).toUpperCase());
				}

				if (targetLang.length() > 0 && hashLanguages.containsKey(targetLang.substring(0, 2).toUpperCase()))
					if (!targetLang.substring(0, 2).toUpperCase().equals("EN"))
						langauges += "," + hashLanguages.get(targetLang.substring(0, 2).toUpperCase());
			}
			displayMessage("sourceLang ..." + sourceLang);
			displayMessage("targetLang ..." + targetLang);
			displayMessage("Recognize Languages ..." + langauges);
			recognizegparams.SetPredefinedTextLanguage(langauges);
			pageprocessingparams.setRecognizerParams(recognizegparams);
			documentparams.setPageProcessingParams(pageprocessingparams);

			// Process document
			displayMessage("Process...");
			document.AddImageFile(input, null, null);
			document.Process(documentparams);

			// Save results
			displayMessage("Saving results...");
			// Save results to docx with default parameters
			document.Export(output, FileExportFormatEnum.FEF_DOCX, null);

			// Close document
		    document.Close();
		    
		} catch (Exception ex) {
			throw ex;
		} finally {
			// Close document
		    if (document != null) {
				document.Close();
		    }
		}
	}

	private Hashtable<String, String> getLanguages() {
		// https://ocrsdk.com/documentation/specifications/recognition-languages/
		Hashtable<String, String> hashLangauge = new Hashtable<String, String>();
		hashLangauge.put("SQ", "Albanian");
		hashLangauge.put("AR", "Arabic");
		hashLangauge.put("EU", "Basque");
		hashLangauge.put("BE", "Belarusian");
		hashLangauge.put("BG", "Bulgarian");
		hashLangauge.put("CA", "Catalan");
		hashLangauge.put("ZH", "ChinesePRC,ChineseTaiwan");
		hashLangauge.put("HR", "Croatian");
		hashLangauge.put("CS", "Czech");
		hashLangauge.put("DA", "Danish");
		hashLangauge.put("NL", "Dutch,DutchBelgian");
		hashLangauge.put("EN", "English");
		hashLangauge.put("ET", "Estonian");
		hashLangauge.put("FI", "Finnish");
		hashLangauge.put("FR", "French");
		hashLangauge.put("DE", "German,GermanLuxembourg,GermanNewSpelling");
		hashLangauge.put("EL", "Greek");
		hashLangauge.put("HA", "Hausa");
		hashLangauge.put("HU", "Hungarian");
		hashLangauge.put("IS", "Icelandic");
		hashLangauge.put("ID", "Indonesian");
		hashLangauge.put("GA", "Irish");
		hashLangauge.put("IT", "Italian");
		hashLangauge.put("JA", "Japanese");
		hashLangauge.put("KO", "Korean,KoreanHangul");
		hashLangauge.put("LV", "Latvian");
		hashLangauge.put("LT", "Lithuanian");
		hashLangauge.put("MK", "Macedonian");
		hashLangauge.put("MS", "Malay");
		hashLangauge.put("MT", "Maltese");
		hashLangauge.put("MY", "Burmese");
		hashLangauge.put("NO", "Norwegian,NorwegianBokmal,NorwegianNynorsk");
		hashLangauge.put("PL", "Polish");
		hashLangauge.put("PT", "PortugueseBrazilian,PortugueseStandard");
		hashLangauge.put("RO", "Romanian,RomanianMoldavia");
		hashLangauge.put("RU", "Russian,RussianOldSpelling");
		hashLangauge.put("SR", "SerbianCyrillic,SerbianLatin");
		hashLangauge.put("SK", "Slovak");
		hashLangauge.put("SL", "Slovenian");
		hashLangauge.put("SO", "Somali");
		hashLangauge.put("ES", "Spanish");
		hashLangauge.put("SV", "Swedish");
		hashLangauge.put("TL", "Tagalog");
		hashLangauge.put("TH", "Thai");
		hashLangauge.put("TR", "Turkish");
		hashLangauge.put("TM", "Turkmen");
		hashLangauge.put("UK", "Ukrainian");
		hashLangauge.put("UR", "Arabic");
		hashLangauge.put("VI", "Vietnamese");
		return hashLangauge;
	}

	private static void displayMessage(String message) {

		Calendar oCal = Calendar.getInstance();
		SimpleDateFormat oDateTimeFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.S");
		System.out.println(oDateTimeFormat.format(oCal.getTime()) + " " + message);
	}

	// Folder with FRE dll
	private String GetDllFolder() {
		return "/opt/ABBYY/FREngine12/Bin";
	}

	// Return path to the license
	private String GetLicensePath() {
		return "";
	}

	// Return password to the license
	private String GetLicensePassword() {
		return "";
	}
}
