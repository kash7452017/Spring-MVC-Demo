package com.luv2code.springdemo.mvc.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CourseCodeConstraintValidator implements ConstraintValidator<CourseCode, String>{

	private String coursePrefix;
	
	@Override
	public void initialize(CourseCode constraintAnnotation) {
		coursePrefix = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(String theCode, ConstraintValidatorContext theConstraintValidatorContext) {

		// Validation logic
		// Test if the form data starts with our course prefix
		// Does it start with "LUV"?
		boolean result;
		
		if (theCode != null)
		{						
			result =theCode.startsWith(coursePrefix);
		}
		else result = true;
		
		return result;
	}
	

}
