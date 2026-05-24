import { useState } from "react";
import { useNavigate } from "react-router-dom";
import TaskForm from "../components/tasks/TaskForm";
import { taskApi } from "../services/api";
import { useToast } from "../context/ToastContext";

export default function CreateTask() {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const toast = useToast();

  const submit = async (payload) => {
    setLoading(true);
    try {
      const task = await taskApi.create(payload);
      toast.push("Task created");
      navigate(`/tasks/${task.id}`);
    } catch (error) {
      toast.push(error.message, "error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-stack">
      {/* <div>
        <p className="text-sm font-semibold uppercase tracking-[0.16em] text-slate-400">
          Task setup
        </p>
        <h1 className="mt-1 text-lg font-bold tracking-tight text-slate-950">
          Create task
        </h1>
      </div> */}
      <TaskForm onSubmit={submit} submitting={loading} />
    </div>
  );
}
