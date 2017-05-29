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
            <s:form action="doUserLoginUnregistered" theme="simple" namespace="/public">
                <h1><s:text name="session.loginunregistered.header"/></h1>
                <s:hidden name="link" />
                <div>
                    <input type="email" class="form-control" placeholder="<s:text name='session.login.email' />" name="email" id="email" maxlength="100" size="50" required />
                </div>

                <div>
                    <button type="submit" class="btn btn-default" href="javascript: void(0);" onclick="doSendRequest()"><s:text name="session.login.submit"/></button>
                </div>

            </s:form>
        </section>

    </div>

</body>


<content tag="footlines">
    <script type="text/javascript">

        // reset user password
        function doSendRequest() {
            // validate user form
            var valid = $("#doUserLoginUnregistered").parsley().validate();
            if(!valid) return;

            // submit form
            document.doUserLoginUnregistered.submit();
        }

    </script>
</content>

</html>
