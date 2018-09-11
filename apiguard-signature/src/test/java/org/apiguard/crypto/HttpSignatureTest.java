package org.apiguard.crypto;

import org.apiguard.HttpSignature;
import org.apiguard.crypto.exception.CryptoException;
import org.junit.Assert;
import org.junit.Test;

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

/**
 Amazon ref: http://docs.aws.amazon.com/AmazonS3/latest/dev/RESTAuthentication.html
*/

public class HttpSignatureTest {

    @Test
    public void testSignHmacSha1() throws CryptoException {
        String signature = HttpSignature.signWithBase64("secret", "Hello world", Algorithm.HMAC_SHA1);
        Assert.assertEquals("CNeYKOidFU50rd+L/LZH53cL/po=", signature);
    }

    @Test
    public void testSignHmacMd5() throws CryptoException {
        String signature = HttpSignature.signWithBase64("secret", "Hello world", Algorithm.HMAC_MD5);
        Assert.assertEquals("P9Fi8eP6dh1uYY6mwaGe+w==", signature);
    }

    @Test
    public void testSignHmacSha256() throws CryptoException {
        String signature = HttpSignature.signWithBase64("secret", "Hello world", Algorithm.HMAC_SHA256);
        Assert.assertEquals("DVVI+3RQ5hmwdTclBocHUZ7UHNISsFALwgQn4+9m4I4=", signature);
    }

    @Test
    public void testSignHmacSha256Get() throws CryptoException {
        String stringToSign = "(request-target): get /items/1/inventory\n" +
                "date: Thu, 20 Apr 2017 14:01:19 PDT\n" +
                "digest: SHA-256=47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=";
        String signature = HttpSignature.signWithBase64("70335ca6-081f-11e7-93ae-92361f003251", stringToSign, Algorithm.HMAC_SHA256);
        Assert.assertEquals("bLIbFHUIMRA1JL27zVNxUSrc5NBH7vRmLs8lpZtz3ck=", signature);
    }

    @Test
    public void testSignHmacSha256Post() throws CryptoException {
        String stringToSign = "(request-target): post /events/test\n" +
                "date: Wed, 19 Apr 2017 10:15:54 PDT\n" +
                "digest: SHA-256=pHU9fx9WiQRRfc0aQFEZL+lo3pcJUSPil1a21kXn1s8=";
        String signature = HttpSignature.signWithBase64("9ab39dc0-b7f2-45d5-aab3-3ca9703e84r0", stringToSign, Algorithm.HMAC_SHA256);
        Assert.assertEquals("W8YtodmBJuaW+HBP0u643vUlmNemrMOQ5G9v+yuTaGI=", signature);
    }

    @Test
    public void testSignHmacSha1AmazonS3Get() throws CryptoException {
        String stringToSign = "GET\n" +
        "\n" +
        "\n" +
        "Tue, 27 Mar 2007 19:36:42 +0000\n" +
                "/johnsmith/photos/puppy.jpg";
        String signature = HttpSignature.signWithBase64("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY", stringToSign, Algorithm.HMAC_SHA1);
        Assert.assertEquals("bWq2s1WEIj+Ydj0vQ697zp+IXMU=", signature);
    }     
    
    @Test
    public void testSignHmacSha1AmazonS3Get2() throws CryptoException {
        String stringToSign = "GET\n" +
                "\n" +
                "\n" +
                "Wed, 28 Mar 2007 01:49:49 +0000\n" +
                "/dictionary/fran%C3%A7ais/pr%c3%a9f%c3%a8re";
        String signature = HttpSignature.signWithBase64("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY", stringToSign, Algorithm.HMAC_SHA1);
        Assert.assertEquals("DNEZGsoieTZ92F3bUfSPQcbGmlM=", signature);
    }    
    
    @Test
    public void testSignHmacSha1AmazonS3Put() throws CryptoException {
        String stringToSign = "PUT\n" +
                "\n" +
                "image/jpeg\n" +
                "Tue, 27 Mar 2007 21:15:45 +0000\n" +
                "/johnsmith/photos/puppy.jpg";
        String signature = HttpSignature.signWithBase64("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY", stringToSign, Algorithm.HMAC_SHA1);
        Assert.assertEquals("MyyxeRY7whkBe+bq8fHCL/2kKUg=", signature);
    }

    @Test
    public void testSignHmacSha1AmazonS3Put2() throws CryptoException {
        String stringToSign = "PUT\n" +
                "4gJE4saaMU4BqNR0kLY+lw==\n" +
                "application/x-download\n" +
                "Tue, 27 Mar 2007 21:06:08 +0000\n" +
                "\n" +
                "x-amz-acl:public-read\n" +
                "x-amz-meta-checksumalgorithm:crc32\n" +
                "x-amz-meta-filechecksum:0x02661779\n" +
                "x-amz-meta-reviewedby:\n" +
                "joe@johnsmith.net,jane@johnsmith.net\n" +
                "/static.johnsmith.net/db-backup.dat.gz";
        String signature = HttpSignature.signWithBase64("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY", stringToSign, Algorithm.HMAC_SHA1);
        Assert.assertEquals("B1MyVh4uYk5F8YFdQMVx2yJrOTA=", signature);
    }

