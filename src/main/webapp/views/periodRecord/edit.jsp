<%--
 * create.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
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

<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
	$(function() {
		$("#datepicker1").datepicker({  dateFormat: "yy/mm/dd", yearRange: "1700:2018",    changeMonth: true,
		      changeYear: true});
		$("#datepicker2").datepicker({ dateFormat: "yy/mm/dd",yearRange: "1700:2500",    changeMonth: true,
		      changeYear: true});
	});
</script>


<security:authentication property="principal.username" var="username" />
<jstl:if
		test='${periodRecord.history.brotherhood.userAccount.username == username || periodRecord.id == 0}'>
<security:authorize access="hasRole('BROTHERHOOD')">
	<div>

		<form:form action="periodRecord/brotherhood/edit.do" method="post"
			id="formCreate" name="formCreate" modelAttribute="periodRecord">

			<!-- Atributos hidden-->

			<form:hidden path="id" />
			<form:hidden path="version" />
			<form:hidden path="history" />
			


			<fieldset>
				<!-------------------Form ------------------------------------>
				<acme:labelForm code="periodRecord.title" path="title" />
				<acme:labelForm code="periodRecord.text" path="text" />
				
				<form:label path="startYear"><spring:message code="periodRecord.startYear"></spring:message></form:label>
				<form:input path="startYear" id="datepicker1" />
				<form:errors cssClass="error" path="startYear" />
				<br>
				
				<form:label path="endYear"><spring:message code="periodRecord.endYear"></spring:message></form:label>
				<form:input path="endYear" id="datepicker2" />
				<form:errors cssClass="error" path="endYear" />
				<br>
				
				<acme:textarea code="periodRecord.photos" path="photos" readonly="false" />
 				
				

			</fieldset>


			<!--  Los botones de crear y cancelar -->

			<input type="submit" name="save"
				value="<spring:message code="periodRecord.save"></spring:message>" />

			<%-- <button type="button"
				onclick="javascript: relativeRedir('periodRecord/brotherhood/list.do')">
				<spring:message code="periodRecord.cancel" />
			</button> --%>

			<jstl:if test="${periodRecord.id != 0}">
				<input type="submit" name="delete"
					value="<spring:message code="periodRecord.delete" />"
					onclick="return confirm('<spring:message code="periodRecord.confirm.delete" />')" />&nbsp;
	</jstl:if>






		</form:form>

	</div>





</security:authorize>

</jstl:if>
<jstl:if
	test='${periodRecord.history.brotherhood.userAccount.username != username}'>
	<h1 style="color: red;">
		<b><spring:message code="periodRecord.permissions"></spring:message></b>
	</h1>
	
	<img src="https://cdn.shopify.com/s/files/1/1061/1924/products/Very_Angry_Emoji_7f7bb8df-d9dc-4cda-b79f-5453e764d4ea_large.png?v=1480481058" alt="Cuestionario Picture"
			style="width: 10%; height: 10%;">

		<br />
		<br />
	
	<button type="button"
				onclick="javascript: relativeRedir('periodRecord/brotherhood/list.do')">
				<spring:message code="periodRecord.cancel" />
			</button>
</jstl:if>