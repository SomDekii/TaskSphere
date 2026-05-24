import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { notificationApi } from "../../services/api";
import { formatDateTime } from "../../utils/format";
import { useToast } from "../../context/ToastContext";
import Icon from "../ui/Icon";

export default function NotificationBell() {
  const [items, setItems] = useState([]);
  const [open, setOpen] = useState(false);
  const toast = useToast();

  const load = () => notificationApi.list().then(setItems).catch(() => {});

  useEffect(() => {
    load();
    const source = new EventSource(notificationApi.streamUrl());
    source.addEventListener("notification", (event) => {
      const notification = JSON.parse(event.data);
      setItems((current) => [notification, ...current]);
      toast.push(notification.message);
    });
    source.onerror = () => source.close();
    return () => source.close();
  }, []);

  const unread = items.filter((item) => !item.readStatus).length;

  return (
    <div className="relative">
      <button
        type="button"
        onClick={() => setOpen((value) => !value)}
        className="ts-focus relative inline-flex h-10 w-10 items-center justify-center rounded-xl border border-slate-200 bg-white text-slate-700 shadow-sm transition hover:border-slate-300 hover:bg-slate-50"
      >
        <Icon name="bell" className="h-5 w-5" />
        {unread > 0 && (
          <span className="absolute -right-1 -top-1 flex min-h-5 min-w-5 items-center justify-center rounded-full bg-rose-600 px-1.5 text-xs font-bold text-white">
            {unread}
          </span>
        )}
      </button>
      {open && (
        <div className="absolute right-0 mt-3 w-80 max-w-[calc(100vw-2rem)] rounded-2xl border border-slate-200 bg-white p-3 shadow-xl">
          <div className="mb-3 flex items-center justify-between px-1">
            <span className="font-semibold text-slate-950">Notifications</span>
            <Link to="/notifications" className="text-sm font-semibold text-sky-700 hover:text-sky-800" onClick={() => setOpen(false)}>
              View all
            </Link>
          </div>
          <div className="max-h-80 space-y-2 overflow-auto">
            {items.slice(0, 5).map((item) => (
              <div key={item.id} className="rounded-xl bg-slate-50 p-3">
                <div className="text-sm font-semibold text-slate-800">{item.message}</div>
                <div className="mt-1 text-xs text-slate-500">{formatDateTime(item.createdAt)}</div>
              </div>
            ))}
            {items.length === 0 && <div className="py-8 text-center text-sm text-slate-500">No notifications</div>}
          </div>
        </div>
      )}
    </div>
  );
}
