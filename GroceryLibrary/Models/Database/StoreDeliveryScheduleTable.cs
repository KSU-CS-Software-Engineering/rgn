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
 * Class: StoreDeliveryScheduleTable.cs
 * Purpose: This models the Store Delivery Schedule table in the Rural Grocery Database
 */
namespace GroceryLibrary.Models.Database
{
    public static class StoreDeliveryScheduleTable
    {
        public const string STORE_DELIVERY_SCHEDULE_ID = "StoreDeliveryScheduleID ";

        public const string STORE_ID = "StoreID";

        public const string DELIVERY_MONDAY = "DeliveryMonday ";

        public const string DELIVERY_TUESDAY = "DeliveryTuesday ";

        public const string DELIVERY_WEDNESDAY = "DeliveryWednesday ";

        public const string DELIVERY_THURSDAY = "DeliveryThursday ";

        public const string DELIVERY_FRIDAY = "DeliveryFriday ";

        public const string DELIVERY_SATURDAY = "DeliverySaturday ";

        public const string DELIVERY_SUNDAY = "DeliverySunday ";
    }
}