/*
* Author: Zachery Brunner
* Modified by: Matt Llewelyn
* Purpose: Initializes the database for the rural grocery init
* 
*/

DROP TABLE IF EXISTS [dbo].[StoreDeliveryInformation];
DROP TABLE IF EXISTS [dbo].[StoreDeliverySchedule];
DROP TABLE IF EXISTS [dbo].[StoreEquipmentInformation];
DROP TABLE IF EXISTS [dbo].[StoreSquareFootage];
DROP TABLE IF EXISTS [dbo].[StoreInformation];
DROP TABLE IF EXISTS [dbo].[SquareFootageCategories];
DROP TABLE IF EXISTS [dbo].[Distributor]; 
DROP TABLE IF EXISTS [dbo].[Cities]; 
DROP TABLE IF EXISTS [dbo].[States];
DROP TABLE IF EXISTS [dbo].[StoreInformationStaging];
DROP TABLE IF EXISTS [dbo].[DistributorStaging];
DROP TABLE IF EXISTS [dbo].[StatesStaging];
DROP TABLE IF EXISTS [dbo].[CitiesStaging];

/*
* Table: [dbo].[States]
* Active Constraints: 
		StateName_Unique: Requires uniqueness amoung the state names
		
* Values: All 50 states within the United States
*/
CREATE TABLE [dbo].[States]
(
  StateID INT PRIMARY KEY IDENTITY(1, 1),
  StateName VARCHAR(32) NOT NULL
);

GO

ALTER TABLE [dbo].[States]
ADD CONSTRAINT StateName_Unique UNIQUE (StateName);

GO

INSERT INTO [dbo].[States] (StateName)
VALUES
 ('Alabama'),
 ('Alaska'),
 ('Arizona'),
 ('Arkansas'),
 ('California'),
 ('Colorado'),
 ('Connecticut'),
 ('Delaware'),
 ('Florida'),
 ('Georgia'),
 ('Hawaii'),
 ('Idaho'),
 ('Illinois'),
 ('Indiana'),
 ('Iowa'),
 ('Kansas'),
 ('Kentucky'),
 ('Louisiana'),
 ('Maine'),
 ('Maryland'),
 ('Massachusetts'),
 ('Michigan'),
 ('Minnesota'),
 ('Mississippi'),
 ('Missouri'),
 ('Montana'),
 ('Nebraska'),
 ('Nevada'),
 ('New Hampshire'),
 ('New Jersey'),
 ('New Mexico'),
 ('New York'),
 ('North Carolina'),
 ('North Dakota'),
 ('Ohio'),
 ('Oklahoma'),
 ('Oregon'),
 ('Pennsylvania'),
 ('Rhode Island'),
 ('South Carolina'),
 ('South Dakota'),
 ('Tennessee'),
 ('Texas'),
 ('Utah'),
 ('Vermont'),
 ('Virginia'),
 ('Washington'),
 ('West Virginia'),
 ('Wisconsin'),
 ('Wyoming')
 ;
 
/* ******************************************************* END OF STATE TABLE *************************************************************** */
 GO
 
 /*
* Table: [dbo].[StatesStaging]
* Active Constraints: 
		None
		
* Values: Updated at runtime temporarily to merge into permanent table
*/

CREATE TABLE [dbo].[StatesStaging]
(
  StateID INT,
  StateName VARCHAR(32) NOT NULL
);

GO

/* ******************************************************* END OF STATES STAGING TABLE *************************************************************** */

/*
* Table: [dbo].[Cities]
* Active Constraints: 
		CityState_Unique: Requires uniqueness amoung the city names and the state where they reside
		
* Values: Currently only holds all the cities in Kansas, this can be expanded to hold all cities in the United States
*/
CREATE TABLE [dbo].[Cities]
(
  CityID INT PRIMARY KEY IDENTITY(1, 1),
  CityName VARCHAR(32) NOT NULL,
  StateID INT NOT NULL,
  
  FOREIGN KEY (StateID) REFERENCES [dbo].[States](StateID)
);

GO

ALTER TABLE [dbo].[Cities]
ADD CONSTRAINT CityState_Unique UNIQUE (CityName, StateID);

GO

