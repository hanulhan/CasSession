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

    <!-- Font Awesome -->
    <link href="<s:url value='/js/'/>vendors/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <!-- Bootstrap -->
    <link href="<s:url value='/js/'/>vendors/bootstrap/css/bootstrap.min.css" media="all" rel="stylesheet" type="text/css" />
    <!-- Datatables -->
    <link href="<s:url value='/js/'/>vendors/datatables/dataTables.bootstrap.min.css" media="all" rel="stylesheet" type="text/css" />
    <link href="<s:url value='/js/'/>vendors/datatables/buttons.bootstrap.min.css" rel="stylesheet">
    <!-- NProgress (waiting progressbar) -->
    <link href="<s:url value='/js/'/>vendors/nprogress/nprogress.css" rel="stylesheet">
    <!-- iCheck (schicke checkboxen) -->
    <link href="<s:url value='/js/'/>vendors/iCheck/skins/flat/blue.css" rel="stylesheet">
    <!-- Select2 (schicke selectboxen) -->
    <link href="<s:url value='/js/'/>vendors/select2/dist/css/select2.min.css" rel="stylesheet">
    <!-- PNotify -->
    <link href="<s:url value='/js/'/>vendors/pnotify/dist/pnotify.css" rel="stylesheet">
    <link href="<s:url value='/js/'/>vendors/pnotify/dist/pnotify.buttons.css" rel="stylesheet">

    <!-- Module specific headline -->
    <decorator:getProperty property="page.headlines" />

    <!-- Custom Theme Style -->
    <link href="<s:url value='/js/'/>vendors/custom.min.css" rel="stylesheet">
</head>

<body>

    <body class="nav-md footer_fixed">
    <div class="container body">
      <div class="main_container">
        <div class="col-md-3 left_col">
          <div class="left_col scroll-view">
            <div class="navbar nav_title" style="border: 0;">
              <a href="<s:url namespace='/session' action='doShowWelcome'/>" class="site_title"><i class="fa fa-cloud"></i> <span>Acentic Cloud</span></a>
            </div>

            <div class="clearfix"></div>

            <!-- menu profile quick info -->
            <div class="profile">
              <div class="hotel_info">
                <h2><i class="fa fa-home"></i> <a id="hotel_selected" href="<s:url value='/session/'/>doShowHotelSelection.action"></a></h2>
              </div>
            </div>
            <!-- /menu profile quick info -->

            <!-- sidebar menu -->
            <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
                <div id="menu" class="menu_section">
                </div>
            </div>
            <!-- /sidebar menu -->

            <!-- /menu footer buttons -->
            <div class="sidebar-footer hidden-small">
              <a data-toggle="tooltip" data-placement="top" title="Settings">
                <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>
              </a>
              <a id="fullscreen" data-toggle="tooltip" data-placement="top" title="FullScreen">
                <span class="glyphicon glyphicon-fullscreen" aria-hidden="true"></span>
              </a>
<!--
              <a data-toggle="tooltip" data-placement="top" title="Lock">
                <span class="glyphicon glyphicon-eye-close" aria-hidden="true"></span>
              </a>
-->
              <a id="logout" href="<s:url value='/public/'/>doLogout.action" data-toggle="tooltip" data-placement="top" title="Logout">
                <span class="glyphicon glyphicon-off" aria-hidden="true"></span>
              </a>
            </div>
            <!-- /menu footer buttons -->
          </div>
        </div>

        <!-- top navigation -->
        <div class="top_nav">
          <div class="nav_menu">
            <nav>
              <div class="nav toggle">
                <a id="menu_toggle"><i class="fa fa-bars"></i></a>
              </div>

              <ul class="nav navbar-nav navbar-right">
                <li class="">
                  <a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    <img src="<s:url value='/images/user/user.png'/>" alt=""><span id="displayName"></span>
                    <span class=" fa fa-angle-down"></span>
                  </a>
                  <ul class="dropdown-menu dropdown-usermenu pull-right">
                    <li><a href="javascript:;"> Profile</a></li>
<!--
                    <li>
                      <a href="javascript:;">
                        <span class="badge bg-red pull-right">50%</span>
                        <span>Settings</span>
                      </a>
                    </li>
