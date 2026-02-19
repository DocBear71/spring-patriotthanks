package org.springframework.samples.petclinic.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// We target the TYPE (Class) so we can access both the 'domain' and the 'id'
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueDomainValidator.class)
public @interface UniqueDomain {

	String message() default "This domain is already registered";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
