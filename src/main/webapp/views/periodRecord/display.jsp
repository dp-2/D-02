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

<acme:out code="periodRecord.title" value="${periodRecord.title}"/>
<acme:out code="periodRecord.text" value="${periodRecord.text}"/>

<b><spring:message code="periodRecord.startYear"></spring:message>:</b> 
<jsp:useBean id="dateValue" class="java.util.Date"/>
<jsp:setProperty name="dateValue" property="time" value="${periodRecord.startYear.time}"/>
<fmt:formatDate value="${dateValue}" pattern="yyyy"/><br /><br />

<b><spring:message code="periodRecord.endYear"></spring:message>:</b> 
<jsp:useBean id="endValue" class="java.util.Date"/>
<jsp:setProperty name="endValue" property="time" value="${periodRecord.endYear.time}"/>
<fmt:formatDate value="${endValue}" pattern="yyyy"/><br /><br />

<fieldset><legend><spring:message code="periodRecord.photos" /></legend>

		<jstl:forEach items="${periodRecord.photos}" var="photo">
	  
	      <!-- <div class="inline-block"> -->

  		<img src="${photo} "  style="width:15%;height:15%; display: inline-block;">
  		   <!--  </div> -->

  	   

	</jstl:forEach>
	
	
</fieldset>
<security:authorize access="hasRole('BROTHERHOOD')">

<security:authentication property="principal.username" var="username" />

	<jstl:if test='${history.brotherhood.userAccount.username == username}'> 

<input type="button" name="edit" value="<spring:message code="periodRecord.edit"></spring:message>" onclick="javascript:relativeRedir('periodRecord/brotherhood/edit.do?periodRecordId=${periodRecord.id}')"/>
</jstl:if>
</security:authorize>	
<input type="button" name="cancel" value="<spring:message code="periodRecord.cancel"></spring:message>" onclick="javascript:relativeRedir('periodRecord/brotherhood/list.do?historyId=${periodRecord.history.id}')" />	










