<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasRole('ADMIN')">


	<fieldset>
		<legend>
			<spring:message code="dashboard.membersPerBrotherhood">
			</spring:message>
		</legend>
		<acme:out code="dashboard.membersPerBrotherhood.max"
			value="${maxMembersPerBrotherhood}" />
		<acme:out code="dashboard.membersPerBrotherhood.min"
			value="${minMembersPerBrotherhood}" />
		<acme:out code="dashboard.membersPerBrotherhood.avg"
			value="${avgMembersPerBrotherhood}" />
		<acme:out code="dashboard.membersPerBrotherhood.stdev"
			value="${stdevMembersPerBrotherhood}" />
	</fieldset>

	<fieldset>
		<legend>
			<spring:message code="dashboard.largestSmallestBrotherhood">
			</spring:message>
		</legend>
		<acme:out code="dashboard.largestBrotherhood"
			value="${largestBrotherhood.title}" />
		<acme:out code="dashboard.numberOfMembers"
			value="${largestBrotherhoodNumMembers}" />
		<br /> <br />
		<acme:out code="dashboard.smallestBrotherhood"
			value="${smallestBrotherhood.title}" />
		<acme:out code="dashboard.numberOfMembers"
			value="${smallestBrotherhoodNumMembers}" />
	</fieldset>

	<fieldset>
		<legend>
			<spring:message code="dashboard.positionStats">
			</spring:message>
		</legend>

		<acme:dashboard-out code="dashboard.positionStats.positionCountTotal"
			value="${positionCountTotal}" id="positionCountTotal" />
		<acme:dashboard-out
			code="dashboard.positionStats.positionCountPresident"
			value="${positionCountPresident}" id="positionCountPresident" />
		<acme:dashboard-out
			code="dashboard.positionStats.positionCountVicepresident"
			value="${positionCountVicepresident}" id="positionCountVicepresident" />
		<acme:dashboard-out
			code="dashboard.positionStats.positionCountSecretary"
			value="${positionCountSecretary}" id="positionCountSecretary" />
		<acme:dashboard-out
			code="dashboard.positionStats.positionCountTreasurer"
			value="${positionCountTreasurer}" id="positionCountTreasurer" />
		<acme:dashboard-out
			code="dashboard.positionStats.positionCountHistorian"
			value="${positionCountHistorian}" id="positionCountHistorian" />
		<acme:dashboard-out
			code="dashboard.positionStats.positionCountOfficer"
			value="${positionCountOfficer}" id="positionCountOfficer" />
		<acme:dashboard-out code="dashboard.positionStats.positionCountVocal"
			value="${positionCountVocal}" id="positionCountVocal" />

		<html>
<head>
<script type="text/javascript"
	src="https://www.gstatic.com/charts/loader.js"></script>


</script>
</head>

</script>
<script type="text/javascript">
	google.charts.load("current", {
		packages : [
			'corechart'
		]
	});
	google.charts.setOnLoadCallback(drawChart);
	function drawChart() {

		var positionCountPresident = document.getElementById('positionCountPresident').innerHTML;
		var president = parseInt(positionCountPresident);

		var positionCountVicepresident = document.getElementById('positionCountVicepresident').innerHTML;
		var vicePresident = parseInt(positionCountVicepresident);

		var positionCountSecretary = document.getElementById('positionCountSecretary').innerHTML;
		var secretary = parseInt(positionCountSecretary);

		var positionCountTreasurer = document.getElementById('positionCountTreasurer').innerHTML;
		var treasurer = parseInt(positionCountTreasurer);

		var positionCountHistorian = document.getElementById('positionCountHistorian').innerHTML;
		var historian = parseInt(positionCountHistorian);

		var positionCountOfficer = document.getElementById('positionCountOfficer').innerHTML;
		var officer = parseInt(positionCountOfficer);

		var positionCountVocal = document.getElementById('positionCountVocal').innerHTML;
		var vocal = parseInt(positionCountVocal);

		var data = google.visualization.arrayToDataTable([
				[
						"Position", "Quantity", {
							role : "style"
						}
				], [
						"President", president, "	#8FBC8F"
				], [
						"Vice-President", vicePresident, "silver"
				], [
						"Secretary", secretary, "gold"
				], [
						"Treasurer", treasurer, "	#00FF00	"
				], [
						"Historian", historian, "	#FAEBD7"
				], [
						"Officer", officer, "#6495ED"
				], [
						"Vocal", vocal, "red"
				],

		]);

		var view = new google.visualization.DataView(data);
		view.setColumns([
				0, 1, {
					calc : "stringify",
					sourceColumn : 1,
					type : "string",
					role : "annotation"
				}, 2
		]);

		var options = {
			title : "Numbers of people per position",
			width : 600,
			height : 400,
			bar : {
				groupWidth : "95%"
			},
			legend : {
				position : "none"
			},
		};
		var chart = new google.visualization.ColumnChart(document.getElementById("columnchart_values"));
		chart.draw(view, options);
	}
</script>
<div id="columnchart_values" style="width: 900px; height: 450px;"></div>
<body>
	<div id="chart_div" style="width: 550px; height: 350px;"></div>
