package com.bankresearch.jcpt.fileserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Diskspace extends HttpServlet {
	private String configure;
	private Properties conf;

	private static StringBuffer result = new StringBuffer();
	private static String[] resultCodes = {"0", "1", "2"};
	private static String[] resultDescriptions = {
												/*0*/"success", 
												/*1*/"configure file loading error", 
												/*2*/"wrong credential", 
												};	

	public void init() throws ServletException {
		configure = this.getServletContext().getInitParameter(Constants.CONFIGURE_FILE_KEY);
	}

	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {
		req.setCharacterEncoding("UTF-8");
		conf = new Properties();
		try {
			conf.load(new BufferedInputStream(this.getClass().getResourceAsStream(configure)));
		} catch (IOException e) {
			e.printStackTrace();
			result.append(resultCodes[1]).append(",").append(resultDescriptions[1]);
			response(res);
			return;
		}

		String credential = req.getParameter(Constants.URL_CREDENTIAL_KEY);
		System.out.println(credential);
		try {
			credential = Digest.decrypt(credential, conf.getProperty(Constants.LOCAL_DES_KEY));
		} catch (Exception e1) {
			e1.printStackTrace();
			result.append(resultCodes[2]).append(",").append(resultDescriptions[2]);
			response(res);
			return;
		}
		if(null == credential || !credential.equals(conf.getProperty(Constants.LOCAL_CREDENTIAL_KEY))) {
			result.append(resultCodes[2]).append(",").append(resultDescriptions[2]);
			response(res);
			return;
		}
		
		File root = new File(conf.getProperty(Constants.LOCAL_DIRECTORY_KEY));
		Long total = root.getTotalSpace();
		Long available = root.getUsableSpace();

		result.append(resultCodes[0]).append(",").append(available).append("/").append(total);
		response(res);
	}
	public void response(HttpServletResponse res) throws IOException {
		res.reset();
		res.setContentType("text/html");
		PrintWriter w = res.getWriter();

		w.write(result.toString());
		result.setLength(0);
		w.flush();
		w.close();
	}
}
