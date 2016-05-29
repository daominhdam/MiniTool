/**
 * 
 */
package com.mh.runner;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.RestAssuredConfig.newConfig;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.ConnectionConfig;
import com.jayway.restassured.config.HttpClientConfig;
import com.jayway.restassured.config.RedirectConfig;
import com.jayway.restassured.config.SSLConfig;
import com.jayway.restassured.response.Response;
import com.mh.driver.DriverFactoryHtmlUnit;
import com.mh.utils.Constant;
import com.mh.utils.Utils;

public class TestRunnerResponseCode {

	private static Object syncRoot = new Object();

	public ArrayList<HashMap<String, String>> urlData = null;

	private static volatile Map<String, String> finishedCheck;
	private static volatile HashMap<String, HashMap<String, String>> result;

	@BeforeSuite
	public void setUp() throws Exception {

		urlData = Constant.TEST_DATA;
		Constant.TOTAL_ROW_RUN = urlData.size();
		int rowRunPerThread = Constant.TOTAL_ROW_RUN / 4;
		Constant.RUN_THREAD_1 = rowRunPerThread;
		Constant.RUN_THREAD_2 = rowRunPerThread * 2;
		Constant.RUN_THREAD_3 = rowRunPerThread * 3;
		Constant.RUN_THREAD_4 = rowRunPerThread * 4;
		Utils.getInstance().createCssColorReport();
		finishedCheck = new HashMap<String, String>();
		RestAssured.config = newConfig()
				.connectionConfig(new ConnectionConfig().closeIdleConnectionsAfterEachResponse()).and()
				.httpClient(HttpClientConfig.httpClientConfig().setParam("http.socket.timeout", new Integer(60000)))
				.and()
				.httpClient(HttpClientConfig.httpClientConfig().setParam("http.connection.timeout", new Integer(60000)))
				.and()
				.redirect(RedirectConfig.redirectConfig().followRedirects(true).and().maxRedirects(50).and()
						.allowCircularRedirects(true).and().rejectRelativeRedirect(false))
				.and().sslConfig(new SSLConfig().allowAllHostnames());
	}

	@AfterSuite
	public void clearEnviroment() {

		finishedCheck.clear();
		Utils.getInstance().killPhantomjsProcess();
	}

	public static List<String> getLinkChecker(HtmlUnitDriver driver) {

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
		return filterUrl(new ArrayList<String>(uniqueValues));
		/*
		 * List<String> resultURL = new ArrayList<String>(); List<DomElement>
		 * elementList = new ArrayList<DomElement>(); elementList =
		 * page.getElementsByTagName("img"); String notLink = "javascript"; for
		 * (DomElement elementTemp : elementList) { String temp =
		 * elementTemp.getAttribute("src"); if ((temp != null && !
		 * temp.contains(notLink))) { try {
		 * resultURL.add(page.getFullyQualifiedUrl(temp).toString()); } catch
		 * (MalformedURLException e) { Reporter.log("Cannot convert " + temp +
		 * " to URI for run linkchecker"); } } } elementList =
		 * (page.getElementsByTagName("a")); for (DomElement elementTemp :
		 * elementList) { String temp = elementTemp.getAttribute("href"); if
		 * ((temp != null && ! temp.contains(notLink))) { try {
		 * resultURL.add(page.getFullyQualifiedUrl(temp).toString()); } catch
		 * (MalformedURLException e) { Reporter.log("Cannot convert " + temp +
		 * " to URI for run linkchecker"); } } } HashSet<String> uniqueValues =
		 * new HashSet<>(resultURL);		 
		return filterUrl(new ArrayList<String>(uniqueValues));
		*/
	}

	public static class isLinkBroken implements Runnable {

		private final String url;
		private static Boolean result = true;

		isLinkBroken(String url) {
			this.url = url;
		}

		public static Boolean getResult() {
			return result;
		}

