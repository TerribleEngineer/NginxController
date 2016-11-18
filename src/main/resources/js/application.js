$(function() {
	getRouteTable();

	$("#submitButton").click(function(event) {
		event.preventDefault();

		var appName = $("#applicationName").val();
		var location = $("#location").val();
		var target = $("#backendUrl").val();

		if (location == "" || target == "" || appName == "") {
			alert("location, backend url, and application name are required fields");
		}

		var data = {};
		data.applicationName = appName;
		data.location = location;
		data.backendUrl = target;

		$.ajax({
			type : "POST",
			url : "../proxy-api/config",
			data : JSON.stringify(data),
			contentType : "application/json",
			success : function(data) {
				getRouteTable();
				clearForm();
			},
			error : function(data) {
				alert("error posting new route")
			}
		})
	})

});

function clearForm() {
	$("#applicationName").val("");
	$("#location").val("");
	$("#backendUrl").val("");
}

function deleteRoute(appname, backend) {
	$.ajax({
		type : "DELETE",
		url : "../proxy-api/config/" + appname + "/" + encodeURI(backend),
		success : function(data) {
			getRouteTable();
		},
		error : function(data) {
			alert("error encountered while deleting route");
		}
	})
}

function getRouteTable() {

	$.ajax({
		type : "GET",
		url : "../proxy-api/config/routes",
		dataType : "json",
		success : function(data) {

			var port = data.proxyPort;
			$("#routeContents").empty();

			for (var i = 0; i < data.routes.length; i++) {
				var d = data.routes[i];
				if (d.proxySpecific == false) {
					var trow = $("<tr>");
					var appData = $("<td height='100%'>");
					var appContent = $("<span>" + d.applicationName + "</span>");
					appData.append(appContent);
					appData.appendTo(trow);

					var locationData = $("<td height='100%'>");

					if (port != 80) {
						var locationContent = $("<a href='" + window.location.protocol + "//" + window.location.hostname + ":" + port + d.location + "' class='colText'><span>" + d.location
								+ "</span></a>");
						locationData.append(locationContent);
						locationData.appendTo(trow);
					} else {
						var locationContent = $("<a href='" + window.location.protocol + "//" + window.location.hostname + d.location + "' class='colText'><span>" + d.location + "</span></a>");
						locationData.append(locationContent);
						locationData.appendTo(trow);
					}

					var targetData = $("<td height='100%'>");
					var targetContent = $("<a href='" + d.backendUrl + "'><span>" + d.backendUrl + "</span></a>");
					targetData.append(targetContent);
					targetData.appendTo(trow);

					var deleteContent = $("<td>");
					var deleteButton = $("<button appname='" + d.applicationName + "' backend='" + d.backendUrl + "' class='btn btn-default'>Delete Route</button>");
					deleteButton.click(function(event) {
						event.preventDefault();
						var attrs = event.target.attributes;
						deleteRoute(attrs["appname"].value, encodeURIComponent(attrs["backend"].value));
					})
					deleteContent.append(deleteButton);
					deleteContent.appendTo(trow)

					$("#routeContents").append(trow);

				}
			}
		},
		error : function(data) {
			alert("error retrieving route data")
		}
	})

}
