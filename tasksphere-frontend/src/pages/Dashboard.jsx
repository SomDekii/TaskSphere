import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { dashboardApi, taskApi } from "../services/api";
import { formatDateTime } from "../utils/format";
import Icon from "../components/ui/Icon";
import EmptyState from "../components/ui/EmptyState";
import { useToast } from "../context/ToastContext";

const statConfig = {
  Total: { icon: "tasks", tone: "bg-slate-950 text-white" },
  Completed: { icon: "check", tone: "bg-emerald-500 text-white" },
  Pending: { icon: "clock", tone: "bg-amber-500 text-white" },
  "In progress": { icon: "board", tone: "bg-sky-500 text-white" },
};

export default function Dashboard() {
  const [data, setData] = useState(null);
  const toast = useToast();

  useEffect(() => {
    let active = true;

    Promise.allSettled([dashboardApi.get(), taskApi.list({ sortBy: "deadline" })])
      .then(([dashboardResult, tasksResult]) => {
        if (!active) return;

        const dashboard = dashboardResult.status === "fulfilled" ? dashboardResult.value : {};
        const tasks = tasksResult.status === "fulfilled" ? tasksResult.value : [];
        const upcomingDeadlines = getUpcomingDeadlines(tasks);

        setData({
          ...dashboard,
          upcomingDeadlines: upcomingDeadlines.length > 0
            ? upcomingDeadlines
            : dashboard.upcomingDeadlines || [],
        });

        if (dashboardResult.status === "rejected") {
          toast.push(dashboardResult.reason.message, "error");
        }
        if (tasksResult.status === "rejected") {
          toast.push(tasksResult.reason.message, "error");
        }
      })
      .catch((error) => {
        if (active) {
          toast.push(error.message, "error");
        }
      });

    return () => {
      active = false;
    };
  }, [toast]);

  const stats = [
    ["Total", data?.totalTasks ?? 0],
    ["Completed", data?.completedTasks ?? 0],
    ["Pending", data?.pendingTasks ?? 0],
    ["In progress", data?.inProgressTasks ?? 0],
  ];

  return (
    <div className="page-stack">
      <section className="w-full overflow-hidden rounded-3xl border border-slate-200 bg-white shadow-sm">
        <div className="grid w-full gap-6 p-5 sm:p-6 lg:grid-cols-[minmax(0,1.6fr)_minmax(340px,0.9fr)] lg:items-stretch lg:gap-8 lg:p-8 xl:p-9">
          <div className="min-w-0">
            <div className="inline-flex items-center gap-2 rounded-full bg-sky-50 px-3.5 py-1.5 text-sm font-semibold text-sky-700">
              <Icon name="calendar" className="h-4 w-4" />
              Today&apos;s workspace
            </div>
            <h1 className="mt-4 text-base font-bold tracking-tight text-slate-950 xl:text-xl">
              Plan, prioritize, and move tasks forward.
            </h1>
            <p className="mt-3 text-sm leading-6 text-slate-500">
              Keep deadlines, notifications, and work status visible without
              losing focus.
            </p>
            <div className="mt-7 flex flex-wrap gap-3">
              <Link
                className="ts-focus inline-flex items-center gap-2 rounded-xl bg-slate-950 px-5 py-3 text-sm font-semibold text-white shadow-sm transition hover:-translate-y-0.5 hover:bg-slate-800"
                to="/tasks/new"
              >
                <Icon name="plus" className="h-4 w-4" />
                New task
              </Link>
              <Link
                className="ts-focus inline-flex items-center gap-2 rounded-xl border border-slate-200 bg-white px-5 py-3 text-sm font-semibold text-slate-700 shadow-sm transition hover:-translate-y-0.5 hover:bg-slate-50"
                to="/tasks"
              >
                <Icon name="board" className="h-4 w-4" />
                View board
              </Link>
            </div>
          </div>

          <div className="flex min-w-0 flex-col justify-center rounded-2xl bg-slate-950 p-6 text-white">
            <div className="text-sm font-medium text-slate-400">
              Completion
            </div>
            <div className="mt-3 text-4xl font-bold">
              {completionRate(data)}%
            </div>
            <div className="mt-5 h-3 rounded-full bg-white/10">
              <div
                className="h-3 rounded-full bg-emerald-400"
                style={{ width: `${completionRate(data)}%` }}
              />
            </div>
            <p className="mt-5 text-sm leading-6 text-slate-300">
              Completed tasks compared with your total workload.
            </p>
          </div>
        </div>
      </section>

      <section className="grid w-full gap-5 sm:grid-cols-2 lg:grid-cols-4 xl:gap-6">
        {stats.map(([label, value]) => (
          <StatCard key={label} label={label} value={value} />
        ))}
      </section>

      <section className="grid w-full gap-5 xl:grid-cols-2 xl:gap-6">
        <Panel
          title="Upcoming deadlines"
          action={
            <Link
              to="/tasks"
              className="text-sm font-semibold text-sky-700 hover:text-sky-800"
            >
              Manage
            </Link>
          }
        >
          <div className="section-stack">
            {data?.upcomingDeadlines?.map((task) => (
              <Link
                key={task.id}
                to={`/tasks/${task.id}`}
                className="group flex items-start justify-between gap-4 rounded-2xl border border-slate-100 bg-slate-50 p-5 transition hover:-translate-y-0.5 hover:border-sky-200 hover:bg-white hover:shadow-sm"
              >
                <div className="min-w-0">
                  <div className="truncate text-base font-semibold text-slate-950 group-hover:text-sky-700">
                    {task.title}
                  </div>
                  <div className="mt-2 flex items-center gap-2 text-sm text-slate-500">
                    <Icon name="calendar" className="h-4 w-4" />
                    {formatDateTime(task.deadline)}
                  </div>
                </div>
                <Icon
                  name="chevronDown"
                  className="h-4 w-4 -rotate-90 text-slate-400"
                />
              </Link>
            ))}
            {data?.upcomingDeadlines?.length === 0 && (
              <EmptyState
                title="No upcoming deadlines"
                description="Tasks with deadlines will appear here as soon as they are created."
              />
            )}
          </div>
        </Panel>

        <Panel
          title="Recent notifications"
          action={
            <Link
              to="/notifications"
              className="text-sm font-semibold text-sky-700 hover:text-sky-800"
            >
              View all
            </Link>
          }
        >
          <div className="section-stack">
            {data?.recentNotifications?.map((item) => (
              <div
                key={item.id}
                className="rounded-2xl border border-slate-100 bg-slate-50 p-5"
              >
                <div className="text-base font-semibold text-slate-950">
                  {item.message}
                </div>
                <div className="mt-2 flex items-center gap-2 text-sm text-slate-500">
                  <Icon name="bell" className="h-4 w-4" />
                  {formatDateTime(item.createdAt)}
                </div>
              </div>
            ))}
            {data?.recentNotifications?.length === 0 && (
              <EmptyState
                title="No notifications yet"
                description="Important task activity and deadline reminders will show here."
              />
            )}
          </div>
        </Panel>
      </section>
    </div>
  );
}

