package org.apiguard.rest.controller;

import org.apiguard.cassandra.entity.*;
import org.apiguard.rest.utils.ObjectsConverter;
import org.apiguard.service.ApiAuthService;
import org.apiguard.service.ApiService;
import org.apiguard.service.ClientService;
import org.apiguard.service.exceptions.ApiAuthException;
import org.apiguard.service.exceptions.ClientException;
import org.apiguard.valueobject.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

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

@Controller
@RequestMapping(value = "/clients")
public class ClientController extends BaseController {

	private static final String PARAM_REQUEST_URI = "request_uri";
	private static final String PARAM_ID = "id";
	private static final String PARAM_KEY = "key";
	private static final String PARAM_GROUP = "group";
	private static final String PARAM_PASSWORD = "password";
	private static final String PARAM_CLIENT_ALIAS = "client_alias";
	private static final String PARAM_SECRET = "secret";
	private static final String PARAM_LDAP_URL = "ldap_url";
	private static final String PARAM_ADMIN_DN = "admin_dn";
	private static final String PARAM_ADMIN_PASSWORD = "admin_Password";
	private static final String PARAM_USER_BASE = "user_base";
	private static final String PARAM_USER_ATTR = "user_attribute";
	private static final String PARAM_CACHE_EXPIRE_SEC = "cache_expire_seconds";
	private static final String PARAM_VALID_NOT_BEFORE = "not_before";
	private static final String PARAM_EXPIRES = "expires";

	@Autowired
	ApiService<ApiEntity> apiService;

	@Autowired
	@Qualifier("cassandraClientService")
	ClientService<ClientEntity> clientService;

	@Autowired
	ApiAuthService apiAuthService;

	@RequestMapping(value = "/{group}/{clientId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<BaseRestResource> getClient(@PathVariable("clientId") String clientId, @PathVariable("group") String group,
													  HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		try {
			if (!isValid(clientId) || !isValid(group)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("clientId and/or group are not provided."));
			}

			ClientEntity client = clientService.getClient(clientId, group);
			ClientVo clientVo = null;
			if (client != null) {
				clientVo = ObjectsConverter.convertClientDomainToValue(client);
			}
			return new ResponseEntity<BaseRestResource>(clientVo, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body((BaseRestResource) new EexceptionVo(e.getMessage()));
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<BaseRestResource> addClient(@RequestBody Map<String, Object> jsonPayload,
			HttpServletResponse res) throws IOException {
		
		try {
			String id = (String) jsonPayload.get(PARAM_ID);
			String group = (String) jsonPayload.get(PARAM_GROUP);

			if (!isValid(id) || !isValid(group)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("Client id and/or group are not provided."));
			}
			
			ClientEntity client = clientService.addClient(id, group);
			ClientVo clientVo = ObjectsConverter.convertClientDomainToValue(client);
			return new ResponseEntity<BaseRestResource>(clientVo, HttpStatus.CREATED);
		} catch (ClientException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body((BaseRestResource) new EexceptionVo(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body((BaseRestResource) new EexceptionVo(e.getMessage()));
		}
	}

	@RequestMapping(value = "/{group}/{clientId}/key-auth", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<BaseRestResource> addKeyAuth(@PathVariable("clientId") String clientId, @PathVariable("group") String group,
													   @RequestBody Map<String, Object> jsonPayload,
			HttpServletResponse res) throws IOException {

		try {
			String key = (String) jsonPayload.get(PARAM_KEY);
			String reqUri = (String) jsonPayload.get(PARAM_REQUEST_URI);

			if (!isValid(clientId) || !isValid(group)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("Client id and/or group are not provided."));
			}

			if (!isValid(key)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("key is not provided."));
			}

			if (!isValid(reqUri)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("Request URI is not provided."));
			}

			if (clientService.exists(clientId, group)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((BaseRestResource) new EexceptionVo(clientId + " is not configured in " + group));
			}
			
			KeyAuthEntity keyAuth = apiAuthService.addKeyAuth(reqUri, clientId, group, key);

			KeyAuthVo keyAuthVo = ObjectsConverter.convertKeyAuthDomainToValue(keyAuth);
			return new ResponseEntity<BaseRestResource>(keyAuthVo, HttpStatus.CREATED);
		}
		catch (ApiAuthException ae) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((BaseRestResource) new EexceptionVo(ae.getMessage()));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body((BaseRestResource) new EexceptionVo(e.getMessage()));
		}
	}

	@RequestMapping(value = "/{group}/{clientId}/basic-auth", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<BaseRestResource> addBasicAuth(@PathVariable("clientId") String clientId,  @PathVariable("group") String group,
														 @RequestBody Map<String, Object> jsonPayload,
			HttpServletResponse res) throws IOException {
		
		try {
			String pwd = (String) jsonPayload.get(PARAM_PASSWORD);
			String reqUri = (String) jsonPayload.get(PARAM_REQUEST_URI);
			
			if (!isValid(clientId) || !isValid(group)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("Client id and/or group are not provided."));
			}
			
			if (!isValid(pwd)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("password is not provided."));
			}
			
			if (!isValid(reqUri)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("Request URI is not provided."));
			}

			if (clientService.exists(clientId, group)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((BaseRestResource) new EexceptionVo(clientId + " is not configured in " + group));
			}
			
			BasicAuthEntity basicAuth = apiAuthService.addBasicAuth(reqUri, clientId, group, pwd);
			
