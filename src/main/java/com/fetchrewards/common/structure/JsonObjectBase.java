package com.fetchrewards.common.structure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)

public class JsonObjectBase {
    @JsonIgnore
    private Response _response;
    @JsonIgnore private int _statusCode;
    @JsonIgnore private ResponseBody _responseBody;
    @JsonIgnore private Headers _headers;
    @JsonIgnore static private boolean mapCamelCase = false;

    // For compares...
    @JsonIgnore static protected String currentNode = "";
    @JsonIgnore static protected boolean equalsFlag = true;
    @JsonIgnore static protected int fieldsCompared = 0;

    public void setReturnCode(int code) {
        this._statusCode = code;
    }
    public int getStatusCode() {
        return this._statusCode;
    }
    public String getResponseBody() {
        return this._responseBody.asString();
    }
    public Response getResponse() {
        return this._response;
    }
    public String getHeaderValue(String headerName) {
        return _headers.getValue(headerName);
    }

    public void setStatusCode(int statusCode) {
        _statusCode = statusCode;
    }
    public void setResponseBody(ResponseBody responseBody) {
        _responseBody = responseBody;
    }
    public void setHeaders(Headers headers) {
        _headers = headers;
    }
    public void setResponse(Response response) {
        this._response = response;
    }

    public void resetEqualsInfo() {
        equalsFlag = true;
        fieldsCompared = 0;
    }
    public boolean getEqualsFlag() {
        return equalsFlag;
    }
    public int getFieldsCompared() {
        return fieldsCompared;
    }

    protected static Object fromJson(String json, Object object) {
        ObjectMapper mapper = new ObjectMapper();

        Class noClass = object.getClass();
        Object returnObj = null;

        mapper.registerModule(new JodaModule());    // This allows us to read into DateTime vars

        if (mapCamelCase) {
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        }

        try {
            returnObj = mapper.readValue(json, noClass);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnObj;
    }

    public String toJsonString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        StringWriter sw = new StringWriter();

        if (mapCamelCase) {
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        }

        try {
            mapper.writeValue(sw, this);
        } catch (Exception e) {
        }

        return sw.toString();
    }

    @Override
    public String toString() {
        return toJsonString();
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        if (o.getClass() != getClass()) {
            equalsFlag = false;
            return false;
        }

        Field[] thisFields = this.getClass().getDeclaredFields();
        Field[] otherFields = o.getClass().getDeclaredFields();

        if (thisFields.length != otherFields.length) {
            equalsFlag = false;
            return false;
        }

        if (thisFields.length > 0) {
            for (Field field : thisFields) {
                Object expected = null;
                Object actual = null;

                try {
                    expected = field.get(this);
                    actual = field.get(o);
                } catch (Exception e) {
                }

                currentNode += "/" + field.getName();
                fieldsCompared++;

                currentNode = currentNode.replace("/" + field.getName(), "");
            }
        }

        return true;
    }
}
