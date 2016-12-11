package com.test.skybet.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.test.skybet.factory.BetAPIRequestTypeFactory;
import com.test.skybet.factory.BetResponseTypeFactory;

/**
 * @author Eva Balazsfalvi
 *
 * Configuration class.
 * 
 */

@Configuration()
public class SkybetApplicationConfiguration {
	
	/**
	 * Connection and read timeout value.
	 */
	@Value("${skybet.connection.timeout}")
	private int timeout;
	
	/**
	 * SkyBet REST API root url.
	 */
	@Value("${skybet.url.root}")
	private String rootUrl;
	
	/**
	 * Creates a {@link RestTemplate} with uri and timeout settings.
	 * 
	 * @return Configured {@link RestTemplate}.
	 */
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplateBuilder()
	    		.setConnectTimeout(timeout)
	    		.setReadTimeout(timeout)
	    		.rootUri(rootUrl)
	    		.build();
	}
	
	/**
	 * Maps {@link BetAPIResponseTypeFactory} to {@link BetResponseTypeFactory}
	 * 
	 * @return {@link BetResponseTypeFactory} with decimal odds.
	 */
	@Bean
	public BetResponseTypeFactory betResponseTypeFactory() {
		return new BetResponseTypeFactory();
	}
	

	/**
	 * Maps {@link BetRequestTypeFactory} to {@link BetAPIRequestTypeFactory}
	 * 
	 * @return {@link BetAPIRequestTypeFactory} with fractional odds.
	 */
	@Bean
	public BetAPIRequestTypeFactory betAPIRequestTypeFactory () {
		return new BetAPIRequestTypeFactory();
	}

}
