import { useEffect, useState } from "react";
import { notificationApi } from "../services/api";
import { formatDateTime } from "../utils/format";
import { useToast } from "../context/ToastContext";
import EmptyState from "../components/ui/EmptyState";
import Icon from "../components/ui/Icon";

export default function Notifications() {
  const [items, setItems] = useState([]);
  const toast = useToast();

  const load = () =>
    notificationApi
      .list()
      .then(setItems)
      .catch((error) => toast.push(error.message, "error"));

  useEffect(() => {
    load();
  }, []);

  const markRead = async (id) => {
    const updated = await notificationApi.markRead(id);
    setItems((current) =>
      current.map((item) => (item.id === id ? updated : item)),
    );
  };

  const remove = async (id) => {
    await notificationApi.remove(id);
    setItems((current) => current.filter((item) => item.id !== id));
  };

  return (
    <div className="page-stack">
      {/* <div>
        <p className="text-sm font-semibold uppercase tracking-[0.16em] text-slate-400">Activity center</p>
        <h1 className="mt-1 text-3xl font-bold tracking-tight text-slate-950">Notifications</h1>
      </div> */}
      <div className="section-stack w-full">
        {items.map((item) => (
          <div
            key={item.id}
            className="flex w-full flex-col justify-between gap-4 rounded-2xl border border-slate-200 bg-white p-5 shadow-sm transition hover:-translate-y-0.5 hover:shadow-md sm:flex-row sm:items-center"
          >
            <div className="flex min-w-0 items-start gap-3">
              <div
                className={`mt-0.5 flex h-9 w-9 shrink-0 items-center justify-center rounded-xl ${item.readStatus ? "bg-slate-100 text-slate-500" : "bg-sky-50 text-sky-700"}`}
              >
                <Icon name="bell" className="h-4 w-4" />
              </div>
              <div className="min-w-0">
                <div className="font-semibold text-slate-950">
                  {item.message}
                </div>
                <div className="mt-1 text-sm text-slate-500">
                  {item.type} - {formatDateTime(item.createdAt)}
                </div>
              </div>
            </div>
            <div className="flex gap-2">
              {!item.readStatus && (
                <button
                  onClick={() => markRead(item.id)}
                  className="rounded-xl border border-slate-200 px-3 py-2 text-sm font-semibold text-slate-700 transition hover:bg-slate-50"
                >
                  Mark read
                </button>
              )}
              <button
                onClick={() => remove(item.id)}
                className="rounded-xl border border-rose-200 px-3 py-2 text-sm font-semibold text-rose-700 transition hover:bg-rose-50"
              >
                Delete
              </button>
            </div>
          </div>
        ))}
        {items.length === 0 && (
          <EmptyState
            title="No notifications"
            description="Task updates and reminders will appear here."
          />
        )}
      </div>
    </div>
  );
}
