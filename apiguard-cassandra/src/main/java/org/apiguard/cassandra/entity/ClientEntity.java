package org.apiguard.cassandra.entity;

import org.apiguard.entity.Client;
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

@Table("client")
public class ClientEntity extends BaseEntity implements Client {

	@PrimaryKey
	private ClientId clientPk;
	
	public ClientEntity(String id, Date creationDate, Date lastUpdateDate, String clientId, String group) {
		super(id, creationDate, lastUpdateDate);
		this.clientPk = new ClientId(clientId, group);
	}

	public String getClientId() {
		return clientPk.getClientId();
	}

	public String getGroup() {
		return clientPk.getGroup();
	}
}
