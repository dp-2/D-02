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
		test='${legalRecord.history.brotherhood.userAccount.username == username || legalRecord.id == 0}'>
<security:authorize access="hasRole('BROTHERHOOD')">
	<div>

		<form:form action="legalRecord/brotherhood/edit.do" method="post"
			id="formCreate" name="formCreate" modelAttribute="legalRecord">

			<!-- Atributos hidden-->

			<form:hidden path="id" />
			<form:hidden path="version" />
			<form:hidden path="history" />
			


			<fieldset>
				<!-------------------Form ------------------------------------>
				<acme:labelForm code="legalRecord.title" path="title" />
				<acme:labelForm code="legalRecord.text" path="text" />
				<acme:labelForm code="legalRecord.legalName" path="legalName" />
				<acme:labelForm code="legalRecord.VATNumber" path="VATNumber" />
				<acme:labelForm code="legalRecord.laws" path="laws" />
				
					

			</fieldset>


			<!--  Los botones de crear y cancelar -->

			<input type="submit" name="save"
				value="<spring:message code="legalRecord.save"></spring:message>" />

			<button type="button"
				onclick="javascript: relativeRedir('legalRecord/brotherhood/list.do')">
				<spring:message code="legalRecord.cancel" />
			</button>

			<jstl:if test="${legalRecord.id != 0}">
				<input type="submit" name="delete"
					value="<spring:message code="legalRecord.delete" />"
					onclick="return confirm('<spring:message code="legalRecord.confirm.delete" />')" />&nbsp;
	</jstl:if>






		</form:form>

	</div>





</security:authorize>

</jstl:if>
<jstl:if
	test='${legalRecord.history.brotherhood.userAccount.username != username}'>
	<h1 style="color: red;">
		<b><spring:message code="legalRecord.permissions"></spring:message></b>
	</h1>
	
	<img src="https://cdn.shopify.com/s/files/1/1061/1924/products/Very_Angry_Emoji_7f7bb8df-d9dc-4cda-b79f-5453e764d4ea_large.png?v=1480481058" alt="Cuestionario Picture"
			style="width: 10%; height: 10%;">

		<br />
		<br />
	
	<button type="button"
				onclick="javascript: relativeRedir('legalRecord/brotherhood/list.do')">
				<spring:message code="legalRecord.cancel" />
			</button>
</jstl:if>