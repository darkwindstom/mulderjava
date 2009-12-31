package home.game.blackjack;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import home.game.blackjack.JackBean;


public class BlackJack {

	private static Logger logger = Logger.getLogger(BlackJack.class);
	
	private int money = 10000;
	private int gamble_money = 0;
	private int gamble_num =1;
	private int rjack_user = 0;	
	private int rjack_com = 0;
	private int count_jack = 0;

	/**
	 * 判斷money是否大於1000或是負的
	 * @param money
	 * @return
	 */
	public boolean check_money(int money){
		
		if(money >1000 || money < 1){
			errmsg_money();
			return false;
		}
		
		return true;
		
	}
		
	private void errmsg_money() {
		System.out.println("only chose 1~1000 doller");
	}
	
	/**
	 * 判斷gamble_num是否大於10或是負的
	 * @param gamble_num
	 * @return
	 */
	public boolean check_gamble_num(int gamble_num){
		
		if(gamble_num >10 || gamble_num < 1){
			errmsg_gamble_num();
			return false;
		}
		
		return true;
		
	}
		
	private void errmsg_gamble_num() {
		System.out.println("only chose 1~10");
	}
	
	/**
	 * 儲存下注金額  顯示資訊
	 */
	private void gamble_info(){
		System.out.println("你下注金額為 : "+gamble_money);
		System.out.println("你的賭率為 : "+gamble_num);
		//gamble_money = gamble_money * gamble_num;
		//money = money - gamble_money;
		System.out.println("你剩下的金額為 : "+(money - gamble_money));
		
	}
	
	
	
	/**
	 * 抽牌直到抽到不是已經抽過的牌
	 * @return
	 */
	public JackBean get_jack(){
	
		boolean t = true;
		
		JackBean jb = new JackBean();
			
		do{

			jb = random_jack();
		
			if(jb.getUse().equals("1")){		
				t = false;
				//System.out.println("95: "+jb.getUse()+"|"+t);
				
			}else {
				t = true;
				
			}
		
			//System.out.println("97: "+jb.getUse());
			
		}while(!t);
		
		set_used_jack(jb.getSn());
		
		//System.out.println("103: "+jb.getSn());
		
		count_jack++;
		
		return jb;
	}
	
	/**
	 * 初次發牌，一次發兩張，玩家兩張都顯示
	 * @param user - JackBean
	 * @return rjack_user - int
	 */
	public void get_first_user_jack(JackBean user, int i){
		rjack_user = rjack_user + user.getValue();
		
		if(i == 0){
			System.out.println("你的牌組為: "+user.getName()+"_"+user.getNum());
		}else if(i == 1){
			System.out.println("你的牌組為: "+user.getName()+"_"+user.getNum()+" 共"+rjack_user+"點");
		}
		
	}
	
	/**
	 * 初次發牌，一次發兩張，電腦只顯示一張
	 * @param com - JackBean
	 */
	public void get_first_com_jack(JackBean com, int i){
		rjack_com = rjack_com + com.getValue();
		
		if(i == 0){
			System.out.println("電腦牌組為: "+com.getName()+"_"+com.getNum());
		}else if(i == 1){
			System.out.println("電腦牌組為: **********"+" 共"+(rjack_com - com.getValue())+"點");			
		}
		
	}
	
	
	
	/**
	 * 顯示玩家的點數
	 * @param user
	 */
	public void display_jack(JackBean user){
		
		rjack_user = rjack_user + user.getValue();
		System.out.println("你的牌組為: "+user.getName()+"_"+user.getNum()+" 共"+rjack_user+"點");
		
	}
	
	
	