INSERT INTO [dbo].[Cities] (CityName, StateID)
VALUES
('Abbyville', 16),
('Abilene', 16),
('Admire', 16),
('Agenda', 16),
('Agra', 16),
('Albert', 16),
('Alden', 16),
('Alexander', 16),
('Allen', 16),
('Alma', 16),
('Almena', 16),
('Alta Vista', 16),
('Altamont', 16),
('Alton', 16),
('Altoona', 16),
('Americus', 16),
('Andale', 16),
('Andover', 16),
('Anthony', 16),
('Arcadia', 16),
('Argonia', 16),
('Arkansas City', 16),
('Arlington', 16),
('Arma', 16),
('Ashland', 16),
('Assaria', 16),
('Atchison', 16),
('Athol', 16),
('Atlanta', 16),
('Attica', 16),
('Atwood', 16),
('Auburn', 16),
('Augusta', 16),
('Aurora', 16),
('Axtell', 16),
('Baldwin City', 16),
('Barnard', 16),
('Barnes', 16),
('Bartlett', 16),
('Basehor', 16),
('Bassett', 16),
('Baxter Springs', 16),
('Bazine', 16),
('Beattie', 16),
('Bel Aire', 16),
('Belle Plaine', 16),
('Belleville', 16),
('Beloit', 16),
('Belpre', 16),
('Belvue', 16),
('Benedict', 16),
('Bennington', 16),
('Bentley', 16),
('Benton', 16),
('Bern', 16),
('Beverly', 16),
('Bird City', 16),
('Bison', 16),
('Blue Mound', 16),
('Blue Rapids', 16),
('Bluff City', 16),
('Bogue', 16),
('Bonner Springs', 16),
('Brewster', 16),
('Bronson', 16),
('Brookville', 16),
('Brownell', 16),
('Bucklin', 16),
('Buffalo', 16),
('Buhler', 16),
('Bunker Hill', 16),
('Burden', 16),
('Burdett', 16),
('Burlingame', 16),
('Burlington', 16),
('Burns', 16),
('Burr Oak', 16),
('Burrton', 16),
('Bushong', 16),
('Bushton', 16),
('Byers', 16),
('Caldwell', 16),
('Cambridge', 16),
('Caney', 16),
('Canton', 16),
('Carbondale', 16),
('Carlton', 16),
('Cassoday', 16),
('Cawker City', 16),
('Cedar', 16),
('Cedar Point', 16),
('Cedar Vale', 16),
('Centralia', 16),
('Chanute', 16),
('Chapman', 16),
('Chase', 16),
('Chautauqua', 16),
('Cheney', 16),
('Cherokee', 16),
('Cherryvale', 16),
('Chetopa', 16),
('Cimarron', 16),
('Circleville', 16),
('Claflin', 16),
('Clay Center', 16),
('Clayton', 16),
('Clearwater', 16),
('Clifton', 16),
('Climax', 16),
('Clyde', 16),
('Coats', 16),
('Coffeyville', 16),
('Colby', 16),
('Coldwater', 16),
('Collyer', 16),
('Colony', 16),
('Columbus', 16),
('Colwich', 16),
('Concordia', 16),
('Conway Springs', 16),
('Coolidge', 16),
('Copeland', 16),
('Corning', 16),
('Cottonwood Falls', 16),
('Council Grove', 16),
('Courtland', 16),
('Coyville', 16),
('Cuba', 16),
('Cullison', 16),
('Culver', 16),
('Cunningham', 16),
('Damar', 16),
('Danville', 16),
('De Soto', 16),
('Dearing', 16),
('Deerfield', 16),
('Delia', 16),
('Delphos', 16),
('Denison', 16),
('Denton', 16),
('Derby', 16),
('Dexter', 16),
('Dighton', 16),
('Dodge City', 16),
('Dorrance', 16),
('Douglass', 16),
('Downs', 16),
('Dresden', 16),
('Dunlap', 16),
('Durham', 16),
('Dwight', 16),
('Earlton', 16),
('Eastborough', 16),
('Easton', 16),
('Edgerton', 16),
('Edmond', 16),
('Edna', 16),
('Edwardsville', 16),
('Effingham', 16),
('El Dorado', 16),
('Elbing', 16),
('Elgin', 16),
('Elk City', 16),
('Elk Falls', 16),
('Elkhart', 16),
('Ellinwood', 16),
('Ellis', 16),
('Ellsworth', 16),
('Elmdale', 16),
('Elsmore', 16),
('Elwood', 16),
('Emmett', 16),
('Emporia', 16),
('Englewood', 16),
('Ensign', 16),
('Enterprise', 16),
('Erie', 16),
('Eskridge', 16),
('Eudora', 16),
('Eureka', 16),
('Everest', 16),
('Fairview', 16),
('Fairway', 16),
('Fall River', 16),
('Florence', 16),
('Fontana', 16),
('Ford', 16),
('Formoso', 16),
('Fort Scott', 16),
('Fowler', 16),
('Frankfort', 16),
('Frederick', 16),
('Fredonia', 16),
('Frontenac', 16),
('Fulton', 16),
('Galatia', 16),
('Galena', 16),
('Galesburg', 16),
('Galva', 16),
('Garden City', 16),
('Garden Plain', 16),
('Gardner', 16),
('Garfield', 16),
('Garnett', 16),
('Gas', 16),
('Gaylord', 16),
('Gem', 16),
('Geneseo', 16),
('Geuda Springs', 16),
('Girard', 16),
('Glade', 16),
('Glasco', 16),
('Glen Elder', 16),
('Goddard', 16),
('Goessel', 16),
('Goff', 16),
('Goodland', 16),
('Gorham', 16),
('Gove City', 16),
('Grainfield', 16),
('Grandview Plaza', 16),
('Great Bend', 16),
('Greeley', 16),
('Green', 16),
('Greenleaf', 16),
('Greensburg', 16),
('Grenola', 16),
('Gridley', 16),
('Grinnell', 16),
('Gypsum', 16),
('Haddam', 16),
('Halstead', 16),
('Hamilton', 16),
('Hamlin', 16),
('Hanover', 16),
('Hanston', 16),
('Hardtner', 16),
('Harper', 16),
('Hartford', 16),
('Harveyville', 16),
('Havana', 16),
('Haven', 16),
('Havensville', 16),
('Haviland', 16),
('Hays', 16),
('Haysville', 16),
('Hazelton', 16),
('Healy', 16),
('Hepler', 16),
('Herington', 16),
('Herndon', 16),
('Hesston', 16),
('Hiawatha', 16),
('Highland', 16),
('Hill City', 16),
('Hillsboro', 16),
('Hoisington', 16),
('Holcomb', 16),
('Hollenberg', 16),
('Holton', 16),
('Holyrood', 16),
('Hope', 16),
('Horace', 16),
('Horton', 16),
('Howard', 16),
('Hoxie', 16),
('Hoyt', 16),
('Hudson', 16),
('Hugoton', 16),
('Humboldt', 16),
('Hunnewell', 16),
('Hunter', 16),
('Huron', 16),
('Hutchinson', 16),
('Independence', 16),
('Ingalls', 16),
('Inman', 16),
('Iola', 16),
('Isabel', 16),
('Iuka', 16),
('Jamestown', 16),
('Jennings', 16),
('Jetmore', 16),
('Jewell', 16),
('Johnson City', 16),
('Junction City', 16),
('Kanopolis', 16),
('Kanorado', 16),
('Kansas City', 16),
('Kechi', 16),
('Kensington', 16),
('Kincaid', 16),
('Kingman', 16),
('Kinsley', 16),
('Kiowa', 16),
('Kirwin', 16),
('Kismet', 16),
('Labette', 16),
('La Crosse', 16),
('La Cygne', 16),
('La Harpe', 16),
('Lake Quivira', 16),
('Lakin', 16),
('Lancaster', 16),
('Lane', 16),
('Langdon', 16),
('Lansing', 16),
('Larned', 16),
('Latham', 16),
('Latimer', 16),
('Lawrence', 16),
('Leavenworth', 16),
('Le Roy', 16),
('Leawood', 16),
('Lebanon', 16),
('Lebo', 16),
('Lecompton', 16),
('Lehigh', 16),
('Lenexa', 16),
('Lenora', 16),
('Leon', 16),
('Leona', 16),
('Leonardville', 16),
('Leoti', 16),
('Lewis', 16),
('Liberal', 16),
('Liberty', 16),
('Liebenthal', 16),
('Lincoln Center', 16),
('Lincolnville', 16),
('Lindsborg', 16),
('Linn', 16),
('Linn Valley', 16),
('Linwood', 16),
('Little River', 16),
('Logan', 16),
('Lone Elm', 16),
('Longford', 16),
('Long Island', 16),
('Longton', 16),
('Lorraine', 16),
('Lost Springs', 16),
('Louisburg', 16),
('Louisville', 16),
('Lucas', 16),
('Luray', 16),
('Lyndon', 16),
('Lyons', 16),
('Macksville', 16),
('Madison', 16),
('Mahaska', 16),
('Maize', 16),
('Manchester', 16),
('Manhattan', 16),
('Mankato', 16),
('Manter', 16),
('Maple Hill', 16),
('Mapleton', 16),
('Marion', 16),
('Marquette', 16),
('Marysville', 16),
('Matfield Green', 16),
('Mayetta', 16),
('Mayfield', 16),
('McCracken', 16),
('McCune', 16),
('McDonald', 16),
('McFarland', 16),
('McLouth', 16),
('McPherson', 16),
('Meade', 16),
('Medicine Lodge', 16),
('Melvern', 16),
('Menlo', 16),
('Meriden', 16),
('Merriam', 16),
('Milan', 16),
('Mildred', 16),
('Milford', 16),
('Miltonvale', 16),
('Minneapolis', 16),
('Minneola', 16),
('Mission', 16),
('Mission Hills', 16),
('Mission Woods', 16),
('Moline', 16),
('Montezuma', 16),
('Moran', 16),
('Morganville', 16),
('Morland', 16),
('Morrill', 16),
('Morrowville', 16),
('Moscow', 16),
('Mound City', 16),
('Mound Valley', 16),
('Moundridge', 16),
('Mount Hope', 16),
('Mulberry', 16),
('Mullinville', 16),
('Mulvane', 16),
('Munden', 16),
('Muscotah', 16),
('Narka', 16),
('Nashville', 16),
('Natoma', 16),
('Neodesha', 16),
('Neosho Falls', 16),
('Neosho Rapids', 16),
('Ness City', 16),
('Netawaka', 16),
('New Albany', 16),
('New Cambria', 16),
('New Strawn', 16),
('Newton', 16),
('Nickerson', 16),
('Niotaze', 16),
('Norcatur', 16),
('North Newton', 16),
('Norton', 16),
('Nortonville', 16),
('Norwich', 16),
('Oak Hill', 16),
('Oakley', 16),
('Oberlin', 16),
('Offerle', 16),
('Ogden', 16),
('Oketo', 16),
('Olathe', 16),
('Olivet', 16),
('Olmitz', 16),
('Olpe', 16),
('Olsburg', 16),
('Onaga', 16),
('Oneida', 16),
('Osage City', 16),
('Osawatomie', 16),
('Osborne', 16),
('Oskaloosa', 16),
('Oswego', 16),
('Otis', 16),
('Ottawa', 16),
('Overbrook', 16),
('Overland Park', 16),
('Oxford', 16),
('Ozawkie', 16),
('Palco', 16),
('Palmer', 16),
('Paola', 16),
('Paradise', 16),
('Park', 16),
('Park City', 16),
('Parker', 16),
('Parkerfield', 16),
('Parkerville', 16),
('Parsons', 16),
('Partridge', 16),
('Pawnee Rock', 16),
('Paxico', 16),
('Peabody', 16),
('Penalosa', 16),
('Perry', 16),
('Peru', 16),
('Phillipsburg', 16),
('Pittsburg', 16),
('Plains', 16),
('Plainville', 16),
('Pleasanton', 16),
('Plevna', 16),
('Pomona', 16),
('Portis', 16),
('Potwin', 16),
('Powhattan', 16),
('Prairie View', 16),
('Prairie Village', 16),
('Pratt', 16),
('Prescott', 16),
('Preston', 16),
('Pretty Prairie', 16),
('Princeton', 16),
('Protection', 16),
('Quenemo', 16),
('Quinter', 16),
('Radium', 16),
('Ramona', 16),
('Randall', 16),
('Randolph', 16),
('Ransom', 16),
('Rantoul', 16),
('Raymond', 16),
('Reading', 16),
('Redfield', 16),
('Republic', 16),
('Reserve', 16),
('Rexford', 16),
('Richfield', 16),
('Richmond', 16),
('Riley', 16),
('Robinson', 16),
('Roeland Park', 16),
('Rolla', 16),
('Rose Hill', 16),
('Roseland', 16),
('Rossville', 16),
('Rozel', 16),
('Rush Center', 16),
('Russell', 16),
('Russell Springs', 16),
('Sabetha', 16),
('St. Francis', 16),
('St. George', 16),
('St. John', 16),
('St. Marys', 16),
('St. Paul', 16),
('Salina', 16),
('Satanta', 16),
('Savonburg', 16),
('Sawyer', 16),
('Scammon', 16),
('Scandia', 16),
('Schoenchen', 16),
('Scott City', 16),
('Scottsville', 16),
('Scranton', 16),
('Sedan', 16),
('Sedgwick', 16),
('Selden', 16),
('Seneca', 16),
('Severance', 16),
('Severy', 16),
('Seward', 16),
('Sharon', 16),
('Sharon Springs', 16),
('Shawnee', 16),
('Silver Lake', 16),
('Simpson', 16),
('Smith Center', 16),
('Smolan', 16),
('Soldier', 16),
('Solomon', 16),
('South Haven', 16),
('South Hutchinson', 16),
('Spearville', 16),
('Speed', 16),
('Spivey', 16),
('Spring Hill', 16),
('Stafford', 16),
('Stark', 16),
('Sterling', 16),
('Stockton', 16),
('Strong City', 16),
('Sublette', 16),
('Summerfield', 16),
('Sun City', 16),
('Susank', 16),
('Sylvan Grove', 16),
('Sylvia', 16),
('Syracuse', 16),
('Tampa', 16),
('Tescott', 16),
('Thayer', 16),
('Timken', 16),
('Tipton', 16),
('Tonganoxie', 16),
('Topeka', 16),
('Toronto', 16),
('Towanda', 16),
('Tribune', 16),
('Troy', 16),
('Turon', 16),
('Tyro', 16),
('Udall', 16),
('Ulysses', 16),
('Uniontown', 16),
('Utica', 16),
('Valley Center', 16),
('Valley Falls', 16),
('Vermillion', 16),
('Victoria', 16),
('Vining', 16),
('Viola', 16),
('Virgil', 16),
('WaKeeney', 16),
('Wakefield', 16),
('Waldo', 16),
('Waldron', 16),
('Wallace', 16),
('Walnut', 16),
('Walton', 16),
('Wamego', 16),
('Washington', 16),
('Waterville', 16),
('Wathena', 16),
('Waverly', 16),
('Webber', 16),
('Weir', 16),
('Wellington', 16),
('Wellsville', 16),
('West Mineral', 16),
('Westmoreland', 16),
('Westphalia', 16),
('Westwood', 16),
('Westwood Hills', 16),
('Wetmore', 16),
('Wheaton', 16),
('White City', 16),
('White Cloud', 16),
('Whitewater', 16),
('Whiting', 16),
('Wichita', 16),
('Willard', 16),
('Williamsburg', 16),
('Willis', 16),
('Willowbrook', 16),
('Wilmore', 16),
('Wilsey', 16),
('Wilson', 16),
('Winchester', 16),
('Windom', 16),
('Winfield', 16),
('Winona', 16),
('Woodbine', 16),
('Woodston', 16),
('Yates Center', 16),
('Zenda', 16),
('Zurich', 16),
('Amarillo', 43),
('Oklahoma City', 36),
('Springfield', 25),
('Norfolk', 27),
('Omaha', 27),
('Kearney', 27)
;

