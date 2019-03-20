<%--
 * create.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<jstl:if test="${isPrincipalAuthorizedEdit}">
<security:authentication property="principal.username" var="username" />
<jstl:if test='${segment.path.parade.brotherhood.userAccount.username == username}'>
<form:form action="segment/brotherhood/edit.do" modelAttribute="segment">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<jstl:if test="${segment.id == 0}">
		<form:hidden path="path" />
	</jstl:if>
	
	<fieldset><legend><spring:message code="segment.origin" /></legend>
		<acme:textbox code="segment.latitude" path="latitudeOrigin" />
		<acme:textbox code="segment.longitude" path="longitudeOrigin" />
		<acme:textbox code="segment.time" path="timeOrigin" />
	</fieldset>
	
	<fieldset><legend><spring:message code="segment.destination" /></legend>
		<fieldset><legend><spring:message code="segment.onlylast" /></legend>	
			<acme:textbox code="segment.latitude" path="latitudeDestination" />
			<acme:textbox code="segment.longitude" path="longitudeDestination" />
		</fieldset>
		<acme:textbox code="segment.time" path="timeDestination" />
	</fieldset>
		
	<acme:submit name="save" code="segment.save" />
	<jstl:if test="${segment.id > 0}">
		<acme:submit name="delete" code="segment.delete" />
	</jstl:if>

</form:form>
</jstl:if>
</jstl:if>