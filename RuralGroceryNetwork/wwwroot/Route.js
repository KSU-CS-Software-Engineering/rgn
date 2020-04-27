require([
    "esri/Map",
    "esri/views/MapView",
    "esri/Graphic",
    "esri/tasks/RouteTask",
    "esri/tasks/support/RouteParameters",
    "esri/tasks/support/FeatureSet"
], function loadMap(Map, MapView, Graphic, RouteTask, RouteParameters, FeatureSet) {


    function CreateMap() {

        var map = new Map({
            basemap: "streets-navigation-vector"
        });
        console.log("This is a view div");
        console.log(document.getElementById("viewDiv"));

        var view = new MapView({

            container: "viewDiv",
            map: map,
            center: [-118.24532, 34.05398],
            zoom: 14
        });



        var routeTask = new RouteTask({
            url: "https://route.arcgis.com/arcgis/rest/services/World/Route/NAServer/Route_World"
        });

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



                // Display the directions
                var directions = document.createElement("ol");
                directions.classList = "esri-widget esri-widget--panel esri-directions__scroller";
                directions.style.marginTop = 0;
                directions.style.paddingTop = "15px";

                // Show the directions
                var features = data.routeResults[0].directions.features;
                features.forEach(function (result, i) {
                    var direction = document.createElement("li");
                    direction.innerHTML = result.attributes.text + " (" + result.attributes.length.toFixed(2) + " miles)";
                    directions.appendChild(direction);
                });

                // Add directions to the view
                view.ui.empty("top-right");
                view.ui.add(directions, "top-right");
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
});
