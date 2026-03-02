import { useState } from "react";
import { useNavigate } from "react-router";
import { Plus, Clock, Check, X, Bell } from "lucide-react";
import BottomNav from "../components/BottomNav";

const medicineReminders = [
  {
    id: 1,
    name: "Vitamin D3",
    dosage: "1 tablet",
    time: "08:00 AM",
    status: "taken",
    color: "from-green-400 to-emerald-500",
  },
  {
    id: 2,
    name: "Aspirin",
    dosage: "500mg",
    time: "01:00 PM",
    status: "upcoming",
    color: "from-[#F8BBD0] to-[#F48FB1]",
  },
  {
    id: 3,
    name: "Metformin",
    dosage: "2 tablets",
    time: "09:00 AM",
    status: "missed",
    color: "from-red-400 to-rose-500",
  },
  {
    id: 4,
    name: "Omega-3",
    dosage: "1 capsule",
    time: "08:30 PM",
    status: "upcoming",
    color: "from-[#90CAF9] to-[#64B5F6]",
  },
];

export default function HomeScreen() {
  const navigate = useNavigate();
  const [reminders, setReminders] = useState(medicineReminders);
  const [showSuggestionPopup, setShowSuggestionPopup] = useState(false);

  const handleMarkAsTaken = (id: number) => {
    setReminders((prev) =>
      prev.map((m) =>
        m.id === id ? { ...m, status: "taken", color: "from-green-400 to-emerald-500" } : m
      )
    );
  };

  const taken = reminders.filter((m) => m.status === "taken").length;
  const missed = reminders.filter((m) => m.status === "missed").length;
  const upcoming = reminders.filter((m) => m.status === "upcoming").length;

  return (
    <div className="min-h-screen w-full bg-[#F9F5FF] font-['Poppins'] pb-28">
      {/* Header */}
      <div className="bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] pt-12 pb-10 px-6 rounded-b-[32px] shadow-lg">
        <div className="flex items-center justify-between">
          <div>
            <p className="text-white/80 text-sm">Hello, Sarah 👋</p>
            <h1 className="text-2xl font-semibold text-white mt-0.5">
              Good Morning 🌸
            </h1>
          </div>
          <button className="w-10 h-10 rounded-full bg-white/20 flex items-center justify-center">
            <Bell className="w-5 h-5 text-white" />
          </button>
        </div>

        {/* Progress pill */}
        <div className="mt-5 bg-white/20 rounded-2xl px-4 py-3 flex items-center gap-3">
          <div className="flex-1">
            <div className="flex items-center justify-between mb-1.5">
              <span className="text-white text-xs font-medium">Today's Progress</span>
              <span className="text-white text-xs font-semibold">{taken}/{reminders.length} done</span>
            </div>
            <div className="h-2 bg-white/30 rounded-full overflow-hidden">
              <div
                className="h-full bg-white rounded-full transition-all"
                style={{ width: `${(taken / reminders.length) * 100}%` }}
              />
            </div>
          </div>
        </div>
      </div>

      {/* Quick Stats */}
      <div className="px-6 mt-6">
        <div className="bg-white rounded-3xl p-5 shadow-md grid grid-cols-3 gap-4">
          <div className="text-center">
            <div className="text-2xl font-semibold text-green-500">{taken}</div>
            <div className="text-xs text-gray-500 mt-1">Taken</div>
          </div>
          <div className="text-center border-x border-gray-100">
            <div className="text-2xl font-semibold text-red-400">{missed}</div>
            <div className="text-xs text-gray-500 mt-1">Missed</div>
          </div>
          <div className="text-center">
            <div className="text-2xl font-semibold text-[#90CAF9]">{upcoming}</div>
            <div className="text-xs text-gray-500 mt-1">Upcoming</div>
          </div>
        </div>
      </div>

      {/* Medicine Reminders */}
      <div className="px-6 mt-6 space-y-4">
        <h2 className="text-base font-semibold text-gray-800">Today's Reminders</h2>

        {reminders.map((medicine) => (
          <div
            key={medicine.id}
            className="bg-white rounded-3xl p-5 shadow-md flex items-center gap-4"
          >
            {/* Icon */}
            <div className={`w-12 h-12 rounded-2xl bg-gradient-to-br ${medicine.color} flex items-center justify-center flex-shrink-0 shadow-md`}>
              {medicine.status === "taken" && <Check className="w-6 h-6 text-white" />}
              {medicine.status === "missed" && <X className="w-6 h-6 text-white" />}
              {medicine.status === "upcoming" && <Clock className="w-6 h-6 text-white" />}
            </div>

            {/* Info */}
            <div className="flex-1 min-w-0">
              <h3 className="text-sm font-semibold text-gray-800 truncate">{medicine.name}</h3>
              <p className="text-xs text-gray-400 mt-0.5">{medicine.dosage}</p>
              <div className="flex items-center gap-1.5 mt-1">
                <Clock className="w-3.5 h-3.5 text-gray-400" />
                <span className="text-xs text-gray-500">{medicine.time}</span>
              </div>
            </div>

            {/* Action */}
            <div className="flex-shrink-0">
              {medicine.status === "taken" && (
                <span className="inline-flex items-center px-3 py-1.5 rounded-full bg-green-100 text-green-700 text-xs font-medium">
                  ✓ Taken
                </span>
              )}
              {medicine.status === "missed" && (
                <span className="inline-flex items-center px-3 py-1.5 rounded-full bg-red-100 text-red-500 text-xs font-medium">
                  ✗ Missed
                </span>
              )}
              {medicine.status === "upcoming" && (
                <button
                  onClick={() => handleMarkAsTaken(medicine.id)}
                  className="px-4 py-2 rounded-full bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] text-white text-xs font-medium shadow hover:shadow-md transition-all active:scale-95 whitespace-nowrap"
                >
                  Mark Taken
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Floating Action Button */}
      <button
        onClick={() => navigate("/add-medicine")}
        className="fixed bottom-24 right-6 w-14 h-14 rounded-full bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] flex items-center justify-center shadow-2xl hover:shadow-3xl transition-all active:scale-90"
      >
        <Plus className="w-7 h-7 text-white" />
      </button>

      <BottomNav />

      {/* Smart Suggestion Popup */}
      {showSuggestionPopup && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-6 z-50">
          <div className="bg-white rounded-3xl p-6 max-w-sm w-full shadow-2xl">
            <div className="text-center mb-6">
              <div className="w-16 h-16 mx-auto mb-4 rounded-full bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] flex items-center justify-center">
                <span className="text-3xl">🌼</span>
              </div>
              <p className="text-gray-700 leading-relaxed">
                We noticed a few missed doses 🌼 Would you like to adjust reminder time?
              </p>
            </div>
            <div className="space-y-3">
              <button className="w-full py-3 rounded-full bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] text-white font-medium shadow-lg hover:shadow-xl transition-all active:scale-95">
                Change Time
              </button>
              <button
                onClick={() => setShowSuggestionPopup(false)}
                className="w-full py-3 rounded-full bg-gray-100 text-gray-700 font-medium hover:bg-gray-200 transition-all active:scale-95"
              >
                Keep Same
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}