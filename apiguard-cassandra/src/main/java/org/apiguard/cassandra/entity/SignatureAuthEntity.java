package org.apiguard.cassandra.entity;

import org.apiguard.entity.SignatureAuth;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.Date;

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

@Table("signatureAuth")
public class SignatureAuthEntity extends BaseEntity implements SignatureAuth {

    @PrimaryKey
    private SignatureAuthId pk;

    private String secret;

    private String decryptedSecret;

    public SignatureAuthEntity() {
    }

    public SignatureAuthEntity(String id, Date creationDate, Date lastUpdateDate, String reqUri, String clientId, String clientAlias,
                               String secret) {
        super(id, creationDate, lastUpdateDate);
        pk = new SignatureAuthId(reqUri, clientId, clientAlias);
        this.secret = secret;
    }

    public SignatureAuthId getPk() {
        return pk;
    }

    public void setPk(SignatureAuthId pk) {
        this.pk = pk;
    }

    public String getClientId() {
        return pk.getClientId();
    }

    public String getReqUri() {
        return pk.getReqUri();
    }

    public String getClientAlias() {
        return pk.getClientAlias();
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getDecryptedSecret() {
        return decryptedSecret;
    }

    public void setDecryptedSecret(String decryptedSecret) {
        this.decryptedSecret = decryptedSecret;
    }
}
