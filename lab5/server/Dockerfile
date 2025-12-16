FROM python:3.14-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY main.py .
COPY entrypoint.sh .

RUN apt-get update && apt-get install -y curl \
	&& rm -rf /var/lib/apt/lists/*

RUN useradd -m appuser && chown -R appuser /app
USER appuser

EXPOSE 8000

CMD ["sh", "/app/entrypoint.sh"]
