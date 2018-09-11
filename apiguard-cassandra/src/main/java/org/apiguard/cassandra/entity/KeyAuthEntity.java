package org.apiguard.cassandra.entity;

import org.apiguard.entity.KeyAuth;
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

@Table("keyAuth")
public class KeyAuthEntity extends BaseEntity implements KeyAuth {

	@PrimaryKey
	private KeyAuthId pk;

	private String clientId;

	public KeyAuthEntity() {
	}
	
	public KeyAuthEntity(String id, Date creationDate, Date lastUpdateDate, String reqUri, String key, String clientId) {
		super(id, creationDate, lastUpdateDate);
		pk = new KeyAuthId(reqUri, key);
		this.clientId = clientId;
	}

	public String getKey() {
		return pk.getKey();
	}

	public String getReqUri() {
		return pk.getReqUri();
	}

	public String getClientId() {
		return clientId;
	}

	public KeyAuthId getPk() {
		return pk;
	}

	public void setPk(KeyAuthId pk) {
		this.pk = pk;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
