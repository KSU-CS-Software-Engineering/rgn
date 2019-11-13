using System.ComponentModel.DataAnnotations;



namespace RuralGroceryBlazor
{
    public class TruckState
    {

        [Required, MaxLength(100)] public string Name { get; set; }
        [Required, MaxLength(100)] public string Capacity { get; set; }
        [Required, MaxLength(100)] public double Start_Location_x { get; set; }
        public bool Refrigerated { get; set; }
        public bool Start { get; set; }
        public bool End { get; set; }
        [Required, MaxLength(100)] public double Start_Location_y { get; set; }
        [Required, MaxLength(100)] public double End_Location_x { get; set; }
        [Required, MaxLength(100)] public double End_Location_y { get; set; }

    }
}
