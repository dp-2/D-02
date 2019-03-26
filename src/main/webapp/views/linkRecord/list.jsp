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



<display:table name="linkRecords" id="linkRecord"
	requestURI="linkRecord/brotherhood/list.do" pagesize="5"
	class="displaytag">

	<%--  Primero compruebo que es un brotherhood --%>
	<security:authorize access="hasRole('BROTHERHOOD')">
<security:authentication property="principal.username" var="username" />

	<jstl:if test='${history.brotherhood.userAccount.username == username}'> 

		<%--  La columna que va a la vista edit de las linkRecord --%>
		<display:column>
			<a
				href="linkRecord/brotherhood/edit.do?linkRecordId=${linkRecord.id}"><spring:message
					code="linkRecord.edit"></spring:message></a>
		</display:column>
		
	</jstl:if>
	</security:authorize>


		<%--  La columna que va a la vista display de las linkRecord --%>
		<display:column>
			<a
				href="linkRecord/brotherhood/display.do?linkRecordId=${linkRecord.id}"><spring:message
					code="linkRecord.display"></spring:message></a>
		</display:column>
	
	
	
	<acme:column code="linkRecord.title" value="${linkRecord.title}"></acme:column>
	<acme:column code="linkRecord.text" value="${linkRecord.text}"></acme:column>
	
		<display:column>
			<a href="${linkRecord.linkBrotherhood}"><spring:message
					code="linkRecord.brotherhoodRelated"></spring:message></a>
	</display:column>
	
<%-- 	<display:column>
			<a href="brotherhood/brotherhood/display.do?brotherhoodId=${linkRecord.history.brotherhood.id}"><spring:message
					code="linkRecord.brotherhood"></spring:message></a>
	</display:column> --%>
	
	
	<acme:column code="linkRecord.history" value="${linkRecord.history.title}"></acme:column>

	


</display:table>


<%--  Boton de creacion --%>
<security:authorize access="hasRole('BROTHERHOOD')">

	<input type="button" name="create"
		value="<spring:message code="linkRecord.create"></spring:message>"
		onclick="javascript:relativeRedir('linkRecord/brotherhood/create.do')" />
</security:authorize>

<%--  Boton de ATRAS --%>
<acme:cancel url="history/display.do?brotherhoodId=${history.brotherhood.id}" code="linkRecord.back" />



