package org.apiguard.cassandra.entity;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.Date;
import java.util.List;

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

@Table("apiReqUriIndx")
public class ApiReqUriIndexEntity extends BaseEntity {

	@PrimaryKey
	private String prefix;

	private List<String> matches;

	public ApiReqUriIndexEntity(String id, Date creationDate, Date lastUpdateDate, String prefix, List<String> matches) {
		super(id, creationDate, lastUpdateDate);
		this.prefix = prefix;
		this.matches = matches;
	}

	public String getPrefix() {
		return prefix;
	}

	public List<String> getMatches() {
		return matches;
	}
}
