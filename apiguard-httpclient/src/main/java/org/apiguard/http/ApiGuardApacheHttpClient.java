package org.apiguard.http;

import org.apache.cxf.common.util.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.util.concurrent.TimeUnit;

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
    public void setup() {
        // setup a Trust Strategy that allows all certificates.

        try {
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

            // start deamon thread for clean up
            Thread t = new Thread(new IdleConnectionMonitorThread(cm));
            t.setDaemon(true);//success is here now
            t.start();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity get(String url, String query, Map<String, String> headers)
            throws HttpClientException {
        HttpResponse response = null;
        try {
            StringBuilder requestUrl = prepareRequest(url, query);

            HttpGet request = new HttpGet(requestUrl.toString());
            if (headers != null && headers.size() > 0) {
                Set<String> keys = headers.keySet();
                for(String k : keys) {
                    request.setHeader(k, headers.get(k));
                }
            }

            response = httpClient.execute(request);
            return constructResp(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new HttpClientException(e);
        }
        finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    public ResponseEntity post(String url, String query, HashMap<String, String> headers, String body)
            throws HttpClientException {

        HttpResponse response = null;
        try {
            StringBuilder urlSb = prepareRequest(url, query);
            response = execute(urlSb.toString(), METHOD_POST, headers, body);
            return constructResp(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new HttpClientException(e);
        }
        finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    public ResponseEntity put(String url, String query, HashMap<String, String> headers, String body)
            throws HttpClientException {

        HttpResponse response = null;
        try {
            StringBuilder urlSb = prepareRequest(url, query);
            response = execute(urlSb.toString(), METHOD_PUT, headers, body);
            return constructResp(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new HttpClientException(e);
        }
        finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    public ResponseEntity delete(String url, String query, HashMap<String, String> headers, String body)
            throws HttpClientException {

        HttpResponse response = null;
        try {
            StringBuilder urlSb = prepareRequest(url, query);
            response = execute(urlSb.toString(), METHOD_DELETE, headers, body);
            return constructResp(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new HttpClientException(e);
        }
        finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    private HttpResponse execute(final String uri, final String method, HashMap<String, String> headers, String body) throws IOException, URISyntaxException {
        log.info("Executing: " + method + " " + uri);
        if (log.isDebugEnabled()) {
            log.debug("*** Headers: " + headers.toString() + " , body: " + body);
        }

        HttpEntityEnclosingRequestBase httpReq = new HttpEntityEnclosingRequestBase() {
            @Override
            public String getMethod() {
                return method;
            }
        };

        httpReq.setURI(new URI(uri));
        prepareRequest(uri, headers, body, httpReq);

        HttpResponse resp = httpClient.execute(httpReq);
        log.info("Executed: " + method + " " + uri + " , status: " + resp.getStatusLine().getStatusCode());

        return resp;
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

    private ResponseEntity constructResp(HttpResponse resp) throws HttpClientException, IOException {
        log.debug("Resp code: " + resp.getStatusLine().getStatusCode());
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpEntity entity = resp.getEntity();
        String mimeType = "";
        String respStr = "";
        if (entity != null) {
            ContentType contentType = ContentType.getOrDefault(entity);
            mimeType = contentType.getMimeType();
            responseHeaders.setContentType(MediaType.valueOf(mimeType));
            respStr = EntityUtils.toString(entity);
        }

        if (mimeType.contains("pdf")) {
            //TODO: support pdf later
            return new ResponseEntity<String>(respStr, responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(respStr, responseHeaders, HttpStatus.OK);
        }
    }

    public class IdleConnectionMonitorThread extends Thread {

        private final HttpClientConnectionManager connMgr;
        private volatile boolean shutdown;

        public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
            super();
            this.connMgr = connMgr;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(5000);
                        // Close expired connections
                        connMgr.closeExpiredConnections();
                        // Optionally, close connections
                        // that have been idle longer than 30 sec
                        connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                    }
                }
            } catch (InterruptedException ex) {
                // terminate
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }

}
