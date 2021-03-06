package org.apiguard.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
public abstract class BaseFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(BaseFilter.class);

    public void logEvent(HttpServletRequest httpServletRequest) {
        try {
            final StringBuilder logMessage = new StringBuilder("REST Request - ")
                    .append("[HTTP METHOD: ")
                    .append(httpServletRequest.getMethod())
                    .append("] [PATH INFO: ")
                    .append(httpServletRequest.getRequestURI())
//                    .append(httpServletRequest.getPathInfo())
                    .append("] [REMOTE ADDRESS: ")
                    .append(httpServletRequest.getRemoteAddr())
                    .append("]");

            if (logger.isDebugEnabled()) {
                BufferedRequestWrapper bufferedReqest = new BufferedRequestWrapper(httpServletRequest);
                Map<String, String> requestMap = this.getTypesafeRequestMap(httpServletRequest);
                logMessage.append("[REQUEST HEADERS: ")
                        .append(getHeaders(httpServletRequest))
                        .append("[REQUEST PARAMETERS: ")
                        .append(requestMap)
                        .append("] [REQUEST BODY: ")
                        .append(bufferedReqest.getRequestBody())
                        .append("]");
            }

            logger.info(logMessage.toString());
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void logEvent(HttpServletResponse httpServletResponse) {
        try {
            if (logger.isDebugEnabled()) {
                BufferedResponseWrapper bufferedResponse = new BufferedResponseWrapper(httpServletResponse);
                logger.debug("[RESPONSE: " + bufferedResponse.getContent() + "]");
            }
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private String getHeaders(HttpServletRequest httpServletRequest) {
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();

        if (headerNames != null) {
            StringBuilder sb = new StringBuilder();
            while (headerNames.hasMoreElements()) {
                String cur = headerNames.nextElement();
                sb.append("[");
                sb.append(cur);
                sb.append(" - ");
                sb.append(httpServletRequest.getHeader(cur));
                sb.append("]");
            }
            return sb.toString();
        }
        return "[]";
    }

    private Map<String, String> getTypesafeRequestMap(HttpServletRequest request) {
        Map<String, String> typesafeRequestMap = new HashMap<String, String>();
        Enumeration<?> requestParamNames = request.getParameterNames();
        while (requestParamNames.hasMoreElements()) {
            String requestParamName = (String)requestParamNames.nextElement();
            String requestParamValue = request.getParameter(requestParamName);
            typesafeRequestMap.put(requestParamName, requestParamValue);
        }
        return typesafeRequestMap;
    }

    private static final class BufferedRequestWrapper extends HttpServletRequestWrapper {

        private ByteArrayInputStream bais = null;
        private ByteArrayOutputStream baos = null;
        private BufferedServletInputStream bsis = null;
        private byte[] buffer = null;


        public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
            super(req);
            // Read InputStream and store its content in a buffer.
            InputStream is = req.getInputStream();
            this.baos = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int letti;
            while ((letti = is.read(buf)) > 0) {
                this.baos.write(buf, 0, letti);
            }
            this.buffer = this.baos.toByteArray();
        }


        @Override
        public ServletInputStream getInputStream() {
            this.bais = new ByteArrayInputStream(this.buffer);
            this.bsis = new BufferedServletInputStream(this.bais);
            return this.bsis;
        }



        String getRequestBody() throws IOException  {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.getInputStream()));
            String line = null;
            StringBuilder inputBuffer = new StringBuilder();
            do {
                line = reader.readLine();
                if (null != line) {
                    inputBuffer.append(line.trim());
                }
            } while (line != null);
            reader.close();
            return inputBuffer.toString().trim();
        }

    }

    private static final class BufferedServletInputStream extends ServletInputStream {

        private ByteArrayInputStream bais;

        public BufferedServletInputStream(ByteArrayInputStream bais) {
            this.bais = bais;
        }

        @Override
        public int available() {
            return this.bais.available();
        }

        @Override
        public int read() {
            return this.bais.read();
        }

        @Override
        public int read(byte[] buf, int off, int len) {
            return this.bais.read(buf, off, len);
        }

//        @Override
        public boolean isFinished() {
            return bais.available() == 0;
        }

//        @Override
        public boolean isReady() {
            return true;
        }

//        @Override
        public void setReadListener(ReadListener listener) {
            throw new RuntimeException("Not implemented");
        }
    }

    public class TeeServletOutputStream extends ServletOutputStream {

        private final TeeOutputStream targetStream;

        public TeeServletOutputStream( OutputStream one, OutputStream two ) {
            targetStream = new TeeOutputStream( one, two);
        }

        @Override
        public void write(int arg0) throws IOException {
            this.targetStream.write(arg0);
        }

        public void flush() throws IOException {
            super.flush();
            this.targetStream.flush();
        }

        public void close() throws IOException {
            super.close();
            this.targetStream.close();
        }

//        @Override
        public boolean isReady() {
            return false;
        }

//        @Override
        public void setWriteListener(WriteListener writeListener) {

        }

    }

    private static class TeeOutputStream extends OutputStream
    {
        private OutputStream mChainStream;
        private OutputStream mTeeStream;

        public TeeOutputStream(OutputStream chainStream, OutputStream teeStream)
        {
            mChainStream = chainStream;
            mTeeStream = teeStream;
        }

        @Override
        public void write(int b) throws IOException
        {
            mChainStream.write(b);
            mTeeStream.write(b);
            mTeeStream.flush();
        }

        @Override
        public void close() throws IOException
        {
            flush();
            mChainStream.close();
            mTeeStream.close();
        }

        @Override
        public void flush() throws IOException
        {
            mChainStream.close();
        }
    }

    public class BufferedResponseWrapper implements HttpServletResponse {

        HttpServletResponse original;
        TeeServletOutputStream tee;
        ByteArrayOutputStream bos;

        public BufferedResponseWrapper(HttpServletResponse response) {
            original = response;
        }

        public String getContent() {
            return bos.toString();
        }

        public PrintWriter getWriter() throws IOException {
            return original.getWriter();
        }

        public ServletOutputStream getOutputStream() throws IOException {
            if( tee == null ){
                bos = new ByteArrayOutputStream();
                tee = new TeeServletOutputStream( original.getOutputStream(), bos );
            }
            return tee;

        }

        @Override
        public String getCharacterEncoding() {
            return original.getCharacterEncoding();
        }

        @Override
        public String getContentType() {
            return original.getContentType();
        }

        @Override
        public void setCharacterEncoding(String charset) {
            original.setCharacterEncoding(charset);
        }

        @Override
        public void setContentLength(int len) {
            original.setContentLength(len);
        }

        @Override
        public void setContentLengthLong(long len) {
            original.setContentLengthLong(len);
        }

        @Override
        public void setContentType(String type) {
            original.setContentType(type);
        }

        @Override
        public void setBufferSize(int size) {
            original.setBufferSize(size);
        }

        @Override
        public int getBufferSize() {
            return original.getBufferSize();
        }

        @Override
        public void flushBuffer() throws IOException {
            tee.flush();
        }

        @Override
        public void resetBuffer() {
            original.resetBuffer();
        }

        @Override
        public boolean isCommitted() {
            return original.isCommitted();
        }

        @Override
        public void reset() {
            original.reset();
        }

        @Override
        public void setLocale(Locale loc) {
            original.setLocale(loc);
        }

        @Override
        public Locale getLocale() {
            return original.getLocale();
        }

        @Override
        public void addCookie(Cookie cookie) {
            original.addCookie(cookie);
        }

        @Override
        public boolean containsHeader(String name) {
            return original.containsHeader(name);
        }

        @Override
        public String encodeURL(String url) {
            return original.encodeURL(url);
        }

        @Override
        public String encodeRedirectURL(String url) {
            return original.encodeRedirectURL(url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String encodeUrl(String url) {
            return original.encodeUrl(url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String encodeRedirectUrl(String url) {
            return original.encodeRedirectUrl(url);
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            original.sendError(sc, msg);
        }

        @Override
        public void sendError(int sc) throws IOException {
            original.sendError(sc);
        }

        @Override
        public void sendRedirect(String location) throws IOException {
            original.sendRedirect(location);
        }

        @Override
        public void setDateHeader(String name, long date) {
            original.setDateHeader(name, date);
        }

        @Override
        public void addDateHeader(String name, long date) {
            original.addDateHeader(name, date);
        }

        @Override
        public void setHeader(String name, String value) {
            original.setHeader(name, value);
        }

        @Override
        public void addHeader(String name, String value) {
            original.addHeader(name, value);
        }

        @Override
        public void setIntHeader(String name, int value) {
            original.setIntHeader(name, value);
        }

        @Override
        public void addIntHeader(String name, int value) {
            original.addIntHeader(name, value);
        }

        @Override
        public void setStatus(int sc) {
            original.setStatus(sc);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void setStatus(int sc, String sm) {
            original.setStatus(sc, sm);
        }

        @Override
        public Collection<String> getHeaderNames() {
            return original.getHeaderNames();
        }

        @Override
        public int getStatus() {
            return original.getStatus();
        }

        @Override
        public String getHeader(String header) {
            return original.getHeader(header);
        }

        public Collection<String> getHeaders(String headers) {
            return original.getHeaders(headers);
        }
    }
}
