/* 
 * Class: DatabaseTables.cs
 * Purpose: This models the Rural Grocery Database table name in the Rural Grocery Database
 */
namespace GroceryLibrary.Models.Database
{
    public static class DatabaseTables
    {
        public const string CITIES = "[dbo].[Cities] ";

        public const string DISTRIBUTOR = "[dbo].[Distributor] ";

        public const string SQUARE_FOOTAGE_CATEGORIES = "[dbo].[SquareFootageCategories] ";

        public const string STATES = "[dbo].[States] ";

        public const string STORE_DELIVERY_INFORMATION = "[dbo].[StoreDeliveryInformation] ";

        public const string STORE_DELIVERY_SCHEDULE = "[dbo].[StoreDeliverySchedule]";

        public const string STORE_EQUIPMENT_INFORMATION = "[dbo].[StoreEquipmentInformation] ";

        public const string STORE_INFORMATION = "[dbo].[StoreInformation] ";
    }
}