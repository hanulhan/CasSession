<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
    <head>
    </head>
<body>

    <div class="login_wrapper" style="width: 600px; max-width: 90%; min-height:600px">

        <h1 class="text-center"><i class="fa fa-cloud"></i>&nbsp;Acentic Cloud</h1>
        <div class="clearfix"></div>

        <section class="login_content animated">
            <form id="profile_form" action="javascript:doSendRequest()" class="form-horizontal form-label-left input_mask">
                <h1><s:text name="session.profile.header" /></h1>

                <div class="col-md-6 col-sm-6 col-xs-12 form-group has-feedback">
                    <input type="text" class="form-control has-feedback-left" placeholder="<s:text name='session.profile.firstname' />" id="firstName" maxlength="50" size="50" required />
                    <span class="fa fa-user form-control-feedback left" aria-hidden="true"></span>
                </div>

                <div class="col-md-6 col-sm-6 col-xs-12 form-group has-feedback">
                    <input type="text" class="form-control has-feedback-right" placeholder="<s:text name='session.profile.lastname' />" id="lastName" maxlength="50" size="50" required />
                    <span class="fa fa-user form-control-feedback right" aria-hidden="true"></span>
                </div>

                <div class="col-md-6 col-sm-6 col-xs-12 form-group has-feedback">
                    <input type="text" class="form-control has-feedback-left" placeholder="<s:text name='session.profile.telefon_work' />" id="telefonWork" maxlength="50" size="50" data-parsley-pattern="^[\d\+\-\.\(\)\/\s]*$"  />
                    <span class="fa fa-phone form-control-feedback left" aria-hidden="true"></span>
                </div>

                <div class="col-md-6 col-sm-6 col-xs-12 form-group has-feedback">
                    <input type="text" class="form-control has-feedback-left" placeholder="<s:text name='session.profile.telefon_mobile' />" id="telefonMobile" maxlength="50" size="50" data-parsley-pattern="^[\d\+\-\.\(\)\/\s]*$"  />
                    <span class="fa fa-mobile form-control-feedback left" aria-hidden="true"></span>
                </div>

                <div class="col-md-12 col-sm-12 col-xs-12 form-group has-feedback">
                    <input type="email" class="form-control has-feedback-left" placeholder="<s:text name='session.profile.email' />" id="emailaddress" maxlength="100" size="50" required />
                    <span class="fa fa-envelope form-control-feedback left" aria-hidden="true"></span>
                </div>

                <div class="col-md-12 col-sm-12 col-xs-12 form-group has-feedback">
                    <input type="text" class="form-control has-feedback-left" placeholder="<s:text name='session.profile.loginname' />" name="loginName" id="loginName" required />
                    <span class="fa fa-user form-control-feedback left" aria-hidden="true"></span>
                </div>

                <div class="col-md-12 col-sm-12 col-xs-12 form-group has-feedback">
                    <input type="password" class="form-control has-feedback-left" placeholder="<s:text name='session.profile.password' />" id="password" name="password" required minlength="8" />
                    <span class="fa fa-key form-control-feedback left" aria-hidden="true"></span>
                </div>

                <div class="col-md-12 col-sm-12 col-xs-12 form-group has-feedback">
                    <input type="password" class="form-control has-feedback-left" placeholder="<s:text name='session.profile.retypedPassword' />" id="retypedPassword" name="retypedPassword" required data-parsley-equalto="#password" />
                    <span class="fa fa-key form-control-feedback left" aria-hidden="true"></span>
                </div>

                <div class="col-md-12 col-sm-12 col-xs-12 form-group">
                    <a class="btn btn-default submit" href="javascript:void(0);" onclick="doSendRequest();"><s:text name="session.login.submit"/></a>
                </div>

            </form>
        </section>

    </div>

</body>

<content tag="footlines">

    <script type="text/javascript">
        // retrieve user profile data
        $(document).ready(function () {
            $.ajax({
                async: false,
                cache: false,
                data: "{}",
                dataType: "json",
                type: "POST",
                url: '<s:url namespace="/public"  action="getUserData"></s:url>',
                success: function (data) {
                    $("#firstName").val(data.userProfile.firstName);
                    $("#lastName").val(data.userProfile.lastName);
                    $("#telefonWork").val(data.userProfile.telefonWork);
                    $("#telefonMobile").val(data.userProfile.telefonMobile);
                    $("#emailaddress").val(data.userProfile.email);
                    $("#loginName").val(data.userProfile.loginName);
                },
                error: function (req, status, error) {
                    alert("unable to get user profile");
                }
            });
        });

        // update user profile w/ provided data
        function doSendRequest() {

            // validate user form
            var valid = $("#profile_form").parsley().validate();
            if(!valid) return;

            var surl = "<s:url action='setUserData' namespace='/public'/>";

            var currObj = {};
            currObj.firstName = $("#firstName").val();
            currObj.lastName = $("#lastName").val();
            currObj.telefonWork = $("#telefonWork").val();
            currObj.telefonMobile = $("#telefonMobile").val();
            currObj.email = $("#emailaddress").val();
            currObj.loginName = $("#loginName").val();

            var obj = {};
            obj.password = $("#password").val();
            obj.retypedPassword = $("#retypedPassword").val();
            obj.userProfile = currObj
            var sdata = JSON.stringify(obj);

            $.ajax({
                type: "POST",
                url: surl,
                dataType: 'json',
                async: false,
                data: sdata,
                contentType: "application/json; charset=utf-8",
                success: function (html) {
                    if ((html == null) || (html.jsonStatus == null)) {
                        console.log("unable to save provile");
                        return;
                    }
                    else if (html.jsonStatus && html.jsonStatus.status != "OK") {
                        // show backend error message
                        $("#password").parsley().addError(
                            'password',
                            { message: html.jsonStatus.errorMsg, assert: false, updateClass: true}
                        );
                        // remove error message when field updated
                        $("#password").one('change', function () {
                            $("#password").parsley().removeError('password', {updateClass: true});
                        })
                        return;
                    } else {
                        // redirect user when authenticated
                        window.location.href = "<s:url action='doShowWelcome' namespace='/session'/>";

                        return;
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert("unable to save system role");
                    console.log("unable to save user profile:" + textStatus);
                    return;
                }
            });
        }

    </script>
</content>

</html>
