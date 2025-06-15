package ImplementationBase;

import java.io.FileInputStream;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import org.openqa.selenium.devtools.v85.network.model.RequestId;
import org.openqa.selenium.devtools.v85.network.model.Response;
import org.openqa.selenium.devtools.v85.network.model.ResourceTiming;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.network.Network;
import org.openqa.selenium.devtools.v85.network.model.RequestId;
import org.openqa.selenium.devtools.v85.network.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import Design.ExcelSpecificActionsForWeb;
import Design.WebSpecificActions;
import io.appium.java_client.functions.ExpectedCondition;


public class WebImplementationBase implements ExcelSpecificActionsForWeb, WebSpecificActions {

	public RemoteWebDriver driver;
	public WebDriverWait wait;

	public static HashMap<String, String> locator;


	@Override
	public void readLocator(String locatorfilepath, String sheetname) throws IOException {
		locator = new HashMap<>();
		XSSFWorkbook workbook = new XSSFWorkbook(locatorfilepath);
		XSSFSheet sheet = workbook.getSheet(sheetname);
		int rowcount = sheet.getLastRowNum();

		for (int i = 1; i <= rowcount; i++) {
			String stringCellValue = sheet.getRow(i).getCell(2).getStringCellValue();
			String stringCellValue2 = sheet.getRow(i).getCell(3).getStringCellValue();
			locator.put(stringCellValue, stringCellValue2);
			workbook.close();
		}
	}

	@Override
	public String testdataload(String sheetname, String testscriptID, String testdataname) throws IOException {

			HashMap<String, Integer> testdataID = new HashMap<>();
			String key;
			int value;
			XSSFWorkbook workbook = new XSSFWorkbook(getConfigurations("testdatapath"));
			XSSFSheet sheet = workbook.getSheet(sheetname);
			int lastCellNum = sheet.getLastRowNum();
			for (int i = 1; i <= lastCellNum; i++) {
				XSSFCell cell = sheet.getRow(i).getCell(0);
				try {
					switch (cell.getCellType()) {
					case NUMERIC:
						double temp = cell.getNumericCellValue();
						long val = (long) temp;
						key = String.valueOf(val);
						if (DateUtil.isCellDateFormatted(cell)) {
							DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
							Date date = cell.getDateCellValue();
							key = df.format(date);
						}
						break;
					case STRING:
						key = cell.getStringCellValue();
						break;
					case BOOLEAN:
						key = String.valueOf(cell.getBooleanCellValue());
						break;
					case FORMULA:
						key = cell.getCellFormula();
					default:
						key = "DEFAULT";
					}
				} catch (NullPointerException npe) {
					key = " ";

				}

				value = i;
				testdataID.put(key, value);

			}

			HashMap<String, Integer> testdatatitle = new HashMap<>();
			int column = sheet.getRow(0).getLastCellNum();
			for (int i = 1; i < column; i++) {
				XSSFCell cell = sheet.getRow(0).getCell(i);
				try {
					switch (cell.getCellType()) {
					case NUMERIC:
						double temp = cell.getNumericCellValue();
						long val = (long) temp;
						key = String.valueOf(val);
						if (DateUtil.isCellDateFormatted(cell)) {
							DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
							Date date = cell.getDateCellValue();
							key = df.format(date);
						}
						break;
					case STRING:
						key = cell.getStringCellValue();
						break;
					case BOOLEAN:
						key = String.valueOf(cell.getBooleanCellValue());
						break;
					case FORMULA:
						key = cell.getCellFormula();
					default:
						key = "DEFAULT";
					}
				} catch (NullPointerException npe) {
					key = " ";

				}
				value = i;
				testdatatitle.put(key, value);

			}

			String expected_value = sheet.getRow(testdataID.get(testscriptID)).getCell(testdatatitle.get(testdataname))
					.getStringCellValue();
			return expected_value;
		}
	


	@Override
	public WebElement locateElement(String locator) {
		String[] split = locator.split("=", 2);
		String locatortype = split[0];
		String lvalue = split[1];
		switch (locatortype.toLowerCase()) {
		case "id":
			return driver.findElement(By.id(lvalue));
		case "xpath":
			return driver.findElement(By.xpath(lvalue));

		case "name":
			return driver.findElement(By.name(lvalue));
		}
		return null;

	}

