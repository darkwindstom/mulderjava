package home.game.blackjack;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import org.apache.commons.lang.math.RandomUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import home.game.blackjack.JackBean;

public class BlackJack {

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
	
	
	public void get_first_jack(JackBean user, JackBean com){
		rjack_user = rjack_user + user.getValue();
		System.out.println("你的初始牌組為: "+user.getName()+"_"+user.getNum()+" 共"+rjack_user+"點");
		rjack_com = rjack_com + com.getValue();
		System.out.println("電腦初始牌組為: "+com.getName()+"_"+com.getNum()+" 共"+rjack_com+"點");
		
	}
	
	
	
	public void display_jack(JackBean user, JackBean com){
		
		rjack_user = rjack_user + user.getValue();
		System.out.println("你的牌組為: "+user.getName()+"_"+user.getNum()+" 共"+rjack_user+"點");
		
		//rjack_com = rjack_com + com.getValue();
		//System.out.println("電腦牌組為: "+com.getName()+"_"+com.getNum()+" 共"+rjack_com+"點");
		
	}
	
	
	
	public void com_computing(){
		
		JackBean com = get_jack();
		
		if(rjack_com + com.getValue() <= 20){
			
			rjack_com = rjack_com + com.getValue();
			System.out.println("電腦加牌");
			//System.out.println("144: "+rjack_com);
			
		}else {
			System.out.println("電腦不加牌");
			//System.out.println("148: "+rjack_com);
			
		}
		
	}
	
	
	public boolean count_user_jack_point(){
		
		boolean t = false;
		
		if(rjack_user >= 22){
						
			System.out.println("你爆點了");
			money = money - (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: "+money+" 輸了: "+gamble_money * gamble_num);
			
			t = true;
			
		}else if(rjack_com >= 22){
			
			System.out.println("電腦爆點了");
			money = money + (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: "+money+" 贏了: "+gamble_money * gamble_num);
			
			t = true;
			
		}else if(rjack_user == 21){
			
			System.out.println("最大點21點! 恭喜你贏了!");
			money = money + (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: "+money+" 贏了: "+gamble_money * gamble_num);
			
			t = true;
		
		}else if(rjack_com == 21){
			
			System.out.println("電腦得到最大點21點!你輸了!");
			money = money - (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: "+money+" 輸了: "+gamble_money * gamble_num);
			
			t = true;
		
		}/*else if(rjack_user == rjack_com){
			
			System.out.println("你的點數為: "+rjack_user+" 電腦的點數為: "+rjack_com+" 平手!");
			//money = money - (gamble_money * gamble_num);
			System.out.println("你的剩下金額為: "+money);
			
			t = true;
			
		}*/	
		
		return t;
	}
	
	
	public boolean count_stop_add_jack(){
		
		boolean t = true;
		
		if(rjack_user > rjack_com){
			
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("176: "+e.getMessage());
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("181: "+e.getMessage());
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
			
			System.out.println("207: "+e.getMessage());
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
			e.printStackTrace();
			System.out.println("241: "+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("244: "+e.getMessage());
		}
		
	}
	
	
	public void begin(){
		
		Scanner scanner = null;
		
		boolean t = true;
		
		JackBean user = null;
		JackBean com = null;
		
		int as;
		
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
		
		//開始配發第一次牌
		user =  get_jack();
		com = get_jack();
		
		//顯示資訊
		get_first_jack(user, com);
		
		do{
			
			scanner = new Scanner(System.in);
			
			try{

				if(count_jack < 51){
					System.out.print("加牌(1)停止(2): ");
					
					as = scanner.nextInt();
					
					if(as == 1){
						
						user =  get_jack();
						
						//電腦計算核心
						//com = get_jack();
						com_computing();
						
						display_jack(user, com);
						t = count_user_jack_point();
						
					}else {
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
