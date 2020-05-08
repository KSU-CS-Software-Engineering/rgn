using System;
using System.Text;

using System.Data.SqlClient;

using GroceryLibrary.Models;
using GroceryLibrary.Models.Database;


namespace GroceryLibrary
{
    /// <summary>
    /// Holds the queries and data manipulation for the client
    /// </summary>
    public static class SqlDataAccess
    {
        /// <summary>
        /// A representation of the client's information.
        /// </summary>
        private static Store store = new Store();


        /// <summary>
        /// This method will run on initialization of the StoreInformation.razor page
        /// </summary>
        /// <param name="email">The current logged in user email address [UPPER CASE]</param>
        public static Store OnInitialize(string email)
        {
            store.StoreEmail = email;
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
                    getStoreInformation(connection);
                    getStoreDeliveryInformation(connection);
                    getStoreDeliverySchedule(connection);
                    getStoreEquipmentInformation(connection);
                }
                return store;
            }
            catch (Exception e)
            {
                store = null;
            }
            return null;
        }


        /// <summary>
        /// Creates entries for users that are not currently in the database.
        ///     This method is only ran if the client email cannot be found
        ///     in the initial search. 
        /// </summary>
        /// <param name="conn">The database connection, it is already open</param>
        /// <param name="email">The client's email. Used for initialization of the entries</param>
        private static void CreateNewDatabaseEntries(SqlConnection conn)
        {
            int StoreID = 0;
            StringBuilder sb = new StringBuilder();

            /* Create the first insert into the main table. This creates the entry we will
             *      be working with the rest of the method
             */
            sb.Append("INSERT INTO " + DatabaseTables.STORE_INFORMATION);
            sb.Append("(" + StoreInformationTable.STORE_EMAIL_ADDRESS + ")");
            sb.Append(" VALUES ");
            sb.Append("('" + store.StoreEmail + "');");

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
            sb.Append("SELECT " + StoreInformationTable.STORE_ID);
            sb.Append("FROM " + DatabaseTables.STORE_INFORMATION);
            sb.Append("WHERE " + StoreInformationTable.STORE_EMAIL_ADDRESS + "='" + store.StoreEmail + "';");

            SqlCommand = sb.ToString();
            using (SqlCommand command = new SqlCommand(SqlCommand, conn))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            StoreID = reader.GetInt32(0);
                        }
                    }
                    else
                        throw new MissingFieldException("There should be rows in the database!!!! Something is incorrect!!!!");
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

            /* Set the store's ID equal to the ID that was auto assigned by the DB */
            store.StoreID = StoreID;
        }


        /// <summary>
        /// Helper function to evaluate 'T' or 'F' characters
        /// </summary>
        /// <param name="c"></param>
        /// <returns></returns>
        private static bool TrueOrFalseEval(char c)
        {
            if (c == 'T')
                return true;
            else if (c == 'F')
                return false;
            else
                throw new NotImplementedException("There is incongruency in the tables. Something is not 'T' or 'F' value");
        }


        /// <summary>
        /// Get the information for the StoreInformation table
        /// </summary>
        /// <param name="conn">The database connection</param>
        private static void getStoreInformation(SqlConnection conn)
        {
            StringBuilder sb = new StringBuilder();

            sb.Append("SELECT ");
            sb.Append(StoreInformationTable.STORE_ID + ", " +
                StoreInformationTable.STORE_NAME + ", " +
                StoreInformationTable.CITY_ID + ", " +
                StoreInformationTable.NUMBER_OF_CHECKOUT_LANES + ", " +
                StoreInformationTable.WEEKLY_BUYING_MIN_REQUIREMENT + ", " +
                StoreInformationTable.SQUARE_FOOTAGE_CATEGORIES_ID + " ");
            sb.Append("FROM " + DatabaseTables.STORE_INFORMATION);
            sb.Append("WHERE " + StoreInformationTable.STORE_EMAIL_ADDRESS + "= '" + store.StoreEmail + "'");

            string sqlCommand = sb.ToString();
            using (SqlCommand command = new SqlCommand(sqlCommand, conn))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            store.StoreID = reader.GetInt32(0); /* This can never be null PK */

                            /* Checks to see if StoreName is currently NULL */
                            if (!reader.IsDBNull(1))
                                store.StoreName = reader.GetString(1);
                            else
                                store.StoreName = null;

                            /* Checks to see if CityID is currently NULL */
                            if (!reader.IsDBNull(2))
                                store.CityID = reader.GetInt32(2);
                            else
                                store.CityID = -1;

                            /* Checks to see if NumChkLanes is currently NULL */
                            if (!reader.IsDBNull(3))
                                store.NumberOfCheckoutLanes = reader.GetInt32(3);
                            else
                                store.NumberOfCheckoutLanes = -1;

                            /* Checks to see if weekly buying requirement is currently NULL */
                            if (!reader.IsDBNull(4))
                                store.HasWeeklyBuyingRequirement = TrueOrFalseEval(reader.GetChar(4));
                            else
                                store.HasWeeklyBuyingRequirement = false;


                            /* Checks to see if square footage category ID is currently NULL */
                            if (!reader.IsDBNull(5))
                                store.SquareFootageCategoryID = reader.GetInt32(5);
                            else
                                store.SquareFootageCategoryID = -1;
                        }
                    }
                    else
                        /* Calls private method to create entries for the table. This method is only called once per user and it is
                        *      called on the user's first time to the account page
                        */
                        CreateNewDatabaseEntries(conn);
                }
            }
        }


        /// <summary>
        /// Retrieves the store's delivery information and populates the global store
        ///     variable accordingly
        /// </summary>
        /// <param name="conn">The database connection</param>
        private static void getStoreDeliveryInformation(SqlConnection conn)
        {
            string sqlCommand;
            StringBuilder sb = new StringBuilder();

            sb.Append("SELECT ");
            sb.Append(StoreDeliveryInformationTable.DISTRIBUTOR_ID + ", " +
                StoreDeliveryInformationTable.PALLET_ORDER_MINIMUM + ", " +
                StoreDeliveryInformationTable.PALLET_ORDER_MAXIMUM + ", " +
                StoreDeliveryInformationTable.SELL_TO_OTHERS + " ");
            sb.Append("FROM " + DatabaseTables.STORE_DELIVERY_INFORMATION);
            sb.Append("WHERE " + StoreDeliveryInformationTable.STORE_ID + "= " + store.StoreID);

            sqlCommand = sb.ToString();
            using (SqlCommand command = new SqlCommand(sqlCommand, conn))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            if (!reader.IsDBNull(0))
                                store.DistributorID = reader.GetInt32(0);
                            else
                                store.DistributorID = -1;


                            if (!reader.IsDBNull(1))
                                store.PalletMinimum = reader.GetInt32(1);
                            else
                                store.PalletMinimum = -1;


                            if (!reader.IsDBNull(2))
                                store.PalletMaximum = reader.GetInt32(2);
                            else
                                store.PalletMaximum = -1;


                            if (!reader.IsDBNull(3))
                                store.SellsToOthers = TrueOrFalseEval(reader.GetChar(3));
                            else
                                store.SellsToOthers = false;
                        }
                    }
                    else
                        throw new MissingFieldException("There should be rows in the database!!!! Something is incorrect!!!!");
                }
            }
        }

        /// <summary>
        /// Retrieves the store's delivery schedule and populates the global store
        ///     variable accordingly
        /// </summary>
        /// <param name="conn">The database connection</param>
        private static void getStoreDeliverySchedule(SqlConnection conn)
        {
            string sqlCommand;
            StringBuilder sb = new StringBuilder();

            sb.Append("SELECT ");
            sb.Append(StoreDeliveryScheduleTable.DELIVERY_MONDAY + ", " +
                StoreDeliveryScheduleTable.DELIVERY_TUESDAY + ", " +
                StoreDeliveryScheduleTable.DELIVERY_WEDNESDAY + ", " +
                StoreDeliveryScheduleTable.DELIVERY_THURSDAY + ", " +
                StoreDeliveryScheduleTable.DELIVERY_FRIDAY + ", " +
                StoreDeliveryScheduleTable.DELIVERY_SATURDAY + ", " +
                StoreDeliveryScheduleTable.DELIVERY_SUNDAY + " ");
            sb.Append("FROM " + DatabaseTables.STORE_DELIVERY_SCHEDULE);
            sb.Append("WHERE " + StoreDeliveryScheduleTable.STORE_ID + "= " + store.StoreID);

            sqlCommand = sb.ToString();
            using (SqlCommand command = new SqlCommand(sqlCommand, conn))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            if (!reader.IsDBNull(0))
                                store.DeliveryMonday = TrueOrFalseEval(reader.GetChar(0));
                            else
                                store.DeliveryMonday = false;


                            if (!reader.IsDBNull(1))
                                store.DeliveryTuesday = TrueOrFalseEval(reader.GetChar(1));
                            else
                                store.DeliveryTuesday = false;


                            if (!reader.IsDBNull(2))
                                store.DeliveryWednesday = TrueOrFalseEval(reader.GetChar(2));
                            else
                                store.DeliveryWednesday = false;


                            if (!reader.IsDBNull(3))
                                store.DeliveryThursday = TrueOrFalseEval(reader.GetChar(3));
                            else
                                store.DeliveryThursday = false;


                            if (!reader.IsDBNull(4))
                                store.DeliveryFriday = TrueOrFalseEval(reader.GetChar(4));
                            else
                                store.DeliveryFriday = false;


                            if (!reader.IsDBNull(5))
                                store.DeliverySaturday = TrueOrFalseEval(reader.GetChar(5));
                            else
                                store.DeliverySaturday = false;


                            if (!reader.IsDBNull(6))
                                store.DeliverySunday = TrueOrFalseEval(reader.GetChar(6));
                            else
                                store.DeliverySunday = false;
                        }
                    }
                    else
                        throw new MissingFieldException("There should be rows in the database!!!! Something is incorrect!!!!");
                }
            }
        }

        /// <summary>
        /// Retrieves the store's equipment information and populates the global store
        ///     variable accordingly
        /// </summary>
        /// <param name="conn">The database connection</param>
        private static void getStoreEquipmentInformation(SqlConnection conn)
        {
            string sqlCommand;
            StringBuilder sb = new StringBuilder();

            sb.Append("SELECT ");
            sb.Append(StoreEquipmentInformationTable.LOADING_DOCK + ", " +
                StoreEquipmentInformationTable.FORK_LIFT + ", " +
                StoreEquipmentInformationTable.PALLET_JACK + " ");
            sb.Append("FROM " + DatabaseTables.STORE_EQUIPMENT_INFORMATION);
            sb.Append("WHERE " + StoreDeliveryScheduleTable.STORE_ID + "= " + store.StoreID);

            sqlCommand = sb.ToString();
            using (SqlCommand command = new SqlCommand(sqlCommand, conn))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            if (!reader.IsDBNull(0))
                                store.HasLoadingDock = TrueOrFalseEval(reader.GetChar(0));
                            else
                                store.HasLoadingDock = false;


                            if (!reader.IsDBNull(1))
                                store.HasForkLift = TrueOrFalseEval(reader.GetChar(1));
                            else
                                store.HasForkLift = false;


                            if (!reader.IsDBNull(2))
                                store.HasPalletJack = TrueOrFalseEval(reader.GetChar(2));
                            else
                                store.HasPalletJack = false;
                        }
                    }
                    else
                        throw new MissingFieldException("There should be rows in the database!!!! Something is incorrect!!!!");
                }
            }
        }
    }
}