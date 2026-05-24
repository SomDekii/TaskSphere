import { createContext, useContext, useMemo, useState } from "react";
import { authApi, clearAuth, getStoredUser, isAuthenticated, saveAuth } from "../services/api";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(getStoredUser());
  const [authenticated, setAuthenticated] = useState(isAuthenticated());

  const login = async (payload) => {
    const data = await authApi.login(payload);
    saveAuth(data);
    setUser(getStoredUser());
    setAuthenticated(true);
  };

  const register = async (payload) => {
    const data = await authApi.register(payload);
    saveAuth(data);
    setUser(getStoredUser());
    setAuthenticated(true);
  };

  const logout = () => {
    clearAuth();
    setUser(null);
    setAuthenticated(false);
  };

  const value = useMemo(
    () => ({ user, authenticated, login, register, logout }),
    [user, authenticated],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export const useAuth = () => useContext(AuthContext);
