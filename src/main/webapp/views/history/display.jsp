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
<a
				href="history/brotherhood/edit.do?historyId=${history.id}"><spring:message
					code="history.editName"></spring:message></a>
					<br>
					
	</jstl:if>

</security:authorize>

	<br>
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
					onclick="javascript:relativeRedir('periodRecord/brotherhood/list.do?historyId=${history.id}')" />

	
				</fieldset>
				
				
	
			<fieldset>
				<legend>
					<b><spring:message code="history.legalRecords"></spring:message></b>
				</legend>
				<display:table name="legalRecords" id="legalRecord"
					pagesize="5" class="displaytag">

					<acme:column code="legalRecord.title" value="${legalRecord.title}"></acme:column>
					<acme:column code="legalRecord.text" value="${legalRecord.text}"></acme:column>
					<acme:column code="legalRecord.legalName" value="${legalRecord.legalName}"></acme:column>
					<acme:column code="legalRecord.VATNumber" value="${legalRecord.VATNumber}"></acme:column>
					
					
					
					
				</display:table>

				<spring:message code="history.seeLegalRecords"
					var="seeLegalRecords"></spring:message>
				<input type="button" name="seeLegalRecords"
					value="${seeLegalRecords}"
					onclick="javascript:relativeRedir('legalRecord/brotherhood/list.do?historyId=${history.id}')" />

			</fieldset>
			
			
			<fieldset>
				<legend>
					<b><spring:message code="history.linkRecords"></spring:message></b>
				</legend>
				<display:table name="linkRecords" id="linkRecord"
					pagesize="5" class="displaytag">

					<acme:column code="linkRecord.title" value="${linkRecord.title}"></acme:column>
					<acme:column code="linkRecord.text" value="${linkRecord.text}"></acme:column>
					<acme:column code="linkRecord.linkBrotherhood" value="${linkRecord.linkBrotherhood}"></acme:column>
					
					
					
					
				</display:table>

				<spring:message code="history.seeLinkRecords"
					var="seeLinkRecords"></spring:message>
				<input type="button" name="seeLinkRecords"
					value="${seeLinkRecords}"
					onclick="javascript:relativeRedir('linkRecord/brotherhood/list.do?historyId=${history.id}')" />

			</fieldset>
			
			
			
						
			<fieldset>
				<legend>
					<b><spring:message code="history.miscellaneousRecords"></spring:message></b>
				</legend>
				<display:table name="miscellaneousRecords" id="miscellaneousRecord"
					pagesize="5" class="displaytag">

					<acme:column code="miscellaneousRecord.title" value="${miscellaneousRecord.title}"></acme:column>
					<acme:column code="miscellaneousRecord.text" value="${miscellaneousRecord.text}"></acme:column>
					
					
					
					
				</display:table>

				<spring:message code="history.seeMiscellaneousRecords"
					var="seeMiscellaneousRecords"></spring:message>
				<input type="button" name="seeMiscellaneousRecords"
					value="${seeMiscellaneousRecords}"
					onclick="javascript:relativeRedir('miscellaneousRecord/brotherhood/list.do?historyId=${history.id}')" />

			</fieldset>



		


