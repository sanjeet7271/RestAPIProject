package com.nagarro.constants;

/**
 * 
 * Different status of the Orders
 *
 */
public class FrameworkConstants {
	public static final String ASSIGNING = "ASSIGNING";
	public static final String ONGOING = "ONGOING";
	public static final String COMPLETED = "COMPLETED";
	public static final String CANCELLED = "CANCELLED";
	public static final String CURRENCY = "HKD";
	public static final double DISTANCE200 = 200.00;
	// Cost and Distance for Normal Time except 9pm to 5am(Present Payload)
	public static final int ZEROKMDISTANCE = 0;
	public static final int ZEROKMCOST = 0;
	public static final double FIRST2KMDISTANCE = 2000.00;
	public static final int FIRST2KMCOST = 20;
	public static final int MORETHANFIRST2KMCOST = 5;

	// Cost and Distance for Normal Time except 9pm to 5am(Future payload)

	public static final int ZEROKMDISTANCEN = 0;
	public static final int ZEROKMCOSTN = 0;
	public static final double FIRST2KMDISTANCEN = 2000.00;
	public static final int FIRST2KMCOSTN = 30;
	public static final int MORETHANFIRST2KMCOSTN = 8;

	//Time Stamps in between 9pm to 5am
	public static final double MORETHAN9PM = 21;
	public static final double LESSTHAN11_59PM =23.59 ;
	public static final double LESSTHAN0AM =0 ;
	public static final double LESSTHAN5AM =5 ;
	
}
