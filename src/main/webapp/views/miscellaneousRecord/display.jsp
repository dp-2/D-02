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

<acme:out code="miscellaneousRecord.title" value="${miscellaneousRecord.title}"/>
<acme:out code="miscellaneousRecord.text" value="${miscellaneousRecord.text}"/>

<security:authorize access="hasRole('BROTHERHOOD')">

<security:authentication property="principal.username" var="username" />

	<jstl:if test='${history.brotherhood.userAccount.username == username}'> 

<input type="button" name="edit" value="<spring:message code="miscellaneousRecord.edit"></spring:message>" onclick="javascript:relativeRedir('miscellaneousRecord/brotherhood/edit.do?miscellaneousRecordId=${miscellaneousRecord.id}')"/>
</jstl:if>
</security:authorize>	
<acme:cancel url="miscellaneousRecord/brotherhood/list.do?historyId=${miscellaneousRecord.history.id}" code="miscellaneousRecord.back" />










