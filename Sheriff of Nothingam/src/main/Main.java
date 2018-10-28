package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthOptionPaneUI;


public final class Main {

    private static final class GameInputLoader {
        private final String mInputPath;

        private GameInputLoader(final String path) {
            mInputPath = path;
        }

        public GameInput load() {
            List<Integer> assetsIds = new ArrayList<>();
            List<String> playerOrder = new ArrayList<>();

            try {
                BufferedReader inStream = new BufferedReader(new FileReader(mInputPath));
                String assetIdsLine = inStream.readLine().replaceAll("[\\[\\] ']", "");
                String playerOrderLine = inStream.readLine().replaceAll("[\\[\\] ']", "");

                for (String strAssetId : assetIdsLine.split(",")) {
                    assetsIds.add(Integer.parseInt(strAssetId));
                }

                for (String strPlayer : playerOrderLine.split(",")) {
                    playerOrder.add(strPlayer);
                }
                inStream.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return new GameInput(assetsIds, playerOrder);
        }
    }
    
    public static void reset(Basic basicPlayer, Greedy greedyPlayer, Bribe bribePlayer, List<Integer> cards, List<String> mPlayersOrder) {
    	for(int i = 0; i < mPlayersOrder.size(); ++ i) {
	    	if(basicPlayer != null && mPlayersOrder.get(i).equals("basic")) {
	    		while(basicPlayer.Hand.size() < 6) {
	    			basicPlayer.Hand.add(cards.get(0));
	    			cards.remove(0);
	    		}
	    		if(basicPlayer.guilty == false) {
	    			basicPlayer.Shop.addAll(basicPlayer.bag);
	    		}
	    		basicPlayer.bag.clear();
	    		basicPlayer.declaration.clear();
	    		basicPlayer.isSheriff = false;
	    		basicPlayer.guilty = false;
	    	}
	    	if(greedyPlayer != null && mPlayersOrder.get(i).equals("greedy")) {
	    		while(greedyPlayer.Hand.size() < 6) {
	    			greedyPlayer.Hand.add(cards.get(0));
	    			cards.remove(0);
	    		}
	    		if(greedyPlayer.guilty == false) {
	    			greedyPlayer.Shop.addAll(greedyPlayer.bag);
	    		}
	    		else {
	    			for(int it : greedyPlayer.bag) {
	    				if(greedyPlayer.G.goods[it].type.equals("Legal")) {
	    					greedyPlayer.Shop.add(it);
	    				}
	    			}
	    		}
	    		greedyPlayer.bag.clear();
	    		greedyPlayer.declaration.clear();
	    		greedyPlayer.isSheriff = false;
	    		greedyPlayer.guilty = false;
	    	}
	    	if(bribePlayer != null && mPlayersOrder.get(i).equals("bribed")) {
	    		while(bribePlayer.Hand.size() < 6) {
	    			bribePlayer.Hand.add(cards.get(0));
	    			cards.remove(0);
	    		}
	    		if(bribePlayer.guilty == false) {
	    			bribePlayer.Shop.addAll(bribePlayer.bag);
	    			//bribePlayer.money -= bribePlayer.bribe;
	    		}
	    		else {
	    			for(int it : bribePlayer.bag) {
	    				if(bribePlayer.G.goods[it].type.equals("Legal")) {
	    					bribePlayer.Shop.add(it);
	    				}
	    			}
	
	    		}
	    		bribePlayer.bag.clear();
	    		bribePlayer.declaration.clear();
	    		bribePlayer.isSheriff = false;
	    		bribePlayer.guilty = false;
	    		bribePlayer.bribe = 0;
	    	}
    	}
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader("src/input/test8.in");
        GameInput gameInput = gameInputLoader.load();

        // TODO Implement the game logic.
        
        List<Integer> mAssetOrder = gameInput.getAssetIds();
        List<String> mPlayersOrder = gameInput.getPlayerNames();
        List<String> playersInit = new ArrayList<String>();
        for(String it : mPlayersOrder) {
        	playersInit.add(it);
        }
        
        List<Integer> cards = new ArrayList<>(mAssetOrder.size());
        
        
        int bonusKing[] = {20, 15, 15, 10};
        int bonusQueen[] = {10, 10, 10, 5};
        
        
        for(int i = 0 ; i < mAssetOrder.size() ; i++) {
        	cards.add(mAssetOrder.get(i));
        }
        
        Basic basicPlayer = null;
        Greedy greedyPlayer = null;
        Bribe bribePlayer = null;
        int runda = 1;
        List<ArrayList<Integer> > Hands = new ArrayList< ArrayList<Integer> >();
        for(int i = 0; i < mPlayersOrder.size(); ++ i) {
        	List<Integer> currentHand = new ArrayList<>();
        	for(int j = 1; j <= 6; ++ j) {
        		currentHand.add(cards.get(0));
        		cards.remove(0);
        	}
        	Hands.add((ArrayList<Integer>) currentHand);
        }
        for(int i = 0; i < mPlayersOrder.size(); ++ i) {
        	if(mPlayersOrder.get(i).equals("basic")) {
        		if(i == 0)
        			basicPlayer = new Basic(Hands.get(i), 50, true);
        		else
        			basicPlayer = new Basic(Hands.get(i), 50, false);
        	}
        	if(mPlayersOrder.get(i).equals("greedy")) {
        		if(i == 0)
        			greedyPlayer = new Greedy(Hands.get(i), 50, true);
        		else
        			greedyPlayer = new Greedy(Hands.get(i), 50, false);
        	}
        	if(mPlayersOrder.get(i).equals("bribed")) {
        		if(i == 0)
        			bribePlayer = new Bribe(Hands.get(i), 50, true);
        		else
        			bribePlayer = new Bribe(Hands.get(i), 50, false);
        	}
        }
        //System.out.println(basicPlayer.Hand);
        
        int round = 0;
        while(runda <= 2 * mPlayersOrder.size()) {
        	
        	if(mPlayersOrder.get(0).equals("basic")) {
        		++ round;
        		basicPlayer.isSheriff = true;
        		
        		if(bribePlayer != null)
        			bribePlayer.BribeComerciantStrategy();
        		if(greedyPlayer != null)
        			greedyPlayer.GreedyCommerciantStrategy(round);
        		basicPlayer.BasicSheriffStrategy(bribePlayer, greedyPlayer);
        	}
        	if(mPlayersOrder.get(0).equals("greedy")) {
        		greedyPlayer.isSheriff = true;
        		
        		if(bribePlayer != null)
        			bribePlayer.BribeComerciantStrategy();
        		if(basicPlayer != null)
        			basicPlayer.BasicComerciantStrategy();
        		greedyPlayer.GreedySheriffStrategy(basicPlayer, bribePlayer);
        	}
        	if(mPlayersOrder.get(0).equals("bribed")) {
        		++ round;
        		bribePlayer.isSheriff = true;
        		
        		if(basicPlayer != null)
        			basicPlayer.BasicComerciantStrategy();
        		if(greedyPlayer != null)
        			greedyPlayer.GreedyCommerciantStrategy(round);
        		bribePlayer.BribeSheriffStrategy(basicPlayer, greedyPlayer);
        		
        	}
        	reset(basicPlayer, greedyPlayer, bribePlayer, cards, playersInit);
        	mPlayersOrder.add(mPlayersOrder.get(0));
        	mPlayersOrder.remove(mPlayersOrder.get(0));
        	++ runda;
        }
        
        System.out.println(basicPlayer.Shop);
        System.out.println(greedyPlayer.Shop);
        System.out.println(bribePlayer.Shop);
        
        //System.out.println(cards);
        int mat[][] = new int[5][5];
        int punctaj[] = new int[5];
        
        if(basicPlayer != null) {
        	for(int it : basicPlayer.Shop) {
        		if(it <= 3) {
        			++ mat[it][0];
        		}
        		else {
        			mat[basicPlayer.G.goods[it].bonusID][0] += basicPlayer.G.goods[it].nrBonus;
        		}
        	}
        }
        
        if(greedyPlayer != null) {
        	for(int it : greedyPlayer.Shop) {
        		if(it <= 3) {
        			++ mat[it][1];
        		}
        		else {
        			mat[greedyPlayer.G.goods[it].bonusID][1] += greedyPlayer.G.goods[it].nrBonus;
        		}
        	}
        }
        
        if(bribePlayer != null) {
        	if(bribePlayer != null) {
            	for(int it : bribePlayer.Shop) {
            		if(it <= 3) {
            			++ mat[it][2];
            		}
            		else {
            			mat[bribePlayer.G.goods[it].bonusID][2] += bribePlayer.G.goods[it].nrBonus;
            		}
            	}
            }
        }
        
        for(int i = 0; i < 4; ++ i) {
        	int max1 = -1, max2 = -1;
        	for(int j = 0; j < 3; ++ j) {
        		if(mat[i][j] > max1) {
        			max2 = max1;
        			max1 = mat[i][j];
        		}
        		else {
        			if(mat[i][j] > max2 && mat[i][j] !=  max1) {
        				max2 = mat[i][j];
        			}
        		}
        	}
        	int nrmax1 = 0, max2type = 0;
        	if(basicPlayer != null) {
        		if(mat[i][0] == max1) {
        			punctaj[0] += bonusKing[i];
        			++ nrmax1;
        		}
        		else if(mat[i][0] == max2) {
        			punctaj[0] += bonusQueen[i];
        			max2type = 0;
        		}
        	}
        	
        	if(greedyPlayer != null) {
        		if(mat[i][1] == max1) {
        			++ nrmax1;
        			punctaj[1] += bonusKing[i];
        		}
        		else if(mat[i][1] == max2) {
        			punctaj[1] += bonusQueen[i];
        			max2type = 1;
        		}
        	}
        	
        	if(bribePlayer != null) {
        		if(mat[i][2] == max1) {
        			++ nrmax1;
        			punctaj[2] += bonusKing[i];
        		}
        		else if(mat[i][2] == max2) {
        			punctaj[2] += bonusQueen[i];
        			max2type = 2;
        		}
        	}
//        	if(nrmax1 == 2) {
//        		punctaj[max2type] -= bonusQueen[i];
//        	}
        
        }
        
        if(basicPlayer != null) {
        	punctaj[0] += basicPlayer.money;
        	for(int it : basicPlayer.Shop) {
        		punctaj[0] += basicPlayer.G.goods[it].profit;
        		if(it >= 10) {
        			punctaj[0] += basicPlayer.G.goods[it].nrBonus * basicPlayer.G.goods[basicPlayer.G.goods[it].bonusID].profit;
        		}
        	}
        }
        
        if(greedyPlayer != null) {
        	punctaj[1] += greedyPlayer.money;
        	for(int it : greedyPlayer.Shop) {
        		punctaj[1] += greedyPlayer.G.goods[it].profit;
        		if(it >= 10) {
        			punctaj[1] += greedyPlayer.G.goods[it].nrBonus * greedyPlayer.G.goods[greedyPlayer.G.goods[it].bonusID].profit;
        		}
        	}
        }
        
        if(bribePlayer != null) {
        	punctaj[2] += bribePlayer.money;
        	for(int it : bribePlayer.Shop) {
        		punctaj[2] += bribePlayer.G.goods[it].profit;
        		if(it >= 10) {
        			punctaj[2] += bribePlayer.G.goods[it].nrBonus * bribePlayer.G.goods[bribePlayer.G.goods[it].bonusID].profit;
        		}
        	}
        }
        System.out.println("BASIC: " + punctaj[0]);
        System.out.println("GREEDY: " + punctaj[1]);
        System.out.println("BRIBED: " + punctaj[2]);
//        System.out.println(bribePlayer.Shop);
//        System.out.println(greedyPlayer.Shop);
    }
}
