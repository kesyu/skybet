package com.test.skybet.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.skybet.bean.BetAPIRequestType;
import com.test.skybet.bean.BetRequestType;
import com.test.skybet.bean.Odds;

/**
 * 
 * @author Eva Balazsfalvi
 * 
 * Factory to create {@link BetAPIRequestType} from {@link BetRequestType}.
 *
 */
public class BetAPIRequestTypeFactory {
	private static final Logger log = LoggerFactory.getLogger(BetAPIRequestTypeFactory.class);
	
	/**
	 * Maps {@link BetRequestType} to {@link BetAPIRequestType}
	 * 
	 * @param betRequestType
	 * @return Mapped {@link BetAPIRequestType}
	 */
	public BetAPIRequestType getBetAPIRequestType(BetRequestType betRequestType) {
		Odds fractionalOdds = calculateFractionalOdds(betRequestType.getOdds());
		return new BetAPIRequestType(betRequestType.getBet_id(), fractionalOdds, betRequestType.getStake());
	}
	
	/**
	 * Calculates fractional odds from decimal odds.
	 * 
	 * @param odds Decimal odds
	 * @return Fractional {@link Odds}
	 * @throws IllegalArgumentException
	 */
	private Odds calculateFractionalOdds(float odds) throws IllegalArgumentException {
		if (odds <= 0) {
			throw new IllegalArgumentException("Fractional odds calculation failed.");
		}
		float numerator = odds - 1;
		float denominator = 1;
		while (numerator*denominator != (int)(numerator*denominator)) {
			denominator++;
		}

		Odds fractionalOdds = new Odds((int)(numerator * denominator), (int)denominator);
		log.info("Fractional odds calculated successfully.");
		return fractionalOdds;
	}
}
