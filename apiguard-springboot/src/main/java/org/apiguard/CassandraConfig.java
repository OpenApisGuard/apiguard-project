package org.apiguard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;

import javax.annotation.PostConstruct;
import java.util.Collections;
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
@Configuration
@EnableCassandraRepositories(basePackages="org.apiguard.repo")
public class CassandraConfig extends AbstractCassandraConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CassandraConfig.class);

    @PostConstruct
    public void debug() {
        logger.debug("spring.data.cassandra.contact-points: " + contactPoints);
        logger.debug("spring.data.cassandra.port: " + port);
        logger.debug("spring.data.cassandra.keyspace-name: " + keySpace);
        logger.debug("spring.data.cassandra.entity-base-packages: " + basePackages);
        logger.debug("sspring.data.cassandra.schema-action: " + schemaAction);
    }

    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.data.cassandra.port}")
    private int port;

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keySpace;

    @Value("${spring.data.cassandra.entity-base-packages}")
    private String basePackages;

    @Value("${spring.data.cassandra.schema-action}")
    private String schemaAction;

    @Override
    protected String getKeyspaceName() {
        return keySpace;
    }

    @Override
    protected String getContactPoints() {
        return contactPoints;
    }

    @Override
    protected int getPort() {
        return port;
    }

    @Override
    public SchemaAction getSchemaAction() {
        if (schemaAction == null || schemaAction.isEmpty()) {
            schemaAction = SchemaAction.NONE.name();
            return SchemaAction.NONE;
        }

        schemaAction = SchemaAction.RECREATE.name();
        return SchemaAction.RECREATE;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] {basePackages};
    }

    /**
     * Auto create keyspace if not exists
     * @return
     */
    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return Collections.singletonList(CreateKeyspaceSpecification.createKeyspace(keySpace)
                .ifNotExists()
                .with(KeyspaceOption.DURABLE_WRITES, true)
                .withSimpleReplication());
    }
}
