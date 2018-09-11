package org.apiguard.valueobject;

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

public class ApiVo extends BaseRestResource {
	private static final long serialVersionUID = 1L;

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
	
	public ApiVo(String id, String creationDate, String lastUpdateDate, String name, String reqUri, String downstreamUri, boolean isAuthRequired, boolean isBasicAuth, boolean isKeyAuth, boolean isHmacAuth, boolean isOAuth2Auth, boolean isJwtAuth, boolean isLdapAuth) {
		super(id, creationDate, lastUpdateDate);
		this.reqUri = reqUri;
		this.name = name;
		this.downstreamUri = downstreamUri;
		this.isAuthRequired = isAuthRequired;
		this.isBasicAuth = isBasicAuth;
		this.isHmacAuth = isHmacAuth;
		this.isJwtAuth = isJwtAuth;
		this.isKeyAuth = isKeyAuth;
		this.isLdapAuth = isLdapAuth;
		this.isOAuth2Auth = isOAuth2Auth;
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

	public boolean isAuthRequired() {
		return isAuthRequired;
	}

	public boolean isBasicAuth() {
		return isBasicAuth;
	}

	public boolean isKeyAuth() {
		return isKeyAuth;
	}

	public boolean isHmacAuth() {
		return isHmacAuth;
	}

	public boolean isOAuth2Auth() {
		return isOAuth2Auth;
	}

	public boolean isJwtAuth() {
		return isJwtAuth;
	}

	public boolean isLdapAuth() {
		return isLdapAuth;
	}
}
