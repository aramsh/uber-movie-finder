<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <link rel="stylesheet" type="text/css" media="all" href="style.css">
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDj446TNUEk41qDdRY7vlTZqoTlYpszKZk&sensor=true"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <script type="text/javascript" src="js/jquery.autocomplete.min.js"></script>
  
    <script type="text/javascript">
    var map;
    var geocoder;
    var allInfoWindows;
    var allMovies;
    var allTitles;

    $(function() {
    	allInfoWindows = new Array();
    	allMovies = new Array();
    	allTitles = new Array();
    	geocoder = new google.maps.Geocoder();
       	map = new google.maps.Map(document.getElementById('map-canvas'), {
 	        zoom: 13,
 	        center: {lat: 37.761343, lng: -122.445242}
 	      });
       	
    	$.ajax({
    		url : "/getAll"
    	}).done(function(data) {
    		$.each($.parseJSON(data), function(idx, obj) {
    			markAddress(obj);
            });
    	});
    	
    	$("#mov-input").unbind('keyup').on("keyup", function() {
			search($("#mov-input").val());
		});
    	
    	map.controls[google.maps.ControlPosition.TOP_LEFT].push($("#mov-input")[0]);
    	
    	setTimeout(function() {
    		$('#mov-input').autocomplete({
        		lookup: allTitles,
        		onSelect: function (suggestion) {
        			search(suggestion.value);
        		} 
        	});
    	}, 5000);
    	
	}); 
    
    function markAddress(obj) {
		geocoder.geocode( { 'address': obj.locations}, function(results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
		    	map.setCenter(results[0].geometry.location);
		       	map.setZoom(13);
		       	var infowindow = new google.maps.InfoWindow({
		            content: MovieToString(obj)
		        });
		        var marker = new google.maps.Marker({
		            map: map,
		            position: results[0].geometry.location
		        });
		        
		        var listener = google.maps.event.addListenerOnce(marker, 'click', function() {
		        	for(var i = 0; i < allInfoWindows.length; i++) {
			        	allInfoWindows[i].close();
			        }
		        	allInfoWindows.push(infowindow);
		            infowindow.open(map, marker);
		        });
		        allMovies.push({"title": obj.title, "marker": marker, "listener": listener});
		        if(allTitles.indexOf(obj.title) == -1) {
		        	allTitles.push(obj.title);
		        }
			} 
		});
	 };
	 
 	 function search(str) {
		 for(var i = 0; i < allMovies.length; i++) {
			 if(allMovies[i].title.indexOf(str) == -1) {
				 allMovies[i].marker.setVisible(false);
			 } else {
				 allMovies[i].marker.setVisible(true);
			 }
		 }
	 }; 
	 
	 function MovieToString(movie) {
		var ret = movie.title;
		if(movie.releaseYear) 
			ret += " -- Release Year: " + movie.releaseYear;
		if(movie.director)
			ret += " -- Director: " + movie.director;
		return ret;
	 };

    </script>
  </head>
  <body>
  	<input id="mov-input" class="controls" type="text" placeholder="Search Movies">
    <div id="map-canvas"/>
  </body>
</html>