package de.survey.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.flyway.enabled=false",
		"spring.jpa.hibernate.ddl-auto=none"
})
class SurveyProjectApplicationTests {

	@Test
	void contextLoads() {
	}

}
