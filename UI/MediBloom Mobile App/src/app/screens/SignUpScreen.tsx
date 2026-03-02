import { useState } from "react";
import { useNavigate } from "react-router";
import { Mail, Lock, User, Phone, Eye, EyeOff, ArrowLeft } from "lucide-react";

export default function SignUpScreen() {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [agreed, setAgreed] = useState(false);
  const [form, setForm] = useState({
    fullName: "",
    email: "",
    phone: "",
    password: "",
    confirmPassword: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSignUp = (e: React.FormEvent) => {
    e.preventDefault();
    navigate("/home");
  };

  return (
    <div className="min-h-screen w-full bg-gradient-to-br from-[#F8BBD0] via-[#CE93D8] to-[#90CAF9] flex items-center justify-center p-6 font-['Poppins']">
      <div className="w-full max-w-md">

        {/* Back Button */}
        <button
          onClick={() => navigate("/login")}
          className="flex items-center gap-2 text-white/90 mb-6 hover:text-white transition-colors"
        >
          <ArrowLeft className="w-5 h-5" />
          <span className="text-sm font-medium">Back to Login</span>
        </button>

        {/* Logo */}
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-white rounded-full shadow-xl mb-3">
            <div className="w-10 h-5 rounded-full bg-gradient-to-r from-[#F8BBD0] via-white to-[#90CAF9]" />
          </div>
          <h1 className="text-3xl font-semibold text-white">MediBloom</h1>
          <p className="text-white/80 mt-1 text-sm">Create your account</p>
        </div>

        {/* Sign Up Card */}
        <div className="bg-white rounded-3xl shadow-2xl p-8">
          <h2 className="text-2xl font-semibold text-gray-800 mb-1 text-center">
            Sign Up
          </h2>
          <p className="text-center text-gray-400 text-sm mb-6">
            Join MediBloom and stay on track 🌸
          </p>

          <form onSubmit={handleSignUp} className="space-y-4">

            {/* Full Name */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1.5">
                Full Name
              </label>
              <div className="relative">
                <User className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type="text"
                  name="fullName"
                  value={form.fullName}
                  onChange={handleChange}
                  placeholder="Your full name"
                  className="w-full pl-12 pr-4 py-3 rounded-2xl border-2 border-gray-200 focus:border-[#F8BBD0] focus:outline-none transition-colors bg-gray-50"
                  required
                />
              </div>
            </div>

            {/* Email */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1.5">
                Email
              </label>
              <div className="relative">
                <Mail className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type="email"
                  name="email"
                  value={form.email}
                  onChange={handleChange}
                  placeholder="your.email@example.com"
                  className="w-full pl-12 pr-4 py-3 rounded-2xl border-2 border-gray-200 focus:border-[#F8BBD0] focus:outline-none transition-colors bg-gray-50"
                  required
                />
              </div>
            </div>

            {/* Phone */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1.5">
                Phone Number
              </label>
              <div className="relative">
                <Phone className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type="tel"
                  name="phone"
                  value={form.phone}
                  onChange={handleChange}
                  placeholder="+1 000 000 0000"
                  className="w-full pl-12 pr-4 py-3 rounded-2xl border-2 border-gray-200 focus:border-[#F8BBD0] focus:outline-none transition-colors bg-gray-50"
                />
              </div>
            </div>

            {/* Password */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1.5">
                Password
              </label>
              <div className="relative">
                <Lock className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type={showPassword ? "text" : "password"}
                  name="password"
                  value={form.password}
                  onChange={handleChange}
                  placeholder="••••••••"
                  className="w-full pl-12 pr-12 py-3 rounded-2xl border-2 border-gray-200 focus:border-[#F8BBD0] focus:outline-none transition-colors bg-gray-50"
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors"
                >
                  {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
                </button>
              </div>
            </div>

            {/* Confirm Password */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1.5">
                Confirm Password
              </label>
              <div className="relative">
                <Lock className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type={showConfirm ? "text" : "password"}
                  name="confirmPassword"
                  value={form.confirmPassword}
                  onChange={handleChange}
                  placeholder="••••••••"
                  className="w-full pl-12 pr-12 py-3 rounded-2xl border-2 border-gray-200 focus:border-[#F8BBD0] focus:outline-none transition-colors bg-gray-50"
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowConfirm(!showConfirm)}
                  className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors"
                >
                  {showConfirm ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
                </button>
              </div>
              {form.confirmPassword && form.password !== form.confirmPassword && (
                <p className="text-xs text-red-400 mt-1 ml-1">Passwords do not match</p>
              )}
            </div>

            {/* Terms */}
            <div className="flex items-start gap-3 pt-1">
              <div
                onClick={() => setAgreed(!agreed)}
                className={`w-5 h-5 min-w-[20px] rounded-md border-2 cursor-pointer flex items-center justify-center transition-all mt-0.5 ${
                  agreed
                    ? "bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] border-transparent"
                    : "border-gray-300"
                }`}
              >
                {agreed && (
                  <svg className="w-3 h-3 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={3}>
                    <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
                  </svg>
                )}
              </div>
              <p className="text-sm text-gray-500 leading-snug">
                I agree to the{" "}
                <span className="text-[#F8BBD0] font-medium cursor-pointer hover:text-[#F48FB1]">
                  Terms of Service
                </span>{" "}
                and{" "}
                <span className="text-[#F8BBD0] font-medium cursor-pointer hover:text-[#F48FB1]">
                  Privacy Policy
                </span>
              </p>
            </div>

            {/* Sign Up Button */}
            <button
              type="submit"
              disabled={!agreed || (form.confirmPassword !== "" && form.password !== form.confirmPassword)}
              className="w-full py-4 rounded-full bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] text-white font-medium text-lg shadow-lg hover:shadow-xl transition-all active:scale-95 mt-2 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              Create Account
            </button>

            {/* Divider */}
            <div className="flex items-center gap-3 my-2">
              <div className="flex-1 h-px bg-gray-200" />
              <span className="text-gray-400 text-sm">or</span>
              <div className="flex-1 h-px bg-gray-200" />
            </div>

            {/* Google Sign Up */}
            <button
              type="button"
              className="w-full py-3 rounded-full border-2 border-gray-200 flex items-center justify-center gap-3 text-gray-600 font-medium hover:bg-gray-50 transition-all active:scale-95"
            >
              <svg className="w-5 h-5" viewBox="0 0 48 48">
                <path fill="#FFC107" d="M43.6 20H24v8h11.3C33.7 33.6 29.3 37 24 37c-7.2 0-13-5.8-13-13s5.8-13 13-13c3.1 0 5.9 1.1 8.1 2.9l5.7-5.7C34.5 5.1 29.5 3 24 3 12.4 3 3 12.4 3 24s9.4 21 21 21c11 0 20.4-8 20.4-21 0-1.3-.1-2.7-.4-4z" />
                <path fill="#FF3D00" d="M6.3 14.7l6.6 4.8C14.6 15.1 19 12 24 12c3.1 0 5.9 1.1 8.1 2.9l5.7-5.7C34.5 5.1 29.5 3 24 3 16.3 3 9.7 7.9 6.3 14.7z" />
                <path fill="#4CAF50" d="M24 45c5.3 0 10.1-1.9 13.8-5.1l-6.4-5.4C29.4 36.1 26.8 37 24 37c-5.3 0-9.8-3.4-11.4-8.1l-6.6 5.1C9.5 41 16.3 45 24 45z" />
                <path fill="#1976D2" d="M43.6 20H24v8h11.3c-.8 2.2-2.2 4-4.1 5.4l6.4 5.4C41.1 35.5 44 30.2 44 24c0-1.3-.1-2.7-.4-4z" />
              </svg>
              Continue with Google
            </button>
          </form>

          {/* Login Link */}
          <p className="text-center text-gray-600 mt-6 text-sm">
            Already have an account?{" "}
            <button
              onClick={() => navigate("/login")}
              className="text-[#F8BBD0] hover:text-[#F48FB1] font-semibold"
            >
              Login
            </button>
          </p>
        </div>
      </div>
    </div>
  );
}