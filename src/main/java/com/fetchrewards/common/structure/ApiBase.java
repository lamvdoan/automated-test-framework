package com.fetchrewards.common.structure;

import com.fetchrewards.common.framework.AutomationBase;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static io.restassured.config.RestAssuredConfig.config;

public class ApiBase extends AutomationBase {
    private boolean checkStatus = true;
    protected String endpointPath;
    protected Response lastResponse;
    protected String entireUrl;
    protected Map<String, String> headers = new HashMap<String, String>();
    protected Map<String, String> parameters = new HashMap<String, String>();

    public ApiBase() {
        RestAssured.config = config().jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));
        headers.put("Content-Type", "application/json");
    }

    private RequestSpecification getRequestSpec() {
        log.debug("    Using headers: " + headers);
        log.debug("    Using parameters: " + parameters);
        return given().headers(headers).queryParameters(parameters);
    }

    private RequestSpecification getRequestSpecWithParameters() {
        log.debug("    Using headers: " + headers);
        log.debug("    Using parameters: " + parameters);

        return given().headers(headers).parameters(parameters);
    }

    public void turnOffRedirects() {
        RestAssured.config = config().redirect(redirectConfig().followRedirects(false));
    }

    public String getEntireUrl(String id) {
        if (parameters.isEmpty()) {
            entireUrl = endpointPath + (id != null ? id : "");
        } else {
            entireUrl = endpointPath + (id != null ? id : "") + "?";

            Iterator it = parameters.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                entireUrl += pair.getKey() + "=" + pair.getValue() + "&";
            }

            // delete the last ampersand
            if (entireUrl.substring(entireUrl.length() - 1).equals("&")) {
                entireUrl = entireUrl.substring(0, entireUrl.length() - 1);
            }
        }

        return entireUrl;
    }

    public String getEntireUrl() {
        return getEntireUrl(null);
    }

    private void setResponseStuff(JsonObjectBase object) {
        object.setResponse(lastResponse);
        object.setStatusCode(lastResponse.getStatusCode());
        object.setResponseBody(lastResponse.getBody());
        object.setHeaders(lastResponse.getHeaders());
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Response getLastResponse() {
        return this.lastResponse;
    }

    public void setEndpointPath(String path) {
        this.endpointPath = path;
    }
    public String getEndpointPath() {
        return this.endpointPath;
    }

    public void appendToEndpointPath(String path) {
        this.endpointPath += path;
    }

    public String post() {
        log.info("  POST: " + getEntireUrl());

        lastResponse = getRequestSpec().request().post(this.endpointPath);
        checkStatusCodeForSuccess();

        return lastResponse.getBody().asString();
    }

    public String postWithParameters() {
        log.info("  POST: " + getEntireUrl());

        lastResponse = getRequestSpecWithParameters().request().post(this.endpointPath);
        checkStatusCodeForSuccess();

        return lastResponse.getBody().asString();
    }

    public String post(String requestBody) {
        log.info("  POST: " + getEntireUrl());
        log.debug("    BODY: " + requestBody);

        lastResponse = getRequestSpec().request().body(requestBody).post(this.endpointPath);
        checkStatusCodeForSuccess();

        String response = lastResponse.getBody().asString();
        logResponse(response);
        return response;
    }

    public String post(JsonObjectBase jsonToPost) {
        return post(jsonToPost, null);
    }

    public String post(JsonObjectBase jsonToPost, String extraPath) {
        String url = getEntireUrl(extraPath);

        log.info("  POST: " + url);
        log.debug("    BODY: " + jsonToPost.toJsonString());

        lastResponse = getRequestSpec().request().body(jsonToPost).post(url);
        this.setResponseStuff(jsonToPost);

        checkStatusCodeForSuccess();

        String response = jsonifyResponse();
        logResponse(response);

        return response;
    }

    private String jsonifyResponse() {
        log.disableSystemOut();
        String response = lastResponse.getBody().prettyPrint();
        log.enableSystemOut();
        return response;
    }

    public String put(JsonObjectBase jsonToPut, String extraPath) {
        String url = getEntireUrl(extraPath);

        log.info("  PUT: " + url);
        log.debug("  BODY: " + jsonToPut.toJsonString());
        lastResponse = getRequestSpec().request().body(jsonToPut).put(url);

        setResponseStuff(jsonToPut);
        checkStatusCodeForSuccess();

        String response = lastResponse.getBody().asString();

        logResponse(response);
        return response;
    }

    public String put(JsonObjectBase jsonToPut) {
        return this.put(jsonToPut, null);
    }

    public String put(String requestBody) {
        log.info("  PUT: " + getEntireUrl());
        log.debug("    BODY: " + requestBody);

        lastResponse = getRequestSpec().request().body(requestBody).put(this.endpointPath);
        checkStatusCodeForSuccess();

        String response = lastResponse.getBody().asString();
        log.debug("RESPONSE: " + response);
        return response;
    }

    public String patch(JsonObjectBase jsonToPatch, String id) {
        log.info("  PATCH: " + this.endpointPath + id);
        lastResponse = getRequestSpec().request().body(jsonToPatch).patch(this.endpointPath + id);

        setResponseStuff(jsonToPatch);
        checkStatusCodeForSuccess();

        return lastResponse.body().asString();
    }

    public String patch(JsonObjectBase jsonToPatch) {
        log.info("  PATCH: " + this.endpointPath);
        log.debug("  BODY: " + jsonToPatch.toJsonString());
        lastResponse = getRequestSpec().request().body(jsonToPatch).patch(this.endpointPath);

        setResponseStuff(jsonToPatch);
        checkStatusCodeForSuccess();

        return lastResponse.body().asString();
    }

    public String delete() {
        log.info("  DELETE: " + this.endpointPath);
        lastResponse = getRequestSpec().delete(this.endpointPath);

        checkStatusCodeForSuccess();

        String response = lastResponse.getBody().asString();
        logResponse(response);

        return response;
    }

    public int delete(String id) {
        log.info("  DELETE: " + this.endpointPath + id);
        lastResponse = getRequestSpec().delete(this.endpointPath + id);

        checkStatusCodeForSuccess();

        return lastResponse.getStatusCode();
    }

    public String delete(JsonObjectBase json) {
        log.info("  DELETE: " + this.endpointPath);
        log.debug("  BODY: " + json.toJsonString());

        lastResponse = getRequestSpec().body(json).delete(this.endpointPath);

        checkStatusCodeForSuccess();

        String response = lastResponse.getBody().asString();
        logResponse(response);

        return response;
    }

    public String get() {
        return this.get(null, true);
    }

    public String get(boolean loggingEnabled) {
        return this.get(null, loggingEnabled);
    }

    public String get(String id) {
        return this.get(id, true);
    }

    public String get(String id, boolean loggingEnabled) {
        if (loggingEnabled) {
            log.info("  GET: " + getEntireUrl(id));
        } else {
            log.debug("  GET: " + getEntireUrl(id));
        }

        lastResponse = getRequestSpec().get(this.endpointPath + (id != null ? id : ""));

        checkStatusCodeForSuccess();

        String response = jsonifyResponse();
        logResponse(response);

        return response;
    }

    public void addHeader(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
    }

    public void removeHeader(String headerName) {
        headers.remove(headerName);
    }

    public void addParameter(String parmName, String parmValue) {
        parameters.put(parmName, parmValue);
    }

    public void setParameterToMap(Map<String, String> map) {
        parameters.putAll(map);
    }

    public void clearParameters() {
        parameters.clear();
    }

    public void addAuthorizationHeader(String key) {
        addHeader("Authorization", key);
    }

    public void enableStatusChecks() {
        this.checkStatus = true;
    }

    public void disableStatusChecks() {
        this.checkStatus = false;
    }

    private void logResponse(String response) {
        if (response.length() > 2000) {
            log.debug("*** NOTE: Hiding response, change logLevel to TRACE to view response ***");
            log.trace("  RESPONSE: " + response);
        } else {
            log.debug("  RESPONSE: " + response);
        }
    }

    private void checkStatusCodeForSuccess() {
        int statusCode = lastResponse.getStatusCode();

        if (checkStatus) {
            switch (statusCode) {
                case 200:
                case 201:
                case 204:
                    log.debug("  Status code:  " + statusCode);
                    break;

                default:
                    log.error("  ^^^^ response return code: " + statusCode);
                    log.error(lastResponse.getBody().asString());
            }
        }
    }
}
