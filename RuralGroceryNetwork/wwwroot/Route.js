require([
      "esri/Map",
      "esri/views/MapView",      
      "esri/Graphic",
      "esri/tasks/RouteTask",
      "esri/tasks/support/RouteParameters",
        "esri/tasks/support/FeatureSet"
], function loadMap(Map, MapView, Graphic, RouteTask, RouteParameters, FeatureSet) {

       // window.onload = function () {
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
                    zoom: 12
                });

                // To allow access to the route service and prevent the user from signing in, do the Challenge step in the lab to set up a service proxy

                var routeTask = new RouteTask({
                    url: "https://route.arcgis.com/arcgis/rest/services/World/Route/NAServer/Route_World"
                });

                view.on("click", function (event) {
                    if (view.graphics.length === 0) {
                        addGraphic("start", event.mapPoint);
                    } else if (view.graphics.length === 1) {
                        addGraphic("finish", event.mapPoint);
                        // Call the route service
                        getRoute();
                    } else {
                        view.graphics.removeAll();
                        addGraphic("start", event.mapPoint);
                    }
                });

                function addGraphic(type, point) {
                    var graphic = new Graphic({
                        symbol: {
                            type: "simple-marker",
                            color: (type === "start") ? "white" : "black",
                            size: "8px"
                        },
                        geometry: point
                    });
                    view.graphics.add(graphic);
                }

                function getRoute() {
                    // Setup the route parameters
                    var routeParams = new RouteParameters({
                        stops: new FeatureSet({
                            features: view.graphics.toArray()
                        }),
                        returnDirections: true
                    });
                    // Get the route
                    routeTask.solve(routeParams).then(function (data) {
                        data.routeResults.forEach(function (result) {
                            result.route.symbol = {
                                type: "simple-line",
                                color: [5, 150, 255],
                                width: 3
                            };
                            view.graphics.add(result.route);
                        });

                    });
                }



        }
        window.CreateMap = CreateMap;

   //         this.setTimeout(function () {            },3000);

          
     //   }

    });