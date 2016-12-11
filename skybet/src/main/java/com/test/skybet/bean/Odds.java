package com.test.skybet.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Eva Balazsfalvi
 *
 * Represents a fractional bet.
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Odds {
	private int numerator;
	private int denominator;
	
	public Odds(){};
	public Odds(int numerator, int denominator) {
		super();
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	public int getNumerator() {
		return numerator;
	}
	
	public void setNumerator(int numerator) {
		this.numerator = numerator;
	}
	
	public int getDenominator() {
		return denominator;
	}
	
	public void setDenominator(int denominator) {
		this.denominator = denominator;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + denominator;
		result = prime * result + numerator;
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
		Odds other = (Odds) obj;
		if (denominator != other.denominator)
			return false;
		if (numerator != other.numerator)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Odds [numerator=").append(numerator).append(", denominator=").append(denominator).append("]");
		return builder.toString();
	}
}