		/**
		 * This created runnable for test executed
		 */
		@Override
		public void run() {

			Map<String, String> finishedCheckTemp = new HashMap<String, String>();
			try {
				Response response = com.jayway.restassured.RestAssured.given().get(url).then().extract().response();
				if (response.getStatusCode() == 200) {
					Reporter.log("URL: " + url + " -- " + response.getStatusCode());
					finishedCheckTemp.put(url, Integer.toString(response.getStatusCode()));
				}
				/**
				 * Verify again if this link correct in error 404, restAssured
				 * API has error with link redirect in many request
				 */
				else {
					try {
						URL siteURL = new URL(url);
						HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
						connection.setUseCaches(false);
						Utils.getInstance().trustConnectionHttps();
						HttpURLConnection.setFollowRedirects(true);
						connection.setRequestMethod("GET");
						connection.setConnectTimeout(10000);
						connection.addRequestProperty("Connection", "Close");
						connection.connect();
						int responseCode = connection.getResponseCode();
						if (responseCode == 200) {
							Reporter.log("URL: " + url + " -- " + responseCode);
							// finishedCheck.add(new HashMap<String,
							// String>(){{put(url,Integer.toString(responseCode));}});
							finishedCheckTemp.put(url, Integer.toString(responseCode));
						} else {
							Reporter.log("URL: " + url + " -- " + response.getStatusCode());
							// finishedCheck.add(new HashMap<String,
							// String>(){{put(url,Integer.toString(response.getStatusCode()));}});
							finishedCheckTemp.put(url, Integer.toString(response.getStatusCode()));
							result = false;
						}
					} catch (Exception e) {
						Reporter.log("URL: " + url + " -- " + e.getMessage());
						// finishedCheck.add(new HashMap<String,
						// String>(){{put(url,e.getMessage());}});
						finishedCheckTemp.put(url, e.getMessage());
						e.printStackTrace();
						result = false;
					}
				}
			} catch (Exception e) {
				Reporter.log("URL error: " + url + " -- " + e.getMessage());
				finishedCheckTemp.put(url, e.getMessage());
				result = false;
			}
			synchronized (syncRoot) {
				finishedCheck.putAll(finishedCheckTemp);
			}
		}
	}

	@Test(dataProvider = "Page Run Thread 1")
	public void Thread1(String urlvalue) throws Exception {

		List<String> allLink = new ArrayList<String>();
		String urlTemp = urlvalue;
		WebClient client = new WebClient();
		try {
			HtmlUnitDriver driver = DriverFactoryHtmlUnit.getInstance().getDriver();
			driver.manage().timeouts().pageLoadTimeout(Constant.TIME_OUT, TimeUnit.SECONDS);
			driver.get(urlTemp);
			/*
			 * client.getOptions().setTimeout(Constant.TIME_OUT * 1000);
			 * client.getCache().setMaxSize(0);
			 * client.getOptions().setUseInsecureSSL(true); // ignore ssl //
			 * certificate
			 * client.getOptions().setThrowExceptionOnScriptError(false);
			 * client.getOptions().setThrowExceptionOnFailingStatusCode(false);
			 * client.getOptions().setPrintContentOnFailingStatusCode(false);
			 * client.getOptions().setJavaScriptEnabled(false);
			 * client.getOptions().setCssEnabled(false);
			 * client.setCssErrorHandler(new SilentCssErrorHandler()); HtmlPage
			 * page = client.getPage(urlTemp);
			 */
			allLink = filterUrl(getLinkChecker(driver));
			/**
			 * Check if driver cannot find any link, system will reload page
			 * again and find link again and also check this page get error 404
			 * or not
			 */
			if (allLink.size() == 0 && !finishedCheck.containsKey(urlTemp)) {
				try {
					int responseChek = given().get(urlvalue).then().extract().response().getStatusCode();
					if (responseChek == 200) {
						client.getPage(urlTemp);
						allLink = filterUrl(getLinkChecker(driver));
						if (allLink.size() == 0 && finishedCheck.containsKey(urlTemp))
							Reporter.log("WARNING: Already check for this URL ");
						else
							Reporter.log("WARNING: can not get any URL links in this page ");
					} else {
						Reporter.log("URL: " + urlvalue + " -- " + responseChek);
					}
				} catch (Exception e) {
					Reporter.log("Url error: " + e.getMessage());
				}
			}
			/**
			 * Exit driver after finish find all link on page
			 */
			// DriverFactoryHtmlUnit.getInstance().removeDriver();
			client.close();
		} catch (Exception e) {
			// DriverFactoryHtmlUnit.getInstance().removeDriver();
			client.close();
			Reporter.log(e.getMessage());
			Reporter.log("CONNECTION GET TIME OUT WHEN OPEN " + urlTemp + " TO GET ALL LINK");
		}
		/**
		 * Run link checker if found >1 link on page
		 */
		if (allLink.size() > 0) {
			Reporter.log("Total link check : " + allLink.size());
			if (allLink.size() == 1)
				Reporter.log("WARNING: THIS URL VALUE COULD BE GET ERROR 404: " + urlvalue);
			ExecutorService executor = Executors.newFixedThreadPool(Constant.NUMBER_THREAD);

			for (String linkCheck : allLink) {
				executor.execute(new isLinkBroken(linkCheck));
				isLinkBroken.getResult();
			}
			executor.shutdown();
			// Wait until all threads are finish
			try {
				// Wait a while for existing tasks to terminate
				if (!executor.awaitTermination(((allLink.size() / 2) + 5), TimeUnit.SECONDS)) {
					Reporter.log("WARNING: GET ERROR WHEN CLOSE CONNECTION ");
					executor.shutdownNow();
					if (!executor.awaitTermination(15, TimeUnit.SECONDS)) {
						executor.shutdownNow();
					}
				}
			} catch (InterruptedException ie) {
				System.err.println("try to shutown");
				executor.shutdownNow();
				System.err.println("try to interrupt");
				Thread.currentThread().interrupt();
			}
		}
	}

