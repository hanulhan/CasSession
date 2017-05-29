<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
    <head>
    </head>
<body>

    <div class="row">
        <div class="col-md-12 col-sm-12 col-xs-12">

            <div class="x_panel">

                <div class="x_title">
                    <h2><s:text name="session.sessions.headline"/></h2>
                    <div class="clearfix"></div>
                </div>

                <div class="x_content">

                    <table id="table_data" class="display table-striped table-bordered responsive nowrap" width="100%">
                        <thead>
                            <tr>
                                <th><s:text name="session.list.hotelname" /></th>
                                <th><s:text name="session.list.lastAction" /></th>
                                <th><s:text name="session.list.lastMenu" /></th>
                                <th><s:text name="session.list.userName" /></th>
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

    <script src="<s:url value='/js/'/>vendors/moment/moment.min.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {

            oTable = $('#table_data').DataTable({
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

            if (!loadSessions()) {
                alert("unable to load sessions");
            }

        });

        function loadSessions() {
            $.ajax({
                url: "<s:url action='getSessions.action' namespace='/session'/>",
                type: "GET",
                cache: false,
                async: false,
                success: function (html) {
                    if ((html == null) || (html.jsonStatus == null) || (html.jsonStatus.status == "ERROR")) {
                        console.log("unable to load hotel list");
                        return false;
                    } else {

                        // clear datatable
                        var oTable = $('#table_data').dataTable();
                        oTable.fnClearTable();

                        // the update w/ data
                        for (var i = 0; i < html.sessions.length; i++) {
                            var el = html.sessions[i];

                            // contruct data
                            var addId = oTable.fnAddData([
                                el.hotelname || "",                                         // hotelname
                                moment(el.lastAction).format("DD.MM.YYYY HH:mm") || "",     // lastAction
                                el.lastMenu || "",                                          // lastMenu
                                String(el.userName).split(',').reverse().join(' ') || "",   // userName
                            ],
                            false);
                            var theNode = oTable.fnSettings().aoData[addId[0]].nTr;
                            theNode.setAttribute('id', el.ident);
                        };
                        oTable.fnDraw();

                        return true;

                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("unable to load hotel list status:" + textStatus);
                    return false;
                }
            });
            return true;
        }

    </script>

</content>

</html>
