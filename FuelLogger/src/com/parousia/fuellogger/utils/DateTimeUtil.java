package com.parousia.fuellogger.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

	public static String convertDateToString(Date date){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd");
		return df.format(date);
	}
	public static String TODAY = convertDateToString(new Date());
}
