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

public class KeyAuthVo extends BaseRestResource {

	private static final long serialVersionUID = 1L;

	private String clientId;
	
	private String key;
	
	private String reqUri;

	public KeyAuthVo(String id, String creationDate, String lastUpdateDate, String clientId, String key, String reqUri) {
		super(id, creationDate, lastUpdateDate);
		this.clientId = clientId;
		this.key = key;
		this.reqUri = reqUri;
	}

	public String getClientId() {
		return clientId;
	}

	public String getKey() {
		return key;
	}

	public String getReqUri() {
		return reqUri;
	}
}
