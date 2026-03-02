import { useState, useRef, useEffect } from "react";
import { Send, Sparkles } from "lucide-react";
import BottomNav from "../components/BottomNav";

interface Message {
  id: number;
  role: "user" | "ai";
  text: string;
  time: string;
}

const initialMessages: Message[] = [
  {
    id: 1,
    role: "ai",
    text: "Hi there! 🌸 I'm your MediBloom Assistant. I can help you understand your medicines, remind habits, and general health tips. How can I help you today?",
    time: "Now",
  },
];

const suggestionChips = [
  "What is my medicine used for?",
  "Common side effects",
  "How can I improve my reminder habit?",
];

const mockResponses: Record<string, string> = {
  "What is my medicine used for?":
    "Your medicines serve different purposes 💊\n\n• **Vitamin D3** – Supports bone health and immune function.\n• **Aspirin** – Used as a pain reliever and blood thinner.\n• **Metformin** – Helps control blood sugar levels.\n• **Omega-3** – Supports heart and brain health.\n\nAlways consult your doctor for personalized advice! 🌸",
  "Common side effects":
    "Here are some common side effects to be aware of 🩺\n\n• **Vitamin D3** – Nausea, weakness (rare with normal doses).\n• **Aspirin** – Stomach upset, increased bleeding risk.\n• **Metformin** – Nausea, diarrhea (especially early on).\n• **Omega-3** – Fishy aftertaste, mild stomach upset.\n\nIf you experience severe symptoms, contact your healthcare provider immediately! 💙",
  "How can I improve my reminder habit?":
    "Great question! Here are some tips to boost your adherence 🌟\n\n1. **Set consistent times** – Take medicines at the same time each day.\n2. **Link to routines** – Pair with meals or brushing teeth.\n3. **Use MediBloom reminders** – Enable notifications for each dose.\n4. **Keep medicines visible** – Store them where you'll see them.\n5. **Track your progress** – Check the History tab to celebrate streaks! 🎉\n\nYou're doing amazing! 🌸",
};

function getTime() {
  return new Date().toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });
}

function getMockResponse(input: string): string {
  const lower = input.toLowerCase();
  if (lower.includes("side effect")) {
    return mockResponses["Common side effects"];
  }
  if (lower.includes("reminder") || lower.includes("habit") || lower.includes("improve")) {
    return mockResponses["How can I improve my reminder habit?"];
  }
  if (lower.includes("medicine") || lower.includes("used for") || lower.includes("what")) {
    return mockResponses["What is my medicine used for?"];
  }
  return "Thank you for your question! 🌸 I'm here to provide general health information. For personalized medical advice, please consult your healthcare provider. Is there anything else I can help you with? 💙";
}

