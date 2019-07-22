package com.bankresearch.jcpt.fileserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

public class Upload extends HttpServlet {
	private String configure;
	private Properties conf;

	private static StringBuffer result = new StringBuffer();
	private static String[] resultCodes = {"0", "1", "2", "3", "4", "5"};
	private static String[] resultDescriptions = {
												"success", 
												"configure file loading error", 
												"wrong credential", 
												"wrong directory", 
												"file writing error", 
												"not a file"
												};	

	public Upload() {
		super();
	}
	public void init() throws ServletException {
		configure = this.getServletContext().getInitParameter(Constants.CONFIGURE_FILE_KEY);
	}
	public void destroy() {
		super.destroy();
	}


	public Boolean startup(HttpServletRequest req, HttpServletResponse res) throws IOException {
		conf = new Properties();
		try {
			conf.load(new BufferedInputStream(this.getClass().getResourceAsStream(configure)));
		} catch (IOException e) {
			e.printStackTrace();
			result.append(resultCodes[1]).append(",").append(resultDescriptions[1]);
			response(res);
			return false;
		}		

		boolean b = ServletFileUpload.isMultipartContent(req);
		if (!b) {
			result.append(resultCodes[5]).append(",").append(resultDescriptions[5]);
			response(res);
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		boolean ok = startup(req, res);
		if (!ok) return;

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);		

		List<FileItem> items = new ArrayList();
		try {
			items = upload.parseRequest(req);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		Map<String, String> fields = new HashMap<String, String>();
		Map<String, String> filenames = new HashMap<String, String>();
		List<FileItem> files = new ArrayList<FileItem>();
		Boolean rename = false;

		Enumeration<String> ps = req.getParameterNames();
		while(ps.hasMoreElements()) {
			String name = ps.nextElement();
			fields.put(name, req.getParameter(name));
		}
		Enumeration<String> as = req.getAttributeNames();
		while(as.hasMoreElements()) {
			String name = as.nextElement();
			fields.put(name, (String)req.getAttribute(name));
		}

		for (FileItem i : items) {
			if (i.isFormField()) {
				String n = i.getFieldName();
				String v = i.getString();

				if (null == n) continue;
				if (Constants.URL_RENAME_KEY.equalsIgnoreCase(n)) rename = Boolean.valueOf(v);
				if (n.startsWith(Constants.URLFILENAME_KEY)) filenames.put(n, v);

				v = new String(v.getBytes("iso-8859-1"), "utf-8");
				System.out.println(n + " =++++= " + v);
				if(v.equals("")) continue;
				fields.put(n, v);
			} else {
				if (!"".equals(i.getName()) && i.getSize() != 0) files.add(i);//////
			}
		}
		String directory = fields.get(Constants.URL_DIRECTORY_KEY);
		String credential = fields.get(Constants.URL_CREDENTIAL_KEY);
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

		File localDirectory = new File(conf.getProperty(Constants.LOCAL_DIRECTORY_KEY) + "/" + directory);
		if (!localDirectory.exists() || ! localDirectory.isDirectory()) localDirectory.mkdirs();

		String filename="wrong-filename";
		for (FileItem f : files) {//////
			filename = f.getName();
			System.out.println("---- "+ rename);
			if (rename) filename=filenames.get(Constants.URLFILENAME_KEY + f.getFieldName().substring(Constants.URL_ATTACHMENT_KEY.length()));
			System.out.println("===== : "+ filename);
			try {
				f.write(new File(localDirectory, filename));
				result.append(resultCodes[0]).append(",").append(resultDescriptions[0]);
			} catch (Exception e) {
				e.printStackTrace();
				result.append(resultCodes[4]).append(",").append(resultDescriptions[4]).append(",").append(filename);	// 文件写入错误
			} finally {
				f.delete();
			}
			response(res);
		}

	}

	public void response(HttpServletResponse res) throws IOException {
		res.setContentType("text/html");
		PrintWriter w = res.getWriter();

		w.write(result.toString());
		result.setLength(0);
		w.flush();
		w.close();
	}
}
