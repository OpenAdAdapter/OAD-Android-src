
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter;

public class Reward {
	float amount;
	String network;
	String currency;
	public Reward(String network, float amount, String currency) {
		super();
		this.amount = amount;
		this.network = network;
		this.currency = currency;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
