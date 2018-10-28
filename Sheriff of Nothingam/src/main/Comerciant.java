package main;

import java.util.ArrayList;
import java.util.List;

public class Comerciant {
	public List<Integer> Hand;
	public int bribe;
	public List<Integer> bag;
	public List<Integer> declaration;
	public int money;
	public boolean isSheriff;
	public boolean guilty;
	public List<Integer> Shop; 
	
	Comerciant(List<Integer> Hand, int money, boolean isSheriff){
		this.Hand = Hand;
		this.money = money;
		this.isSheriff = isSheriff;
		this.Shop = new ArrayList<Integer>();
		bag = new ArrayList<Integer>();
		declaration = new ArrayList<Integer>();
	}
	
	
	
}
