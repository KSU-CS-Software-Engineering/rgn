using System.ComponentModel.DataAnnotations;


namespace RuralGroceryBlazor
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
