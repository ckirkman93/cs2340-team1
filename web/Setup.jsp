<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="com.feudalwar.model.Player"
    %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Preparing for War</title>
<link href="css/pepper-grinder/jquery-ui-1.10.3.custom.css" rel="stylesheet">
	<script src="js/jquery-1.9.1.js"></script>
	<script src="js/jquery-ui-1.10.3.custom.js"></script>
</head>
<body>
	<c:if test="${playercount < 6}">
		<form action="/FeudalWar/Setup" method="post">
		<input type="hidden" name="newplayer" value="true"/>
		Name: <input name="name" type="text"></input><br>
		Color: <select name="color">
				<option value="#FF0000">Red</option>
				<option value="#00FF00">Green</option>
				<option value="#0000FF">Blue</option>
				<option value="#FFFF00">Yellow</option>
				<option value="#00FFFF">Cyan</option>
				<option value="#FF00FF">Magenta</option>
				<option value="#C0C0C0">Grey</option>
				</select> <br>
			<input type="submit" value="Add Player">
		</form>
	</c:if>
<h3>Current Players</h3>
<div>
	<c:forEach var="player" items="${playerlist}">
		<div>
			<b>Name: </b><b style="color:${player.getColor()};"><c:out value="${player.getName() }"/></b></br>
		</div>
	</c:forEach>
</div>
</br>
	<c:if test="${playercount >= 3}">
		<form action="/FeudalWar/Initializer" method="post">
			<input type="submit" value="Begin Game">
		</form>
	</c:if>
</body>
</html>