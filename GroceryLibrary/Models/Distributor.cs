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
using System.Collections.Generic;
using System.Text;

namespace GroceryLibrary.Models
{
    public class Distributor
    {
        /// <summary>
        /// ID of Distributor in database
        /// </summary>
        public int DistributorID { get; set; }
        /// <summary>
        /// Name of Distributor
        /// </summary>
        public string DistributorName { get; set; }

        /// <summary>
        /// Address of Distributor
        /// </summary>
        public string Address { get; set; }

        /// <summary>
        /// City ID from database for distributor
        /// </summary>
        public int CityID { get; set; }

        /// <summary>
        /// Zip code for distributor
        /// </summary>
        public string ZipCode { get; set; }

        /// <summary>
        /// Y coordinate for distributor
        /// </summary>
        public decimal YLAT { get; set; }

        /// <summary>
        /// X coordinate for distributor
        /// </summary>
        public decimal XLONG { get; set; }
    }
}
