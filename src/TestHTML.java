import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import static org.openqa.selenium.lift.Finders.*;
import static org.openqa.selenium.lift.Matchers.*;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NiceRefreshHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.mh.driver.DriverFactoryPhantomjs;
import com.mh.utils.Utils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.lift.HamcrestWebDriverTestCase;
public class TestHTML {
@Test
public void html() throws InterruptedException, Exception, Exception, Exception
{
	
	HtmlUnitDriver driver = new HtmlUnitDriver(){
        @Override
        protected WebClient getWebClient() {        	
           WebClient client= super.getWebClient();
	client.getOptions().setTimeout(10000);	
	client.getCache().setMaxSize(0);
	client.getOptions().setUseInsecureSSL(true); //ignore ssl certificate
    client.getOptions().setThrowExceptionOnScriptError(false);
    client.getOptions().setThrowExceptionOnFailingStatusCode(false);
    client.getOptions().setPrintContentOnFailingStatusCode(false);
    client.getOptions().setJavaScriptEnabled(false);           
    client.getOptions().setCssEnabled(true);
    client.setCssErrorHandler(new SilentCssErrorHandler());
    client.setRefreshHandler(new NiceRefreshHandler(60));
            return client;
        }
    };
    driver.manage().timeouts().pageLoadTimeout(10,TimeUnit.SECONDS);
	driver.get("http://www.24h.com.vn/");
	System.out.println(driver.getTitle());
	List<String> resultURL = new ArrayList<String>();
	List<WebElement> elementList = new ArrayList<WebElement>();
	elementList = driver.findElements(By.tagName("img"));
	String notLink = "javascript";
	for (WebElement elementTemp : elementList) {
		String temp = elementTemp.getAttribute("src");
		if ((temp != null && !temp.contains(notLink))) {
			resultURL.add(temp);
		}
	}
	elementList.addAll(driver.findElements(By.tagName("a")));
	for (WebElement elementTemp : elementList) {
		String temp = elementTemp.getAttribute("href");
		if ((temp != null && !temp.contains(notLink))) {
			resultURL.add(temp);
		}
	}
	HashSet<String> uniqueValues = new HashSet<>(resultURL);
	System.out.println(uniqueValues.size());
	System.out.println(driver.getCurrentUrl());
	driver.quit();
	for(String temp : uniqueValues)
		System.out.println(temp);
	/*
	WebClient client= new WebClient();
	client.getOptions().setTimeout(10000);	
	client.getCache().setMaxSize(0);
	client.getOptions().setUseInsecureSSL(true); //ignore ssl certificate
    client.getOptions().setThrowExceptionOnScriptError(false);
    client.getOptions().setThrowExceptionOnFailingStatusCode(false);
    client.getOptions().setPrintContentOnFailingStatusCode(false);
    client.getOptions().setJavaScriptEnabled(false);           
    client.getOptions().setCssEnabled(true);
    client.setCssErrorHandler(new SilentCssErrorHandler());                      
    String baseUrl=("http://www.24h.com.vn/");
     //webClient.getOptions().setRedirectEnabled(true);	
	HtmlPage page = client.getPage(baseUrl);
	System.out.println(page.getTitleText());
	List<String> resultURL = new ArrayList<String>();
	List<DomElement> elementList = new ArrayList<DomElement>();
	//elementList = driver.findElements(By.tagName("img"));
	elementList = page.getElementsByTagName("img");
	String notLink = "javascript";
	for (DomElement elementTemp : elementList) {
		String temp = elementTemp.getAttribute("src");
		if ((temp != null && !temp.contains(notLink))) {
			resultURL.add(page.getFullyQualifiedUrl(temp).toString());
		}
	}
	
	elementList=(page.getElementsByTagName("a"));
	
	for(DomElement abc : elementList)
	{
		String temp = abc.getAttribute("href");
		if ((temp != null && !temp.contains(notLink))) 
		resultURL.add(page.getFullyQualifiedUrl(temp).toString());		
	}		
	System.out.println(resultURL.size());
	HashSet<String> uniqueValues = new HashSet<>(resultURL);
	System.out.println(uniqueValues.size());		
	System.out.println(page.getBaseURL());
	System.out.println(page.getUrl());
	
	client.close();
	for(String link:uniqueValues)
		System.err.println(link);
	
	*/
}
}
