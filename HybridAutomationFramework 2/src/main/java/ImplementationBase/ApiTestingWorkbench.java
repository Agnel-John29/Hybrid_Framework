
package ImplementationBase;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.json.JSONArray;
import org.json.JSONObject;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.logs.AWSLogs;
import com.amazonaws.services.logs.AWSLogsClientBuilder;
import com.amazonaws.services.logs.model.*;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Region;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.Map;
import java.util.HashMap;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.logs.AWSLogs;
import com.amazonaws.services.logs.AWSLogsClientBuilder;
import com.amazonaws.services.logs.model.FilterLogEventsRequest;
import com.amazonaws.services.logs.model.FilterLogEventsResult;
import com.amazonaws.services.logs.model.FilteredLogEvent;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

public class ApiTestingWorkbench {

    protected static ExtentReports extent;
    protected static ExtentTest test;
    protected static ExtentTest node;
    protected static String folderName;
    private String customBearerToken;

    
    static {
        initializeReport();
    }

    public static void initializeReport() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        folderName = "api_reports/" + timeStamp;

        File folder = new File("./" + folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        extent = new ExtentReports();
        ExtentSparkReporter reporter = new ExtentSparkReporter("./" + folderName + "/api_test_result.html");
        extent.attachReporter(reporter);
    }
    
    public String getWebhookBearerToken(String clientId, String clientSecret) throws Exception {
        String tokenUrl = "https://etmqc.auth.us-east-1.amazoncognito.com/oauth2/token";
        String encodedCredentials = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        Response response = RestAssured.given()
            .header("Authorization", "Basic " + encodedCredentials)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .formParam("grant_type", "client_credentials")
            .post(tokenUrl);

        if (response.getStatusCode() != 200) {
            throw new Exception("Failed to obtain bearer token. Status code: " + response.getStatusCode());
        }

        JSONObject jsonResponse = new JSONObject(response.getBody().asString());
        String accessToken = jsonResponse.getString("access_token");

        reportStep("Successfully obtained webhook bearer token", "pass");
        return accessToken;
    }

    public void setupTest(String testName, String testDescription, String category, String author) {
        test = extent.createTest(testName, testDescription);
        test.assignCategory(category);
        test.assignAuthor(author);
        node = test.createNode(testName);
    }

 
    public void reportStep(String msg, String status) {
        if (node == null) {
            System.out.println("Warning: node is null. Message: " + msg);
            return;
        }
        switch (status.toLowerCase()) {
            case "pass":
                node.pass(msg);
                break;
            case "fail":
                node.fail(msg);
                break;
            case "info":
                node.info(msg);
                break;
            default:
                node.info(msg);
        }
    }

    public void reportApiRequest(String endpoint, String method, String requestBody) {
        node.info("API Request: " + method + " " + endpoint);
        if (requestBody != null && !requestBody.isEmpty()) {
            node.info("Request Body: " + requestBody);
        }
    }

    public void reportApiResponse(Response response) {
        node.info("Response Status Code: " + response.getStatusCode());
        node.info("Response Body: " + response.getBody().asString());
    }

    public String getURL(String module, String url) {
        String value = null;
        try {
            FileInputStream file = new FileInputStream("./src/main/resources/locators repositories/" + module + ".properties");
            Properties properties = new Properties();
            properties.load(file);
            value = properties.getProperty(url);
        } catch (Exception e) {
            reportStep("Error accessing properties file: " + e.getMessage(), "fail");
        }
        return value;
    }
    
  
  
