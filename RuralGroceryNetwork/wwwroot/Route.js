
//Modified version of the ArcGis javascript routing tutorial https://developers.arcgis.com/labs/javascript/driving-directions/
require([
    "esri/Map",
    "esri/views/MapView",
    "esri/widgets/Directions",
    "esri/Graphic",
    "esri/geometry/Point"
], function loadMap(Map, MapView, Directions, Graphic, Point) {
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

        /*view.on("click", function (event) {
            if (view.graphics.length === 0) {
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
            }
        });*/

        var popupTemplate = {
            title: "{Name}",
            content: "I am located at <b>{Lat}, {Lon}</b>."
        };

        function addGraphic(name, lat, lon) {
            var graphic = new Graphic({
                symbol: {
                    type: "simple-marker",
                    color: "blue",
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