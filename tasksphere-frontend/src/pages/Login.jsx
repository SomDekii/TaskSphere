import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useToast } from "../context/ToastContext";

export default function Login() {
  const [form, setForm] = useState({ email: "", password: "" });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();
  const toast = useToast();

  const submit = async (event) => {
    event.preventDefault();
    setLoading(true);
    try {
      await login(form);
      navigate("/dashboard", { replace: true });
    } catch (error) {
      toast.push(error.message, "error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-[#f4f7fb] px-4 py-12 sm:py-16">
      <form onSubmit={submit} className="w-full max-w-md rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
        <h1 className="text-2xl font-bold text-slate-950">Welcome back</h1>
        <p className="mt-1 text-sm text-slate-500">Sign in to TaskSphere.</p>
        <label className="mt-6 block">
          <span className="text-sm font-medium text-slate-700">Email address</span>
          <input
            required
            type="email"
            autoComplete="email"
            placeholder="you@gmail.com"
            value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })}
            className="ts-focus mt-1 w-full rounded-md border border-slate-300 px-3 py-2"
          />
        </label>
        <label className="mt-4 block">
          <span className="text-sm font-medium text-slate-700">Password</span>
          <input required type="password" autoComplete="current-password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} className="ts-focus mt-1 w-full rounded-md border border-slate-300 px-3 py-2" />
        </label>
        <button disabled={loading} className="ts-focus mt-6 w-full rounded-md bg-sky-600 px-4 py-2 font-semibold text-white hover:bg-sky-700 disabled:opacity-60">
          {loading ? "Signing in..." : "Login"}
        </button>
        <p className="mt-4 text-sm text-slate-500">
          Need an account? <Link className="font-semibold text-sky-700" to="/signup">Register</Link>
        </p>
      </form>
    </div>
  );
}
