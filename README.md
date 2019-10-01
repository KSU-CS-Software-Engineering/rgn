# Rural grocery network optimizer

### About:

This application is meant to aid rural grocers by optimizing their distribution networks. It takes in 
information about stores or other distribution points of interest and will calculate the best way to 
load trucks, the order to distribute to stores in, etc. The application gives the user control to 
manipulate nodes (a store or distribution center), trucks, demand, and more. To accomplish the routing 
portion of the calculations, the application utilizes ArcGIS Online, a service that can find the best 
routes between nodes on a map. An ArcGIS Online subscription is required to fully utilize this application.

### Building with CLI:

* Install jdk11
* Install Maven
* Clone repository to a local folder
* Enter rgn folder
* Type `mvn package --file ./GroceryImplementation/pom.xml`
* Output jar can be found in ./rgn/GroceryImplementation/target/

### Building With Intellij Ultimate

* Install jdk11
* Install Intellij Ultimate
* Clone repository to a local folder
* Import repository to Intellij as a Maven Project
* Build & Run in Intellij

### Credits:

 * __Icons:__ https://icons8.com (Under CC BY-ND 3.0 licence)
 
