"""This code is for testing only"""

import json
import time
from datetime import date
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path

HOST = "127.0.0.1"
PORT = 8069
DELAY_SECONDS = 0.5
ROWS = [
    {"id": 1, "code": "QWHZK", "date": "2026-01-02"},
    {"id": 2, "code": "LMBTA", "date": "2026-01-04"},
    {"id": 3, "code": "XRJPE", "date": "2026-01-07"},
    {"id": 4, "code": "DNVYC", "date": "2026-01-09"},
    {"id": 5, "code": "GUKSO", "date": "2026-01-12"},
    {"id": 6, "code": "PFIWB", "date": "2026-01-14"},
    {"id": 7, "code": "TZEHM", "date": "2026-01-17"},
    {"id": 8, "code": "AOYRL", "date": "2026-01-19"},
    {"id": 9, "code": "VCXND", "date": "2026-01-22"},
    {"id": 10, "code": "KJQGU", "date": "2026-01-24"},
    {"id": 11, "code": "BHSFT", "date": "2026-01-27"},
    {"id": 12, "code": "NWLAI", "date": "2026-01-29"},
    {"id": 13, "code": "EYPKZ", "date": "2026-02-01"},
    {"id": 14, "code": "RMDOX", "date": "2026-02-03"},
    {"id": 15, "code": "ICVJQ", "date": "2026-02-06"},
    {"id": 16, "code": "OTGBW", "date": "2026-02-08"},
    {"id": 17, "code": "ZAUEN", "date": "2026-02-11"},
    {"id": 18, "code": "HKRYS", "date": "2026-02-13"},
    {"id": 19, "code": "SXFLP", "date": "2026-02-16"},
    {"id": 20, "code": "MBNQH", "date": "2026-02-18"},
    {"id": 21, "code": "CLWTV", "date": "2026-02-21"},
    {"id": 22, "code": "YGOIF", "date": "2026-02-23"},
    {"id": 23, "code": "UEZDR", "date": "2026-02-26"},
    {"id": 24, "code": "JPAMK", "date": "2026-02-28"},
    {"id": 25, "code": "WIHXC", "date": "2026-03-03"},
    {"id": 26, "code": "FQTUY", "date": "2026-03-06"},
    {"id": 27, "code": "TDKVA", "date": "2026-03-09"},
    {"id": 28, "code": "GSJNE", "date": "2026-03-12"},
    {"id": 29, "code": "LRCPO", "date": "2026-03-16"},
    {"id": 30, "code": "VNBZG", "date": "2026-03-20"},
]
IDS = [row["id"] for row in ROWS]
FILE_PATH = Path(__file__).resolve().parent / "Shrek.png"


class Handler(BaseHTTPRequestHandler):
    def do_GET(self):
        time.sleep(DELAY_SECONDS)
        if self.path.startswith("/items/"):
            self.handle_download(self.path[len("/items/") :])
        else:
            self.send_json(404, {"error": "Not found"})

    def do_POST(self):
        time.sleep(DELAY_SECONDS)
        if self.path == "/items":
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
        self.send_json(
            200,
            [
                row
                for row in ROWS
                if date_from <= date.fromisoformat(row["date"]) <= date_to
            ],
        )

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
