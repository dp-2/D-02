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
	test='${linkRecord.history.brotherhood.userAccount.username == username || linkRecord.id == 0}'>
	<security:authorize access="hasRole('BROTHERHOOD')">
		<div>

			<form:form action="linkRecord/brotherhood/edit.do" method="post"
				id="formCreate" name="formCreate" modelAttribute="linkRecord">

				<!-- Atributos hidden-->

				<form:hidden path="id" />
				<form:hidden path="version" />
				<form:hidden path="history" />



				<fieldset>
					<!-------------------Form ------------------------------------>
					<acme:labelForm code="linkRecord.title" path="title" />
					<acme:labelForm code="linkRecord.text" path="text" />
					<acme:labelForm code="linkRecord.linkBrotherhood" path="linkBrotherhood" />
					
					




			</fieldset>


			<!--  Los botones de crear y cancelar -->

			<input type="submit" name="save"
				value="<spring:message code="linkRecord.save"></spring:message>" />

			<%-- <button type="button"
				onclick="javascript: relativeRedir('linkRecord/brotherhood/list.do')">
				<spring:message code="linkRecord.cancel" />
			</button> --%>

			<jstl:if test="${linkRecord.id != 0}">
				<input type="submit" name="delete"
					value="<spring:message code="linkRecord.delete" />"
					onclick="return confirm('<spring:message code="linkRecord.confirm.delete" />')" />&nbsp;
	</jstl:if>





			</form:form>
			<acme:cancel url="linkRecord/brotherhood/list.do?historyId=${linkRecord.history.id}" code="linkRecord.cancel" />


		</div>





	</security:authorize>

</jstl:if>
<jstl:if
	test='${linkRecord.history.brotherhood.userAccount.username != username}'>
	<h1>
		<b><spring:message code="history.permissions"></spring:message></b>
	</h1>

	<img
		src="http://lymediseaseuk.com/wp-content/uploads/2018/07/oops-300x300.png"
		alt="Cuestionario Picture" style="width: 10%; height: 10%;">

	<br />
	<br />

<acme:cancel url="" code="linkRecord.back" />

</jstl:if>