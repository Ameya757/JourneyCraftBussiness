// // vite.config.js
// import { defineConfig } from 'vite'
// import react from '@vitejs/plugin-react'
// import tailwindPostcss from '@tailwindcss/postcss'
// import autoprefixer from 'autoprefixer'

// export default defineConfig({
//   plugins: [
//     // Your React plugin
//     react()
//   ],
//   css: {
//     postcss: {
//       plugins: [
//         // The new Tailwind PostCSS plugin
//         tailwindPostcss({
//           // If you want to point to a specific Tailwind config file:
//           config: './tailwind.config.js',
//         }),
//         // Autoprefixer
//         autoprefixer()
//       ],
//     },
//   },
//   server: {
//     proxy: {
//       '/auth': {
//         target: 'http://localhost:8082', // no extra /auth/signup here
//         changeOrigin: true,
//         // optional rewrite if needed:
//         // rewrite: (path) => path.replace(/^\/auth/, '/auth')
//       },
//     },
//   },
// })
import { defineConfig } from 'vite';
import tailwindcss from '@tailwindcss/vite';
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [
    tailwindcss(),react()
  ],
});