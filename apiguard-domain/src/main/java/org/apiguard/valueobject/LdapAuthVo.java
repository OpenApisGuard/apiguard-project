package org.apiguard.valueobject;

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

public class LdapAuthVo extends BaseRestResource {

	private static final long serialVersionUID = 1L;

	private String clientId;

	private String reqUri;

	private String ldapUrl;

	private String adminDn;

	private String adminPassword;

	private String userBase;

	private String userAttr;

	private Integer cacheExpireInSecond;

    private Date lastLoginDate;

	public LdapAuthVo(String id, String creationDate, String lastUpdateDate, String clientId, String reqUri, String ldapUrl,
					  String adminDn, String adminPassword, String userBase, String userAttr, Integer cacheExpireInSecond) {
		super(id, creationDate, lastUpdateDate);
		this.clientId = clientId;
		this.reqUri = reqUri;
		this.ldapUrl = ldapUrl;
		this.adminDn = adminDn;
		this.adminPassword = adminPassword;
		this.userBase = userBase;
		this.userAttr = userAttr;
		this.cacheExpireInSecond = cacheExpireInSecond;
	}

	public String getClientId() {
		return clientId;
	}

	public String getReqUri() {
		return reqUri;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getLdapUrl() {
		return ldapUrl;
	}

	public String getAdminDn() {
		return adminDn;
	}

	public String getAdminPassword() {
		return "******";
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
}
