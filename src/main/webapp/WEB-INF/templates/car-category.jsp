<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Car category</title>
</head>
<body>
<%@ include file="header.jsp" %>
<h1>CAR CATEGORY</h1>
<ul>
    CAR CATEGORY: ${requestScope.carCategory.category}
    PRICE PER DAY $: ${requestScope.carCategory.dayPrice}
</ul>
</body>
</html>
