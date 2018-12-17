package com.nagarro.util;

import java.text.DecimalFormat;
import com.nagarro.constants.FrameworkConstants;

/**
 * 
 * @author sanjeetpandit
 *
 */
public class FareCalculationService {
	/**
	 * Total cost traveled in normal time
	 * 
	 * @param sumOfDistance
	 * @return TotalCostOfJeourney
	 */
	public double travelCostInNormalTimeStamp(double sumOfDistance) {
		double remainingDistanceForEach200Meter, TotalCostForJeourney, remainingCostForEach200Meter, TotalCost;
		if (sumOfDistance == FrameworkConstants.ZERO_KM_DISTANCE) {
			return FrameworkConstants.ZERO_KM_DISTANCE;
		} else if (sumOfDistance <= FrameworkConstants.FIRST_2KM_DISTANCE) {
			return FrameworkConstants.FIRST_2KM_DAY_COST;
		} else if (sumOfDistance >= FrameworkConstants.FIRST_2KM_DISTANCE) {
			sumOfDistance = sumOfDistance - FrameworkConstants.FIRST_2KM_DISTANCE;
			remainingDistanceForEach200Meter = sumOfDistance / (FrameworkConstants.DISTANCE200);
			remainingCostForEach200Meter = remainingDistanceForEach200Meter * (FrameworkConstants.MORE_THAN_FIRST_2KM_DAY_COST);
			TotalCost = FrameworkConstants.FIRST_2KM_DAY_COST + remainingCostForEach200Meter;
			TotalCostForJeourney = Double.parseDouble(new DecimalFormat("##.##").format(TotalCost));
			return TotalCostForJeourney;
		}
		return 0;
	}

	/**
	 * Total cost traveled in between 9pm to 5am
	 * 
	 * @param sumOfDistance
	 * @return TotalCostOfJeourney
	 */
	public double travelCostInBetween9to5TimeStamp(double sumOfDistance) {
		double remainingDistanceForEach200Meter, TotalCostForJeourney, TotalCost;
		if (sumOfDistance == FrameworkConstants.NIGHT_ZERO_KM_DISTANCE) {
			return FrameworkConstants.NIGHT_ZERO_KM_COST;
		} else if (sumOfDistance <= FrameworkConstants.NIGHT_FIRST_2KM_DISTANCE) {
			return FrameworkConstants.NIGHT_FIRST_2KM_COST;
		} else if (sumOfDistance >= FrameworkConstants.NIGHT_FIRST_2KM_DISTANCE) {
			sumOfDistance = sumOfDistance - FrameworkConstants.NIGHT_FIRST_2KM_DISTANCE;
			remainingDistanceForEach200Meter = sumOfDistance / (FrameworkConstants.DISTANCE200);
			TotalCost = FrameworkConstants.NIGHT_FIRST_2KM_COST
					+ (remainingDistanceForEach200Meter * (FrameworkConstants.NIGHT_MORE_THAN_FIRST_2KM_COST));
			TotalCostForJeourney = Double.parseDouble(new DecimalFormat("##.##").format(TotalCost));
			return TotalCostForJeourney;
		}
		return 0;
	}
}
