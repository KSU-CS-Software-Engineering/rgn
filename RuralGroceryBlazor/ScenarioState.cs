
using System.ComponentModel.DataAnnotations;

namespace RuralGroceryBlazor
{
    public class ScenarioState
    {
        [Required, MaxLength(100)] public string Name { get; set; }
        [Required, MaxLength(100)] public string Description { get; set; }
        [Required, MaxLength(100)] public string Server_URL { get; set; }
        [Required, MaxLength(100)] public string ClientID { get; set; }
        [Required, MaxLength(100)] public string Client_Secret { get; set; }

    }
}
