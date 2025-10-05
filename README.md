# Sport Betting Market Conversion

## 📄 Overview
This project implements a converter that takes sports betting markets in JSON format and outputs them
in the required to be standardized format.

---

## 🛠️ Technologies
- Java 17  
- Maven  
- Jackson (JSON serialization/deserialization)  
- JUnit 5  

---

## ⚙️ Installation & Run

Build the project:
```bash
./mvnw clean package
```

Run the converter:
```bash
java -jar target/converter-1.0-SNAPSHOT-shaded.jar <input name>.json <output name>.json [--pretty]
```

- `--pretty` (optional) – format output JSON with indentation.

---

## 📂 Project Structure
- `model/` – input and output DTOs (`InMarket`, `OutMarket`, `Specifiers`, etc.).  
- `service/` – main conversion logic (`MarketConversionService`).  
- `map/` – mappers for each market type (`OneXTwoMapper`, `TotalMapper`, `HandicapMapper`, `BttsMapper`).  
- `parse/` – specifier extractors for totals/handicaps.  
- `util/` – utilities (`UidBuilder`, `Validation`, `Regexes`, `TextUtils`).  
- `ConverterApp` – entry point.  

---

## ✅ Tests
Unit tests are provided (JUnit 5) for:
- Market conversion service.
- End-to-end conversion scenarios.

Run tests with:
```bash
./mvnw test
```

---

## 🖥️ Example

**Input**
```json
[
    {
      "name": "1x2",
      "event_id": "123456",
      "selections": [
        {
          "name": "Team A",
          "odds": 1.65
        },
        {
          "name": "draw",
          "odds": 3.2
        },
        {
          "name": "Team B",
          "odds": 2.6
        }
      ]
    }
]
```

**Output**
```json
[
  {
    "market_uid": "123456_1",
    "market_type_id": "1",
    "specifiers": {

    },
    "selections": [
      {
        "selection_uid": "123456_1_1",
        "selection_type_id": 1,
        "decimal_odds": 1.65
      },
      {
        "selection_uid": "123456_1_2",
        "selection_type_id": 2,
        "decimal_odds": 3.2
      },
      {
        "selection_uid": "123456_1_3",
        "selection_type_id": 3,
        "decimal_odds": 2.6
      }
    ]
  }
]
```

---

## 👤 Author
Gal Levi
