using Bunit;
using System;
using Bunit.Mocking.JSInterop;
using Microsoft.Extensions.DependencyInjection;
using Xunit;
using RuralGroceryNetwork.Pages;
using RuralGroceryNetwork.Shared;
using Index = RuralGroceryNetwork.Pages.Index;

namespace RuralGroceryNetwork.Test
{
	public class BlazorComponentTest : ComponentTestFixture
	{
		[Fact]
		public void IndexComponentTest()
		{
			IRenderedComponent<Index> cut = RenderComponent<Index>();

			var expectedHtml = @"<h1>Rural Grocery Initiative</h1>
								<p>
								  <br>The mission of the
								  <a href=""https://www.ruralgrocery.org/"">Rural Grocery Initiative</a>
								(RGI) is to provide resources to help sustain and enhance independently-owned rural grocery stores.  RGI assists
									communities and citizens to strengthen rural grocery operations and improve access to healthy foods.
								</p>

								<p>
								This application is meant to aid rural grocers by optimizing their distribution networks. It takes in information about stores or other distribution points
									of interest and will calculate the best way to load trucks, the order to distribute to stores in, etc. The application gives the user control to manipulate
									nodes (a store or distribution center), trucks, demand, and more. To accomplish the routing portion of the calculations, the application utilizes ArcGIS
									Online, a service that can find the best routes between nodes on a map.								
								</p>";

			cut.MarkupMatches(expectedHtml);
		}
	}
}
