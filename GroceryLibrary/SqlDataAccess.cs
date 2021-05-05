/*
Copyright 2020 Kansas State University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

using System;
using System.Text;
using System.IO;
using System.Data;
using System.Collections.Generic;
using System.Configuration;
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

        private static string ConnectionString = "Data Source = (local); Initial Catalog = RuralGrocery; Integrated Security = True; Connect Timeout = 30; Encrypt = False; TrustServerCertificate = False; ApplicationIntent = ReadWrite; MultiSubnetFailover = False";

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
            sb.Append("FROM " + DatabaseTables.STORE_INFORMATION);
            sb.Append("INNER JOIN " + DatabaseTables.SQUARE_FOOTAGE_CATEGORIES + "ON SFC." + SquareFootageCategoriesTable.SQUARE_FOOTAGE_CATEGORIES_ID + " = SI." + StoreInformationTable.SQUARE_FOOTAGE_CATEGORIES_ID);
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

                            /* Checks to see if CityName is currently NULL */
                            if (!reader.IsDBNull(2))
                                store.CityName = reader.GetString(1);
                            else
                                store.CityName = "Not yet assigned";

                            /* Checks to see if NumChkLanes is currently NULL */
                            if (!reader.IsDBNull(3))
                                store.NumberOfCheckoutLanes = reader.GetInt32(3);
                            else
                                store.NumberOfCheckoutLanes = -1;

                            /* Checks to see if weekly buying requirement is currently NULL */
                            if (!reader.IsDBNull(4))
                                store.HasWeeklyPurchaseRequirement = ConvertCharToBool(reader.GetString(4)[0]);
                            else
                                store.HasWeeklyPurchaseRequirement = false;


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
                                store.HasWeeklyPurchaseRequirement = ConvertCharToBool(reader.GetString(4)[0]);
                            else
                                store.HasWeeklyPurchaseRequirement = false;
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
            if (store.CityName != "Not yet assigned" || store.DistributorID != -1)
            {
                bool needsCity = false, needsDist = false;
                StringBuilder sb = new StringBuilder();

                sb.Append("SELECT ");

                if (store.CityName != "Not yet assigned")
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

                sb.Append("FROM " + DatabaseTables.STORE_INFORMATION);

                if (store.CityName != "Not yet assigned")
                    sb.Append("INNER JOIN " + DatabaseTables.CITIES + "ON C." + CitiesTable.CITY_ID + "= S." + StoreInformationTable.CITY_ID);
                if (store.DistributorID != -1)
                {
                    sb.Append("INNER JOIN " + DatabaseTables.STORE_DELIVERY_INFORMATION + "ON SDI." + StoreDeliveryInformationTable.STORE_ID + "= S." + StoreInformationTable.STORE_ID);
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

            sb.Append("SELECT * ");
            sb.Append("FROM " + DatabaseTables.STORE_INFORMATION);
            sb.Append(" INNER JOIN " + DatabaseTables.CITIES + "ON SI.CityID = C.CityID ");
            sb.Append("INNER JOIN " + DatabaseTables.STATES + "ON S.StateID = C.StateID ");
            //sb.Append("INNER JOIN " + DatabaseTables.STORE_DELIVERY_INFORMATION + "ON SDI.StoreID = SI.StoreID ");   

            builder.ConnectionString = ConnectionString;
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
                                tempStore.Address = reader["Address"].ToString();
                                tempStore.CityName = reader["CityName"].ToString();
                                tempStore.StateName = reader["StateName"].ToString();
                                tempStore.ZipCode = reader["Zip"].ToString();
                                tempStore.CityPopulation = Convert.ToInt32(reader["Population"]);
                                tempStore.YLAT = Convert.ToDecimal(reader["YLAT"]);
                                tempStore.XLONG = Convert.ToDecimal(reader["XLONG"]);
                                tempStore.WeeklyPurchaseAmount = Convert.ToInt32(reader["WeeklyPurchaseAmount"]);

                                /*
                                var r = reader["WeeklyPurchaseAmount"];
                                var rs = r.ToString();
                                if (r.ToString() == "")
                                    tempStore.WeeklyPurchaseAmount = 0;
                                else
                                    tempStore.WeeklyPurchaseAmount = Convert.ToInt32(reader["WeeklyPurchaseAmount"]);
                                */

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
            builder.ConnectionString = ConnectionString;
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

        public static List<Page> getPageContent(String page)
        {
            List<Page> allParagraphs = new List<Page>();
            StringBuilder sb = new StringBuilder();

            sb.Append("SELECT * ");
            sb.Append("FROM " + DatabaseTables.PAGES);
            sb.Append("WHERE [Page] = '" + page + "'");
            sb.Append("ORDER BY ParagraphNumber");

            builder.ConnectionString = ConnectionString;
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
                                Page tempPage = new Page();
                                tempPage.PageName = reader["Page"].ToString();
                                tempPage.ParagraphNumber = Convert.ToInt32(reader["ParagraphNumber"]);
                                tempPage.HeaderName = reader["Header"].ToString();
                                tempPage.Content = reader["Content"].ToString();

                                allParagraphs.Add(tempPage);
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

            return allParagraphs;
        }


        public static void updatePageContent(Page p)
        {
            StringBuilder sb = new StringBuilder();

            sb.Append("UPDATE [dbo].[Pages] ");
            sb.Append("SET Pages.Header = '" + p.HeaderName + "', Pages.Content = '" + p.Content + "' ");
            sb.Append("WHERE Pages.[Page] = '" + p.PageName + "' AND Pages.ParagraphNumber = " + p.ParagraphNumber + "; ");


            builder.ConnectionString = ConnectionString;
            //builder.ConnectionString = "SERVER=23.99.140.241;DATABASE=master;UID=sa;PWD=Testpassword1!";

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

        // STARTING THE SECTION OF FUNCTIONS REQUIRED FOR DOWNLOADING/UPLOADING THE DATABASE



        /// <summary>
        /// Converts a table in the database to csv format
        /// </summary>
        /// <param name="dbTable">The table to convert</param>
        /// <returns>a csv of the given table</returns>
        public static string DBTableToCSV(string dbTable)
        {
            string csv = "";
            StringBuilder sb = new StringBuilder();

            sb.Append("SELECT *");
            sb.Append("FROM " + dbTable);
            builder.ConnectionString = ConnectionString;
            //builder.ConnectionString = "SERVER=23.99.140.241;DATABASE=master;UID=sa;PWD=Testpassword1!";

            try
            {
                using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
                {
                    connection.Open();
                    using (SqlCommand command = new SqlCommand(sb.ToString(), connection))
                    {
                        // Most of the code from this section is from: https://www.aspsnippets.com/Articles/Export-data-from-SQL-Server-to-CSV-file-in-ASPNet-using-C-and-VBNet.aspx
                        // Adapter to fill a datatable

                        using (SqlDataAdapter sda = new SqlDataAdapter())
                        {
                            command.Connection = connection;
                            sda.SelectCommand = command;

                            using (DataTable dt = new DataTable())
                            {
                                // Fill the data table with the all the info from Store Information
                                sda.Fill(dt);

                                // Fill the csv files with column names
                                foreach(DataColumn column in dt.Columns)
                                {
                                    csv += column.ColumnName + ",";
                                }

                                // new line
                                csv += "\r\n";

                                // Fill the data table with all of the results
                                foreach(DataRow row in dt.Rows)
                                {
                                    foreach(DataColumn column in dt.Columns)
                                    {
                                        csv += row[column.ColumnName].ToString().Replace(",", ";") + ",";
                                    }

                                    csv += "\r\n";
                                }
                            }
                        }
                    }
                    connection.Close();                   
                }
            }
            catch(SqlException sqle)
            {

            }
            return csv;
        }

        /// <summary>
        /// Bulk insert data from a given datatable into the store information table
        /// </summary>
        /// <param name="data">the datatable parsed from a csv file</param>
        public static void InsertNewStoreInformation(DataTable data)
        {
            builder.ConnectionString = ConnectionString;

            StringBuilder sb = new StringBuilder();
            // Want to switch to TRUNCATE TABLE to reset Primary key as well, but there are the foreign keys stopping me right now.
            sb.Append("DELETE FROM [dbo].[StoreInformationStaging]; ");

            using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
            {

                // Delete any existing entries from the staging table
                ExecuteNonQuery(sb.ToString(), connection);

                connection.Open();
                // bulk copy the data from the csv into the staging table
                using (SqlBulkCopy s = new SqlBulkCopy(connection))
                {
                    // define target table name
                    s.DestinationTableName = "[dbo].[StoreInformationStaging]";
                    // match the columns
                    foreach (var column in data.Columns)
                    {
                        s.ColumnMappings.Add(column.ToString(), column.ToString());
                    }
                    //bulk write the information
                    s.WriteToServer(data);
                }
                connection.Close();

                sb.Clear();

                // Create the SQL to Merge staging table (source) into permanent store information table (target).
                // If the ID matches, we update the row. If the ID is not in the target then we insert. If the ID is not in the source then we delete from the target.
                sb.Append("MERGE [dbo].StoreInformation AS TARGET");
                sb.Append(" USING [dbo].StoreInformationStaging AS SOURCE");
                sb.Append(" ON (TARGET.StoreID = SOURCE.StoreID)");
                sb.Append(" WHEN MATCHED THEN UPDATE SET TARGET.StoreName = SOURCE.StoreName, TARGET.Address = SOURCE.Address, TARGET.CityID = SOURCE.CityID, TARGET.Zip = SOURCE.Zip, " +
                    "TARGET.YLAT = SOURCE.YLAT, TARGET.XLONG = SOURCE.XLONG, TARGET.WeeklyPurchaseAmount = SOURCE.WeeklyPurchaseAmount");
                sb.Append(" WHEN NOT MATCHED BY TARGET THEN INSERT (StoreName, Address, CityID, Zip, YLAT, XLONG, WeeklyPurchaseAmount) VALUES (SOURCE.StoreName, SOURCE.Address, SOURCE.CityID, SOURCE.Zip, " +
                    "SOURCE.YLAT, SOURCE.XLONG, SOURCE.WeeklyPurchaseAmount)");
                sb.Append(" WHEN NOT MATCHED BY SOURCE THEN DELETE;");

                ExecuteNonQuery(sb.ToString(), connection);
            }
        }

        /// <summary>
        /// Bulk insert data from a given datatable into the distributor table
        /// </summary>
        /// <param name="data">the datatable parsed from a csv file</param>
        public static void InsertNewDistributorInformation(DataTable data)
        {
            builder.ConnectionString = ConnectionString;

            StringBuilder sb = new StringBuilder();
            // Want to switch to TRUNCATE TABLE to reset Primary key as well, but there are the foreign keys stopping me right now.
            sb.Append("DELETE FROM [dbo].[DistributorStaging]; ");

            using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
            {

                // Delete any existing entries from the staging table
                ExecuteNonQuery(sb.ToString(), connection);

                connection.Open();
                // bulk copy the data from the csv into the staging table
                using (SqlBulkCopy s = new SqlBulkCopy(connection))
                {
                    // define target table name
                    s.DestinationTableName = "[dbo].[DistributorStaging]";
                    // match the columns
                    foreach (var column in data.Columns)
                    {
                        s.ColumnMappings.Add(column.ToString(), column.ToString());
                    }
                    //bulk write the information
                    s.WriteToServer(data);
                }
                connection.Close();

                sb.Clear();

                // Create the SQL to Merge staging table (source) into permanent store information table (target).
                // If the ID matches, we update the row. If the ID is not in the target then we insert. If the ID is not in the source then we delete from the target.
                sb.Append("MERGE [dbo].Distributor AS TARGET");
                sb.Append(" USING [dbo].DistributorStaging AS SOURCE");
                sb.Append(" ON (TARGET.DistributorID = SOURCE.DistributorID)");
                sb.Append(" WHEN MATCHED THEN UPDATE SET TARGET.DistributorName = SOURCE.DistributorName, TARGET.Address = SOURCE.Address, TARGET.CityID = SOURCE.CityID, TARGET.Zip = SOURCE.Zip, " +
                    "TARGET.YLAT = SOURCE.YLAT, TARGET.XLONG = SOURCE.XLONG");
                sb.Append(" WHEN NOT MATCHED BY TARGET THEN INSERT (DistributorName, Address, CityID, Zip, YLAT, XLONG) VALUES (SOURCE.DistributorName, SOURCE.Address, SOURCE.CityID, SOURCE.Zip, " +
                    "SOURCE.YLAT, SOURCE.XLONG)");
                sb.Append(" WHEN NOT MATCHED BY SOURCE THEN DELETE;");

                ExecuteNonQuery(sb.ToString(), connection);
            }
        }

        /// <summary>
        /// Bulk insert data from a given datatable into the city table
        /// </summary>
        /// <param name="data">the datatable parsed from a csv file</param>
        public static void InsertNewCityInformation(DataTable data)
        {
            builder.ConnectionString = ConnectionString;

            StringBuilder sb = new StringBuilder();
            // Want to switch to TRUNCATE TABLE to reset Primary key as well, but there are the foreign keys stopping me right now.
            sb.Append("DELETE FROM [dbo].[CitiesStaging]; ");

            using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
            {

                // Delete any existing entries from the staging table
                ExecuteNonQuery(sb.ToString(), connection);

                connection.Open();
                // bulk copy the data from the csv into the staging table
                using (SqlBulkCopy s = new SqlBulkCopy(connection))
                {
                    // define target table name
                    s.DestinationTableName = "[dbo].[CitiesStaging]";
                    // match the columns
                    foreach (var column in data.Columns)
                    {
                        s.ColumnMappings.Add(column.ToString(), column.ToString());
                    }
                    //bulk write the information
                    s.WriteToServer(data);
                }
                connection.Close();

                sb.Clear();

                // Create the SQL to Merge staging table (source) into permanent store information table (target).
                // If the ID matches, we update the row. If the ID is not in the target then we insert. If the ID is not in the source then we delete from the target.
                sb.Append("MERGE [dbo].Cities AS TARGET");
                sb.Append(" USING [dbo].CitiesStaging AS SOURCE");
                sb.Append(" ON (TARGET.CityID = SOURCE.CityID)");
                sb.Append(" WHEN MATCHED THEN UPDATE SET TARGET.CityName = SOURCE.CityName, TARGET.StateID = SOURCE.StateID, TARGET.Population = SOURCE.Population");
                sb.Append(" WHEN NOT MATCHED BY TARGET THEN INSERT (CityName, StateID, Population) VALUES (SOURCE.CityName, SOURCE.StateID, SOURCE.Population)");
                sb.Append(" WHEN NOT MATCHED BY SOURCE THEN DELETE;");

                ExecuteNonQuery(sb.ToString(), connection);
            }
        }

        /// <summary>
        /// Bulk insert data from a given datatable into the state table
        /// </summary>
        /// <param name="data">the datatable parsed from a csv file</param>
        public static void InsertNewStateInformation(DataTable data)
        {
            builder.ConnectionString = ConnectionString;

            StringBuilder sb = new StringBuilder();
            // Want to switch to TRUNCATE TABLE to reset Primary key as well, but there are the foreign keys stopping me right now.
            sb.Append("DELETE FROM [dbo].[StatesStaging]; ");

            using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
            {

                // Delete any existing entries from the staging table
                ExecuteNonQuery(sb.ToString(), connection);

                connection.Open();
                // bulk copy the data from the csv into the staging table
                using (SqlBulkCopy s = new SqlBulkCopy(connection))
                {
                    // define target table name
                    s.DestinationTableName = "[dbo].[StatesStaging]";
                    // match the columns
                    foreach (var column in data.Columns)
                    {
                        s.ColumnMappings.Add(column.ToString(), column.ToString());
                    }
                    //bulk write the information
                    s.WriteToServer(data);
                }
                connection.Close();

                sb.Clear();

                // Create the SQL to Merge staging table (source) into permanent store information table (target).
                // If the ID matches, we update the row. If the ID is not in the target then we insert. If the ID is not in the source then we delete from the target.
                sb.Append("MERGE [dbo].States AS TARGET");
                sb.Append(" USING [dbo].StatesStaging AS SOURCE");
                sb.Append(" ON (TARGET.StateID = SOURCE.StateID)");
                sb.Append(" WHEN MATCHED THEN UPDATE SET TARGET.StateName = SOURCE.StateName");
                sb.Append(" WHEN NOT MATCHED BY TARGET THEN INSERT (StateName) VALUES (SOURCE.StateName)");
                sb.Append(" WHEN NOT MATCHED BY SOURCE THEN DELETE;");

                ExecuteNonQuery(sb.ToString(), connection);
            }
        }

        public static void InsertNewStoreDeliveryInformation(DataTable data)
        {
            builder.ConnectionString = ConnectionString;

            StringBuilder sb = new StringBuilder();
            // Want to switch to TRUNCATE TABLE to reset Primary key as well, but there are the foreign keys stopping me right now.
            sb.Append("DELETE FROM [dbo].[StoreDeliveryInformationStaging]; ");

            using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
            {

                // Delete any existing entries from the staging table
                ExecuteNonQuery(sb.ToString(), connection);

                connection.Open();
                // bulk copy the data from the csv into the staging table
                using (SqlBulkCopy s = new SqlBulkCopy(connection))
                {
                    // define target table name
                    s.DestinationTableName = "[dbo].[StoreDeliveryInformationStaging]";
                    // match the columns
                    foreach (var column in data.Columns)
                    {
                        s.ColumnMappings.Add(column.ToString(), column.ToString());
                    }
                    //bulk write the information
                    s.WriteToServer(data);
                }
                connection.Close();

                sb.Clear();

                // Create the SQL to Merge staging table (source) into permanent store information table (target).
                // If the ID matches, we update the row. If the ID is not in the target then we insert. If the ID is not in the source then we delete from the target.
                sb.Append("MERGE [dbo].StoreDeliveryInformation AS TARGET");
                sb.Append(" USING [dbo].StoreDeliveryInformationStaging AS SOURCE");
                sb.Append(" ON (TARGET.StoreDeliveryInformationID = SOURCE.StoreDeliveryInformationID)");
                sb.Append(" WHEN MATCHED THEN UPDATE SET TARGET.StoreID = SOURCE.StoreID, TARGET.DistributorID = SOURCE.DistributorID, TARGET.WeeklyPurchaseMinRequirement = SOURCE.WeeklyPurchaseMinRequirement, " +
                    "TARGET.PalletOrderMinimum = SOURCE.PalletOrderMinimum, TARGET.PalletOrderMaximum = SOURCE.PalletOrderMaximum, " +
                    "TARGET.SellToBusinesses = SOURCE.SellToBusinesses, TARGET.OtherBusinesses = SOURCE.OtherBusinesses, TARGET.SplitWithGroceryStore = SOURCE.SplitWithGroceryStore, " +
                    "TARGET.OtherGroceryStore = SOURCE.OtherGroceryStore, TARGET.DeliveryNotes = SOURCE.DeliveryNotes");
                sb.Append(" WHEN NOT MATCHED BY TARGET THEN INSERT (StoreID, DistributorID, WeeklyPurchaseMinRequirement, PalletOrderMinimum, " +
                    "PalletOrderMaximum, SellToBusinesses, OtherBusinesses, SplitWithGroceryStore, OtherGroceryStore, DeliveryNotes) VALUES (SOURCE.StoreID, " +
                    "SOURCE.DistributorID, SOURCE.WeeklyPurchaseMinRequirement, SOURCE.PalletOrderMinimum, " +
                    "SOURCE.PalletOrderMaximum, SOURCE.SellToBusinesses, SOURCE.OtherBusinesses, SOURCE.SplitWithGroceryStore, SOURCE.OtherGroceryStore, SOURCE.DeliveryNotes)");
                sb.Append(" WHEN NOT MATCHED BY SOURCE THEN DELETE;");

                ExecuteNonQuery(sb.ToString(), connection);
            }
        }

        /// <summary>
        /// This function gets the option to display/not display the weekly purchase amount variable
        /// </summary>
        /// <returns>a string containing the desired option</returns>
        public static string GetPurchaseVariablePreference()
        {
            builder.ConnectionString = ConnectionString;

            StringBuilder sb = new StringBuilder();
            
            sb.Append("SELECT * FROM [dbo].[VariablePreference]; ");
            string option = "";

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
                                option = reader["PurchasePreference"].ToString();
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

            return option;
        }

        /// <summary>
        /// Updates the preferenc in the database regarding whether or not to show the weekly purchase variable
        /// </summary>
        /// <param name="option">the string containing the option</param>
        public static void UpdatePurchaseVariablePreference(string option)
        {
            builder.ConnectionString = ConnectionString;

            StringBuilder sb = new StringBuilder();

            sb.Append("UPDATE [dbo].[VariablePreference] SET PurchasePreference = '" + option + "'; ");

            using (SqlConnection connection = new SqlConnection(builder.ConnectionString))
            {
                ExecuteNonQuery(sb.ToString(), connection);
            }
        }
    }
}