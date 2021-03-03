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
  [Population] INT NOT NULL,
  
  FOREIGN KEY (StateID) REFERENCES [dbo].[States](StateID)
);

GO

ALTER TABLE [dbo].[Cities]
ADD CONSTRAINT CityState_Unique UNIQUE (CityName, StateID);

GO

INSERT INTO [dbo].[Cities] (CityName, StateID, [Population])
VALUES
('Abbyville', 16, 72),
('Abilene', 16, 6362),
('Admire', 16, 100),
('Agenda', 16, 61),
('Agra', 16, 206),
('Albert', 16, 185),
('Alden', 16, 121),
('Alexander', 16, 74),
('Allen', 16, 168),
('Alma', 16, 899),
('Almena', 16, 430),
('Alta Vista', 16, 491),
('Altamont', 16, 1144),
('Alton', 16, 88),
('Altoona', 16, 253),
('Americus', 16, 908),
('Andale', 16, 934),
('Andover', 16, 13062),
('Anthony', 16, 2193),
('Arcadia', 16, 364),
('Argonia', 16, 521),
('Arkansas City', 16, 11868),
('Arlington', 16, 528),
('Arma', 16, 1385),
('Ashland', 16, 694),
('Assaria', 16, 438),
('Atchison', 16, 10598),
('Athol', 16, 34),
('Atlanta', 16, 167),
('Attica', 16, 682),
('Atwood', 16, 1109),
('Auburn', 16, 1178),
('Augusta', 16, 9368),
('Aurora', 16, 84),
('Axtell', 16, 390),
('Baldwin City', 16, 4670),
('Barnard', 16, 52),
('Barnes', 16, 159),
('Bartlett', 16, 69),
('Basehor', 16, 5951),
('Bassett', 16, 13),
('Baxter Springs', 16, 3983),
('Bazine', 16, 340),
('Beattie', 16, 228),
('Bel Aire', 16, 7858),
('Belle Plaine', 16, 1511),
('Belleville', 16, 1927),
('Beloit', 16, 3793),
('Belpre', 16, 108),
('Belvue', 16, 325),
('Benedict', 16, 102),
('Bennington', 16, 833),
('Bentley', 16, 487),
('Benton', 16, 1013),
('Bern', 16, 170),
('Beverly', 16, 125),
('Bird City', 16, 536),
('Bison', 16, 167),
('Blue Mound', 16, 303),
('Blue Rapids', 16, 1247),
('Bluff City', 16, 111),
('Bogue', 16, 250),
('Bonner Springs', 16, 7782),
('Brewster', 16, 269),
('Bronson', 16, 258),
('Brookville', 16, 191),
('Brownell', 16, 60),
('Bucklin', 16, 715),
('Buffalo', 16, 292),
('Buhler', 16, 1111),
('Bunker Hill', 16, 144),
('Burden', 16, 666),
('Burdett', 16, 503),
('Burlingame', 16, 885),
('Burlington', 16, 2560),
('Burns', 16, 296),
('Burr Oak', 16, 164),
('Burrton', 16, 907),
('Bushong', 16, 9),
('Bushton', 16, 322),
('Byers', 16, 15),
('Caldwell', 16, 1006),
('Cambridge', 16, 151),
('Caney', 16, 1827),
('Canton', 16, 627),
('Carbondale', 16, 1438),
('Carlton', 16, 12),
('Cassoday', 16, 149),
('Cawker City', 16, 581),
('Cedar', 16, 25),
('Cedar Point', 16, 2),
('Cedar Vale', 16, 638),
('Centralia', 16, 519),
('Chanute', 16, 9102),
('Chapman', 16, 1213),
('Chase', 16, 401),
('Chautauqua', 16, 49),
('Cheney', 16, 2014),
('Cherokee', 16, 849),
('Cherryvale', 16, 2330),
('Chetopa', 16, 1238),
('Cimarron', 16, 2293),
('Circleville', 16, 186),
('Claflin', 16, 406),
('Clay Center', 16, 4026),
('Clayton', 16, 61),
('Clearwater', 16, 2520),
('Clifton', 16, 367),
('Climax', 16, 43),
('Clyde', 16, 912),
('Coats', 16, 81),
('Coffeyville', 16, 9457),
('Colby', 16, 5473),
('Coldwater', 16, 832),
('Collyer', 16, 79),
('Colony', 16, 487),
('Columbus', 16, 3086),
('Colwich', 16, 1270),
('Concordia', 16, 5070),
('Conway Springs', 16, 1483),
('Coolidge', 16, 171),
('Copeland', 16, 318),
('Corning', 16, 188),
('Cottonwood Falls', 16, 785),
('Council Grove', 16, 2260),
('Courtland', 16, 313),
('Coyville', 16, 33),
('Cuba', 16, 266),
('Cullison', 16, 82),
('Culver', 16, 111),
('Cunningham', 16, 403),
('Damar', 16, 153),
('Danville', 16, 35),
('De Soto', 16, 6254),
('Dearing', 16, 536),
('Deerfield', 16, 769),
('Delia', 16, 157),
('Delphos', 16, 371),
('Denison', 16, 133),
('Denton', 16, 164),
('Derby', 16, 24067),
('Dexter', 16, 275),
('Dighton', 16, 873),
('Dodge City', 16, 27555),
('Dorrance', 16, 220),
('Douglass', 16, 1780),
('Downs', 16, 752),
('Dresden', 16, 42),
('Dunlap', 16, 23),
('Durham', 16, 85),
('Dwight', 16, 205),
('Earlton', 16, 124),
('Eastborough', 16, 743),
('Easton', 16, 380),
('Edgerton', 16, 1713),
('Edmond', 16, 104),
('Edna', 16, 417),
('Edwardsville', 16, 4492),
('Effingham', 16, 509),
('El Dorado', 16, 12988),
('Elbing', 16, 335),
('Elgin', 16, 75),
('Elk City', 16, 288),
('Elk Falls', 16, 86),
('Elkhart', 16, 1533),
('Ellinwood', 16, 2063),
('Ellis', 16, 2047),
('Ellsworth', 16, 3013),
('Elmdale', 16, 28),
('Elsmore', 16, 87),
('Elwood', 16, 842),
('Emmett', 16, 171),
('Emporia', 16, 24607),
('Englewood', 16, 91),
('Ensign', 16, 140),
('Enterprise', 16, 953),
('Erie', 16, 1444),
('Eskridge', 16, 540),
('Eudora', 16, 6602),
('Eureka', 16, 2262),
('Everest', 16, 290),
('Fairview', 16, 326),
('Fairway', 16, 3960),
('Fall River', 16, 111),
('Florence', 16, 451),
('Fontana', 16, 320),
('Ford', 16, 221),
('Formoso', 16, 161),
('Fort Scott', 16, 7742),
('Fowler', 16, 509),
('Frankfort', 16, 754),
('Frederick', 16, 7),
('Fredonia', 16, 2141),
('Frontenac', 16, 3400),
('Fulton', 16, 112),
('Galatia', 16, 29),
('Galena', 16, 2892),
('Galesburg', 16, 190),
('Galva', 16, 1015),
('Garden City', 16, 26647),
('Garden Plain', 16, 807),
('Gardner', 16, 21528),
('Garfield', 16, 150),
('Garnett', 16, 3234),
('Gas', 16, 510),
('Gaylord', 16, 164),
('Gem', 16, 142),
('Geneseo', 16, 264),
('Geuda Springs', 16, 120),
('Girard', 16, 2707),
('Glade', 16, 30),
('Glasco', 16, 489),
('Glen Elder', 16, 438),
('Goddard', 16, 4330),
('Goessel', 16, 639),
('Goff', 16, 100),
('Goodland', 16, 4315),
('Gorham', 16, 365),
('Gove City', 16, 86),
('Grainfield', 16, 328),
('Grandview Plaza', 16, 1191),
('Great Bend', 16, 15358),
('Greeley', 16, 324),
('Green', 16, 173),
('Greenleaf', 16, 400),
('Greensburg', 16, 863),
('Grenola', 16, 170),
('Gridley', 16, 352),
('Grinnell', 16, 269),
('Gypsum', 16, 464),
('Haddam', 16, 88),
('Halstead', 16, 2277),
('Hamilton', 16, 259),
('Hamlin', 16, 79),
('Hanover', 16, 674),
('Hanston', 16, 300),
('Hardtner', 16, 220),
('Harper', 16, 1360),
('Hartford', 16, 369),
('Harveyville', 16, 307),
('Havana', 16, 85),
('Haven', 16, 1166),
('Havensville', 16, 125),
('Haviland', 16, 750),
('Hays', 16, 20899),
('Haysville', 16, 11132),
('Hazelton', 16, 117),
('Healy', 16, 223),
('Hepler', 16, 158),
('Herington', 16, 2147),
('Herndon', 16, 139),
('Hesston', 16, 3803),
('Hiawatha', 16, 3147),
('Highland', 16, 1152),
('Hill City', 16, 1629),
('Hillsboro', 16, 2839),
('Hoisington', 16, 2840),
('Holcomb', 16, 2188),
('Hollenberg', 16, 22),
('Holton', 16, 3237),
('Holyrood', 16, 506),
('Hope', 16, 400),
('Horace', 16, 80),
('Horton', 16, 1750),
('Howard', 16, 602),
('Hoxie', 16, 1197),
('Hoyt', 16, 555),
('Hudson', 16, 99),
('Hugoton', 16, 4226),
('Humboldt', 16, 1880),
('Hunnewell', 16, 87),
('Hunter', 16, 53),
('Huron', 16, 77),
('Hutchinson', 16, 40914),
('Independence', 16, 8698),
('Ingalls', 16, 259),
('Inman', 16, 1406),
('Iola', 16, 5351),
('Isabel', 16, 115),
('Iuka', 16, 188),
('Jamestown', 16, 289),
('Jennings', 16, 84),
('Jetmore', 16, 962),
('Jewell', 16, 397),
('Johnson City', 16, 1142),
('Junction City', 16, 23104),
('Kanopolis', 16, 574),
('Kanorado', 16, 224),
('Kansas City', 16, 152522),
('Kechi', 16, 2745),
('Kensington', 16, 488),
('Kincaid', 16, 108),
('Kingman', 16, 2928),
('Kinsley', 16, 1515),
('Kiowa', 16, 840),
('Kirwin', 16, 166),
('Kismet', 16, 367),
('Labette', 16, 57),
('La Crosse', 16, 1232),
('La Cygne', 16, 1186),
('La Harpe', 16, 506),
('Lake Quivira', 16, 920),
('Lakin', 16, 1780),
('Lancaster', 16, 245),
('Lane', 16, 179),
('Langdon', 16, 34),
('Lansing', 16, 11900),
('Larned', 16, 3813),
('Latham', 16, 125),
('Latimer', 16, 31),
('Lawrence', 16, 96369),
('Leavenworth', 16, 36064),
('Le Roy', 16, 617),
('Leawood', 16, 34670),
('Lebanon', 16, 252),
('Lebo', 16, 804),
('Lecompton', 16, 749),
('Lehigh', 16, 255),
('Lenexa', 16, 54011),
('Lenora', 16, 345),
('Leon', 16, 614),
('Leona', 16, 48),
('Leonardville', 16, 464),
('Leoti', 16, 1582),
('Lewis', 16, 393),
('Liberal', 16, 19731),
('Liberty', 16, 87),
('Liebenthal', 16, 164),
('Lincoln Center', 16, 1266),
('Lincolnville', 16, 189),
('Lindsborg', 16, 3301),
('Linn', 16, 354),
('Linn Valley', 16, 746),
('Linwood', 16, 484),
('Little River', 16, 446),
('Logan', 16, 602),
('Lone Elm', 16, 10),
('Longford', 16, 138),
('Long Island', 16, 154),
('Longton', 16, 340),
('Lorraine', 16, 139),
('Lost Springs', 16, 115),
('Louisburg', 16, 4443),
('Louisville', 16, 174),
('Lucas', 16, 370),
('Luray', 16, 212),
('Lyndon', 16, 1058),
('Lyons', 16, 3564),
('Macksville', 16, 500),
('Madison', 16, 851),
('Mahaska', 16, 56),
('Maize', 16, 4579),
('Manchester', 16, 130),
('Manhattan', 16, 55290),
('Mankato', 16, 829),
('Manter', 16, 220),
('Maple Hill', 16, 592),
('Mapleton', 16, 116),
('Marion', 16, 2046),
('Marquette', 16, 801),
('Marysville', 16, 3281),
('Matfield Green', 16, 52),
('Mayetta', 16, 315),
('Mayfield', 16, 91),
('McCracken', 16, 141),
('McCune', 16, 406),
('McDonald', 16, 176),
('McFarland', 16, 332),
('McLouth', 16, 1237),
('McPherson', 16, 13054),
('Meade', 16, 1466),
('Medicine Lodge', 16, 1725),
('Melvern', 16, 400),
('Menlo', 16, 49),
('Meriden', 16, 792),
('Merriam', 16, 11185),
('Milan', 16, 73),
('Mildred', 16, 15),
('Milford', 16, 283),
('Miltonvale', 16, 586),
('Minneapolis', 16, 1795),
('Minneola', 16, 788),
('Mission', 16, 9523),
('Mission Hills', 16, 3574),
('Mission Woods', 16, 186),
('Moline', 16, 399),
('Montezuma', 16, 885),
('Moran', 16, 394),
('Morganville', 16, 177),
('Morland', 16, 177),
('Morrill', 16, 208),
('Morrowville', 16, 145),
('Moscow', 16, 245),
('Mound City', 16, 1088),
('Mound Valley', 16, 337),
('Moundridge', 16, 2222),
('Mount Hope', 16, 749),
('Mulberry', 16, 378),
('Mullinville', 16, 253),
('Mulvane', 16, 6045),
('Munden', 16, 109),
('Muscotah', 16, 155),
('Narka', 16, 109),
('Nashville', 16, 41),
('Natoma', 16, 273),
('Neodesha', 16, 2083),
('Neosho Falls', 16, 104),
('Neosho Rapids', 16, 280),
('Ness City', 16, 1571),
('Netawaka', 16, 164),
('New Albany', 16, 40),
('New Cambria', 16, 74),
('New Strawn', 16, 408),
('Newton', 16, 18877),
('Nickerson', 16, 1015),
('Niotaze', 16, 65),
('Norcatur', 16, 323),
('North Newton', 16, 1778),
('Norton', 16, 2766),
('Nortonville', 16, 696),
('Norwich', 16, 511),
('Oak Hill', 16, 59),
('Oakley', 16, 2090),
('Oberlin', 16, 1622),
('Offerle', 16, 283),
('Ogden', 16, 1600),
('Oketo', 16, 75),
('Olathe', 16, 137618),
('Olivet', 16, 50),
('Olmitz', 16, 71),
('Olpe', 16, 577),
('Olsburg', 16, 239),
('Onaga', 16, 751),
('Oneida', 16, 167),
('Osage City', 16, 2808),
('Osawatomie', 16, 4293),
('Osborne', 16, 1575),
('Oskaloosa', 16, 1344),
('Oswego', 16, 1676),
('Otis', 16, 218),
('Ottawa', 16, 12260),
('Overbrook', 16, 800),
('Overland Park', 16, 191011),
('Oxford', 16, 1078),
('Ozawkie', 16, 759),
('Palco', 16, 157),
('Palmer', 16, 146),
('Paola', 16, 5611),
('Paradise', 16, 29),
('Park', 16, 129),
('Park City', 16, 8138),
('Parker', 16, 503),
('Parkerfield', 16, 389),
('Parkerville', 16, 64),
('Parsons', 16, 9736),
('Partridge', 16, 188),
('Pawnee Rock', 16, 310),
('Paxico', 16, 280),
('Peabody', 16, 1083),
('Penalosa', 16, 2),
('Perry', 16, 949),
('Peru', 16, 203),
('Phillipsburg', 16, 2583),
('Pittsburg', 16, 20171),
('Plains', 16, 1161),
('Plainville', 16, 1987),
('Pleasanton', 16, 1409),
('Plevna', 16, 113),
('Pomona', 16, 1232),
('Portis', 16, 144),
('Potwin', 16, 466),
('Powhattan', 16, 74),
('Prairie View', 16, 109),
('Prairie Village', 16, 22170),
('Pratt', 16, 6703),
('Prescott', 16, 261),
('Preston', 16, 163),
('Pretty Prairie', 16, 514),
('Princeton', 16, 237),
('Protection', 16, 340),
('Quenemo', 16, 382),
('Quinter', 16, 917),
('Radium', 16, 20),
('Ramona', 16, 82),
('Randall', 16, 123),
('Randolph', 16, 173),
('Ransom', 16, 329),
('Rantoul', 16, 185),
('Raymond', 16, 86),
('Reading', 16, 219),
('Redfield', 16, 124),
('Republic', 16, 101),
('Reserve', 16, 50),
('Rexford', 16, 154),
('Richfield', 16, 30),
('Richmond', 16, 596),
('Riley', 16, 1421),
('Robinson', 16, 202),
('Roeland Park', 16, 6769),
('Rolla', 16, 622),
('Rose Hill', 16, 4038),
('Roseland', 16, 84),
('Rossville', 16, 1248),
('Rozel', 16, 119),
('Rush Center', 16, 227),
('Russell', 16, 4525),
('Russell Springs', 16, 21),
('Sabetha', 16, 2552),
('St. Francis', 16, 1421),
('St. George', 16, 914),
('St. John', 16, 1390),
('St. Marys', 16, 2714),
('St. Paul', 16, 575),
('Salina', 16, 46998),
('Satanta', 16, 1078),
('Savonburg', 16, 69),
('Sawyer', 16, 157),
('Scammon', 16, 574),
('Scandia', 16, 454),
('Schoenchen', 16, 170),
('Scott City', 16, 3970),
('Scottsville', 16, 31),
('Scranton', 16, 585),
('Sedan', 16, 859),
('Sedgwick', 16, 1467),
('Selden', 16, 272),
('Seneca', 16, 2084),
('Severance', 16, 116),
('Severy', 16, 223),
('Seward', 16, 47),
('Sharon', 16, 147),
('Sharon Springs', 16, 882),
('Shawnee', 16, 65540),
('Silver Lake', 16, 1395),
('Simpson', 16, 58),
('Smith Center', 16, 1477),
('Smolan', 16, 197),
('Soldier', 16, 83),
('Solomon', 16, 1166),
('South Haven', 16, 388),
('South Hutchinson', 16, 2499),
('Spearville', 16, 925),
('Speed', 16, 41),
('Spivey', 16, 31),
('Spring Hill', 16, 6626),
('Stafford', 16, 939),
('Stark', 16, 74),
('Sterling', 16, 2596),
('Stockton', 16, 1525),
('Strong City', 16, 480),
('Sublette', 16, 1420),
('Summerfield', 16, 113),
('Sun City', 16, 40),
('Susank', 16, 30),
('Sylvan Grove', 16, 296),
('Sylvia', 16, 115),
('Syracuse', 16, 1833),
('Tampa', 16, 146),
('Tescott', 16, 293),
('Thayer', 16, 391),
('Timken', 16, 79),
('Tipton', 16, 212),
('Tonganoxie', 16, 5359),
('Topeka', 16, 126397),
('Toronto', 16, 246),
('Towanda', 16, 1311),
('Tribune', 16, 804),
('Troy', 16, 892),
('Turon', 16, 311),
('Tyro', 16, 163),
('Udall', 16, 695),
('Ulysses', 16, 5712),
('Uniontown', 16, 261),
('Utica', 16, 104),
('Valley Center', 16, 7176),
('Valley Falls', 16, 1039),
('Vermillion', 16, 73),
('Victoria', 16, 1019),
('Vining', 16, 73),
('Viola', 16, 96),
('Virgil', 16, 43),
('WaKeeney', 16, 1949),
('Wakefield', 16, 956),
('Waldo', 16, 53),
('Waldron', 16, 28),
('Wallace', 16, 54),
('Walnut', 16, 183),
('Walton', 16, 315),
('Wamego', 16, 4876),
('Washington', 16, 1069),
('Waterville', 16, 635),
('Wathena', 16, 1547),
('Waverly', 16, 650),
('Webber', 16, 25),
('Weir', 16, 659),
('Wellington', 16, 7788),
('Wellsville', 16, 2026),
('West Mineral', 16, 129),
('Westmoreland', 16, 763),
('Westphalia', 16, 143),
('Westwood', 16, 1657),
('Westwood Hills', 16, 463),
('Wetmore', 16, 303),
('Wheaton', 16, 84),
('White City', 16, 621),
('White Cloud', 16, 116),
('Whitewater', 16, 778),
('Whiting', 16, 207),
('Wichita', 16, 389877),
('Willard', 16, 81),
('Williamsburg', 16, 388),
('Willis', 16, 28),
('Willowbrook', 16, 81),
('Wilmore', 16, 40),
('Wilsey', 16, 166),
('Wilson', 16, 708),
('Winchester', 16, 578),
('Windom', 16, 89),
('Winfield', 16, 12085),
('Winona', 16, 237),
('Woodbine', 16, 209),
('Woodston', 16, 108),
('Yates Center', 16, 1381),
('Zenda', 16, 70),
('Zurich', 16, 85),
('Amarillo', 43, 198955),
('Oklahoma City', 36, 643692),
('Springfield', 25, 167051),
('Norfolk', 27, 24424),
('Omaha', 27, 475862),
('Kearney', 27, 33464),
('Ada', 16, 160),
('Asherville', 16, 0),
('Baileyville', 16, 201),
('Bendena', 16, 134),
('Bucyrus', 16, 322),
('Catharine', 16, 130),
('Chicopee', 16, 565),
('Detroit', 16, 178),
('Esbon', 16, 99),
('Falun', 16, 84),
('Fort Dodge', 16, 168),
('Fort Riley', 16, 6368),
('Franklin', 16, 590),
('Freeport', 16, 0),
('Grantville', 16, 201),
('Greeley County unified government', 16, 301),
('Harris', 16, 8),
('Hillsdale', 16, 311),
('Home', 16, 128),
('Kickapoo Site 1', 16, 68),
('Kickapoo Site 2', 16, 70),
('Kickapoo Site 5', 16, 44),
('Kickapoo Site 6', 16, 35),
('Kickapoo Site 7', 16, 43),
('Kickapoo Tribal Center', 16, 158),
('Kipp', 16, 0),
('Levant', 16, 11),
('Lowell', 16, 349),
('McConnell AFB', 16, 1532),
('Marienthal', 16, 52),
('Milton', 16, 184),
('Munjor', 16, 193),
('Oaklawn-Sunview', 16, 3441),
('Odin', 16, 124),
('Piqua', 16, 81),
('Riverton', 16, 741),
('Rosalia', 16, 108),
('Roxbury', 16, 16),
('Talmage', 16, 112),
('The Highlands', 16, 380),
('Vassar', 16, 799),
('Wakarusa', 16, 226),
('Welda', 16, 61),
('Weskan', 16, 138),
('Wilroads Gardens', 16, 446),
('Wright', 16, 97),
('Yoder', 16, 179)
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