/* 
 * Class: StoreDeliveryInformationTable.cs
 * Purpose: This models the Store Delivery Information table in the Rural Grocery Database
 */
namespace GroceryLibrary.Models.Database
{
    public static class StoreDeliveryInformationTable
    {
        public const string STORE_DELIVERY_INFORMATION_ID = "StoreDeliveryInformationID ";

        public const string STORE_ID = "StoreID";

        public const string DISTRIBUTOR_ID = "DistributorID ";

        public const string PALLET_ORDER_MINIMUM = "PalletOrderMinimum ";

        public const string PALLET_ORDER_MAXIMUM = "PalletOrderMaximum ";

        public const string WEEKLY_BUYING_MIN_REQUIREMENT = "WeeklyBuyingMinRequirement ";

        public const string SELL_TO_OTHERS = "SellToOthers ";
    }
}