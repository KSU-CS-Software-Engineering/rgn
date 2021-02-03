/*
Copyright 2020 Kansas State University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

//Modified version of the ArcGis javascript routing tutorial https://developers.arcgis.com/labs/javascript/driving-directions/
require([
    "esri/Map",
    "esri/views/MapView",
    "esri/widgets/Directions",
    "esri/Graphic",
    "esri/geometry/Point",
    "esri/geometry/Circle",
    "esri/symbols/SimpleFillSymbol",
    "esri/tasks/RouteTask",
    "esri/tasks/support/RouteParameters",
    "esri/tasks/support/FeatureSet"
], function loadMap(Map, MapView, Directions, Graphic, Point, Circle, SimpleFillSymbol, RouteTask, RouteParameters, FeatureSet) {
    var map = new Map({
        basemap: "streets-navigation-vector"
    });

    function CreateMap() {

        var view = new MapView({
            container: "viewDiv",
            map: map,
            center: [-98.346331, 38.501613],
            zoom: 6,
        });

        var directions = new Directions({
            view: view,
            routeServiceUrl: "https://utility.arcgis.com/usrsvcs/appservices/6g6tiL0fLOmFllkm/rest/services/World/Route/NAServer/Route_World"
        });
        //view.ui.add(directions, "top-right");

        var routeTask = new RouteTask({
            url: "https://utility.arcgis.com/usrsvcs/appservices/6g6tiL0fLOmFllkm/rest/services/World/Route/NAServer/Route_World/solve"
        });

        function ChangeMapBase() {

            var x = document.getElementById("MapBase").value;
            map.basemap = x;

        }
        window.ChangeMapBase = ChangeMapBase;

        //Add nodes on the map by a given input
        function MapInput(lat, lon) {

            var Point = {
                type: "point",
                longitude: lon,
                latitude: lat,
            };
            if (view.graphics.length === 0) {
                addGraphicClick("start", Point);


            } else if (view.graphics.length === 1) {
                addGraphicClick("finish", Point);
                // Call the route service
                //getRoute();
            } else {
                //view.graphics.removeAll();
                addGraphicClick("start", Point);
            }
        }

        window.MapInput = MapInput;

        view.on("click", function (event) {
            /*if (view.graphics.length === 0) {
                addGraphicClick("start", event.mapPoint);


            } else if (view.graphics.length === 1) {
                addGraphicClick("end", event.mapPoint);
                // Call the route service
                //getRoute();
            } else {
                //view.graphics.removeAll();
                addGraphicClick("start", event.mapPoint);
            }*/

            //set clicked part as lat and long in textboxes
            document.getElementById("x-long-input").value = event.mapPoint.longitude;
            document.getElementById("y-lat-input").value = event.mapPoint.latitude;


            var screenPoint = {
                x: event.x,
                y: event.y
            };

            // Search for graphics at the clicked location
            view.hitTest(screenPoint).then(function (response) {
                if (response.results.length == 2) {
                    var graphic = response.results[0].graphic.geometry
                    var latitude = graphic.latitude
                    var longitude = graphic.longitude
                    document.getElementById("x-long-input").value = longitude;
                    document.getElementById("y-lat-input").value = latitude;

                    var direction_inputs = document.getElementsByClassName("esri-search__input");
                    if (direction_inputs[0].value === "") {
                        direction_inputs[0].value = longitude + ", " + latitude;
                    }
                    else if (direction_inputs[1].value === "") {
                        direction_inputs[1].value = longitude + ", " + latitude;
                    }
                    else {
                        direction_inputs[1].value = "";
                        direction_inputs[0].value = longitude + ", " + latitude;
                    }
                }
            });
        });

        function GetRadius(allStores) {
            var longitude = document.getElementById("x-long-input").value;
            var latitude = document.getElementById("y-lat-input").value;
            var radius = document.getElementById("radius").value

            view.graphics.items.forEach(function (g, i) {
                
                if (g.id === "circle1") {
                    view.graphics.remove(g);
                }
            });

            view.graphics.items.forEach(function (g, i) {
                if (g.id === "route1") {
                    view.graphics.remove(g);
                }
            });
            
            var symbol = new SimpleFillSymbol({ color: null, style: "solid", outline: { color: "blue", width: 1 } });
            var cir = new Circle({ center: new Point([longitude, latitude]), radius: radius, geodesic: true, radiusUnit: "miles" })
            var graphic = new Graphic(cir, symbol);
            graphic.id = "circle1";
            view.graphics.add(graphic);

            var inCircle = new Array();
            var circleCenter = {
                type: "point",
                longitude: cir.center.longitude,
                latitude: cir.center.latitude
            };
            
            allStores.forEach(function (store) {
                var x = store.xlong;
                var y = store.ylat;
                var point = new Graphic({
                    geometry: new Point(x, y),
                    longitude: x,
                    latitude: y,
                    attributes: { distanceToCenter: 0, name: store.storeName }
                });

                var dis = distance(point.latitude, point.longitude, circleCenter.latitude, circleCenter.longitude)

                if (dis <= cir.radius) {
                    point.attributes.distanceToCenter = dis;
                    inCircle.push(point);
                }               
            });

            //sort by length
            var len = inCircle.length;
            for (i = 0; i < len; i++) {
                for (j = 0; j < len - 1; j++) {
                    if (inCircle[j].attributes.distanceToCenter > inCircle[j + 1].attributes.distanceToCenter) {
                        var tmp = inCircle[j];
                        inCircle[j] = inCircle[j + 1];
                        inCircle[j + 1] = tmp;
                    }
                }
            }

            document.getElementById("secondary-provider").innerHTML = inCircle[0].attributes["name"];

            for (i = 1; i < inCircle.length; i++) {
                var node = document.createElement("div");
                var textnode = document.createTextNode(inCircle[i].attributes["name"]);
                node.appendChild(textnode);
                document.getElementById("radius-stores").appendChild(node);
            }

            console.log(inCircle);

            getRouteInCircle(inCircle);
        }
        window.GetRadius = GetRadius;

        // Used to calculate distance between points -- Haversine Formula
        function distance(lat1, lon1, lat2, lon2) {
            var p = 0.017453292519943295;    // Math.PI / 180
            var c = Math.cos;
            var a = 0.5 - c((lat2 - lat1) * p) / 2 +
                c(lat1 * p) * c(lat2 * p) *
                (1 - c((lon2 - lon1) * p)) / 2;

            return ((12742 * Math.asin(Math.sqrt(a))) / 1.60934); // 2 * R; R = 6371 km -- This is converted to miles
        }
        window.distance = distance;

        function getRouteInCircle(points) {
            var routeParams = new RouteParameters({
                stops: new FeatureSet({
                    features: points
                }),
                returnDirections: true
            });
            routeTask.solve(routeParams).then(function (data) {
                data.routeResults.forEach(function (result) {
                    result.route.symbol = {
                        type: "simple-line",
                        color: [5, 150, 255],
                        width: 3
                    };
                    result.route.id = "route1";
                    view.graphics.add(result.route);
                });
            });
        }
        window.getRouteInCircle = getRouteInCircle;
        
        var popupTemplate = {
            title: "{Name}",
            content: "I am located at <b>{Lon}, {Lat}</b>."
        };

        function addGraphic(name, lat, lon, color) {
            var graphic = new Graphic({
                symbol: {
                    type: "simple-marker",
                    color: color,
                    size: "10px"
                },
                geometry: new Point(lon, lat),
                attributes: { Name: name, Lat: lat, Lon: lon },
                popupTemplate: popupTemplate
            });
            view.graphics.add(graphic);
        }
        window.addGraphic = addGraphic;

        function addGraphicClick(type, point) {
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

        function centerMap(lat, lon) {

            view.center = [lon, lat];

        }
        window.centerMap = centerMap;
    }
    window.CreateMap = CreateMap;
});