# Rural Grocery Network Web Application - User Documentation
***

Here is a link to the website: https://ruralgrocerynetwork.azurewebsites.net/

When a user first gets on the website, they first encounter an about page, along with buttons to login or register
an account for this application. 

![About Page](img/about-page.PNG)

### Login/Registration
Click one of the buttons on the upper right hand corner of the page to either create an account or login to it.

For registering an account, just enter an email address along with a password, and then enter the password again
to confirm it. We are currenlty working on account verification in order to ensure accounts are created when they
should be. Once you are done entering the information, hit the "Register" button.

![Register an account](img/register-account.PNG)

To Login, you just enter the email and password that you provided in the registration. Once it is entered, hit the
"Log In" Button. Users can also click "forgot password" to change the password through email, or "Register as a new user" to redirect to the registration page. Currently the functionality for recovering/changing passwords through email is a Work-In Progress.

![Login page](img/login.PNG)

Once you login, you should see your email along with a logout button in place of the login and register buttons
on the upper right hand corner of the page.

![Login Successful](img/login-complete.PNG)

### Account Settings
When logged in, click on the email on the upper right hand corner to access Account Settings. To navigate back to the main web pages, just click on the "RuralGroceryNetwork" Banner at the top of the page. Currently we have the following settings for user accounts:

#### Profile information
Users can change username and add/change the phone number for the account.

![Profile Setting](img/profile.PNG)

#### Email
This is where users can see the email associated with their account, as well as change it to a different email address. Verification
emails can also be sent as well.

![Email Setting](img/email.PNG)

#### Password
This is where users can change the password for their account. They do need to enter their previous password in this page in order to change it. 

![Password Setting](img/password.PNG)

#### Two-Step Authentication
Users can add an app to do Two-Step Authentication with. After clicking the "set up authenticator app" button, you get steps to add an app like Microsoft Authenticator, using QR Code generation or through a key entry that is provided in this page. Users can also reset their authentication key, for when they want to stop using it or want to switch authentication apps.

![2 Step Authentication](img/authentication.PNG)

![Add Authentication](img/add-authentication.PNG)

#### Personal Data
Any personal data associated with users can either be downloaded or deleted through this page. Data is downloaded as a JSON file. Note that deleting data does delete the account, but it needs password confirmation in order to accomplish it.

![Personal Data](img/personal-data.PNG)

### Web pages
Aside from the about page, we currently have 4 pages, which are called Scenarios, 
Stores, Routes, and Trucks.

#### Scenarios Page
Scenarios are used for storing different scenarios for distribution. It allows user to name and describe scenarios, which will be saved
with the User. This page also has a form to enter arcGIS information, since the mapping portion requires a arcGIS account/license to use. Scenario Saving is currently implemented, but otherwise this page is still a Work-In-Progress.

![Scenarios Page](img/scenarios-page.PNG)

#### Stores Page
Stores allows users to enter store locations and will allow routing between stores, along with importing store data from .CSV files.
This page also shows the routing through a map using ArcGIS. Currently the map can allow clicking on to place markers, but otherwise
the route drawing, file importing, and field entries are still a Work-In Progress.

![Stores Page](img/stores-page.PNG)

![arcGIS map](img/stores-map.PNG)

#### Routes Page
Routes are where the calculations take place and will calculate the optimal route to take for distribution. Hit the "Calculate" Button to run the calculation. This functionality is still a Work-In Progress.

![Routes Page](img/routes-page.PNG)

#### Trucks Page
Trukcs are where truck information can be, such as the start and end location for the truck, as well as the truck name and carrying capacity. Hit "Add Truck" to save the truck information. This functionality is still a Work-In Progress.

![Trucks Page](img/trucks-page.PNG)

