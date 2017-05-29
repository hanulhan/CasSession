<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
    <head>
    </head>
<body>

    <div class="x_panel">

        <div class="x_title">
            <h2>Session test functions</h2>
            <div class="clearfix"></div>
        </div>

        <div class="x_content">
            <i class="fa fa-edit"></i> <a href='<s:url namespace="/session"  action="getMenuUL"></s:url>'>getMenuUL</a>
            <br>
        </div>

        <div class="x_content">
            <i class="fa fa-edit"></i> <a href='<s:url namespace="/session"  action="doGetHotelList"></s:url>'>get Hotel List</a>
            <br>
        </div>

        <div class="x_content">
            <i class="fa fa-edit"></i> <a href='<s:url namespace="/session"  action="doSetHotel"></s:url>?ident=VIDEOSYS'>set Hotel List</a>
            <br>
        </div>

        <div class="x_content">
            <i class="fa fa-edit"></i> <a href='<s:url namespace="/session"  action="doShowHotelSelection"></s:url>'>hotel selection</a>
            <br>
        </div>

        <div class="x_content">
            <i class="fa fa-edit"></i> <a href='<s:url namespace="/session"  action="accessFilter"></s:url>'>hotel filter</a>
            <br>
        </div>

        <div class="x_content">
            <i class="fa fa-edit"></i> <a href='<s:url namespace="/session"  action="getLoggedInUserInformation"></s:url>'>user information</a>
            <br>
        </div>

        <div class="x_content">
            <i class="fa fa-edit"></i> <a href='<s:url namespace="/session"  action="accessActiveSessions"></s:url>'>show active sessions</a>
            <br>
        </div>

    </div>

</body>
</html>
