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
            <form id="login_form" action="javascript:doLogin()">
                <h1><s:text name='session.loginunregistered.header' /></h1>
                <div>
                    <input type="text" class="form-control" placeholder="<s:text name='session.login.username' />" name="username" id="inputloginname" required />
                </div>

                <div>
                    <input type="password" class="form-control" placeholder="<s:text name='session.login.password' />" id="inputpassword" name="password" required />
                </div>

                <div>
                    <a class="btn btn-default submit" href="javascript:void(0);" onclick="doLogin();"><s:text name="session.login.submit"/></a>
                </div>

                <div class="clearfix"></div>
                <br/>

                <div class="separator">
                    <a href="javascript:void(0);" onclick="animateCss();"> <s:text name="session.login.lostpassword"/> </a>
                </div>

            </form>
        </section>

        <section class="login_content">
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

                <div class="separator">
                    <a href="javascript:void(0);" onclick="animateCss();"> <s:text name="session.login.submit"/> </a>
                </div>

            </form>
        </section>

    </div>

</body>

    <content tag="footlines">
        <script>

            // animate login sections
            function animateCss () {
                $(".login_content").toggleClass("animated");
                $("input").val("");
            }

            // login user w/ credentials
            function doLogin() {
                // validate user form
                var valid = $("#login_form").parsley().validate();
                if(!valid) return;

                var surl = "<s:url action='doLoginUser' namespace='/public'/>";
                var credientials = {
                    username: $("#inputloginname").val(),
                    password: $("#inputpassword").val()
                };
                var sdata = credientials;

                $.ajax({
                    type: "POST",
                    url: surl,
                    data: sdata,
                    async: false,
                    success: function (html) {
                        // redirect user when authenticated
                        window.location.href = "<s:url action='doShowWelcome' namespace='/session'/>";

                        return;
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
