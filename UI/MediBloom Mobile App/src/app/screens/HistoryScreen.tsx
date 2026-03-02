import { TrendingUp, Award } from "lucide-react";
import { PieChart, Pie, Cell, ResponsiveContainer } from "recharts";
import BottomNav from "../components/BottomNav";

const chartData = [
  { name: "Taken", value: 75, color: "#4ade80" },
  { name: "Missed", value: 25, color: "#f87171" },
];

const dailyLog = [
  { date: "Feb 23, 2026", medicines: ["Vitamin D3", "Omega-3"], status: "upcoming", taken: 0, total: 2 },
  { date: "Feb 22, 2026", medicines: ["Vitamin D3", "Aspirin", "Omega-3"], status: "complete", taken: 3, total: 3 },
  { date: "Feb 21, 2026", medicines: ["Vitamin D3", "Aspirin", "Metformin", "Omega-3"], status: "partial", taken: 3, total: 4 },
  { date: "Feb 20, 2026", medicines: ["Vitamin D3", "Aspirin", "Omega-3"], status: "complete", taken: 3, total: 3 },
  { date: "Feb 19, 2026", medicines: ["Vitamin D3", "Aspirin", "Metformin", "Omega-3"], status: "complete", taken: 4, total: 4 },
];

export default function HistoryScreen() {
  return (
    <div className="min-h-screen w-full bg-gradient-to-br from-[#F8BBD0]/20 to-[#90CAF9]/20 font-['Poppins'] pb-24">
      {/* Header */}
      <div className="bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] pt-12 pb-6 px-6 rounded-b-3xl shadow-lg">
        <h1 className="text-2xl font-semibold text-white">History & Progress</h1>
      </div>

      <div className="px-6 py-6 space-y-4">
        {/* Weekly Progress */}
        <div className="bg-white rounded-3xl p-6 shadow-lg">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold text-gray-800">Weekly Progress</h2>
            <TrendingUp className="w-5 h-5 text-green-500" />
          </div>
          <div className="text-center">
            <div className="text-5xl font-bold bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] bg-clip-text text-transparent mb-2">
              85%
            </div>
            <p className="text-sm text-gray-600">Adherence Rate</p>
            <div className="mt-4 w-full h-3 bg-gray-200 rounded-full overflow-hidden">
              <div className="h-full w-[85%] bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] rounded-full" />
            </div>
          </div>
        </div>

        {/* Pie Chart */}
        <div className="bg-white rounded-3xl p-6 shadow-lg">
          <h2 className="text-lg font-semibold text-gray-800 mb-4">Taken vs Missed</h2>
          <div className="flex items-center justify-center">
            <ResponsiveContainer width="100%" height={200}>
              <PieChart>
                <Pie
                  data={chartData}
                  cx="50%"
                  cy="50%"
                  innerRadius={60}
                  outerRadius={80}
                  paddingAngle={5}
                  dataKey="value"
                >
                  {chartData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} />
                  ))}
                </Pie>
              </PieChart>
            </ResponsiveContainer>
          </div>
          <div className="flex justify-center gap-6 mt-4">
            <div className="flex items-center gap-2">
              <div className="w-4 h-4 rounded-full bg-green-400" />
              <span className="text-sm text-gray-600">Taken (75%)</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-4 h-4 rounded-full bg-red-400" />
              <span className="text-sm text-gray-600">Missed (25%)</span>
            </div>
          </div>
        </div>

        {/* Streak Counter */}
        <div className="bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] rounded-3xl p-6 shadow-lg text-white">
          <div className="flex items-center justify-between">
            <div>
              <div className="flex items-center gap-2 mb-2">
                <Award className="w-6 h-6" />
                <h2 className="text-lg font-semibold">Current Streak</h2>
              </div>
              <p className="text-sm text-white/80">Keep up the great work!</p>
            </div>
            <div className="text-right">
              <div className="text-4xl font-bold">12</div>
              <div className="text-sm text-white/80">Days</div>
            </div>
          </div>
        </div>

        {/* Daily Log */}
        <div className="bg-white rounded-3xl p-6 shadow-lg">
          <h2 className="text-lg font-semibold text-gray-800 mb-4">Daily Log</h2>
          <div className="space-y-3">
            {dailyLog.map((log, index) => (
              <div
                key={index}
                className="bg-gray-50 rounded-2xl p-4 hover:bg-gray-100 transition-colors cursor-pointer"
              >
                <div className="flex items-start justify-between mb-2">
                  <div>
                    <p className="font-medium text-gray-800">{log.date}</p>
                    <p className="text-sm text-gray-500 mt-1">
                      {log.taken} of {log.total} medicines
                    </p>
                  </div>
                  <div>
                    {log.status === "complete" && (
                      <div className="px-3 py-1 rounded-full bg-green-100 text-green-700 text-xs font-medium">
                        ✓ Complete
                      </div>
                    )}
                    {log.status === "partial" && (
                      <div className="px-3 py-1 rounded-full bg-yellow-100 text-yellow-700 text-xs font-medium">
                        ⚠ Partial
                      </div>
                    )}
                    {log.status === "upcoming" && (
                      <div className="px-3 py-1 rounded-full bg-blue-100 text-blue-700 text-xs font-medium">
                        ⏰ Today
                      </div>
                    )}
                  </div>
                </div>
                <div className="flex flex-wrap gap-2">
                  {log.medicines.map((medicine, i) => (
                    <span
                      key={i}
                      className="text-xs px-2 py-1 rounded-full bg-white border border-gray-200 text-gray-600"
                    >
                      {medicine}
                    </span>
                  ))}
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Monthly Stats */}
        <div className="bg-white rounded-3xl p-6 shadow-lg">
          <h2 className="text-lg font-semibold text-gray-800 mb-4">This Month</h2>
          <div className="grid grid-cols-2 gap-4">
            <div className="bg-green-50 rounded-2xl p-4 text-center">
              <div className="text-2xl font-semibold text-green-600 mb-1">63</div>
              <p className="text-xs text-gray-600">Doses Taken</p>
            </div>
            <div className="bg-red-50 rounded-2xl p-4 text-center">
              <div className="text-2xl font-semibold text-red-600 mb-1">21</div>
              <p className="text-xs text-gray-600">Doses Missed</p>
            </div>
            <div className="bg-blue-50 rounded-2xl p-4 text-center">
              <div className="text-2xl font-semibold text-blue-600 mb-1">22</div>
              <p className="text-xs text-gray-600">Days Logged</p>
            </div>
            <div className="bg-purple-50 rounded-2xl p-4 text-center">
              <div className="text-2xl font-semibold text-purple-600 mb-1">4</div>
              <p className="text-xs text-gray-600">Active Meds</p>
            </div>
          </div>
        </div>
      </div>

      {/* Bottom Navigation */}
      <BottomNav />
    </div>
  );
}