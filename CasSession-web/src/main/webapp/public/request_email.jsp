<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
    <head>
    </head>
<body>

    <div class="login_wrapper">

        <h1 class="text-center"><i class="fa fa-cloud"></i>&nbsp;Acentic Cloud</h1>
        <div class="clearfix"></div>

        <div class="login_content">
            <form id="reset_form" action="javascript:doSendRequest()">
                <h1><s:text name="session.resendpw.header"/></h1>

                <div>
                    <input type="email" class="form-control" placeholder="<s:text name='session.login.email' />" name="email" id="inputemail" maxlength="100" size="50" required />
                </div>

                <div>
                    <a class="btn btn-default submit" href="javascript: void(0);" onclick="doSendRequest()"><s:text name="session.login.reset"/></a>
                </div>

                <div class="clearfix"></div>
                <br/>

            </form>
        </div>

    </div>

</body>

    <content tag="footlines">
        <script>

            // animate login sections
            function animateCss () {
                $("input").val("");
            }

            // reset user password
            function doSendRequest() {
                // validate user form
                var valid = $("#reset_form").parsley().validate();
                if(!valid) return;

                var surl = "<s:url action='doTryResetPassword' namespace='/public'/>?email=" + $("#inputemail").val();
                $.ajax({
                    type: "GET",
                    url: surl,
                    async: false,
                    success: function (html) {
                        if ((html == null) || (html.jsonStatus == null)) {
                            // nice notify message
                            new PNotify({
                                title: "<s:text name='common.error' />",
                                styling: 'bootstrap3',
                                type: "error"
                            });
                            return;
                        }
                        else if (html.jsonStatus.status != "OK") {
                            // nice notify message
                            new PNotify({
                                title: "<s:text name='common.error' />",
                                text: html.jsonStatus.errorMsg,
                                styling: 'bootstrap3',
                                type: "error"
                            });
                            return
                        }
                        else {
                            // redirect user on password reset request
                            window.location.href = "<s:url action='accessPasswordResetConfirm' namespace='/public'/>";

                            return;
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        // nice notify message
                        new PNotify({
                            title: "<s:text name='common.error' />",
                            text: errorThrown + ": " + textStatus,
                            styling: 'bootstrap3',
                            type: "error"
                        });
                        return;
                    }
                });
            }

        </script>
    </content>

</html>
