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

using Bunit;
using System;
using Microsoft.Extensions.DependencyInjection;
using Xunit;
using RuralGroceryNetwork.Pages;
using RuralGroceryNetwork.Shared;
using Index = RuralGroceryNetwork.Pages.Index;

namespace RuralGroceryNetwork.Test
{
	public class BlazorComponentTest
	{
		[Fact]
		public void IndexComponentTest()
		{

		}

        [Fact]
        public void Login() { 

        }
        [Fact]
        public void Logout() {
        
        }
        [Fact]
        public void RegisterAccount() {
        
        }
        [Fact]
        public void NavigateToRoutes() {
            // Arrange
            using var ctx = new TestContext();
            var cut = ctx.RenderComponent<RouteEditor>();
            var paraElm = cut.Find("row1");

            //// Act
            //cut.Find("button").Click();
            //var paraElmText = paraElm.TextContent;

            //// Assert
            //paraElmText.MarkupMatches("Current count: 1");

            Assert.True(paraElm.ClassName == "row1");

        }
        
        [Fact]
        public void RendersAboutPage() {
        
        }

        [Fact]
        public void RendersDistributionSim() {
        
        }

        [Fact]
        public void RendersRGIPage() {
        
        }

        [Fact]
        public void RendersInstructionsPage() {
        
        
        }


    }
}
