import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  // base: "/DeleGate/",
  server: {
    proxy: {
      "/oauth2/authorization/github": "http://localhost:8080",
      "/oauth/authorization/google": "http://localhost:8080",
      "/logout": "http://localhost:8080",
    },
  },
});
