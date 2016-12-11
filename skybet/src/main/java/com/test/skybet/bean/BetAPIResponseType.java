package com.test.skybet.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Eva Balazsfalvi
 *
 * Represents the Sykbet REST API /available & /bets response.
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BetAPIResponseType {
	private Integer bet_id;
	private String event;
	private String name;
	private Odds odds;
	private Integer stake;
	private Integer transaction_id;
	
	public BetAPIResponseType() {};

	public BetAPIResponseType(Integer bet_id, String event, String name, Odds odds) {
		super();
		this.bet_id = bet_id;
		this.event = event;
		this.name = name;
		this.odds = odds;
	}
	
	public BetAPIResponseType(Integer bet_id, String event, String name, Odds odds, Integer stake, Integer transaction_id) {
		this(bet_id, event, name, odds);
		this.stake = stake;
		this.transaction_id = transaction_id;
	}

	public Integer getBet_id() {
		return bet_id;
	}

	public void setBet_id(Integer bet_id) {
		this.bet_id = bet_id;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Odds getOdds() {
		return odds;
	}

	public void setOdds(Odds odds) {
		this.odds = odds;
	}

	public Integer getStake() {
		return stake;
	}

	public void setStake(Integer stake) {
		this.stake = stake;
	}

	public Integer getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(Integer transaction_id) {
		this.transaction_id = transaction_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bet_id == null) ? 0 : bet_id.hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((odds == null) ? 0 : odds.hashCode());
		result = prime * result + ((stake == null) ? 0 : stake.hashCode());
		result = prime * result + ((transaction_id == null) ? 0 : transaction_id.hashCode());
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
		BetAPIResponseType other = (BetAPIResponseType) obj;
		if (bet_id == null) {
			if (other.bet_id != null)
				return false;
		} else if (!bet_id.equals(other.bet_id))
			return false;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (odds == null) {
			if (other.odds != null)
				return false;
		} else if (!odds.equals(other.odds))
			return false;
		if (stake == null) {
			if (other.stake != null)
				return false;
		} else if (!stake.equals(other.stake))
			return false;
		if (transaction_id == null) {
			if (other.transaction_id != null)
				return false;
		} else if (!transaction_id.equals(other.transaction_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BetAPIResponseType [bet_id=").append(bet_id).append(", event=").append(event).append(", name=")
				.append(name).append(", odds=").append(odds).append(", stake=").append(stake)
				.append(", transaction_id=").append(transaction_id).append("]");
		return builder.toString();
	}
}