	public void com_computing(){
		
		JackBean com = new JackBean();
		
		boolean t = true;
		
		do{
			
			com = get_jack();

			if(com.getValue() <= 5){
				//System.out.println("147:");
				rjack_com = rjack_com + com.getValue();
				System.out.println("電腦加牌");
				
				t = true;
				
			}else if(rjack_com < rjack_user){
				//System.out.println("154:");
				rjack_com = rjack_com + com.getValue();
				System.out.println("電腦加牌");
				
				t = true;
				
			}else {
				
				if(rjack_com + com.getValue() <= 20){
					//System.out.println("163:");
					rjack_com = rjack_com + com.getValue();
					System.out.println("電腦加牌");
					//System.out.println("144: "+rjack_com);
					
					t = true;
					
				}else {
					//System.out.println("171:");
					System.out.println("電腦不加牌");
					//System.out.println("148: "+rjack_com);
					
					t = false;
					
				}
				
			}
		
		}while(t);
		
		
	}
	
	
	public boolean count_user_jack_point(){
		
		boolean t = false;
		
		if(rjack_user >= 22){
						
			System.out.println("你爆點了");
			money = money - (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: "+money+" 輸了: "+gamble_money * gamble_num);
			
			t = true;
			
		}else if(rjack_user == 21){
			
			System.out.println("最大點21點! 恭喜你贏了!");
			money = money + (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: "+money+" 贏了: "+gamble_money * gamble_num);
			
			t = true;
		
		}
		
		return t;
	}
	
	
	public boolean count_stop_add_jack(){
		
		boolean t = true;
		
		
		if(rjack_com >= 22){
		
			System.out.println("電腦爆點了: "+rjack_com+"點");
			money = money + (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: "+money+" 贏了: "+gamble_money * gamble_num);
			
			t = true;
		
		}else if(rjack_com == 21){
		
			System.out.println("電腦得到最大點21點!你輸了!");
			money = money - (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: "+money+" 輸了: "+gamble_money * gamble_num);
			
			t = true;
	
		}else if(rjack_user > rjack_com){
			
			System.out.println("你的點數為: "+rjack_user+" 電腦的點數為: "+rjack_com+" 你贏了!");
			money = money + (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: "+money+" 贏了: "+gamble_money * gamble_num);
			
			t = true;
			
		}else if(rjack_user < rjack_com){
		
			System.out.println("你的點數為: "+rjack_user+" 電腦的點數為: "+rjack_com+" 你輸了!");
			money = money - (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: "+money+" 輸了: "+gamble_money * gamble_num);
			
			t = true;
			
		}else if(rjack_user == rjack_com){
			
			System.out.println("你的點數為: "+rjack_user+" 電腦的點數為: "+rjack_com+" 平手!");
			//money = money - (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: "+money);
			
			t = true;
			
		}
		
		return t;		
	}
	
	
	
	
	/**
	 * 抽過的牌，use記錄為"1"
	 */
	public void set_used_jack(String sn){
		
		SAXReader reader = new SAXReader();
		Document config_xml;
		
		try {
			
			config_xml = reader.read("jack.xml");
					
			Element element = (Element) config_xml.selectSingleNode("/root/jack[@sn='"+sn+"']");			
			if(element != null){
				element.addAttribute("use", "1");	
			}
			
			XMLWriter writer = new XMLWriter(new FileWriter("jack.xml"));	
			writer.write(config_xml);			
			writer.close(); 
		
		} catch (DocumentException e) {
			logger.error("line 315:"+e.getMessage(), e);
			create_jack_xml();
		
		} catch (IOException e) {
			logger.error("line 319:"+e.getMessage(), e);
			create_jack_xml();
			
		}		
	}
	
	
	public JackBean random_jack(){
		
		JackBean jb = new JackBean();
		try {
			
			int rand = RandomUtils.nextInt(52);
			SAXReader reader = new SAXReader();
			Document config_xml = reader.read("jack.xml");			
			Element element = (Element) config_xml.selectSingleNode("/root/jack[@sn='"+rand+"']");			
			if(element != null){
				jb.setSn(element.attributeValue("sn"));				
				jb.setName(element.attributeValue("name"));
				jb.setNum(element.attributeValue("num"));
				jb.setValue(Integer.valueOf(element.attributeValue("value")));
				jb.setUse(element.attributeValue("use"));	
			}
			
		} catch (DocumentException e) {
			logger.error("line 344:"+e.getMessage(), e);
			create_jack_xml();
			
		}
		
		return jb;
	}
	
	
	/*
	 * 初始化撲克牌 
	 */
	public void reset_jack(){
		
		SAXReader reader = new SAXReader();
		Document config_xml;
		
		try	{
			
			config_xml = reader.read("jack.xml");
			
			for(int i=0; i<=51; i++){
				Element element = (Element) config_xml.selectSingleNode("/root/jack[@sn='"+i+"']");			
				if(element != null){
					element.addAttribute("use", "0");
				}
			}
			
			XMLWriter writer = new XMLWriter(new FileWriter("jack.xml"));
			writer.write(config_xml);		
			writer.close();
			
			count_jack = 0;
			rjack_user = 0;
			rjack_com = 0;
			
		} catch (DocumentException e) {
			logger.error("line 380:"+e.getMessage(), e);
			create_jack_xml();

		} catch (IOException e) {
			logger.error("line 383:"+e.getMessage(), e);
			create_jack_xml();

		}
		
	}
	
	
	/**
	 * 產生jack.xml
	 */
	public void create_jack_xml(){
		
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("root");
		
		String name[] = {"Spade", "Heart", "Diamond", "Club"}; 
		String num[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q" ,"K"};
		int value[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10 ,10};
		int j = 0;
		
		for(int i=0; i<=12; i++){
			root.addElement("jack").addAttribute("sn", String.valueOf(i)).addAttribute("name", name[0]).addAttribute("num", num[i]).addAttribute("value", String.valueOf(value[i])).addAttribute("use", "0");
		}
		
		for(int i=13; i<=25; i++){
			root.addElement("jack").addAttribute("sn", String.valueOf(i)).addAttribute("name", name[1]).addAttribute("num", num[j]).addAttribute("value", String.valueOf(value[j])).addAttribute("use", "0");
			j++;
		}
		
		j = 0; 
		for(int i=26; i<=38; i++){
			root.addElement("jack").addAttribute("sn", String.valueOf(i)).addAttribute("name", name[2]).addAttribute("num", num[j]).addAttribute("value", String.valueOf(value[j])).addAttribute("use", "0");
			j++;
		}
		
		j = 0;
		for(int i=39; i<=51; i++){
			root.addElement("jack").addAttribute("sn", String.valueOf(i)).addAttribute("name", name[3]).addAttribute("num", num[j]).addAttribute("value", String.valueOf(value[j])).addAttribute("use", "0");
			j++;
		}
		
		try {
			
			XMLWriter writer = new XMLWriter(new FileWriter("jack.xml"));	
			writer.write(doc);			
			writer.close();
		
		} catch (IOException e) {
			logger.error("434:無法產生xml "+e.getMessage(), e);
			
		}
		
	}
	
	/**
	 * 遊戲開始
	 */
	public void begin(){
		
		Scanner scanner = null;
		
		boolean t = true;
		
		JackBean user = null;
		JackBean com = null;
		
		int as;
		
		//檢查jack.xml
		try {
			Reader rd = new FileReader("jack.xml");
		} catch (FileNotFoundException e) {
			//找不到jack.xml再產生jack.xml
			logger.error("找不到jack.xml所以產生", e);
			create_jack_xml();
			logger.info("產生完成");
		}
		
		do{
			
			scanner = new Scanner(System.in);
			
			try{
				
				System.out.println("你目前金額為: "+money);
				System.out.print("下注金額 : ");
				gamble_money = scanner.nextInt();
				
				if(!check_money(gamble_money)){					
					t = false;					
				
				}else {												
					t = true;
					
				}
				
			}catch(java.util.InputMismatchException ex){
				
				System.out.print("請輸入1~1000數字.....");	
				t = false;
				
			}

		}while(!t);
				  
		do{	  
			
			scanner = new Scanner(System.in);
			
			try{
				
				System.out.print("賭率  : ");
				gamble_num = scanner.nextInt();
				
				if(!check_gamble_num(gamble_num)){
					t = false;
					
				}else {
					t = true;
					
				}
				
			}catch(java.util.InputMismatchException ex){
				
				System.out.print("請輸入1~10數字.....");	
				t = false;
				
			}

		}while(!t);
		
		gamble_info();	//顯示資訊
		
		//開始配發第一次牌，玩家電腦各發兩張牌
		for(int i=0; i<2; i++){
			user = get_jack();	
			//顯示資訊
			get_first_user_jack(user, i);
		}
		System.out.println("--------------------------------");
		for(int i=0; i<2; i++){
			com = get_jack();
			//顯示資訊
			get_first_com_jack(com, i);
		}
		
		
		do{
			
			scanner = new Scanner(System.in);
			
			try{

				if(count_jack < 51){
					System.out.print("加牌(1)停止(2): ");
					
					as = scanner.nextInt();
					
					if(as == 1){
						
						user = get_jack();
						display_jack(user);
						t = count_user_jack_point();
						
					}else {
						
						//電腦計算核心
						com_computing();
						
						//System.out.println("376:");
						t = count_stop_add_jack();
						
					}
					
				}else {
					t = true;
					
				}
			
			}catch(java.util.InputMismatchException ex){
				
				System.out.print("請輸入加牌(1)停止(2).....");	
				t = false;
				
			}
			
		}while(!t);
		
		//重設
		reset_jack();
				
		int yn = 0;
		
		do{
			
			System.out.println("要再玩嗎?");
			System.out.print("請輸入1(繼續)或2(離開): ");
		
			scanner = new Scanner(System.in);
		    
			try{
		    	
				yn = scanner.nextInt();
		    
			}catch(java.util.InputMismatchException ex){
		    	
				System.out.print("請輸入1(繼續)或2(離開.....");	
				t = false;
					
			}
		    
		}while(!t);

		
		if(yn == 1){
			begin();
		}
		
		//System.out.println("close");
		
	}
	
	public static void main(String[] args) {
		
		BlackJack bj = new BlackJack();
		
		bj.begin();
		
		
		
	}
	
	
}
