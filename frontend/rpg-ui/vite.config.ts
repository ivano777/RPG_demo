import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  base: '/',
  plugins: [react()],
    server: {
    cors: {
      // the origin you will be accessing via browser
      origin: 'http://localhost:8080',
    },
  },
    build: {
    // generate .vite/manifest.json in outDir
    manifest: true
  },
    define: {
    'import.meta.env.VITE_API_URL': JSON.stringify('http://localhost:8080'),
  },
})