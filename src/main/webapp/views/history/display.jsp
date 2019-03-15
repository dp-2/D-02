<%--
 * action-1.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
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

<acme:out code="history.title" value="${history.title}" />



<security:authorize access="hasRole('BROTHERHOOD')">

	<security:authentication property="principal.username" var="username" />

	<jstl:if test='${history.brotherhood.userAccount.username == username}'>


		<fieldset>
			<legend>
				<b><spring:message code="history.inceptionRecord"></spring:message></b>
			</legend>
			<acme:out code="history.inceptionRecord.title"
				value="${inceptionRecord.title}" />


			<spring:message code="history.display" var="display"></spring:message>
			<input type="button" name="display" value="${display}"
				onclick="javascript:relativeRedir('inceptionRecord/brotherhood/display.do?inceptionRecordId=${inceptionRecord.id}')" />
			<jstl:if test='${history.brotherhood.userAccount.username == username}'>
			
				<spring:message code="history.edit" var="edit"></spring:message>
				<input type="button" name="edit" value="${edit}"
					onclick="javascript:relativeRedir('inceptionRecord/brotherhood/edit.do?inceptionRecordId=${inceptionRecord.id}')" />
			</jstl:if>


		</fieldset>



			<fieldset>
				<legend>
					<b><spring:message code="history.periodRecords"></spring:message></b>
				</legend>
				<display:table name="periodRecords" id="periodRecord"
					pagesize="5" class="displaytag">

					<acme:column code="periodRecord.title" value="${periodRecord.title}"></acme:column>
					<acme:column code="periodRecord.text" value="${periodRecord.text}"></acme:column>
					<spring:message code="periodRecord.startYear" var="startYear"></spring:message>
								<display:column property="startYear.time"
									title="${startYear}" format="{0,date,YYYY}" />
					<spring:message code="periodRecord.endYear" var="endYear"></spring:message>
								<display:column property="endYear.time"
									title="${endYear}" format="{0,date,YYYY}" />
				</display:table>

				<spring:message code="history.seePeriodRecords"
					var="seePeriodRecords"></spring:message>
				<input type="button" name="seePeriodRecords"
					value="${seePeriodRecords}"
					onclick="javascript:relativeRedir('periodRecord/brotherhood/list.do')" />

			</fieldset>

			<%-- <fieldset>
				<legend>
					<b><spring:message code="history.professionalRecords"></spring:message></b>
				</legend>
				<display:table name="professionalRecords" id="professionalRecord"
					pagesize="5" class="displaytag">

					<spring:message code="history.professionalRecord.company"
						var="company"></spring:message>
					<display:column property="company" title="${company}"
						sortable="true" />

					<spring:message code="history.professionalRecord.role" var="role"></spring:message>
					<display:column property="role" title="${role}" sortable="true" />

				</display:table>

				<spring:message code="history.seeProfessionalRecords"
					var="seeProfessionalRecords"></spring:message>
				<input type="button" name="seeProfessionalRecords"
					value="${seeProfessionalRecords}"
					onclick="javascript:relativeRedir('professionalRecord/brotherhood/list.do')" />



			</fieldset>



			<fieldset>
				<legend>
					<b><spring:message code="history.endorserRecords"></spring:message></b>
				</legend>
				<display:table name="endorserRecords" id="endorserRecord"
					pagesize="5" class="displaytag">

					<spring:message code="history.endorserRecord.fullName"
						var="fullName"></spring:message>
					<display:column property="fullName" title="${fullName}"
						sortable="true" />

					<spring:message code="history.endorserRecord.linkedinProfile"
						var="linkedinProfile"></spring:message>
					<display:column property="linkedinProfile"
						title="${linkedinProfile}" sortable="true" />

				</display:table>

				<spring:message code="history.seeEndorserRecords"
					var="seeEndorserRecords"></spring:message>
				<input type="button" name="seeEndorserRecords"
					value="${seeEndorserRecords}"
					onclick="javascript:relativeRedir('endorserRecord/brotherhood/list.do')" />



			</fieldset>

			<fieldset>
				<legend>
					<b><spring:message code="history.miscellaneousRecords"></spring:message></b>
				</legend>
				<display:table name="miscellaneousRecords" id="miscellaneousRecord"
					pagesize="5" class="displaytag">

					<spring:message code="history.miscellaneousRecord.title"
						var="title"></spring:message>
					<display:column property="title" title="${title}" sortable="true" />



				</display:table>

				<spring:message code="history.seeMiscellaneousRecords"
					var="seeMiscellaneousRecords"></spring:message>
				<input type="button" name="seeMiscellaneousRecords"
					value="${seeMiscellaneousRecords}"
					onclick="javascript:relativeRedir('miscellaneousRecord/brotherhood/list.do')" />



			</fieldset>




 --%>


		

	</jstl:if>

</security:authorize>
