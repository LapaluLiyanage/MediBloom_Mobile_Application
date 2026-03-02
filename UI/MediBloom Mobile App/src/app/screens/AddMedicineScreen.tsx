import { useState } from "react";
import { useNavigate } from "react-router";
import { ArrowLeft, Plus, X } from "lucide-react";

export default function AddMedicineScreen() {
  const navigate = useNavigate();
  const [medicineName, setMedicineName] = useState("");
  const [dosage, setDosage] = useState("");
  const [stock, setStock] = useState("");
  const [reminderTimes, setReminderTimes] = useState<string[]>(["08:00"]);
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [repeat, setRepeat] = useState("Daily");

  const addReminderTime = () => {
    setReminderTimes([...reminderTimes, "12:00"]);
  };

  const removeReminderTime = (index: number) => {
    setReminderTimes(reminderTimes.filter((_, i) => i !== index));
  };

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    navigate("/home");
  };

  return (
    <div className="min-h-screen w-full bg-gradient-to-br from-[#F8BBD0]/20 to-[#90CAF9]/20 font-['Poppins'] pb-8">
      {/* Header */}
      <div className="bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] pt-12 pb-6 px-6 rounded-b-3xl shadow-lg">
        <div className="flex items-center gap-4 mb-2">
          <button
            onClick={() => navigate("/home")}
            className="w-10 h-10 rounded-full bg-white/20 flex items-center justify-center"
          >
            <ArrowLeft className="w-5 h-5 text-white" />
          </button>
          <h1 className="text-2xl font-semibold text-white">Add Medicine</h1>
        </div>
      </div>

      {/* Form */}
      <form onSubmit={handleSave} className="px-6 py-6 space-y-5">
        {/* Medicine Name */}
        <div className="bg-white rounded-3xl p-6 shadow-md">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Medicine Name
          </label>
          <input
            type="text"
            value={medicineName}
            onChange={(e) => setMedicineName(e.target.value)}
            placeholder="e.g., Aspirin"
            className="w-full px-4 py-3 rounded-2xl border-2 border-gray-200 focus:border-[#F8BBD0] focus:outline-none transition-colors"
            required
          />
        </div>

        {/* Dosage and Stock */}
        <div className="bg-white rounded-3xl p-6 shadow-md space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Dosage
            </label>
            <input
              type="text"
              value={dosage}
              onChange={(e) => setDosage(e.target.value)}
              placeholder="e.g., 500mg or 1 tablet"
              className="w-full px-4 py-3 rounded-2xl border-2 border-gray-200 focus:border-[#F8BBD0] focus:outline-none transition-colors"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Stock Amount
            </label>
            <input
              type="number"
              value={stock}
              onChange={(e) => setStock(e.target.value)}
              placeholder="Number of tablets/capsules"
              className="w-full px-4 py-3 rounded-2xl border-2 border-gray-200 focus:border-[#F8BBD0] focus:outline-none transition-colors"
              required
            />
          </div>
        </div>

        {/* Reminder Times */}
        <div className="bg-white rounded-3xl p-6 shadow-md">
          <div className="flex items-center justify-between mb-4">
            <label className="text-sm font-medium text-gray-700">
              Reminder Times
            </label>
            <button
              type="button"
              onClick={addReminderTime}
              className="flex items-center gap-1 text-sm text-[#F8BBD0] hover:text-[#F48FB1] font-medium"
            >
              <Plus className="w-4 h-4" />
              Add Time
            </button>
          </div>

          <div className="space-y-3">
            {reminderTimes.map((time, index) => (
              <div key={index} className="flex items-center gap-3">
                <input
                  type="time"
                  value={time}
                  onChange={(e) => {
                    const newTimes = [...reminderTimes];
                    newTimes[index] = e.target.value;
                    setReminderTimes(newTimes);
                  }}
                  className="flex-1 px-4 py-3 rounded-2xl border-2 border-gray-200 focus:border-[#F8BBD0] focus:outline-none transition-colors"
                />
                {reminderTimes.length > 1 && (
                  <button
                    type="button"
                    onClick={() => removeReminderTime(index)}
                    className="w-10 h-10 rounded-full bg-red-100 flex items-center justify-center text-red-500 hover:bg-red-200 transition-colors"
                  >
                    <X className="w-5 h-5" />
                  </button>
                )}
              </div>
            ))}
          </div>
        </div>

        {/* Duration */}
        <div className="bg-white rounded-3xl p-6 shadow-md space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Start Date
            </label>
            <input
              type="date"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
              className="w-full px-4 py-3 rounded-2xl border-2 border-gray-200 focus:border-[#F8BBD0] focus:outline-none transition-colors"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              End Date (Optional)
            </label>
            <input
              type="date"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
              className="w-full px-4 py-3 rounded-2xl border-2 border-gray-200 focus:border-[#F8BBD0] focus:outline-none transition-colors"
            />
          </div>
        </div>

        {/* Repeat Option */}
        <div className="bg-white rounded-3xl p-6 shadow-md">
          <label className="block text-sm font-medium text-gray-700 mb-3">
            Repeat
          </label>
          <div className="grid grid-cols-3 gap-3">
            {["Daily", "Weekly", "Custom"].map((option) => (
              <button
                key={option}
                type="button"
                onClick={() => setRepeat(option)}
                className={`py-3 rounded-2xl font-medium transition-all ${
                  repeat === option
                    ? "bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] text-white shadow-md"
                    : "bg-gray-100 text-gray-700 hover:bg-gray-200"
                }`}
              >
                {option}
              </button>
            ))}
          </div>
        </div>

        {/* Save Button */}
        <button
          type="submit"
          className="w-full py-4 rounded-full bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] text-white font-medium text-lg shadow-lg hover:shadow-xl transition-all active:scale-95"
        >
          Save Medicine
        </button>
      </form>
    </div>
  );
}
