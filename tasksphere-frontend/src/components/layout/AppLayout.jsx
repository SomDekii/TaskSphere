import { useState } from "react";
import { NavLink, Outlet, useLocation, useNavigate } from "react-router-dom";
import NotificationBell from "../notifications/NotificationBell";
import { useAuth } from "../../context/AuthContext";
import Icon from "../ui/Icon";
import logoSrc from "../../assets/TOdo.png";

const links = [
  { to: "/dashboard", label: "Dashboard", icon: "dashboard" },
  { to: "/tasks", label: "My Tasks", icon: "tasks" },
  { to: "/tasks/new", label: "Create Task", icon: "plus" },
];

const pageTitles = {
  "/dashboard": "Dashboard",
  "/tasks": "Tasks",
  "/tasks/new": "Create Task",
  "/notifications": "Notifications",
};

export default function AppLayout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [mobileOpen, setMobileOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate("/login", { replace: true });
  };

  const title = pageTitles[location.pathname] || "TaskSphere";

  return (
    <div className="min-h-screen bg-slate-50 text-slate-900">
      <Sidebar user={user} onLogout={handleLogout} />

      {mobileOpen && (
        <div
          className="fixed inset-0 z-40 bg-slate-950/50 backdrop-blur-sm md:hidden"
          onClick={() => setMobileOpen(false)}
        >
          <div
            className="h-full w-[260px]"
            onClick={(event) => event.stopPropagation()}
          >
            <Sidebar
              user={user}
              onLogout={handleLogout}
              onNavigate={() => setMobileOpen(false)}
              mobile
            />
          </div>
        </div>
      )}

      <div className="flex min-h-screen min-w-0 flex-1 flex-col md:pl-[260px]">
        <header className="sticky top-0 z-30 border-b border-slate-200/80 bg-white/90 backdrop-blur">
          <div className="flex min-h-[72px] items-center justify-between gap-4 px-4 py-3 sm:px-6 lg:px-8">
            <div className="flex min-w-0 items-center gap-3">
              <button
                type="button"
                onClick={() => setMobileOpen(true)}
                className="ts-focus inline-flex h-10 w-10 items-center justify-center rounded-xl border border-slate-200 text-slate-700 transition hover:bg-slate-100 md:hidden"
              >
                <Icon name="menu" />
              </button>
              <div className="min-w-0">
                <h1 className="truncate text-base font-semibold text-slate-950">
                  {title}
                </h1>
              </div>
            </div>
            <div className="flex items-center gap-2 sm:gap-3">
              <NotificationBell />
              <button
                type="button"
                onClick={handleLogout}
                className="ts-focus inline-flex items-center gap-2 rounded-xl border border-slate-200 bg-white px-4 py-2.5 text-sm font-semibold text-slate-700 shadow-sm transition hover:border-slate-300 hover:bg-slate-50"
              >
                <Icon name="logout" className="h-4 w-4" />
                <span className="hidden sm:inline">Logout</span>
              </button>
            </div>
          </div>
        </header>

        <main className="flex min-w-0 flex-1 px-5 py-5 md:px-6 md:py-6 xl:px-8">
          <div className="app-workspace">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  );
}

function Sidebar({ user, onLogout, onNavigate, mobile = false }) {
  return (
    <aside
      className={`${mobile ? "flex" : "fixed inset-y-0 left-0 hidden md:flex"} w-[260px] flex-col bg-slate-950 text-white`}
    >
      <div className="flex h-[104px] flex-col items-center justify-center border-b border-white/10 px-5 text-center">
        <div className="flex h-20 w-20 items-center justify-center overflow-hidden rounded-full bg-white/10 p-1 shadow-lg shadow-sky-500/20">
          <img src={logoSrc} alt="TaskSphere" className="h-full w-full rounded-full object-cover" />
        </div>
      </div>

      <nav className="flex-1 space-y-1 px-3 py-3">
        {links.map((link) => (
          <NavLink
            key={link.to}
            to={link.to}
            end={link.to === "/tasks"}
            onClick={onNavigate}
            className={({ isActive }) =>
              `group flex items-center gap-3 rounded-xl px-3 py-3 text-sm font-semibold transition ${
                isActive
                  ? "bg-white text-slate-950 shadow-sm"
                  : "text-slate-300 hover:bg-white/10 hover:text-white"
              }`
            }
          >
            <Icon name={link.icon} className="h-5 w-5 shrink-0" />
            {link.label}
          </NavLink>
        ))}
      </nav>

      <div className="border-t border-white/10 p-4">
        <div className="rounded-2xl bg-white/[0.08] p-3">
          <div className="flex items-center gap-3">
            <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-full bg-white/10 text-sm font-bold">
              {(user?.username || user?.email || "U").slice(0, 1).toUpperCase()}
            </div>
            <div className="min-w-0">
              <div className="truncate text-sm font-semibold">
                {user?.username || "User"}
              </div>
              <div className="truncate text-xs text-slate-400">
                {user?.email}
              </div>
            </div>
          </div>
          <button
            type="button"
            onClick={onLogout}
            className="mt-3 inline-flex w-full items-center justify-center gap-2 rounded-xl bg-white/10 px-3 py-2 text-sm font-semibold text-slate-200 transition hover:bg-white/15 hover:text-white"
          >
            <Icon name="logout" className="h-4 w-4" />
            Sign out
          </button>
        </div>
      </div>
    </aside>
  );
}