	@Test(dataProvider = "Page Run Thread 2")
	public void Thread2(String urlvalue) throws Exception {

		List<String> allLink = new ArrayList<String>();
		String urlTemp = urlvalue;
		WebClient client = new WebClient();
		try {
			HtmlUnitDriver driver = DriverFactoryHtmlUnit.getInstance().getDriver();
			driver.manage().timeouts().pageLoadTimeout(Constant.TIME_OUT, TimeUnit.SECONDS);
			driver.get(urlTemp);
			/*
			 * client.getOptions().setTimeout(Constant.TIME_OUT * 1000);
			 * client.getCache().setMaxSize(0);
			 * client.getOptions().setUseInsecureSSL(true); // ignore ssl //
			 * certificate
			 * client.getOptions().setThrowExceptionOnScriptError(false);
			 * client.getOptions().setThrowExceptionOnFailingStatusCode(false);
			 * client.getOptions().setPrintContentOnFailingStatusCode(false);
			 * client.getOptions().setJavaScriptEnabled(false);
			 * client.getOptions().setCssEnabled(false);
			 * client.setCssErrorHandler(new SilentCssErrorHandler()); HtmlPage
			 * page = client.getPage(urlTemp);
			 */
			allLink = filterUrl(getLinkChecker(driver));
			/**
			 * Check if driver cannot find any link, system will reload page
			 * again and find link again and also check this page get error 404
			 * or not
			 */
			if (allLink.size() == 0 && !finishedCheck.containsKey(urlTemp)) {
				try {
					int responseChek = given().get(urlvalue).then().extract().response().getStatusCode();
					if (responseChek == 200) {
						client.getPage(urlTemp);
						allLink = filterUrl(getLinkChecker(driver));
						if (allLink.size() == 0 && finishedCheck.containsKey(urlTemp))
							Reporter.log("WARNING: Already check for this URL ");
						else
							Reporter.log("WARNING: can not get any URL links in this page ");
					} else {
						Reporter.log("URL: " + urlvalue + " -- " + responseChek);
					}
				} catch (Exception e) {
					Reporter.log("Url error: " + e.getMessage());
				}
			}
			/**
			 * Exit driver after finish find all link on page
			 */
			// DriverFactoryHtmlUnit.getInstance().removeDriver();
			client.close();
		} catch (Exception e) {
			// DriverFactoryHtmlUnit.getInstance().removeDriver();
			client.close();
			Reporter.log(e.getMessage());
			Reporter.log("CONNECTION GET TIME OUT WHEN OPEN " + urlTemp + " TO GET ALL LINK");
		}
		/**
		 * Run link checker if found >1 link on page
		 */
		if (allLink.size() > 0) {
			Reporter.log("Total link check : " + allLink.size());
			if (allLink.size() == 1)
				Reporter.log("WARNING: THIS URL VALUE COULD BE GET ERROR 404: " + urlvalue);
			ExecutorService executor = Executors.newFixedThreadPool(Constant.NUMBER_THREAD);
			for (String linkCheck : allLink) {
				executor.execute(new isLinkBroken(linkCheck));
			}
			executor.shutdown();
			// Wait until all threads are finish
			try {
				// Wait a while for existing tasks to terminate
				if (!executor.awaitTermination(((allLink.size() / 2) + 5), TimeUnit.SECONDS)) {
					Reporter.log("WARNING: GET ERROR WHEN CLOSE CONNECTION ");
					executor.shutdownNow();
					if (!executor.awaitTermination(15, TimeUnit.SECONDS)) {
						executor.shutdownNow();
					}
				}
			} catch (InterruptedException ie) {
				System.err.println("try to shutown");
				executor.shutdownNow();
				System.err.println("try to interrupt");
				Thread.currentThread().interrupt();
			}
		}
	}

