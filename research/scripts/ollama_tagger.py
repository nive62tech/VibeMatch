import pandas as pd
import requests
import json

OLLAMA_URL = "http://localhost:11434/api/generate"
MODEL = "llama3.2:3b"   # swap if you're using a different local model

def get_tags(description: str) -> dict:
    prompt = f"""
Given this venue description, respond ONLY with valid JSON, no other text:
{{
  "vibe_tags": ["tag1", "tag2"],
  "crowd_level": "low" | "medium" | "high"
}}

Description: "{description}"
"""
    response = requests.post(OLLAMA_URL, json={
        "model": MODEL,
        "prompt": prompt,
        "stream": False
    })

    data = response.json()

    if "response" not in data:
        print("Ollama returned unexpected payload:")
        print(data)
        return {"vibe_tags": [], "crowd_level": "medium"}

    raw_text = data["response"]
    try:
        return json.loads(raw_text)
    except json.JSONDecodeError:
        print(f"Failed to parse: {raw_text}")
        return {"vibe_tags": [], "crowd_level": "medium"}

def main():
    df = pd.read_csv("research/data/raw_venues.csv")
    results = []
    for _, row in df.iterrows():
        tags = get_tags(row["description"])
        results.append({
            "id": row["name"].lower().replace(" ", "_"),
            "name": row["name"],
            "category": row["category"],
            "description": row["description"],
            "price_level": row["price_level"],
            "vibe_tags": tags["vibe_tags"],
            "crowd_level": tags["crowd_level"]
        })
    with open("research/data/seed_venues.json", "w") as f:
        json.dump(results, f, indent=2)
    print(f"Tagged {len(results)} venues → research/data/seed_venues.json")

if __name__ == "__main__":
    main()