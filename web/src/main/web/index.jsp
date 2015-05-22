<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.js"></script>
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

    <!-- Jumbotron Header -->
    <header class="jumbotron hero-spacer">
        <h1>Vitajte v bazáre u zeleného kozla</h1>
    </header>
    <hr>




    <c:if test="${not empty chyba}">
        <div style="border: solid 1px red; background-color: yellow; padding: 10px">
            <c:out value="${chyba}"/>
        </div>
    </c:if>

    <!-- Title -->
    <div class="row">
        <div class="col-lg-12">
            <h3>Aktuálna ponuka</h3>
        </div>
    </div>
    <!-- /.row -->

    <!-- Page Features -->
    <div class="row text-center">
        <div class="col-md-3 col-sm-6 hero-feature">
            <div class="thumbnail">
                <button type="button" class="btn btn-info" data-toggle="collapse" style="width: 100%" data-target="#demo">Filter</button>
                <div id="demo" class="collapse in">
                    <form role="form" action="${pageContext.request.contextPath}/" method="post">
                        <div class="form-group" style="margin-bottom: 0">
                            <p class="filter-p">Značka:</p>
                            <input type="text" name="manufacturer" class="filter-input" value="<c:out value="${manufacturer}"/>"/>
                            <p class="filter-p">Farba:</p>
                            <input type="color" name="color" class="filter-input" value="<c:out value="${color}"/>"/>

                            <p class="filter-p">Najazdené od :</p>
                            <input type="text"  name="kmMore" class="filter-input" value="<c:out value="${kmMore}"/>"/>
                            <p class="filter-p">Najazdené do :</p>
                            <input type="text"  name="kmLess" class="filter-input" value="<c:out value="${kmLess}"/>"/>

                            <p class="filter-p">Cena od :</p>
                            <input type="text" name="priceMore" class="filter-input" value="<c:out value="${priceMore}"/>"/>
                            <p class="filter-p">Cena do :</p>
                            <input type="text" name="priceLess" class="filter-input" style="margin-bottom: 4px" value="<c:out value="${priceLess}"/>"/>
                        </div>
                        <input type="Submit" class="btn btn-primary" style="width: 49%;padding: 0" value="Odoslať" />
                        <a href="${pageContext.request.contextPath}/" class="btn btn-primary" style="width: 49%;padding: 0">Zrušiť filter</a>
                    </form>
                </div>
            </div>
        </div>
        <c:forEach items="${cars}" var="car">
            <div class="col-md-3 col-sm-6 hero-feature">
                <div class="thumbnail">
                    <img src="buyIcon.jpg" alt="" height="80" width="80">
                    <p class="caption">
                        <h3><c:out value="${car.manufacturer}"/></h3>
                        <p>KM: <c:out value="${car.km}"/></p>

                        <p style="display: inline-block">Farba:
                            <div style="margin: 0 5px; display: inline-block;
                            background: ${car.color}; height: 11px; width: 100px; display: inline-block; border: 1px solid transparent;
                            border-radius: 4px;"> </div>
                        </p>
                        <p>Popis: <c:out value="${car.description}"/></p>
                        <p>Cena: <c:out value="${car.price}"/></p>
                        <a href="${pageContext.request.contextPath}/buy?id=${car.id}" class="btn btn-primary">Kúpiť!</a>
                    </p>
                </div>
            </div>
        </c:forEach>

    </div>
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