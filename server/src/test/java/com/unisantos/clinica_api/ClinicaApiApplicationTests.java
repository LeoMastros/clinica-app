package com.unisantos.clinica_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(
    // Test-only JWT secret — safe to commit, it only signs tokens issued by the test suite.
    properties = "app.jwt.secret=754301b53f88e0134885f37b98a80b22820d3132366e95184de321460fb80c3d")
class ClinicaApiApplicationTests {

  @Test
  void contextLoads() {}
}
