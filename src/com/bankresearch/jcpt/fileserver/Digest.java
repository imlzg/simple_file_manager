package com.bankresearch.jcpt.fileserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class Digest {

	private static MessageDigest digest = null;
	private static Cipher cipher;
	

	static {
		try {
			digest = MessageDigest.getInstance("MD5");
			cipher = Cipher.getInstance("DES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
	public static String md5(File f) throws IOException{
		FileInputStream in = new FileInputStream(f);
//		MappedByteBuffer buf = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, f.length());
//		digest.update(buf);
//		if(in!=null)
//			in.close();
//	    in=null;
//		
//		return new BigInteger(1, digest.digest()).toString(16);
		return DigestUtils.md5Hex(in);
	}
	// The key should be eight.
	public static String encrypt(String txt, String key) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "DES"));
		return Base64.encode(cipher.doFinal(txt.getBytes("UTF-8")));
	}
	public static String decrypt(String txt, String key) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, Base64DecodingException {
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "DES"));
		return new String(cipher.doFinal(Base64.decode(txt)));
	}

	public static String encryptImperfect(String txt, String key, int len) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		String enc = encrypt(txt, key);
		if (null != enc && enc.length()>len) return enc.substring(0, len);
		return enc;
	}

	public static void main(String[] args) throws Exception {
//		System.out.println(encrypt("123456", "abcdefgh")); // sHTd/9exYuk=
//		System.out.println(decrypt("sHTd/9exYuk=", "abcdefgh"));
//		
//		System.out.println(md5(new File("D:/test/ddd/text.txt")));	//77ff1049902c6dbc3d0e9cbb1f5c76e4
//		System.out.println(md5(new File("D:/test/ddd/银行对公业务介绍.pdf")));	//e5a650a3bc8df0ffa3784ce08338e723
//		System.out.println(md5(new File("D:/test/ddd/银行对公业务介绍银行对公业务介绍银行对公业务介绍银行对公业务介绍.pdf")));	//e5a650a3bc8df0ffa3784ce08338e723

//		System.out.println(encrypt("/ddd/text.txt", "12345678")); // 8b8wV8l/7d0wfnyzffsS+g==
//		System.out.println(encrypt("/ddd/银行对公业务介绍.pdf", "12345678")); // Af4WM3mKtT2MPNxjR8XYAE4cFW99uRbUN9Ppt5nAFfDPEYFu13gobA==
//		
//		
//		System.out.println(md5(new File("D:/test/ddd/银行分析与行业观察-2011年-四季度-上海-零售业.pdf")));	//e5a650a3bc8df0ffa3784ce08338e723
		

//		System.out.println(md5(new File("D:/T.xlsx")));	//f9e21d04f7ad8a34ae65356094d35d8e
	

		//System.out.println(md5(new File("D:/test/ddd/text.txt")));	//77ff1049902c6dbc3d0e9cbb1f5c76e4
//		System.out.println(md5(new File("D:/test/ddd/银行对公业务介绍.pdf")));	//e5a650a3bc8df0ffa3784ce08338e723
		//System.out.println(md5(new File("D:/安信证券--行业研究--农林牧渔--生猪价格持续非理性下跌02.pdf")));	//e869973f86bc70f5219651ea16b6a2d4
		
//		System.out.println(md5(new File("D:/test/ddd/产业园区文件.xlsx")));	//5b8bf2f8bde90517a5b07ec03ffd82b9
//		
//		System.out.println(URLEncoder.encode("产 业园区文件", "UTF-8").replaceAll("\\+", "%20"));
//
//		System.out.println(md5(new File("C:/Users/PC/Desktop/新建文件夹/行业季度分析报告-2014年一季度-湖南-畜牧业.pdf"))); //d6d4775561a2953c02c55542920c83
//		
//		System.out.println(encrypt("123456", "12345678")); // ED5wLgc3Mnw=
		
		System.out.println(encrypt("/qsbg/2014/04/04/行业季度分析报告-2014年一季度-湖南-畜牧业.pdf", "abcdefgh"));
		System.out.println(encryptImperfect("/qsbg/2014/04/04/行业季度分析报告-2014年一季度-湖南-畜牧业.pdf", "abcdefgh", 78)); //RwCRLuW9FroRQSqJMOjxf3nzrFTbkODX //RwCRLuW9FroRQSqJ
		System.out.println("5b8bf2f8bde90517a5b07ec03ffd82b9".length());
	}
}
