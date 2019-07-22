package com.bankresearch.jcpt.fileserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Properties;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Delete extends HttpServlet {
	private String configure;
	private Properties conf;

	private static StringBuffer result = new StringBuffer();
	private static String[] resultCodes = {"0", "1", "2", "3", "4", "5", "6"};
	private static String[] resultDescriptions = {
												/*0*/"success", 
												/*1*/"configure file loading error", 
												/*2*/"wrong credential", 
												/*3*/"file not found",
												/*4*/"not a file",
												/*5*/"please wait 30 seconds at least",
												/*6*/"deleting file failed" 
												};	


	public void init() throws ServletException {
		configure = this.getServletContext().getInitParameter(Constants.CONFIGURE_FILE_KEY);
	}

	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
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

		String path = req.getParameter(Constants.URL_FILEPATH_KEY);
		String credential= req.getParameter(Constants.URL_CREDENTIAL_KEY);
		System.out.println("===" + path);
		System.out.println("===" + credential);
		try {
			credential = Digest.decrypt(credential, conf.getProperty(Constants.LOCAL_DES_KEY));
		} catch (Exception e1) {
			e1.printStackTrace();
			result.append(resultCodes[2]).append(",").append(resultDescriptions[2]);
			response(res);
			return;
		}
		if(null ==credential || !credential.equals(conf.getProperty(Constants.LOCAL_CREDENTIAL_KEY))) {
			result.append(resultCodes[2]).append(",").append(resultDescriptions[2]);
			response(res);
			return;
		}

		File file = new File(conf.getProperty(Constants.LOCAL_DIRECTORY_KEY) + "/" + path);
		if(! file.exists()) {
			result.append(resultCodes[3]).append(",").append(resultDescriptions[3]);
			response(res);
			return;
		}
		if(! file.isFile()) {
			result.append(resultCodes[4]).append(",").append(resultDescriptions[4]);
			response(res);
			return;
		}

		if(new Date().getTime()-file.lastModified()<30*1000) {
			result.append(resultCodes[5]).append(",").append(resultDescriptions[5]);
			response(res);
			return;
		}

		Boolean t = file.delete();
		if (! t) {
			result.append(resultCodes[6]).append(",").append(resultDescriptions[6]);
			response(res);
			return;
		}

		result.append(resultCodes[0]).append(",").append(resultDescriptions[0]);
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
