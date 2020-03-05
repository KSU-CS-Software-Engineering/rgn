using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RuralGroceryNetwork
{
    public class Node
    {
       public NodeState State { get; private set; } = new NodeState();
    }
}
