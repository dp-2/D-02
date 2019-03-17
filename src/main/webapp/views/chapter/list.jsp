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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<display:table name="chapters" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<acme:column value="${row.title}" code="chapter.title" sortable="true" />

	<acme:column value="${row.email}" code="chapter.email" />

	<display:column titleKey="chapter.photo">
		<img src="${row.photo}" height="100px" width="100px" />
	</display:column>

	<acme:column value="${row.phone}" code="chapter.phone" />

	<acme:column value="${row.address}" code="chapter.address" />

	<display:column titleKey="chapter.area">
		<a href="area/chapterList.do?chapterId=${row.id}"><spring:message
				code="chapter.myarea" /> </a>
	</display:column>
	
	<display:column titleKey="chapter.brotherhood">
		<a href="brotherhood/chapterList.do?chapterId=${row.id}"><spring:message
				code="chapter.mybrotherhood" /> </a>
	</display:column>
	
	<display:column titleKey="chapter.parade">
		<a href="parade/chapterList.do?chapterId=${row.id}"><spring:message
				code="chapter.myparade" /> </a>
	</display:column>

</display:table>