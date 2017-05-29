<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>

<head>
</head>

<body>

    <div class="x_panel">

        <div class="x_title">
            <h2><s:text name="session.filter.hotel.headline"/></h2>
            <div class="clearfix"></div>
        </div>

        <div class="x_content">
            <table id="table_hotels" class="display table-striped table-bordered">
                <thead>
                    <tr>
                        <th><s:text name="session.filter.hotel.selected" /></th>
                        <th><s:text name="session.filter.hotel.status" /></th>
                        <th><s:text name="session.filter.hotel.ident" /></th>
                        <th><s:text name="session.filter.hotel.hotelname" /></th>
                        <th><s:text name="session.filter.hotel.city" /></th>
                        <th><s:text name="session.filter.hotel.country" /></th>
                        <th><s:text name="session.filter.hotel.timeoffline" /></th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>

    </div>
</body>

<content tag="footlines">

    <script type="text/javascript">
        $(document).ready(function() {

            // Create datatable w/ callbacks
            $('#table_hotels').DataTable({
                "bStateSave": false,
                "bPaginate": true,
                "bProcessing": true,
                "bFilter": true,
                "bInfo": true,
                "bServerSide": false,
                "aoColumns": [
                    { "width": "10%", "sType": "string", "sClass": "dt-center" }, // selected
                    { "width": "10%", "sType": "string", "sClass": "dt-center" }, // status
                    { "width": "10%", "sType": "string" }, // ident
                    { "width": "30%", "sType": "string" }, // hotelname
                    { "width": "20%", "sType": "string" }, // city
                    { "width": "10%", "sType": "string", "sClass": "dt-center" }, // country
                    { "width": "10%", "sType": "string" }, // timeoffline
                ],
                "order": [[ 0, "desc" ]],
                "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    $('td:eq(0)', nRow).html(aData[0].replace('#ident#', aData[2]));
                },
                "oLanguage": {
                    "sProcessing": "<s:text name='global.table.Processing'/>",
                    "sLengthMenu": "<s:text name='global.table.lengthMenu'/>",
                    "sZeroRecords": "<s:text name='global.table.ZeroRecords'/>",
                    "sInfo": "<s:text name='global.table.Info'/>",
                    "sInfoEmpty": "<s:text name='global.table.InfoEmpty'/>",
                    "sInfoFiltered": "<s:text name='global.table.InfoFiltered'/>",
                    "sInfoPostFix": "",
                    "sSearch": "<s:text name='global.table.Search'/>",
                    "sUrl": "",
                    "oPaginate": {
                        "sFirst": "<s:text name='global.table.Paginate.First'/>",
                        "sPrevious": "<s:text name='global.table.Paginate.Previous'/>",
                        "sNext": "<s:text name='global.table.Paginate.Next'/>",
                        "sLast": "<s:text name='global.table.Paginate.Last'/>"
                    }
                }
            });

            // load list of circuittyp
            if (!loadHotels()) {
                alert("unable to initialize hotels list");
            };

        }); // End document.ready


        // Load list of circuittyps
        function loadHotels() {
            $.ajax({
                url: "<s:url action='doGetHotelList' namespace='/session'/>",
                type: "GET",
                cache: false,
                async: false,
                success: function(html) {
                    if (html == null || html.jsonStatus == null || html.jsonStatus.status != "OK") {
                        // nicer error message
                        new PNotify({
                            title: "<s:text name='common.error' />",
                            text: html.StatusMsg,
                            styling: 'bootstrap3',
                            type: "error"
                        });
                        return false;
                    } else {
                        // get list of selected hotels
                        getSelectedIdents(function(callback) {
                            // clear datatable
                            var oTable = $('#table_hotels').dataTable();
                            oTable.fnClearTable();

                            // the update w/ data
                            for (var i = 0; i < html.hotels.length; i++) {
                                var el = html.hotels[i];
                                var checked = "";
                                var order = "0";

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

                                var connectStatus = '<i class="fa fa-' + status + '-circle" data-order' + status + '></i>';

                                // check if selected
                                if(callback.indexOf(el.ident) > -1) {
                                    checked = "checked";
                                    order = "1";
                                }
                                // contruct data
                                var addId = oTable.fnAddData([
                                    "<input name='ident' value='#ident#' onclick='javascript:doChangeSelection()' type='checkbox'" + checked + " data-order='" + order + "' \>", // selected state => ident will be set by datatable callback
                                    connectStatus || "", // status
                                    el.ident || "", // ident
                                    el.hotelname || "", // hotelname
                                    el.city || "", // city
                                    el.country || "", // country
                                    timeConverted || "", // timeoffline
                                ],
                                false);
                                var theNode = oTable.fnSettings().aoData[addId[0]].nTr;
                                theNode.setAttribute('id', el.ident);
                                // console.log("add " + el.ident);
                            };
                            oTable.fnDraw();

                            return true;
                        });
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    // nicer error message
                    new PNotify({
                        title: "<s:text name='common.error' />",
                        text: html.StatusMsg,
                        styling: 'bootstrap3',
                        type: "error"
                    });
                    return false;
                }
            });
            return true;
        };

        function getSelectedIdents(callback) {
            $.ajax({
                type: "GET",
                url: "<s:url action='getSelectedIdents.action' namespace='/session'/>",
                async: false,
                success: function(html) {
                    if ((html == null) || (html.jsonStatus == null)) {
                        console.log("unable to save");
                        return;
                    } else if (html.jsonStatus.status != "OK") {
                        alert(html.jsonStatus.errorMsg);
                        return false;
                    } else {
                        // console.log(html.idents);
                        callback(html.idents || "");
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    //console.log("unable to save " + textStatus);
                    return false;
                }
            });
        }

        // Submit edit form to create or update circuittyp
        function doChangeSelection() {
            var surl = "<s:url action='setSelectedIdents.action' namespace='/session'/>";

            var checkedIdents = $("input[name=ident]:checked").map(function() {
                return this.value;
            }).get();

            var obj = {};
            obj.idents = checkedIdents;
            var sdata = JSON.stringify(obj);

            $.ajax({
                type: "POST",
                url: surl,
                dataType: 'json',
                async: false,
                data: sdata,
                contentType: "application/json; charset=utf-8",
                success: function(html) {
                    if ((html == null) || (html.jsonStatus == null)) {
                        console.log("unable to save");
                        return;
                    } else if (html.jsonStatus.status != "OK") {
                        alert(html.jsonStatus.errorMsg);
                        return;
                    } else {
                        return;
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    //console.log("unable to save " + textStatus);
                    return;
                }
            });
        };


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
