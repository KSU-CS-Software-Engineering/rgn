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

/* 
 * Class: StoreInformationTable.cs
 * Purpose: This models the Store information table in the Rural Grocery Database
 */
namespace GroceryLibrary.Models.Database
{
    public static class StoreInformationTable
    {
        public const string STORE_ID = "StoreID ";

        public const string STORE_NAME = "StoreName ";

        public const string CITY_ID = "CityID ";

        public const string STORE_EMAIL_ADDRESS = "StoreEmailAddress";

        public const string NUMBER_OF_CHECKOUT_LANES = "NumberOfCheckoutLanes ";

        public const string WEEKLY_BUYING_MIN_REQUIREMENT = "WeeklyBuyingMinRequirement ";

        public const string SQUARE_FOOTAGE_CATEGORIES_ID = "SquareFootageCategoriesID ";

        public const string STORE_DELIVERY_INFORMATION_ID = "StoreDeliveryInformationID ";

        public const string STORE_EQUIPMENT_INFORMATION_ID = "StoreEquipmentInformationID ";
    }
}