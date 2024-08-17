from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from server.socket import server, socketio
import uvicorn, dotenv, os

dotenv.load_dotenv()

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

socketApp = socketio.ASGIApp(
    socketio_server=server,
)

app.mount("/", socketApp)

@app.get("/")
async def get():
    return {"message": "Api is running"}

if __name__ == "__main__":
    uvicorn.run(app=app, host="0.0.0.0", port=int(os.getenv("PORT", 8000)), )