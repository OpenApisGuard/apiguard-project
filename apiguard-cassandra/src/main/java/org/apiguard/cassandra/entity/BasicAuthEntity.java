package org.apiguard.cassandra.entity;

import org.apiguard.entity.BasicAuth;
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

@Table("basicAuth")
public class BasicAuthEntity extends BaseEntity implements BasicAuth {

	@PrimaryKey
	private BasicAuthId pk;

	private String password;

	public BasicAuthEntity() {
	}
	
	public BasicAuthEntity(String id, Date creationDate, Date lastUpdateDate, String reqUri, String clientId, String group,
			String password) {
		super(id, creationDate, lastUpdateDate);
		pk = new BasicAuthId(reqUri, clientId, group);
		this.password = password;
	}

	public BasicAuthId getPk() {
		return pk;
	}

	public void setPk(BasicAuthId pk) {
		this.pk = pk;
	}

	public String getClientId() {
		return pk.getClientId();
	}

	public String getReqUri() {
		return pk.getReqUri();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
