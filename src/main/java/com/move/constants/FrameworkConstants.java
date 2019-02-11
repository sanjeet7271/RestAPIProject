package com.move.constants;

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
	public static final int ZERO_KM_DISTANCE = 0;
	public static final int ZERO_KM_COST = 0;
	public static final double FIRST_2KM_DISTANCE = 2000.00;
	public static final int FIRST_2KM_DAY_COST = 20;
	public static final int MORE_THAN_FIRST_2KM_DAY_COST = 5;

	// Cost and Distance for Normal Time except 9pm to 5am(Future payload)

	public static final int NIGHT_ZERO_KM_DISTANCE = 0;
	public static final int NIGHT_ZERO_KM_COST = 0;
	public static final double NIGHT_FIRST_2KM_DISTANCE = 2000.00;
	public static final int NIGHT_FIRST_2KM_COST = 30;
	public static final int NIGHT_MORE_THAN_FIRST_2KM_COST = 8;

	// Time Stamps in between 9pm to 5am
	public static final double MORE_THAN_9PM = 21;
	public static final double LESS_THAN_11_59PM = 23.59;
	public static final double LESS_THAN_0AM = 0;
	public static final double LESS_THAN_5AM = 5;

	public static final String PAST_ERROR_MESSAGE = "field orderAt is behind the present time";
	public static final String WRONG_JSON_ERROR_MESSAGE = "error in field(s): stops";
	public static final String ORDER_NOT_IN_ASSIGNING_STATE = "Order status is not ASSIGNING";

}
