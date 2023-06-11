import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  base: "/DeleGate",
  server: {
    proxy: {
      "/oauth2/authorization/github": "https://delegate.herokuapp.com",
      "/oauth/authorization/google": "https://delegate.herokuapp.com",
      "/auth/github": "https://delegate.herokuapp.com",
      "/users/logout": "https://delegate.herokuapp.com",
      "/users/user": "https://delegate.herokuapp.com",
      "/projects": "https://delegate.herokuapp.com",
      "/projects/new": "https://delegate.herokuapp.com",
      "/steps": "https://delegate.herokuapp.com",
      "/swagger-ui/index.html": "https://delegate.herokuapp.com",
    },
  },
});
