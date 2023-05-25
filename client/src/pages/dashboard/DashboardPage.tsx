import { Route, Routes } from "react-router-dom";
import { Home } from "./Home";
import { Data } from "./Data";

export function DashboardPage() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/data" element={<Data />} />
    </Routes>
  );
}
