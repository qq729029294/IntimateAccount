package com.nan.ia.app.http.cmd;

/**
 * Created by weijiangnan on 14-8-13.
 */
public class HttpCmdInfo {
    public enum HttpMethod {
        GET,
        POST
    }

    protected String url;
    protected HttpMethod httpMethod = HttpMethod.GET;
    protected int scoketTimeout = 20000;
    protected int connectTimeout = 10000;

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public int getScoketTimeout() {
        return scoketTimeout;
    }

    public void setScoketTimeout(int scoketTimeout) {
        this.scoketTimeout = scoketTimeout;
    }
}