using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Text;
using System.IO;


namespace RuralGroceryNetworkLibrary
{
    public static class SqlDataAccess
    {
        public static void SomeMethod()
        {
            string server, User_ID, Password;
            
            try
            {
                string[] env = new string[3];
                using (StreamReader sr = new StreamReader("C:\\Users\\Zachery Brunner\\Documents\\Senior Year First Semester\\CIS 642\\RuralGroceryRepo\\rgn\\RuralGroceryNetwork\\RuralGroceryNetworkLibrary\\evironment.txt"))
                {
                    int i = 0;
                    string s;
                    while((s = sr.ReadLine()) != null)
                    {
                        env[i] = s;
                        i++;
                    }
                    
                }

                server = env[0];
                User_ID = env[1];
                Password = env[2];

            }
            catch(Exception e)
            { 
                throw new Exception("Could not find and read file");
            }

   
            try 
            {
                SqlConnectionStringBuilder builder = new SqlConnectionStringBuilder();
                builder.DataSource = server;
                builder.UserID = User_ID;
                builder.Password = Password;
                builder.InitialCatalog = "RuralGroceryNetwork";

                using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
                {
                    Console.WriteLine("\nQuery data example:");
                    Console.WriteLine("=========================================\n");

                    StringBuilder sb = new StringBuilder();
                    sb.Append("SELECT TOP 20 pc.Name as CategoryName, p.name as ProductName ");
                    sb.Append("FROM [SalesLT].[ProductCategory] pc ");
                    sb.Append("JOIN [SalesLT].[Product] p ");
                    sb.Append("ON pc.productcategoryid = p.productcategoryid;");
                    String sql = sb.ToString();

                    using (SqlCommand command = new SqlCommand(sql, connection))
                    {
                        connection.Open();
                        using (SqlDataReader reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                Console.WriteLine("{0} {1}", reader.GetString(0), reader.GetString(1));
                            }
                        }
                    }
                }
            }
            catch (SqlException e)
            {
                Console.WriteLine(e.ToString());
            }
            Console.ReadLine();
        }
    }
}

