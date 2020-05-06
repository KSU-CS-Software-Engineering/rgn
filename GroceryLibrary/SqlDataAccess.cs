using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Text;
using System.IO;

using GroceryLibrary.Models;
using GroceryLibrary.Models.Database;


namespace GroceryLibrary
{
    public static class SqlDataAccess
    {
        private static Store store = new Store();

        public static void OnInitialize(string email)
        {
            string server = "ruralgrocery.database.windows.net";
            string User_ID = "ruralgrocerynetwork";
            string Password = "09UCWWtV!vy3b^WrTZo$";

            try 
            {
                SqlConnectionStringBuilder builder = new SqlConnectionStringBuilder();
                builder.DataSource = server;
                builder.UserID = User_ID;
                builder.Password = Password;
                builder.InitialCatalog = "RuralGroceryNetwork";

                using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
                {
                    connection.Open();
                    getStoreInformation(connection, email);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
            Console.ReadLine();
        }

        private static void getStoreInformation(SqlConnection conn, string email)
        {
            bool didRead = false;
            StringBuilder sb = new StringBuilder();


            sb.Append("SELECT * FROM " + DatabaseTables.STORE_INFORMATION);
            sb.Append("WHERE " + StoreInformationTable.STORE_EMAIL_ADDRESS + "= '" + email + "'");

            string sqlCommand = sb.ToString();
            using (SqlCommand command = new SqlCommand(sqlCommand, conn))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        didRead = true;
                        int gs1 = reader.GetInt32(0);
                        string gs2 = reader.GetString(1);
                        int gs3 = reader.GetInt32(2);
                    }
                }
            }
            if (!didRead)
            {
                CreateNewDatabaseEntries(conn, email);
            }
        }

        /// <summary>
        /// Creates entries for users that are not currently in the database.
        ///     This method is only ran if the client email cannot be found
        ///     in the initial search. 
        /// </summary>
        /// <param name="conn">The database connection, it is already open</param>
        /// <param name="email">The client's email. Used for initialization of the entries</param>
        private static void CreateNewDatabaseEntries(SqlConnection conn, string email)
        {
            int StoreID = 0;
            StringBuilder sb = new StringBuilder();
            
            /* Create the first insert into the main table. This creates the entry we will
             *      be working with the rest of the method
             */
            sb.Append("INSERT INTO " + DatabaseTables.STORE_INFORMATION);
            sb.Append("(" + StoreInformationTable.STORE_EMAIL_ADDRESS + ")");
            sb.Append(" VALUES ");
            sb.Append("('" + email + "');");

            string SqlCommand = sb.ToString();
            using (SqlCommand command = new SqlCommand(SqlCommand, conn))
            {
                /* Tries really hard to make the new table */
                int result = -1, tries = 0;
                while (result < 0 && tries < 3)
                {
                    result = command.ExecuteNonQuery();
                }
            }
            sb.Clear();


            /* Retrieves the StoreID made from the previous statement to populate the rest of
             *      the tables
             */
            sb.Append("SELECT " + StoreInformationTable.STORE_ID + "FROM " + DatabaseTables.STORE_INFORMATION);
            sb.Append("WHERE " + StoreInformationTable.STORE_EMAIL_ADDRESS + "='" + email + "';");

            SqlCommand = sb.ToString();
            using (SqlCommand command = new SqlCommand(SqlCommand, conn))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        StoreID = reader.GetInt32(0);
                    }
                }
            }
            sb.Clear();


            /* Begin inserting into the rest of the tables
             *      This insert covers the StoreDeliveryInformation table
             */
            sb.Append("INSERT INTO " + DatabaseTables.STORE_DELIVERY_INFORMATION);
            sb.Append("(" + StoreDeliveryInformationTable.STORE_ID + ")");
            sb.Append(" VALUES ");
            sb.Append("(" + StoreID + ");");

            SqlCommand = sb.ToString();
            using (SqlCommand command = new SqlCommand(SqlCommand, conn))
            {
                /* Tries really hard to make the new table */
                int result = -1, tries = 0;
                while (result < 0 && tries < 3)
                {
                    result = command.ExecuteNonQuery();
                }
            }
            sb.Clear();


            /* Continue inserting into the rest of the tables
             *      This insert covers the StoreEquipmentInformation table
             */
            sb.Append("INSERT INTO " + DatabaseTables.STORE_EQUIPMENT_INFORMATION);
            sb.Append("(" + StoreEquipmentInformationTable.STORE_ID + ")");
            sb.Append("VALUES");
            sb.Append("(" + StoreID + ");");

            SqlCommand = sb.ToString();
            using (SqlCommand command = new SqlCommand(SqlCommand, conn))
            {
                /* Tries really hard to make the new table */
                int result = -1, tries = 0;
                while (result < 0 && tries < 3)
                {
                    result = command.ExecuteNonQuery();
                }
            }
            sb.Clear();

            
            /* Continue inserting into the rest of the tables
             *      This insert covers the StoreDeliverySchedule table
             */
            sb.Append("INSERT INTO " + DatabaseTables.STORE_DELIVERY_SCHEDULE);
            sb.Append("(" + StoreDeliveryScheduleTable.STORE_ID + ")");
            sb.Append("VALUES");
            sb.Append("(" + StoreID + ");");

            SqlCommand = sb.ToString();
            using (SqlCommand command = new SqlCommand(SqlCommand, conn))
            {
                /* Tries really hard to make the new table */
                int result = -1, tries = 0;
                while (result < 0 && tries < 3)
                {
                    result = command.ExecuteNonQuery();
                }
            }
            sb.Clear();

            store.StoreID = StoreID;
        }


        private static void GetDeliveryDays(SqlConnection conn, int DeliveryID)
        {

        }
    }
}




