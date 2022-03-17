package ecommerce.web.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EcommerceApplication {

	public static void main(String[] args) {

		Logger logger = LoggerFactory.getLogger(EcommerceApplication.class);

		logger.warn(" **** Waiting for application to respond... **** ");

		SpringApplication.run(EcommerceApplication.class, args);

		logger.warn("Al-bli started successfully...");
	}
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource
				= new ReloadableResourceBundleMessageSource();

		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}
