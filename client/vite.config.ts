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
      "/users/logout": "http://localhost:8080",
      "/users/user": "http://localhost:8080",
      "/projects": "http://localhost:8080",
      "/projects/new": "http://localhost:8080",
      "/steps": "http://localhost:8080",
      "/swagger-ui/index.html": "http://localhost:8080",
    },
  },
});
