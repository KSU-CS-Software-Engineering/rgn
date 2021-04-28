﻿/*
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
 * Class: DatabaseTables.cs
 * Purpose: This models the Rural Grocery Database table name in the Rural Grocery Database
 */
namespace GroceryLibrary.Models.Database
{
    public static class DatabaseTables
    {
        public const string CITIES = "[dbo].[Cities] C ";

        public const string DISTRIBUTOR = "[dbo].[Distributor] D ";

        public const string SQUARE_FOOTAGE_CATEGORIES = "[dbo].[SquareFootageCategories] SFC ";

        public const string STATES = "[dbo].[States] S ";

        public const string STORE_DELIVERY_INFORMATION = "[dbo].[StoreDeliveryInformation] SDI ";

        public const string STORE_DELIVERY_SCHEDULE = "[dbo].[StoreDeliverySchedule] SDS ";

        public const string STORE_EQUIPMENT_INFORMATION = "[dbo].[StoreEquipmentInformation] SEI ";

        public const string STORE_INFORMATION = "[dbo].[StoreInformation] SI ";

        public const string PAGES = "[dbo].[Pages] P ";
    }
}