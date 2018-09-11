package org.apiguard.cassandra.entity;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import java.io.Serializable;

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

@PrimaryKeyClass
public class ClientId implements Serializable {
	private static final long serialVersionUID = 1L;

	@PrimaryKeyColumn(name = "clientId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String clientId;

	@PrimaryKeyColumn(name = "group", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	private String group;

	public ClientId(String clientId, String group) {
		this.group = group;
		this.clientId = clientId;
	}

	public String getGroup() {
		return group;
	}

	public String getClientId() {
		return clientId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + group.hashCode();
		result = prime * result + clientId.hashCode();
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
		ClientId other = (ClientId) obj;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		return true;
	}
}
