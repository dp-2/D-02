<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>



			<acme:out code="inceptionRecord.title" value="${inceptionRecord.title}"/>
			<acme:out code="inceptionRecord.text" value="${inceptionRecord.text}"/>


<fieldset><legend><spring:message code="inceptionRecord.photos" /></legend>

		<jstl:forEach items="${inceptionRecord.photos}" var="photo">

  		<img src="${photo} "  style="width:50%;height:50%; display: inline-block;">


	</jstl:forEach>
	<br>
	<br>
	
	<jstl:if test='${brotherhood.userAccount.username == username}'>

	<spring:message code="brotherhood.edit" var="edit"></spring:message>
	<input type="button" name="edit" value="${edit}"
		onclick="javascript:relativeRedir('inceptionRecord/edit.do?inceptionRecordId=${inceptionRecord.id}')" />
</jstl:if>
	
	
</fieldset>


