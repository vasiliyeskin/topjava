<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table border=1>
    <thead>
    <tr>
        <th>ID</th>
        <th>Date Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan=2>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="meal" items="${mealsWithExceeded}">
        <tr style="color:${meal.exceed ? 'red' : 'green'}">
            <td>${meal.id}</td>
            <td>${fn:replace(meal.dateTime, 'T', ' ')}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=edit&id=<c:out value="${meal.id}"/>">Update</a></td>
            <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<form method="POST" action='meals' name="frmAddUser">
    ID : <input type="text" readonly="readonly" name="id_edit"
                     value="<c:out value="${mealEdit.id == 0 ? '' : mealEdit.id}" />" /> <br />
    Date Time : <input
        type="datetime" name="datetime" value="<c:out value="${mealEdit.id == 0 ? '' : fn:replace(mealEdit.dateTime, 'T', ' ')}" />" /> <br />
    Description : <input
        type="text" name="description"
        value="<c:out value="${mealEdit.id == 0 ? '' : mealEdit.description}" />" /> <br />
    Calories : <input type="text" name="calories"
                   value="<c:out value="${mealEdit.id == 0 ? '' : mealEdit.calories}" />" /> <br />
    <input  type="submit" value=${mealEdit.id == 0 ? "Add meal" : "Edit meal"} />
</form>


</body>
</html>
