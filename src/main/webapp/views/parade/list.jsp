<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
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

<%@page import="java.util.Collection" %>

<display:table name="parades" id="row" requestURI="${requestURI}"
	pagesize="${numResults}" class="displaytag">

	<security:authorize access="hasRole('BROTHERHOOD')">

		<display:column>
			<jstl:if test="${row.ffinal == false}">
				<a href="parade/brotherhood/edit.do?paradeId=${row.id}"> <spring:message
						code="parade.edit" />
				</a>
				<br>
				<a href="parade/brotherhood/addFloat.do?paradeId=${row.id}"> <spring:message
						code="parade.addFloat" />
				</a>
				<br>
				<a href="parade/brotherhood/removeFloat.do?paradeId=${row.id}">
					<spring:message code="parade.removeFloat" />
				</a> 
				<a href="path/brotherhood/display.do?paradeId=${row.id}">
					<spring:message code="path.display" />
				</a>
			</jstl:if>
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('CHAPTER')">

		<display:column>
			<a href="parade/edit.do?paradeId=${row.id}"> <spring:message
						code="parade.edit" />
			</a>
		</display:column>
	</security:authorize>


	<display:column titleKey="parade.hood">
		<a href="parade/listBrotherhood.do?paradeId=${row.id}"> <jstl:out
				value="${row.brotherhood.name}" />
		</a>
	</display:column>

	<display:column titleKey="parade.sponsorship">
		<jstl:if test="${paradeService.findSponsorshipByParadeId(row.id).size()!=0}">
		<img src="${paradeService.findSponsorshipByParadeId(row.id).get(0)}" height="100px" width="100px" />
		</jstl:if>
	</display:column>
	
		<jstl:if test="${row.status=='SUBMITTED'}">
		<display:column property="status" titleKey="parade.status"
			style="background-color:Yellow" sortable="true" />
	</jstl:if>

	<jstl:if test="${row.status=='ACCEPTED' }">
		<display:column property="status" titleKey="parade.status"
			style="background-color:Blue" sortable="true " />
	</jstl:if>

	<jstl:if test="${row.status=='REJECTED'}">
		<display:column property="status" titleKey="parade.status"
			style="background-color:Red" sortable="true" />
	</jstl:if>
	

	<display:column property="ticker" titleKey="parade.ticker" />
	<security:authorize access="hasRole('BROTHERHOOD')">

		<display:column titleKey="parade.member">

			<a href="march/brotherhood/list.do?paradeId=${row.id}"> <spring:message
					code="parade.list" />
			</a>

		</display:column>
	</security:authorize>



	<display:column>

		<a href="parade/brotherhood/show.do?paradeId=${row.id}"> <spring:message
				code="parade.show" />
		</a>

	</display:column>



	<security:authorize access="hasRole('MEMBER')">

		<display:column>
			<jstl:if
				test="${marchService.findMatchByParadeidAndMemberid(row.id,memberId)==0}">
				<a href="march/member/create.do?paradeId=${row.id}"> <spring:message
						code="parade.createMarch" />
				</a>
			</jstl:if>
		</display:column>

	</security:authorize>

</display:table>

<br />
<security:authorize access="hasRole('BROTHERHOOD')">
	<jstl:if test="${hasArea != null}">
		<a href="parade/brotherhood/create.do"> <spring:message
				code="parade.create" />
		</a>
	</jstl:if>

	<jstl:if test="${hasArea == null}">
		<p>
			<spring:message code="parade.area.null" />
		</p>
	</jstl:if>
</security:authorize>




