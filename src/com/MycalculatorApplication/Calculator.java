package com.MycalculatorApplication;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;


public class Calculator extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField displayText = new JTextField(30); // Heading of the calculator
	private JButton[] button = new JButton[23]; // We have total 23 buttons
	String resultstring1;
	double result1 = 0;	
	// Store all the buttons in an array
	private String[] keys = { "OFF", "C", "D", "/", "7", "8", "9", "*", "4", "5", "6", "-", "1", "2", "3", "+", "0",
			 ".", "√","=","∛","Sq","%" };
	private String numStr1 = ""; // Stores the first number given by the user
	private String numStr2 = ""; // Stores the second number given by the user
	private char op; // Stores the operator given by the user
	private char op1;// used to clean the operator
	private boolean firstInput = true; // Stores the first character given by the user

	public Calculator() {
		// Set the properties of the main frame			
		setTitle("Barnali's Calculator");
		setSize(335, 400); // set size of frame
		Container pane = getContentPane();
		pane.setLayout(null); 
		pane.setBackground(Color.gray);
		displayText.setSize(300, 30);
		displayText.setLocation(10, 10);
		pane.add(displayText);
		LineBorder thick;
		thick=new LineBorder(Color.DARK_GRAY,3);			
		int x=10, y=50;
		// Draw 22 buttons
		for (int ind = 0; ind < 23; ind++) {			
			button[ind] = new JButton(keys[ind]);
			button[ind].addActionListener(this);
			button[ind].setSize(75, 50);
			button[ind].setLocation(x, y);
			button[ind].setBorder(thick);
			switch (ind) {			
			 case 0: case 1: case 2: button[ind].setBackground(Color.orange); break;
			 case 18: case 22: case 3: case 7: case 11: case 15:case 20: case 21: button[ind].setBackground(Color.lightGray); break; 
			 case 19:button[ind].setBackground(Color.red);button[ind].setSize(75, 100); 			 
			}			
			pane.add(button[ind]);
			x = x + 75; // shift 75 positions for the next button to right
			if ((ind + 1) % 4 == 0) // When we have reached 4 buttons in a row shift to next row
			{ x = 10;	y = y + 50;	}
		}
		// Exit the program if user presses X
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);	}});
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
// Event management code 	
	public void actionPerformed(ActionEvent e) {
		String resultStr = "";
		String str = String.valueOf(e.getActionCommand());
		char ch = str.charAt(0);
		
		switch (ch) {		
        // User types any number
		  case '0': case '1': case '2': case '3': case '4': case '5':
		  case '6': case '7': case '8': case '9': case '.': 		
			when_number(ch); break;	
		
			// User gives any operator	
		  case '+': case '-': case '*': case '/':  case '%':	
		  case '√': case'∛' : case'S' :
		    resultStr = when_operator(resultStr, ch);break;			
		 
		    // User gives equal to sign	
		  case '=': 		
			// Call method to check empty strings before calculations			
			if(check_values() == false) {break;} 
			resultStr = evaluate(); // Call method to calculate	
			reset_values(resultStr); break;	
		
			// User gives cancel	
		  case 'C':
			clear_all();break;
		
			// User gives OFF	
		  case 'O':
			Exit_program();	break;	
		
			// User wants to delete input  from the display	
		  case 'D':
			when_delete(); break;
		}
	}
	
	private void when_delete() {
		int length = displayText.getText().length();
		int number = length-1;
		if (length>0) {			
			StringBuilder back = new StringBuilder(displayText.getText());
			back.deleteCharAt(number);
			displayText.setText(back.toString());
			if(firstInput) { numStr1 = numStr1.substring(0,number);}
			else {numStr2 = numStr2.substring(0,number);}					
		 }
	}
	
	private String evaluate() {
		try {
			double num1 = Double.parseDouble(numStr1); // convert str1 to number
			double num2 = Double.parseDouble(numStr2); // convert str2 to number
			double result = 0;			
			switch (op) {
			  case '+': result = num1 + num2; break;
			  case '-': result = num1 - num2; break;
			  case '*': result = num1 * num2; break;
			  case '/': result = num1 / num2; break;			
			  case '%': result = num1*num2/100; break;
			  case '√': result = Math.sqrt(num2); break;
			  case '∛': result = Math.cbrt(num2); break;
			  case 'S': result = Math.pow(num2,2); break;	
			}
					
			
			result1 = Math.round(result * 100.0) / 100.0;		
			if ((result == Double.POSITIVE_INFINITY) || (result == Double.NEGATIVE_INFINITY)) {
				op=op1;numStr1="";numStr2="";result=0; // reset all values on error
				return "E R R O R: / by zero";
			} else
				if (Double.toString(result1).endsWith(".0")){
				    displayText.setText(Double.toString(result1).replace(".0", " "));		
			        }else {
				     displayText.setText(Double.toString(result1));
			        }
				return String.valueOf(result1);
		}
		// Trap unchecked error or unusual outcome of code that is thrown when
		//wrong arithmetic or mathematical operation occurs in code at run time.
		catch (ArithmeticException e) {
			op=op1;numStr1="";numStr2="";			
			return "E R R O R: Aritmetic Error";
		} 
		//occurs when an attempt is made to convert a
		//string with an incorrect format to a numeric value.
		catch (NumberFormatException e) {
			op=op1;numStr1="";numStr2="";
			if (numStr1.equals(""))
				return "E R R O R: Invalid First Number";
			else
				return "E R R O R: Invalid Second Number";
		}
		//an event that disrupts the normal flow of the program
		catch (Exception e) {
			op=op1;numStr1="";numStr2="";
			return "E R R O R";
		}
	}
	
   private void clear_all(){
	   displayText.setText(""); // Set and clear 1st, 2nd and final value
	   numStr1 = "";
	   numStr2 = "";
	   firstInput = true; // Toggle 
	   op = op1;
	   result1=0;	   
   }
  	
   private void Exit_program(){
	   setDefaultCloseOperation(EXIT_ON_CLOSE);
	   System.exit(0);
   }
   
   private boolean check_values() {
	   boolean flag;
	   flag=true;
	   // when operator is '√' or any of the below, we don't need the second number; put a  dummy to num2
	   if((op=='√')||(op=='∛')||(op=='S')) {numStr2="0";}
	   
	 //if both the numbers are empty and user presses =; do nothing
		if ( (numStr1.equals(""))&& (numStr2.equals("")) ){
		      op = op1; // reset operator if it has any values
		      firstInput = true;
		      flag= false;
		 }
		//if first number is blank and second number is not blank; make first number as second and clear second number 
		if ( (numStr1.equals(""))&& !(numStr2.equals("")) ){
		      numStr1 = numStr2;
		      op=op1;
		      numStr2="";
		      flag= false; 
		}
				
		// if first number is not blank but second number is blank 
		if (!(numStr1.equals(""))&& (numStr2.equals("")) ){ // do nothing
			flag= false;
		}				
		return flag;
   }
   
   private void when_number(char ch1) {
	// when operator is empty and user put a number but there is already a value in result1
	// clear set first input as true and clear result1
	   if ((op1==op)&& (result1!=0)) {
			displayText.setText(""); // Set and clear 1st, 2nd and final value
			numStr1 = "";
			numStr2 = "";
			firstInput = true; // Toggle 
			op = op1;
			result1=0;
		}
	   if (firstInput) { // attach the next char to first number
			numStr1 = numStr1 + ch1;
			displayText.setText(numStr1);
		} else { // attach the next char to second number
			numStr2 = numStr2 + ch1;
			displayText.setText(numStr2);
		}
   }  
   
   private String when_operator(String result_str,char ch1) {
	   double numx = 0;
	   int special = 0;	  	  
	   displayText.setText("");
	   if((ch1=='√')||(ch1=='∛')||(ch1=='S')) {special=1;}
	   
	   // operator works as = when we have 2 numbers and a operator already in place
	   // and it is not a special operator
	   if ( !(numStr1.equals(""))&& !(numStr2.equals(""))&& (op!=op1)&& (special==0) ) {
			result_str = evaluate(); // Call method to calculate			
			displayText.setText(result_str);
			numStr1 = result_str; // the 1st number takes value from final number
			numStr2 = ""; // second number is set to empty
			firstInput = false; // Toggle			
		}
	    op = ch1; 
	    // when special operator that do not need 2 digits to function
	    
	    if (!(numStr1.equals(""))) {	
	       numx = Double.parseDouble(numStr1);	
	       if (!(numStr2.equals(""))){numx = Double.parseDouble(numStr2);}
	 	   if (op=='S') {numx=numx*numx; special=1;}
	 	   if (op=='√') {numx= Math.sqrt(numx); special=1;}
	 	   if (op=='∛') {numx=Math.cbrt(numx);special=1;}
	 	     
		   if (special==1) {			  
			  numx = Math.round(numx * 100.0) / 100.0;
			  result_str= String.valueOf(numx);
			  displayText.setText(result_str);
			  numStr1 = result_str;
			  numStr2 = ""; // second number is set to empty
			  firstInput = false; // Toggle
			  if (Double.toString(numx).endsWith(".0")){
				    displayText.setText(Double.toString(numx).replace(".0", " "));		
			        }else {
				     displayText.setText(Double.toString(numx));
			        }
	 	    }
	    }		

		 
		firstInput = false; // it time to capture the second number
	    return result_str;
	   
   }
   
   private void reset_values(String resultStr1) {
	   displayText.setText(resultStr1);
	   numStr1 = resultStr1; // the 1st number takes value from final number
	   numStr2 = ""; // second number is set to empty
	   firstInput = false; // Toggle 
	   op = op1;
	   
	   if (Double.toString(result1).endsWith(".0")){
		    displayText.setText(Double.toString(result1).replace(".0", " "));		
	        }else {
		     displayText.setText(Double.toString(result1));
	        }
   }
         
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Calculator C = new Calculator();
	}
}

