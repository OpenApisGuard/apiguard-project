package org.apiguard.commons.utils;

import org.jasypt.digest.PooledStringDigester;
import org.jasypt.salt.RandomSaltGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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

@Component
public class EncryptUtil {

	private final static PooledStringDigester digester = new PooledStringDigester();

	@Value("${basic.auth.password.algorithm}")
	private String algorithm = "MD5";

	@Value("${basic.auth.encryption.iteration}")
	private Integer iteration = 1234;

	@PostConstruct
	private void setup() {
		int cores = Runtime.getRuntime().availableProcessors();
		digester.setPoolSize(cores);
		digester.setAlgorithm(algorithm);
		digester.setIterations(iteration);

		RandomSaltGenerator saltGenerator = new RandomSaltGenerator();
		digester.setSaltGenerator(saltGenerator);
		digester.setSaltSizeBytes(32);
	}

	public static String getEncryptedString(String password) {
		return digester.digest(password);
	}

	public static boolean verify(String inputPassword, String encryptedPassword) {
		return digester.matches(inputPassword, encryptedPassword);
	}

}