/* ******************************************************* END OF CITY TABLE *************************************************************** */
 GO
 
 /*
* Table: [dbo].[CitiesStaging]
* Active Constraints: 
		None
		
* Values: Updated at runtime temporarily to merge new city data into permanent table
*/

CREATE TABLE [dbo].[CitiesStaging]
(
  CityID INT,
  CityName VARCHAR(32) NOT NULL,
  StateID INT NOT NULL
);

GO

/* ******************************************************* END OF CITY STAGING TABLE *************************************************************** */

/*
* Table: [dbo].[Distributor]
* Active Constraints: 
		DistributorName_Unique: Requires uniqueness amoung the distributor name
		
* Values: Nothing, have not recieved list of distributors from client
*/
CREATE TABLE [dbo].[Distributor]
(
  DistributorID INT PRIMARY KEY IDENTITY(1,1),
  DistributorName VARCHAR(128) NOT NULL,
  [Address] VARCHAR(64) NOT NULL,
  CityID INT NOT NULL,
  Zip CHAR(5) NOT NULL,
  YLAT DECIMAL(15,10) NOT NULL,
  XLONG DECIMAL(15,10) NOT NULL

  FOREIGN KEY (CityID) REFERENCES [dbo].[Cities](CityID),
);

GO

ALTER TABLE [dbo].[Distributor]
ADD CONSTRAINT DistributorName_Unique UNIQUE (Address, CityID);

