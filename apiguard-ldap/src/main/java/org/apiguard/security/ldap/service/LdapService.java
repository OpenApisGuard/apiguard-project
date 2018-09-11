package org.apiguard.security.ldap.service;

import org.apiguard.security.ldap.exceptions.ApiGuardLdapException;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.stereotype.Service;

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
@Service
public class LdapService {

    public boolean authenticate(String ldapUrl, String adminDn, String adminPassword, String userBase, String userAttr, String userName, String password) throws ApiGuardLdapException {
        try {
            LdapContextSource contextSource = new LdapContextSource();
            contextSource.setUrl(ldapUrl);
            contextSource.setReferral("follow");
            contextSource.setUserDn(adminDn);
            contextSource.setPassword(adminPassword);
            contextSource.afterPropertiesSet();

            LdapUserSearch userSearch = new FilterBasedLdapUserSearch(userBase, "(" + userAttr + "={0})", contextSource);
            BindAuthenticator b = new BindAuthenticator(contextSource);
            b.setUserSearch(userSearch);
            DirContextOperations authenticate1 = b.authenticate(new UsernamePasswordAuthenticationToken(userName, password));

            return true;
        }
        catch(AuthenticationException e) {
            throw new ApiGuardLdapException("Invalid admin credentails", e);
        }
        catch(BadCredentialsException e) {
            throw new ApiGuardLdapException("Invalid user credentails", e);
        }
        catch(UsernameNotFoundException e) {
            throw new ApiGuardLdapException("Invalid user id", e);
        }
        catch(Exception e) {
            throw new ApiGuardLdapException(e.getMessage(), e);
        }
    }
}
