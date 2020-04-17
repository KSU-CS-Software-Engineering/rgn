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

using System.ComponentModel.DataAnnotations;

namespace RuralGroceryNetwork
{
    public class NodeState
    {
        [Required, MaxLength(100)] public string Name { get; set; }
        [Required, MaxLength(100)] public double Location_x { get; set; }
        [Required, MaxLength(100)] public double Location_y { get; set; }

        [Required, MaxLength(100)] public string Demand { get; set; }
        [Required, MaxLength(100)] public string Supply { get; set; }
    }
}