function StatCard({ label, value }) {
  const config = statConfig[label];

  return (
    <div className="w-full rounded-2xl border border-slate-200 bg-white p-6 shadow-sm transition hover:-translate-y-0.5 hover:shadow-md">
      <div className="flex items-center justify-between gap-4">
        <div>
          <div className="text-sm font-medium text-slate-500">{label}</div>
          <div className="mt-3 text-3xl font-bold tracking-tight text-slate-950">
            {value}
          </div>
        </div>
        <div
          className={`flex h-12 w-12 items-center justify-center rounded-2xl ${config.tone}`}
        >
          <Icon name={config.icon} className="h-6 w-6" />
        </div>
      </div>
    </div>
  );
}

function Panel({ title, action, children }) {
  return (
    <section className="w-full rounded-3xl border border-slate-200 bg-white p-6 shadow-sm">
      <div className="mb-5 flex items-center justify-between gap-4">
        <h2 className="text-lg font-semibold text-slate-950">{title}</h2>
        {action}
      </div>
      {children}
    </section>
  );
}

function completionRate(data) {
  if (!data?.totalTasks) return 0;
  return Math.round((data.completedTasks / data.totalTasks) * 100);
}

function getUpcomingDeadlines(tasks) {
  const now = Date.now();

  return tasks
    .filter((task) => task.deadline && task.status !== "COMPLETED")
    .filter((task) => new Date(task.deadline).getTime() >= now)
    .sort((first, second) => new Date(first.deadline) - new Date(second.deadline))
    .slice(0, 5);
}
