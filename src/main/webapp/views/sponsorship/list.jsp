<%--
 * list.jsp
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('SPONSOR')">
	<display:table name="sponsorships" id="row" requestURI="${requestURI}"
		pagesize="5" class="displaytag">


		<display:column>
			<jstl:if test="${sponsorId==row.sponsor.id}">
				<a href="sponsorship/sponsor/edit.do?sponsorshipId=${row.id}"> <spring:message
						code="sponsorship.edit" />
				</a>
			</jstl:if>
		</display:column>

		<display:column titleKey="sponsorship.banner">
			<img src="${row.banner}" height="100px" width="100px" />
		</display:column>

		<display:column titleKey="sponsorship.target">
			<a href="${row.target}">${row.target}</a>
		</display:column>


		<display:column property="sponsor.name" titleKey="sponsorship.sponsor" />
		
		<display:column>
			<jstl:if test="${sponsorId==row.sponsor.id}">
				<a href="sponsorship/sponsor/deActive.do?sponsorshipId=${row.id}"> <spring:message
						code="sponsorship.deActive" />
				</a>
			</jstl:if>
		</display:column>

	</display:table>

	<a href="sponsorship/sponsor/create.do"> <spring:message
			code="sponsorship.create" />
	</a>
</security:authorize>
