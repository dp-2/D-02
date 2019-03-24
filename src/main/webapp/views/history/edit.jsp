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



<security:authentication property="principal.username" var="username" />
<jstl:if
		test='${history.brotherhood.userAccount.username == username || history.id == 0}'>
<security:authorize access="hasRole('BROTHERHOOD')">
	<div>

		<form:form action="history/brotherhood/edit.do" method="post"
			id="formCreate" name="formCreate" modelAttribute="history">

			<!-- Atributos hidden-->

			<form:hidden path="id" />
			<form:hidden path="version" />
			<form:hidden path="brotherhood" />
			


			<fieldset>
				<!-------------------Form ------------------------------------>
				<acme:labelForm code="history.title" path="title" />
				
				
					

			</fieldset>


			<!--  Los botones de crear y cancelar -->

			<input type="submit" name="save"
				value="<spring:message code="history.save"></spring:message>" />

			<button type="button"
				onclick="javascript: relativeRedir('history/brotherhood/myDisplay.do')">
				<spring:message code="history.cancel" />
			</button>

<%-- 			<jstl:if test="${history.id != 0}">
				<input type="submit" name="delete"
					value="<spring:message code="history.delete" />"
					onclick="return confirm('<spring:message code="history.confirm.delete" />')" />&nbsp;
	</jstl:if> --%>






		</form:form>

	</div>





</security:authorize>

</jstl:if>
<jstl:if
	test='${history.brotherhood.userAccount.username != username}'>
	<h1 style="color: red;">
		<b><spring:message code="history.permissions"></spring:message></b>
	</h1>
	
	<img src="https://cdn.shopify.com/s/files/1/1061/1924/products/Very_Angry_Emoji_7f7bb8df-d9dc-4cda-b79f-5453e764d4ea_large.png?v=1480481058" alt="Cuestionario Picture"
			style="width: 10%; height: 10%;">

		<br />
		<br />
	
	<button type="button"
				onclick="javascript: relativeRedir('history/brotherhood/myDisplay.do')">
				<spring:message code="history.cancel" />
			</button>
</jstl:if>