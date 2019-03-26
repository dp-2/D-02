<%--
 * action-2.jsp
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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<security:authentication property="principal.username" var="username" />
<jstl:if
		test='${inceptionRecord.history.brotherhood.userAccount.username == username || inceptionRecord.id == 0}'>
		

<security:authorize access="hasRole('BROTHERHOOD')">
	<div>

		<form:form action="inceptionRecord/brotherhood/edit.do" method="post"
			id="formCreate" name="formCreate" modelAttribute="inceptionRecord">

			<!-- Atributos hidden-->

			<form:hidden path="id" />
			<form:hidden path="version" />
			<form:hidden path="history" />
			


			<fieldset>
				<!-------------------Form ------------------------------------>
				<acme:labelForm code="inceptionRecord.title" path="title" />
				<acme:textarea code="inceptionRecord.text" path="text" />
				<acme:textarea code="periodRecord.photos" path="photos" readonly="false" />



			</fieldset>

			


			<!--  Los botones de crear y cancelar -->

			<input type="submit" name="save"
				value="<spring:message code="inceptionRecord.save"></spring:message>" />

			<acme:cancel url="history/display.do?brotherhoodId=${inceptionRecord.history.brotherhood.id}" code="inceptionRecord.cancel" />








		</form:form>

	</div>





</security:authorize>
</jstl:if>
<jstl:if
	test='${inceptionRecord.history.brotherhood.userAccount.username != username && inceptionRecord.id != 0}'>
	<h1>
		<b><spring:message code="history.permissions"></spring:message></b>
	</h1>
	
	<img src="http://lymediseaseuk.com/wp-content/uploads/2018/07/oops-300x300.png" alt="Cuestionario Picture"
			style="width: 10%; height: 10%;">

		<br />
		<br />
	
	<button type="button"
				onclick="javascript: relativeRedir('')">
				<spring:message code="inceptionRecord.cancel" />
			</button>
</jstl:if>