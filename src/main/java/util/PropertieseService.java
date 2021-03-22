package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertieseService {
	
	String value = null;
	InputStream inputStream = null;
	Properties prop = new Properties();
	String propFileName = null;

	public PropertieseService(){
		try {
			prop = new Properties();
			propFileName = "extravt.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		//System.out.println("value: "+value);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String getPropertiesValue(String key) throws IOException {
		
		value = null;
		inputStream = null;
		value = prop.getProperty(key);
		return value;
		
	}

}
