package org.apiguard.crypto;

//import org.apache.commons.codec.binary.Base64;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;

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
public class Test {
    public static String hashValue(String message, String key) {
        byte[] hash = toHmacSHA256(message, key);
        String hashHexed = toHex(hash);
        return hashHexed;
    }

    private static String toHex(byte[] value) {
        String hexed = String.format("%040x", new BigInteger(1, value));
        return hexed;
    }

    private static byte[] toHmacSHA256(String value, String key) {
        byte[] hash = null;
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            hash = mac.doFinal(value.getBytes("UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hash;
    }

    public static String getHash(String message, String secret) throws Exception{
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);


        byte[] byteData = sha256_HMAC.doFinal(message.getBytes("UTF-8"));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            String secret = "apiguard_sys_user_20171004:98356c60-75bd-43db-8b54-4a1c61aafa57";
            String message = "http://localhost:8080/apiguard/apis/health 2017-10-12 03:45:27pm ";

            System.out.println(Base64.encodeBase64String(getHash(message, secret).getBytes()));
            System.out.println(hashValue(message, secret));
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

}
