# Rural Grocery Network
https://ruralgrocerynetwork.azurewebsites.net/


### About:

This application is meant to aid rural grocers by optimizing their distribution networks. It takes in 
information about stores or other distribution points of interest and will calculate the best way to 
load trucks, the order to distribute to stores in, etc. The application gives the user control to 
manipulate nodes (a store or distribution center), trucks, demand, and more. To accomplish the routing 
portion of the calculations, the application utilizes ArcGIS Online, a service that can find the best 
routes between nodes on a map. An ArcGIS Online subscription is required to fully utilize this application.

### Building && Running

* Install .NET Core 3.1
* Clone git repository
* Enter repository folder
* Run `dotnet build`
* Run `dotnet run --project ./RuralGroceryNetwork/`
* Navigate to `http://localhost:5001`