			BasicAuthVo basicAuthVo = ObjectsConverter.convertBasicAuthDomainToValue(basicAuth);
			return new ResponseEntity<BaseRestResource>(basicAuthVo, HttpStatus.CREATED);
		}
		catch (ApiAuthException ae) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((BaseRestResource) new EexceptionVo(ae.getMessage()));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body((BaseRestResource) new EexceptionVo(e.getMessage()));
		}
	}

	@RequestMapping(value = "/{group}/{client}/signature-auth", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<BaseRestResource> addSignatureAuth(@PathVariable("client") String clientId, @PathVariable("group") String group,
															 @RequestBody Map<String, Object> jsonPayload,
			HttpServletResponse res) throws IOException {

		try {
			String clientAlias = (String) jsonPayload.get(PARAM_CLIENT_ALIAS);
			String secret = (String) jsonPayload.get(PARAM_SECRET);
			String reqUri = (String) jsonPayload.get(PARAM_REQUEST_URI);

			if (!isValid(clientId) || !isValid(group)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("Client id and/or group are not provided."));
			}
			
			if (!isValid(clientAlias)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("client alias is not provided."));
			}


			if (!isValid(secret)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("secret is not provided."));
			}

			if (!isValid(reqUri)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("Request URI is not provided."));
			}

			if (clientService.exists(clientId, group)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((BaseRestResource) new EexceptionVo(clientId + " is not configured in " + group));
			}

            SignatureAuthEntity signatureAuth = apiAuthService.addHttpSignatureAuth(reqUri, clientId, group, clientAlias, secret);

            SignatureAuthVo signatureAuthVo = ObjectsConverter.convertSignatureAuthDomainToValue(signatureAuth);
            return new ResponseEntity<BaseRestResource>(signatureAuthVo, HttpStatus.CREATED);
		}
		catch (ApiAuthException ae) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((BaseRestResource) new EexceptionVo(ae.getMessage()));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body((BaseRestResource) new EexceptionVo(e.getMessage()));
		}
	}

	@RequestMapping(value = "/{group}/{clientId}/ldap-auth", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<BaseRestResource> addLdapAuth(@PathVariable("clientId") String clientId,  @PathVariable("group") String group,
														@RequestBody Map<String, Object> jsonPayload,
														 HttpServletResponse res) throws IOException {
		try {
			String reqUri = (String) jsonPayload.get(PARAM_REQUEST_URI);
			String ldapUrl = (String) jsonPayload.get(PARAM_LDAP_URL);
			String adminDn = (String) jsonPayload.get(PARAM_ADMIN_DN);
			String adminPwd = (String) jsonPayload.get(PARAM_ADMIN_PASSWORD);
			String userBase = (String) jsonPayload.get(PARAM_USER_BASE);
			String userAttr = (String) jsonPayload.get(PARAM_USER_ATTR);
			Integer cacheExpireInSecond = (Integer) jsonPayload.get(PARAM_CACHE_EXPIRE_SEC);

			if (!isValid(clientId) || !isValid(group)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("Client id and/or group are not provided."));
			}

			if (!isValid(reqUri)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("Request URI is not provided."));
			}

			if (!isValid(ldapUrl)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("LDAP url is not provided."));
			}

			if (!isValid(adminDn)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("Admin DN is not provided."));
			}

			if (!isValid(adminPwd)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("Admin password is not provided."));
			}

			if (!isValid(userBase)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("User base is not provided."));
			}

			if (clientService.exists(clientId, group)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((BaseRestResource) new EexceptionVo(clientId + " is not configured in " + group));
			}

			LdapAuthEntity ldapAuthEntity = apiAuthService.addLdapAuth(reqUri, clientId, group, ldapUrl, adminDn, adminPwd, userBase, userAttr, cacheExpireInSecond);

			LdapAuthVo ldapAuthVo = ObjectsConverter.convertLdapAuthDomainToValue(ldapAuthEntity);
			return new ResponseEntity<BaseRestResource>(ldapAuthVo, HttpStatus.CREATED);
		}
		catch (ApiAuthException ae) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((BaseRestResource) new EexceptionVo(ae.getMessage()));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body((BaseRestResource) new EexceptionVo(e.getMessage()));
		}
	}

	@RequestMapping(value = "/{group}/{clientId}/jwt-auth", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<BaseRestResource> addJwtAuth(@PathVariable("clientId") String clientId,  @PathVariable("group") String group,
													   @RequestBody Map<String, Object> jsonPayload,
														HttpServletResponse res) throws IOException {
		try {
			String reqUri = (String) jsonPayload.get(PARAM_REQUEST_URI);
			Boolean notBefore = new Boolean((String) jsonPayload.get(PARAM_VALID_NOT_BEFORE));
			Boolean expires = new Boolean((String) jsonPayload.get(PARAM_EXPIRES));

			if (!isValid(clientId) || !isValid(group)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("Client id and/or group are not provided."));
			}

			if (!isValid(reqUri)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((BaseRestResource) new EexceptionVo("Request URI is not provided."));
			}

			if (clientService.exists(clientId, group)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((BaseRestResource) new EexceptionVo(clientId + " is not configured in " + group));
			}

			JwtAuthEntity jwtAuthEntity = apiAuthService.addJwtAuth(reqUri, clientId, group, notBefore, expires);

			JwtAuthVo jwtAuthVo = ObjectsConverter.convertJwtAuthDomainToValue(jwtAuthEntity);
			return new ResponseEntity<BaseRestResource>(jwtAuthVo, HttpStatus.CREATED);
		}
		catch (ApiAuthException ae) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((BaseRestResource) new EexceptionVo(ae.getMessage()));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body((BaseRestResource) new EexceptionVo(e.getMessage()));
		}
	}

}