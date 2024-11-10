
# FASTAPI를 이용해 API 통신을 통해 질문을 입력받으면 모델에서 답변을 해 응답하는 구조


import model
import preprocessing
import util

from typing import Union
from fastapi import FastAPI
from fastapi import Body, Path, Query
from pydantic import BaseModel
import torch

from fastapi.middleware.cors import CORSMiddleware

app = FastAPI()

origins = [
    "http://localhost:3000",
    "http://localhost:8080"
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"]
)
class Item(BaseModel):
    name: str
    price: float
    is_offer: Union[bool, None] = None

@app.get("/")
def read_root():
    return {"hello" : "World!"}

@app.get("/items/{item_id}")
def read_item(item_id: int, q: Union[str, None] = None):
    return {"item_id" : item_id, "q" : q}

@app.put("/items/{item_id}")
def update_item(item_id: int, item: Item):
    return {"item_name" : item.name, "item_id": item_id}

@app.get("/ask")
async def answer(question: str, ver: str):
    print(question)
    print(ver)
    return {"answer" : "딸랑딸랑"}

if __name__ == "__main__":
    
    print("Hello! I'm nonblinker. Ask me anything!")