package com.bankresearch.jcpt.fileserver;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class TestDelete extends HttpServlet {

	public void service(HttpServletRequest req, HttpServletResponse res) throws ClientProtocolException, IOException {
		String u = "http://192.168.0.58:8080/fileserver/sapi/delete?path=/ddd/JCPT CMS 修改建议记录-20131127.docx&credential=sHTd/9exYuk=";
		String u2 = "http://192.168.0.58:8080/fileserver/sapi/delete?path="+ URLEncoder.encode("/ddd/JCPT CMS 修改建议记录-20131127.docx", "UTF-8") + "&credential=sHTd/9exYuk=";

		
		String a1 = "http://192.168.0.58:8080/fileserver/sapi/delete?path=/ddd/银行对公业务介绍.pdf&credential=sHTd/9exYuk=";
		String a2 = "http://192.168.0.58:8080/fileserver/sapi/delete?path=/ddd/银行分析与行业观察.pdf&credential=sHTd/9exYuk=";
		String[] us = {a1, a2};
		for (String s : us) {
			delete(s);
		}

		
	}
	public void delete(String u) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost p = new HttpPost(u);
		CloseableHttpResponse resp = client.execute(p);
		HttpEntity respEntity = resp.getEntity();

		System.out.println(EntityUtils.toString(respEntity));

		resp.close();
		client.close();
		
	}
}
