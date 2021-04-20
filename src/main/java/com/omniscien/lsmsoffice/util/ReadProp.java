package com.omniscien.lsmsoffice.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProp {
	
	InputStream inputStream;

	public ReadProp() {
		// TODO Auto-generated constructor stub
	}
	
public String getProp(String key)  {
		String result = "";
		
		String propFileName = "ExtractDocConfig.properties";
		try {
		Properties prop = new Properties();

//		inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		inputStream = new FileInputStream(propFileName);
		
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}

		result = prop.getProperty(key);

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
		

		
		
		return result;
		
	}

}
