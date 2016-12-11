package com.test.skybet;

import java.nio.charset.Charset;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.test.skybet.bean.BetAPIRequestType;
import com.test.skybet.bean.BetAPIResponseType;
import com.test.skybet.bean.BetRequestType;
import com.test.skybet.bean.BetResponseType;
import com.test.skybet.bean.Odds;
import com.test.skybet.controller.SkybetApplicationController;
import com.test.skybet.factory.BetAPIRequestTypeFactory;
import com.test.skybet.factory.BetResponseTypeFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = SkybetApplicationTests.TestConfig.class)
public class SkybetApplicationTests {
	
	@Value("${skybet.url.available}")
	private String available;
	
	@Value("${skybet.url.bets}")
	private String bets;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private SkybetApplicationController controller;
	
	@Before
	public void setup() {
		Mockito.reset(restTemplate);
	}
	
	@Test
	public void testGetBet() {
		BetAPIResponseType[] betAPIResponseTypesMock = {new BetAPIResponseType(1, "Test Event", "Test name", new Odds(2, 5))};
		Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any())).thenReturn((ResponseEntity<Object>) new ResponseEntity<Object>(betAPIResponseTypesMock, HttpStatus.OK));
		BetResponseType[] expectedBetResponseTypeList = {new BetResponseType(1, "Test Event", "Test name", 1.4f)};
		
		List<BetResponseType> actualBetResponseTypeList = controller.getBet();
		
		for (int i=0; i<actualBetResponseTypeList.size(); i++) {
			Assert.assertEquals(expectedBetResponseTypeList[i], actualBetResponseTypeList.get(i));
		}
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testPostBet() {
		BetRequestType betRequestTypeMock = new BetRequestType(1, 11.0f, 10);
		BetAPIResponseType betAPIResponseTypeMock = new BetAPIResponseType(1, "Test Event", "Test name", new Odds(10, 1), 8, 440313);
		Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any()))
			.thenReturn((ResponseEntity<Object>) new ResponseEntity<Object>(betAPIResponseTypeMock, HttpStatus.CREATED));
		ResponseEntity<BetResponseType> expectedBetResponseType = new ResponseEntity<BetResponseType>(new BetResponseType(1, "Test Event", "Test name", 11f, 8, 440313), HttpStatus.CREATED);
		
		ResponseEntity<BetResponseType> actualBetResponseType = (ResponseEntity<BetResponseType>) controller.postBet(betRequestTypeMock);
		
		Mockito.verify(restTemplate, Mockito.times(1)).postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any());
		Assert.assertEquals(expectedBetResponseType, actualBetResponseType);
	}
	
	@Test
	public void testPostBetError() {
		String errorResponseBody = "{\"error\": \"Invalid bet ID\"}";
		HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.I_AM_A_TEAPOT, "", errorResponseBody.getBytes(), Charset.defaultCharset());
		Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any())).thenThrow(httpClientErrorException);
		BetRequestType betRequestTypeMock = new BetRequestType(1, 12.0f, 10);
		ResponseEntity<String> expectedBetResponseType = new ResponseEntity<String>(errorResponseBody, HttpStatus.I_AM_A_TEAPOT);
		
		ResponseEntity<?> actualBetResponseType = (ResponseEntity<?>) controller.postBet(betRequestTypeMock);
		
		Mockito.verify(restTemplate, Mockito.times(1)).postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any());
		Assert.assertEquals(expectedBetResponseType, actualBetResponseType);
	}
	
	@Test
	public void testBetResponseFactoryWithValidData() {
		Object[][] validDatas = betResponseFactoryValidData();
		for (Object[] validData : validDatas) {
			BetResponseTypeFactory responseFactory = new BetResponseTypeFactory();
			BetAPIResponseType betRequestType = new BetAPIResponseType(1, "Test Event", "Test Name", (Odds)validData[0]);
			
			BetResponseType betResponseType = responseFactory.getBetResponseType(betRequestType);
			
			Assert.assertEquals(betRequestType.getBet_id(), betResponseType.getBet_id());
			Assert.assertEquals(betRequestType.getEvent(), betResponseType.getEvent());
			Assert.assertEquals(betRequestType.getName(), betResponseType.getName());
			Assert.assertEquals((float)validData[1], betResponseType.getOdds(), 0);
		}
	}

	private Object[][] betResponseFactoryValidData() {
		return new Object[][] {
			{new Odds(1, 4), 1.25f},
			{new Odds(4, 1), 5f},
			{new Odds(1, 1), 2f},
			{new Odds(6, 5), 2.2f}
		};
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBetResponseFactoryWithInvalidData() {
		Object[] invalidDatas = betResponseFactoryInvalidData();
		for (Object invalidData : invalidDatas) {
			BetResponseTypeFactory responseFactory = new BetResponseTypeFactory();
			BetAPIResponseType betRequestType = new BetAPIResponseType(1, "Test Event", "Test Name", (Odds)invalidData);
			
			responseFactory.getBetResponseType(betRequestType);
		}
	}

	private Object[] betResponseFactoryInvalidData() {
		return new Object[] {
			null,
			new Odds(),
			new Odds(-4, 1),
			new Odds(4, -1),
			new Odds(0, 1),
			new Odds(4, 0)
		};
	}
	
	@Test
	public void testBetAPIRequestFactoryWithValidData() {
		Object[][] validDatas = betAPIRequestFactoryValidData();
		for (Object[] validData : validDatas) {
			BetAPIRequestTypeFactory betAPIRequestTypeFactory = new BetAPIRequestTypeFactory();
			BetRequestType betRequestType = new BetRequestType(1, (float)validData[0], 10);
			
			BetAPIRequestType betAPIRequestType = betAPIRequestTypeFactory.getBetAPIRequestType(betRequestType);
			
			Assert.assertEquals(betRequestType.getBet_id(), betAPIRequestType.getBet_id());
			Assert.assertEquals(betRequestType.getStake(), betAPIRequestType.getStake());
			Assert.assertEquals(((Odds)validData[1]).getNumerator(), betAPIRequestType.getOdds().getNumerator());
			Assert.assertEquals(((Odds)validData[1]).getDenominator(), betAPIRequestType.getOdds().getDenominator());
		}
	}

	private Object[][] betAPIRequestFactoryValidData() {
		return new Object[][] {
			{1.25f, new Odds(1, 4)},
			{5f, new Odds(4, 1)},
			{2f, new Odds(1, 1)},
			{2.2f, new Odds(6, 5)}
		};
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBetAPIRequestFactoryWithInvalidData() {
		float[] validDatas = {0, -1};
		for (float validData : validDatas) {
			BetAPIRequestTypeFactory betAPIRequestTypeFactory = new BetAPIRequestTypeFactory();
			BetRequestType betRequestType = new BetRequestType(1, validData, 10);
			
			betAPIRequestTypeFactory.getBetAPIRequestType(betRequestType);
		}
	}
	
	public static class TestConfig {
		@Bean
		public BetResponseTypeFactory betResponseTypeFactory() {
			return new BetResponseTypeFactory();
		}
		
		@Bean
		public BetAPIRequestTypeFactory betAPIRequestTypeFactory() {
			return new BetAPIRequestTypeFactory();
		}
		
		@Bean RestTemplate restTemplate () {
			return Mockito.mock(RestTemplate.class);
		}
		
		@Bean SkybetApplicationController skybetApplicationController() {
			return new SkybetApplicationController();
		}
	}
}
