package org.apiguard.cassandra.entity;

import org.apiguard.entity.LdapAuth;
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

@Table("ldapAuth")
public class LdapAuthEntity extends BaseEntity implements LdapAuth {

    @PrimaryKey
    private LdapAuthId pk;

    private String ldapUrl;

    private String adminDn;

    private String adminPassword;

    private String userBase;

    private String userAttr;

    private Integer cacheExpireInSecond;

    private String token;

    private Date lastLoginDate;

    public LdapAuthEntity() {
    }

    public LdapAuthEntity(String id, Date creationDate, Date lastUpdateDate, String reqUri, String clientId, String group, String ldapUrl,
                          String adminDn, String adminPassword, String userBase, String userAttr, Integer cacheExpireInSecond) {
        super(id, creationDate, lastUpdateDate);
        pk = new LdapAuthId(reqUri, clientId, group);
        this.ldapUrl = ldapUrl;
        this.adminDn = adminDn;
        this.adminPassword = adminPassword;
        this.userBase = userBase;
        this.userAttr = userAttr;
        this.cacheExpireInSecond = cacheExpireInSecond;
    }

    public LdapAuthId getPk() {
        return pk;
    }

    public String getClientId() {
        return pk.getClientId();
    }

    public String getReqUri() {
        return pk.getReqUri();
    }

    public String getLdapUrl() {
        return ldapUrl;
    }

    public String getAdminDn() {
        return adminDn;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public String getUserBase() {
        return userBase;
    }

    public String getUserAttr() {
        return userAttr;
    }

    public Integer getCacheExpireInSecond() {
        return cacheExpireInSecond;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
