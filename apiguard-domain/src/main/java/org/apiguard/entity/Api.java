package org.apiguard.entity;

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

public interface Api extends Base {

	public String getReqUri();

	public String getName();

	public String getDownstreamUri();
	
	public boolean isAuthRequired();
	
	public boolean isBasicAuth();
	
	public boolean isKeyAuth();
	
	public boolean isHmacAuth();
	
	public boolean isOAuth2Auth();
	
	public boolean isJwtAuth();
	
	public boolean isLdapAuth();
}
