$(function() {
	getRouteTable();

	$("#submitButton").click(function(event) {
		event.preventDefault();

		console.log("submitting new route");

		var location = $("#location").val();
		var target = $("#target").val();

		if (location == "" || target == "") {
			alert("both location and target are required fields");
		}

		var data = {};
		data.location = location;
		data.target = target;

		console.log(data)

		$.ajax({
			type : "POST",
			url : "../nginx/config",
			data : JSON.stringify(data),
			contentType : "application/json",
			success : function(data) {
				console.log("success");
				console.log(data);
				getRouteTable();
			},
			error : function(data) {
				console.log("error");
				console.log(data);
			}
		})
	})

	$("#deleteButton").click(function(event) {
		event.preventDefault();

		var uuid = $("#uuid").val();

		if (uuid == undefined || uuid == "") {
			alert("location is a required field for deleting a route");
		}

		$.ajax({
			type : "DELETE",
			url : "../nginx/config/" + uuid,
			success : function(data) {
				console.log("success");
				console.log(data);
				getRouteTable();
			},
			error : function(data) {
				console.log("error");
				console.log(data);
			}
		})
	})

});

function getRouteTable() {

	$.ajax({
		type : "GET",
		url : "../nginx/config/routes",
		dataType : "json",
		success : function(data) {
			console.log("success");
			console.log(data.routes);
			var port = data.proxyPort;
			$("#routeContents").empty();

			for (var i = 0; i < data.routes.length; i++) {
				var d = data.routes[i];
				var trow = $("<tr>");

				var locationData = $("<td>");
				if (port != 80) {
					var locationStr = "<a href='" + window.location.protocol
							+ "//" + window.location.hostname + ":" + port
							+ d.location + "'>" + d.location + "</a>";
					var locationContent = $(locationStr);
					locationData.append(locationContent);
					locationData.appendTo(trow);
				} else {
					var locationStr = "<a href='" + window.location.protocol
							+ "//" + window.location.hostname + d.location
							+ "'>" + d.location + "</a>";
					var locationContent = $(locationStr);
					locationData.append(locationContent);
					locationData.appendTo(trow);
				}

				var targetData = $("<td>");
				var targetContent = $("<a href='" + d.target + "'>" + d.target
						+ "</a>");
				targetData.append(targetContent);
				targetData.appendTo(trow);

				var uuidData = $("<td>");
				uuidData.append(d.uuid);
				uuidData.appendTo(trow)

				$("#routeContents").append(trow);
			}
		},
		error : function(data) {
			console.log("error");
			console.log(data);
		}
	})

}
