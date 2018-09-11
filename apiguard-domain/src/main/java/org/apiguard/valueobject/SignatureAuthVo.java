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

public class SignatureAuthVo extends BaseRestResource {

	private static final long serialVersionUID = 1L;

	private String clientId;

	private String clientAlias;

	private String secret;
	
	private String reqUri;

	public SignatureAuthVo(String id, String creationDate, String lastUpdateDate, String clientAlias, String clientId, String secret, String reqUri) {
		super(id, creationDate, lastUpdateDate);
		this.clientAlias = clientAlias;
		this.clientId = clientId;
		this.secret = secret;
		this.reqUri = reqUri;
	}

	public String getClientAlias() {
		return clientAlias;
	}

	public String getClientId() {
		return clientId;
	}

	public String getSecret() {
		return secret;
	}

	public String getReqUri() {
		return reqUri;
	}

}
