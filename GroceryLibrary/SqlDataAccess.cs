using System;
using System.Text;
using System.IO;
using System.Collections.Generic;

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
        /// Used to initialize connections to the database
        /// </summary>
        private static SqlConnectionStringBuilder builder = new SqlConnectionStringBuilder();

        /// <summary>
        /// This method is responsible for populating the lists that are used for editing account information
        /// </summary>
        /// <returns>An array of lists which hold information about the static tables</returns>
        public static List<string>[] GetAllNames()
        {
            string sqlCommand;
            StringBuilder sb = new StringBuilder();
            List<string>[] names = new List<string>[3];

            List<string> cityNames = new List<string>();
            List<string> distributorNames = new List<string>();
            List<string> SFCatNames = new List<string>();
            try
            {
                using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
                {
                    /* This part gets the city names from the DB
                     *      Populates the cityNameList List
                     */
                    connection.Open();

                    sb.Append("SELECT " + CitiesTable.CITY_NAME);
                    sb.Append("FROM " + DatabaseTables.CITIES);
                    sb.Append("ORDER BY " + CitiesTable.CITY_ID + "ASC");

                    sqlCommand = sb.ToString();
                    using (SqlCommand command = new SqlCommand(sqlCommand, connection))
                    {
                        using (SqlDataReader reader = command.ExecuteReader())
                        {
                            if (reader.HasRows)
                            {
                                while (reader.Read())
                                {
                                    cityNames.Add(reader.GetString(0));
                                }
                            }
                            else
                                throw new MissingFieldException("There should be rows in the database!!!! Something is incorrect!!!!");
                        }
                    }
                    sb.Clear();


                    /* This part populates the distributors table
                     * 
                     */
                    sb.Append("SELECT " + DistributorTable.DISTRIBUTOR_NAME);
                    sb.Append("FROM " + DatabaseTables.DISTRIBUTOR);
                    sb.Append("ORDER BY " + DistributorTable.DISTRIBUTOR_ID + "ASC");

                    sqlCommand = sb.ToString();
                    using (SqlCommand command = new SqlCommand(sqlCommand, connection))
                    {
                        using (SqlDataReader reader = command.ExecuteReader())
                        {
                            if (reader.HasRows)
                            {
                                while (reader.Read())
                                {
                                    distributorNames.Add(reader.GetString(0));
                                }
                            }
                            else
                                throw new MissingFieldException("There should be rows in the database!!!! Something is incorrect!!!!");
                        }
                    }
                    sb.Clear();


                    /* This part populates the square footage category tables
                     * 
                     */
                    sb.Append("SELECT " + SquareFootageCategoriesTable.CATEGORY_NAME);
                    sb.Append("FROM " + DatabaseTables.SQUARE_FOOTAGE_CATEGORIES);
                    sb.Append("ORDER BY " + SquareFootageCategoriesTable.SQUARE_FOOTAGE_CATEGORIES_ID + "ASC");

                    sqlCommand = sb.ToString();
                    using (SqlCommand command = new SqlCommand(sqlCommand, connection))
                    {
                        using (SqlDataReader reader = command.ExecuteReader())
                        {
                            if (reader.HasRows)
                            {
                                while (reader.Read())
                                {
                                    SFCatNames.Add(reader.GetString(0));
                                }
                            }
                            else
                                throw new MissingFieldException("There should be rows in the database!!!! Something is incorrect!!!!");
                        }
                    }
                    sb.Clear();
                    connection.Close();
                }
            }
            catch (SqlException sqle)
            {   
                store = null;
            }
            
            names[0] = cityNames;
            names[1] = distributorNames;
            names[2] = SFCatNames;
            return names;
        }

        /// <summary>
        /// This method will run on initialization of the StoreInformation.razor page
        /// </summary>
        /// <param name="email">The current logged in user email address [UPPER CASE]</param>
        public static Store OnInitialize(string email)
        {
            store.StoreEmail = email;
            try
            {
                using (StreamReader sr = new StreamReader("../GroceryLibrary/DBAccountInfo.txt"))
                {
                    builder.DataSource = sr.ReadLine();
                    builder.UserID = sr.ReadLine();
                    builder.Password = sr.ReadLine();
                    builder.InitialCatalog = "RuralGroceryNetwork";
                }
            }
            catch(IOException ioe)
            {
                store = null;
            }

            try
            { 
                using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
                {
                    connection.Open();
                    getStoreInformation(connection);
                    getStoreDeliveryInformation(connection);
                    getStoreDeliverySchedule(connection);
                    getStoreEquipmentInformation(connection);
                    getRestOfInfo(connection);
                    connection.Close();
                }
            }
            catch (Exception e)
            {
                store = null;
            }
            return store;
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

            ExecuteNonQuery(sb.ToString(), conn);
            sb.Clear();


            /* Retrieves the StoreID made from the previous statement to populate the rest of
             *      the tables
             */
            sb.Append("SELECT " + StoreInformationTable.STORE_ID);
            sb.Append("FROM " + DatabaseTables.STORE_INFORMATION);
            sb.Append("WHERE " + StoreInformationTable.STORE_EMAIL_ADDRESS + "='" + store.StoreEmail + "';");

            using (SqlCommand command = new SqlCommand(sb.ToString(), conn))
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

            ExecuteNonQuery(sb.ToString(), conn);
            sb.Clear();


            /* Continue inserting into the rest of the tables
             *      This insert covers the StoreEquipmentInformation table
             */
            sb.Append("INSERT INTO " + DatabaseTables.STORE_EQUIPMENT_INFORMATION);
            sb.Append("(" + StoreEquipmentInformationTable.STORE_ID + ")");
            sb.Append("VALUES");
            sb.Append("(" + StoreID + ");");

            ExecuteNonQuery(sb.ToString(), conn);
            sb.Clear();


            /* Continue inserting into the rest of the tables
             *      This insert covers the StoreDeliverySchedule table
             */
            sb.Append("INSERT INTO " + DatabaseTables.STORE_DELIVERY_SCHEDULE);
            sb.Append("(" + StoreDeliveryScheduleTable.STORE_ID + ")");
            sb.Append("VALUES");
            sb.Append("(" + StoreID + ");");

            ExecuteNonQuery(sb.ToString(), conn);
            sb.Clear();

            /* Set the store's ID equal to the ID that was auto assigned by the DB */
            store.StoreID = StoreID;
        }


        /// <summary>
        /// Helper function to evaluate 'T' or 'F' characters
        /// </summary>
        /// <param name="c"></param>
        /// <returns></returns>
        private static bool ConvertCharToBool(char c)
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
            bool hasRows = false;
            StringBuilder sb = new StringBuilder();

            sb.Append("SELECT ");
            sb.Append("SI." + StoreInformationTable.STORE_ID + ", " +
                "SI." + StoreInformationTable.STORE_NAME + ", " +
                "SI." + StoreInformationTable.CITY_ID + ", " +
                "SI." + StoreInformationTable.NUMBER_OF_CHECKOUT_LANES + ", " +
                "SI." + StoreInformationTable.WEEKLY_BUYING_MIN_REQUIREMENT + ", " +
                "SFC." + SquareFootageCategoriesTable.CATEGORY_NAME + " ");
            sb.Append("FROM " + DatabaseTables.STORE_INFORMATION + "SI ");
            sb.Append("INNER JOIN " + DatabaseTables.SQUARE_FOOTAGE_CATEGORIES + "SFC ON SFC." + SquareFootageCategoriesTable.SQUARE_FOOTAGE_CATEGORIES_ID + " = SI." + StoreInformationTable.SQUARE_FOOTAGE_CATEGORIES_ID);
            sb.Append("WHERE " + StoreInformationTable.STORE_EMAIL_ADDRESS + "= '" + store.StoreEmail + "'");

            using (SqlCommand command = new SqlCommand(sb.ToString(), conn))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    if (hasRows = reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            store.StoreID = reader.GetInt32(0); /* This can never be null PK */

                            /* Checks to see if StoreName is currently NULL */
                            if (!reader.IsDBNull(1))
                                store.StoreName = reader.GetString(1);
                            else
                                store.StoreName = "Not yet assigned";

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
                                store.HasWeeklyBuyingRequirement = ConvertCharToBool(reader.GetString(4)[0]);
                            else
                                store.HasWeeklyBuyingRequirement = false;


                            /* Checks to see if square footage category ID is currently NULL */
                            if (!reader.IsDBNull(5))
                                store.SquareFootageCategory = reader.GetString(5);
                            else
                                store.SquareFootageCategory = "Not yet assigned";
                        }
                    }
                }
            }
            /* Calls private method to create entries for the table. This method is only called once per user and it is
            *      called on the user's first time to the account page
            */
            if(!hasRows)
                CreateNewDatabaseEntries(conn);
        }


        /// <summary>
        /// Retrieves the store's delivery information and populates the global store
        ///     variable accordingly
        /// </summary>
        /// <param name="conn">The database connection</param>
        private static void getStoreDeliveryInformation(SqlConnection conn)
        {
            StringBuilder sb = new StringBuilder();

            sb.Append("SELECT ");
            sb.Append(StoreDeliveryInformationTable.DISTRIBUTOR_ID + ", " +
                StoreDeliveryInformationTable.PALLET_ORDER_MINIMUM + ", " +
                StoreDeliveryInformationTable.PALLET_ORDER_MAXIMUM + ", " +
                StoreDeliveryInformationTable.SELL_TO_OTHERS + ", " +
                StoreDeliveryInformationTable.WEEKLY_BUYING_MIN_REQUIREMENT);
            sb.Append("FROM " + DatabaseTables.STORE_DELIVERY_INFORMATION);
            sb.Append("WHERE " + StoreDeliveryInformationTable.STORE_ID + "= " + store.StoreID);

            using (SqlCommand command = new SqlCommand(sb.ToString(), conn))
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
                                store.SellsToOthers = ConvertCharToBool(reader.GetString(3)[0]);
                            else
                                store.SellsToOthers = false;

                            if (!reader.IsDBNull(4))
                                store.HasWeeklyBuyingRequirement = ConvertCharToBool(reader.GetString(4)[0]);
                            else
                                store.HasWeeklyBuyingRequirement = false;
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

            using (SqlCommand command = new SqlCommand(sb.ToString(), conn))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            if (!reader.IsDBNull(0))
                                store.DeliveryMonday = ConvertCharToBool(reader.GetString(0)[0]);
                            else
                                store.DeliveryMonday = false;


                            if (!reader.IsDBNull(1))
                                store.DeliveryTuesday = ConvertCharToBool(reader.GetString(1)[0]);
                            else
                                store.DeliveryTuesday = false;


                            if (!reader.IsDBNull(2))
                                store.DeliveryWednesday = ConvertCharToBool(reader.GetString(2)[0]);
                            else
                                store.DeliveryWednesday = false;


                            if (!reader.IsDBNull(3))
                                store.DeliveryThursday = ConvertCharToBool(reader.GetString(3)[0]);
                            else
                                store.DeliveryThursday = false;


                            if (!reader.IsDBNull(4))
                                store.DeliveryFriday = ConvertCharToBool(reader.GetString(4)[0]);
                            else
                                store.DeliveryFriday = false;


                            if (!reader.IsDBNull(5))
                                store.DeliverySaturday = ConvertCharToBool(reader.GetString(5)[0]);
                            else
                                store.DeliverySaturday = false;


                            if (!reader.IsDBNull(6))
                                store.DeliverySunday = ConvertCharToBool(reader.GetString(6)[0]);
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
            StringBuilder sb = new StringBuilder();

            sb.Append("SELECT ");
            sb.Append(StoreEquipmentInformationTable.LOADING_DOCK + ", " +
                StoreEquipmentInformationTable.FORK_LIFT + ", " +
                StoreEquipmentInformationTable.PALLET_JACK + " ");
            sb.Append("FROM " + DatabaseTables.STORE_EQUIPMENT_INFORMATION);
            sb.Append("WHERE " + StoreDeliveryScheduleTable.STORE_ID + "= " + store.StoreID);

            using (SqlCommand command = new SqlCommand(sb.ToString(), conn))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            if (!reader.IsDBNull(0))
                                store.HasLoadingDock = ConvertCharToBool(reader.GetString(0)[0]);
                            else
                                store.HasLoadingDock = false;


                            if (!reader.IsDBNull(1))
                                store.HasForkLift = ConvertCharToBool(reader.GetString(1)[0]);
                            else
                                store.HasForkLift = false;


                            if (!reader.IsDBNull(2))
                                store.HasPalletJack = ConvertCharToBool(reader.GetString(2)[0]);
                            else
                                store.HasPalletJack = false;
                        }
                    }
                    else
                        throw new MissingFieldException("There should be rows in the database!!!! Something is incorrect!!!!");
                }
            }
        }

        /// <summary>
        /// Used to get the actual city name and distributor name given they have selected one
        /// </summary>
        /// <param name="conn">The database connection</param>
        private static void getRestOfInfo(SqlConnection conn)
        {
            if (store.CityID != -1 || store.DistributorID != -1)
            {
                bool needsCity = false, needsDist = false;
                StringBuilder sb = new StringBuilder();

                sb.Append("SELECT ");

                if (store.CityID != -1)
                {
                    sb.Append("C." + CitiesTable.CITY_NAME);
                    needsCity = true;
                }
                else
                    store.CityName = "Not yet assigned";

                if (store.DistributorID != -1)
                {
                    if (needsCity)
                        sb.Append(", ");

                    sb.Append("D." + DistributorTable.DISTRIBUTOR_NAME);
                    needsDist = true;
                }
                else
                    store.DistributorName = "Not yet assigned";

                sb.Append("FROM " + DatabaseTables.STORE_INFORMATION + "S ");

                if (store.CityID != -1)
                    sb.Append("INNER JOIN " + DatabaseTables.CITIES + "C ON C." + CitiesTable.CITY_ID + "= S." + StoreInformationTable.CITY_ID);
                if (store.DistributorID != -1)
                {
                    sb.Append("INNER JOIN " + DatabaseTables.STORE_DELIVERY_INFORMATION + "SDI ON SDI." + StoreDeliveryInformationTable.STORE_ID + "= S." + StoreInformationTable.STORE_ID);
                    sb.Append("INNER JOIN " + DatabaseTables.DISTRIBUTOR + "D ON D." + DistributorTable.DISTRIBUTOR_ID + "= SDI." + StoreDeliveryInformationTable.DISTRIBUTOR_ID);
                }

                sb.Append("WHERE S." + StoreInformationTable.STORE_ID + "= " + store.StoreID.ToString());

                using (SqlCommand command = new SqlCommand(sb.ToString(), conn))
                {
                    using (SqlDataReader reader = command.ExecuteReader())
                    {
                        if (reader.HasRows)
                        {
                            while (reader.Read())
                            {
                                if (needsCity && needsDist)
                                {
                                    store.CityName = reader.GetString(0);
                                    store.DistributorName = reader.GetString(1);
                                }
                                else
                                {
                                    if (needsCity)
                                        store.CityName = reader.GetString(0);
                                    else
                                        store.DistributorName = reader.GetString(0);
                                }
                            }
                        }
                        else
                            throw new MissingFieldException("There should be rows in the database!!!! Something is incorrect!!!!");
                    }
                }
            }
        }

        /// <summary>
        /// Handles conversions to chars
        /// </summary>
        /// <param name="var">Some boolean</param>
        /// <returns>'T' if var is true, 'F' if var is false</returns>
        private static char ConvertBoolToChar(bool var)
        {
            if (var)
                return 'T';
            else
                return 'F';
        }

        /*
         * This is where the getting information from the database stops. All SELECT and INSERT statements are above this point
         * 
         * Below this point are UPDATE statements which take client information and updates the DB accordingly
         */

        /// <summary>
        /// Responsible for update the store information table with geo information
        /// </summary>
        public static void updateGeoInformation(string StoreName, int CityID, int DistID)
        {
            StringBuilder sb = new StringBuilder();

            sb.Append("UPDATE " + DatabaseTables.STORE_INFORMATION);
            sb.Append("SET ");
            sb.Append(StoreInformationTable.STORE_NAME + "= '" + StoreName + "', ");
            sb.Append(StoreInformationTable.CITY_ID + "= " + (CityID + 1).ToString() + " ");
            sb.Append("WHERE " + StoreDeliveryScheduleTable.STORE_ID + "= " + store.StoreID.ToString());

            try
            {
                using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
                {
                    ExecuteNonQuery(sb.ToString(), connection);
                }
            }
            catch (Exception e)
            {
                store = null;
            }


            sb.Clear();

            sb.Append("UPDATE " + DatabaseTables.STORE_DELIVERY_INFORMATION);
            sb.Append("SET ");
            sb.Append(StoreDeliveryInformationTable.DISTRIBUTOR_ID + "= " + (DistID + 1).ToString() + " ");
            sb.Append("WHERE " + StoreDeliveryInformationTable.STORE_ID + "= " + store.StoreID.ToString());

            try
            {
                using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
                {
                    ExecuteNonQuery(sb.ToString(), connection);
                }
            }
            catch (SqlException sqle)
            {
                /* Error Handling Here */
            }
        }

        /// <summary>
        /// Responsible for updating the physical information
        /// </summary>
        /// <param name="SFCat"></param>
        /// <param name="NumChkLns"></param>
        public static void updatePhysInformation(int SFCat, int NumChkLns)
        {
            StringBuilder sb = new StringBuilder();

            sb.Append("UPDATE " + DatabaseTables.STORE_INFORMATION);
            sb.Append("SET ");
            sb.Append(StoreInformationTable.NUMBER_OF_CHECKOUT_LANES + "= " + NumChkLns + ", ");
            sb.Append(StoreInformationTable.SQUARE_FOOTAGE_CATEGORIES_ID + "= " + (SFCat + 1) + " ");
            sb.Append("WHERE " + StoreDeliveryScheduleTable.STORE_ID + "= " + store.StoreID.ToString());

            try
            {
                using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
                {
                    ExecuteNonQuery(sb.ToString(), connection);
                }
            }
            catch (SqlException sqle)
            {
                /* Error Handling Here */
            }
        }


        /// <summary>
        /// Responsible for update the delivery schedule table
        /// </summary>
        /// <param name="mon">Does Monday have delivery?</param>
        /// <param name="tues"></param>
        /// <param name="wed"></param>
        /// <param name="thurs"></param>
        /// <param name="fri"></param>
        /// <param name="sat"></param>
        /// <param name="sun"></param>
        public static void updateDeliveryDays(bool mon, bool tues, bool wed, bool thurs,
                                                    bool fri, bool sat, bool sun)
        {
            StringBuilder sb = new StringBuilder();

            sb.Append("UPDATE " + DatabaseTables.STORE_DELIVERY_SCHEDULE);
            sb.Append("SET ");
            sb.Append(StoreDeliveryScheduleTable.DELIVERY_MONDAY + "= '" + ConvertBoolToChar(mon) + "', ");
            sb.Append(StoreDeliveryScheduleTable.DELIVERY_TUESDAY + "= '" + ConvertBoolToChar(tues) + "', ");
            sb.Append(StoreDeliveryScheduleTable.DELIVERY_WEDNESDAY + "= '" + ConvertBoolToChar(wed) + "', ");
            sb.Append(StoreDeliveryScheduleTable.DELIVERY_THURSDAY + "= '" + ConvertBoolToChar(thurs) + "', ");
            sb.Append(StoreDeliveryScheduleTable.DELIVERY_FRIDAY + "= '" + ConvertBoolToChar(fri) + "', ");
            sb.Append(StoreDeliveryScheduleTable.DELIVERY_SATURDAY + "= '" + ConvertBoolToChar(sat) + "', ");
            sb.Append(StoreDeliveryScheduleTable.DELIVERY_SUNDAY + "= '" + ConvertBoolToChar(sun) + "' ");
            sb.Append("WHERE " + StoreDeliveryScheduleTable.STORE_ID + "= " + store.StoreID.ToString());

            try
            {
                using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
                {
                    ExecuteNonQuery(sb.ToString(), connection);
                }
            }
            catch(SqlException sqle)
            {
                /* Error Handling Here */
            }
        }

        /// <summary>
        /// Responsible for update the pallet infomation
        /// </summary>
        /// <param name="palletMin">The new pallet minimum</param>
        /// <param name="palletMax">The new pallet maximum</param>
        /// <param name="weeklyBuyMin">Is there a weekly buying minimum per pallet?></param>
        /// <param name="sellsToOthers">Does the store sell to others?</param>
        public static void updatePalletInformation(int palletMin, int palletMax, bool weeklyBuyMin, bool sellsToOthers)
        {
            StringBuilder sb = new StringBuilder();

            sb.Append("UPDATE " + DatabaseTables.STORE_DELIVERY_INFORMATION);
            sb.Append("SET ");
            sb.Append(StoreDeliveryInformationTable.PALLET_ORDER_MINIMUM + "= " + palletMin + ", ");
            sb.Append(StoreDeliveryInformationTable.PALLET_ORDER_MAXIMUM + "= " + palletMax + ", ");
            sb.Append(StoreDeliveryInformationTable.WEEKLY_BUYING_MIN_REQUIREMENT + "= '" + ConvertBoolToChar(weeklyBuyMin) + "', ");
            sb.Append(StoreDeliveryInformationTable.SELL_TO_OTHERS + "= '" + ConvertBoolToChar(sellsToOthers) + "' ");
            sb.Append("WHERE " + StoreDeliveryInformationTable.STORE_ID + "= " + store.StoreID.ToString());

            try
            {
                using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
                {
                    ExecuteNonQuery(sb.ToString(), connection);
                }
            }
            catch (SqlException sqle)
            {
                /* Error Handling Here */
            }
        }

        /// <summary>
        /// Responsible for updating the equipment information table
        /// </summary>
        /// <param name="fork">Does store have a forklift?</param>
        /// <param name="pallet"></param>
        /// <param name="loading"></param>
        public static void updateStoreEquipmentInformation(bool fork, bool pallet, bool loading)
        {
            StringBuilder sb = new StringBuilder();

            sb.Append("UPDATE " + DatabaseTables.STORE_EQUIPMENT_INFORMATION);
            sb.Append("SET ");
            sb.Append(StoreEquipmentInformationTable.FORK_LIFT + "= '" + ConvertBoolToChar(fork) + "', "); 
            sb.Append(StoreEquipmentInformationTable.PALLET_JACK + "= '" + ConvertBoolToChar(pallet) + "', ");
            sb.Append(StoreEquipmentInformationTable.LOADING_DOCK + "= '" + ConvertBoolToChar(loading) + "' ");
            sb.Append("WHERE " + StoreEquipmentInformationTable.STORE_ID + "= " + store.StoreID.ToString());

            try
            {
                using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
                {
                    ExecuteNonQuery(sb.ToString(), connection);
                }
            }
            catch (SqlException sqle)
            {
                /* Error Handling Here */
            }
        }

        /// <summary>
        /// Used for executing update queries within the database
        /// </summary>
        /// <param name="sqlCommand">The command that needs to be executed</param>
        /// <param name="conn">The database connection</param>
        private static void ExecuteNonQuery(string sqlCommand, SqlConnection conn)
        {
            conn.Open();
            using (SqlCommand command = new SqlCommand(sqlCommand, conn))
            {
                int result = command.ExecuteNonQuery();
                if (result < 0)
                    throw new Exception("Could not update table");
            }
            conn.Close();
        }

        public static List<Store> getAllStores()
        {
            List<Store> allStores = new List<Store>();
            StringBuilder sb = new StringBuilder();

            sb.Append("SELECT *");
            sb.Append("FROM " + DatabaseTables.STORE_INFORMATION);
            builder.ConnectionString = "Data Source = (local); Initial Catalog = RuralGrocery; Integrated Security = True; Connect Timeout = 30; Encrypt = False; TrustServerCertificate = False; ApplicationIntent = ReadWrite; MultiSubnetFailover = False";
            //builder.ConnectionString = "SERVER=23.99.140.241;DATABASE=master;UID=sa;PWD=Testpassword1!";

            try
            {
                using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
                {
                    connection.Open();
                    using (SqlCommand command = new SqlCommand(sb.ToString(), connection))
                    {
                        using (var reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                Store tempStore = new Store();
                                tempStore.StoreID = Convert.ToInt32(reader["StoreID"]);
                                tempStore.StoreName = reader["StoreName"].ToString();
                                tempStore.YLAT = Convert.ToDecimal(reader["YLAT"]);
                                tempStore.XLONG = Convert.ToDecimal(reader["XLONG"]);

                                allStores.Add(tempStore);
                            }
                        }
                    }
                    connection.Close();
                }
            }
            catch (SqlException sqle)
            {
                /* Error Handling Here */
            }

            return allStores;
        }

        public static List<Distributor> getAllDistributors()
        {
            List<Distributor> allDist = new List<Distributor>();
            StringBuilder sb = new StringBuilder();

            sb.Append("SELECT *");
            sb.Append("FROM " + DatabaseTables.DISTRIBUTOR);
            builder.ConnectionString = "SERVER=23.99.140.241;DATABASE=master;UID=sa;PWD=Testpassword1!";

            try
            {
                using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
                {
                    connection.Open();
                    using (SqlCommand command = new SqlCommand(sb.ToString(), connection))
                    {
                        using (var reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                Distributor tempDist = new Distributor();
                                tempDist.DistributorID = Convert.ToInt32(reader["DistributorID"]);
                                tempDist.DistributorName = reader["DistributorName"].ToString();
                                tempDist.YLAT = Convert.ToDecimal(reader["YLAT"]);
                                tempDist.XLONG = Convert.ToDecimal(reader["XLONG"]);

                                allDist.Add(tempDist);
                            }
                        }
                    }
                    connection.Close();
                }
            }
            catch (SqlException sqle)
            {
                /* Error Handling Here */
            }

            return allDist;
        }
    }
}