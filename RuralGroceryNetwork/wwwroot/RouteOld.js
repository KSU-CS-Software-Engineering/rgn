
//Modified version of the ArcGis javascript routing tutorial https://developers.arcgis.com/labs/javascript/get-a-route-and-directions/
/*require([
    "esri/Map",
    "esri/views/MapView",
    "esri/Graphic",
    "esri/tasks/RouteTask",
    "esri/tasks/support/RouteParameters",
    "esri/tasks/support/FeatureSet",
    "esri/widgets/Search"
], function loadMap(Map, MapView, Graphic, RouteTask, RouteParameters, FeatureSet, Search) {
    var map = new Map({
        basemap: "streets-navigation-vector"
    });

    function CreateMap() {


        console.log("This is a view div");
        console.log(document.getElementById("viewDiv"));

        var view = new MapView({
            container: "viewDiv",
            map: map,
            center: [-98.346331, 38.501613],
            zoom: 6
        });

        // Add Search widget
        var search = new Search({
            view: view
        });

        //Add addres search bar in top right
        view.ui.add(search, "top-right"); // Add to the map


        // used to add your own Arcgis account authentication. For more info check the "Challenge" part of ArcGis javascript routing tutorial https://developers.arcgis.com/labs/javascript/get-a-route-and-directions/
        var routeTask = new RouteTask({
            url: "https://utility.arcgis.com/usrsvcs/appservices/6g6tiL0fLOmFllkm/rest/services/World/Route/NAServer/Route_World/solve"
        });


        //------------------- Section for adding "click" event for display nodes on the map -------------------------------//
        view.on("click", function (event) {
            if (view.graphics.length === 0) {
                console.log(view.graphics.length);
                addGraphic("start", event.mapPoint);


            } else if (view.graphics.length === 1) {
                console.log(view.graphics.length);
                addGraphic("finish", event.mapPoint);
                // Call the route service
                getRoute();
            } else {
                console.log(view.graphics.length);
                view.graphics.removeAll();
                addGraphic("start", event.mapPoint);
            }
        });


        //------------------- Section for displaying current  Lat/Lon in the map -------------------------------//

        //*** Add div element to show coordates ***/
        /*var coordsWidget = document.createElement("div");
        coordsWidget.id = "coordsWidget";
        coordsWidget.className = "esri-widget esri-component";
        coordsWidget.style.padding = "7px 15px 5px";
        view.ui.add(coordsWidget, "bottom-right");

        //*** Update lat, lon, zoom and scale ***/
        /*function showCoordinates(pt) {
            var coords = "Lat/Lon " + pt.latitude.toFixed(3) + " " + pt.longitude.toFixed(3);
            coordsWidget.innerHTML = coords;
        }

        //*** Add event and show center coordinates after the view is finished moving e.g. zoom, pan ***/
        /*view.watch(["stationary"], function () {
            showCoordinates(view.center);
        });

        //*** Add event to show mouse coordinates on click and move ***/
        /*view.on(["pointer-down", "pointer-move"], function (evt) {
            showCoordinates(view.toMap({ x: evt.x, y: evt.y }));
        });




        //Add the nodes graphics in map
        function addGraphic(type, point) {
            var graphic = new Graphic({
                symbol: {
                    type: "simple-marker",
                    color: (type === "start") ? "green" : "red",
                    size: "10px"
                },
                geometry: point

            });
            view.graphics.add(graphic);
        }

        function getRoute() {
            // Setup the route parameters
            var routeParams = new RouteParameters({
                stops: new FeatureSet({
                    features: view.graphics.toArray() // Pass the array of graphics
                }),
                returnDirections: true
            });
            // Get the route
            routeTask.solve(routeParams).then(function (data) {
                // Display the route
                data.routeResults.forEach(function (result) {
                    result.route.symbol = {
                        type: "simple-line",
                        color: [5, 150, 255],
                        width: 3
                    };
                    view.graphics.add(result.route);
                });



                //A click event to display  a Popup of addres and coordinates of a location.
                view.on("click", function (evt) {
                    search.clear();
                    view.popup.clear();
                    if (search.activeSource) {
                        var geocoder = search.activeSource.locator; // World geocode service
                        var params = {
                            location: evt.mapPoint
                        };
                        geocoder.locationToAddress(params)
                            .then(function (response) { // Show the address found
                                var address = response.address;
                                showPopup(address, evt.mapPoint);
                            }, function (err) { // Show no address found
                                showPopup("No address found.", evt.mapPoint);
                            });
                    }
                });

                //The Popup window. 
                function showPopup(address, pt) {
                    view.popup.open({
                        title: + Math.round(pt.latitude * 100000) / 100000 + ", " + Math.round(pt.longitude * 100000) / 100000,
                        content: address,
                        location: pt
                    });
                }




                // Display the directions
                var directions = document.createElement("ol");
                directions.classList = "esri-widget esri-widget--panel esri-directions__scroller";
                directions.style.marginTop = 0;
                directions.style.width = "150px";
                // Show the directions
                var features = data.routeResults[0].directions.features;
                features.forEach(function (result, i) {
                    var direction = document.createElement("li");
                    direction.innerHTML = result.attributes.text + " (" + result.attributes.length.toFixed(2) + " miles)";
                    directions.appendChild(direction);
                });

                // Add directions to the view
                view.ui.empty("top-left");
                view.ui.add(directions, "top-left");
            });
        }

        //Add nodes on the map by a given input
        function MapInput(lat, lon) {

            var Point = {
                type: "point",
                longitude: lon,
                latitude: lat,
            };
            if (view.graphics.length === 0) {
                console.log(view.graphics.length);
                addGraphic("start", Point);


            } else if (view.graphics.length === 1) {
                console.log(view.graphics.length);
                addGraphic("finish", Point);
                // Call the route service
                getRoute();
            } else {
                console.log(view.graphics.length);
                view.graphics.removeAll();
                addGraphic("start", Point);
            }
            console.log("MapFromInput lon: " + lon + " lat: " + lat);
        }

        window.MapInput = MapInput;

        //Center the map on a given lat/lon 
        function centerMap(lat, lon) {

            view.center = [lon, lat];
            console.log("Centered the Map:  lon:" + lon + " lat:" + lat);

        }
        window.centerMap = centerMap;

        function ChangeMapBase() {

            var x = document.getElementById("MapBase").value;
            console.log(x);
            map.basemap = x;

        }
        window.ChangeMapBase = ChangeMapBase;

    }
    window.CreateMap = CreateMap;

});*/
