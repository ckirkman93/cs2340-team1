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
<title>Feudal War!</title>
<link href="css/pepper-grinder/jquery-ui-1.10.3.custom.css" rel="stylesheet">
	<script src="js/jquery-1.9.1.js"></script>
	<script src="js/jquery-ui-1.10.3.custom.js"></script>
</head>
<body>
<h3>Game Status:</h3>
<div>
	<c:forEach var="player" items="${playerlist}">
		<div>
			<b>Name: </b><b style="color:${player.getColor()};"><c:out value="${player.getName() }"/></b></br>
			<b>Infantry: ${player.getInfantry() }</br>
			   Generals: ${player.getGenerals() }</b>
			   </br></br>
		</div>
	</c:forEach>
</div>
</body>
</html>