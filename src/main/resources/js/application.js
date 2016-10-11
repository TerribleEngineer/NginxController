$(function() {
	getRouteTable();

	$("#submitButton").click(function(event) {
		event.preventDefault();

		var location = $("#location").val();
		var target = $("#target").val();

		if (location == "" || target == "") {
			alert("both location and target are required fields");
		}

		var data = {};
		data.location = location;
		data.target = target;

		$.ajax({
			type : "POST",
			url : "../nginx/config",
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
	var location = $("#location").val("");
	var target = $("#target").val("");
}

function deleteRoute(routeId) {
	$.ajax({
		type : "DELETE",
		url : "../nginx/config/" + routeId,
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
		url : "../nginx/config/routes",
		dataType : "json",
		success : function(data) {

			var port = data.proxyPort;
			$("#routeContents").empty();

			for (var i = 0; i < data.routes.length; i++) {
				var d = data.routes[i];
				var trow = $("<tr>");
				var locationData = $("<td height='100%'>");

				if (port != 80) {
					var locationContent = $("<a href='" + window.location.protocol + "//" + window.location.hostname + ":" + port + d.location + "' class='colText'><span>" + d.location + "</span></a>");
					locationData.append(locationContent);
					locationData.appendTo(trow);
				} else {
					var locationContent = $("<a href='" + window.location.protocol + "//" + window.location.hostname + d.location + "' class='colText'><span>" + d.location + "</span></a>");
					locationData.append(locationContent);
					locationData.appendTo(trow);
				}

				var targetData = $("<td height='100%'>");
				var targetContent = $("<a href='" + d.target + "'><span>" + d.target + "</span></a>");
				targetData.append(targetContent);
				targetData.appendTo(trow);

				var deleteContent = $("<td>");
				var deleteButton = $("<button id='" + d.uuid + "' class='btn btn-default'>Delete Route</button>");
				deleteButton.click(function(event) {
					event.preventDefault();
					deleteRoute(event.target.id);
				})
				deleteContent.append(deleteButton);
				deleteContent.appendTo(trow)

				$("#routeContents").append(trow);
			}
		},
		error : function(data) {
			alert("error retrieving route data")
		}
	})

}
