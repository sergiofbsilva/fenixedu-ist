<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="page-header">
    <h2>
        <spring:message code="authorize.personal.data.access.title" />
    </h2>
    <spring:url value="/authorize-personal-data-access/historic" var="baseUrl"/>
    <a href="${baseUrl}">Consultar histórico de autorizações</a>
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
                        <button class="btn btn-primary" type="submit">
                            <spring:message code="authorize.personal.data.access.acknowledge" />
                        </button>
                    </p>
                </form>
            </div>
        </div>
    </div>

</div>