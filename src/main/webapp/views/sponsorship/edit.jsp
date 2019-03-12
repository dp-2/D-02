<%--
 * edit.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>


<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('SPONSOR')">
	<form:form action="${requestURI}" modelAttribute="sponsorship">
		<form:hidden path="id" />
		<form:hidden path="version" />
		<form:hidden path="sponsor" />

		<acme:textbox code="sponsorship.banner" path="banner" />
		<br />

		<acme:textbox code="sponsorship.target" path="target" />
		<br />

		<acme:checkbox code="sponsorship.active" path="Active" />
		<br>

		<acme:select items="${parades}" itemLabel="title"
			code="sponsorship.parades" path="parade" />

		<fieldset>
			<legend>
				<spring:message code="sponsorship.creditcard" />
			</legend>

			<acme:textbox code="creditCard.holderName"
				path="creditCard.holderName" />
			<br>

			<acme:select items="${makeName}" itemLabel="makeName"
				code="sponsorship.makeName" path="makeName" />
			<br>

			<acme:textbox code="creditCard.number" path="creditCard.number" />
			<br>

			<acme:textbox code="creditCard.expirationMonth"
				path="creditCard.expirationMonth" />
			<br>

			<acme:textbox code="creditCard.expirationYear"
				path="creditCard.expirationYear" />
			<br>

			<acme:textbox code="creditCard.CVVCode" path="creditCard.CVVCode" />
			<br>

		</fieldset>



		<jstl:if test="${isRead == false}">
			<acme:submit name="save" code="sponsorship.save" />




			<security:authorize access="hasRole('SPONSOR')">
				<acme:cancel url="/sponsorship/sponsor/MyList.do"
					code="sponsorship.cancel" />

				<br />
			</security:authorize>


		</jstl:if>

		<acme:cancel url="/sponsorship/list.do" code="sponsorship.cancel" />

		<br />

	</form:form>
</security:authorize>