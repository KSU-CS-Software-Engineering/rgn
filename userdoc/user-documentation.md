# Rural Grocery Network Web Application - User Documentation
***

Here is a link to the website: https://ruralgrocerynetwork.azurewebsites.net/

When a user first gets on the website, they first encounter an about page, along with buttons to login or register
an account for this application. 

![About Page](img/about-page.png)

### Login/Registration

Clicks one of the buttons on the upper right hand corner of the page to either create an account or login to it.

For registering an account, just enter an email address along with a password, and then enter the password again
to confirm it. We are currenlty working on account verification in order to ensure accounts are created when they
should be. Once you are done entering the information, hit the Register button.

![Register an account](img/register-account.png)

To Login, you just enter the email and password that you provided in the registration. Once it is entered, hit the
Log In Button

![Login page](img/login.png)

Once you login, you should see your email along with a logout button in place of the login and register buttons
on the upper right hand corner of the page.

![Login Successful](img/login-complete.png)


### Web pages

Aside from the about page, we currently have 4 pages, which are called Scenarios, 
Stores, Routes, and Trucks.

Scenarios are used for storing different scenarios for distribution.

![Scenarios Page](img/scenarios-page.png)

Stores allows users to enter store locations and will allow routing of stores, along with importing data from .CSV files.
This page also shows the routing through a map using ArcGIS.

![Stores Page](img/stores-page.png)

![arcGIS map](img/stores-map.png)
