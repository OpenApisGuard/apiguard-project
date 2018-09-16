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

public class ClientVo extends BaseRestResource {

	private static final long serialVersionUID = 1L;

	private String clientId;

	private String email;

	private String firstName;

	private String lastName;

	public ClientVo(String id, String creationDate, String lastUpdateDate, String clientId, String email, String firstName, String lastName) {
		super(id, creationDate, lastUpdateDate);
		this.clientId = clientId;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getClientId() {
		return clientId;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
}
