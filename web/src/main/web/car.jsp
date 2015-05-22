<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<body>

<table border="2" width="100%" bordercolor="Maroon" align="center">
    <thead>
    <tr>
        <th align="center" bgcolor="#a9a9a9">Manufacturer</th>
        <th align="center" bgcolor="#a9a9a9">KM</th>
        <th align="center" bgcolor="#a9a9a9">Price</th>
        <th align="center" bgcolor="#a9a9a9">Color</th>
        <th align="center" bgcolor="#a9a9a9">Description</th>
    </tr>
    </thead>
    <c:forEach items="${cars}" var="car">
        <tr>
            <td align="center"><c:out value="${car.manufacturer}"/></td>
            <td align="center"><c:out value="${car.km}"/></td>
            <td align="center"><c:out value="${car.price}"/></td>
            <td align="center"><c:out value="${car.color}"/></td>
            <td align="center"><c:out value="${car.description}"/></td>

            <td align="left" width="5%"><form method="post" action="${pageContext.request.contextPath}/cars/showUpdate?id=${car.id}"
                                              style="margin-bottom: 0;"><input type="submit" value="Upravit"></form></td>

            <td align="left" width="5%"><form method="post" action="${pageContext.request.contextPath}/cars/delete?id=${car.id}"
                                              style="margin-bottom: 0;"><input type="submit" value="Smazat"></form></td>

        </tr>
    </c:forEach>
</table>

<c:if test="${not empty chyba}">
    <div style="border: solid 1px red; background-color: yellow; padding: 10px">
        <c:out value="${chyba}"/>
    </div>
</c:if>

<c:choose>
    <c:when test="${not empty param.id}">
        <h2>Upravit</h2>
        <form action="${pageContext.request.contextPath}/cars/update" method="post">
            <table>
                <tr>
                    <th>Manufacturer:</th>
                    <td><input type="text" name="manufacturer" value="<c:out value="${manufacturer}"/>"/></td>
                </tr>
                <tr>
                    <th>Km:</th>
                    <td><input type="text" name="km" value="<c:out value="${km}"/>"/></td>
                </tr>
                <tr>
                    <th>Price:</th>
                    <td><input type="text" name="price" value="<c:out value="${price}"/>"/></td>
                </tr>
                <tr>
                    <th>Color:</th>
                    <td> <input type="color" name="color" value="<c:out value="${color}"/>"/></td>
                </tr>
                <tr>
                    <th>Description:</th>
                    <td><input type="text" name="description" value="<c:out value="${description}"/>"/></td>
                </tr>
                <input type="hidden" name="id" value="${param.id}"/>
            </table>
            <input type="Submit" value="Odoslat" />
        </form>
        <a href="${pageContext.request.contextPath}/cars/"><button>Zrusit</button></a>
    </c:when>


    <c:otherwise>
        <h2>Nove auto</h2>
        <form action="${pageContext.request.contextPath}/cars/add" method="post">
            <table>
                <tr>
                    <th>Manufacturer:</th>
                    <td><input type="text" name="manufacturer" value="<c:out value="${manufacturer}"/>"/></td>
                </tr>
                <tr>
                    <th>Km:</th>
                    <td><input type="text" name="km" value="<c:out value="${km}"/>"/></td>
                </tr>
                <tr>
                    <th>Price:</th>
                    <td><input type="text" name="price" value="<c:out value="${price}"/>"/></td>
                </tr>
                <tr>
                    <th>Color:</th>
                    <td> <input type="color" name="color" value="<c:out value="${color}"/>"/></td>
                </tr>
                <tr>
                    <th>Description:</th>
                    <td><input type="text" name="description" value="<c:out value="${description}"/>"/></td>
                </tr>
            </table>
            <input type="Submit" value="Zadat" />
        </form>
    </c:otherwise>

</c:choose>
<a href="/"><input type="Submit" value="Spat" /></a>

</body>
</html>