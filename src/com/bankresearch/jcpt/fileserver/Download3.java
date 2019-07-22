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
import java.util.Properties;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Used for client to avoid downloading by invalid user
 */
public class Download3 extends HttpServlet {
	private String configure;
	private Properties conf;

	private static StringBuffer result = new StringBuffer();
	private static String[] resultCodes = {"0", "1", "2", "3", "4", "5", "6"};
	private static String[] resultDescriptions = {
												/*0*/"success", 
												/*1*/"configure file loading error", 
												/*2*/"wrong credential", 
												/*3*/"file not found", 
												/*4*/"md5 code not match", 
												/*5*/"file reading error", 
												/*6*/"not a file"
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

		String credential = req.getParameter(Constants.URL_CREDENTIAL_KEY);
		String path = req.getParameter(Constants.URL_FILEPATH_KEY);
		String newCredential;
		try {
			newCredential = Digest.encryptImperfect(path, conf.getProperty(Constants.LOCAL_DES_KEY), 16);
		} catch (Exception e1) {
			e1.printStackTrace();
			result.append(resultCodes[2]).append(",").append(resultDescriptions[2]);
			response(res);
			return;
		}
		if(!credential.equalsIgnoreCase(newCredential)) {
			result.append(resultCodes[2]).append(",").append(resultDescriptions[2]);
			response(res);
			return;
		}
		
		String md5= req.getParameter(Constants.URL_MD5_KEY);
		String filename=path.substring(path.lastIndexOf("/")+1);
		System.out.println("==== "+ filename);

		File file = new File(conf.getProperty(Constants.LOCAL_DIRECTORY_KEY) + "/" + path);
		if(! file.exists() || !file.isFile()) {
			result.append(resultCodes[3]).append(",").append(resultDescriptions[3]);
			response(res);
			return;
		}
		
		String newMD5;
		try {
			newMD5 = Digest.md5(file);
		} catch(IOException e) {
			result.append(resultCodes[4]).append(",").append(resultDescriptions[4]);
			response(res);
			return;
		}
		if (!md5.equalsIgnoreCase(newMD5)) {
			result.append(resultCodes[4]).append(",").append(resultDescriptions[4]);
			response(res);
			return;
		}

		res.reset();
        res.setContentType("application/octet-stream");
        res.addHeader("Content-Length", "" + file.length());
        String agent = req.getHeader("USER-AGENT").toLowerCase();
        System.out.println(agent);
		if(null != agent && agent.indexOf("msie") != -1) {
			res.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20"));
		} else if (null != agent && agent.indexOf("trident") != -1) {
			res.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20"));
		} else if (null != agent && agent.indexOf("mozilla") != -1) {
			res.addHeader("Content-Disposition", "attachment;filename=\"" + MimeUtility.encodeText(filename, "UTF-8", "B")+"\"");
		} else {
			res.setHeader("Content-Disposition", "attachment;filename=" + filename);
		}

        InputStream is = new BufferedInputStream(new FileInputStream(file));
        OutputStream os = new BufferedOutputStream(res.getOutputStream());
        byte[] buf = new byte[is.available()];
        try {
            is.read(buf);
			os.write(buf);
		} catch (IOException e) {
			//客户端异常关闭IE或取消文件下载 ,服务端抛出 ClientAbortException 异常。对文件的占用没有释放
			//导致Delete无法删除文件(该文件被其它进程占用)。这个bug待处理?
			
			e.printStackTrace();
			result.append(resultCodes[5]).append(",").append(resultDescriptions[5]);
			response(res);
		} finally {
            os.flush();
			os.close();
            is.close();
			buf=null;
            res=null;
            os=null;
            is=null;
            file=null;
            System.gc();
		}
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
