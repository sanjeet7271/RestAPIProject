package com.nagarro.Util;

import java.text.DecimalFormat;
import com.nagarro.constants.FrameworkConstants;

/**
 * 
 * @author sanjeetpandit
 *
 */
public class TravelCostCalculation {
	/**
	 * Total cost traveled in normal time
	 * 
	 * @param sumOfDistance
	 * @return TotalCostOfJeourney
	 */
	public double TravelCostInNormalTimeStamp(double sumOfDistance) {
		double remainingDistanceForEach200Meter, TotalCostForJeourney, remainingCostForEach200Meter, TotalCost;
		if (sumOfDistance == FrameworkConstants.ZEROKMDISTANCE) {
			return FrameworkConstants.ZEROKMCOST;
		} else if (sumOfDistance <= FrameworkConstants.FIRST2KMDISTANCE) {
			return FrameworkConstants.FIRST2KMCOST;
		} else if (sumOfDistance >= FrameworkConstants.FIRST2KMDISTANCE) {
			sumOfDistance = sumOfDistance - FrameworkConstants.FIRST2KMDISTANCE;
			remainingDistanceForEach200Meter = sumOfDistance / (FrameworkConstants.DISTANCE200);
			remainingCostForEach200Meter = remainingDistanceForEach200Meter * (FrameworkConstants.MORETHANFIRST2KMCOST);
			TotalCost = FrameworkConstants.FIRST2KMCOST + remainingCostForEach200Meter;
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
	public double TravelCostInBetween9to5TimeStamp(double sumOfDistance) {
		double remainingDistanceForEach200Meter, TotalCostForJeourney, TotalCost;
		if (sumOfDistance == FrameworkConstants.ZEROKMDISTANCEN) {
			return FrameworkConstants.ZEROKMCOSTN;
		} else if (sumOfDistance <= FrameworkConstants.FIRST2KMDISTANCEN) {
			return FrameworkConstants.FIRST2KMCOSTN;
		} else if (sumOfDistance >= FrameworkConstants.FIRST2KMDISTANCEN) {
			sumOfDistance = sumOfDistance - FrameworkConstants.FIRST2KMDISTANCEN;
			remainingDistanceForEach200Meter = sumOfDistance / (FrameworkConstants.DISTANCE200);
			TotalCost = FrameworkConstants.FIRST2KMCOSTN
					+ (remainingDistanceForEach200Meter * (FrameworkConstants.MORETHANFIRST2KMCOSTN));
			TotalCostForJeourney = Double.parseDouble(new DecimalFormat("##.##").format(TotalCost));
			return TotalCostForJeourney;
		}
		return 0;
	}
}
