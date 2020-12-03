
//Modified version of the ArcGis javascript routing tutorial https://developers.arcgis.com/labs/javascript/driving-directions/
require([
    "esri/Map",
    "esri/views/MapView",
    "esri/widgets/Directions",
    "esri/Graphic",
    "esri/geometry/Point",
    "esri/geometry/Circle",
    "esri/symbols/SimpleFillSymbol"
], function loadMap(Map, MapView, Directions, Graphic, Point, Circle, SimpleFillSymbol) {
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
        view.ui.add(directions, "top-right");

        function ChangeMapBase() {

            var x = document.getElementById("MapBase").value;
            console.log(x);
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
                console.log(view.graphics.length);
                addGraphicClick("start", Point);


            } else if (view.graphics.length === 1) {
                console.log(view.graphics.length);
                addGraphicClick("finish", Point);
                // Call the route service
                //getRoute();
            } else {
                console.log(view.graphics.length);
                //view.graphics.removeAll();
                addGraphicClick("start", Point);
            }
            console.log("MapFromInput lon: " + lon + " lat: " + lat);
        }

        window.MapInput = MapInput;

        view.on("click", function (event) {
            /*if (view.graphics.length === 0) {
                console.log(view.graphics.length);
                addGraphicClick("start", event.mapPoint);


            } else if (view.graphics.length === 1) {
                console.log(view.graphics.length);
                addGraphicClick("end", event.mapPoint);
                // Call the route service
                //getRoute();
            } else {
                console.log(view.graphics.length);
                //view.graphics.removeAll();
                addGraphicClick("start", event.mapPoint);
            }*/

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

        function enter1(obj) {
            console.log("here");
            var keyboardEvent = new KeyboardEvent('keydown');
            delete keyboardEvent.which;
            keyboardEvent.which = 9;
            obj.dispatchEvent(keyboardEvent);
        }

        function GetRadius() {
            var longitude = document.getElementById("x-long-input").value;
            var latitude = document.getElementById("y-lat-input").value;
            var radius = document.getElementById("radius").value

            var symbol = new SimpleFillSymbol({ color: null, style: "solid", outline: { color: "blue", width: 1 } });
            var cir = new Circle({ center: new Point([longitude, latitude]), radius: radius, geodesic: true, radiusUnit: "miles" })
            var graphic = new Graphic(cir, symbol);
            console.log(graphic);
            view.graphics.add(graphic);
        }
        window.GetRadius = GetRadius;

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
            console.log("Centered the Map:  lon:" + lon + " lat:" + lat);

        }
        window.centerMap = centerMap;
    }
    window.CreateMap = CreateMap;
});