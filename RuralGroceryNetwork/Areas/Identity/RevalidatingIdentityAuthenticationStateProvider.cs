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
using System.Security.Claims;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Components.Authorization;
using Microsoft.AspNetCore.Components.Server;
using Microsoft.AspNetCore.Identity;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;

namespace RuralGroceryNetwork.Areas.Identity
{
	public class RevalidatingIdentityAuthenticationStateProvider<TUser>
		: RevalidatingServerAuthenticationStateProvider where TUser : class
	{
		private readonly IServiceScopeFactory _scopeFactory;
		private readonly IdentityOptions _options;

		public RevalidatingIdentityAuthenticationStateProvider(
			ILoggerFactory loggerFactory,
			IServiceScopeFactory scopeFactory,
			IOptions<IdentityOptions> optionsAccessor)
			: base(loggerFactory)
		{
			_scopeFactory = scopeFactory;
			_options = optionsAccessor.Value;
		}

		protected override TimeSpan RevalidationInterval => TimeSpan.FromMinutes(30);

		protected override async Task<bool> ValidateAuthenticationStateAsync(
			AuthenticationState authenticationState, CancellationToken cancellationToken)
		{
			// Get the user manager from a new scope to ensure it fetches fresh data
			var scope = _scopeFactory.CreateScope();
			try
			{
				var userManager = scope.ServiceProvider.GetRequiredService<UserManager<TUser>>();
				return await ValidateSecurityStampAsync(userManager, authenticationState.User);
			}
			finally
			{
				if (scope is IAsyncDisposable asyncDisposable)
				{
					await asyncDisposable.DisposeAsync();
				}
				else
				{
					scope.Dispose();
				}
			}
		}

		private async Task<bool> ValidateSecurityStampAsync(UserManager<TUser> userManager, ClaimsPrincipal principal)
		{
			var user = await userManager.GetUserAsync(principal);
			if (user == null)
			{
				return false;
			}
			else if (!userManager.SupportsUserSecurityStamp)
			{
				return true;
			}
			else
			{
				var principalStamp = principal.FindFirstValue(_options.ClaimsIdentity.SecurityStampClaimType);
				var userStamp = await userManager.GetSecurityStampAsync(user);
				return principalStamp == userStamp;
			}
		}
	}
}
