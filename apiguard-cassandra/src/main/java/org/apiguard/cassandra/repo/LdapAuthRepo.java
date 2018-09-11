package org.apiguard.cassandra.repo;

import org.apiguard.cassandra.entity.LdapAuthEntity;
import org.apiguard.cassandra.entity.LdapAuthId;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

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

public interface LdapAuthRepo extends CrudRepository<LdapAuthEntity, LdapAuthId> {

    //TODO: replace me, ok for now (allow filtering has performance issue, but this is barely used
    @Query("select * from ldapAuth where requri = ?0 allow filtering")
    List<LdapAuthEntity> findByReqUri(String reqUri);
}
