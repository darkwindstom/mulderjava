package home.game.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class BlackJack {

	private int money = 10000;
	private int gamble_money = 0;
	private int gamble_num = 1;

	private List<JackBean> deck;
	private List<JackBean> userHand;
	private List<JackBean> comHand;

	public BlackJack() {
		deck = new ArrayList<>();
		userHand = new ArrayList<>();
		comHand = new ArrayList<>();
	}

	/**
	 * 判斷money是否大於1000或是負的
	 */
	public boolean check_money(int money){
		if(money > 1000 || money < 1){
			System.out.println("only chose 1~1000 doller");
			return false;
		}
		return true;
	}

	/**
	 * 判斷gamble_num是否大於10或是負的
	 */
	public boolean check_gamble_num(int gamble_num){
		if(gamble_num > 10 || gamble_num < 1){
			System.out.println("only chose 1~10");
			return false;
		}
		return true;
	}

	/**
	 * 儲存下注金額  顯示資訊
	 */
	private void gamble_info(){
		System.out.println("你下注金額為 : " + gamble_money);
		System.out.println("你的賭率為 : " + gamble_num);
		System.out.println("你剩下的金額為 : " + (money - gamble_money));
	}

	/**
	 * 初始化撲克牌
	 */
	public void reset_jack(){
		deck.clear();
		userHand.clear();
		comHand.clear();

		String[] name = {"Spade", "Heart", "Diamond", "Club"};
		String[] num = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q" ,"K"};
		int[] value = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};

		int sn = 0;
		for (String suit : name) {
			for (int i = 0; i < 13; i++) {
				JackBean jb = new JackBean();
				jb.setSn(String.valueOf(sn++));
				jb.setName(suit);
				jb.setNum(num[i]);
				jb.setValue(value[i]);
				deck.add(jb);
			}
		}

		Collections.shuffle(deck);
	}

	/**
	 * 抽牌
	 */
	public JackBean get_jack() {
		if (deck.isEmpty()) {
			return null;
		}
		return deck.remove(0);
	}

	/**
	 * 計算手牌分數，考慮 A 可能是 1 或 11
	 */
	public int calculateScore(List<JackBean> hand) {
		int score = 0;
		int aces = 0;
		for (JackBean jb : hand) {
			if ("1".equals(jb.getNum())) {
				aces++;
				score += 11;
			} else {
				score += jb.getValue();
			}
		}

		while (score > 21 && aces > 0) {
			score -= 10;
			aces--;
		}
		return score;
	}

	/**
	 * 初次發牌給玩家
	 */
	public void get_first_user_jack() {
		userHand.add(get_jack());
		userHand.add(get_jack());
		System.out.print("你的牌組為: ");
		for (JackBean jb : userHand) {
			System.out.print(jb.getName() + "_" + jb.getNum() + " ");
		}
		System.out.println("共" + calculateScore(userHand) + "點");
	}

	/**
	 * 初次發牌給電腦
	 */
	public void get_first_com_jack() {
		comHand.add(get_jack());
		comHand.add(get_jack());
		System.out.print("電腦牌組為: ");
		JackBean first = comHand.get(0);
		System.out.print(first.getName() + "_" + first.getNum() + " ********** ");
		int firstScore = 0;
		if ("1".equals(first.getNum())) {
			firstScore = 11;
		} else {
			firstScore = first.getValue();
		}
		System.out.println("共" + firstScore + "點");
	}

	/**
	 * 顯示玩家牌組與點數
	 */
	public void display_user_jack() {
		System.out.print("你的牌組為: ");
		for (JackBean jb : userHand) {
			System.out.print(jb.getName() + "_" + jb.getNum() + " ");
		}
		System.out.println("共" + calculateScore(userHand) + "點");
	}

	public void display_com_jack() {
		System.out.print("電腦牌組為: ");
		for (JackBean jb : comHand) {
			System.out.print(jb.getName() + "_" + jb.getNum() + " ");
		}
		System.out.println("共" + calculateScore(comHand) + "點");
	}

	/**
	 * 當使用者停牌後的統計點數
	 */
	public boolean count_user_jack_point(){
		int score = calculateScore(userHand);
		if(score >= 22){
			System.out.println("你爆點了");
			money = money - (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: " + money + " 輸了: " + gamble_money * gamble_num);
			return true;
		}else if(score == 21){
			System.out.println("最大點21點! 恭喜你贏了!");
			money = money + (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: " + money + " 贏了: " + gamble_money * gamble_num);
			return true;
		}
		return false;
	}

	/**
	 * 電腦 AI
	 */
	public void com_computing(){
		System.out.println("--- 換電腦行動 ---");
		display_com_jack();
		while(true) {
			int comScore = calculateScore(comHand);
			if (comScore < 17) {
				System.out.println("電腦加牌");
				comHand.add(get_jack());
				display_com_jack();
			} else {
				System.out.println("電腦不加牌");
				break;
			}
		}
	}

	/**
	 * 當電腦停牌後的統計點數
	 */
	public boolean count_stop_add_jack(){
		int rjack_user = calculateScore(userHand);
		int rjack_com = calculateScore(comHand);

		if(rjack_com >= 22){
			System.out.println("電腦爆點了: " + rjack_com + "點");
			money = money + (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: " + money + " 贏了: " + gamble_money * gamble_num);
			return true;
		}else if(rjack_com == 21 && rjack_user != 21){
			System.out.println("電腦得到最大點21點!你輸了!");
			money = money - (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: " + money + " 輸了: " + gamble_money * gamble_num);
			return true;
		}else if(rjack_user > rjack_com){
			System.out.println("你的點數為: " + rjack_user + " 電腦的點數為: " + rjack_com + " 你贏了!");
			money = money + (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: " + money + " 贏了: " + gamble_money * gamble_num);
			return true;
		}else if(rjack_user < rjack_com){
			System.out.println("你的點數為: " + rjack_user + " 電腦的點數為: " + rjack_com + " 你輸了!");
			money = money - (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: " + money + " 輸了: " + gamble_money * gamble_num);
			return true;
		}else if(rjack_user == rjack_com){
			System.out.println("你的點數為: " + rjack_user + " 電腦的點數為: " + rjack_com + " 平手!");
			System.out.println("你的剩下金額為: " + money);
			return true;
		}
		return true;
	}

	/**
	 * 遊戲開始
	 */
	public void begin(){
		Scanner scanner = new Scanner(System.in);
		boolean t = true;

		do {
			System.out.println("你目前金額為: " + money);
			System.out.print("下注金額 : ");
			try {
				gamble_money = Integer.parseInt(scanner.nextLine().trim());
				t = check_money(gamble_money);
			} catch (NumberFormatException e) {
				System.out.println("請輸入1~1000數字.....");
				t = false;
			}
		} while(!t);

		do {
			System.out.print("賭率  : ");
			try {
				gamble_num = Integer.parseInt(scanner.nextLine().trim());
				t = check_gamble_num(gamble_num);
			} catch (NumberFormatException e) {
				System.out.println("請輸入1~10數字.....");
				t = false;
			}
		} while(!t);

		gamble_info();

		reset_jack();

		get_first_user_jack();
		System.out.println("--------------------------------");
		get_first_com_jack();

		// 檢查初次發牌是否已經 21 點
		t = count_user_jack_point();

		if (!t && calculateScore(comHand) == 21) {
			display_com_jack();
			System.out.println("電腦直接得到最大點21點!你輸了!");
			money = money - (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: " + money + " 輸了: " + gamble_money * gamble_num);
			t = true;
		}

		while (!t) {
			System.out.print("加牌(1)停止(2): ");
			try {
				int as = Integer.parseInt(scanner.nextLine().trim());
				if(as == 1){
					userHand.add(get_jack());
					display_user_jack();
					t = count_user_jack_point();
				} else if (as == 2) {
					com_computing();
					t = count_stop_add_jack();
				} else {
					System.out.println("請輸入加牌(1)停止(2).....");
				}
			} catch (NumberFormatException e) {
				System.out.println("請輸入加牌(1)停止(2).....");
			}
		}

		int yn = 0;
		do {
			System.out.println("要再玩嗎?");
			System.out.print("請輸入1(繼續)或2(離開): ");
			try {
				yn = Integer.parseInt(scanner.nextLine().trim());
				if (yn == 1 || yn == 2) {
					t = true;
				} else {
					t = false;
				}
			} catch (NumberFormatException e) {
				System.out.println("請輸入1(繼續)或2(離開.....");
				t = false;
			}
		} while(!t);

		if (yn == 1) {
			begin();
		}
	}

	public static void main(String[] args) {
		BlackJack bj = new BlackJack();
		bj.begin();
	}
}
