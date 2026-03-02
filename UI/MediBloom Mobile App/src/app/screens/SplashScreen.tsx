import { useEffect } from "react";
import { useNavigate } from "react-router";
import { motion } from "motion/react";

export default function SplashScreen() {
  const navigate = useNavigate();

  useEffect(() => {
    const timer = setTimeout(() => {
      navigate("/onboarding");
    }, 2500);
    return () => clearTimeout(timer);
  }, [navigate]);

  return (
    <div className="h-screen w-full bg-gradient-to-br from-[#F8BBD0] via-[#CE93D8] to-[#90CAF9] flex flex-col items-center justify-center font-['Poppins']">
      <motion.div
        initial={{ scale: 0, rotate: -180 }}
        animate={{ scale: 1, rotate: 0 }}
        transition={{ duration: 0.8, ease: "easeOut" }}
        className="mb-8"
      >
        <div className="relative w-32 h-32">
          <div className="absolute inset-0 bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] rounded-full shadow-2xl" />
          <div className="absolute inset-2 bg-white rounded-full flex items-center justify-center">
            <div className="w-20 h-10 rounded-full bg-gradient-to-r from-[#F8BBD0] via-white to-[#90CAF9] shadow-lg" />
          </div>
        </div>
      </motion.div>

      <motion.h1
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.5, duration: 0.6 }}
        className="text-5xl font-semibold text-white tracking-wide"
      >
        MediBloom
      </motion.h1>

      <motion.p
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.8, duration: 0.6 }}
        className="mt-3 text-white/80 text-sm"
      >
        Your gentle health companion
      </motion.p>
    </div>
  );
}