-->
                    <li><a href="javascript:;">Help</a></li>
                    <li><a href="<s:url value='/public/'/>doLogout.action""><i class="fa fa-sign-out pull-right"></i> Log Out</a></li>
                  </ul>
                </li>

                <li role="presentation" class="dropdown">
                  <a href="javascript:;" class="dropdown-toggle info-number" data-toggle="dropdown" aria-expanded="false">
                    <i class="fa fa-envelope-o"></i>
                    <span class="badge bg-primary">6</span>
                  </a>
                  <ul id="menu1" class="dropdown-menu list-unstyled msg_list" role="menu">
                    <li>
                      <a>
                        <span class="image"><img src="<s:url value='/images/user/user.png'/>" alt="Profile Image" /></span>
                        <span>
                          <span>John Smith</span>
                          <span class="time">3 mins ago</span>
                        </span>
                        <span class="message">
                          Film festivals used to be do-or-die moments for movie makers. They were where...
                        </span>
                      </a>
                    </li>
                    <li>
                      <a>
                        <span class="image"><img src="<s:url value='/images/user/user.png'/>" alt="Profile Image" /></span>
                        <span>
                          <span>John Smith</span>
                          <span class="time">3 mins ago</span>
                        </span>
                        <span class="message">
                          Film festivals used to be do-or-die moments for movie makers. They were where...
                        </span>
                      </a>
                    </li>
                    <li>
                      <a>
                        <span class="image"><img src="<s:url value='/images/user/user.png'/>" alt="Profile Image" /></span>
                        <span>
                          <span>John Smith</span>
                          <span class="time">3 mins ago</span>
                        </span>
                        <span class="message">
                          Film festivals used to be do-or-die moments for movie makers. They were where...
                        </span>
                      </a>
                    </li>
                    <li>
                      <a>
                        <span class="image"><img src="<s:url value='/images/user/user.png'/>" alt="Profile Image" /></span>
                        <span>
                          <span>John Smith</span>
                          <span class="time">3 mins ago</span>
                        </span>
                        <span class="message">
                          Film festivals used to be do-or-die moments for movie makers. They were where...
                        </span>
                      </a>
                    </li>
                    <li>
                      <div class="text-center">
                        <a>
                          <strong>See All Alerts</strong>
                          <i class="fa fa-angle-right"></i>
                        </a>
                      </div>
                    </li>
                  </ul>
                </li>
              </ul>
            </nav>
          </div>
        </div>
        <!-- /top navigation -->

        <!-- page content -->
        <div class="right_col" role="main">
          <div class="">

            <div class="page-title">
              <div class="title_left">
                <h3><decorator:getProperty property="page.pagetitle" /></h3>
              </div>

                <decorator:getProperty property="page.pagetitle.right" />
            </div>

            <div class="clearfix"></div>

            <decorator:body />

          </div>
        </div>
        <!-- /page content -->

        <!-- footer content -->
        <footer>
          <div class="pull-right">
            &copy; Cloud Services by <a href="https://acentic.com" target="_blank">Acentic</a>
          </div>
          <div class="clearfix"></div>
        </footer>
        <!-- /footer content -->
      </div>
    </div>

    <!-- jQuery 2.2.4 -->
    <script src="<s:url value='/js/'/>vendors/jquery/jquery.min.js"></script>
    <!-- Bootstrap 3.3.6 -->
    <script src="<s:url value='/js/'/>vendors/bootstrap/js/bootstrap.min.js"></script>
    <!-- FastClick -->
    <script src="<s:url value='/js/'/>vendors/fastclick/fastclick.js"></script>
    <!-- strftime -->
    <script src="<s:url value='/js/'/>strftime/0.9/strftime-min.js"></script>
    <!-- Datatables 1.10.12 -->
    <script src="<s:url value='/js/'/>vendors/datatables/jquery.dataTables.min.js"></script>
    <script src="<s:url value='/js/'/>vendors/datatables/dataTables.bootstrap.min.js"></script>
    <script src="<s:url value='/js/'/>vendors/datatables/dataTables.buttons.js"></script>
    <script src="<s:url value='/js/'/>vendors/datatables/buttons.bootstrap.min.js"></script>
    <script src="<s:url value='/js/'/>vendors/datatables/buttons.html5.min.js"></script>
    <script src="<s:url value='/js/'/>vendors/datatables/buttons.print.min.js"></script>
    <script>
        /**
         * Read information from a column of checkboxes (input elements with type
         * checkbox) and return an array to use as a basis for sorting.
         *
         *  @summary Sort based on the checked state of checkboxes in a column
         *  @name Checkbox data source
         *  @author [Allan Jardine](http://sprymedia.co.uk)
         */

        $.fn.dataTable.ext.order['dom-checkbox'] = function(settings, col) {
            return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
                return $('input', td).prop('checked') ? '1' : '0';
            });
        };
    </script>
    <!-- NProgress 0.2.0 -->
    <script src="<s:url value='/js/'/>vendors/nprogress/nprogress.js"></script>
    <!-- iCheck 1.0.2 -->
    <script src="<s:url value='/js/'/>vendors/iCheck/icheck.min.js"></script>
    <!-- Select2 4.0.3 -->
    <script src="<s:url value='/js/'/>vendors/select2/dist/js/select2.full.min.js"></script>
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

    <!-- call the menu -->
    <script type="text/javascript">
        $(document).ready(function () {
            $.ajax({
                url: "<s:url action='getMenuUL.action' namespace='/session' />",
                type: "GET",
                cache: false,
                async: false,
                success: function (html) {
                    $("#menu").html(html);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert("unable to get menu information");
                    return;
                }
            });

            /* get user information
             * and populate details on screen
             * - user.displayname (Vorname Nachname)
             * - user.selectedHotel (Name of last selected Hotel)
             */
            $.ajax({
                async: false,
                cache: false,
                data: "{}",
                dataType: "json",
                type: "POST",
                url: '<s:url namespace="/session"  action="getLoggedInUserInformation"></s:url>',
                success: function (data) {
                    //console.log(data);
                    const hotel_selected = data.loggedInUser.selectedHotel ? data.loggedInUser.selectedHotel.hotelname : "<s:text name="session.user.noselectedhotel" />";
                    $("#hotel_selected").html(hotel_selected);

                    if(data && data.loggedInUser && data.loggedInUser.displayName) {
                        $("#displayName").html(data.loggedInUser.displayName);
                    }
                },
                error: function (req, status, error) {
                    alert("unable to get server list");
                }
            });

        });

    </script>

    <!-- Custom Theme Scripts -->
    <script src="<s:url value='/js/'/>vendors/custom.min.js"></script>

    <decorator:getProperty property="page.footlines"/>

</body>
</html>
