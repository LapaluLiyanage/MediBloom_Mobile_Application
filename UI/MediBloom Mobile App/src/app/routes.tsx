import { createBrowserRouter } from "react-router";
import SplashScreen from "./screens/SplashScreen";
import OnboardingScreens from "./screens/OnboardingScreens";
import LoginScreen from "./screens/LoginScreen";
import SignUpScreen from "./screens/SignUpScreen";
import ForgotPasswordScreen from "./screens/ForgotPasswordScreen";
import HomeScreen from "./screens/HomeScreen";
import AddMedicineScreen from "./screens/AddMedicineScreen";
import CalendarScreen from "./screens/CalendarScreen";
import HistoryScreen from "./screens/HistoryScreen";
import MoodTrackingScreen from "./screens/MoodTrackingScreen";
import ProfileScreen from "./screens/ProfileScreen";
import AssistantScreen from "./screens/AssistantScreen";

export const router = createBrowserRouter([
  {
    path: "/",
    Component: SplashScreen,
  },
  {
    path: "/onboarding",
    Component: OnboardingScreens,
  },
  {
    path: "/login",
    Component: LoginScreen,
  },
  {
    path: "/signup",
    Component: SignUpScreen,
  },
  {
    path: "/forgot-password",
    Component: ForgotPasswordScreen,
  },
  {
    path: "/home",
    Component: HomeScreen,
  },
  {
    path: "/add-medicine",
    Component: AddMedicineScreen,
  },
  {
    path: "/calendar",
    Component: CalendarScreen,
  },
  {
    path: "/history",
    Component: HistoryScreen,
  },
  {
    path: "/assistant",
    Component: AssistantScreen,
  },
  {
    path: "/mood",
    Component: MoodTrackingScreen,
  },
  {
    path: "/profile",
    Component: ProfileScreen,
  },
]);
