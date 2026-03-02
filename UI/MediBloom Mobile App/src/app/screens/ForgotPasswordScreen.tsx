import { useState } from "react";
import { useNavigate } from "react-router";
import { Mail, ArrowLeft, KeyRound, Eye, EyeOff, CheckCircle2 } from "lucide-react";

type Step = "email" | "otp" | "reset" | "success";

export default function ForgotPasswordScreen() {
  const navigate = useNavigate();
  const [step, setStep] = useState<Step>("email");
  const [email, setEmail] = useState("");
  const [otp, setOtp] = useState(["", "", "", "", "", ""]);
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showNew, setShowNew] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [resendTimer, setResendTimer] = useState(0);

  const handleOtpChange = (index: number, value: string) => {
    if (!/^\d*$/.test(value)) return;
    const updated = [...otp];
    updated[index] = value.slice(-1);
    setOtp(updated);
    if (value && index < 5) {
      const next = document.getElementById(`otp-${index + 1}`);
      next?.focus();
    }
  };

  const handleOtpKeyDown = (index: number, e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Backspace" && !otp[index] && index > 0) {
      const prev = document.getElementById(`otp-${index - 1}`);
      prev?.focus();
    }
  };

  const startResendTimer = () => {
    setResendTimer(30);
    const interval = setInterval(() => {
      setResendTimer((prev) => {
        if (prev <= 1) { clearInterval(interval); return 0; }
        return prev - 1;
      });
    }, 1000);
  };

  const handleSendOtp = (e: React.FormEvent) => {
    e.preventDefault();
    startResendTimer();
    setStep("otp");
  };

  const handleVerifyOtp = (e: React.FormEvent) => {
    e.preventDefault();
    setStep("reset");
  };

  const handleResetPassword = (e: React.FormEvent) => {
    e.preventDefault();
    setStep("success");
  };

  const passwordsMatch = confirmPassword === "" || newPassword === confirmPassword;

  /* ── shared gradient background wrapper ── */
  const Wrapper = ({ children }: { children: React.ReactNode }) => (
    <div className="min-h-screen w-full bg-gradient-to-br from-[#F8BBD0] via-[#CE93D8] to-[#90CAF9] flex items-center justify-center p-6 font-['Poppins']">
      <div className="w-full max-w-md">{children}</div>
    </div>
  );

  /* ─────────────── STEP 1 — Email ─────────────── */
  if (step === "email") return (
    <Wrapper>
      <button
        onClick={() => navigate("/login")}
        className="flex items-center gap-2 text-white/90 mb-6 hover:text-white transition-colors"
      >
        <ArrowLeft className="w-5 h-5" />
        <span className="text-sm font-medium">Back to Login</span>
      </button>

      {/* icon */}
      <div className="text-center mb-8">
        <div className="inline-flex items-center justify-center w-20 h-20 bg-white rounded-full shadow-xl mb-4">
          <Mail className="w-9 h-9 text-[#F8BBD0]" />
        </div>
        <h1 className="text-2xl font-semibold text-white">Forgot Password?</h1>
        <p className="text-white/80 mt-2 text-sm leading-relaxed">
          No worries! Enter your email and we'll<br />send you a reset code.
        </p>
      </div>

      <div className="bg-white rounded-3xl shadow-2xl p-8">
        <form onSubmit={handleSendOtp} className="space-y-5">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Email Address</label>
            <div className="relative">
              <Mail className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="your.email@example.com"
                className="w-full pl-12 pr-4 py-3 rounded-2xl border-2 border-gray-200 focus:border-[#F8BBD0] focus:outline-none transition-colors bg-gray-50"
                required
              />
            </div>
          </div>

          <button
            type="submit"
            className="w-full py-4 rounded-full bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] text-white font-medium text-lg shadow-lg hover:shadow-xl transition-all active:scale-95"
          >
            Send Reset Code
          </button>
        </form>

        <p className="text-center text-gray-600 mt-6 text-sm">
          Remembered it?{" "}
          <button
            onClick={() => navigate("/login")}
            className="text-[#F8BBD0] hover:text-[#F48FB1] font-semibold"
          >
            Login
          </button>
        </p>
      </div>
    </Wrapper>
  );

  /* ─────────────── STEP 2 — OTP ─────────────── */
  if (step === "otp") return (
    <Wrapper>
      <button
        onClick={() => setStep("email")}
        className="flex items-center gap-2 text-white/90 mb-6 hover:text-white transition-colors"
      >
        <ArrowLeft className="w-5 h-5" />
        <span className="text-sm font-medium">Back</span>
      </button>

      <div className="text-center mb-8">
        <div className="inline-flex items-center justify-center w-20 h-20 bg-white rounded-full shadow-xl mb-4">
          <KeyRound className="w-9 h-9 text-[#90CAF9]" />
        </div>
        <h1 className="text-2xl font-semibold text-white">Check Your Email</h1>
        <p className="text-white/80 mt-2 text-sm leading-relaxed">
          We sent a 6-digit code to<br />
          <span className="font-semibold text-white">{email}</span>
        </p>
      </div>

      <div className="bg-white rounded-3xl shadow-2xl p-8">
        <form onSubmit={handleVerifyOtp} className="space-y-6">
          {/* OTP boxes */}
          <div className="flex gap-2 justify-center">
            {otp.map((digit, i) => (
              <input
                key={i}
                id={`otp-${i}`}
                type="text"
                inputMode="numeric"
                maxLength={1}
                value={digit}
                onChange={(e) => handleOtpChange(i, e.target.value)}
                onKeyDown={(e) => handleOtpKeyDown(i, e)}
                className={`w-11 h-13 text-center text-xl font-semibold rounded-2xl border-2 focus:outline-none transition-all
                  ${digit ? "border-[#F8BBD0] bg-[#FFF0F5] text-[#D81B60]" : "border-gray-200 bg-gray-50 text-gray-800"}
                  focus:border-[#90CAF9]`}
                style={{ height: "52px" }}
              />
            ))}
          </div>

          {/* Resend */}
          <div className="text-center text-sm text-gray-500">
            Didn't receive the code?{" "}
            {resendTimer > 0 ? (
              <span className="text-gray-400">Resend in {resendTimer}s</span>
            ) : (
              <button
                type="button"
                onClick={startResendTimer}
                className="text-[#F8BBD0] hover:text-[#F48FB1] font-semibold"
              >
                Resend
              </button>
            )}
          </div>

          <button
            type="submit"
            disabled={otp.some((d) => d === "")}
            className="w-full py-4 rounded-full bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] text-white font-medium text-lg shadow-lg hover:shadow-xl transition-all active:scale-95 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Verify Code
          </button>
        </form>
      </div>
    </Wrapper>
  );

  /* ─────────────── STEP 3 — New Password ─────────────── */
  if (step === "reset") return (
    <Wrapper>
      <button
        onClick={() => setStep("otp")}
        className="flex items-center gap-2 text-white/90 mb-6 hover:text-white transition-colors"
      >
        <ArrowLeft className="w-5 h-5" />
        <span className="text-sm font-medium">Back</span>
      </button>

      <div className="text-center mb-8">
        <div className="inline-flex items-center justify-center w-20 h-20 bg-white rounded-full shadow-xl mb-4">
          <KeyRound className="w-9 h-9 text-[#CE93D8]" />
        </div>
        <h1 className="text-2xl font-semibold text-white">Create New Password</h1>
        <p className="text-white/80 mt-2 text-sm">Make it strong and memorable 🔒</p>
      </div>

      <div className="bg-white rounded-3xl shadow-2xl p-8">
        <form onSubmit={handleResetPassword} className="space-y-5">
          {/* New Password */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">New Password</label>
            <div className="relative">
              <KeyRound className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
              <input
                type={showNew ? "text" : "password"}
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                placeholder="••••••••"
                className="w-full pl-12 pr-12 py-3 rounded-2xl border-2 border-gray-200 focus:border-[#F8BBD0] focus:outline-none transition-colors bg-gray-50"
                required
                minLength={8}
              />
              <button
                type="button"
                onClick={() => setShowNew(!showNew)}
                className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors"
              >
                {showNew ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
              </button>
            </div>

            {/* Strength bar */}
            {newPassword.length > 0 && (
              <div className="mt-2 space-y-1">
                <div className="flex gap-1">
                  {[1, 2, 3, 4].map((lvl) => {
                    const strength =
                      newPassword.length >= 12 && /[A-Z]/.test(newPassword) && /[0-9]/.test(newPassword) && /[^a-zA-Z0-9]/.test(newPassword) ? 4
                      : newPassword.length >= 10 && /[A-Z]/.test(newPassword) && /[0-9]/.test(newPassword) ? 3
                      : newPassword.length >= 8 ? 2
                      : 1;
                    const color =
                      strength === 1 ? "bg-red-300"
                      : strength === 2 ? "bg-yellow-300"
                      : strength === 3 ? "bg-green-300"
                      : "bg-green-500";
                    return (
                      <div
                        key={lvl}
                        className={`h-1.5 flex-1 rounded-full transition-all ${lvl <= strength ? color : "bg-gray-200"}`}
                      />
                    );
                  })}
                </div>
                <p className="text-xs text-gray-400">
                  {newPassword.length < 8 ? "Too short — min. 8 characters"
                    : newPassword.length < 10 ? "Fair — add uppercase & numbers"
                    : newPassword.length < 12 ? "Good — add symbols for stronger password"
                    : "Strong password 💪"}
                </p>
              </div>
            )}
          </div>

          {/* Confirm Password */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Confirm Password</label>
            <div className="relative">
              <KeyRound className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
              <input
                type={showConfirm ? "text" : "password"}
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                placeholder="••••••••"
                className={`w-full pl-12 pr-12 py-3 rounded-2xl border-2 focus:outline-none transition-colors bg-gray-50
                  ${!passwordsMatch ? "border-red-300 focus:border-red-400" : "border-gray-200 focus:border-[#F8BBD0]"}`}
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
            {!passwordsMatch && (
              <p className="text-xs text-red-400 mt-1 ml-1">Passwords do not match</p>
            )}
          </div>

          <button
            type="submit"
            disabled={!newPassword || !confirmPassword || !passwordsMatch}
            className="w-full py-4 rounded-full bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] text-white font-medium text-lg shadow-lg hover:shadow-xl transition-all active:scale-95 disabled:opacity-50 disabled:cursor-not-allowed mt-2"
          >
            Reset Password
          </button>
        </form>
      </div>
    </Wrapper>
  );

  /* ─────────────── STEP 4 — Success ─────────────── */
  return (
    <Wrapper>
      <div className="text-center">
        {/* Success card */}
        <div className="bg-white rounded-3xl shadow-2xl p-10 flex flex-col items-center gap-5">
          {/* animated check circle */}
          <div className="w-24 h-24 rounded-full bg-gradient-to-br from-[#F8BBD0] to-[#90CAF9] flex items-center justify-center shadow-xl">
            <CheckCircle2 className="w-12 h-12 text-white" strokeWidth={2} />
          </div>

          <div>
            <h2 className="text-2xl font-semibold text-gray-800">Password Reset!</h2>
            <p className="text-gray-500 mt-2 text-sm leading-relaxed">
              Your password has been successfully<br />reset. You're all set to login 🌸
            </p>
          </div>

          {/* decorative dots */}
          <div className="flex gap-2 mt-1">
            {["bg-[#F8BBD0]", "bg-[#CE93D8]", "bg-[#90CAF9]"].map((c, i) => (
              <div key={i} className={`w-2.5 h-2.5 rounded-full ${c}`} />
            ))}
          </div>

          <button
            onClick={() => navigate("/login")}
            className="w-full py-4 rounded-full bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] text-white font-medium text-lg shadow-lg hover:shadow-xl transition-all active:scale-95"
          >
            Back to Login
          </button>
        </div>
      </div>
    </Wrapper>
  );
}