export default function AssistantScreen() {
  const [messages, setMessages] = useState<Message[]>(initialMessages);
  const [inputText, setInputText] = useState("");
  const [isTyping, setIsTyping] = useState(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages, isTyping]);

  const sendMessage = (text: string) => {
    if (!text.trim()) return;

    const userMsg: Message = {
      id: Date.now(),
      role: "user",
      text: text.trim(),
      time: getTime(),
    };

    setMessages((prev) => [...prev, userMsg]);
    setInputText("");
    setIsTyping(true);

    setTimeout(() => {
      const aiMsg: Message = {
        id: Date.now() + 1,
        role: "ai",
        text: getMockResponse(text),
        time: getTime(),
      };
      setMessages((prev) => [...prev, aiMsg]);
      setIsTyping(false);
    }, 1200);
  };

  const handleSend = () => sendMessage(inputText);
  const handleChip = (chip: string) => sendMessage(chip);

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") handleSend();
  };

  const renderText = (text: string) => {
    return text.split("\n").map((line, i) => {
      const parts = line.split(/\*\*(.*?)\*\*/g);
      return (
        <p key={i} className={i > 0 ? "mt-1" : ""}>
          {parts.map((part, j) =>
            j % 2 === 1 ? (
              <strong key={j} className="font-semibold">
                {part}
              </strong>
            ) : (
              part
            )
          )}
        </p>
      );
    });
  };

  return (
    <div className="min-h-screen w-full bg-gradient-to-br from-[#F8BBD0]/15 to-[#90CAF9]/15 font-['Poppins'] flex flex-col">
      {/* Header */}
      <div className="bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] pt-12 pb-6 px-6 rounded-b-3xl shadow-lg flex-shrink-0">
        <div className="flex items-center gap-3">
          <div className="w-11 h-11 rounded-full bg-white/25 flex items-center justify-center shadow-inner">
            <Sparkles className="w-6 h-6 text-white" />
          </div>
          <div>
            <h1 className="text-xl font-semibold text-white leading-tight">
              MediBloom Assistant 🌸
            </h1>
            <p className="text-white/80 text-xs mt-0.5">Always here to help</p>
          </div>
          {/* Online indicator */}
          <div className="ml-auto flex items-center gap-1.5">
            <div className="w-2 h-2 rounded-full bg-green-300 animate-pulse" />
            <span className="text-white/80 text-xs">Online</span>
          </div>
        </div>
      </div>

      {/* Messages */}
      <div className="flex-1 overflow-y-auto px-4 py-4 space-y-4 pb-48">
        {messages.map((msg) => (
          <div
            key={msg.id}
            className={`flex ${msg.role === "user" ? "justify-end" : "justify-start"} items-end gap-2`}
          >
            {msg.role === "ai" && (
              <div className="w-8 h-8 rounded-full bg-gradient-to-br from-[#F8BBD0] to-[#90CAF9] flex items-center justify-center flex-shrink-0 shadow-md mb-1">
                <span className="text-sm">🌸</span>
              </div>
            )}
            <div className={`max-w-[78%] ${msg.role === "user" ? "items-end" : "items-start"} flex flex-col`}>
              <div
                className={`px-4 py-3 rounded-3xl shadow-sm text-sm leading-relaxed ${
                  msg.role === "user"
                    ? "bg-[#90CAF9]/80 text-gray-800 rounded-br-md"
                    : "bg-[#F8BBD0]/60 text-gray-800 rounded-bl-md"
                }`}
              >
                {renderText(msg.text)}
              </div>
              <span className="text-[10px] text-gray-400 mt-1 px-1">{msg.time}</span>
            </div>
          </div>
        ))}

        {/* Typing indicator */}
        {isTyping && (
          <div className="flex items-end gap-2">
            <div className="w-8 h-8 rounded-full bg-gradient-to-br from-[#F8BBD0] to-[#90CAF9] flex items-center justify-center flex-shrink-0 shadow-md">
              <span className="text-sm">🌸</span>
            </div>
            <div className="bg-[#F8BBD0]/60 rounded-3xl rounded-bl-md px-5 py-4 shadow-sm">
              <div className="flex gap-1.5 items-center">
                <span className="w-2 h-2 rounded-full bg-pink-400 animate-bounce" style={{ animationDelay: "0ms" }} />
                <span className="w-2 h-2 rounded-full bg-pink-400 animate-bounce" style={{ animationDelay: "150ms" }} />
                <span className="w-2 h-2 rounded-full bg-pink-400 animate-bounce" style={{ animationDelay: "300ms" }} />
              </div>
            </div>
          </div>
        )}

        <div ref={messagesEndRef} />
      </div>

      {/* Fixed Bottom Area */}
      <div className="fixed bottom-[72px] left-0 right-0 bg-white/95 backdrop-blur-sm border-t border-gray-100 shadow-[0_-4px_20px_rgba(0,0,0,0.06)] px-4 pt-3 pb-3">
        {/* Suggestion Chips */}
        <div className="flex gap-2 overflow-x-auto pb-3 scrollbar-hide">
          {suggestionChips.map((chip) => (
            <button
              key={chip}
              onClick={() => handleChip(chip)}
              className="flex-shrink-0 px-3 py-2 rounded-full bg-gradient-to-r from-[#F8BBD0]/40 to-[#90CAF9]/40 border border-[#F8BBD0] text-gray-700 text-xs font-medium hover:from-[#F8BBD0]/70 hover:to-[#90CAF9]/70 transition-all active:scale-95 whitespace-nowrap"
            >
              {chip}
            </button>
          ))}
        </div>

        {/* Input Row */}
        <div className="flex items-center gap-3">
          <div className="flex-1 bg-gray-50 rounded-full border border-gray-200 flex items-center px-4 py-2.5 gap-2 focus-within:border-[#F8BBD0] transition-colors">
            <input
              ref={inputRef}
              type="text"
              value={inputText}
              onChange={(e) => setInputText(e.target.value)}
              onKeyDown={handleKeyDown}
              placeholder="Ask me anything…"
              className="flex-1 bg-transparent text-sm text-gray-800 placeholder-gray-400 outline-none"
            />
          </div>
          <button
            onClick={handleSend}
            disabled={!inputText.trim()}
            className={`w-11 h-11 rounded-full flex items-center justify-center shadow-md transition-all active:scale-90 ${
              inputText.trim()
                ? "bg-gradient-to-r from-[#F8BBD0] to-[#90CAF9] shadow-pink-200/60"
                : "bg-gray-200"
            }`}
          >
            <Send className={`w-4 h-4 ${inputText.trim() ? "text-white" : "text-gray-400"}`} />
          </button>
        </div>

        {/* Disclaimer */}
        <p className="text-center text-[10px] text-gray-400 mt-2 leading-relaxed">
          AI provides general health information only.
        </p>
      </div>

      <BottomNav />
    </div>
  );
}