    public Map<String, Boolean> checkCloudWatchLogs(String logGroupName, Map<String, String> identifiers, long startTimeMillis, long endTimeMillis) {
        System.out.println("Checking CloudWatch logs for group: " + logGroupName);
        System.out.println("Time range: " + Instant.ofEpochMilli(startTimeMillis) + " to " + Instant.ofEpochMilli(endTimeMillis));
        System.out.println("Searching for identifiers: " + identifiers);

        // AWS client setup code remains the same
        String accessKey = getSecKey("AWS", "access_key");
        String secretKey = getSecKey("AWS", "secret_key");
        String regionName = getSecKey("AWS", "region");
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AWSLogs client = AWSLogsClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(regionName)
                .build();

        Map<String, Boolean> foundIdentifiers = new HashMap<>();
        for (String key : identifiers.keySet()) {
            foundIdentifiers.put(key, false);
        }

        String nextToken = null;
        int totalLogEvents = 0;
        do {
            FilterLogEventsRequest filterRequest = new FilterLogEventsRequest()
                .withLogGroupName(logGroupName)
                .withStartTime(startTimeMillis)
                .withEndTime(endTimeMillis)
                .withLimit(10000);

            if (nextToken != null) {
                filterRequest.setNextToken(nextToken);
            }

            FilterLogEventsResult result = client.filterLogEvents(filterRequest);
            System.out.println("Retrieved " + result.getEvents().size() + " log events.");
            totalLogEvents += result.getEvents().size();

            for (FilteredLogEvent event : result.getEvents()) {
                try {
                	System.out.println(event);
                    JSONObject logEntry = new JSONObject(event.getMessage());
                    String logContent = logEntry.optString("log", "");
                    String logTimeStr = logEntry.optString("time", "");

                    for (Map.Entry<String, String> entry : identifiers.entrySet()) {
                        String key = entry.getKey();
                        String expectedValue = entry.getValue();

                        if (key.equalsIgnoreCase("event") && logContent.contains(expectedValue)) {
                            foundIdentifiers.put(key, true);
                            System.out.println("Found match for event: " + expectedValue);
                        } else if (key.equalsIgnoreCase("inTime")) {
                            Instant logTime = Instant.parse(logTimeStr);
                            Instant expectedTime = Instant.parse(expectedValue);
                            if (logTime.truncatedTo(ChronoUnit.MINUTES).equals(expectedTime.truncatedTo(ChronoUnit.MINUTES))) {
                                foundIdentifiers.put(key, true);
                                System.out.println("Found match for inTime: " + logTimeStr);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing log message: " + e.getMessage());
                    System.err.println("Problematic log message: " + event.getMessage());
                }
            }

            nextToken = result.getNextToken();
        } while (nextToken != null);

        System.out.println("Total log events processed: " + totalLogEvents);
        System.out.println("Search results: " + foundIdentifiers);

        return foundIdentifiers;
    }
    
    public String getSecKey(String module, String SecKey) {
        String value = null;
        try {
            FileInputStream file = new FileInputStream("./src/main/resources/locators repositories/" + module + ".properties");
            Properties properties = new Properties();
            properties.load(file);
            value = properties.getProperty(SecKey);
        } catch (Exception e) {
            reportStep("Error accessing properties file: " + e.getMessage(), "fail");
        }
        return value;
    }

    public void setupBaseURI(String module, String urlKey) {
        String apiUrl = getURL(module, urlKey);
        RestAssured.baseURI = apiUrl;
        reportStep("Base URI set to: " + apiUrl, "info");
    }

    public String readJsonFile(String filePath) throws Exception {
        System.out.println("Attempting to read file: " + filePath);
        File file = new File(filePath);
        System.out.println("File exists: " + file.exists());
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        System.out.println("File content length: " + content.length());
        reportStep("JSON file read successfully: " + filePath, "info");
        return content;
    }
    

    public Response sendGetRequest(String endpoint, Map<String, String> params, Map<String, String> headers) throws Exception {
        reportApiRequest(endpoint, "GET", null);
        RequestSpecification request = RestAssured.given();
        if (params != null) {
            request.queryParams(params);
        }
        if (headers != null) {
            request.headers(headers);
        }
        Response response = request.get(endpoint);
        reportApiResponse(response);
        return response;
    }


    public Response sendPostRequest(String endpoint, String requestBody, Map<String, String> headers) throws Exception {
        reportApiRequest(endpoint, "POST", requestBody);
        System.out.println("Sending POST request to: " + RestAssured.baseURI + endpoint);
        System.out.println("Headers: " + headers);
        System.out.println("Request Body: " + requestBody);
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody);
        if (headers != null) {
            request.headers(headers);
        }
        try {
            Response response = request.post(endpoint);
            reportApiResponse(response);
            return response;
        } catch (Exception e) {
            System.out.println("Exception occurred while sending POST request: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    

    public Response sendPutRequest(String endpoint, String requestBody, Map<String, String> headers) throws Exception {
        reportApiRequest(endpoint, "PUT", requestBody);
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody);
        if (headers != null) {
            request.headers(headers);
        }
        Response response = request.put(endpoint);
        reportApiResponse(response);
        return response;
    }

    public Response sendDeleteRequest(String endpoint, Map<String, String> headers) throws Exception {
        reportApiRequest(endpoint, "DELETE", null);
        RequestSpecification request = RestAssured.given();
        if (headers != null) {
            request.headers(headers);
        }
        Response response = request.delete(endpoint);
        reportApiResponse(response);
        return response;
    }
    
    

    
    public String extractValueFromResponse(Response response, String key) throws Exception {
        JSONObject jsonResponse = new JSONObject(response.asString());
        if (jsonResponse.has(key)) {
            Object value = jsonResponse.get(key);
            String stringValue;
            
            if (value instanceof String) {
                stringValue = (String) value;
            } else if (value instanceof Integer) {
                stringValue = String.valueOf(value);
            } else {
                stringValue = value.toString();
            }
            
            reportStep("Extracted value for key '" + key + "': " + stringValue, "info");
            return stringValue;
        } else {
            reportStep("Key '" + key + "' not found in response", "info");
            return null;
        }
    }

    public boolean verifyStatusCode(Response response, int expectedStatusCode) throws Exception {
        int actualStatusCode = response.getStatusCode();
        if (actualStatusCode == expectedStatusCode) {
            reportStep("Status code verification passed. Expected: " + expectedStatusCode + ", Actual: " + actualStatusCode, "pass");
            return true;
        } else {
            reportStep("Status code verification failed. Expected: " + expectedStatusCode + ", Actual: " + actualStatusCode, "fail");
            return false;
        }
    }

    public boolean isKeyPresentInResponse(Response response, String key) throws Exception {
        JSONObject jsonResponse = new JSONObject(response.asString());
        boolean isPresent = jsonResponse.has(key);
        if (isPresent) {
            reportStep("Key '" + key + "' is present in the response", "pass");
        } else {
            reportStep("Key '" + key + "' is not present in the response", "fail");
        }
        return isPresent;
    }

    public void waitForCondition(Supplier<Boolean> condition, int timeoutInSeconds) throws Exception {
        int waited = 0;
        while (!condition.get() && waited < timeoutInSeconds) {
            Thread.sleep(1000);
            waited++;
        }
        if (waited >= timeoutInSeconds) {
            reportStep("Condition not met within " + timeoutInSeconds + " seconds", "fail");
            throw new RuntimeException("Condition not met within " + timeoutInSeconds + " seconds");
        } else {
            reportStep("Condition met within " + waited + " seconds", "pass");
        }
    }

    public void validateResponseTime(Response response, long maxTime) throws Exception {
        long responseTime = response.getTime();
        if (responseTime <= maxTime) {
            reportStep("Response time is within acceptable limit. Actual: " + responseTime + "ms, Max: " + maxTime + "ms", "pass");
        } else {
            reportStep("Response time exceeded limit. Actual: " + responseTime + "ms, Max: " + maxTime + "ms", "fail");
        }
    }

    public void finishReport() {
        extent.flush();
    }
}