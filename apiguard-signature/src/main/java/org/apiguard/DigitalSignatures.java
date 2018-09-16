package org.apiguard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apiguard.crypto.Algorithm;
import org.apiguard.crypto.exception.CryptoException;

import java.io.File;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;



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

public class DigitalSignatures {

    private static final Logger log = LogManager.getLogger(DigitalSignatures.class);

    public static String sign(String privateKeyFile, String data, Algorithm algorithm) throws CryptoException {
        return sign(privateKeyFile, null, data, algorithm);
    }

    private static String sign(String privateKeyFile, String provider, String data, Algorithm algorithm) throws CryptoException {
        try {
            if (algorithm.getRefClass().getName().equals(Signature.class.getName())) {
                byte[] keyBytes = Files.readAllBytes(new File(privateKeyFile).toPath());
                X509EncodedKeySpec privateKeySpec = new X509EncodedKeySpec(keyBytes);

                String type = algorithm.getName().startsWith("dsa") ? "DSA" : "RSA";
                KeyFactory keyFactory = null;
                if (provider == null) {
                    keyFactory = KeyFactory.getInstance(type);
                }
                else {
                    keyFactory = KeyFactory.getInstance(type, provider);
                }
                PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

                /* create a Signature object and initialize it with the public key */
                Signature sig = null;
                if (provider == null) {
                    sig = Signature.getInstance(algorithm.getId());
                }
                else {
                    sig = Signature.getInstance(algorithm.getId(), provider);
                }
                sig.initSign(privateKey, new SecureRandom());
                sig.update(data.getBytes());
                return new String(sig.sign());
            }
            else {
                log.warn("Unsupported algorithm: " + algorithm.getId());
                throw new CryptoException("Unsupported algorithm: " + algorithm.getId());
            }
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new CryptoException(e.getMessage(), e);
        }

    }

    public static boolean verify(String publicKeyFile, String signature, String data, Algorithm algorithm) throws CryptoException {
        return verify(publicKeyFile, null, signature, data, algorithm);
    }

    public static boolean verify(String publicKeyFile, String provider, String signature, String data, Algorithm algorithm) throws CryptoException {
        try {
            if (algorithm.getRefClass().getName().equals(Signature.class.getName())) {
                byte[] keyBytes = Files.readAllBytes(new File(publicKeyFile).toPath());
                X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(keyBytes);

                String type = algorithm.getName().startsWith("dsa") ? "DSA" : "RSA";
                KeyFactory keyFactory = null;
                if (provider == null) {
                    keyFactory = KeyFactory.getInstance(type);
                }
                else {
                    keyFactory = KeyFactory.getInstance(type, provider);
                }
                PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

                byte[] sigToVerify = signature.getBytes();

                /* create a Signature object and initialize it with the public key */
                Signature sig = null;
                if (provider == null) {
                    sig = Signature.getInstance(algorithm.getId());
                }
                else {
                    sig = Signature.getInstance(algorithm.getId(), provider);
                }
                sig.initVerify(pubKey);

                /* Update and verify the data */
                sig.update(data.getBytes());
                return sig.verify(sigToVerify);
            }
            else {
                log.warn("Unsupported algorithm: " + algorithm.getId());
                throw new CryptoException("Unsupported algorithm: " + algorithm.getId());
            }
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new CryptoException(e.getMessage(), e);
        }
    }
}
