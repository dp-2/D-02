<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

	<h2><spring:message code="path.pathof" /> <jstl:out value="${path.parade.brotherhood.title}" /></h2>
	<br>
	
	<fieldset><legend><spring:message code="path.segments" /></legend>
		<jstl:forEach items="${segments}" var="segment">
			<fieldset><legend><spring:message code="segment" /> ${segment.numberOrder}</legend>
				<fieldset><legend><spring:message code="segment.origin" /></legend>
					<p><spring:message code="segment.latitude" /> : <jstl:out value="${segment.latitudeOrigin}" /></p>
					<p><spring:message code="segment.longitude" /> : <jstl:out value="${segment.longitudeOrigin}" /></p>
					<p><spring:message code="segment.time" /> : <jstl:out value="${segment.timeOrigin}" /></p>
				</fieldset>
				<fieldset><legend><spring:message code="segment.destination" /></legend>
					<p><spring:message code="segment.latitude" /> : <jstl:out value="${segment.latitudeDestination}" /></p>
					<p><spring:message code="segment.longitude" /> : <jstl:out value="${segment.longitudeDestination}" /></p>
					<p><spring:message code="segment.time" /> : <jstl:out value="${segment.timeDestination}" /></p>
				</fieldset>
				<jstl:if test="${isPrincipalAuthorizedEdit}">
					<acme:link url="segment/brotherhood/edit.do?segmentId=${segment.id}" code="segment.edit" />
				</jstl:if>
			</fieldset>
		</jstl:forEach>
		<jstl:if test="${isPrincipalAuthorizedEdit}">
			<acme:link url="segment/brotherhood/create.do?pathId=${path.id}" code="segment.create" />
			<acme:link url="path/brotherhood/delete.do?pathId=${path.id}" code="path.delete" />
		</jstl:if>
	</fieldset>
