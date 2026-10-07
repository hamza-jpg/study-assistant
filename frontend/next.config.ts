import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  async rewrites() {
    const springUrl = process.env.SPRING_API_URL || "http://127.0.0.1:8080";
    const pythonUrl = process.env.PYTHON_API_URL || "http://127.0.0.1:8000";

    return [
      // Spring Boot Enterprise & Auth routes
      {
        source: "/api/auth/:path*",
        destination: `${springUrl}/api/auth/:path*`,
      },
      {
        source: "/api/courses/:path*",
        destination: `${springUrl}/api/courses/:path*`,
      },
      {
        source: "/api/chat/:path*",
        destination: `${springUrl}/api/chat/:path*`,
      },
      // Core RAG AI & UI routes (upload, ask/stream, kepler sample, clear)
      {
        source: "/api/:path*",
        destination: `${pythonUrl}/api/:path*`,
      },
    ];
  },
};

export default nextConfig;
