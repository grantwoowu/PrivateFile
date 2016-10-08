package com.grant.privately.file.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	private static char hexidgits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
			'c', 'd', 'e', 'f' };
	
	public static String getMD5(String str){
		String MD5 = "";
		try {
			byte[]data = str.getBytes("UTF-8");
			MD5 = getMD5(data);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return MD5.toLowerCase();
	}
	
	public static String getMD5(byte[]data){
		String MD5 = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(data, 0, data.length);
			byte[] b = md.digest();
			MD5 = toMD5String(b);
		}  catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return MD5.toLowerCase();
	}
	
	/**
	 * 获取文件的MD5值。
	 * 
	 * @param file
	 *            文件对象。
	 * @return 文件的MD5值，如果不是文件或者获取失败，则返回 empty 。
	 */
	public static String getMD5(File file) {
		String MD5 = "";
		if (file != null && file.exists() && file.isFile()) {
			FileInputStream fis = null;
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				fis = new FileInputStream(file);
				byte[] buffer = new byte[2048];
				int length = -1;

				while ((length = fis.read(buffer)) != -1) {
					md.update(buffer, 0, length);
				}
				byte[] b = md.digest();

				MD5 = toMD5String(b);
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException ex) {
						System.out.println(ex);
					}
					fis = null;
				}
			}
		}
		return MD5.toLowerCase();
	}

	/**
	 * 把byte[]转换成16进制字符串表现形式。
	 * 
	 * @param data
	 *            要转换的byte数组。
	 * @return 十六进制字符串表现形式。
	 */
	private static String toMD5String(byte[] data) {
		if (data == null || data.length < 16) {
			return null;
		}

		String s;

		// 用字节表示就是16个字节
		char str[] = new char[16 * 2]; // 每个字节用16进制表示的话，使用两个字符,
		// 所以表示成16进制需要32个字符
		int k = 0; // 表示转换结果中对应的字符位置
		for (int i = 0; i < 16; i++) {
			// 从第一个字节开始，对MD5的每一个字节
			// 转换成16进制字符的转换
			byte byte0 = data[i];
			str[k++] = hexidgits[byte0 >>> 4 & 0xf];// 取字节中高4位的数字转换
			// >>>为逻辑右移，将符号位一起右移
			str[k++] = hexidgits[byte0 & 0xf]; // 取字节中低4位的数字转换
		}

		s = new String(str); // 转换后的结果转换成字符串
		return s.toLowerCase();
	}
}
