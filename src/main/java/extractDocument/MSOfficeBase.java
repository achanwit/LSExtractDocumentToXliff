package extractDocument;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MSOfficeBase {
	
	private boolean debugMode = true;
	private String debugPath = "/var/www/logs/lse-logs/msoffice/";
	private String resourcesPath = "/var/www/nlp-7085/webapps/resources/msoffice/";
	private String abbyyExtension = "pdf,bmp,gif,jpg,jpeg,pcx,png,tif,tiff";
	private String fontConfigPath = "/home/data/lse-data/account/JavaScript/FontsConfig.json";
	private String log4JPath = "/var/www/lse/log4j.xml";
	private String abbyyPath = "/var/www/script/abbyy/";
	private String abbyyGetInfoFileName = "activation-getinfo.sh";
	private String abbyyWaitInterval = "200|20";
	private Boolean isSharedCPUCoresMode = false;
	private Integer timeToRemoveJobMinutes = 60; 
	
	private String configFilePath;

	public MSOfficeBase() {
		// TODO Auto-generated constructor stub
	}
	
	public void loadConfig(String configFile) throws ParserConfigurationException, SAXException, IOException {
		System.out.println("loading config..." + configFile);
		
		this.configFilePath = configFile;
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(configFile);
		
		//optional, but recommended
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("configuration");
		
		Node nNode = nList.item(0);
		
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			
			Element eElement = (Element) nNode;			
			NodeList parameters = eElement.getElementsByTagName("parameters");
			Node parametersNode = parameters.item(0);
			if (parametersNode.getNodeType() == Node.ELEMENT_NODE) {
				
				Element parametersNodeElement = (Element) parametersNode;

				NodeList debugMode = parametersNodeElement.getElementsByTagName("debugmode");
				Node debugModeNode = debugMode.item(0);
				
				if (debugModeNode.getNodeType() == Node.ELEMENT_NODE) {
					Element debugModeElement = (Element) debugModeNode;
					this.debugMode = Boolean.valueOf(debugModeElement.getAttribute("value"));
				}
				NodeList logPath = parametersNodeElement.getElementsByTagName("debugpath");
				Node logPathNode = logPath.item(0);
				if (logPathNode.getNodeType() == Node.ELEMENT_NODE) {
					Element logPathElement = (Element) logPathNode;
					this.debugPath = logPathElement.getAttribute("value");
				}
				NodeList resourcesPath = parametersNodeElement.getElementsByTagName("resourcespath");
				Node resourcesPathNode = resourcesPath.item(0);
				if (resourcesPathNode.getNodeType() == Node.ELEMENT_NODE) {
					Element resourcesPathElement = (Element) resourcesPathNode;
					this.resourcesPath = resourcesPathElement.getAttribute("value");
				}
				NodeList abbyyExtension = parametersNodeElement.getElementsByTagName("abbyyextension");
				Node abbyyExtensionNode = abbyyExtension.item(0);
				if (abbyyExtensionNode.getNodeType() == Node.ELEMENT_NODE) {
					Element abbyyExtensionElement = (Element) abbyyExtensionNode;
					this.abbyyExtension = abbyyExtensionElement.getAttribute("value");
				}
				NodeList nlFontConfigPath = parametersNodeElement.getElementsByTagName("fontconfigpath");
				Node fontConfigPathNode = nlFontConfigPath.item(0);
				if (fontConfigPathNode.getNodeType() == Node.ELEMENT_NODE) {
					Element fontConfigPathElement = (Element) fontConfigPathNode;
					this.fontConfigPath = fontConfigPathElement.getAttribute("value");
				}
				NodeList nlLog4JPath = parametersNodeElement.getElementsByTagName("log4jpath");
				Node log4JPathNode = nlLog4JPath.item(0);
				if (log4JPathNode.getNodeType() == Node.ELEMENT_NODE) {
					Element log4JPathElement = (Element) log4JPathNode;
					this.log4JPath = log4JPathElement.getAttribute("value");
				}
				NodeList nlABBYYPath = parametersNodeElement.getElementsByTagName("abbyypath");
				Node abbyyPathNode = nlABBYYPath.item(0);
				if (abbyyPathNode.getNodeType() == Node.ELEMENT_NODE) {
					Element abbyyPathElement = (Element) abbyyPathNode;
					this.abbyyPath = abbyyPathElement.getAttribute("value");
				}
				NodeList nlABBYYInfoName = parametersNodeElement.getElementsByTagName("abbyygetinfoname");
				Node abbyyInfoNode = nlABBYYInfoName.item(0);
				if (abbyyInfoNode.getNodeType() == Node.ELEMENT_NODE) {
					Element abbyyInfoElement = (Element) abbyyInfoNode;
					this.abbyyGetInfoFileName = abbyyInfoElement.getAttribute("value");
				}
				NodeList nlABBYYWaitInterval = parametersNodeElement.getElementsByTagName("abbyywaitinterval");
				Node abbyyWaitIntervalNode = nlABBYYWaitInterval.item(0);
				if (abbyyWaitIntervalNode.getNodeType() == Node.ELEMENT_NODE) {
					Element abbyyWaitIntervalElement = (Element) abbyyWaitIntervalNode;
					this.abbyyWaitInterval = abbyyWaitIntervalElement.getAttribute("value");
				}
				NodeList nlIsSharedCPUCoresMode = parametersNodeElement.getElementsByTagName("issharedcpucoresmode");
				Node nIsSharedCPUCoresMode = nlIsSharedCPUCoresMode.item(0);
				if (nIsSharedCPUCoresMode.getNodeType() == Node.ELEMENT_NODE) {
					Element eIsSharedCPUCoresMode = (Element) nIsSharedCPUCoresMode;
					this.isSharedCPUCoresMode = Boolean.valueOf(eIsSharedCPUCoresMode.getAttribute("value"));
				}
				NodeList nlTimeToRemoveJob = parametersNodeElement.getElementsByTagName("timetoremovejob");
				Node nTimeToRemoveJob = nlTimeToRemoveJob.item(0);
				if (nTimeToRemoveJob.getNodeType() == Node.ELEMENT_NODE) {
					Element eTimeToRemoveJob = (Element) nTimeToRemoveJob;
					this.timeToRemoveJobMinutes = Integer.valueOf(eTimeToRemoveJob.getAttribute("value").toString());
				}
				
				
			}
		
			
		}
	}
	public String getDebugPath() {
		return debugPath;
	}
	
	public Boolean getDebugMode() {
		return debugMode;
	}
	
	public String getLog4JPath() {
		return log4JPath;
	}
	
	public String getResourcesPath() {
		return resourcesPath;
	}
	
	public String getABBYYExtension() {
		return abbyyExtension;
	}
	
	public String getFontConfigPath() {
		return fontConfigPath;
	}
	
	public String getABBYYPath() {
		return abbyyPath;
	}
	
	public String getABBYYGetInfoFileName() {
		return abbyyGetInfoFileName;
	}
	
	public String getABBYYWaitInterval() {
		return abbyyWaitInterval;
	}
	
	public Boolean getIsSharedCPUCoresMode() {
		return isSharedCPUCoresMode;
	}	
	
	public Integer getTimeToRemoveJobMinutes() {
		return timeToRemoveJobMinutes;
	}

}
