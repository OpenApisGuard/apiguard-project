package org.apiguard;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apiguard.crypto.Algorithm;
import org.apiguard.crypto.exception.CryptoException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

public class HttpSignature {

    /**
     * Create a base64 HMAC signature
     * @param secret
     * @param stringToSign
     * @param algorithm
     * @return
     */
    public static String signWithBase64(String secret, String stringToSign, Algorithm algorithm) throws CryptoException {
        try {
            if (algorithm.getRefClass().getName().equals(Mac.class.getName())) {
                byte[] data = getSignBytes(secret, stringToSign, algorithm);
                return Base64.encodeBase64String(data);
            }
            else {
                throw new CryptoException("Unsupported algorithm: " + algorithm.getId());
            }
        }
        catch (Exception e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }


    public static String signWithHex(String secret, String stringToSign, Algorithm algorithm) throws CryptoException {
        try {
            if (algorithm.getRefClass().getName().equals(Mac.class.getName())) {
                byte[] data = getSignBytes(secret, stringToSign, algorithm);
                return Hex.encodeHexString(data);
            }
            else {
                throw new CryptoException("Unsupported algorithm: " + algorithm.getId());
            }
        }
        catch (Exception e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    private static byte[] getSignBytes(String secret, String stringToSign, Algorithm algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Mac hmac = Mac.getInstance(algorithm.getId());
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes("UTF-8"), algorithm.getId());
        hmac.init(secret_key);

        return hmac.doFinal(stringToSign.getBytes("UTF-8"));
    }
}
