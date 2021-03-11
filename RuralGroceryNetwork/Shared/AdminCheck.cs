/*Copyright 2020 Kansas State University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

using System.Text;
using System.IO;
using System.Data;
using System.Configuration;
using System.Data.SqlClient;

using GroceryLibrary.Models;
using GroceryLibrary.Models.Database;

/*NOTE:
 * This only works if an info is added to the dbo.AspNetUserRoles and dbo.AspNetRoles tables for an admin manually in sql. 
 * As it is, the file thinks the Admin Role's RoleId is 1*/

namespace RuralGroceryNetwork.Shared
{
    /*This class is called from NavMenu.razor with a given string containing a user name and will 
     return a bool, true if the user is an admin otherwise false. It does this by comparing the userId with the 
     adminId. As of right now the class assumes their is only one admin login.*/
    public class AdminCheck
    {
        private string AdminId = null;
        private bool isAdmin = false;
        /*The method called from NavMenu, it calls all the other methods. It finds the userId for the Admin and 
         * calls FindUserId to find the users id and compare the ids. It returns the 
         * bool for if the user is an admin.*/
        public bool IsAdmin(string userName)
        {
            //connection string to connect to a sql database server.
            string connString = @"Server =(local); Database = RuralGrocery; Trusted_Connection = True;";
            string adminId, adminRole, userId;

            //Trys to get a connection to the sql server and and find the Admin userId for the site.
            try
            {
                //sql connection object
                using (SqlConnection conn = new SqlConnection(connString))
                {
                    /*sql query for finding data on the admin.
                     * it assumes the RoleId for an Admin is 1*/
                    string query = @"SELECT *
                                     FROM dbo.AspNetUserRoles
                                     WHERE RoleId = 1";

                    SqlCommand cmd = new SqlCommand(query, conn);

                    conn.Open();

                    //execute the SQLCommand
                    SqlDataReader dr = cmd.ExecuteReader();

                    if (dr.HasRows)
                    {
                        while (dr.Read())
                        {
                            adminId = dr.GetString(0);
                            adminRole = dr.GetString(1);
                            SetAdmin(adminId);
                        }
                    }
                    //close data reader
                    dr.Close();
                    //close connection
                    conn.Close();
                }
            }
            catch
            {
                return false;
            }
            return FindUserId(userName);
        }

        /*Searches through database to find the userId of the given username and compares it to the AdminId. 
         * Returns a bool saying if the given user is the Admin*/
        private bool FindUserId (string name)
        {
            //connection string to connect to a sql database server.
            string connString = @"Server =(local); Database = RuralGrocery; Trusted_Connection = True;";
            string userId;

            //Trys to get a connection to the sql server and and find the userId.
            try
            {
                //sql connection object
                using (SqlConnection conn = new SqlConnection(connString))
                {
                    //sql query for finding the Id of given user.
                    string query = @"SELECT Id
                                     FROM dbo.AspNetUsers
                                     WHERE UserName = '" + name +"'";

                    SqlCommand cmd = new SqlCommand(query, conn);

                    conn.Open();

                    //execute the SQLCommand
                    SqlDataReader dr = cmd.ExecuteReader();

                    if (dr.HasRows)
                    {
                        while (dr.Read())
                        {
                            userId = dr.GetString(0);
                            //checks if found id is the same as AdminId
                            if (userId  == AdminId)
                            {
                                isAdmin = true;
                            }
                        }
                    }
                    //close data reader
                    dr.Close();
                    //close connection
                    conn.Close();
                }
            }
            catch
            {
                return false;
            }
            return isAdmin;
        }

        //Simple sets the given string to be the value of AdminId
        private void SetAdmin(string a)
        {
            AdminId = a;
        }
    }
}
