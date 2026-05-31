import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8090/api";

export const getToken = () => localStorage.getItem("ts_token");

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { "Content-Type": "application/json" },
});

api.interceptors.request.use((config) => {
  if (config.url?.startsWith("/auth/")) {
    return config;
  }

  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response?.status === 401 && !error.config?.url?.startsWith("/auth/")) {
      clearAuth();
      window.location.assign("/login");
    }

    const message = error.response?.data?.message || error.message || "Request failed";
    return Promise.reject(new Error(message));
  },
);

export const authApi = {
  register: (payload) => api.post("/auth/register", payload),
  signup: (payload) => api.post("/auth/signup", payload),
  login: (payload) => api.post("/auth/login", payload),
};

export const dashboardApi = {
  get: () => api.get("/dashboard"),
};

export const taskApi = {
  list: (params) => api.get("/tasks", { params }),
  get: (id) => api.get(`/tasks/${id}`),
  create: (payload) => api.post("/tasks", payload),
  update: (id, payload) => api.put(`/tasks/${id}`, payload),
  updateStatus: (id, status) => api.patch(`/tasks/${id}/status`, { status }),
  remove: (id) => api.delete(`/tasks/${id}`),
};

export const notificationApi = {
  list: () => api.get("/notifications"),
  markRead: (id) => api.patch(`/notifications/${id}/read`),
  registerPushSubscription: (pushToken) => api.post("/notifications/push-subscription", { pushToken }),
  remove: (id) => api.delete(`/notifications/${id}`),
  streamUrl: () => `${API_BASE_URL}/notifications/stream?token=${encodeURIComponent(getToken() || "")}`,
};

export const saveAuth = (data) => {
  localStorage.setItem("ts_token", data.token);
  localStorage.setItem(
    "ts_user",
    JSON.stringify({
      userId: data.userId,
      username: data.username,
      email: data.email,
    }),
  );
};

export const clearAuth = () => {
  localStorage.removeItem("ts_token");
  localStorage.removeItem("ts_user");
};

export const getStoredUser = () => {
  const user = localStorage.getItem("ts_user");
  return user ? JSON.parse(user) : null;
};

export const isAuthenticated = () => Boolean(getToken());
