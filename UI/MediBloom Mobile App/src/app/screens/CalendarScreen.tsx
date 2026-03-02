import { useState } from "react";
import { useNavigate } from "react-router";
import { ChevronLeft, ChevronRight } from "lucide-react";
import BottomNav from "../components/BottomNav";

// Mock calendar data: status for each day (taken, missed, upcoming, or null)
const mockCalendarData: { [key: string]: "taken" | "missed" | "upcoming" | null } = {
  "2026-02-01": "taken",
  "2026-02-02": "taken",
  "2026-02-03": "missed",
  "2026-02-04": "taken",
  "2026-02-05": "taken",
  "2026-02-06": "missed",
  "2026-02-07": "taken",
  "2026-02-08": "taken",
  "2026-02-09": "taken",
  "2026-02-10": "missed",
  "2026-02-11": "taken",
  "2026-02-12": "taken",
  "2026-02-13": "taken",
  "2026-02-14": "taken",
  "2026-02-15": "taken",
  "2026-02-16": "missed",
  "2026-02-17": "taken",
  "2026-02-18": "taken",
  "2026-02-19": "taken",
  "2026-02-20": "taken",
  "2026-02-21": "missed",
  "2026-02-22": "taken",
  "2026-02-23": "upcoming",
};

export default function CalendarScreen() {
  const navigate = useNavigate();
  const [currentMonth, setCurrentMonth] = useState(new Date(2026, 1)); // February 2026

  const getDaysInMonth = (date: Date) => {
    const year = date.getFullYear();
    const month = date.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const daysInMonth = lastDay.getDate();
    const startingDayOfWeek = firstDay.getDay();

    const days: (number | null)[] = [];
    
    // Add empty slots for days before month starts
    for (let i = 0; i < startingDayOfWeek; i++) {
      days.push(null);
    }
    
    // Add all days of the month
    for (let i = 1; i <= daysInMonth; i++) {
      days.push(i);
    }
    
    return days;
  };

  const getDateKey = (day: number) => {
    const year = currentMonth.getFullYear();
    const month = String(currentMonth.getMonth() + 1).padStart(2, "0");
    const dayStr = String(day).padStart(2, "0");
    return `${year}-${month}-${dayStr}`;
  };

  const getStatusColor = (status: "taken" | "missed" | "upcoming" | null) => {
    switch (status) {
      case "taken":
        return "bg-green-400";
      case "missed":
        return "bg-red-400";
      case "upcoming":
        return "bg-[#F8BBD0]";
      default:
        return "bg-gray-100";
    }
  };

  const days = getDaysInMonth(currentMonth);
  const monthName = currentMonth.toLocaleString("default", { month: "long", year: "numeric" });

  const previousMonth = () => {
    setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() - 1));
  };

  const nextMonth = () => {
    setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1));
  };

  return (
    <div className="min-h-screen w-full bg-gradient-to-br from-[#F8BBD0]/20 to-[#90CAF9]/20 font-['Poppins'] pb-24">
      {/* Header */}
      <div className="bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] pt-12 pb-6 px-6 rounded-b-3xl shadow-lg">
        <div className="flex items-center justify-between mb-2">
          <h1 className="text-2xl font-semibold text-white">Calendar</h1>
        </div>
      </div>

      {/* Calendar */}
      <div className="px-6 py-6">
        <div className="bg-white rounded-3xl p-6 shadow-lg">
          {/* Month Navigation */}
          <div className="flex items-center justify-between mb-6">
            <button
              onClick={previousMonth}
              className="w-10 h-10 rounded-full bg-gray-100 hover:bg-gray-200 flex items-center justify-center transition-colors"
            >
              <ChevronLeft className="w-5 h-5 text-gray-600" />
            </button>
            <h2 className="text-lg font-semibold text-gray-800">{monthName}</h2>
            <button
              onClick={nextMonth}
              className="w-10 h-10 rounded-full bg-gray-100 hover:bg-gray-200 flex items-center justify-center transition-colors"
            >
              <ChevronRight className="w-5 h-5 text-gray-600" />
            </button>
          </div>

          {/* Day Headers */}
          <div className="grid grid-cols-7 gap-2 mb-3">
            {["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"].map((day) => (
              <div key={day} className="text-center text-sm font-medium text-gray-500">
                {day}
              </div>
            ))}
          </div>

          {/* Calendar Days */}
          <div className="grid grid-cols-7 gap-2">
            {days.map((day, index) => {
              if (day === null) {
                return <div key={`empty-${index}`} />;
              }

              const dateKey = getDateKey(day);
              const status = mockCalendarData[dateKey];
              const statusColor = getStatusColor(status);

              return (
                <div
                  key={day}
                  className={`aspect-square rounded-2xl ${statusColor} flex items-center justify-center text-sm font-medium ${
                    status ? "text-white shadow-md" : "text-gray-600"
                  } transition-all hover:scale-105 cursor-pointer`}
                >
                  {day}
                </div>
              );
            })}
          </div>

          {/* Legend */}
          <div className="mt-6 pt-6 border-t border-gray-200 grid grid-cols-3 gap-3">
            <div className="flex items-center gap-2">
              <div className="w-4 h-4 rounded-full bg-green-400" />
              <span className="text-xs text-gray-600">Taken</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-4 h-4 rounded-full bg-red-400" />
              <span className="text-xs text-gray-600">Missed</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-4 h-4 rounded-full bg-[#F8BBD0]" />
              <span className="text-xs text-gray-600">Upcoming</span>
            </div>
          </div>
        </div>

        {/* Monthly Summary */}
        <div className="mt-4 bg-white rounded-3xl p-6 shadow-lg">
          <h3 className="text-lg font-semibold text-gray-800 mb-4">Monthly Summary</h3>
          <div className="grid grid-cols-3 gap-4">
            <div className="text-center">
              <div className="text-2xl font-semibold text-green-500">18</div>
              <div className="text-xs text-gray-600 mt-1">Days Taken</div>
            </div>
            <div className="text-center border-x border-gray-200">
              <div className="text-2xl font-semibold text-red-500">4</div>
              <div className="text-xs text-gray-600 mt-1">Days Missed</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-semibold text-gray-800">82%</div>
              <div className="text-xs text-gray-600 mt-1">Adherence</div>
            </div>
          </div>
        </div>
      </div>

      {/* Bottom Navigation */}
      <BottomNav />
    </div>
  );
}