</body>
		</html>

	</fieldset>



	<fieldset>
		<legend>
			<spring:message code="dashboard.paradesIn30Days">
			</spring:message>
		</legend>

		<display:table name="paradesIn30Days" id="parade">
			<acme:column code="dashboard.parade.title" value="${ parade.title}"></acme:column>
			<acme:column code="dashboard.parade.momentOrganised"
				value="${ parade.momentOrganised}"></acme:column>
		</display:table>

	</fieldset>
	<fieldset>
		<legend>
			<spring:message code="dashboard.members10RequestAccepted">
			</spring:message>
		</legend>



		<display:table name="members10RequestAccepted" id="member">
			<acme:column code="dashboard.march.member" value="${member.name}"></acme:column>
		</display:table>
		<br />
		<acme:out code="dashboard.march.ratioRequestByStatusAPPROVED"
			value="${ratioRequestByStatusAPPROVED}" />

		<acme:out code="dashboard.march.ratioRequestByStatusPENDING"
			value="${ratioRequestByStatusPENDING}" />

		<acme:out code="dashboard.march.ratioRequestByStatusREJECTED"
			value="${ratioRequestByStatusREJECTED}" />

	</fieldset>

	<fieldset>
		<legend>
			<spring:message code="dashboard.brotherhoodsPerArea">
			</spring:message>
		</legend>
		<acme:out code="dashboard.countHermandadesPorArea"
			value="${countHermandadesPorArea}" />
		<acme:out code="dashboard.maxHermandadesPorArea.max"
			value="${maxHermandadesPorArea}" />
		<acme:out code="dashboard.minHermandadesPorArea.min"
			value="${minHermandadesPorArea}" />
		<acme:out code="dashboard.avgHermandadesPorArea.avg"
			value="${avgHermandadesPorArea}" />
		<acme:out code="dashboard.stddevHermandadesPorArea.stdev"
			value="${stddevHermandadesPorArea}" />
	</fieldset>

	<fieldset>
		<legend>
			<spring:message code="dashboard.resultsInFinder">
			</spring:message>
		</legend>

		<acme:out code="dashboard.maxResultsInFinder.max"
			value="${maxResultsInFinder}" />
		<acme:out code="dashboard.minResultsInFinder.min"
			value="${minResultsInFinder}" />
		<acme:out code="dashboard.avgResultsInFinder.avg"
			value="${avgResultsInFinder}" />
		<acme:out code="dashboard.stdResultsInFinder.stdev"
			value="${stdResultsInFinder}" />
		<acme:out code="dashboard.emptyVSNonEmptyFinderv"
			value="${emptyVSNonEmptyFinderv}" />
	</fieldset>

	<fieldset>
		<legend>
			<spring:message code="dashboard.recordsByHistory">
			</spring:message>
		</legend>
		<acme:out code="dashboard.recordsByHistory.avg" value="${avgC1}" />
		<acme:out code="dashboard.recordsByHistory.max" value="${maxC1}" />
		<acme:out code="dashboard.recordsByHistory.min" value="${minC1}" />
		<acme:out code="dashboard.recordsByHistory.stddev" value="${stddevC1}" />
	</fieldset>

	<fieldset>
		<legend>
			<spring:message code="dashboard.queryC2">
			</spring:message>
		</legend>
		<jstl:out value="${queryC2}" />
		<br />

	</fieldset>

	<fieldset>
		<legend>
			<spring:message code="dashboard.queryC3">
			</spring:message>
		</legend>
		<display:table name="queryC3" id="row">
			<acme:column code="dashboard.queryC3" value="${row.name}"></acme:column>
		</display:table>
		<br />

	</fieldset>
	<fieldset>
		<legend>
			<spring:message code="dashboard.queryB1">
			</spring:message>
		</legend>
		<acme:out code="dashboard.queryB1" value="${queryB1}" />

	</fieldset>

	<fieldset>
		<legend>
			<spring:message code="dashboard.queryB4">
			</spring:message>
		</legend>
		<acme:out code="dashboard.queryB4" value="${queryB4}" />

	</fieldset>

	<fieldset>
		<legend>
			<spring:message code="dashboard.queryB5">
			</spring:message>
		</legend>

		<acme:out code="dashboard.ratioParadeByStatusACCEPTED"
			value="${ratioParadeByStatusACCEPTED}" />

		<acme:out code="dashboard.ratioParadeByStatusSUBMITTED"
			value="${ratioParadeByStatusSUBMITTED}" />

		<acme:out code="dashboard.ratioParadeByStatusREJECTED"
			value="${ratioParadeByStatusREJECTED}" />

	</fieldset>

	<fieldset>
		<legend>
			<spring:message code="dashboard.queryA1">
			</spring:message>
		</legend>
		<acme:out code="dashboard.queryA1" value="${queryA1}" />

	</fieldset>

	<fieldset>
		<legend>
			<spring:message code="dashboard.queryA2">
			</spring:message>
		</legend>
		<acme:out code="dashboard.avgA2" value="${avgA2}" />
		<acme:out code="dashboard.maxA2" value="${maxA2}" />
		<acme:out code="dashboard.minA2" value="${minA2}" />
		<acme:out code="dashboard.stddevA2" value="${stddevA2}" />
	</fieldset>

	<fieldset>
		<legend>
			<spring:message code="dashboard.queryA3">
			</spring:message>
		</legend>
		<display:table name="queryA3" id="row" requestURI="${requestURI}"
			pagesize="5" class="displaytag">
			<acme:column code="dashboard.queryA3" value="${row}" />
		</display:table>
	</fieldset>
</security:authorize>