GO

INSERT INTO [dbo].[Distributor]
VALUES
('Affiliated Foods Inc.', '1401 W Farmers Ave', 626, '79118', 35.13251, -101.851),
('Associated Wholesale Grocers Inc.', '5000 Kansas Ave', 289, '66106', 39.09106, -94.6908),
('Associated Wholesale Grocers Inc.', '5600 S Council RD', 627,'73179',35.41111, -97.6509),
('Associated Wholesale Grocers Inc.', '3201 E Division St', 628, '65802', 37.22712, -93.2284),
('Associated Wholesale Grocers Inc.', '1301 Omaha Ave', 629, '68701',42.01638, -97.429),
('Cash-Wa Distributing', '401 W 4th St', 631, '68845', 40.6791, -99.0889),
('Spartan Nash', '7401 F St', 630, '68127', 41.21892, -96.0261);

/* ************************************************** END OF DISTRIBUTORS TABLE ************************************************************ */
 GO

 /*
* Table: [dbo].[DistributorStaging]
* Active Constraints: 
		None
		
* Values: Updated at runtime temporarily with new Distribtor information to be merged into permanent table
*/

CREATE TABLE [dbo].[DistributorStaging]
(
  DistributorID INT,
  DistributorName VARCHAR(128) NOT NULL,
  [Address] VARCHAR(64) NOT NULL,
  CityID INT NOT NULL,
  Zip CHAR(5) NOT NULL,
  YLAT DECIMAL(15,10) NOT NULL,
  XLONG DECIMAL(15,10) NOT NULL
);

