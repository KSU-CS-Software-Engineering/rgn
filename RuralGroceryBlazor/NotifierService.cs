﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RuralGroceryBlazor
{
    public class NotifierService
    {
        public async Task Update(string key, int value)
        {
            if (Notify != null)
            {
                await Notify.Invoke(key, value).ConfigureAwait(false);
            }
        }

        public event Func<string, int, Task> Notify;
    }
}