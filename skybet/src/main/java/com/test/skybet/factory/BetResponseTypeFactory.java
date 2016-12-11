package com.test.skybet.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.skybet.bean.BetAPIResponseType;
import com.test.skybet.bean.BetResponseType;
import com.test.skybet.bean.Odds;

/**
 * 
 * @author Eva Balazsfalvi
 *
 * Factory to create {@link BetResponseType} from {@link BetAPIResponseType}.
 * 
 */
public class BetResponseTypeFactory {
	private static final Logger log = LoggerFactory.getLogger(BetResponseTypeFactory.class);
	
	/**
	 * Maps {@link BetAPIResponseType} to {@link BetResponseType}.
	 * 
	 * @param betRequestType
	 * @return Mapped {@link BetResponseType}
	 */
	public BetResponseType getBetResponseType(BetAPIResponseType betRequestType) {
		float decimalOdds = calculateDecimalOdds(betRequestType.getOdds());
		return new BetResponseType(betRequestType.getBet_id(), betRequestType.getEvent(), betRequestType.getName(), decimalOdds, betRequestType.getStake(), betRequestType.getTransaction_id());
	}
	
	/**
	 * Calculates decimal odds from fractional {@link Odds}.
	 * 
	 * @param fractionalOdds
	 * @return Decimal odds
	 * @throws IllegalArgumentException
	 */
	private float calculateDecimalOdds(Odds fractionalOdds) throws IllegalArgumentException {
		if (fractionalOdds == null
				|| fractionalOdds.getNumerator() <= 0
				|| fractionalOdds.getDenominator() <= 0) {
			throw new IllegalArgumentException("Decimal odds calculation failed.");
		}
		float decimalOdds = (float) fractionalOdds.getNumerator() / fractionalOdds.getDenominator() + 1; 
		log.info("Decimal odds calculated successfully.");
		return decimalOdds;
	}
}