<%--
 * action-1.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<acme:out code="legalRecord.title" value="${legalRecord.title}"/>
<acme:out code="legalRecord.text" value="${legalRecord.text}"/>
<acme:out code="legalRecord.legalName" value="${legalRecord.legalName}"/>
<acme:out code="legalRecord.VATNumber" value="${legalRecord.VATNumber}"/>
<acme:out code="legalRecord.laws" value="${legalRecord.laws}"/>


<security:authorize access="hasRole('BROTHERHOOD')">

<security:authentication property="principal.username" var="username" />

	<jstl:if test='${history.brotherhood.userAccount.username == username}'> 

<input type="button" name="edit" value="<spring:message code="legalRecord.edit"></spring:message>" onclick="javascript:relativeRedir('legalRecord/brotherhood/edit.do?legalRecordId=${legalRecord.id}')"/>
</jstl:if>
</security:authorize>	
<acme:cancel url="legalRecord/brotherhood/list.do?historyId=${legalRecord.history.id}" code="legalRecord.back" />









