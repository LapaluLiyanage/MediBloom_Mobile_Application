import { useState } from "react";
import { useNavigate } from "react-router";
import { Calendar, History, Smile, User, Plus, Check } from "lucide-react";
import { motion } from "motion/react";

const moods = [
  { id: "good", emoji: "🙂", label: "Good", color: "from-green-400 to-emerald-500" },
  { id: "okay", emoji: "😐", label: "Okay", color: "from-yellow-400 to-amber-500" },
  { id: "notwell", emoji: "😞", label: "Not Well", color: "from-red-400 to-rose-500" },
];

const recentMoods = [
  { date: "Feb 22, 2026", mood: "good", emoji: "🙂", note: "Feeling great today!" },
  { date: "Feb 21, 2026", mood: "okay", emoji: "😐", note: "Little tired but okay" },
  { date: "Feb 20, 2026", mood: "good", emoji: "🙂", note: "Good energy levels" },
  { date: "Feb 19, 2026", mood: "good", emoji: "🙂", note: "" },
  { date: "Feb 18, 2026", mood: "notwell", emoji: "😞", note: "Had a headache" },
];

export default function MoodTrackingScreen() {
  const navigate = useNavigate();
  const [selectedMood, setSelectedMood] = useState<string | null>(null);
  const [note, setNote] = useState("");
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = () => {
    if (selectedMood) {
      setSubmitted(true);
      setTimeout(() => {
        setSubmitted(false);
        setSelectedMood(null);
        setNote("");
      }, 2000);
    }
  };

  return (
    <div className="min-h-screen w-full bg-gradient-to-br from-[#F8BBD0]/20 to-[#90CAF9]/20 font-['Poppins'] pb-24">
      {/* Header */}
      <div className="bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] pt-12 pb-6 px-6 rounded-b-3xl shadow-lg">
        <h1 className="text-2xl font-semibold text-white text-center">Mood Tracking</h1>
      </div>

      <div className="px-6 py-8 space-y-6">
        {/* Today's Mood */}
        <div className="bg-white rounded-3xl p-8 shadow-lg">
          <h2 className="text-xl font-semibold text-gray-800 text-center mb-2">
            How are you feeling today?
          </h2>
          <p className="text-sm text-gray-500 text-center mb-8">
            Track your daily mood to understand your health better
          </p>

          {/* Mood Selection */}
          <div className="grid grid-cols-3 gap-4 mb-6">
            {moods.map((mood) => (
              <motion.button
                key={mood.id}
                onClick={() => setSelectedMood(mood.id)}
                whileTap={{ scale: 0.95 }}
                className={`flex flex-col items-center gap-3 p-6 rounded-3xl transition-all ${
                  selectedMood === mood.id
                    ? `bg-gradient-to-br ${mood.color} shadow-xl scale-105`
                    : "bg-gray-50 hover:bg-gray-100"
                }`}
              >
                <span className="text-5xl">{mood.emoji}</span>
                <span
                  className={`text-sm font-medium ${
                    selectedMood === mood.id ? "text-white" : "text-gray-700"
                  }`}
                >
                  {mood.label}
                </span>
              </motion.button>
            ))}
          </div>

          {/* Optional Note */}
          {selectedMood && (
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              className="space-y-4"
            >
              <textarea
                value={note}
                onChange={(e) => setNote(e.target.value)}
                placeholder="Add a note (optional)"
                className="w-full px-4 py-3 rounded-2xl border-2 border-gray-200 focus:border-[#F8BBD0] focus:outline-none transition-colors resize-none"
                rows={3}
              />

              <button
                onClick={handleSubmit}
                disabled={submitted}
                className={`w-full py-4 rounded-full font-medium text-lg shadow-lg transition-all ${
                  submitted
                    ? "bg-green-500 text-white"
                    : "bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] text-white hover:shadow-xl active:scale-95"
                }`}
              >
                {submitted ? (
                  <span className="flex items-center justify-center gap-2">
                    <Check className="w-5 h-5" />
                    Saved!
                  </span>
                ) : (
                  "Save Mood"
                )}
              </button>
            </motion.div>
          )}
        </div>

        {/* Mood History */}
        <div className="bg-white rounded-3xl p-6 shadow-lg">
          <h3 className="text-lg font-semibold text-gray-800 mb-4">Recent Moods</h3>
          <div className="space-y-3">
            {recentMoods.map((entry, index) => (
              <div
                key={index}
                className="flex items-start gap-4 p-4 bg-gray-50 rounded-2xl hover:bg-gray-100 transition-colors"
              >
                <span className="text-3xl flex-shrink-0">{entry.emoji}</span>
                <div className="flex-1 min-w-0">
                  <div className="flex items-center justify-between mb-1">
                    <p className="font-medium text-gray-800 capitalize">{entry.mood}</p>
                    <p className="text-xs text-gray-500">{entry.date}</p>
                  </div>
                  {entry.note && (
                    <p className="text-sm text-gray-600 truncate">{entry.note}</p>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Mood Insights */}
        <div className="bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] rounded-3xl p-6 shadow-lg text-white">
          <h3 className="text-lg font-semibold mb-3">This Week's Insights</h3>
          <div className="space-y-2">
            <div className="flex items-center justify-between">
              <span className="text-sm">Most Common:</span>
              <span className="text-2xl">🙂</span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm">Good Days:</span>
              <span className="font-semibold">5 out of 7</span>
            </div>
            <p className="text-sm text-white/90 mt-4 bg-white/10 rounded-2xl p-3">
              You've been feeling good most of this week! Keep up with your medicine routine. 🌸
            </p>
          </div>
        </div>
      </div>

      {/* Bottom Navigation */}
      <div className="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 rounded-t-3xl shadow-2xl">
        <div className="grid grid-cols-5 py-3 px-2">
          <button
            onClick={() => navigate("/home")}
            className="flex flex-col items-center gap-1 py-2 text-gray-400"
          >
            <Plus className="w-6 h-6" />
            <span className="text-xs">Home</span>
          </button>
          <button
            onClick={() => navigate("/calendar")}
            className="flex flex-col items-center gap-1 py-2 text-gray-400"
          >
            <Calendar className="w-6 h-6" />
            <span className="text-xs">Calendar</span>
          </button>
          <button
            onClick={() => navigate("/mood")}
            className="flex flex-col items-center gap-1 py-2 text-[#F8BBD0]"
          >
            <div className="w-10 h-10 rounded-full bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] flex items-center justify-center">
              <Smile className="w-5 h-5 text-white" />
            </div>
            <span className="text-xs font-medium">Mood</span>
          </button>
          <button
            onClick={() => navigate("/history")}
            className="flex flex-col items-center gap-1 py-2 text-gray-400"
          >
            <History className="w-6 h-6" />
            <span className="text-xs">History</span>
          </button>
          <button
            onClick={() => navigate("/profile")}
            className="flex flex-col items-center gap-1 py-2 text-gray-400"
          >
            <User className="w-6 h-6" />
            <span className="text-xs">Profile</span>
          </button>
        </div>
      </div>
    </div>
  );
}
