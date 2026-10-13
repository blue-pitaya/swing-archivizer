"""This code is for testing only"""

import json
from datetime import date
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path

HOST = "127.0.0.1"
PORT = 8069
IDS = [x for x in range(1, 100)]
FILE_PATH = Path(__file__).resolve().parent / "Shrek.png"


class Handler(BaseHTTPRequestHandler):
    def do_POST(self):
        if self.path == "/items/ids":
            self.handle_ids()
        elif self.path.startswith("/items/"):
            self.handle_download(self.path[len("/items/") :])
        else:
            self.send_json(404, {"error": "Not found"})

    def handle_ids(self):
        try:
            body = json.loads(
                self.rfile.read(int(self.headers.get("Content-Length", 0)))
            )
            date_from = date.fromisoformat(body["from"])
            date_to = date.fromisoformat(body["to"])
        except (ValueError, KeyError, TypeError):
            self.send_json(
                422,
                {"error": 'Expected JSON {"from": "YYYY-MM-DD", "to": "YYYY-MM-DD"}'},
            )
            return
        if date_from > date_to:
            self.send_json(422, {"error": '"from" is after "to"'})
            return
        self.send_json(200, IDS)

    def handle_download(self, item_id):
        if not item_id.isdigit() or int(item_id) not in IDS:
            self.send_json(404, {"error": f"Unknown item {item_id}"})
            return
        if not FILE_PATH.is_file():
            self.send_json(500, {"error": f"Missing file {FILE_PATH.name}"})
            return
        data = FILE_PATH.read_bytes()
        self.send_response(200)
        self.send_header("Content-Type", "application/octet-stream")
        self.send_header(
            "Content-Disposition", f'attachment; filename="{FILE_PATH.name}"'
        )
        self.send_header("Content-Length", str(len(data)))
        self.end_headers()
        self.wfile.write(data)

    def send_json(self, status, payload):
        data = json.dumps(payload).encode()
        self.send_response(status)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(data)))
        self.end_headers()
        self.wfile.write(data)


def main():
    server = ThreadingHTTPServer((HOST, PORT), Handler)
    print(f"Server is listening on http://{HOST}:{PORT}.")
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        pass
    finally:
        server.server_close()


if __name__ == "__main__":
    main()
