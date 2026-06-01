import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import TaskForm from "../components/tasks/TaskForm";
import { taskApi } from "../services/api";
import { useToast } from "../context/ToastContext";

export default function EditTask() {
  const [task, setTask] = useState(null);
  const [loading, setLoading] = useState(false);
  const { id } = useParams();
  const navigate = useNavigate();
  const toast = useToast();

  useEffect(() => {
    taskApi
      .get(id)
      .then(setTask)
      .catch((error) => toast.push(error.message, "error"));
  }, [id]);

  const submit = async (payload) => {
    setLoading(true);
    try {
      const updated = await taskApi.update(id, payload);
      toast.push("Task updated");
      navigate(`/tasks/${updated.id}`);
    } catch (error) {
      toast.push(error.message, "error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-stack">
      <div>
        <h1 className="mt-1 text-base font-light tracking-tight text-slate-950">
          Edit task
        </h1>
      </div>
      <TaskForm initialTask={task} onSubmit={submit} submitting={loading} />
    </div>
  );
}
