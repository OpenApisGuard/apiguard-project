package org.apiguard.cassandra.entity;

import org.apiguard.entity.Api;
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

/*
 * CREATE TABLE apiguard.apis (
 *	    id uuid,
 *	    name text,
 *	    reqUri text,
 *	    downstreamUri text,
 *	    creationDate timestamp,
 *      ...,
 *	    PRIMARY KEY (reqUri)
 *	)
 */

@Table("api")
public class ApiEntity extends BaseEntity implements Api {

	@PrimaryKey
	private String reqUri;

	private String name;

	private String downstreamUri;

	private boolean isAuthRequired;

	private boolean isBasicAuth;

	private boolean isKeyAuth;
	
	private boolean isHmacAuth;
	
	private boolean isOAuth2Auth;
	
	private boolean isJwtAuth;
	
	private boolean isLdapAuth;

	private boolean isSignatureAuth;

	private boolean isDigitalAuth;

	public ApiEntity(String id, Date creationDate, Date lastUpdateDate, String name, String reqUri, String downstreamUri) {
		super(id, creationDate, lastUpdateDate);
		this.reqUri = reqUri;
		this.name = name;
		this.downstreamUri = downstreamUri;
	}

	public String getReqUri() {
		return reqUri;
	}

	public String getName() {
		return name;
	}

	public String getDownstreamUri() {
		return downstreamUri;
	}
	
	public void setAuthRequired(boolean isAuthRequired) {
		this.isAuthRequired = isAuthRequired;
	}

	public boolean isAuthRequired() {
		return isAuthRequired;
	}
	
	public boolean isBasicAuth() {
		return isBasicAuth;
	}

	public void setBasicAuth(boolean isBasicAuth) {
		this.isBasicAuth = isBasicAuth;
	}

	public boolean isKeyAuth() {
		return isKeyAuth;
	}

	public void setKeyAuth(boolean isKeyAuth) {
		this.isKeyAuth = isKeyAuth;
	}

	public boolean isHmacAuth() {
		return isHmacAuth;
	}

	public void setHmacAuth(boolean isHmacAuth) {
		this.isHmacAuth = isHmacAuth;
	}

	public boolean isOAuth2Auth() {
		return isOAuth2Auth;
	}

	public void setOAuth2Auth(boolean isOAuth2Auth) {
		this.isOAuth2Auth = isOAuth2Auth;
	}

	public boolean isJwtAuth() {
		return isJwtAuth;
	}

	public void setJwtAuth(boolean isJwtAuth) {
		this.isJwtAuth = isJwtAuth;
	}

	public void setLdapAuth(boolean isLdapAuth) {
		this.isLdapAuth = isLdapAuth;
	}

	public boolean isLdapAuth() {
		return isLdapAuth;
	}

	public boolean isSignatureAuth() {
		return isSignatureAuth;
	}

	public void setSignatureAuth(boolean signatureAuth) {
		isSignatureAuth = signatureAuth;
	}

	public boolean isDigitalAuth() {
		return isDigitalAuth;
	}

	public void setDigitalAuth(boolean digitalAuth) {
		isDigitalAuth = digitalAuth;
	}

	@Override
	public String toString() {
		return "Api [id=" + getId() + ", name=" + name + ", reqUri=" + reqUri + ", downstreamUri=" + downstreamUri + ", creationDate="
				+ getCreationDate() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reqUri == null) ? 0 : reqUri.hashCode())
				+ ((downstreamUri == null) ? 0 : downstreamUri.hashCode());
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApiEntity other = (ApiEntity) obj;
		if (reqUri == null) {
			if (other.reqUri != null)
				return false;
		} else if (!reqUri.equals(other.reqUri))
			return false;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
}
