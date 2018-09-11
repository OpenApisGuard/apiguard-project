package org.apiguard.cassandra.entity;

import org.apiguard.entity.JwtAuth;
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

@Table("jwtAuth")
public class JwtAuthEntity extends BaseEntity implements JwtAuth {

    @PrimaryKey
    private String issuer;

    private String clientId;

    private String secret;

    private String reqUri;

    // nbf- identifies the time before which the JWT must not be accepted for processing.
    private boolean notBefore;

    // exp - identifies the expiration time on or after which the JWT must not be accepted for processing.
    private boolean expires;

    public JwtAuthEntity() {
    }

    public JwtAuthEntity(String id, Date creationDate, Date lastUpdateDate, String reqUri, String clientId, String group,
                         String secret) {
        super(id, creationDate, lastUpdateDate);
        this.issuer = id;
        this.secret = secret;
        this.clientId = clientId;
        this.reqUri = reqUri;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getClientId() {
        return clientId;
    }

    public String getReqUri() {
        return reqUri;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public boolean isNotBefore() {
        return notBefore;
    }

    public void setNotBefore(boolean notBefore) {
        this.notBefore = notBefore;
    }

    public boolean isExpires() {
        return expires;
    }

    public void setExpires(boolean expires) {
        this.expires = expires;
    }
}
