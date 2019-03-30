package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.coder520.user.entity.User;

/** 
*@Title: Test.java 
*@Package test 
*@Description: TODO(添加描述) 
*@author xxp 
*@date 2018年11月22日 下午2:36:22 
*@version V1.0 
*/
public class Test {

	public static void main(String[] args) throws ParseException {
		String str="张无忌_98_男";
		String age=str.substring(4, 6);
		Integer age2=Integer.valueOf(age);
		int age3=age2;
		char ch=(char) age3;
		Character ch2=Character.valueOf(ch);
		String str1=ch2+"";
		String str2=ch2.toString();
		System.out.println("str1:"+str1);
		System.out.println("str2:"+str2);


	

	}

}
