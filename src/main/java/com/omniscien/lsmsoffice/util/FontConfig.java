package com.omniscien.lsmsoffice.util;

import java.util.ArrayList;
import java.util.List;

public class FontConfig {
	List<LangPair> fontConfig = new ArrayList<LangPair>();

	public List<LangPair> getFontConfig() {
		return fontConfig;
	}

	public void setFontConfig(List<LangPair> fontConfig) {
		this.fontConfig = fontConfig;
	}
	
	class LangPair{
		String langPair;
		List<Fonts> fonts = new ArrayList<Fonts>();
		
		
		public String getLangPair() {
			return langPair;
		}
		public void setLangPair(String langPair) {
			this.langPair = langPair;
		}
		public List getFonts() {
			return fonts;
		}
		public void setFonts(List<Fonts> fonts) {
			this.fonts = fonts;
		}
		
		
	}
	
	
	class Fonts{
		String source;
		String target;
		
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		public String getTarget() {
			return target;
		}
		public void setTarget(String target) {
			this.target = target;
		}
		
		
		
	}
	
}