	@Override
	public boolean elementIsDisplayed(String locator) {
		return locateElement(locator).isDisplayed();

	}

	@Override
	public boolean verifyElementtext(String locator, String expected) {
		boolean text = false;
		String name = locateElement(locator).getText();
		if (name.equals(expected)) {
			text = true;

		} else {
			text = false;
		}
		return text;
	}

	@Override
	public boolean verifyElementContainsText(String locator,String expected) {
		boolean text = false;
		String name = locateElement(locator).getText();
		if (name.contains(expected)) {
			text = true;

		} else {
			text = false;
		}
		return text;
	}

	@Override
	public void clickElement(String locator) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.elementToBeClickable(locateElement(locator)));
		locateElement(locator).click();

	}

	@Override
	public void EnterInput(String locator, String text) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.elementToBeClickable(locateElement(locator)));
		WebElement ele = locateElement(locator);
		ele.clear();
		ele.sendKeys(text);

	}

	@Override
	public String getElementText(String locator) {

		String text = locateElement(locator).getText();
		return text;
	}

	@Override
	public Boolean isEnabled(String locator) {
		return locateElement(locator).isDisplayed();
	}

	@Override
	public Boolean isSelected(String locator) {
		return locateElement(locator).isDisplayed();
	}

	@Override
	public void clearInput(String locator) {
		locateElement(locator).clear();

	}

	@Override
	public boolean isDisplayed(String locator) {
		boolean displayed = locateElement(locator).isDisplayed();
		return displayed;

	}

	@Override
	public void selectDropDownByVisibleText(String locator, String visibletext) {
		WebElement ele = locateElement(locator);
		Select drp = new Select(ele);
		drp.selectByVisibleText(visibletext);

	}

	@Override
	public void selectDropDownByValue(String locator, String value) {
		WebElement ele = locateElement(locator);
		Select drp = new Select(ele);
		drp.selectByValue(value);

	}

	@Override
	public void selectDropdownByindex(String locator, int index) {
		WebElement ele = locateElement(locator);
		Select drp = new Select(ele);
		drp.selectByIndex(index);

	}

	@Override
	public String getElementColour(String locator) {
		WebElement ele = locateElement(locator);
		String cssValue = ele.getCssValue("color");
		return cssValue;

	}

	@Override
	public void switchToFrameUsingIndex(int index) {
		driver.switchTo().frame(index);

	}

	@Override
	public void switchToFrameUsingLocator(String locator) {
		WebElement ele = locateElement(locator);
		driver.switchTo().frame(ele);

	}

	@Override
	public void switchToDefaultContent() {
		driver.switchTo().defaultContent();

	}

	@Override
	public void switchToWindowUsingIndex(int index) {
		Set<String> windowHandles = driver.getWindowHandles();
		List<String> Handles = new ArrayList<>(windowHandles);
		driver.switchTo().window(Handles.get(index));
	}

	@Override
	public void switchToWindowUsingTitle(String title) {
		Set<String> windowHandles = driver.getWindowHandles();
		List<String> Handles = new ArrayList<>(windowHandles);
		for (String string : Handles) {
			if (string.equals(title.toLowerCase())) {
				driver.switchTo().window(string);
			}
		}
	}

	@Override
	public String getCurrentWindowTitle(int index) {
		return driver.getTitle();

	}

	@Override
	public void acceptAlert() {
		Alert alert = driver.switchTo().alert();
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();
	}

	@Override
	public void dismissAlert() {
		Alert alert = driver.switchTo().alert();
		alert.dismiss();
	}

	@Override
	public String getAlertText() {
		Alert alert = driver.switchTo().alert();
		String text = alert.getText();
		return text;

	}

	@Override
	public void doubleClick(String locator) {
		WebElement ele = locateElement(locator);
		Actions act = new Actions(driver);
		act.doubleClick(ele).perform();

	}

	@Override
	public void dragAndDrop(String locator, String locator2) {
		WebElement source = locateElement(locator);
		WebElement target = locateElement(locator2);
		Actions act = new Actions(driver);
		act.dragAndDrop(source, target).perform();

	}

	@Override
	public void sendAlertText(String alerttext) {
		Alert alert = driver.switchTo().alert();
		alert.sendKeys(alerttext);
	}
	
	public void checkPagination(String paginationApi, int expectedPages) {
	    int actualPages = 0;
	    int itemsPerPage = 0;
	    int totalItems = 0;

	    for (Map.Entry<RequestId, Response> entry : trackedRequests.entrySet()) {
	        Response response = entry.getValue();
	        if (response.getUrl().contains(paginationApi)) {
	            actualPages++;

	            // Assuming the response body is in JSON format
	            try {
	                String responseBody = driver.executeScript("var req = new XMLHttpRequest();" +
	                        "req.open('GET', arguments[0], false);" +
	                        "req.send(null);" +
	                        "return req.responseText;", response.getUrl()).toString();

	                JSONObject jsonResponse = new JSONObject(responseBody);

	                // Extract pagination information from the response
	                // Adjust these keys based on your API's actual response structure
	                int currentPage = jsonResponse.getInt("currentPage");
	                itemsPerPage = jsonResponse.getInt("itemsPerPage");
	                totalItems = jsonResponse.getInt("totalItems");

	                // Verify that the current page number matches our counter
	                if (currentPage != actualPages) {
	                    System.out.println("Warning: Page number mismatch. Expected: " + actualPages + ", Actual: " + currentPage);
	                }

	                // Check if this is the last page
	                if (currentPage * itemsPerPage >= totalItems) {
	                    break;
	                }
	            } catch (Exception e) {
	                System.out.println("Error parsing pagination response: " + e.getMessage());
	            }
	        }
	    }

	    // Verify the total number of pages
	    int calculatedPages = (int) Math.ceil((double) totalItems / itemsPerPage);
	    if (actualPages != expectedPages || actualPages != calculatedPages) {
	        System.out.println("Pagination error: Expected pages: " + expectedPages + 
	                           ", Actual pages: " + actualPages + 
	                           ", Calculated pages: " + calculatedPages);
	    }

	    // Check if all expected pages were loaded
	    if (actualPages < expectedPages) {
	        System.out.println("Warning: Not all expected pages were loaded. Expected: " + expectedPages + ", Actual: " + actualPages);
	    }

	    // Verify consistent items per page (except possibly on the last page)
	    for (int i = 1; i <= actualPages; i++) {
	        int expectedItemsOnPage = (i == actualPages && totalItems % itemsPerPage != 0) ? 
	                                  totalItems % itemsPerPage : itemsPerPage;
	        int actualItemsOnPage = getItemCountForPage(paginationApi, i);
	        if (actualItemsOnPage != expectedItemsOnPage) {
	            System.out.println("Inconsistent item count on page " + i + 
	                               ". Expected: " + expectedItemsOnPage + 
	                               ", Actual: " + actualItemsOnPage);
	        }
	    }
	}
	
	public Map<RequestId, Response> trackedRequests = new ConcurrentHashMap<>();

	private int getItemCountForPage(String paginationApi, int pageNumber) {
	    for (Map.Entry<RequestId, Response> entry : trackedRequests.entrySet()) {
	        Response response = entry.getValue();
	        if (response.getUrl().contains(paginationApi) && response.getUrl().contains("page=" + pageNumber)) {
	            try {
	                String responseBody = driver.executeScript("var req = new XMLHttpRequest();" +
	                        "req.open('GET', arguments[0], false);" +
	                        "req.send(null);" +
	                        "return req.responseText;", response.getUrl()).toString();

	                JSONObject jsonResponse = new JSONObject(responseBody);
	                JSONArray items = jsonResponse.getJSONArray("items");
	                return items.length();
	            } catch (Exception e) {
	                System.out.println("Error parsing response for page " + pageNumber + ": " + e.getMessage());
	            }
	        }
	    }
	    return 0;
	}
	public void analyzeNetworkData(String excelFilePath) {
	    try (Workbook workbook = new XSSFWorkbook()) {
	        createSummarySheet(workbook);
	        createDetailedRequestsSheet(workbook);
	        createStatusCodeSheet(workbook);
	        createSlowResponsesSheet(workbook);
	        createDuplicateRequestsSheet(workbook);
	        createContentTypeSheet(workbook);

	        // Auto-size columns for all sheets
	        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	            Sheet sheet = workbook.getSheetAt(i);
	            for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
	                sheet.autoSizeColumn(j);
	            }
	        }

	        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
	            workbook.write(outputStream);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	private void createSummarySheet(Workbook workbook) {
	    Sheet sheet = workbook.createSheet("Summary Dashboard");
	    CellStyle headerStyle = createHeaderStyle(workbook);
	    CellStyle subHeaderStyle = createSubHeaderStyle(workbook);

	    int rowNum = 0;

	    // Title
	    Row titleRow = sheet.createRow(rowNum++);
	    Cell titleCell = titleRow.createCell(0);
	    titleCell.setCellValue("Network Traffic Analysis Dashboard");
	    CellStyle titleStyle = workbook.createCellStyle();
	    Font titleFont = workbook.createFont();
	    titleFont.setBold(true);
	    titleFont.setFontHeightInPoints((short) 16);
	    titleStyle.setFont(titleFont);
	    titleCell.setCellStyle(titleStyle);
	    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

	    rowNum++; // Empty row for spacing

	    // General Statistics
	    rowNum = addSectionHeader(sheet, rowNum, "General Statistics", headerStyle);
	    rowNum = addKeyValuePair(sheet, rowNum, "Total Requests", trackedRequests.size(), subHeaderStyle);
	    rowNum = addKeyValuePair(sheet, rowNum, "Unique URLs", trackedRequests.values().stream().map(Response::getUrl).distinct().count(), subHeaderStyle);
	    rowNum = addKeyValuePair(sheet, rowNum, "Average Response Time", String.format("%.2f ms", calculateAverageResponseTime()), subHeaderStyle);

	    rowNum++; // Empty row for spacing

	    // Status Code Summary
	    rowNum = addSectionHeader(sheet, rowNum, "Status Code Distribution", headerStyle);
	    Map<String, Long> statusCodeSummary = summarizeStatusCodes();
	    for (Map.Entry<String, Long> entry : statusCodeSummary.entrySet()) {
	        rowNum = addKeyValuePair(sheet, rowNum, entry.getKey(), entry.getValue(), subHeaderStyle);
	    }

	    rowNum++; // Empty row for spacing

	    // Content Type Summary
	    rowNum = addSectionHeader(sheet, rowNum, "Content Type Distribution", headerStyle);
	    Map<String, Long> contentTypeSummary = summarizeContentTypes();
	    for (Map.Entry<String, Long> entry : contentTypeSummary.entrySet()) {
	        rowNum = addKeyValuePair(sheet, rowNum, entry.getKey(), entry.getValue(), subHeaderStyle);
	    }

	    rowNum++; // Empty row for spacing

	    // Performance Metrics
	    rowNum = addSectionHeader(sheet, rowNum, "Performance Metrics", headerStyle);
	    rowNum = addKeyValuePair(sheet, rowNum, "Slowest Response", String.format("%.2f ms", findSlowestResponse()), subHeaderStyle);
	    rowNum = addKeyValuePair(sheet, rowNum, "Fastest Response", String.format("%.2f ms", findFastestResponse()), subHeaderStyle);
	    rowNum = addKeyValuePair(sheet, rowNum, "Responses > 1000ms", countSlowResponses(), subHeaderStyle);

	    rowNum++; // Empty row for spacing

	    // Duplicate Requests Summary
	    rowNum = addSectionHeader(sheet, rowNum, "Duplicate Requests", headerStyle);
	    rowNum = addKeyValuePair(sheet, rowNum, "Total Duplicate Requests", countDuplicateRequests(), subHeaderStyle);
	    rowNum = addKeyValuePair(sheet, rowNum, "Unique URLs with Duplicates", countUrlsWithDuplicates(), subHeaderStyle);

	    // Auto-size columns
	    for (int i = 0; i < 4; i++) {
	        sheet.autoSizeColumn(i);
	    }
	}

	// Helper methods (add these to your class)



	private double calculateAverageResponseTime() {
	    return trackedRequests.values().stream()
	            .filter(response -> response.getTiming().isPresent())
	            .mapToDouble(response -> {
	                ResourceTiming timing = response.getTiming().get();
	                return timing.getReceiveHeadersEnd().doubleValue() - timing.getRequestTime().doubleValue();
	            })
	            .average()
	            .orElse(0.0);
	}

	private Map<String, Long> summarizeStatusCodes() {
	    Map<String, Long> summary = new LinkedHashMap<>();
	    summary.put("2xx Successful", 0L);
	    summary.put("3xx Redirection", 0L);
	    summary.put("4xx Client Error", 0L);
	    summary.put("5xx Server Error", 0L);

	    for (Response response : trackedRequests.values()) {
	        int statusCode = response.getStatus();
	        if (statusCode >= 200 && statusCode < 300) summary.put("2xx Successful", summary.get("2xx Successful") + 1);
	        else if (statusCode >= 300 && statusCode < 400) summary.put("3xx Redirection", summary.get("3xx Redirection") + 1);
	        else if (statusCode >= 400 && statusCode < 500) summary.put("4xx Client Error", summary.get("4xx Client Error") + 1);
	        else if (statusCode >= 500) summary.put("5xx Server Error", summary.get("5xx Server Error") + 1);
	    }

	    return summary;
	}

	private Map<String, Long> summarizeContentTypes() {
	    return trackedRequests.values().stream()
	            .collect(Collectors.groupingBy(this::getContentType, Collectors.counting()));
	}

	private double findSlowestResponse() {
	    return trackedRequests.values().stream()
	            .filter(response -> response.getTiming().isPresent())
	            .mapToDouble(response -> {
	                ResourceTiming timing = response.getTiming().get();
	                return timing.getReceiveHeadersEnd().doubleValue() - timing.getRequestTime().doubleValue();
	            })
	            .max()
	            .orElse(0.0);
	}

	private double findFastestResponse() {
	    return trackedRequests.values().stream()
	            .filter(response -> response.getTiming().isPresent())
	            .mapToDouble(response -> {
	                ResourceTiming timing = response.getTiming().get();
	                return timing.getReceiveHeadersEnd().doubleValue() - timing.getRequestTime().doubleValue();
	            })
	            .min()
	            .orElse(0.0);
	}

	private long countSlowResponses() {
	    return trackedRequests.values().stream()
	            .filter(response -> response.getTiming().isPresent())
	            .filter(response -> {
	                ResourceTiming timing = response.getTiming().get();
	                return timing.getReceiveHeadersEnd().doubleValue() - timing.getRequestTime().doubleValue() > 1000;
	            })
	            .count();
	}

	private long countDuplicateRequests() {
	    return trackedRequests.values().stream()
	            .collect(Collectors.groupingBy(Response::getUrl, Collectors.counting()))
	            .values().stream()
	            .filter(count -> count > 1)
	            .mapToLong(count -> count - 1)
	            .sum();
	}

	private long countUrlsWithDuplicates() {
	    return trackedRequests.values().stream()
	            .collect(Collectors.groupingBy(Response::getUrl, Collectors.counting()))
	            .values().stream()
	            .filter(count -> count > 1)
	            .count();
	}

	private String formatBytes(long bytes) {
	    if (bytes < 1024) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(1024));
	    String pre = "KMGTPE".charAt(exp-1) + "";
	    return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
	}

	private int addSectionHeader(Sheet sheet, int rowNum, String title, CellStyle style) {
	    Row row = sheet.createRow(rowNum);
	    Cell cell = row.createCell(0);
	    cell.setCellValue(title);
	    cell.setCellStyle(style);
	    sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 3));
	    return rowNum + 1;
	}

	private int addKeyValuePair(Sheet sheet, int rowNum, String key, Object value, CellStyle style) {
	    Row row = sheet.createRow(rowNum);
	    Cell keyCell = row.createCell(0);
	    keyCell.setCellValue(key);
	    keyCell.setCellStyle(style);

	    Cell valueCell = row.createCell(1);
	    if (value instanceof String) {
	        valueCell.setCellValue((String) value);
	    } else if (value instanceof Number) {
	        valueCell.setCellValue(((Number) value).doubleValue());
	    }
	    return rowNum + 1;
	}

	private CellStyle createSubHeaderStyle(Workbook workbook) {
	    CellStyle style = workbook.createCellStyle();
	    Font font = workbook.createFont();
	    font.setItalic(true);
	    style.setFont(font);
	    return style;
	}

	private void createDetailedRequestsSheet(Workbook workbook) {
	    Sheet sheet = workbook.createSheet("Detailed Requests");
	    Row headerRow = sheet.createRow(0);
	    CellStyle headerStyle = createHeaderStyle(workbook);

	    String[] headers = {"URL", "Status Code", "Content Type", "Response Time (ms)", "Response Size (bytes)"};
	    for (int i = 0; i < headers.length; i++) {
	        cell(headerRow, i, headers[i], headerStyle);
	    }

	    int rowNum = 1;
	    CellStyle redStyle = createColorStyle(workbook, IndexedColors.RED);
	    CellStyle orangeStyle = createColorStyle(workbook, IndexedColors.ORANGE);
	    CellStyle greenStyle = createColorStyle(workbook, IndexedColors.GREEN);

	    for (Response response : trackedRequests.values()) {
	        Row row = sheet.createRow(rowNum++);
	        cell(row, 0, response.getUrl());
	        
	        int statusCode = response.getStatus();
	        Cell statusCell = cell(row, 1, statusCode);
	        if (statusCode >= 400) {
	            statusCell.setCellStyle(redStyle);
	        } else if (statusCode >= 300) {
	            statusCell.setCellStyle(orangeStyle);
	        } else {
	            statusCell.setCellStyle(greenStyle);
	        }

	        cell(row, 2, getContentType(response));
	        
	        Optional<ResourceTiming> timing = response.getTiming();
	        if (timing.isPresent()) {
	            ResourceTiming resourceTiming = timing.get();
	            double responseTime = resourceTiming.getReceiveHeadersEnd().doubleValue() - resourceTiming.getRequestTime().doubleValue();
	            cell(row, 3, responseTime);
	        }

	        cell(row, 4, response.getEncodedDataLength());
	    }
	}

	private void createStatusCodeSheet(Workbook workbook) {
	    Sheet sheet = workbook.createSheet("Status Code Distribution");
	    Row headerRow = sheet.createRow(0);
	    CellStyle headerStyle = createHeaderStyle(workbook);

	    cell(headerRow, 0, "Status Code", headerStyle);
	    cell(headerRow, 1, "Count", headerStyle);
	    cell(headerRow, 2, "Description", headerStyle);

	    Map<Integer, Integer> statusCodeDistribution = new HashMap<>();
	    for (Response response : trackedRequests.values()) {
	        int statusCode = response.getStatus();
	        statusCodeDistribution.put(statusCode, statusCodeDistribution.getOrDefault(statusCode, 0) + 1);
	    }

	    int rowNum = 1;
	    for (Map.Entry<Integer, Integer> entry : statusCodeDistribution.entrySet()) {
	        Row row = sheet.createRow(rowNum++);
	        cell(row, 0, entry.getKey());
	        cell(row, 1, entry.getValue());
	        cell(row, 2, getStatusCodeDescription(entry.getKey()));
	    }
	}

	private void createSlowResponsesSheet(Workbook workbook) {
	    Sheet sheet = workbook.createSheet("Slow Responses (>1000ms)");
	    Row headerRow = sheet.createRow(0);
	    CellStyle headerStyle = createHeaderStyle(workbook);

	    cell(headerRow, 0, "URL", headerStyle);
	    cell(headerRow, 1, "Response Time (ms)", headerStyle);

	    int rowNum = 1;
	    for (Response response : trackedRequests.values()) {
	        Optional<ResourceTiming> timing = response.getTiming();
	        if (timing.isPresent()) {
	            ResourceTiming resourceTiming = timing.get();
	            double responseTime = resourceTiming.getReceiveHeadersEnd().doubleValue() - resourceTiming.getRequestTime().doubleValue();
	            if (responseTime > 1000) {
	                Row row = sheet.createRow(rowNum++);
	                cell(row, 0, response.getUrl());
	                cell(row, 1, responseTime);
	            }
	        }
	    }
	}

	private void createDuplicateRequestsSheet(Workbook workbook) {
	    Sheet sheet = workbook.createSheet("Duplicate Requests");
	    Row headerRow = sheet.createRow(0);
	    CellStyle headerStyle = createHeaderStyle(workbook);

	    cell(headerRow, 0, "URL", headerStyle);
	    cell(headerRow, 1, "Count", headerStyle);

	    Map<String, Integer> urlCount = new HashMap<>();
	    for (Response response : trackedRequests.values()) {
	        String url = response.getUrl();
	        urlCount.put(url, urlCount.getOrDefault(url, 0) + 1);
	    }

	    int rowNum = 1;
	    for (Map.Entry<String, Integer> entry : urlCount.entrySet()) {
	        if (entry.getValue() > 1) {
	            Row row = sheet.createRow(rowNum++);
	            cell(row, 0, entry.getKey());
	            cell(row, 1, entry.getValue());
	        }
	    }
	}

	private void createContentTypeSheet(Workbook workbook) {
	    Sheet sheet = workbook.createSheet("Content Type Distribution");
	    Row headerRow = sheet.createRow(0);
	    CellStyle headerStyle = createHeaderStyle(workbook);

	    cell(headerRow, 0, "Content Type", headerStyle);
	    cell(headerRow, 1, "Count", headerStyle);

	    Map<String, Integer> contentTypeCount = new HashMap<>();
	    for (Response response : trackedRequests.values()) {
	        String contentType = getContentType(response);
	        contentTypeCount.put(contentType, contentTypeCount.getOrDefault(contentType, 0) + 1);
	    }

	    int rowNum = 1;
	    for (Map.Entry<String, Integer> entry : contentTypeCount.entrySet()) {
	        Row row = sheet.createRow(rowNum++);
	        cell(row, 0, entry.getKey());
	        cell(row, 1, entry.getValue());
	    }
	}

	private CellStyle createHeaderStyle(Workbook workbook) {
	    CellStyle style = workbook.createCellStyle();
	    Font font = workbook.createFont();
	    font.setBold(true);
	    style.setFont(font);
	    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    return style;
	}

	private CellStyle createColorStyle(Workbook workbook, IndexedColors color) {
	    CellStyle style = workbook.createCellStyle();
	    style.setFillForegroundColor(color.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    return style;
	}

	private Cell cell(Row row, int column, Object value) {
	    Cell cell = row.createCell(column);
	    if (value instanceof String) {
	        cell.setCellValue((String) value);
	    } else if (value instanceof Integer) {
	        cell.setCellValue((Integer) value);
	    } else if (value instanceof Double) {
	        cell.setCellValue((Double) value);
	    }
	    return cell;
	}

	private Cell cell(Row row, int column, Object value, CellStyle style) {
	    Cell cell = cell(row, column, value);
	    cell.setCellStyle(style);
	    return cell;
	}

	private String getContentType(Response response) {
	    for (Map.Entry<String, Object> header : response.getHeaders().entrySet()) {
	        if (header.getKey().equalsIgnoreCase("Content-Type")) {
	            return header.getValue().toString().split(";")[0];
	        }
	    }
	    return "Unknown";
	}

	private String getStatusCodeDescription(int statusCode) {
	    switch (statusCode) {
	        case 200: return "OK";
	        case 201: return "Created";
	        case 204: return "No Content";
	        case 301: return "Moved Permanently";
	        case 302: return "Found";
	        case 304: return "Not Modified";
	        case 400: return "Bad Request";
	        case 401: return "Unauthorized";
	        case 403: return "Forbidden";
	        case 404: return "Not Found";
	        case 500: return "Internal Server Error";
	        default: return "Unknown";
	    }
	}
	

	@Override
	public void waitUntilInvisibilityOfElement(int waitime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver, Duration.ofSeconds(waitime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilElementIsClickable(int waitime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.elementToBeClickable(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.elementToBeClickable(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilElementIsDisplayed(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilPresenceOfAllElementsLocated(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(lvalue)));
		}

	}
	@Override
	public void waitUntilElementToBeSelected(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.elementToBeSelected(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.elementToBeSelected(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.elementToBeSelected(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilPresenceofElementlocated(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilVisisblityOfAllElementsLocated(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilInvisibliltyOfAllElementsLocated(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(lvalue)));
		}
	}

	@Override
	public void waituntilFrameIsVisibleAndSwitchToIt(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(lvalue)));
		}

	}
	

	@Override
	public void LaunchBrowser(String browser) {
	    switch (browser.toLowerCase()) {
	        case "chrome":
	        	driver = new ChromeDriver();
				driver.manage().window().maximize();
	            
	            // Initialize DevTools for Chrome
	            DevTools devTools = ((ChromeDriver) driver).getDevTools();
	            devTools.createSession();
	            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
	            
	            // Add listener for network responses
	            devTools.addListener(Network.responseReceived(), response -> {
	                System.out.println("Response received: " + response.getResponse().getUrl());
	                trackedRequests.put(response.getRequestId(), response.getResponse());
	            });
	            
	            break;

	        case "edge":
	            EdgeOptions options1 = new EdgeOptions();
	            options1.addArguments("--remote-allow-origins=*");
	            driver = new EdgeDriver(options1);
	            break;

	        case "firefox":
	            driver = new FirefoxDriver();
	            break;

	        case "safari":
	            driver = new SafariDriver();
	            break;

	        default:
	            throw new RuntimeException("Unsupported browser: " + browser);
	    }
	}

	@Override
	public String getConfigurations(String key) {

		String value = null;
		try {
			FileInputStream file = new FileInputStream("./src/main/resources/local repositories/config.properties");
			Properties properties = new Properties();
			try {
				properties.load(file);
				value = properties.getProperty(key);

			} catch (Exception e) {
				System.out.println(e);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return value;
	}
	
	
	public String getLocators(String module,String locator) {
		String value = null;
		try {
			FileInputStream file = new FileInputStream("./src/main/resources/locators repositories/"+module+".properties");
			Properties properties = new Properties();
			try {
				properties.load(file);
				value = properties.getProperty(locator);

			} catch (Exception e) {
				System.out.println(e);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return value;
		
	}

	@Override
	public void clickElement(String locator, int waittime) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		wait.until(ExpectedConditions.elementToBeClickable(locateElement(locator)));
		locateElement(locator).click();

	}

	@Override
	public void EnterInput(String locator, String text, int waittime) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		wait.until(ExpectedConditions.elementToBeClickable(locateElement(locator)));
		locateElement(locator).sendKeys(text);
	}
	@Override
	public String readAndDecryptexcel(String encryptedValue) throws IOException {
		byte[] decodedBytes = Base64.getDecoder().decode(encryptedValue);
		return new String(decodedBytes);
	}
	@Override
	public void refreshBrowser() {
		try {
			driver.navigate().refresh();
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	// Method to perform Copy (Ctrl+C) keyboard shortcut
    public void copyText(String element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(locateElement(element)).sendKeys(Keys.chord(Keys.CONTROL, "c")).perform();
    }
    // Method to perform Paste (Ctrl+V) keyboard shortcut
    public void pasteText(String element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(locateElement(element)).sendKeys(Keys.chord(Keys.CONTROL, "v")).perform();
    }

    // Method to perform Cut (Ctrl+X) keyboard shortcut
    public void cutText(String element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(locateElement(element)).sendKeys(Keys.chord(Keys.CONTROL, "x")).perform();
    }

    // Method to perform Undo (Ctrl+Z) keyboard shortcut
    public void undoAction() {
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.chord(Keys.CONTROL, "z")).perform();
    }

    // Method to perform Redo (Ctrl+Y) keyboard shortcut
    public void redoAction() {
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.chord(Keys.CONTROL, "y")).perform();
    }

    // Method to perform Bold (Ctrl+B) keyboard shortcut
    public void makeTextBold(String element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(locateElement(element)).sendKeys(Keys.chord(Keys.CONTROL, "b")).perform();
    }

}
	