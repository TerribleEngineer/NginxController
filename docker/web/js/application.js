var behindProxy = false;

$(function() {

	console.log(window.location);
	if (window.location.pathname.startsWith("/proxy-ui")) {
		behindProxy = true;
	}

	getRouteTable();

	$("#submitButton")
			.click(
					function(event) {
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

						var targetUrl;
						if (behindProxy) {
							targetUrl = "../proxy-api/config";
						} else {
							targetUrl = "../nginx/config";
						}

						$.ajax({
							type : "POST",
							url : targetUrl,
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
	var targetUrl;
	if (behindProxy) {
		targetUrl = "../proxy-api/config/" + appname + "/" + encodeURI(backend);
	} else {
		targetUrl = "../nginx/config/" + appname + "/" + encodeURI(backend);
	}

	$.ajax({
		type : "DELETE",
		url : targetUrl,
		success : function(data) {
			getRouteTable();
		},
		error : function(data) {
			alert("error encountered while deleting route");
		}
	})
}

function createBackendUrl(location, backendMap) {
	var url = location.scheme + "://" + backendMap.host;
	if (backendMap.port != 80) {
		url += ":";
		url += backendMap.port;
	}
	url += location.proxyPath;

	console.log(url);

	return url;
}

function getRouteTable() {
	var targetUrl;
	if (behindProxy) {
		targetUrl = "../proxy-api/config/routes";
	} else {
		targetUrl = "../nginx/config/routes"
	}

	$.ajax({
		type : "GET",
		url : targetUrl,
		dataType : "json",
		success : function(data) {

			console.log(data);

			var port = data.proxyPort;
			$("#routeContents").empty();

			console.log(data.routes.length)

			for (var i = 0; i < data.routes.length; i++) {
				console.log("cycle " + i + " for "
						+ data.routes[i].applicationName);
				var d = data.routes[i];

				var trow = $("<tr>");

				var appData = $("<td height='100%'>");
				var appContent = $("<span>" + d.applicationName + "</span>");
				appData.append(appContent);
				appData.appendTo(trow);

				console.log("adding app data...");

				var locationData = $("<td height='100%'>");

				if (port != 80) {
					var locationContent = $("<a href='"
							+ window.location.protocol + "//"
							+ window.location.hostname + ":" + port
							+ d.location + "' class='colText'><span>"
							+ d.location + "</span></a>");
					locationData.append(locationContent);
					locationData.appendTo(trow);
				} else {
					var locationContent = $("<a href='"
							+ window.location.protocol + "//"
							+ window.location.hostname + d.location
							+ "' class='colText'><span>" + d.location
							+ "</span></a>");
					locationData.append(locationContent);
					locationData.appendTo(trow);
				}

				console.log("adding location data...");

				var targetData = $("<td height='100%'>");
				var targetContent = $("<a href='" + d.backendUrl + "'><span>"
						+ d.backendUrl + "</span></a><br />");
				targetData.append(targetContent);
				targetData.appendTo(trow);

				console.log("adding target data...");

				var deleteContent = $("<td>");
				var deleteButton = $("<button appname='" + d.applicationName
						+ "' backend='" + d.backendUrl
						+ "' class='btn btn-default'>Delete Route</button>");
				deleteButton.click(function(event) {
					event.preventDefault();
					var attrs = event.target.attributes;
					deleteRoute(attrs["appname"].value,
							encodeURIComponent(attrs["backend"].value));
				})
				deleteContent.append(deleteButton);
				deleteContent.appendTo(trow)

				console.log("adding delete buttons...");

				$("#routeContents").append(trow);

			}
		},
		error : function(data) {
			alert("error retrieving route data")
		}
	})

}
