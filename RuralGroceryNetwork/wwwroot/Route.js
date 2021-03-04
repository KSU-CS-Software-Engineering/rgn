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
        //Creates container for map to be view in
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

        //Function that changes map base from what's selected from "MapBase" in the *Editor.razor files
        function ChangeMapBase() {
            var x = document.getElementById("MapBase").value;
            map.basemap = x;
        }
        window.ChangeMapBase = ChangeMapBase;

        //Adds diffrent types nodes on the map by a given lat/lon input
        function MapInput(lat, lon) {
            var Point = {
                type: "point",
                longitude: lon,
                latitude: lat,
            };
            //If first point added then add a starting node
            if (view.graphics.length === 0) {
                addGraphicClick("start", Point);
            } else if (view.graphics.length === 1) {  //If it's to be the second graphic (or node) added, then add a finish node
                addGraphicClick("finish", Point);
            } else {   //If two or more nodes are on the map then added another starting node
                //view.graphics.removeAll();
                addGraphicClick("start", Point);
            }
        }
        window.MapInput = MapInput;

        //Controls what happens when the map is clicked
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

            //Set clicked part as lat and long in textboxes
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
                    } else if (direction_inputs[1].value === "") {
                        direction_inputs[1].value = longitude + ", " + latitude;
                    } else {
                        direction_inputs[1].value = "";
                        direction_inputs[0].value = longitude + ", " + latitude;
                    }
                }
            });
        });

        // Generates a radius on the map
        async function GetRadius(allStores) {
            // Pulls the latitude, longitude, and radius size from corresponding textboxes
            var longitude = document.getElementById("x-long-input").value;
            var latitude = document.getElementById("y-lat-input").value;
            var radius = document.getElementById("radius").value;
            if (radius == 0) {
                radius = 25;
                document.getElementById("radius").value = 25;
            }

            var symbol = new SimpleFillSymbol({ color: null, style: "solid", outline: { color: "blue", width: 1 } });
            var cir = new Circle({ center: new Point([longitude, latitude]), radius: radius, geodesic: true, radiusUnit: "miles" })

            // Generates a array of all stores in the radius called inCircle
            var inCircle = new Array();

            // Makes a point in the center of the radius
            var circleCenter = {
                type: "point",
                longitude: cir.center.longitude,
                latitude: cir.center.latitude
            };

            // Goes through all the stores to make a point and add the stores info to it
            allStores.forEach(function (store) {
                var x = store.xlong;
                var y = store.ylat;
                var point = new Graphic({
                    geometry: new Point(x, y),
                    longitude: x,
                    latitude: y,
                    attributes: {
                        distanceToCenter: 0,
                        name: store.storeName,
                        address: store.address,
                        city: store.cityName,
                        state: store.stateName,
                        zip: store.zipCode,
                        cityPopulation: store.cityPopulation,
                        weeklyPurchaseAmount: store.weeklyPurchaseAmount
                    }
                });

                // Finds distance of store from center of radius
                var dis = distance(point.latitude, point.longitude, circleCenter.latitude, circleCenter.longitude)

                // If store point is within the radius then add to the inCircle array
                if (dis <= cir.radius) {
                    point.attributes.distanceToCenter = dis;
                    inCircle.push(point);
                }

                if (dis == 0) {
                    addGraphic(point, point.latitude, point.longitude, "", "15px");
                }
            });

            // Generates the radius and adds to map
            var graphic = new Graphic(cir, symbol);
            graphic.id = "circle1";
            view.graphics.add(graphic);

            // Reorginize inCircle array by closest distance to the center to farthest
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

            // Finds the weekely Purchase Amount total for all stores in radius
            var weeklyPurchaseAmount = 0;
            var div = document.getElementById("radius-stores");
            div.innerHTML = ""; //clear it
            for (i = 0; i < inCircle.length; i++) {
                if (i != 0) {
                    var checkbox = document.createElement("input");
                    checkbox.setAttribute("type", "checkbox");
                    checkbox.setAttribute("class", "scenarios-checkbox");
                    checkbox.setAttribute("onClick", "updateSummary(this)");
                    checkbox.setAttribute("data-weeklyamount", inCircle[i].attributes["weeklyPurchaseAmount"])
                    div.appendChild(checkbox);
                }

                createAndAppendTo("h3", inCircle[i].attributes["name"], div);

                if (i == 0)
                    createAndAppendTo("p", "(Secondary Distributor)", div);

                createAndAppendTo("p", displayAddress(inCircle[i]), div);
                createAndAppendTo("p", "City Population: " + inCircle[i].attributes["cityPopulation"], div)

                createAndAppendTo("p", "Weekly Purchase Amount: $" + inCircle[i].attributes["weeklyPurchaseAmount"], div)
                weeklyPurchaseAmount += inCircle[i].attributes["weeklyPurchaseAmount"]


                if (i != 0) {
                    await displayDistanceToCenter(inCircle[0], inCircle[i]).then(function (result) {
                        createAndAppendTo("p", result, div)
                    })
                    //createAndAppendTo("p", displayDistanceToCenter(inCircle[0], inCircle[i]), div)
                }
            }


            div = document.getElementById("summary");
            div.innerHTML = "" //clear it

            // Create text to show following statistics
            createAndAppendTo("h3", "Summary", div);
            createAndAppendTo("p", "Stores in Radius: " + inCircle.length, div)
            createAndAppendTo("h5", "Weekly Purchase Amount", div)
            createAndAppendTo("p", "Amount of All Stores in Radius: $" + weeklyPurchaseAmount, div)
            createAndAppendTo("p", "Amount for Selected Stores: $" + inCircle[0].attributes["weeklyPurchaseAmount"], div, "selected-stores-amount")
            createAndAppendTo("p", "Distributor Minimum Amount: $25,000", div, "min-distributor-amount")
            createAndAppendTo("p", "Selected Stores Meets Minimum Amount: " + meetsMinimum(inCircle[0].attributes["weeklyPurchaseAmount"]), div, "meets-minimum")
        }
        window.GetRadius = GetRadius;

        // A function to create an element and append it to given var
        function createAndAppendTo(element, text, append_to, id = "") {
            var name = document.createElement(element);
            if (id != "")
                name.setAttribute("id", id);
            var textnode = document.createTextNode(text);
            name.appendChild(textnode);
            append_to.appendChild(name);
        }

        // A function to return a string of text with the address for a store 
        function displayAddress(store) {
            return store.attributes["address"] + " " + store.attributes["city"] + ", " + store.attributes["state"] + " " + store.attributes["zip"];
        }

        // A function return text containing the distance from one given store to the second given store in a straight line 
        async function displayDistanceToCenter(centerStore, store) {
            length = await getRouteInCircle([centerStore, store]).then(function (result) {
                return result;
            });
            return "Distance to " + centerStore.attributes["name"] + ": " + Math.round(length * 10) / 10 + " miles";
        }

        // A function that returns whether the minimum amount is met or not.
        function meetsMinimum(store_amount) {
            if (store_amount >= 25000)
                return "Yes"
            return "No"
        }

        // A function that updates whether the minimum amount is met or not.
        function updateMeetsMinimum(store_amount) {
            element = document.getElementById("meets-minimum");
            element = element.textContent.split(": ");
            element[1] = parseInt(element[1].substring(1)); //get rid of $ and parseInt

            if (store_amount >= 25000)
                return document.getElementById("meets-minimum").innerHTML = element[0] + ": Yes";
            return document.getElementById("meets-minimum").innerHTML = element[0] + ": No";
        }

        // A function to update the weekly buying amount for stores
        function updateSummary(checkbox) {
            element = document.getElementById("selected-stores-amount");
            element = element.textContent.split(": ");
            element[1] = parseInt(element[1].substring(1)); //get rid of $ and parseInt

            var weeklyAmount = parseInt(checkbox.dataset.weeklyamount);
            if (checkbox.checked) {
                element[1] += weeklyAmount;
            } else { 
                element[1] -= weeklyAmount;
            }

            document.getElementById("selected-stores-amount").innerHTML = element[0] + ": $" + element[1];
            updateMeetsMinimum(element[1]);
        }
        window.updateSummary = updateSummary;

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

        // A function to find a route with given points and adds that path to the map
        async function getRouteInCircle(points) {
            var length = 0;
            var routeParams = new RouteParameters({
                stops: new FeatureSet({
                    features: points
                }),
                returnDirections: true
            });
            await routeTask.solve(routeParams).then(function (data) {
                data.routeResults.forEach(function (result) {
                    result.route.symbol = {
                        type: "simple-line",
                        color: [5, 150, 255],
                        width: 3
                    };
                    result.route.class = "route1";
                    view.graphics.add(result.route);
                    length = result.route.attributes.Total_Miles;
                });
            });
            return length;
        }
        window.getRouteInCircle = getRouteInCircle;

        // A template for a popup for a store of it's information
        var popupTemplate = {
            title: "<b>{name}</b>",
            content: "{address}<br>{city}, {state} {zip}<br><br>Weekly Purchase Amount: ${weeklyPurchaseAmount}"
        };

        // A function to add a graphic to the map for a store
        function addGraphic(store, lat, lon, color, size = "10px") {
            outlineColor = "black";
            outlineSize = 1;
            id = "";
            if (size == "15px") {
                // Checks to see if their is already a radius, route, or center point on the map, if so removes the old one
                view.graphics.forEach(function (g, i) {
                    console.log(g)
                    if (g.id === "circle1" || g.class === "route1" || g.id === "centerPoint") {
                        view.graphics.remove(g);
                    }
                });

                store = store.attributes;
                outlineColor = "#00f";
                outlineSize = 1.5;
                id = "centerPoint"
            }
            if (color == "") {
                const s = store.weeklyPurchaseAmount;
                // Sets a color based off a stores weekly purchaseing amount
                switch (true) {
                    case (s == 0): color = "#E6E6FA"
                        break;
                    case (s < 5000): color = "#D8BFD8"
                        break;
                    case (s < 10000): color = "#EE82EE"
                        break;
                    case (s < 15000): color = "#9370DB"
                        break;
                    case (s < 20000): color = "#8A2BE2"
                        break;
                    case (s < 36001): color = "#4B0082"
                        break;
                    default:
                        color = "yellow";
                }
            }

            // Creates a graphic for a store
            var graphic = new Graphic({
                symbol: {
                    type: "simple-marker",
                    color: color,
                    size: size,
                    outline: {
                        color: outlineColor,
                        width: outlineSize
                    }
                },
                geometry: new Point(lon, lat),
                attributes: {
                    name: store.storeName,
                    address: store.address,
                    city: store.cityName,
                    state: store.stateName,
                    zip: store.zipCode,
                    weeklyPurchaseAmount: store.weeklyPurchaseAmount,
                    store: store
                },
                popupTemplate: popupTemplate
            });
            graphic.id = id
            view.graphics.add(graphic);
        }
        window.addGraphic = addGraphic;

        // A function to display the legend for the map
        function displayLegend() {
            document.getElementById("map-legend").style.display = "block";
        }
        window.displayLegend = displayLegend;

        // A function to add a either a start or finish point type
        function addGraphicClick(type, point) {
            var graphic = new Graphic({
                symbol: {
                    type: "simple-marker",
                    color: (type === "start") ? "green" : "red", // If the type is start then the point is green, otherwise it's red.
                    size: "10px"
                },
                geometry: point
            });
            view.graphics.add(graphic);
        }

        // A function to center the map on a given lat and lon point
        function centerMap(lat, lon) {
            view.center = [lon, lat];
        }
        window.centerMap = centerMap;

       
        // Used to start the download for a csv file from the Database
        // This code is directly taken from: https://www.meziantou.net/generating-and-downloading-a-file-in-a-blazor-webassembly-application.htm
        function BlazorDownloadFile(filename, contentType, content) {
            // Blazor marshall byte[] to a base64 string, so we first need to convert the string (content) to a Uint8Array to create the File
            const data = base64DecToArr(content);

            // Create the URL
            const file = new File([data], filename, { type: contentType });
            const exportUrl = URL.createObjectURL(file);

            // Create the <a> element and click on it
            const a = document.createElement("a");
            document.body.appendChild(a);
            a.href = exportUrl;
            a.download = filename;
            a.target = "_self";
            a.click();

            // We don't need to keep the url, let's release the memory            
            URL.revokeObjectURL(exportUrl);
        }
        window.BlazorDownloadFile = BlazorDownloadFile;

        // Convert a base64 string to a Uint8Array. This is needed to create a blob object from the base64 string.
        // These two functions for conversion comes from: https://developer.mozilla.org/fr/docs/Web/API/WindowBase64/D%C3%A9coder_encoder_en_base64
        function b64ToUint6(nChr) {
            return nChr > 64 && nChr < 91 ? nChr - 65 : nChr > 96 && nChr < 123 ? nChr - 71 : nChr > 47 && nChr < 58 ? nChr + 4 : nChr === 43 ? 62 : nChr === 47 ? 63 : 0;
        }
        window.b64ToUint6 = b64ToUint6;

        // A function to convert strings to help convert .csv files
        function base64DecToArr(sBase64, nBlocksSize) {
            var
                sB64Enc = sBase64.replace(/[^A-Za-z0-9\+\/]/g, ""),
                nInLen = sB64Enc.length,
                nOutLen = nBlocksSize ? Math.ceil((nInLen * 3 + 1 >> 2) / nBlocksSize) * nBlocksSize : nInLen * 3 + 1 >> 2,
                taBytes = new Uint8Array(nOutLen);
            for (var nMod3, nMod4, nUint24 = 0, nOutIdx = 0, nInIdx = 0; nInIdx < nInLen; nInIdx++) {
                nMod4 = nInIdx & 3;
                nUint24 |= b64ToUint6(sB64Enc.charCodeAt(nInIdx)) << 18 - 6 * nMod4;
                if (nMod4 === 3 || nInLen - nInIdx === 1) {
                    for (nMod3 = 0; nMod3 < 3 && nOutIdx < nOutLen; nMod3++, nOutIdx++) {
                        taBytes[nOutIdx] = nUint24 >>> (16 >>> nMod3 & 24) & 255;
                    }
                    nUint24 = 0;
                }
            }
            return taBytes;
        }
        window.base64DecToArr = base64DecToArr;

        // Create a simple browser message alert to display messages to the user.
        function ShowMessage(message) {
            window.alert(message);
        }
        window.ShowMessage = ShowMessage;
    }
    window.CreateMap = CreateMap;
});