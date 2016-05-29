package com.mh.driver;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import com.gargoylesoftware.htmlunit.NiceRefreshHandler;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;

public class DriverFactoryHtmlUnit {
	 private DriverFactoryHtmlUnit() {
	    }
	    
	    private static DriverFactoryHtmlUnit instance = new DriverFactoryHtmlUnit();
	    
	    public static DriverFactoryHtmlUnit getInstance() {
	        return instance;
	    }
	    
	    ThreadLocal<HtmlUnitDriver> driver = new ThreadLocal<HtmlUnitDriver>() {
	        @Override
	        protected HtmlUnitDriver initialValue() {	   
	        	HtmlUnitDriver driver = new HtmlUnitDriver(){
	                @Override
	                protected WebClient getWebClient() {        	
	                    WebClient webClient = super.getWebClient();
	                    webClient.getCache().setMaxSize(0);
	                    webClient.getOptions().setUseInsecureSSL(true); //ignore ssl certificate
	                    webClient.getOptions().setThrowExceptionOnScriptError(false);
	                    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	                    webClient.getOptions().setPrintContentOnFailingStatusCode(false);
	                    webClient.getOptions().setJavaScriptEnabled(false);           
	                    webClient.getOptions().setCssEnabled(false);
	                    webClient.setCssErrorHandler(new SilentCssErrorHandler());
	                    webClient.getOptions().setRedirectEnabled(true);	
	                    webClient.setRefreshHandler(new NiceRefreshHandler(60));
	                    return webClient;
	                }
	            };
	            return driver;
	        }
	    };
	    
	    public HtmlUnitDriver getDriver() {
	        return driver.get();
	    }
	    
	    public void removeDriver() {
	        driver.get().quit();
	        driver.remove();
	    }
	    
	}