/* 
 * Class: DatabaseTables.cs
 * Purpose: This models the Rural Grocery Database table name in the Rural Grocery Database
 */
namespace GroceryLibrary.Models.Database
{
    public static class DatabaseTables
    {
        public static string CITIES = "[dbo].[Cities] ";

        public static string DISTRIBUTOR = "[dbo].[Distributor] ";

        public static string SQUARE_FOOTAGE_CATEGORIES = "[dbo].[SquareFootageCategories] ";

        public static string STATES = "[dbo].[States] ";

        public static string STORE_DELIVERY_INFORMATION = "[dbo].[StoreDeliveryInformation] ";

        public static string STORE_DELIVERY_SCHEDULE = "[dbo].[StoreDeliverySchedule]";

        public static string STORE_EQUIPMENT_INFORMATION = "[dbo].[StoreEquipmentInformation] ";

        public static string STORE_INFORMATION = "[dbo].[StoreInformation] ";
    }
}