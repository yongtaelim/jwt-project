<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript">
	var path = location.origin;

	function register() {
		$.ajax({
			type : "POST",
			url : path+"/register.do",
			data : $("#registerForm").serialize(),
			dataType : "json",
			success : function(response) {
				alert(response.rsltMsg);
			},
			error : function (xhr, status, error) {
				
			}
		})
	}

	function login() {
		$.ajax({
			type : "POST",
			url : path+"/login.do",
			data : $("#loginForm").serialize(),
			dataType : "json",
			success : function(response) {
				alert(response.rsltMsg);
			},
			error : function (xhr, status, error) {
				
			}
		})
	}

	function transactionTest() {
		$.ajax({
			type : "POST",
			url : path+"/transactionTest.do",
			data : {},
			dataType : "json",
			success : function(response) {
				alert(response.rsltMsg);
			},
			error : function (xhr, status, error) {
				
			}
		})
	}

	function logout() {
		$.ajax({
			type : "POST",
			url : path+"/logout.do",
			data : {},
			dataType : "json",
			success : function(response) {
				alert(response.rsltMsg);
			},
			error : function (xhr, status, error) {
				
			}
		})
	}
	
	function setData(data) {
		for ( key in data ) {
			$("#"+key).val(data[key]);
		}
	}
</script>
</head>
<body>
	<div>
		<form id="registerForm" name="registerForm">
			<span>ID : </span> <input type="text" name="userId" id="userId" /> <span>Name
				: </span> <input type="text" name="userName" id="userName" /> <span>Password
				: </span> <input type="text" name="userPassword" id="userPassword" /> <span>Scope
				: </span> <input type="text" name="scope" id="scope" /> <a
				href="javascript:register()">회원가입</a>
		</form>
	</div>
	<br />
	<div>
		<form id="loginForm" name="loginForm">
			<span>ID : </span> <input type="text" name="userId" id="userId" /> <span>Password
				: </span> <input type="text" name="userPassword" id="userPassword" /> <a
				href="javascript:login()">로그인</a>
		</form>
	</div>
	<br />
	<div>
		<form id="transactionTestForm" name="transactionTestForm">
			<a href="javascript:transactionTest()">트랜잭션 Test</a>
		</form>
	</div>
	<br />
	<div>
		<form id="logoutForm" name="logoutForm">
			<a href="javascript:logout()">로그아웃</a>
		</form>
	</div>
</body>
</html>