	@Test(dataProvider = "Page Run Thread 3")
	public void Thread3(String urlvalue) throws Exception {

		List<String> allLink = new ArrayList<String>();
		String urlTemp = urlvalue;
		WebClient client = new WebClient();
		try {

			HtmlUnitDriver driver = DriverFactoryHtmlUnit.getInstance().getDriver();
			driver.manage().timeouts().pageLoadTimeout(Constant.TIME_OUT, TimeUnit.SECONDS);
			driver.get(urlTemp);
			/*
			 * client.getOptions().setTimeout(Constant.TIME_OUT * 1000);
			 * client.getCache().setMaxSize(0);
			 * client.getOptions().setUseInsecureSSL(true); // ignore ssl //
			 * certificate
			 * client.getOptions().setThrowExceptionOnScriptError(false);
			 * client.getOptions().setThrowExceptionOnFailingStatusCode(false);
			 * client.getOptions().setPrintContentOnFailingStatusCode(false);
			 * client.getOptions().setJavaScriptEnabled(false);
			 * client.getOptions().setCssEnabled(false);
			 * client.setCssErrorHandler(new SilentCssErrorHandler()); HtmlPage
			 * page = client.getPage(urlTemp);
			 */
			allLink = filterUrl(getLinkChecker(driver));
			/**
			 * Check if driver cannot find any link, system will reload page
			 * again and find link again and also check this page get error 404
			 * or not
			 */
			if (allLink.size() == 0 && !finishedCheck.containsKey(urlTemp)) {
				try {
					int responseChek = given().get(urlvalue).then().extract().response().getStatusCode();
					if (responseChek == 200) {
						client.getPage(urlTemp);
						allLink = filterUrl(getLinkChecker(driver));
						if (allLink.size() == 0 && finishedCheck.containsKey(urlTemp))
							Reporter.log("WARNING: Already check for this URL ");
						else
							Reporter.log("WARNING: can not get any URL links in this page ");
					} else {
						Reporter.log("URL: " + urlvalue + " -- " + responseChek);
					}
				} catch (Exception e) {
					Reporter.log("Url error: " + e.getMessage());
				}
			}
			/**
			 * Exit driver after finish find all link on page
			 */
			// DriverFactoryHtmlUnit.getInstance().removeDriver();
			client.close();
		} catch (Exception e) {
			// DriverFactoryHtmlUnit.getInstance().removeDriver();
			client.close();
			Reporter.log(e.getMessage());
			Reporter.log("CONNECTION GET TIME OUT WHEN OPEN " + urlTemp + " TO GET ALL LINK");
		}
		/**
		 * Run link checker if found >1 link on page
		 */
		if (allLink.size() > 0) {
			Reporter.log("Total link check : " + allLink.size());
			if (allLink.size() == 1)
				Reporter.log("WARNING: THIS URL VALUE COULD BE GET ERROR 404: " + urlvalue);
			ExecutorService executor = Executors.newFixedThreadPool(Constant.NUMBER_THREAD);
			for (String linkCheck : allLink) {
				executor.execute(new isLinkBroken(linkCheck));
			}
			executor.shutdown();
			// Wait until all threads are finish
			try {
				// Wait a while for existing tasks to terminate
				if (!executor.awaitTermination(((allLink.size() / 2) + 5), TimeUnit.SECONDS)) {
					Reporter.log("WARNING: GET ERROR WHEN CLOSE CONNECTION ");
					executor.shutdownNow();
					if (!executor.awaitTermination(15, TimeUnit.SECONDS)) {
						executor.shutdownNow();
					}
				}
			} catch (InterruptedException ie) {
				System.err.println("try to shutown");
				executor.shutdownNow();
				System.err.println("try to interrupt");
				Thread.currentThread().interrupt();
			}
		}
	}