GO

/* ************************************************** END OF DISTRIBUTOR STAGING TABLE ************************************************************ */

/*
* Table: [dbo].[SquareFootageCategories]
* Active Constraints: 
		DistributorName_Unique: Requires uniqueness between the categories
		
* Values: A set of categories retrieved from the client
*/
CREATE TABLE [dbo].[SquareFootageCategories]
(
  SquareFootageCategoriesID INT PRIMARY KEY IDENTITY(1,1),
  CategoryName VARCHAR(16) NOT NULL
);

GO

ALTER TABLE [dbo].[SquareFootageCategories]
ADD CONSTRAINT CategoryName_Unique UNIQUE (CategoryName);

GO

INSERT INTO [dbo].[SquareFootageCategories] (CategoryName)
VALUES
('<5000'),
('5,000 - 7,499'),
('7,500 - 9,999'),
('10,000 - 14,999'),
('15,000 - 19,999'),
('>20,000')
;

/* ******************************************* END OF SQUARE FOOTAGE CATEGORIES TABLE ******************************************************* */
 GO

/*
* Table: [dbo].[StoreInformation]
* Active Constraints: 
		None
		
* Values: Populated by users
*/
CREATE TABLE [dbo].[StoreInformation]
(
  StoreID INT PRIMARY KEY IDENTITY(1, 1),
  StoreName VARCHAR(64) NOT NULL,
  [Address] VARCHAR(64) NOT NULL,
  CityID INT NOT NULL,
  Zip CHAR(5) NOT NULL,
  YLAT DECIMAL(15,10) NOT NULL,
  XLONG DECIMAL(15,10) NOT NULL,

  /*SquareFootageCategoriesID INT NULL,*/
  
  FOREIGN KEY (CityID) REFERENCES [dbo].[Cities](CityID),
  /*FOREIGN KEY (SquareFootageCategoriesID) REFERENCES [dbo].[SquareFootageCategories](SquareFootageCategoriesID)*/
);

