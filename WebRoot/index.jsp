<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    This is my JSP page. <br>
    <h2><hr/></h2>


    <form name="uploadtest" action="<%=basePath%>/sapi/upload?directory=123" method="post" enctype="multipart/form-data">
    	目录：<input name="directory" type="text"/>
    	秘钥：<input name="credential" type="text" value="sHTd/9exYuk="/>
    	文件：<input name="file" type="file"/>
    	<input name="file" type="file"/>
    	<input name="submit" value="提交" type="submit"/>
    </form>

	<br/><hr/>
	<a href="<%=basePath%>/sapi/download?path=/ddd/text.txt&md5=77ff1049902c6dbc3d0e9cbb1f5c76e4">download</a> / 
	<a href="<%=basePath%>/sapi/download?path=/ddd/银行对公业务介绍.pdf&md5=e5a650a3bc8df0ffa3784ce08338e723">download</a> /
	<a href="<%=basePath%>/sapi/download?path=/ddd/银行分析与行业观察-2011年-四季度-上海-零售业.pdf&md5=e5a650a3bc8df0ffa3784ce08338e723">download</a>
	<a href="<%=basePath%>/sapi/download?path=/qsbg/2014/04/04/行业季度分析报告-2014年一季度-湖南-畜牧业.pdf&md5=00d6d4775561a2953c02c55542920c83">download</a>
    <form name="uploadtest" action="<%=basePath%>/sapi/download" method="post">
    	路径：<input name="path" type="text" value="/ddd/银行对公业务介绍.pdf"/>
    	秘钥：<input name="md5" type="text" value="e5a650a3bc8df0ffa3784ce08338e723"/>
    	<input name="submit" value="下载" type="submit"/>
    </form>
    <a href="http://192.168.0.9:8080/fileserver/sapi/download?path=/hybg/2014/2/21/银行分析与行业观察-2011年-四季度-上海-零售业.pdf&md5=441d24d1f987b47fe1bea2947e46d706">download</a>
    <a href="http://192.168.0.9:8080/fileserver/sapi/download?path=hybg/2014/2/21/银行分析与行业观察-2011年-四季度-上海-零售业.pdf&md5=441d24d1f987b47fe1bea2947e46d706">download</a>
	<br/>
	<a href="<%=basePath%>/sapi/download2?path=/ddd/text.txt&md5=77ff1049902c6dbc3d0e9cbb1f5c76e4">download</a> / 
	<a href="<%=basePath%>/sapi/download2?path=/ddd/银行对公业务介绍.pdf&md5=e5a650a3bc8df0ffa3784ce08338e723">download</a> /
	<a href="<%=basePath%>/sapi/download2?path=/ddd/银行分析与行业观察-2011年-四季度-上海-零售业.pdf&md5=e5a650a3bc8df0ffa3784ce08338e723">download</a>
	<br/>
	<a href="<%=basePath%>/sapi/download3?path=/qsbg/2014/04/04/行业季度分析报告-2014年一季度-湖南-畜牧业.pdf&md5=00d6d4775561a2953c02c55542920c83&credential=RwCRLuW9FroRQSqJ">download</a>
	<a href="<%=basePath%>/sapi/download?path=/qsbg/2014/04/04/企业环境信用评价办法（试行）（20140318172942614）.pdf&md5=fdf55f3552f12d22d3a7d64d79e0e47">download</a>
	<br/><br/><hr/>

	<a href="<%=basePath%>/sapi/delete?path=/ddd/text.txt&credential=sHTd/9exYuk=">delete</a> / 
	<a href="<%=basePath%>/sapi/delete?path=/ddd/银行对公业务介绍.pdf&credential=sHTd/9exYuk=">delete</a>	
    <form name="uploadtest" action="<%=basePath%>/sapi/delete" method="post">
    	路径：<input name="path" type="text" value="/ddd/text.txt"/>
    	秘钥：<input name="credential" type="text" value="sHTd/9exYuk="/>
    	<input name="submit" value="删除" type="submit"/>
    </form>
    <a href="<%=basePath%>/sapi/delete?path=/ddd/JCPT CMS 修改建议记录-20131127.docx&credential=sHTd/9exYuk=">delete</a>
	<a href="<%=basePath%>/sapi/delete?path=<%=java.net.URLEncoder.encode("/ddd/JCPT CMS 修改建议记录-20131127.docx")%>&credential=sHTd/9exYuk=">delete</a>
	<a href="http://www.bairui100.com:9067/fileserver/sapi/delete?path=yhbg/2014/3/6/银行对公业务介绍 - 副本（20140306145608985）.pdf&credential=sHTd/9exYuk=">delete</a>
	<br/><br/><hr/>
	<a href="<%=basePath%>/sapi/diskspace?credential=sHTd/9exYuk=">diskspace</a>	
    <form name="uploadtest" action="<%=basePath%>/sapi/diskspace" method="post">
    	秘钥：<input name="credential" type="text" value="sHTd/9exYuk="/>
    	<input name="submit" value="提交" type="submit"/>
    </form>	
  </body>
</html>
