package home.game.gnum;

import java.util.LinkedHashSet;
import java.util.Scanner;

import org.apache.commons.lang.math.RandomUtils;

/**
 * guess num
 * @author darkwindstom
 *
 */
public class Gnum {

	public String com_random_num = "";
	
	public LinkedHashSet<Integer> random_num(int num){		
		
		LinkedHashSet<Integer> hs = new LinkedHashSet<Integer>();
											
		while(hs.size() < num){
			hs.add(RandomUtils.nextInt(9)+1);
		}
		
		for(Integer i : hs){			
			com_random_num += i;
		}
		
		//for(Integer i : hs){
		//	System.out.print(i);
		//}
		//System.out.println();
		
		return hs;

	}
	
	public int[] input;
	
	
	public boolean split_num(int num){
				
		int[] self = new int[4];
		
		boolean auth = true;
		
		for(int i=3; i>=0; i--){
			self[i] = num%10;
			num/=10;
		}
		
		for(int i=0; i<=3; i++){
			for(int j=0; j<=i; j++){	
				if(i != j){
					if(self[i] == self[j]){
						auth = false;
						break;
					}
				}
			}
		}
				
		if(auth){
			input = self;
		}
		
		//System.out.println(String.valueOf(auth));
		
		//for(Integer i : self){
		//	System.out.println(i);
		//}
				
		return auth;
		
	}
	
	public LinkedHashSet<Integer> save_set_num(){
		
		LinkedHashSet<Integer> hs = new LinkedHashSet<Integer>();
		
		for(int i=0; i<=3; i++){
			hs.add(input[i]);
		}
		
		//for(Integer i : hs){
		//	System.out.println(i);
		//}
				
		return hs;
		
	}
	
	public int count = 0;
	
	
	public boolean display_xAyB(LinkedHashSet<Integer> com, LinkedHashSet<Integer> user){
		
		boolean t = true;
			
		int a=0, b=0;
	
		Integer[] acom = com.toArray(new Integer[4]);
			
		Integer[] auser = user.toArray(new Integer[4]);
		
		for(int i=0; i<=3; i++){
				
			if(acom[i] == auser[i]){
	
				a++;			//數字相同位置相同
				
			}else {
	
				for(int j=0; j<=3; j++){
						
					if(acom[i] == auser[j]){
							
						b++;	//數字相同位置不同
						
					}				
				}
	
			}
	
		}
					
		//印出?A?B
		if(a <= 3){
			count++;
			System.out.println("第"+count+"次是"+a+"A"+b+"B");
			t = false;
				
		}else if(a == 4 && b == 0){
			count++;
			System.out.println("第"+count+"次猜對了!");
			t = true;

		}
		
		return t;
		
	}
	
	public void begin(){
		
		Scanner scanner = null;
		
		LinkedHashSet<Integer> com  = new LinkedHashSet<Integer>();		//存放電腦產生的四位數字
		LinkedHashSet<Integer> user  = new LinkedHashSet<Integer>();	//存放使用者輸入的四位數字
		
		int self = 0;		
		boolean t = true;
		
		com = random_num(4);	//random four num
			
		do{
				
			scanner = new Scanner(System.in);
				
			try{
					
				if(count <= 9){
					
					System.out.print("請輸入4位數字 : ");
						
					self = scanner.nextInt();
						
					if(self < 1000 || self > 9999){
						System.out.println("請輸入4位數字.....");
						t = false;
							
					}else if(!split_num(self)){
						System.out.println("請輸入不可重複數字.....");
						t = false;
							
					}else {												
						t = true;
							
					}
						
					if(t){						
						user = save_set_num();						
						t = display_xAyB(com, user);
					}
						
				}else {
					System.out.println("超過10次，答案為"+com_random_num);
					t = true;
						
				}
					
				//System.out.println(t);
					
			}catch(java.util.InputMismatchException ex){
					
				System.out.println("請輸入數字.....");	
				t = false;
				//scanner.next();
					
			}
				
		}while(!t);
			
		int yn = 0;
						
		do{
			
			System.out.println("要再玩嗎?");
			System.out.println("請輸入1(繼續)或2(離開)");
		
			scanner = new Scanner(System.in);
		    
			try{
		    	
				yn = scanner.nextInt();
		    
			}catch(java.util.InputMismatchException ex){
		    	
				System.out.println("請輸入1(繼續)或2(離開.....");	
				t = false;
					
			}
		    
		}while(!t);
		
		count = 0;
		
		if(yn == 1){
			begin();
		}
		
	}
	
}
