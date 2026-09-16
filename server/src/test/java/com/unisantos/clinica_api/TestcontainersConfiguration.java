package com.unisantos.clinica_api;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	MySQLContainer mysqlContainer() {
		// Mesma versao do docker-compose.yml: testar contra uma versao diferente
		// da que roda em producao esconde diferenca de comportamento do MySQL.
		return new MySQLContainer(DockerImageName.parse("mysql:8.4"));
	}

}
