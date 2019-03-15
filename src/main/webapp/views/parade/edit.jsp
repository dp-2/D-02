<%--
 * action-2.jsp
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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="parade/edit.do" modelAttribute="parade">
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="brotherhood" />
	<form:hidden path="ticker" />

	<acme:textbox code="parade.title" path="title" readonly="${isRead}" />
	<acme:textbox code="parade.description" path="description"
		readonly="${isRead}" />
	<acme:textbox code="parade.moment" path="momentOrganised"
		readonly="${isRead}" placeholder="yyyy/mm/dd" />


	<security:authorize access="hasRole('CHAPTER')">
		<jstl:if test="${parade.status=='ACCEPTED'}">
			<form:label path="status">
				<spring:message code="parade.status"></spring:message>
			</form:label>
			<form:select id="status" path="status">
				<option value="SUBMITTED">SUBMITTED</option>
				<option value="ACCEPTED">ACCEPTED</option>
				<option value="REJECTED">REJECTED</option>

			</form:select>

		</jstl:if>
	</security:authorize>

	<jstl:if test="${isRead == false}">
		<acme:checkbox code="parade.final" path="ffinal" />

		<input type="submit" name="save"
			value="<spring:message code="parade.save" />" />

		<jstl:if test="${parade.id != 0}">
			<input type="submit" name="delete"
				value="<spring:message code="parade.delete" />" />
		</jstl:if>

		<acme:cancel url="parade/list.do" code="parade.cancel" />
	</jstl:if>

	<jstl:if test="${isRead == true}">

		<acme:cancel url="parade/list.do" code="parade.back" />

	</jstl:if>




</form:form>