	@Test(dataProvider = "Page Run Thread 4")
	public void Thread4(String urlvalue) throws Exception {

		List<String> allLink = new ArrayList<String>();
		String urlTemp = urlvalue;
		WebClient client = new WebClient();
		try {
			HtmlUnitDriver driver = DriverFactoryHtmlUnit.getInstance().getDriver();
			driver.manage().timeouts().pageLoadTimeout(Constant.TIME_OUT, TimeUnit.SECONDS);
			driver.get(urlTemp);
			/*
			 * client.getOptions().setTimeout(Constant.TIME_OUT * 1000);
			 * client.getCache().setMaxSize(0);
			 * client.getOptions().setUseInsecureSSL(true); // ignore ssl //
			 * certificate
			 * client.getOptions().setThrowExceptionOnScriptError(false);
			 * client.getOptions().setThrowExceptionOnFailingStatusCode(false);
			 * client.getOptions().setPrintContentOnFailingStatusCode(false);
			 * client.getOptions().setJavaScriptEnabled(false);
			 * client.getOptions().setCssEnabled(false);
			 * client.setCssErrorHandler(new SilentCssErrorHandler()); HtmlPage
			 * page = client.getPage(urlTemp);
			 */
			allLink = filterUrl(getLinkChecker(driver));
			/**
			 * Check if driver cannot find any link, system will reload page
			 * again and find link again and also check this page get error 404
			 * or not
			 */
			if (allLink.size() == 0 && !finishedCheck.containsKey(urlTemp)) {
				try {
					int responseChek = given().get(urlvalue).then().extract().response().getStatusCode();
					if (responseChek == 200) {
						client.getPage(urlTemp);
						allLink = filterUrl(getLinkChecker(driver));
						if (allLink.size() == 0 && !finishedCheck.containsKey(urlTemp))
							Reporter.log("WARNING: Already check for this URL ");
						else
							Reporter.log("WARNING: can not get any URL links in this page ");
					} else {
						Reporter.log("URL: " + urlvalue + " -- " + responseChek);
					}
				} catch (Exception e) {
					Reporter.log("Url error: " + e.getMessage());
				}
			}
			/**
			 * Exit driver after finish find all link on page
			 */
			// DriverFactoryHtmlUnit.getInstance().removeDriver();
			client.close();
		} catch (Exception e) {
			// DriverFactoryHtmlUnit.getInstance().removeDriver();
			client.close();
			Reporter.log(e.getMessage());
			Reporter.log("CONNECTION GET TIME OUT WHEN OPEN " + urlTemp + " TO GET ALL LINK");
		}
		/**
		 * Run link checker if found >1 link on page
		 */
		if (allLink.size() > 0) {
			Reporter.log("Total link check : " + allLink.size());
			if (allLink.size() == 1)
				Reporter.log("WARNING: THIS URL VALUE COULD BE GET ERROR 404: " + urlvalue);
			ExecutorService executor = Executors.newFixedThreadPool(Constant.NUMBER_THREAD);
			for (String linkCheck : allLink) {
				executor.execute(new isLinkBroken(linkCheck));
			}
			executor.shutdown();
			// Wait until all threads are finish
			try {
				// Wait a while for existing tasks to terminate
				if (!executor.awaitTermination(((allLink.size() / 2) + 5), TimeUnit.SECONDS)) {
					Reporter.log("WARNING: GET ERROR WHEN CLOSE CONNECTION ");
					executor.shutdownNow();
					if (!executor.awaitTermination(15, TimeUnit.SECONDS)) {
						executor.shutdownNow();
					}
				}
			} catch (InterruptedException ie) {
				System.err.println("try to shutown");
				executor.shutdownNow();
				System.err.println("try to interrupt");
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Created data for test 1
	 * 
	 * @return
	 */
	@DataProvider(name = "Page Run Thread 1")
	public Object[][] dataProviderMethod1() {

		// int row = Constant.RUN_THREAD_1;
		// Object[][] data = new Object[row][1];
		// for (int n = 0; n < Constant.RUN_THREAD_1; n++) {
		// data[n][0] = urlData.get(n).get(Constant.HEADER_DATA_NAME);
		// }
		return getData(Constant.RUN_THREAD_1, 0);
	}

	/**
	 * Created data for test 2
	 * 
	 * @return
	 */
	@DataProvider(name = "Page Run Thread 2")
	public Object[][] dataProviderMethod2() {

		// int row = Constant.RUN_THREAD_2 - Constant.RUN_THREAD_1;
		// Object[][] data = new Object[row][1];
		// for (int n = 0; n < row; n++) {
		// data[n][0] = urlData.get(n +
		// Constant.RUN_THREAD_1).get(Constant.HEADER_DATA_NAME);
		// }
		return getData(Constant.RUN_THREAD_2 - Constant.RUN_THREAD_1, Constant.RUN_THREAD_1);
	}

	/**
	 * Created data for test 3
	 * 
	 * @return
	 */
	@DataProvider(name = "Page Run Thread 3")
	public Object[][] dataProviderMethod3() {

		// int row = Constant.RUN_THREAD_3 - Constant.RUN_THREAD_2;
		// Object[][] data = new Object[row][1];
		// for (int n = 0; n < row; n++) {
		// data[n][0] = urlData.get(n +
		// Constant.RUN_THREAD_2).get(Constant.HEADER_DATA_NAME);
		// }
		return getData(Constant.RUN_THREAD_3 - Constant.RUN_THREAD_2, Constant.RUN_THREAD_2);
	}

	/**
	 * Created data for test 4
	 * 
	 * @return
	 */
	@DataProvider(name = "Page Run Thread 4")
	public Object[][] dataProviderMethod4() {

		// int row = Constant.TOTAL_ROW_RUN - Constant.RUN_THREAD_3;
		// Object[][] data = new Object[row][1];
		// for (int n = 0; n < row; n++) {
		// data[n][0] = urlData.get(n +
		// Constant.RUN_THREAD_3).get(Constant.HEADER_DATA_NAME);
		// }
		return getData(Constant.TOTAL_ROW_RUN - Constant.RUN_THREAD_3, Constant.RUN_THREAD_3);
	}

	/**
	 * This method is use for return Test Data for data provider
	 * 
	 * @param row
	 * @param startRow
	 * @return
	 */
	private Object[][] getData(int row, int startRow) {

		Object[][] dataReturn = new Object[row][1];
		for (int n = 0; n < row; n++)
			dataReturn[n][0] = urlData.get(n + startRow).get(Constant.HEADER_DATA_NAME);
		return dataReturn;
	}

	/**
	 * This method is use for filter URL has finished check
	 * 
	 * @param resultUrl
	 * @return
	 */
	private static List<String> filterUrl(List<String> resultUrl) {

		synchronized (syncRoot) {
			List<String> returnUrl = resultUrl;
			for (Map.Entry<String, String> existURL : finishedCheck.entrySet()) {
				if (returnUrl.contains(existURL.getKey()))
					returnUrl.remove(existURL.getKey());
			}
			return returnUrl;
		}
	}
}
