<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table>
    <tr>
        <th>Date Time</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach var="meal" items="${mealsWithExceeded}">
        <tr style="color:${meal.exceed ? 'red' : 'green'}">
            <td>${fn:replace(meal.dateTime, 'T', ' ')}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
