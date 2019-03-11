<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<jstl:if test="${isPrincipalAuthorizedEdit}">
	
	<form:form action="parade/brotherhood/removeFloat.do" modelAttribute="paradeFloatForm">
		
		<form:hidden path="parade" /> 
			
		<acme:select code="float" itemLabel="title" items="${floatsForRemove}" path="dFloat" />
			
		<acme:submit code="parade.removeFloat" name="remove" />
		<acme:cancel url="parade/list.do" code="parade.back" />
	
	</form:form>

</jstl:if>
