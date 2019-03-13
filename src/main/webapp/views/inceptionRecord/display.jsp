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



<p><spring:message code="inceptionRecord.title" /> : <jstl:out value="${inceptionRecord.title}" /></p>
<p><spring:message code="inceptionRecord.text" /> : <jstl:out value="${inceptionRecord.text}" /></p>


<fieldset><legend><spring:message code="inceptionRecord.photos" /></legend>

		<jstl:forEach items="${inceptionRecord.photos}" var="photo">
	  <br />
	      <div class="inline-block">

  		<img src="${photo.url} "  style="width:800px;height:600px; display: inline-block;">
  		    </div>

  	   <br />

	</jstl:forEach>
	
	
</fieldset>




<jstl:if test="${isPrincipalAuthorizedEdit}">
	<a href="inceptionRecord/edit.do?inceptionRecordId=${inceptionRecord.id}"><spring:message code="inceptionRecord.edit" /></a>
</jstl:if>

