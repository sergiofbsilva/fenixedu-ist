<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="page-header">
    <h2>
        <spring:message code="authorize.personal.data.access.title" />
    </h2>
</div>

<div>

    <h3>
        ${title}
    </h3>

    <div class="infoop8">
        <p style="margin-bottom: 20px;">
            ${message}
        </p>

        <div class="row">
            <div class="col-lg-12 text-left">
                <form action="${requestScope['javax.servlet.forward.request_uri']}" method="post">
                    ${csrf.field()}
                    <p>
                        <span style="line-height: 20px; vertical-align: bottom; margin-right: 55px;">
                            <input type="radio" name="allowAccess" value="true" onclick="removeDisabled()">
                            <spring:message code="authorize.personal.data.access.yes"/>
                        </span>
                                <span>
                            <input type="radio" name="allowAccess" value="false" onclick="removeDisabled()">
                            <spring:message code="authorize.personal.data.access.no"/>
                        </span>
                    </p>
                    <button id="form-submit-button" class="btn btn-primary disabled" type="submit">
                        <spring:message code="authorize.personal.data.access.submit"/>
                    </button>
                </form>
            </div>
        </div>
    </div>

</div>

<script type="text/javascript">
    function removeDisabled() {
        $('#form-submit-button').removeClass('disabled');
    }
</script>