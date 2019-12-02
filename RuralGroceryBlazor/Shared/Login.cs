using System.ComponentModel.DataAnnotations;

namespace RuralGroceryBlazor.Shared
{
    public class Login
    {
        [Required, MaxLength(100)]
        public string Username { get; set; }

        [Required, MaxLength(100)]
        public string Password { get; set; }
    }
}
