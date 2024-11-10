package com.example.service.result;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RegistrationResultTest {

	@Test
	void addError() {
		RegistrationResult result = new RegistrationResult();
		String field = "username";
		String message = "Username is required";
		
		result.addError(field, message);
		
		assertThat(result.getErrors()).containsEntry(field, message);

	}

}
