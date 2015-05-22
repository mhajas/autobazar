<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Bazar u zeleného kozla</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/heroic-features.css" rel="stylesheet">


    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>

    <![endif]-->

</head>

<body>

<!-- Navigation -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">Ponuka</a>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li>
                    <a href="https://goo.gl/maps/wSjY5">Kde nás nájdete</a>
                </li>
                <li>
                    <a href="/cars" >Spravovať</a>
                </li>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container -->
</nav>

<!-- Page Content -->
<div class="container">

    <!-- Title -->
    <div class="row">
        <div class="col-lg-12">
            <h3>Toto auto chcete kúpiť</h3>
        </div>
    </div>
    <!-- /.row -->

    <!-- Page Features -->
    <div class="row text-center">

        <div class="col-md-3 col-sm-6 hero-feature">
            <div class="thumbnail">
                <img src="buyIcon.jpg" alt="" height="80" width="80">
                <div class="caption">
                    <h3><c:out value="${car.manufacturer}"/></h3>
                    <p>KM: <c:out value="${car.km}"/></p>
                    <p class="filter-p">Farba:</p>
                    <input type="color" name="color" value="<c:out value="${color}"/>"/>
                    <p>Popis: <c:out value="${car.description}"/></p>
                    <p>Cena: <c:out value="${car.price}"/></p>
                </div>
            </div>
        </div>

        <div class="col-md-3 col-sm-6 hero-feature">
            <div class="thumbnail">
                <div class="caption">
                    <h3>Zadajte svoje osobné údaje</h3>
                    <form action="${pageContext.request.contextPath}/buy/sell" method="post">
                        <p>Meno a priezvisko:</p>
                        <input type="text" name="name" value="<c:out value="${name}"/>"/>
                        <p>Adresa:<p>
                        <input type="text" name="adress" value="<c:out value="${adress}"/>"/>

                        <p>Dátum narodenia:</p>
                        <fmt:formatDate value="" type="BOTH" var="parsedBornDate" pattern="yyyy-MM-dd" />
                        <c:set var="string2" value="${fn:replace(parsedBornDate, ' ', 'T')}" />
                        <td><input name="born" type="date" step="1" value="${string2}"/></td>

                        <input type="hidden" name="carId" class="btn btn-primary" value="${car.id}" />
                        <input type="Submit" class="btn btn-primary" value="Kúpiť" />
                    </form>
                </div>
            </div>
        </div>
    </div>
    <a href="/" class="btn btn-primary">Zrušiť</a>

    <!-- /.row -->

    <hr>

    <!-- Footer -->
    <footer>
        <div class="row">
            <div class="col-lg-12">
                <p>Projekt PB138</p>
            </div>
        </div>
    </footer>

</div>
<!-- /.container -->

</body>

</html>