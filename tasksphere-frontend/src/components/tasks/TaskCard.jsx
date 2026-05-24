import { Link } from "react-router-dom";
import { formatDateTime } from "../../utils/format";
import Icon from "../ui/Icon";
import { PriorityBadge, StatusBadge } from "../ui/TaskBadges";

export default function TaskCard({ task, onDelete, onStatus }) {
  return (
    <article className="group rounded-2xl border border-slate-200 bg-white p-6 shadow-sm transition hover:-translate-y-0.5 hover:border-slate-300 hover:shadow-md">
      <div className="flex items-start justify-between gap-4">
        <div className="min-w-0">
          <Link to={`/tasks/${task.id}`} className="line-clamp-1 text-lg font-semibold text-slate-950 transition group-hover:text-sky-700">
            {task.title}
          </Link>
          <p className="mt-2 line-clamp-2 text-base leading-7 text-slate-500">{task.description || "No description"}</p>
        </div>
        <PriorityBadge priority={task.priority} />
      </div>

      <div className="mt-6 flex flex-wrap items-center gap-2">
        <StatusBadge status={task.status} />
        <span className="inline-flex items-center rounded-full bg-slate-100 px-2.5 py-1 text-xs font-semibold text-slate-600">
          {task.taskType}
        </span>
      </div>

      <div className="mt-6 flex items-center gap-2 rounded-xl bg-slate-50 px-4 py-3 text-base text-slate-500">
        <Icon name="calendar" className="h-4 w-4 text-slate-400" />
        Due {formatDateTime(task.deadline)}
      </div>

      <div className="mt-6 flex flex-wrap gap-2">
        <Link className="rounded-xl border border-slate-200 px-4 py-2.5 text-sm font-semibold text-slate-700 transition hover:bg-slate-50" to={`/tasks/${task.id}/edit`}>
          Edit
        </Link>
        {task.status === "PENDING" && (
          <button onClick={() => onStatus(task.id, "IN_PROGRESS")} className="rounded-xl border border-sky-200 px-4 py-2.5 text-sm font-semibold text-sky-700 transition hover:bg-sky-50">
            Start
          </button>
        )}
        {task.status === "IN_PROGRESS" && (
          <button onClick={() => onStatus(task.id, "COMPLETED")} className="rounded-xl border border-emerald-200 px-4 py-2.5 text-sm font-semibold text-emerald-700 transition hover:bg-emerald-50">
            Complete
          </button>
        )}
        <button onClick={() => onDelete(task.id)} className="rounded-xl border border-rose-200 px-4 py-2.5 text-sm font-semibold text-rose-700 transition hover:bg-rose-50">
          Delete
        </button>
      </div>
    </article>
  );
}