    @Test
    public void testSignHmacSha1Hex() throws CryptoException {
        String signature = HttpSignature.signWithHex("secret", "Hello world", Algorithm.HMAC_SHA1);
        Assert.assertEquals("08d79828e89d154e74addf8bfcb647e7770bfe9a", signature);
    }

    @Test
    public void testSignHmacMd5Hex() throws CryptoException {
        String signature = HttpSignature.signWithHex("secret", "Hello world", Algorithm.HMAC_MD5);
        Assert.assertEquals("3fd162f1e3fa761d6e618ea6c1a19efb", signature);
    }

    @Test
    public void testSignHmacSha256Hex() throws CryptoException {
        String signature = HttpSignature.signWithHex("secret", "Hello world", Algorithm.HMAC_SHA256);
        Assert.assertEquals("0d5548fb7450e619b0753725068707519ed41cd212b0500bc20427e3ef66e08e", signature);
    }

    @Test
    public void testSignHmacSha256GetHex() throws CryptoException {
        String stringToSign = "(request-target): get /items/1/inventory\n" +
                "date: Thu, 20 Apr 2017 14:01:19 PDT\n" +
                "digest: SHA-256=47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=";
        String signature = HttpSignature.signWithHex("70335ca6-081f-11e7-93ae-92361f003251", stringToSign, Algorithm.HMAC_SHA256);
        Assert.assertEquals("6cb21b14750831103524bdbbcd5371512adce4d047eef4662ecf25a59b73ddc9", signature);
    }

    @Test
    public void testSignHmacSha256PostHex() throws CryptoException {
        String stringToSign = "(request-target): post /events/test\n" +
                "date: Wed, 19 Apr 2017 10:15:54 PDT\n" +
                "digest: SHA-256=pHU9fx9WiQRRfc0aQFEZL+lo3pcJUSPil1a21kXn1s8=";
        String signature = HttpSignature.signWithHex("9ab39dc0-b7f2-45d5-aab3-3ca9703e84r0", stringToSign, Algorithm.HMAC_SHA256);
        Assert.assertEquals("5bc62da1d98126e696f8704fd2eeb8def52598d7a6acc390e46f6ffb2b936862", signature);
    }

    @Test
    public void testSignHmacSha1AmazonS3GetHex() throws CryptoException {
        String stringToSign = "GET\n" +
        "\n" +
        "\n" +
        "Tue, 27 Mar 2007 19:36:42 +0000\n" +
                "/johnsmith/photos/puppy.jpg";
        String signature = HttpSignature.signWithHex("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY", stringToSign, Algorithm.HMAC_SHA1);
        Assert.assertEquals("6d6ab6b35584223f98763d2f43af7bce9f885cc5", signature);
    }

    @Test
    public void testSignHmacSha1AmazonS3Get2Hex() throws CryptoException {
        String stringToSign = "GET\n" +
                "\n" +
                "\n" +
                "Wed, 28 Mar 2007 01:49:49 +0000\n" +
                "/dictionary/fran%C3%A7ais/pr%c3%a9f%c3%a8re";
        String signature = HttpSignature.signWithHex("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY", stringToSign, Algorithm.HMAC_SHA1);
        Assert.assertEquals("0cd1191aca2279367dd85ddb51f48f41c6c69a53", signature);
    }

    @Test
    public void testSignHmacSha1AmazonS3PutHex() throws CryptoException {
        String stringToSign = "PUT\n" +
                "\n" +
                "image/jpeg\n" +
                "Tue, 27 Mar 2007 21:15:45 +0000\n" +
                "/johnsmith/photos/puppy.jpg";
        String signature = HttpSignature.signWithHex("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY", stringToSign, Algorithm.HMAC_SHA1);
        Assert.assertEquals("332cb179163bc219017be6eaf1f1c22ffda42948", signature);
    }

    @Test
    public void testSignHmacSha1AmazonS3Put2Hex() throws CryptoException {
        String stringToSign = "PUT\n" +
                "4gJE4saaMU4BqNR0kLY+lw==\n" +
                "application/x-download\n" +
                "Tue, 27 Mar 2007 21:06:08 +0000\n" +
                "\n" +
                "x-amz-acl:public-read\n" +
                "x-amz-meta-checksumalgorithm:crc32\n" +
                "x-amz-meta-filechecksum:0x02661779\n" +
                "x-amz-meta-reviewedby:\n" +
                "joe@johnsmith.net,jane@johnsmith.net\n" +
                "/static.johnsmith.net/db-backup.dat.gz";
        String signature = HttpSignature.signWithHex("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY", stringToSign, Algorithm.HMAC_SHA1);
        Assert.assertEquals("075332561e2e624e45f1815d40c571db226b3930", signature);
    }
}
