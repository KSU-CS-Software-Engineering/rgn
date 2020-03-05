using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RuralGroceryNetwork
{
    public class Truck
    {
        public TruckState State { get; private set; } = new TruckState();
    }
}
