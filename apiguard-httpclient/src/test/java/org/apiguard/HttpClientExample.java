package org.apiguard;

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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apiguard.http.ApiGuardApacheHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class HttpClientExample {

    private String cookies;
    private HttpClient client = HttpClientBuilder.create().build();
    private static final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        String url = "http://www.google.com/search?q=httpClient";


        ApiGuardApacheHttpClient httpClient = new ApiGuardApacheHttpClient();
        httpClient.setup();
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("User-Agent", USER_AGENT);
        headers.put("postman-token", "87660015-96bd-dc5e-05ad-c86f31032bf7");
        // cause 404
//        headers.put("host","localhost:8080");
        HttpResponse response = httpClient.get("https://www.google.com/search", "q=httpClient", headers);

        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result);
    }
}
