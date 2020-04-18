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
Stores scenario information
*/

using System.ComponentModel.DataAnnotations;

namespace RuralGroceryNetwork
{
	public class ScenarioState
	{
		/// <summary>
		/// Name of scenario
		/// </summary>
		[Required, MaxLength(100)] public string Name { get; set; }

		/// <summary>
		/// Description of scenario
		/// </summary>
		[Required, MaxLength(100)] public string Description { get; set; }

		/// <summary>
		/// URL of ArcGIS server
		/// </summary>
		[Required, MaxLength(100)] public string Server_URL { get; set; }

		/// <summary>
		/// ID for server login
		/// </summary>
		[Required, MaxLength(100)] public string ClientID { get; set; }

		/// <summary>
		/// Secret for server login
		/// </summary>
		[Required, MaxLength(100)] public string Client_Secret { get; set; }
	}
}
