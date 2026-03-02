import { useState } from "react";
import { useNavigate } from "react-router";
import { motion, AnimatePresence } from "motion/react";
import { Heart, Pill, Sparkles } from "lucide-react";

const onboardingData = [
  {
    icon: Heart,
    title: "Your health deserves gentle care 💗",
    description: "Take control of your medication routine with ease and comfort",
    color: "from-[#F8BBD0] to-[#F48FB1]",
  },
  {
    icon: Pill,
    title: "Never miss a dose again 🌸",
    description: "Smart reminders that adapt to your schedule and lifestyle",
    color: "from-[#CE93D8] to-[#BA68C8]",
  },
  {
    icon: Sparkles,
    title: "Small steps. Big health. 🌼",
    description: "Track your progress and build healthy habits every day",
    color: "from-[#90CAF9] to-[#64B5F6]",
  },
];

export default function OnboardingScreens() {
  const [currentScreen, setCurrentScreen] = useState(0);
  const navigate = useNavigate();

  const handleNext = () => {
    if (currentScreen < onboardingData.length - 1) {
      setCurrentScreen(currentScreen + 1);
    } else {
      navigate("/login");
    }
  };

  const handleSkip = () => {
    navigate("/login");
  };

  const current = onboardingData[currentScreen];
  const Icon = current.icon;

  return (
    <div className="h-screen w-full bg-white flex flex-col font-['Poppins'] overflow-hidden">
      {/* Skip button */}
      <div className="absolute top-6 right-6 z-10">
        <button
          onClick={handleSkip}
          className="text-gray-400 text-sm font-medium hover:text-gray-600 transition-colors"
        >
          Skip
        </button>
      </div>

      <AnimatePresence mode="wait">
        <motion.div
          key={currentScreen}
          initial={{ opacity: 0, x: 100 }}
          animate={{ opacity: 1, x: 0 }}
          exit={{ opacity: 0, x: -100 }}
          transition={{ duration: 0.4 }}
          className="flex-1 flex flex-col items-center justify-center px-8"
        >
          {/* Gradient Icon Circle */}
          <div className={`w-48 h-48 rounded-full bg-gradient-to-br ${current.color} flex items-center justify-center shadow-xl mb-12`}>
            <Icon className="w-24 h-24 text-white" strokeWidth={1.5} />
          </div>

          {/* Title */}
          <h2 className="text-2xl font-semibold text-gray-800 text-center mb-4 px-4">
            {current.title}
          </h2>

          {/* Description */}
          <p className="text-gray-500 text-center max-w-sm leading-relaxed">
            {current.description}
          </p>
        </motion.div>
      </AnimatePresence>

      {/* Bottom section */}
      <div className="pb-12 px-8">
        {/* Dots indicator */}
        <div className="flex justify-center gap-2 mb-8">
          {onboardingData.map((_, index) => (
            <div
              key={index}
              className={`h-2 rounded-full transition-all duration-300 ${
                index === currentScreen
                  ? "w-8 bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9]"
                  : "w-2 bg-gray-300"
              }`}
            />
          ))}
        </div>

        {/* Next/Get Started button */}
        <button
          onClick={handleNext}
          className="w-full py-4 rounded-full bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] text-white font-medium text-lg shadow-lg hover:shadow-xl transition-all active:scale-95"
        >
          {currentScreen === onboardingData.length - 1 ? "Get Started" : "Next"}
        </button>
      </div>
    </div>
  );
}
