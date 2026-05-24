import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import TaskCard from "../components/tasks/TaskCard";
import EmptyState from "../components/ui/EmptyState";
import Icon from "../components/ui/Icon";
import { taskApi } from "../services/api";
import { useToast } from "../context/ToastContext";

export default function Tasks() {
  const [tasks, setTasks] = useState([]);
  const [params, setParams] = useState({ search: "", status: "", priority: "", type: "", sortBy: "" });
  const toast = useToast();

  const load = () => taskApi.list(params).then(setTasks).catch((error) => toast.push(error.message, "error"));

  useEffect(() => {
    load();
  }, [params.status, params.priority, params.type, params.sortBy]);

  const search = (event) => {
    event.preventDefault();
    load();
  };

  const remove = async (id) => {
    await taskApi.remove(id);
    setTasks((current) => current.filter((task) => task.id !== id));
    toast.push("Task deleted");
  };

  const status = async (id, nextStatus) => {
    const updated = await taskApi.updateStatus(id, nextStatus);
    setTasks((current) => current.map((task) => (task.id === id ? updated : task)));
    toast.push("Task status updated");
  };

  return (
    <div className="page-stack">
      <div className="flex w-full flex-col justify-between gap-4 sm:flex-row sm:items-end">
        <div>
          {/* <p className="text-sm font-semibold uppercase tracking-[0.16em] text-slate-400">Task board</p> */}
          {/* <h1 className="mt-1 text-lg font-bold tracking-tight text-slate-950">My Tasks</h1> */}
          <p className="mt-3 text-base text-slate-500">Search, filter, and update your work from one responsive board.</p>
        </div>
        <Link
          to="/tasks/new"
          className="ts-focus inline-flex items-center justify-center gap-2 rounded-xl bg-slate-950 px-5 py-3 text-base font-semibold text-white shadow-sm transition hover:-translate-y-0.5 hover:bg-slate-800"
        >
          <Icon name="plus" className="h-4 w-4" />
          New task
        </Link>
      </div>

      <form onSubmit={search} className="w-full rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:p-5">
        <div className="grid w-full gap-3 lg:grid-cols-[minmax(280px,0.9fr)_minmax(0,2fr)_auto] xl:gap-4">
          <label className="relative min-w-0 flex-1">
            <Icon name="search" className="absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-slate-400" />
            <input
              placeholder="Search by title or description"
              value={params.search}
              onChange={(event) => setParams({ ...params, search: event.target.value })}
              className="ts-focus min-h-[52px] w-full rounded-xl border border-slate-200 bg-slate-50 py-3.5 pl-12 pr-4 text-base text-slate-900 transition placeholder:text-slate-400 hover:border-slate-300"
            />
          </label>

          <div className="grid min-w-0 gap-3 sm:grid-cols-2 xl:grid-cols-4">
            <FilterSelect label="Status" value={params.status} onChange={(value) => setParams({ ...params, status: value })}>
              <option value="">All statuses</option>
              <option value="PENDING">Pending</option>
              <option value="IN_PROGRESS">In progress</option>
              <option value="COMPLETED">Completed</option>
            </FilterSelect>
            <FilterSelect label="Priority" value={params.priority} onChange={(value) => setParams({ ...params, priority: value })}>
              <option value="">All priorities</option>
              <option value="LOW">Low</option>
              <option value="MEDIUM">Medium</option>
              <option value="HIGH">High</option>
            </FilterSelect>
            <FilterSelect label="Type" value={params.type} onChange={(value) => setParams({ ...params, type: value })}>
              <option value="">All types</option>
              <option value="PERSONAL">Personal</option>
              <option value="WORK">Work</option>
              <option value="STUDY">Study</option>
            </FilterSelect>
            <FilterSelect label="Sort" value={params.sortBy} onChange={(value) => setParams({ ...params, sortBy: value })}>
              <option value="">No sort</option>
              <option value="deadline">Deadline</option>
              <option value="priority">Priority</option>
            </FilterSelect>
          </div>

          <button className="ts-focus inline-flex min-h-[52px] items-center justify-center gap-2 rounded-xl bg-sky-600 px-6 text-base font-semibold text-white shadow-sm transition hover:bg-sky-700">
            <Icon name="filter" className="h-4 w-4" />
            Apply
          </button>
        </div>
      </form>

      {tasks.length > 0 ? (
        <div className="grid w-full grid-cols-[repeat(auto-fit,minmax(min(100%,360px),1fr))] gap-5 xl:gap-6">
          {tasks.map((task) => (
            <TaskCard key={task.id} task={task} onDelete={remove} onStatus={status} />
          ))}
        </div>
      ) : (
        <EmptyState
          title="No tasks found"
          description="Create a task or adjust your filters to bring items back into view."
          action={
            <Link to="/tasks/new" className="inline-flex items-center gap-2 rounded-xl bg-slate-950 px-5 py-3 text-base font-semibold text-white transition hover:bg-slate-800">
              <Icon name="plus" className="h-4 w-4" />
              Create task
            </Link>
          }
        />
      )}
    </div>
  );
}

function FilterSelect({ label, value, onChange, children }) {
  return (
    <label className="block">
      <span className="sr-only">{label}</span>
      <select
        value={value}
        onChange={(event) => onChange(event.target.value)}
        className="ts-focus min-h-[52px] w-full rounded-xl border border-slate-200 bg-slate-50 px-4 text-base font-medium text-slate-700 transition hover:border-slate-300"
      >
        {children}
      </select>
    </label>
  );
}
