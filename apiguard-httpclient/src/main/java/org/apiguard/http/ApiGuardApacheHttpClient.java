package org.apiguard.http;

import org.apache.cxf.common.util.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@Component
public class ApiGuardApacheHttpClient {
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";

    public static final String HEADER_CONTENT_TYPE = "Content-Type";

    private static final int DEFAULT_MAX_CONNECTIONS = 1000;
    private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 50;
    private static final int DEFAULT_MAX_INACTIVITY_MS = 60000;
    private static final int DEFAULT_REQ_CONNECT_TIMEOUT_MS = 120000;
    private static final int DEFAULT_SOCKET_TIMEOUT_MS = 120000;

    private static final Logger log = LogManager.getLogger(ApiGuardApacheHttpClient.class);
    private static HttpClient httpClient;

    @Value("${httpclient.max.connections}")
    private Integer maxConnections = DEFAULT_MAX_CONNECTIONS;

    @Value("${httpclient.max.connections.per.route}")
    private Integer maxConnectionsPerRoute = DEFAULT_MAX_CONNECTIONS_PER_ROUTE;

    @Value("${httpclient.max.inactivity.ms}")
    private Integer maxInactivityMs = DEFAULT_MAX_INACTIVITY_MS;

    @Value("${httpclient.max.socket.connection.timeout.ms}")
    private Integer maxRequestConnectTimeoutMs = DEFAULT_REQ_CONNECT_TIMEOUT_MS;

    @Value("${httpclient.max.socket.so.timeout.ms}")
    private Integer maxSocketTimeoutMs = DEFAULT_SOCKET_TIMEOUT_MS;

    @PostConstruct
    public void setup() throws Exception {
        // setup a Trust Strategy that allows all certificates.
        //
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();

        // don't check Hostnames, either.
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;

        // here's the special part:
        //      -- need to create an SSL Socket Factory, to use our weakened "trust strategy";
        //      -- and create a Registry, to register it.
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(maxConnections);
        cm.setDefaultMaxPerRoute(maxConnectionsPerRoute);
        cm.setValidateAfterInactivity(maxInactivityMs);

        RequestConfig.Builder requestBuilder = RequestConfig.custom()
                .setConnectTimeout(maxRequestConnectTimeoutMs)
                .setConnectionRequestTimeout(maxRequestConnectTimeoutMs)
                .setSocketTimeout(maxSocketTimeoutMs);

        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setDefaultRequestConfig(requestBuilder.build());
        builder.setConnectionManager(cm);
        builder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));

        httpClient = builder.build();
    }

    public HttpResponse get(String url, String query, Map<String, String> headers)
            throws HttpClientException {
        try {
            StringBuilder requestUrl = prepareRequest(url, query);

            HttpGet request = new HttpGet(requestUrl.toString());
            if (headers != null && headers.size() > 0) {
                Set<String> keys = headers.keySet();
                for(String k : keys) {
                    request.setHeader(k, headers.get(k));
                }
            }

            return httpClient.execute(request);
        } catch (Exception e) {
            throw new HttpClientException(e);
        }
    }

    public HttpResponse post(String url, String query, HashMap<String, String> headers, String body)
            throws HttpClientException {

        try {
            StringBuilder urlSb = prepareRequest(url, query);
            return execute(urlSb.toString(), METHOD_POST, headers, body);
        } catch (Exception e) {
            throw new HttpClientException(e);
        }
    }

    public HttpResponse put(String url, String query, HashMap<String, String> headers, String body)
            throws HttpClientException {

        try {
            StringBuilder urlSb = prepareRequest(url, query);
            return execute(urlSb.toString(), METHOD_PUT, headers, body);
        } catch (Exception e) {
            throw new HttpClientException(e);
        }
    }

    public HttpResponse delete(String url, String query, HashMap<String, String> headers, String body)
            throws HttpClientException {

        try {
            StringBuilder urlSb = prepareRequest(url, query);
            return execute(urlSb.toString(), METHOD_DELETE, headers, body);
        } catch (Exception e) {
            throw new HttpClientException(e);
        }
    }

    private HttpResponse execute(final String uri, final String method, HashMap<String, String> headers, String body) throws IOException, URISyntaxException {
        HttpEntityEnclosingRequestBase httpReq = new HttpEntityEnclosingRequestBase() {
            @Override
            public String getMethod() {
                return method;
            }
        };

        httpReq.setURI(new URI(uri));
        prepareRequest(uri, headers, body, httpReq);

        return httpClient.execute(httpReq);
    }

    private void prepareRequest(String uri, HashMap<String, String> headers, String body, HttpEntityEnclosingRequestBase httpReq) throws IOException {
        if (!StringUtils.isEmpty(body)) {
            @SuppressWarnings("Since15") HttpEntity entity = new ByteArrayEntity(body.getBytes(Consts.UTF_8));
            httpReq.setEntity(entity);
        }

        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for(String k : keys) {
                httpReq.setHeader(k, headers.get(k));
            }
        }

        log.debug("Http request: " + uri + " - " + body);
    }

    private StringBuilder prepareRequest(String url, String query) {
            StringBuilder requestUrl = new StringBuilder(url);

            if (!StringUtils.isEmpty(query)) {
                requestUrl.append("?");
                requestUrl.append(query);
            }

            log.debug(requestUrl.toString());

            return requestUrl;
        }

    private StringBuilder prepareRequest(String url, Map<String, String> params) {
        String querystring = null;

        if (params != null && params.size() > 0) {
            List<BasicNameValuePair> postParameters = new ArrayList<BasicNameValuePair>();
            Set<String> keys = params.keySet();
            for(String k : keys) {
                postParameters.add(new BasicNameValuePair(k, params.get(k)));
            }
            querystring = URLEncodedUtils.format(postParameters, Consts.UTF_8);
        }

        StringBuilder requestUrl = new StringBuilder(url);
        if (querystring != null) {
            requestUrl.append("?");
            requestUrl.append(querystring);
        }

        log.debug(requestUrl.toString());

        return requestUrl;
    }
}
