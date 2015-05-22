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

    <title>Bazar u zeleneho kozla</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/heroic-features.css" rel="stylesheet">

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

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
                    <a href="https://goo.gl/maps/wSjY5">Kde nas najdete</a>
                </li>
                <li>
                    <a href="#">Kontakt</a>
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
        <h1>Vitajte v bazare u zeleneho kozla!</h1>
    </header>
    <hr>

    <div class="container hero-spacer" style="padding: 0">
        <button type="button" class="btn btn-info" data-toggle="collapse" style="width: 100%" data-target="#demo">Filter</button>
        <div id="demo" class="collapse out">
            <div class="thumbnail " style="width: 100%">
                <form role="form">
                    <div class="form-group">
                        <label for="znacka">Znacka:</label>
                        <input type="text" class="form-control" id="znacka">
                        <label for="farba">Farba:</label>
                        <input type="text" class="form-control" id="farba">
                    </div>

                    <div class="form-group ">
                        <label for="kmDo">Najazdene od :</label>
                        <input type="text" class="form-control" id="kmDo">
                        <label for="kmOd">Najazdene do :</label>
                        <input type="text" class="form-control" id="kmOd">
                    </div>

                    <div class="form-group ">
                        <label for="cenaDo">Cena do :</label>
                        <input type="text" class="form-control" id="cenaDo">
                        <label for="cenaOd">Cena od :</label>
                        <input type="text" class="form-control" id="cenaOd">
                    </div>
                    <a href="/cars" class="btn btn-primary" style="width: 100%">Vyhladat!</a>
                </form>
            </div>
        </div>
    </div>


    <!-- Title -->
    <div class="row">
        <div class="col-lg-12">
            <h3>Aktualna ponuka</h3>
        </div>
    </div>
    <!-- /.row -->

    <!-- Page Features -->
    <div class="row text-center">

        <c:forEach items="${cars}" var="car">
            <div class="col-md-3 col-sm-6 hero-feature">
                <div class="thumbnail">
                    <img src="buyIcon.jpg" alt="" height="80" width="80">
                    <div class="caption">
                        <h3><c:out value="${car.manufacturer}"/></h3>
                        <p>KM: <c:out value="${car.km}"/></p>
                        <p>Farba: <c:out value="${car.color}"/></p>
                        <p>Popis: <c:out value="${car.description}"/></p>
                        <p>Cena: <c:out value="${car.price}"/></p>
                        <p>

                        <a href="${pageContext.request.contextPath}/buy?id=${car.id}" class="btn btn-primary">Kupit!</a>
                        </p>
                    </div>
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
                <p>Copyright &copy; Your Website 2014</p>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <a href="/cars" class="btn btn-primary">Spravovat!</a>
            </div>
        </div>
    </footer>

</div>
<!-- /.container -->

</body>

</html>