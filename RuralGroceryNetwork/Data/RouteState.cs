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
Stores Route information
*/
 
using System.ComponentModel.DataAnnotations;

namespace RuralGroceryNetwork
{
	public class RouteState
	{
		/// <summary>
		/// Size of  radius
		/// </summary>
		[Required, MaxLength(100)] public double Radius_Size { get; set; }
		/// <summary>
		/// Start location of radius, x
		/// </summary>
		[Required, MaxLength(100)] public double Radius_Start_x { get; set; }

		/// <summary>
		/// Sart Location of radius, y
		/// </summary>
		[Required, MaxLength(100)] public double Radius_Start_y { get; set; }

	}
}