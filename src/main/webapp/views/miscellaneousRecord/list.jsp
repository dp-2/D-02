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



<display:table name="miscellaneousRecords" id="miscellaneousRecord"
	requestURI="miscellaneousRecord/brotherhood/list.do" pagesize="5"
	class="displaytag">

	<%--  Primero compruebo que es un brotherhood --%>
	<security:authorize access="hasRole('BROTHERHOOD')">
<security:authentication property="principal.username" var="username" />

	<jstl:if test='${history.brotherhood.userAccount.username == username}'> 


		<%--  La columna que va a la vista edit de las miscellaneousRecord --%>
		<display:column>
			<a
				href="miscellaneousRecord/brotherhood/edit.do?miscellaneousRecordId=${miscellaneousRecord.id}"><spring:message
					code="miscellaneousRecord.edit"></spring:message></a>
		</display:column>
		</jstl:if>
		
</security:authorize>

		<%--  La columna que va a la vista display de las miscellaneousRecord --%>
		<display:column>
			<a
				href="miscellaneousRecord/brotherhood/display.do?miscellaneousRecordId=${miscellaneousRecord.id}"><spring:message
					code="miscellaneousRecord.display"></spring:message></a>
		</display:column>
	
	
	
	<acme:column code="miscellaneousRecord.title" value="${miscellaneousRecord.title}"></acme:column>
	<acme:column code="miscellaneousRecord.text" value="${miscellaneousRecord.text}"></acme:column>
	<acme:column code="miscellaneousRecord.history" value="${miscellaneousRecord.history.title}"></acme:column>

	


</display:table>


<%--  Boton de creacion --%>
<security:authorize access="hasRole('BROTHERHOOD')">

	<input type="button" name="create"
		value="<spring:message code="miscellaneousRecord.create"></spring:message>"
		onclick="javascript:relativeRedir('miscellaneousRecord/brotherhood/create.do')" />
</security:authorize>

<%-- <%--  Boton de ATRAS
<security:authorize access="hasRole('BROTHERHOOD')">

	<input type="button" name="back"
		value="<spring:message code="miscellaneousRecord.back"></spring:message>"
		onclick="javascript:relativeRedir('history/brotherhood/display.do')" />
</security:authorize> --%>


