import { useEffect, useState } from "react";
import { toDateTimeLocal } from "../../utils/format";

const emptyTask = {
  title: "",
  description: "",
  deadline: "",
  priority: "MEDIUM",
  status: "PENDING",
  taskType: "PERSONAL",
};

export default function TaskForm({ initialTask, onSubmit, submitting }) {
  const [form, setForm] = useState(emptyTask);

  useEffect(() => {
    if (initialTask) {
      setForm({
        title: initialTask.title || "",
        description: initialTask.description || "",
        deadline: toDateTimeLocal(initialTask.deadline),
        priority: initialTask.priority || "MEDIUM",
        status: initialTask.status || "PENDING",
        taskType: initialTask.taskType || "PERSONAL",
      });
    }
  }, [initialTask]);

  const update = (event) => {
    setForm((current) => ({ ...current, [event.target.name]: event.target.value }));
  };

  const submit = (event) => {
    event.preventDefault();
    onSubmit({ ...form, deadline: new Date(form.deadline).toISOString() });
  };

  return (
    <form onSubmit={submit} className="w-full rounded-3xl border border-slate-200 bg-white p-5 shadow-sm sm:p-6 lg:p-7">
      <div className="grid w-full gap-5 md:grid-cols-2 xl:grid-cols-4 xl:gap-6">
        <Field label="Title" className="md:col-span-2 xl:col-span-4">
          <input required name="title" value={form.title} onChange={update} className={inputClass} placeholder="Write a clear task title" />
        </Field>
        <Field label="Description" className="md:col-span-2 xl:col-span-4">
          <textarea name="description" value={form.description} onChange={update} rows="5" className={inputClass} placeholder="Add context, scope, or acceptance notes" />
        </Field>
        <Field label="Deadline">
          <input required type="datetime-local" name="deadline" value={form.deadline} onChange={update} className={inputClass} />
        </Field>
        <Field label="Priority">
          <select name="priority" value={form.priority} onChange={update} className={inputClass}>
            <option>LOW</option>
            <option>MEDIUM</option>
            <option>HIGH</option>
          </select>
        </Field>
        <Field label="Status">
          <select name="status" value={form.status} onChange={update} className={inputClass}>
            <option>PENDING</option>
            <option>IN_PROGRESS</option>
            <option>COMPLETED</option>
          </select>
        </Field>
        <Field label="Task type">
          <select name="taskType" value={form.taskType} onChange={update} className={inputClass}>
            <option>PERSONAL</option>
            <option>WORK</option>
            <option>STUDY</option>
          </select>
        </Field>
      </div>
      <button disabled={submitting} className="ts-focus mt-6 rounded-xl bg-sky-600 px-5 py-2.5 text-sm font-semibold text-white shadow-sm transition hover:bg-sky-700 disabled:cursor-not-allowed disabled:opacity-60">
        {submitting ? "Saving..." : "Save task"}
      </button>
    </form>
  );
}

function Field({ label, className = "", children }) {
  return (
    <label className={className}>
      <span className="text-sm font-semibold text-slate-700">{label}</span>
      <div className="mt-2">{children}</div>
    </label>
  );
}

const inputClass = "ts-focus w-full rounded-xl border border-slate-200 bg-slate-50 px-3 py-2.5 text-sm text-slate-900 transition placeholder:text-slate-400 hover:border-slate-300";
