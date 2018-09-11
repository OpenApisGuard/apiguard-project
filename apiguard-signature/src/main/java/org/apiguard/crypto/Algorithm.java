package org.apiguard.crypto;

import javax.crypto.Mac;
import java.security.Signature;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

public enum Algorithm {
    HMAC_SHA1("HmacSHA1", "hmac-sha1", Mac.class),
    HMAC_SHA224("HmacSHA224", "hmac-sha224", Mac.class),
    HMAC_SHA256("HmacSHA256", "hmac-sha256", Mac.class),
    HMAC_SHA384("HmacSHA384", "hmac-sha384", Mac.class),
    HMAC_SHA512("HmacSHA512", "hmac-sha512", Mac.class),
    HMAC_MD5("HmacMD5", "hmac-md5", Mac.class),
    RSA_MD5("MD5withRSA", "rsa-md5", Signature.class),
    RSA_SHA1("SHA1withRSA", "rsa-sha1", Signature.class),
    RSA_SHA256("SHA256withRSA", "rsa-sha256", Signature.class),
    RSA_SHA384("SHA384withRSA", "rsa-sha384", Signature.class),
    RSA_SHA512("SHA512withRSA", "rsa-sha512", Signature.class),
    DSA_SHA1("SHA1withDSA", "dsa-sha1", Signature.class),
    DSA_SHA224("SHA224withDSA", "dsa-sha224", Signature.class),
    DSA_SHA256("SHA256withDSA", "dsa-sha256", Signature.class);

    private String id;
    private String name;
    private Class refClass;

    private Algorithm(String id, String name, Class refClass) {
        this.id = id;
        this.name = name;
        this.refClass = refClass;
    }

    private static Map<String, Algorithm> idMap;
    private static Map<String, Algorithm> nameMap;

    static {
        final Algorithm[] values = Algorithm.values();
        final Map<String, Algorithm> idEnumMap = new HashMap<String, Algorithm>();
        final Map<String, Algorithm> nameEnumMap = new HashMap<String, Algorithm>();
        final Map<Class, Algorithm> refEnumClassMap = new HashMap<Class, Algorithm>();

        for (final Algorithm cur : values) {
            idEnumMap.put(cur.getId(), cur);
            nameEnumMap.put(cur.getName(), cur);
            refEnumClassMap.put(cur.getRefClass(), cur);

        }
        idMap = Collections.unmodifiableMap(idEnumMap);
        nameMap = Collections.unmodifiableMap(nameEnumMap);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Class getRefClass() {
        return refClass;
    }

    public static Algorithm getAlgorithmById(final Integer id) {
        return idMap.get(id);
    }

    public static Algorithm getAlgorithmByName(final String name) {
        return nameMap.get(name);
    }
}
