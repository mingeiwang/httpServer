package com.http.server.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	
	public static String dateFormat(long time,String parrent){
		SimpleDateFormat sdf = new SimpleDateFormat(parrent);
		return sdf.format(new Date(time));
	}
	
	public static String dateFormat(long time){
		String parrent = "yyyy-MM-dd HH:mm:ss";
		return dateFormat(time, parrent);
	}
	
	public static String dateFormat(){
		return dateFormat(System.currentTimeMillis());
	}
}
