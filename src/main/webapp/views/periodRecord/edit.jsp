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

<security:authorize access="hasRole('HANDYWORKER')">
	<div>

		<form:form action="educationRecord/handyWorker/edit.do" method="post"
			id="formCreate" name="formCreate" modelAttribute="educationRecord">

			<!-- Atributos hidden-->

			<form:hidden path="id" />
			<form:hidden path="version" />
			<form:hidden path="curriculum" />


			<fieldset>
				<!-------------------Form ------------------------------------>
				<div>
					<form:label path="diplomaTitle">
						<spring:message code="educationRecord.diplomaTitle"></spring:message>
					</form:label>
					<form:input path="diplomaTitle" id="diplomaTitle"
						name="diplomaTitle" />
					<form:errors cssClass="error" path="diplomaTitle" />
					<br />
				</div>

				<div>
					<form:label path="start">
						<spring:message code="educationRecord.start"></spring:message>
					</form:label>
					<form:input path="start" id="start" name="start" />
					<form:errors cssClass="error" path="start" />
					<br />
				</div>

				<div>
					<form:label path="end">
						<spring:message code="educationRecord.end"></spring:message>
					</form:label>
					<form:input path="end" id="end" name="end" />
					<form:errors cssClass="error" path="end" />
					<br />
				</div>

				<div>
					<form:label path="institution">
						<spring:message code="educationRecord.institution"></spring:message>
					</form:label>
					<form:input path="institution" id="institution" name="institution" />
					<form:errors cssClass="error" path="institution" />
					<br />
				</div>

				<div>
					<form:label path="attachment">
						<spring:message code="educationRecord.attachment"></spring:message>
					</form:label>
					<form:input path="attachment" id="attachment" name="attachment" />
					<form:errors cssClass="error" path="attachment" />
					<br />
				</div>



				<div>
					<form:label path="comments">
						<spring:message code="educationRecord.comments"></spring:message>
					</form:label>
					<form:input path="comments" id="comments" name="comments" />
					<form:errors cssClass="error" path="comments" />
					<br />
				</div>






			</fieldset>


			<!--  Los botones de crear y cancelar -->

			<input type="submit" name="save"
				value="<spring:message code="educationRecord.save"></spring:message>" />

			<button type="button"
				onclick="javascript: relativeRedir('educationRecord/handyWorker/list.do')">
				<spring:message code="educationRecord.cancel" />
			</button>

			<jstl:if test="${educationRecord.id != 0}">
				<input type="submit" name="delete"
					value="<spring:message code="educationRecord.delete" />"
					onclick="return confirm('<spring:message code="educationRecord.confirm.delete" />')" />&nbsp;
	</jstl:if>






		</form:form>

	</div>





</security:authorize>