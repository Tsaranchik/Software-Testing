from flask import Flask, request, jsonify
import threading
import time
import logging
import uuid

app = Flask(__name__)

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(message)s')


def execute_timer(timer_id: str, duration: int):
	logging.info(f"[{timer_id}] Таймер запущен на {duration} секунд.")
	time.sleep(duration)
	logging.info(f"[{timer_id}] ТАЙМЕР ВЫПОЛНЕН!")


@app.route("/start_timer", methods=["POST"])
def start_timer():
	try:
		data = request.get_json(force=True)

		duration = data.get("duration", 5)
		timer_id = data.get("timer_id", "default_id")

		if not isinstance(duration, int) or duration <= 0 or duration > 86400:
			return jsonify({
				"status": "error",
				"message": "duration должен быть целым числом от 1 до 86400"
			}), 400

		if timer_id == "default_id":
			timer_id = str(uuid.uuid4())

		thread = threading.Thread(
			target=execute_timer,
			args=(timer_id, duration),
			daemon=True
		)
		thread.start()

		return jsonify({
			"status": "success",
			"timer_id": timer_id,
			"duration": duration,
			"message": f"Таймер #{timer_id} принят в обработку."
		})

	except Exception as e:
		return jsonify({
			"status": "error",
			"message": str(e)
		}), 500


@app.route("/health", methods=["GET"])
def health():
	return jsonify(status="ok"), 200


if __name__ == "__main__":
	app.run(host="0.0.0.0", port=8000)
