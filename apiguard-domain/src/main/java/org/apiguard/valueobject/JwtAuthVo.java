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

public class JwtAuthVo extends BaseRestResource {

	private static final long serialVersionUID = 1L;

	private String clientId;

	private String issuer;

	private String reqUri;

	private String secret;

	// nbf- identifies the time before which the JWT must not be accepted for processing.
	private boolean notBefore;

	// exp - identifies the expiration time on or after which the JWT must not be accepted for processing.
	private boolean expires;

	public JwtAuthVo(String id, String creationDate, String lastUpdateDate, String clientId, String reqUri, String issuer, String secret, boolean notBefore, boolean expires) {
		super(id, creationDate, lastUpdateDate);
		this.clientId = clientId;
		this.issuer = issuer;
		this.reqUri = reqUri;
		this.secret = secret;
		this.notBefore = notBefore;
		this.expires = expires;
	}

	public String getClientId() {
		return clientId;
	}

	public String getReqUri() {
		return reqUri;
	}

	public String getIssuer() {
		return issuer;
	}

	public String getSecret() {
		return secret;
	}

	public boolean isNotBefore() {
		return notBefore;
	}

	public boolean isExpires() {
		return expires;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


}
