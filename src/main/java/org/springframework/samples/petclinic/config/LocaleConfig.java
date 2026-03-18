package org.springframework.samples.petclinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.time.Duration;
import java.util.Locale;

@Configuration
public class LocaleConfig {

	@Bean
	@Primary
	public LocaleResolver localeResolver() {
		// Creates a cookie named "PREFERRED_LANGUAGE"
		CookieLocaleResolver resolver = new CookieLocaleResolver("PREFERRED_LANGUAGE");
		resolver.setDefaultLocale(Locale.ENGLISH);
		resolver.setCookieMaxAge(Duration.ofDays(365)); // Remembers them for a year!
		return resolver;
	}

}
