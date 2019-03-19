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



<display:table name="legalRecords" id="legalRecord"
	requestURI="legalRecord/brotherhood/list.do" pagesize="5"
	class="displaytag">

	<%--  Primero compruebo que es un brotherhood --%>
	<security:authorize access="hasRole('BROTHERHOOD')">
		<security:authentication property="principal.username" var="username" />

		<jstl:if
			test='${history.brotherhood.userAccount.username == username}'>


			<%--  La columna que va a la vista edit de las legalRecord --%>
			<display:column>
				<a
					href="legalRecord/brotherhood/edit.do?legalRecordId=${legalRecord.id}"><spring:message
						code="legalRecord.edit"></spring:message></a>
			</display:column>
		</jstl:if>
	</security:authorize>


	<%--  La columna que va a la vista display de las legalRecord --%>
	<display:column>
		<a
			href="legalRecord/brotherhood/display.do?legalRecordId=${legalRecord.id}"><spring:message
				code="legalRecord.display"></spring:message></a>
	</display:column>



	<acme:column code="legalRecord.title" value="${legalRecord.title}"></acme:column>
	<acme:column code="legalRecord.text" value="${legalRecord.text}"></acme:column>
	<acme:column code="legalRecord.legalName"
		value="${legalRecord.legalName}"></acme:column>
	<acme:column code="legalRecord.VATNumber"
		value="${legalRecord.VATNumber}"></acme:column>
	<acme:column code="legalRecord.laws" value="${legalRecord.laws}"></acme:column>
	<acme:column code="legalRecord.history"
		value="${legalRecord.history.title}"></acme:column>




</display:table>


<%--  Boton de creacion --%>
<security:authorize access="hasRole('BROTHERHOOD')">

	<input type="button" name="create"
		value="<spring:message code="legalRecord.create"></spring:message>"
		onclick="javascript:relativeRedir('legalRecord/brotherhood/create.do')" />
</security:authorize>




