<%--
 * action-1.jsp
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
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<display:table name="periodRecords" id="periodRecord"
	requestURI="periodRecord/brotherhood/list.do" pagesize="5"
	class="displaytag">

	<%--  Primero compruebo que es un brotherhood --%>
<security:authorize access="hasRole('BROTHERHOOD')">

<security:authentication property="principal.username" var="username" />

	<jstl:if test='${history.brotherhood.userAccount.username == username}'> 
		<%--  La columna que va a la vista edit de las periodRecord --%>
		<display:column>
			<a
				href="periodRecord/brotherhood/edit.do?periodRecordId=${periodRecord.id}"><spring:message
					code="periodRecord.edit"></spring:message></a>
		</display:column>
	</jstl:if>

</security:authorize>

		<%--  La columna que va a la vista display de las periodRecord --%>
		<display:column>
			<a
				href="periodRecord/brotherhood/display.do?periodRecordId=${periodRecord.id}"><spring:message
					code="periodRecord.display"></spring:message></a>
		</display:column>
	
	
	
	<acme:column code="periodRecord.title" value="${periodRecord.title}"></acme:column>
	<acme:column code="periodRecord.text" value="${periodRecord.text}"></acme:column>
	<spring:message code="periodRecord.startYear" var="startYear"></spring:message>
				<display:column property="startYear.time"
					title="${startYear}" format="{0,date,YYYY}" />
	<spring:message code="periodRecord.endYear" var="endYear"></spring:message>
				<display:column property="endYear.time"
					title="${endYear}" format="{0,date,YYYY}" />
	<acme:column code="periodRecord.history" value="${periodRecord.history.title}"></acme:column>

	


</display:table>


<%--  Boton de creacion --%>
<security:authorize access="hasRole('BROTHERHOOD')">

	<input type="button" name="create"
		value="<spring:message code="periodRecord.create"></spring:message>"
		onclick="javascript:relativeRedir('periodRecord/brotherhood/create.do')" />
</security:authorize>

<%--  Boton de ATRAS --%>
<%-- <security:authorize access="hasRole('BROTHERHOOD')">

	<input type="button" name="back"
		value="<spring:message code="periodRecord.back"></spring:message>"
		onclick="javascript:relativeRedir('history/brotherhood/display.do')" />
</security:authorize> --%>


