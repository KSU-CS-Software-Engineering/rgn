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

namespace RuralGroceryNetwork.Shared
{
    public class AdminCheck
    {
        private string AdminId = null;
        private bool isAdmin = false;
        public bool IsAdmin(string userName)
        {

            string connString = @"Server =(local); Database = RuralGrocery; Trusted_Connection = True;";
            string adminId, adminRole, userId;

            try
            {
                //sql connection object
                using (SqlConnection conn = new SqlConnection(connString))
                {
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

        private bool FindUserId (string name)
        {
            string connString = @"Server =(local); Database = RuralGrocery; Trusted_Connection = True;";
            string userId;

            try
            {
                //sql connection object
                using (SqlConnection conn = new SqlConnection(connString))
                {
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

        private void SetAdmin(string a)
        {
            AdminId = a;
        }
    }
}
