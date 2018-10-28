package main;

import java.util.ArrayList;
import java.util.List;

public class Basic extends Comerciant{

	public InstantiateGoods G;
	Basic(List<Integer> Hand, int money, boolean isSheriff) {
		super(Hand, money, isSheriff);
		G = new InstantiateGoods();
		G.create();
	}
	
	public int checkIfIllegal() {
		int max = 0;
		for(int it : super.Hand) {
			if(G.goods[it].type.equals("Legal"))
				return 0;
			if(G.goods[it].profit > G.goods[max].profit)
				max = it;
		}
		return max;
	}
	
	public int getMaxFreq() {
		int maxFreq = -1, maxGood = 0;
		int[] freq = new int[20];
		for(int it : super.Hand) {
			++ freq[it];
		}
		for(int i = 0; i < 4; ++ i) {
			if(freq[i] >= maxFreq) {
				maxFreq = freq[i];
				maxGood = i;
			}
		}
		return maxGood;
	}
	
	public void BasicComerciantStrategy() {
		List<Integer> toRemove = new ArrayList<>();
		int max = checkIfIllegal();
		if(max != 0) {
			super.bag.add(max);
			super.declaration.add(0);
			for(int it : super.Hand) {
				if(it == max) {
					toRemove.add(it);
				}
			}
			super.Hand.removeAll(toRemove);
			return;
		}
		int maxFreq = getMaxFreq();
		for(int it : super.Hand) {
			if(super.bag.size() == 5)
				break;
			if(it == maxFreq) {
				super.bag.add(it);
				super.declaration.add(it);
				
			}
		}
		super.Hand.removeAll(super.bag);
	}
	
	public boolean isLying(List<Integer> bag, List<Integer> declaration) {
		if(bag.size() != declaration.size())
			return true;
		for(int i = 0; i < bag.size(); ++ i) {
			if(bag.get(i) != declaration.get(i))
				return true;
		}
		return false;
	}
	
	public int greedyPlayerInspection(Greedy greedyPlayer) {
		int currentMoney = 0;
		if(isLying(greedyPlayer.bag, greedyPlayer.declaration)) {
			greedyPlayer.guilty = true;
			for(int it : greedyPlayer.bag) {
				if(G.goods[it].type.equals("Illegal")) {
					currentMoney += G.goods[it].penalty;
					greedyPlayer.money -= G.goods[it].penalty;
				}
			}
		}
		else {
			for(int it : greedyPlayer.bag) {
				currentMoney -= G.goods[it].penalty;
				greedyPlayer.money += G.goods[it].penalty;
			}
		}
		return currentMoney;
	}
	
	public int bribePlayerInspection(Bribe bribePlayer) {
		int currentMoney = 0;
		if(isLying(bribePlayer.bag, bribePlayer.declaration)) {
			bribePlayer.guilty = true;
			for(int it : bribePlayer.bag) {
				if(G.goods[it].type.equals("Illegal")) {
					currentMoney += G.goods[it].penalty;
					bribePlayer.money -= G.goods[it].penalty;
				}
			}
		}
		else {
			for(int it : bribePlayer.bag) {
				currentMoney -= G.goods[it].penalty;
				bribePlayer.money += G.goods[it].penalty;
			}
		}
		return currentMoney;
	}
	
	public int basicPlayerInspection(Basic basicPlayer) {
		int currentMoney = 0;
		if(isLying(basicPlayer.bag, basicPlayer.declaration)) {
			basicPlayer.guilty = true;
			for(int it : basicPlayer.bag) {
				if(G.goods[it].type.equals("Illegal")) {
					currentMoney += G.goods[it].penalty;
					basicPlayer.money -= G.goods[it].penalty;
				}
			}
		}
		else {
			for(int it : basicPlayer.bag) {
				currentMoney -= G.goods[it].penalty;
				basicPlayer.money += G.goods[it].penalty;
			}
		}
		return currentMoney;
	}
	
	
	
	public void BasicSheriffStrategy(Bribe bribePlayer, Greedy greedyPlayer) {
		if(bribePlayer != null) {
			this.money += bribePlayerInspection(bribePlayer);
			
		}
		if(greedyPlayer != null) {
			this.money += greedyPlayerInspection(greedyPlayer);
		}
	}
}
