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
This class stores the data for a single truck
*/

using System.ComponentModel.DataAnnotations;

namespace RuralGroceryNetwork
{
	public class TruckState
	{
		/// <summary>
		/// Name of truck
		/// </summary>
		[Required, MaxLength(100)] public string Name { get; set; }

		/// <summary>
		/// Capacity of truck
		/// </summary>
		[Required, MaxLength(100)] public string Capacity { get; set; }

		/// <summary>
		/// Starting location X
		/// </summary>
		[Required, MaxLength(100)] public double Start_Location_x { get; set; }

		/// <summary>
		/// Whether the truck is refridgerated or not
		/// </summary>
		public bool Refrigerated { get; set; }

		/// <summary>
		/// The bool for if it's the trucks starting point
		/// </summary>
		public bool Start { get; set; }

		/// <summary>
		/// The bool for if it's the trucks last point
		/// </summary>
		public bool End { get; set; }

		/// <summary>
		/// Starting location Y
		/// </summary>
		[Required, MaxLength(100)] public double Start_Location_y { get; set; }

		/// <summary>
		/// Ending location X
		/// </summary>
		[Required, MaxLength(100)] public double End_Location_x { get; set; }

		/// <summary>
		/// Ending location Y
		/// </summary>
		[Required, MaxLength(100)] public double End_Location_y { get; set; }
	}
}
