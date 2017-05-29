<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
    <head>
    </head>
<body>

    <div class="login_wrapper">

        <h1 class="text-center"><i class="fa fa-cloud"></i>&nbsp;Acentic Cloud</h1>
        <div class="clearfix"></div>

        <section class="login_content animated">
            <form>
                <h1><s:property value='logoutHeadline'/></h1>

                <div>
                    <a href="<s:url action='doAccessLoginUser' namespace='/public'/>"><s:property value='logoutReLogin'/></a>
                </div>
            </form>
        </section>
    </div>

</body>

</html>
