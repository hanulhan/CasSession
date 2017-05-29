<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
    <head>
        <content tag="headlines">
            <!-- Datatables Responsive Bootstrap -->
            <link  rel="stylesheet" type="text/css" href="<s:url value='/js/'/>vendors/datatables.net-responsive-bs/css/responsive.bootstrap.min.css" />
        </content>
    </head>
<body>

    <div class="row">
        <div class="col-md-12 col-sm-12 col-xs-12">

            <div class="x_panel">

                <div class="x_title">
                    <h2><s:text name="system.hotel.header"/></h2>
                    <div class="clearfix"></div>
                </div>

                <div class="x_content">

                    <table id="table_hotels" class="display table-striped table-bordered responsive nowrap" width="100%">
                        <thead>
                            <tr>
                                <th><s:text name="system.hotel.list.status" /></th>
                                <th><s:text name="system.hotel.list.ident" /></th>
                                <th><s:text name="system.hotel.list.hotelname" /></th>
                                <th><s:text name="system.hotel.list.city" /></th>
                                <th><s:text name="system.hotel.list.country" /></th>
                                <th><s:text name="system.hotel.list.sidecode" /></th>
                                <th><s:text name="system.hotel.list.connecturl" /></th>
                                <th><s:text name="system.hotel.list.timeoffline" /></th>
                                <th><s:text name="system.hotel.list.lastip" /></th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>

                </div>  <!-- x_content  -->

            </div>  <!-- x_panel    -->

        </div>
    </div>

</body>

<content tag="footlines">
    <!-- Datatables Responsive Bootstrap -->
    <script src="<s:url value='/js/'/>vendors/datatables.net-responsive/js/dataTables.responsive.min.js"></script>
    <script src="<s:url value='/js/'/>vendors/datatables.net-responsive-bs/js/responsive.bootstrap.js"></script>

    <script type="text/javascript">
        var sDateFormat = "<s:text name='global.dateformat.long'/>";
        var oTable = null;
        var isHotelAccount = true;

        $(document).ready(function () {

            oTable = $('#table_hotels').DataTable({
                "stateSave": false,
                "paging": true,
                "processing": true,
                "serverSide": false,
                "language": {
                    "processing": "<s:text name='global.table.Processing'/>",
                    "lengthMenu": "<s:text name='global.table.lengthMenu'/>",
                    "zeroRecords": "<s:text name='global.table.ZeroRecords'/>",
                    "info": "<s:text name='global.table.Info'/>",
                    "infoEmpty": "<s:text name='global.table.InfoEmpty'/>",
                    "infoFiltered": "<s:text name='global.table.InfoFiltered'/>",
                    "infoPostFix": "",
                    "search": "<s:text name='global.table.Search'/>",
                    "url": "",
                    "paginate": {
                        "first": "<s:text name='global.table.Paginate.First'/>",
                        "previous": "<s:text name='global.table.Paginate.Previous'/>",
                        "next": "<s:text name='global.table.Paginate.Next'/>",
                        "last": "<s:text name='global.table.Paginate.Last'/>"
                    }
                }
            });

            if (!loadConfig()) {
                alert("unable to load configuration");
                window.location = "<%=request.getContextPath()%>";
            }

            if (!loadHotelList()) {
                alert("unable to initialize system hotels");
                window.location = "<%=request.getContextPath()%>";
            }

        });


        function loadConfig() {
            $.ajax({
                url: "<s:url action='getLoggedInUserInformation.action' namespace='/session'/>",
                type: "GET",
                cache: false,
                async: false,
                success: function (html) {
                    if ((html == null) || (html.jsonStatus == null) || (html.jsonStatus.status == "ERROR")) {
                        console.log("unable to load configuration");
                        return false;
                    } else {
                        // console.log("userrights", html.userrights);
                        isHotelAccount = html.loggedInUser.hotelaccount;
                        return true;
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("unable to load configuration  status:" + textStatus);
                    return false;
                }
            });
            return true;
        }


        function loadHotelList() {
            $.ajax({
                url: "<s:url action='doGetHotelList.action' namespace='/session'/>",
                type: "GET",
                cache: false,
                async: false,
                success: function (html) {
                    if ((html == null) || (html.jsonStatus == null) || (html.jsonStatus.status == "ERROR")) {
                        console.log("unable to load hotel list");
                        return false;
                    } else {

                        if(isHotelAccount) {
                            oTable.columns( [ 5, 6, 7, 8 ] ).visible( false, false );
                        }
                        oTable.columns.adjust().draw(); // adjust column sizing and redraw

                        for (var i = 0; i < html.hotels.length; i++) {
                            var el = html.hotels[i];


                            var minutesOffline = parseInt(el.minutesOffline);
                            var timeConverted = minutesOffline ? timeConvert(minutesOffline) : "-";

                            var status = "";
                            if(minutesOffline >= 60 || minutesOffline == 0) {
                                status = "exclamation";
                            }
                            else if(minutesOffline < 60 && minutesOffline > 15) {
                                status = "minus";
                            }
                            else {
                                status = "check";
                            }

                            var connectStatus = '<i class="fa fa-' + status + '-circle"></i>';

                            var hotelname = '<a href="<s:url namespace="/session"  action="doSetHotel"></s:url>?ident=' + el.ident + '">' + el.hotelname + '</a>';

                            // teamviewer link
                            var connect = '<i class="fa fa-desktop"></i>';
                            if(el.teamViewerID != 0) {
                                connect = '<a href="'+el.connectURL+'"><i class="fa fa-desktop"></i> '+el.teamViewerID+'</a>'
                            }

                            // combine row data
                            var row = [
                                connectStatus,
                                el.ident,
                                hotelname,
                                el.city,
                                el.country,
                                el.customerSideCode,
                                connect,
                                timeConverted,
                                el.lastIP,
                            ]
                            oTable.row.add( row );
                        };
                        oTable.draw();

                        // align first cell
                        $('#table_hotels tr td:nth-child(1)').addClass('dt-center');

                        // recalc responsivness
                        oTable.responsive.recalc();
                        //return true;
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("unable to load hotel list status:" + textStatus);
                    return false;
                }
            });
            return true;
        }


        /* helper function to convert backend minutes
         * into human readable date, as days, hours, minutes … */
        function timeConvert(timestamp) {
            // defines units and converts time
            var units = {
                "year": 24*60*365,
                "month": 24*60*30,
                "week": 24*60*7,
                "day": 24*60,
                "hour": 60,
                "minute": 1
            }
            // calculate result
            var result = "";
            for(var name in units) {
                var p =  Math.floor(timestamp/units[name]);
                if(p == 1) result += ( " " + p + " " + name);
                if(p >= 2) result += ( " " + p + " " + name + "s");
                timestamp %= units[name]
            }

            return result;
        }

    </script>

</content>

</html>
