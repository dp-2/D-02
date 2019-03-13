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

<acme:out code="periodRecord.title" value="${periodRecord.title}"/>
<acme:out code="periodRecord.text" value="${periodRecord.text}"/>
<acme:out code="periodRecord.startYear" value="${periodRecord.startYear}"/>
<acme:out code="periodRecord.endYear" value="${periodRecord.endYear}"/>
<acme:out code="periodRecord.photos" value="${periodRecord.photos}"/>


<input type="button" name="edit" value="<spring:message code="periodRecord.edit"></spring:message>" onclick="javascript:relativeRedir('periodRecord/handyWorker/edit.do?periodRecordId=${periodRecord.id}')"/>	
<input type="button" name="cancel" value="<spring:message code="periodRecord.cancel"></spring:message>" onclick="javascript:relativeRedir('periodRecord/handyWorker/list.do')" />	










