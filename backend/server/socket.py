import socketio

server = socketio.AsyncServer(
    async_mode="asgi",
    cors_allowed_origins='*'
)


@server.event
async def connect(sid, environ):
    print(f"User connected: {sid}")

@server.event
async def disconnect(sid):
    print(f"User disconnected: {sid}")

@server.event
async def sendMessage(sid, data):
    try:
        sender_id = data.get("senderId")
        receiver_id = data.get("receiverId")
        message = data.get("message")
        print(f"Message from {sender_id} to {receiver_id}: {message}")
        await server.emit("message", {
            "senderId": sender_id,
            "receiverId": receiver_id,
            "message": message
        })
    except Exception as e:
        print(f"Error handling message: {e}")