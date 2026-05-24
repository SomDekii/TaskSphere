import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useToast } from "../context/ToastContext";

export default function Signup() {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
  });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { register } = useAuth();
  const toast = useToast();

  const submit = async (event) => {
    event.preventDefault();
    if (form.password !== form.confirmPassword) {
      toast.push("Passwords do not match", "error");
      return;
    }
    setLoading(true);
    try {
      await register({
        firstName: form.firstName,
        lastName: form.lastName,
        username: form.username,
        email: form.email,
        password: form.password,
      });
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
        <h1 className="text-2xl font-bold text-slate-950">Create account</h1>
        <p className="mt-1 text-sm text-slate-500">Start using TaskSphere.</p>
        <div className="mt-6 grid gap-4 sm:grid-cols-2">
          <label className="block">
            <span className="text-sm font-medium text-slate-700">First name</span>
            <input required value={form.firstName} onChange={(e) => setForm({ ...form, firstName: e.target.value })} className="ts-focus mt-1 w-full rounded-md border border-slate-300 px-3 py-2" />
          </label>
          <label className="block">
            <span className="text-sm font-medium text-slate-700">Last name</span>
            <input required value={form.lastName} onChange={(e) => setForm({ ...form, lastName: e.target.value })} className="ts-focus mt-1 w-full rounded-md border border-slate-300 px-3 py-2" />
          </label>
        </div>
        <label className="mt-6 block">
          <span className="text-sm font-medium text-slate-700">Username</span>
          <input required minLength="3" value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} className="ts-focus mt-1 w-full rounded-md border border-slate-300 px-3 py-2" />
        </label>
        <label className="mt-4 block">
          <span className="text-sm font-medium text-slate-700">Email</span>
          <input required type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} className="ts-focus mt-1 w-full rounded-md border border-slate-300 px-3 py-2" />
        </label>
        <label className="mt-4 block">
          <span className="text-sm font-medium text-slate-700">Password</span>
          <input required minLength="6" type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} className="ts-focus mt-1 w-full rounded-md border border-slate-300 px-3 py-2" />
        </label>
        <label className="mt-4 block">
          <span className="text-sm font-medium text-slate-700">Confirm password</span>
          <input required minLength="6" type="password" value={form.confirmPassword} onChange={(e) => setForm({ ...form, confirmPassword: e.target.value })} className="ts-focus mt-1 w-full rounded-md border border-slate-300 px-3 py-2" />
        </label>
        <button disabled={loading} className="ts-focus mt-6 w-full rounded-md bg-sky-600 px-4 py-2 font-semibold text-white hover:bg-sky-700 disabled:opacity-60">
          {loading ? "Creating..." : "Register"}
        </button>
        <p className="mt-4 text-sm text-slate-500">
          Already registered? <Link className="font-semibold text-sky-700" to="/login">Login</Link>
        </p>
      </form>
    </div>
  );
}