INSERT INTO [dbo].[StoreInformation]
VALUES
('Venture Foods LLC', '803 Main St', 25, '67831', 37.18845, -99.766),
('Main Street Market', '102 N Main St', 68, '67834', 37.54979, -99.6339),
('K & J Foods', '101 W Long St', 143, '67839', 38.48235, -100.467),
('Hometown Pride Grocery', '1002 Elm St', 248, '67850', 38.60095, -100.617),
('Leoti Foods', '123 N 4th St', 324, '67861', 38.48243, -101.358),
('Meade Thriftway', '922 W Carthage St', 371, '67864', 37.28652, -100.352),
('Home Town Market', '135 S Main St', 382, '67865', 37.44305, -100.014),
('Satanta Groceries', '109 Comanche St', 515, '67870', 37.43935, -100.984),
('Kelly''s Corner Grocery', '301 N Main St', 542, '67876', 37.84905, -99.7563),
('Venture Foods-Sublette', '101 S Inman St', 551, '67877', 37.48206, -100.844),
('Syracuse Food Center', '301 E US-50', 557, '67878', 37.97958, -101.75),
('Gooch''s Foods', '503 Broadway Ave', 567, '67879', 38.47002, -101.753),
('Cimarron Shurfine Foods LLC', '18309 US-50', 102, '67835', 37.80582, -100.335),
('Moore''s Food Pride', '445 Colorado St', 165, '67950', 37.00325, -101.898),
('Bob''s Affiliated Foods', '710 Tampa St', 303, '67860', 37.94387, -101.264),
('Harvest Market', '205 N Aztec St', 387, '67867', 37.59757, -100.443)

GO

/*ALTER TABLE  [dbo].[StoreInformation]
ADD CONSTRAINT StoreInformation_StoreEmailAddress_Unique UNIQUE (StoreEmailAddress);*/
/* ************************************************ END OF STORE INFORMATION TABLE ********************************************************* */

/*
* Table: [dbo].[StoreInformationStaging]
* Active Constraints:
		None
* Values: Populated temporarily via InsertNewStoreInformation in SqlDataAccess. Used to MERGE into StoreInformation table.
*/

CREATE TABLE [dbo].[StoreInformationStaging]
(
  StoreID INT,
  StoreName VARCHAR(64) NOT NULL,
  [Address] VARCHAR(64) NOT NULL,
  CityID INT NOT NULL,
  Zip CHAR(5) NOT NULL,
  YLAT DECIMAL(15,10) NOT NULL,
  XLONG DECIMAL(15,10) NOT NULL,
);

GO

/* ************************************************ END OF STORE INFORMATION STAGING TABLE ********************************************************* */

/*
* Table: [dbo].[StoreEquipmentInformation]
* Active Constraints: 
		None
		
* Values: Populated by users
*/

