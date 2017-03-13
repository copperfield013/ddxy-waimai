<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<base href="${basePath }" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title></title>
		<meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
	</head>
	<body>
		<form action="admin/doLogin" method="post">
			<c:if test="${error != null }">
				<p style="color: red;">${errorMap[error] }</p>
			</c:if>
			用户名：<input type="text" name="ddxy-admin-username"><br/>
			密码：<input type="password" name="ddxy-admin-password" /><br/>
			<input type="submit" value="提交">
		</form>
	</body>
</html>