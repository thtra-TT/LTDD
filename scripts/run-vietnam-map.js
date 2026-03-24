const http = require("http");
const fs = require("fs");
const path = require("path");
const { exec } = require("child_process");

const projectRoot = path.resolve(__dirname, "..");
const host = "127.0.0.1";
const port = Number(process.env.PORT || 8080);
const entryFile = "vietnam-map-osm.html";

const mimeByExt = {
  ".html": "text/html; charset=utf-8",
  ".css": "text/css; charset=utf-8",
  ".js": "application/javascript; charset=utf-8",
  ".json": "application/json; charset=utf-8",
  ".png": "image/png",
  ".jpg": "image/jpeg",
  ".jpeg": "image/jpeg",
  ".svg": "image/svg+xml",
  ".ico": "image/x-icon"
};

function send(res, code, contentType, body) {
  res.writeHead(code, {
    "Content-Type": contentType,
    "Cache-Control": "no-store"
  });
  res.end(body);
}

function toSafePath(urlPath) {
  const decoded = decodeURIComponent(urlPath.split("?")[0]);
  const normalized = path.normalize(decoded).replace(/^([.][.][/\\])+/, "");
  return normalized === "/" ? `/${entryFile}` : normalized;
}

function openBrowser(url) {
  // Works on Windows, macOS and Linux.
  const cmd = process.platform === "win32"
    ? `start "" "${url}"`
    : process.platform === "darwin"
      ? `open "${url}"`
      : `xdg-open "${url}"`;

  exec(cmd, (err) => {
    if (err) {
      console.log(`[INFO] Could not auto-open browser. Open this URL manually: ${url}`);
    }
  });
}

const server = http.createServer((req, res) => {
  try {
    const safePath = toSafePath(req.url || "/");
    const absolutePath = path.join(projectRoot, safePath);

    if (!absolutePath.startsWith(projectRoot)) {
      send(res, 403, "text/plain; charset=utf-8", "Forbidden");
      return;
    }

    fs.stat(absolutePath, (statErr, stat) => {
      if (statErr || !stat.isFile()) {
        send(res, 404, "text/plain; charset=utf-8", "Not found");
        return;
      }

      const ext = path.extname(absolutePath).toLowerCase();
      const contentType = mimeByExt[ext] || "application/octet-stream";

      fs.readFile(absolutePath, (readErr, data) => {
        if (readErr) {
          send(res, 500, "text/plain; charset=utf-8", "Internal server error");
          return;
        }
        send(res, 200, contentType, data);
      });
    });
  } catch (error) {
    send(res, 500, "text/plain; charset=utf-8", "Internal server error");
  }
});

server.on("error", (error) => {
  if (error && error.code === "EADDRINUSE") {
    console.error(`[ERROR] Port ${port} is already in use. Try: set PORT=5500 && npm start`);
    return;
  }
  console.error("[ERROR] Could not start server:", error.message);
});

server.listen(port, host, () => {
  const appUrl = `http://${host}:${port}/${entryFile}`;
  console.log(`[OK] Vietnam map is running at ${appUrl}`);
  console.log("[INFO] Press Ctrl + C to stop.");
  openBrowser(appUrl);
});

