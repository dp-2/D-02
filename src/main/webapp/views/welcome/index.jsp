<%--
 * index.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Mensaje De Seguridad -->

<security:authorize access="isAuthenticated()">
	<security:authorize access="hasRole('ADMIN')">
		<fieldset>
			<legend>
				<spring:message code="master.security.message" />
			</legend>
			<br />
			<form:form>
				<button style="background-color: red;" name="activate">
					<spring:message code="master.security.activar" />
				</button>
				<br />
				<br />
				<button style="background-color: yellow;" name="desactivate">
					<spring:message code="master.security.desactivar" />
				</button>
			</form:form>
			<br />
		</fieldset>

	</security:authorize>
	<p style="color: red; font-size: 38px;" align="center" >${securityMessage}</p>
</security:authorize>

<h3>${nameSys}</h3>
<br />
<p>${welcomeMessage}</p>

<p>
	<spring:message code="welcome.greeting.current.time" />
	${moment}
</p>
