package org.apiguard.commons;

import org.apiguard.commons.utils.EncryptUtil;
import org.jgroups.tests.perf.UPerf.Config;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class,
        Config.class, EncryptUtil.class })
@TestPropertySource(properties = { "basic.auth.password.algorithm=SHA-1", "basic.auth.encryption.iteration=1000" })
public class EncryptUtilTest {
	@Autowired
    protected Environment env;

    @Test
    public void verifyProperty() {
        Assert.assertEquals("SHA-1", env.getProperty("basic.auth.password.algorithm"));
    }

    @Test
    public void verifyInvalidProperty() {
    	Assert.assertNotEquals("MD-5", env.getProperty("basic.auth.password.algorithm"));
    }

	@Test
	public void verifyPasswordTest() {
		String inputPassword = "abc123";
		String encryptedPassword = EncryptUtil.getEncryptedString(inputPassword);
		Assert.assertTrue(EncryptUtil.verify("abc123", encryptedPassword));
	}

	@Test
	public void verifyPasswordTest2() {
		String inputPassword = "helloworld";
		String encryptedPassword = EncryptUtil.getEncryptedString(inputPassword);
		Assert.assertTrue(EncryptUtil.verify("helloworld", encryptedPassword));
	}

	@Test
	public void verifyInvalidPasswordTest() {
		String inputPassword = "abc123";
		String encryptedPassword = EncryptUtil.getEncryptedString(inputPassword);
		Assert.assertFalse(EncryptUtil.verify("abc1231", encryptedPassword));
	}

}
