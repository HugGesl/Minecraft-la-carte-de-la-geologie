package com.ignfab.minalac.Strategies;

public class ContextND {
	StrategyND DataDim;
	
	public ContextND (StrategyND DataDim) {this.DataDim = DataDim;}
	
	private void BuildMap() {
		DataDim.buildMap();
	}
	
}
