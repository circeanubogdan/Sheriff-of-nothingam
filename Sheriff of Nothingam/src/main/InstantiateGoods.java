package main;

public class InstantiateGoods {
	public Goods[] goods;

	public void create() {
		goods = new Goods[20];
		for (int i = 0; i < 20; ++i) {
			goods[i] = new Goods();
		}

		goods[0].name = "Apple";
		goods[1].name = "Cheese";
		goods[2].name = "Bread";
		goods[3].name = "Chicken";
		goods[10].name = "Silk";
		goods[11].name = "Pepper";
		goods[12].name = "Barrel";

		goods[0].type = goods[1].type = goods[2].type = goods[3].type = "Legal";
		goods[10].type = goods[11].type = goods[12].type = "Illegal";

		goods[0].profit = 2;
		goods[1].profit = 3;
		goods[2].profit = goods[3].profit = 4;
		goods[10].profit = 9;
		goods[11].profit = 8;
		goods[12].profit = 7;

		goods[0].penalty = goods[1].penalty = goods[2].penalty = goods[3].penalty = 2;
		goods[10].penalty = goods[11].penalty = goods[12].penalty = 4;

		goods[10].bonusID = 1;
		goods[10].nrBonus = 3;
		goods[11].bonusID = 3;
		goods[11].nrBonus = 2;
		goods[12].bonusID = 2;
		goods[12].nrBonus = 2;
	}
}
