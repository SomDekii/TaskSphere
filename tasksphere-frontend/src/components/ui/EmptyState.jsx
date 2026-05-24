import Icon from "./Icon";

export default function EmptyState({ title, description, action }) {
  return (
    <div className="flex w-full flex-col items-center rounded-2xl border border-dashed border-slate-300 bg-white p-10 text-center shadow-sm">
      <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-slate-100 text-slate-500">
        <Icon name="tasks" />
      </div>
      <h2 className="mt-4 text-lg font-semibold text-slate-950">{title}</h2>
      <p className="mt-2 text-sm leading-6 text-slate-500">{description}</p>
      {action && <div className="mt-5">{action}</div>}
    </div>
  );
}
