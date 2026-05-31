const statusStyles = {
  PENDING: "border-amber-200 bg-amber-50 text-amber-700",
  IN_PROGRESS: "border-sky-200 bg-sky-50 text-sky-700",
  COMPLETED: "border-emerald-200 bg-emerald-50 text-emerald-700",
  ARCHIVED: "border-slate-200 bg-slate-100 text-slate-600",
};

const priorityStyles = {
  LOW: "bg-emerald-500",
  MEDIUM: "bg-amber-500",
  HIGH: "bg-rose-500",
};

export function StatusBadge({ status }) {
  return (
    <span className={`inline-flex items-center rounded-full border px-3 py-1.5 text-sm font-semibold ${statusStyles[status]}`}>
      {status.replace("_", " ")}
    </span>
  );
}

export function PriorityBadge({ priority }) {
  return (
    <span className="inline-flex items-center gap-2 rounded-full bg-slate-100 px-3 py-1.5 text-sm font-semibold text-slate-700">
      <span className={`h-2 w-2 rounded-full ${priorityStyles[priority]}`} />
      {priority}
    </span>
  );
}
