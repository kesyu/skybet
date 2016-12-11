package com.test.skybet.bean;

/**
 * @author Eva Balazsfalvi
 *
 * Represents the application's /bets request.
 * 
 */
public class BetRequestType {
	private int bet_id;
	private Float odds;
	private int stake;
	
	public BetRequestType() {}
	
	public BetRequestType(int bet_id, float odds, int stake) {
		super();
		this.bet_id = bet_id;
		this.odds = odds;
		this.stake = stake;
	}

	public int getBet_id() {
		return bet_id;
	}

	public void setBet_id(int bet_id) {
		this.bet_id = bet_id;
	}

	public Float getOdds() {
		return odds;
	}

	public void setOdds(Float odds) {
		this.odds = odds;
	}

	public int getStake() {
		return stake;
	}

	public void setStake(int stake) {
		this.stake = stake;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bet_id;
		result = prime * result + ((odds == null) ? 0 : odds.hashCode());
		result = prime * result + stake;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BetRequestType other = (BetRequestType) obj;
		if (bet_id != other.bet_id)
			return false;
		if (odds == null) {
			if (other.odds != null)
				return false;
		} else if (!odds.equals(other.odds))
			return false;
		if (stake != other.stake)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BetRequestType [bet_id=").append(bet_id).append(", odds=").append(odds).append(", stake=")
				.append(stake).append("]");
		return builder.toString();
	}
}
