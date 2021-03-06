package com.example.polls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

import javax.annotation.PostConstruct;
import java.util.TimeZone;
import javax.servlet.Filter;

@SpringBootApplication
@EntityScan(basePackageClasses = {
		PollsApplication.class,
		Jsr310JpaConverters.class
})
public class PollsApplication {

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(PollsApplication.class, args);
	}

	@Bean
	public Filter OpenFilter() {
		return new OpenEntityManagerInViewFilter();
	}
}
