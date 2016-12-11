package com.test.skybet.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.test.skybet.bean.BetAPIRequestType;
import com.test.skybet.bean.BetAPIResponseType;
import com.test.skybet.bean.BetRequestType;
import com.test.skybet.bean.BetResponseType;
import com.test.skybet.factory.BetAPIRequestTypeFactory;
import com.test.skybet.factory.BetResponseTypeFactory;

/**
 * 
 * @author Eva Balazsfalvi
 *
 * Endpoints controller class.
 * 
 */
@RestController
public class SkybetApplicationController {
	private static final Logger log = LoggerFactory.getLogger(SkybetApplicationController.class);
	
	@Value("${skybet.welcome}")
	private String welcome;
	
	@Value("${skybet.url.available}")
	private String available;
	
	@Value("${skybet.url.bets}")
	private String bets;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private BetResponseTypeFactory betResponseTypeFactory;
	
	@Autowired
	private BetAPIRequestTypeFactory betAPIRequestTypeFactory;
	
	/**
	 * Root end point.
	 * @return Default welcoming message.
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String welcome () {
		log.info("Endpoint \"/\" was called");
		return welcome;
	}

	/**
	 * Gets a response from SkyBet REST API and converts the response's fractional odds member to decimal odds.
	 *  
	 * @return Mapped {@link BetResponseType}.
	 */
	@RequestMapping(value="/available", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	public List<BetResponseType> getBet () {
		log.info("Endpoint \"/available\" was called");

		ResponseEntity<BetAPIResponseType[]> responseEntity = restTemplate.getForEntity(available, BetAPIResponseType[].class);

		List<BetResponseType> betResponseTypes = Arrays.asList(responseEntity.getBody()).stream()
				.map(e -> betResponseTypeFactory.getBetResponseType(e))
				.collect(Collectors.toList());
		return betResponseTypes;
	}
	
	/**
	 * Maps {@link BetRequestType} to {@link BetAPIRequestType} before sending it to SkyBet REST API,
	 * and maps the {@link BetAPIResponseType} response to {@link BetResponseType}.
	 * 
	 * @param betRequestType {@link BetRequestType} with decimal odds member.
	 * @return Mapped {@link BetResponseType}
	 */
	@RequestMapping(value="/bets", method=RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> postBet (@RequestBody BetRequestType betRequestType) {
		log.info("Endpoint \"/bets\" was called");
		
		ResponseEntity<?> response;
		try {
			BetAPIRequestType betAPIRequestType = betAPIRequestTypeFactory.getBetAPIRequestType(betRequestType);
			ResponseEntity<BetAPIResponseType> responseEntity = restTemplate.postForEntity(bets, betAPIRequestType, BetAPIResponseType.class);
			BetResponseType betResponseType = betResponseTypeFactory.getBetResponseType(responseEntity.getBody());

			response = new ResponseEntity<BetResponseType>(betResponseType, responseEntity.getStatusCode());
		} catch (HttpClientErrorException e) {
			response = ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
		}
		return response;
	}
}
