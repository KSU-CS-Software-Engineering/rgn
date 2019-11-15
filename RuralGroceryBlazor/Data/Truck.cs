using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RuralGroceryBlazor
{
    public class Truck
    {
        public TruckState State { get; private set; } = new TruckState();
    }
}
