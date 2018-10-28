package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bribe extends Basic{

	public InstantiateGoods G;
	Bribe(List<Integer> Hand, int money, boolean isSheriff) {
		super(Hand, money, isSheriff);
		G = new InstantiateGoods();
		G.create();
	}
	
	public List<Integer> getMaxIllegalGoods() {
		List<Integer> vec = new ArrayList<Integer>();
		for(int it : super.Hand) {
			if(G.goods[it].type.equals("Illegal")){
				vec.add(it);
			}
		}
		Collections.sort(vec, Collections.reverseOrder());
		return vec;
	}
	
	public void BribeComerciantStrategy() {
		List<Integer> maxIllegalGoods = getMaxIllegalGoods();
		if(maxIllegalGoods.size() > 2 && super.money >= 10) {
			for(int i : maxIllegalGoods) {
				super.bag.add(i);
				super.declaration.add(0);
			}
			super.bribe = 10;
			super.Hand.removeAll(super.bag);
			return;
		}
		
		if(maxIllegalGoods.size() > 0 && super.money >= 5) {
			int steps = 0;
			for(int i : maxIllegalGoods) {
				if(steps == 2)
					break;
				super.bag.add(i);
				super.declaration.add(0);
				++ steps;
			}
			super.bribe = 5;
			super.Hand.removeAll(super.bag);
			return;
		}
		
		super.BasicComerciantStrategy();
	}
	
	public void BribeSheriffStrategy(Basic basicPlayer, Greedy greedyPlayer) {
		if(basicPlayer != null) {
			this.money += super.basicPlayerInspection(basicPlayer);
			
		}
		if(greedyPlayer != null) {
			this.money += super.greedyPlayerInspection(greedyPlayer);
		}
	}

}