CREATE TABLE [dbo].[StoreSquareFootage]
(
  StoreSquareFootageID INT PRIMARY KEY IDENTITY(1, 1),
  StoreID INT NOT NULL,

  TotalSquareFootage INT NULL,
  RetailSquareFootage INT NULL,
  StorageSquareFootage INT NULL,
  PercentRetailSquareFootage INT NULL,
  PercentStorageSquareFootage INT NULL,

  NumberOfAiles INT NULL,
  NumberOfCheckoutLanes INT NULL,

  FOREIGN KEY (StoreID) REFERENCES [dbo].[StoreInformation](StoreID)
)

ALTER TABLE [dbo].[StoreSquareFootage]
ADD CONSTRAINT StoreSquareFootage_StoreID_Unique UNIQUE (StoreID);

INSERT INTO [dbo].[StoreSquareFootage]
VALUES
(1, NULL, NULL, NULL, 75, 25, NULL, NULL),
(2, 3500, 3325, 175, 95, 5, 5, 2),
(3, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(4, 5000, NULL, NULL, NULL, NULL, NULL, NULL),
(5, 7000, 5600, 1400, 80, 20, 5, 2),
(6, 20000, 15000, 5000, NULL, NULL, NULL, NULL),
(7, 10000, 9000, 1000, 90, 10, 5, 2),
(8, 6000, 5400, 600, 90, 10, NULL, NULL),
(9, 17500, 10000, 7500, NULL, NULL, 8, 2),
(10, 5000, 3500, 1500, 70, 30, NULL, NULL),
(11, 12000, 10200, 1800, 85, 15, 5, 3),
(12, 6000, 4500, 2500, 75, 25, 8, 2),
(13, 22000, 17000, 5000, 77, 23, 10, 3),
(14, 15000, 13500, 1500, 90, 10, 13, 4),
(15, 4000, 3400, 600, 85, 15, 6, 2),
(16, 5250, 4450,800, 85, 15, 4, 1)
/* ******************************************* END OF STORE SQUARE FOOTAGE INFORMATION TABLE **************************************************** */
 GO


CREATE TABLE [dbo].[StoreEquipmentInformation]
(
  StoreEquipmentInformationID INT PRIMARY KEY IDENTITY(1, 1),
  StoreID INT NOT NULL,

  LoadingDock BIT NULL,
  ForkLift BIT NULL,
  PalletJack BIT NULL,
  
  FOREIGN KEY (StoreID) REFERENCES [dbo].[StoreInformation](StoreID)
);

GO

ALTER TABLE [dbo].[StoreEquipmentInformation]
ADD CONSTRAINT StoreEquipmentInformation_StoreID_Unique UNIQUE (StoreID);

INSERT INTO [dbo].[StoreEquipmentInformation]
VALUES
(1, 0, 1, 1),
(2, NULL, NULL, NULL),
(3, 0, 1, 0),
(4, 0, 0, 1),
(5, 0, 1, 1),
(6, 0, 1, 1),
(7, 1, 1, 1),
(8, 0, 0, 0),
(9, 0, 1, 1),
(10, 0, 1, 0),
(11, 0, 0, 1),
(12, 0, 1, 1),
(13, 1, 0, 1),
(14, 0, 0, 1),
(15, 0, 1, 0),
(16, 0, 1, 1)
/* ******************************************* END OF STORE EQUIPMENT INFORMATION TABLE **************************************************** */
 GO

/*
* Table: [dbo].[StoreDeliverySchedule]
* Active Constraints: 
		None
		
* Values: Populated by users
*/
CREATE TABLE [dbo].[StoreDeliverySchedule]
(
  StoreDeliveryScheduleID INT PRIMARY KEY IDENTITY(1, 1),
  StoreID INT NOT NULL,

  DeliveriesPerWeek INT NULL,
  DeliverySunday BIT NOT NULL,
  DeliveryMonday BIT NOT NULL,
  DeliveryTuesday BIT NOT NULL,
  DeliveryWednesday BIT NOT NULL,
  DeliveryThursday BIT NOT NULL,
  DeliveryFriday BIT NOT NULL,
  DeliverySaturday BIT NOT NULL,
  DeliveryNotes VARCHAR(64) NULL
  
  FOREIGN KEY (StoreID) REFERENCES [dbo].[StoreInformation](StoreID)
);

GO

ALTER TABLE [dbo].[StoreDeliverySchedule]
ADD CONSTRAINT StoreDeliverySchedule_StoreID_Unique UNIQUE (StoreID);

INSERT INTO [dbo].[StoreDeliverySchedule]
VALUES
(1, 2, 0, 1, 0, 0, 0, 1, 0, NULL),
(2, 2, 0, 0, 0, 0, 0, 0, 0, NULL),
(3, 2, 0, 0, 0, 0, 0, 0, 0, NULL),
(4, 1, 0, 0, 0, 0, 0, 0, 0, NULL),
(5, 3, 0, 1, 0, 1, 0, 1, 0, NULL),
(6, 3, 0, 1, 0, 1, 0, 1, 0, '35 minues to unload'),
(7, 2, 0, 1, 0, 0, 0, 1, 0, '6-7 on Mondays, 1-2 Fridays'),
(8, 2, 0, 0, 0, 0, 0, 0, 0, '2 hours to unload'),
(9, 2, 0, 0, 1, 0, 0, 1, 0, NULL),
(10, 2, 0, 0, 1, 0, 0, 1, 0, '8-10 on Tuesdays, 4-5 on Fridays'),
(11, 3, 0, 1, 0, 1, 0, 1, 0, NULL),
(12, 3, 0, 1, 0, 1, 0, 1, 0, NULL),
(13, 2, 0, 1, 0, 0, 1, 0, 0, NULL),
(14, 3, 0, 0, 0, 0, 0, 0, 0, NULL),
(15, 2, 0, 0, 1, 0, 0, 1, 0, '8 on Tuesday, 6 on Friday'),
(16, 2, 0, 0, 0, 0, 0, 0, 0, NULL)
/* ********************************************* END OF STORE DELIVERY SCHEDULE TABLE ****************************************************** */
GO

/*
* Table: [dbo].[StoreDeliveryInformation]
* Active Constraints: 
		None
		
* Values: Populated by users
*/
CREATE TABLE [dbo].[StoreDeliveryInformation]
(
  StoreDeliveryInformationID INT PRIMARY KEY IDENTITY(1, 1),
  StoreID INT NOT NULL,
  DistributorID INT NULL,

  WeeklyPurchaseMinRequirement BIT NULL,
  WeeklyPurchaseAmount INT NULL,
  PalletOrderMinimum VARCHAR(10) NULL,
  PalletOrderMaximum VARCHAR(10) NULL,
  SellToBusinesses BIT NULL,
  OtherBusinesses VARCHAR(100) NULL,
  SplitWithGroceryStore BIT NULL,
  OtherGroceryStore VARCHAR(100) NULL,
  DeliveryNotes VARCHAR(64) NULL
  
  FOREIGN KEY (DistributorID) REFERENCES [dbo].[Distributor] (DistributorID),  
  FOREIGN KEY (StoreID) REFERENCES [dbo].[StoreInformation] (StoreID)
);

GO

ALTER TABLE [dbo].[StoreDeliveryInformation]
ADD CONSTRAINT StoreDeliveryInformation_StoreID_Unique UNIQUE (StoreID);

INSERT INTO [dbo].[StoreDeliveryInformation]
VALUES
(1, 1, 0, 20000, '8', '10', 1, 'Hospital, local restaurants', 1, 'Coldwater, Buffalo, OK' , NULL),
(2, 1, 0, NULL, '4', '5', 1, 'Senior center, school', 0, NULL, NULL),
(3, 1, 0, 2500, NULL, '20', 0, NULL, 0, NULL, 'Varies, say 20'),
(4, 3, NULL, 3500, '2', '3', 0, NULL, 1, 'Scott City Heartland?', NULL),
(5, 1, 0, NULL, '6', '7', 1, 'School, hospital, resturants, other businesses (Christmas dinner)', 1, 'Occasionally with Scott City and Tribune', NULL),
(6, 1, 0, 36000, '12', '12', 0, NULL, 0, NULL, NULL),
(7, 1, 0, NULL, '1', '7', 1, 'Hospital, school', 0, NULL, NULL),
(8, 1, 0, NULL, '8', '12', 1, 'School, hospital', 0, NULL, NULL),
(9, 1, 0, 8500, '4', '6', 1, 'Retirement apartment, restaurants, bar, school', 1, 'Occasionally with Jetmore', NULL),
(10, 1, NULL, NULL, '4-8', '5-10', 1, 'Courhouse, sheriff''s deparment, school', 0, NULL, NULL),
(11, 1, 0, 25000, '9', '12', 1, 'Special order - not regular', 1, 'Sometimes Johnson', NULL),
(12, 1, 0, 20000, '6', '6', 1, 'School (weekly), hospital (2x/week), resturant (as needed), convenience store (as needed)', 1, 'Leoti foods, Baca County Foods in Springfield, CO (all owned by Gooch''s)', NULL),
(13, 1, 0, NULL, '10', '12', 0, NULL, 0, NULL, NULL),
(14, 1, 0, 12000, NULL, '12', 1, 'Special order from schools and hospital', 0, NULL, NULL),
(15, 1, 0, 9000, '6', '8', 1, 'Correctional facility, hosptial', 0, NULL, NULL),
(16, 1, 0, 4500, '3', '9', 1, 'Schools and nursing home', 0, NULL, NULL)
/* ******************************************** END OF STORE DELIVERY INFORMATION TABLE **************************************************** */