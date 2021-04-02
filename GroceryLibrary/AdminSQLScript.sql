/*Insert Admin into Users*/
INSERT INTO dbo.AspNetUsers
VALUES ('66902402-27f6-4168-aa67-e91fbb0e564d', 'RGI@Admin', 'RGI@ADMIN', 'RGI@Admin', 'RGI@ADMIN', 0, 'AQAAAAEAACcQAAAAEHj8IcHL9g1dxgwBhJ4QTQtbfXgG4e9i0gHuMf+/m6h2Dr4HIG1yHZ9CUWexTyxEqA==', 'EZHXA7PD4WBZQDO7U4SEWNZ5NDJDKZPY', 'd546548b-d1bf-4a6c-9454-56b7bac7698e', NULL, 0, 0, NULL, 1, 0);

/*Insert an Admin role into Roles*/
INSERT INTO dbo.AspNetRoles
VALUES (1, 'Admin', 'ADMIN', 'g1593l10-gh58-8264-91h3-b883d6il14g5');

/*Give added Admin User the Admin role*/
INSERT INTO dbo.AspNetUserRoles
VALUES ('66902402-27f6-4168-aa67-e91fbb0e564d', 1);
