import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { taskApi } from "../services/api";
import { formatDateTime } from "../utils/format";
import { useToast } from "../context/ToastContext";
import Icon from "../components/ui/Icon";
import { PriorityBadge, StatusBadge } from "../components/ui/TaskBadges";

export default function TaskDetails() {
  const [task, setTask] = useState(null);
  const { id } = useParams();
  const toast = useToast();

  useEffect(() => {
    taskApi.get(id).then(setTask).catch((error) => toast.push(error.message, "error"));
  }, [id]);

  if (!task) return <div className="rounded-2xl border border-slate-200 bg-white p-5 text-slate-500 shadow-sm sm:p-6">Loading task...</div>;

  return (
    <div className="w-full rounded-3xl border border-slate-200 bg-white p-5 shadow-sm sm:p-6 lg:p-7">
      <div className="flex flex-col justify-between gap-5 sm:flex-row sm:items-start">
        <div className="min-w-0">
          <div className="flex flex-wrap items-center gap-2">
            <StatusBadge status={task.status} />
            <PriorityBadge priority={task.priority} />
          </div>
          <h1 className="mt-4 text-3xl font-bold tracking-tight text-slate-950">{task.title}</h1>
          <p className="mt-3 max-w-none text-sm leading-6 text-slate-600">{task.description || "No description"}</p>
        </div>
        <Link className="ts-focus inline-flex items-center justify-center rounded-xl bg-sky-600 px-4 py-2.5 text-sm font-semibold text-white shadow-sm transition hover:bg-sky-700" to={`/tasks/${task.id}/edit`}>
          Edit
        </Link>
      </div>
      <dl className="mt-7 grid w-full gap-4 sm:grid-cols-2 xl:grid-cols-4 xl:gap-6">
        <Info icon="calendar" label="Deadline" value={formatDateTime(task.deadline)} />
        <Info icon="flag" label="Priority" value={task.priority} />
        <Info icon="board" label="Status" value={task.status.replace("_", " ")} />
        <Info icon="tasks" label="Type" value={task.taskType} />
      </dl>
    </div>
  );
}

function Info({ icon, label, value }) {
  return (
    <div className="rounded-2xl border border-slate-100 bg-slate-50 p-4">
      <div className="flex items-center gap-2 text-sm font-medium text-slate-500">
        <Icon name={icon} className="h-4 w-4" />
        <dt>{label}</dt>
      </div>
      <dd className="mt-2 font-semibold text-slate-950">{value}</dd>
    </div>
  );
}
