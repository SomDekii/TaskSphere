const paths = {
  bell: "M15 17h5l-1.4-1.4A2 2 0 0 1 18 14.2V11a6 6 0 1 0-12 0v3.2a2 2 0 0 1-.6 1.4L4 17h5m6 0a3 3 0 0 1-6 0m3-14v2",
  board: "M4 5a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v14H4V5Zm5 2H7v10h2V7Zm4 0h-2v6h2V7Zm4 0h-2v8h2V7Z",
  calendar: "M7 2v3m10-3v3M4 9h16M5 5h14a1 1 0 0 1 1 1v14H4V6a1 1 0 0 1 1-1Z",
  check: "m5 13 4 4L19 7",
  chevronDown: "m6 9 6 6 6-6",
  clock: "M12 6v6l4 2m5-2a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z",
  close: "M6 6l12 12M18 6 6 18",
  dashboard: "M4 13h7V4H4v9Zm9 7h7V4h-7v16ZM4 20h7v-5H4v5Z",
  filter: "M4 5h16l-6 7v5l-4 2v-7L4 5Z",
  flag: "M5 21V4h9l1 3h4v9h-9l-1-3H5",
  logout: "M10 17l5-5-5-5m5 5H3m7 8h8a3 3 0 0 0 3-3V7a3 3 0 0 0-3-3h-8",
  menu: "M4 6h16M4 12h16M4 18h16",
  plus: "M12 5v14m-7-7h14",
  search: "m21 21-5.2-5.2M10.5 18a7.5 7.5 0 1 1 0-15 7.5 7.5 0 0 1 0 15Z",
  tasks: "M9 6h11M9 12h11M9 18h11M4 6h.01M4 12h.01M4 18h.01",
  user: "M20 21a8 8 0 0 0-16 0m12-13a4 4 0 1 1-8 0 4 4 0 0 1 8 0Z",
};

export default function Icon({ name, className = "h-5 w-5" }) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d={paths[name]} stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}
