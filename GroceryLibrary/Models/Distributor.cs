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
