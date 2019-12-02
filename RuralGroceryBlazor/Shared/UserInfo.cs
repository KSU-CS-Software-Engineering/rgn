using System;

namespace RuralGroceryBlazor.Shared
{
    public class UserInfo
    {
        public bool IsAuthenticated { get; set; }

        public event Action OnChange;

        public void Authenticate()
        {
            IsAuthenticated = true;
            OnChange?.Invoke();
        }
        public Login Login { get; private set; } = new Login();
    }
}
