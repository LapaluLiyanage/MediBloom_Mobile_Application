import { ChevronRight, Bell, Moon, LogOut, Edit, Shield, HelpCircle, Clock } from "lucide-react";
import { useNavigate } from "react-router";
import { useState } from "react";
import BottomNav from "../components/BottomNav";

export default function ProfileScreen() {
  const navigate = useNavigate();
  const [darkMode, setDarkMode] = useState(false);
  const [notifications, setNotifications] = useState(true);
  const [reminderSound, setReminderSound] = useState(true);

  const handleLogout = () => {
    navigate("/login");
  };

  return (
    <div className="min-h-screen w-full bg-[#F9F5FF] font-['Poppins'] pb-28">
      {/* Header */}
      <div className="bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] pt-12 pb-16 px-6 rounded-b-[32px] shadow-lg">
        <h1 className="text-2xl font-semibold text-white">Profile & Settings</h1>
      </div>

      {/* Avatar card — overlapping header */}
      <div className="px-6 -mt-10">
        <div className="bg-white rounded-3xl p-6 shadow-lg">
          <div className="flex items-center gap-4">
            {/* Avatar */}
            <div className="relative">
              <div className="w-20 h-20 rounded-full bg-gradient-to-br from-[#F8BBD0] to-[#90CAF9] flex items-center justify-center text-3xl shadow-lg select-none">
                🌸
              </div>
              <button className="absolute -bottom-1 -right-1 w-7 h-7 rounded-full bg-white shadow-md flex items-center justify-center border-2 border-[#F8BBD0]">
                <Edit className="w-3.5 h-3.5 text-[#F8BBD0]" />
              </button>
            </div>

            {/* Info */}
            <div className="flex-1 min-w-0">
              <h2 className="text-lg font-semibold text-gray-800 truncate">Sarah Johnson</h2>
              <p className="text-sm text-gray-400 mt-0.5 truncate">sarah.j@example.com</p>
              <span className="inline-flex items-center mt-2 px-3 py-1 rounded-full bg-gradient-to-r from-[#F8BBD0]/30 to-[#90CAF9]/30 text-xs font-medium text-gray-600">
                🌸 Active Member
              </span>
            </div>
          </div>

          {/* Quick Stats */}
          <div className="grid grid-cols-3 gap-3 mt-5 pt-5 border-t border-gray-100">
            <div className="text-center">
              <div className="text-xl font-semibold text-gray-800">12</div>
              <div className="text-xs text-gray-400 mt-0.5">Day Streak</div>
            </div>
            <div className="text-center border-x border-gray-100">
              <div className="text-xl font-semibold text-gray-800">85%</div>
              <div className="text-xs text-gray-400 mt-0.5">Adherence</div>
            </div>
            <div className="text-center">
              <div className="text-xl font-semibold text-gray-800">4</div>
              <div className="text-xs text-gray-400 mt-0.5">Medicines</div>
            </div>
          </div>
        </div>
      </div>

      <div className="px-6 mt-4 space-y-4">

        {/* Notification Settings */}
        <div className="bg-white rounded-3xl p-6 shadow-md space-y-1">
          <h3 className="text-sm font-semibold text-gray-500 uppercase tracking-wide mb-4">Notifications</h3>

          {/* Push Notifications */}
          <div className="flex items-center justify-between py-3">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-2xl bg-[#F8BBD0]/20 flex items-center justify-center">
                <Bell className="w-5 h-5 text-[#F48FB1]" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-800">Push Notifications</p>
                <p className="text-xs text-gray-400">Remind me to take medicine</p>
              </div>
            </div>
            <button
              onClick={() => setNotifications(!notifications)}
              className={`w-13 h-7 rounded-full transition-all relative flex-shrink-0 ${
                notifications ? "bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9]" : "bg-gray-200"
              }`}
              style={{ width: "52px" }}
            >
              <div
                className={`absolute top-0.5 w-6 h-6 bg-white rounded-full shadow-md transition-transform ${
                  notifications ? "translate-x-6" : "translate-x-0.5"
                }`}
              />
            </button>
          </div>

          <div className="h-px bg-gray-100 mx-1" />

          {/* Reminder Sound */}
          <div className="flex items-center justify-between py-3">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-2xl bg-[#90CAF9]/20 flex items-center justify-center">
                <Clock className="w-5 h-5 text-[#64B5F6]" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-800">Reminder Sound</p>
                <p className="text-xs text-gray-400">Play sound for reminders</p>
              </div>
            </div>
            <button
              onClick={() => setReminderSound(!reminderSound)}
              className={`w-13 h-7 rounded-full transition-all relative flex-shrink-0 ${
                reminderSound ? "bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9]" : "bg-gray-200"
              }`}
              style={{ width: "52px" }}
            >
              <div
                className={`absolute top-0.5 w-6 h-6 bg-white rounded-full shadow-md transition-transform ${
                  reminderSound ? "translate-x-6" : "translate-x-0.5"
                }`}
              />
            </button>
          </div>

          <div className="h-px bg-gray-100 mx-1" />

          {/* Dark Mode */}
          <div className="flex items-center justify-between py-3">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-2xl bg-gray-100 flex items-center justify-center">
                <Moon className="w-5 h-5 text-gray-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-800">Dark Mode</p>
                <p className="text-xs text-gray-400">Switch to dark theme</p>
              </div>
            </div>
            <button
              onClick={() => setDarkMode(!darkMode)}
              className={`h-7 rounded-full transition-all relative flex-shrink-0 ${
                darkMode ? "bg-gray-700" : "bg-gray-200"
              }`}
              style={{ width: "52px" }}
            >
              <div
                className={`absolute top-0.5 w-6 h-6 bg-white rounded-full shadow-md transition-transform ${
                  darkMode ? "translate-x-6" : "translate-x-0.5"
                }`}
              />
            </button>
          </div>
        </div>

        {/* More Options */}
        <div className="bg-white rounded-3xl shadow-md overflow-hidden">
          <h3 className="text-sm font-semibold text-gray-500 uppercase tracking-wide px-6 pt-5 pb-3">More</h3>

          <button className="w-full flex items-center gap-4 px-6 py-4 hover:bg-gray-50 transition-colors border-t border-gray-100">
            <div className="w-10 h-10 rounded-2xl bg-[#F8BBD0]/20 flex items-center justify-center">
              <Clock className="w-5 h-5 text-[#F48FB1]" />
            </div>
            <span className="flex-1 text-sm font-medium text-gray-800 text-left">Reminder Settings</span>
            <ChevronRight className="w-4 h-4 text-gray-300" />
          </button>

          <button className="w-full flex items-center gap-4 px-6 py-4 hover:bg-gray-50 transition-colors border-t border-gray-100">
            <div className="w-10 h-10 rounded-2xl bg-[#90CAF9]/20 flex items-center justify-center">
              <Shield className="w-5 h-5 text-[#64B5F6]" />
            </div>
            <span className="flex-1 text-sm font-medium text-gray-800 text-left">Privacy & Data</span>
            <ChevronRight className="w-4 h-4 text-gray-300" />
          </button>

          <button className="w-full flex items-center gap-4 px-6 py-4 hover:bg-gray-50 transition-colors border-t border-gray-100">
            <div className="w-10 h-10 rounded-2xl bg-purple-50 flex items-center justify-center">
              <HelpCircle className="w-5 h-5 text-purple-400" />
            </div>
            <span className="flex-1 text-sm font-medium text-gray-800 text-left">Help & Support</span>
            <ChevronRight className="w-4 h-4 text-gray-300" />
          </button>
        </div>

        {/* Logout */}
        <button
          onClick={handleLogout}
          className="w-full bg-red-50 hover:bg-red-100 rounded-3xl p-5 flex items-center justify-center gap-3 text-red-500 font-medium shadow-sm transition-all active:scale-95"
        >
          <LogOut className="w-5 h-5" />
          Logout
        </button>

        {/* Version */}
        <p className="text-center text-xs text-gray-300 pb-2">MediBloom v1.0.0</p>
      </div>

      <BottomNav />
    </div>
  );
}
