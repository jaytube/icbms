package com.wz.socket.utils;

import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ValidUtils {
	public static Logger logger = Logger.getLogger(ValidUtils.class);

	/**
	 * 将byte[]转为各种进制的字符串
	 * 
	 * @param bytes
	 *            byte[]
	 * @param radix
	 *            基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
	 * @return 转换后的字符串
	 */
	private static String binary(byte[] datas, int radix) {
		return new BigInteger(1, datas).toString(radix);// 这里的1代表正数
	}
	/**
	 * 奇偶校验 val 1为奇校验 0为偶校验
	 * 
	 * @return
	 */
	public static  boolean checkEvenOdd(byte[] datas, int val) {
		// 先获取字节二进制的1的个数
		String byteStr = binary(datas, 2);
		logger.info("==============>奇偶校验原始字节：" + byteStr);
		if (StringUtils.isNotBlank(byteStr)) {
			String[] oddCnts = byteStr.split("1", -1);
			if (val == (oddCnts.length - 1) % 2) {
				logger.info("==============>奇偶校验通过!");
				return true;
			}
		}
		logger.info("==============>奇偶校验不通过!");
		return false;
	}
	
	public static void main(String[] args){
		/*MessageTcpDecoder mtTcpDecode = new MessageTcpDecoder();
		byte[] datas = new byte[] {0x1A, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00,0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				(byte) 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1B};*/
		//datas[0] = 0xB7;
//		datas[1] = 0x08;
		//	mtTcpDecode.checkEvenOdd(datas, 1);

		//String alarmStr = CommUtils.byteToBit(datas[0])+CommUtils.byteToBit(datas[1]);
		//System.out.println(alarmStr);
		String byteStr = "1101000000000000100000000000100000000000000000000000000000000100000010000000000000000000000000000000000000001101100110000000000000000001010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000100011011";

		String[] oddCnts = byteStr.split("1", -1);
		System.out.println(oddCnts.length);
		if(1 == (oddCnts.length-1)%2){
			System.out.println("奇校验通过！");
		} else {
			System.out.println("奇校验不通过！");
		}
		
		byte[] test = new byte[] { 0x00, 0x00, (byte) 0xA5, 0x00 };
		// int b = test[0]<< 8 + test[1];
		// System.out.println(b);
		System.out.println(CommUtils.byteArrayToInt(test));
		byte type = 0x3E;
		System.out.println(CommUtils.byteToInt(type));

	}



}
