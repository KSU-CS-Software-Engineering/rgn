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

using System;
using System.Collections.Generic;
using System.Text;

namespace GroceryLibrary.Models
{
    public class Store
    {
        /// <summary>
        /// The StoreID within the Database
        ///     This is used for almost all queries in the WHERE clause
        /// </summary>
        public int StoreID { get; set; }

        /// <summary>
        /// The email address associated with the account that is currently logged in
        /// </summary>
        public string StoreEmail { get; set; }

        /// <summary>
        /// The Store's name
        /// </summary>
        public string StoreName { get; set; }

        /// <summary>
        /// The Store's address
        /// </summary>
        public string Address { get; set; }

        /// <summary>
        /// The city which the store is located in
        /// </summary>
        public string CityName { get; set; }

        /// <summary>
        /// The state which the store is located in
        /// </summary>
        public string StateName { get; set; }

        /// <summary>
        /// The zip code where the store is located in
        /// </summary>
        public string ZipCode { get; set; }

        /// <summary>
        /// The y coordinate for the store
        /// </summary>
        public decimal YLAT { get; set; }

        /// <summary>
        /// The x coordinate for the store
        /// </summary>
        public decimal XLONG { get; set; }

        /// <summary>
        /// The number of checkout lanes that the store has
        /// </summary>
        public int NumberOfCheckoutLanes { get; set; }

        /// <summary>
        /// The square footage category ID for the store
        /// </summary>
        public int SquareFootageCategoryID { get; set; }

        /// <summary>
        /// The square footage category that the store is classified in
        /// </summary>
        public string SquareFootageCategory { get; set; }

        /// <summary>
        /// The stores DistributorID
        /// </summary>
        public int DistributorID { get; set; }

        /// <summary>
        /// The stores Distributor
        /// </summary>
        public string DistributorName { get; set; }

        /// <summary>
        /// Does the Store have a minimum buying requirement per week
        /// </summary>
        public bool HasWeeklyPurchaseRequirement { get; set; }

        /// <summary>
        /// The Store's lower purchase amount per week
        /// </summary>
        public int WeeklyPurchaseAmountMin { get; set; }

        /// <summary>
        /// The Store's higher purchase amount per week
        /// </summary>
        public int WeeklyPurchaseAmountMax { get; set; }

        /// <summary>
        /// What is the minimum number of pallets the store recieved per delivery
        /// </summary>
        public int PalletMinimum { get; set; }

        /// <summary>
        /// What is the maximum number of pallets the store recieved per delivery
        /// </summary>
        public int PalletMaximum { get; set; }

        /// <summary>
        /// Does the store sell to others
        /// </summary>
        public bool SellsToOthers { get; set; }

        /// <summary>
        /// Does the store receive deliveries on Mondays
        /// </summary>
        public bool DeliveryMonday { get; set; }

        /// <summary>
        /// Does the store receive deliveries on Tuesdays
        /// </summary>
        public bool DeliveryTuesday { get; set; }

        /// <summary>
        /// Does the store receive deliveries on Wednesdays
        /// </summary>
        public bool DeliveryWednesday { get; set; }

        /// <summary>
        /// Does the store receive deliveries on Thursdays
        /// </summary>
        public bool DeliveryThursday { get; set; }

        /// <summary>
        /// Does the store receive deliveries on Fridays
        /// </summary>
        public bool DeliveryFriday { get; set; }

        /// <summary>
        /// Does the store receive deliveries on Saturdays
        /// </summary>
        public bool DeliverySaturday { get; set; }

        /// <summary>
        /// Does the store receive deliveries on Sundays
        /// </summary>
        public bool DeliverySunday { get; set; }

        /// <summary>
        /// Does the store have a loading dock
        /// </summary>
        public bool HasLoadingDock { get; set; }

        /// <summary>
        /// Does the store have a fork lift
        /// </summary>
        public bool HasForkLift { get; set; }

        /// <summary>
        /// Does the store have a pallet jack
        /// </summary>
        public bool HasPalletJack { get; set; }

    }
}
