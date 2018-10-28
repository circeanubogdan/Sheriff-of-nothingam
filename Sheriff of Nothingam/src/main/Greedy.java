package main;

import java.util.ArrayList;
import java.util.List;

public class Greedy extends Basic {
	
	public InstantiateGoods G;
	Greedy(List<Integer> Hand, int money, boolean isSheriff) {
		super(Hand, money, isSheriff);
		G = new InstantiateGoods();
		G.create();
	}
	
	public int GetMaxIllegal() {
		int max = -1;
		int type = -1;
		List<Integer> toRemove = new ArrayList<>();
		for(int it : super.Hand) {
			if(G.goods[it].type.equals("Illegal")) {
				if(G.goods[it].profit > max) {
					toRemove.clear();
					toRemove.add(it);
					max = G.goods[it].profit;
					type = it;
				}
			}
		}
		if(type != -1)
			super.Hand.removeAll(toRemove);
		return type;
	}
	
	public void GreedyCommerciantStrategy(int round) {
		super.BasicComerciantStrategy();
		if(round % 2 == 0 && super.bag.size() < 5) {
			int type = GetMaxIllegal();
			if(type != -1) {
				super.bag.add(type);
			}
		}
	}
	
	public void GreedySheriffStrategy(Basic basicPlayer, Bribe bribePlayer) {
		if(basicPlayer != null) {
			this.money += super.basicPlayerInspection(basicPlayer);
		}
		if(bribePlayer != null) {
			if(bribePlayer.bribe != 0) {
				this.money += bribePlayer.bribe;
				bribePlayer.money -= bribePlayer.bribe;
			}
			else {
				this.money += super.bribePlayerInspection(bribePlayer);
			}
				
		}
	}
	
}
