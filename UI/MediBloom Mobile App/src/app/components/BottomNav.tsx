import { useNavigate, useLocation } from "react-router";
import { Home, Calendar, BarChart2, Bot, User } from "lucide-react";

const tabs = [
  { label: "Home", icon: Home, path: "/home" },
  { label: "Calendar", icon: Calendar, path: "/calendar" },
  { label: "History", icon: BarChart2, path: "/history" },
  { label: "Assistant", icon: Bot, path: "/assistant" },
  { label: "Profile", icon: User, path: "/profile" },
];

export default function BottomNav() {
  const navigate = useNavigate();
  const location = useLocation();

  return (
    <div
      className="fixed bottom-0 left-0 right-0 bg-white rounded-t-[20px] shadow-[0_-4px_24px_rgba(0,0,0,0.08)] z-50"
      style={{ paddingBottom: "env(safe-area-inset-bottom, 0px)" }}
    >
      <div className="grid grid-cols-5 py-2 px-1">
        {tabs.map(({ label, icon: Icon, path }) => {
          const isActive = location.pathname === path;
          return (
            <button
              key={path}
              onClick={() => navigate(path)}
              className="flex flex-col items-center gap-1 py-2 transition-all active:scale-95"
            >
              {isActive ? (
                <div className="w-11 h-11 rounded-full bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] flex items-center justify-center shadow-md shadow-pink-200/60">
                  <Icon className="w-5 h-5 text-white" strokeWidth={2} />
                </div>
              ) : (
                <div className="w-11 h-11 flex items-center justify-center">
                  <Icon className="w-6 h-6 text-gray-400" strokeWidth={1.75} />
                </div>
              )}
              <span
                className={`text-[10px] font-medium tracking-wide transition-colors ${
                  isActive
                    ? "bg-gradient-to-r from-[#F48FB1] to-[#64B5F6] bg-clip-text text-transparent"
                    : "text-gray-400"
                }`}
              >
                {label}
              </span>
            </button>
          );
        })}
      </div>
    </div>
  );
}
