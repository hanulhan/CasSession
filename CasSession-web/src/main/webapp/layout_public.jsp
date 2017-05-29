<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>
        <decorator:title default="Acentic Cloud Services" />
    </title>

    <!-- Bootstrap -->
    <link href="<s:url value='/js/'/>vendors/bootstrap/css/bootstrap.min.css" media="all" rel="stylesheet" type="text/css" />

    <!-- Font Awesome -->
    <link href="<s:url value='/js/'/>vendors/font-awesome/css/font-awesome.min.css" rel="stylesheet">

    <!-- NProgress (waiting progressbar) -->
    <link href="<s:url value='/js/'/>vendors/nprogress/nprogress.css" rel="stylesheet">

    <!-- PNotify -->
    <link href="<s:url value='/js/'/>vendors/pnotify/dist/pnotify.css" rel="stylesheet">
    <link href="<s:url value='/js/'/>vendors/pnotify/dist/pnotify.buttons.css" rel="stylesheet">

    <!-- Module specific headline -->
    <decorator:getProperty property="page.headlines" />

    <!-- Custom Theme Style -->
    <link href="<s:url value='/js/'/>vendors/custom.min.css" rel="stylesheet">
</head>

<body class="login">

    <div class="footer_fixed">

            <decorator:body />

        <!-- /page content -->

        <!-- footer content -->
        <footer>
          <div class="pull-right">
            &copy; Cloud Services by <a href="https://acentic.com" target="_blank">Acentic</a>
          </div>
          <div class="clearfix"></div>
        </footer>

    </div>

    <!-- jQuery 2.2.4 -->
    <script src="<s:url value='/js/'/>vendors/jquery/jquery.min.js"></script>
    <!-- Bootstrap 3.3.6 -->
    <script src="<s:url value='/js/'/>vendors/bootstrap/js/bootstrap.min.js"></script>
    <!-- FastClick -->
    <script src="<s:url value='/js/'/>vendors/fastclick/fastclick.js"></script>

    <!-- NProgress 0.2.0 -->
    <script src="<s:url value='/js/'/>vendors/nprogress/nprogress.js"></script>

    <!-- PNotify 3.0.0  -->
    <script src="<s:url value='/js/'/>vendors/pnotify/dist/pnotify.js"></script>
    <script src="<s:url value='/js/'/>vendors/pnotify/dist/pnotify.buttons.js"></script>

    <!-- Parsley 2.4.3 -->
    <script type="text/javascript" src="<s:url value='/js/'/>vendors/parsleyjs/parsley.min.js"></script>
    <!-- // browser language // -->
    <script type="text/javascript">
        var lang = navigator.language || navigator.userLanguage;
        $.getScript("<s:url value='/js/'/>vendors/parsleyjs/i18n/" + lang + ".js", function(){});
    </script>


    <!-- Custom Theme Scripts -->
    <script src="<s:url value='/js/'/>vendors/custom.min.js"></script>

    <decorator:getProperty property="page.footlines"/>

</body>
</html>
