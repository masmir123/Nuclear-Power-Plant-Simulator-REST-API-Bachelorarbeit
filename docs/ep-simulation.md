# Simulation


---
## Endpunkte

### **1. GET /valve/{id}**
Liefert informationen über bestehende Ventile des Reaktors. Ventile werden über den Pfadparameter `id` identifiziert.

#### Request
- **HTTP Method**: <code style="color : greenyellow">GET</code>
- **Endpoint**: `/valve/{id}`
- **Path Parameter**:
    - `id`: ID des Ventils (String)
#### Response
- **Status Codes**:
    - `200 OK`: Request was successful.
    - `400 Bad Request`: Invalid request.
    - `500 Internal Server Error`: Problem with Reactor or Server.
- **Schema**:
    ```json
     {
        "name": "WV1",
        "blown": false,
        "status": true
     }    
    ```


### **2. GET /pump/{id}**
Liefert informationen über bestehende Pumpen des Reaktors. Pumpen werden über den Pfadparameter `id` identifiziert.

#### Request
- **HTTP Method**: <code style="color : greenyellow">GET</code>
- **Endpoint**: `/pump/{id}`
- **Path Parameter**:
    - `id`: ID der Pumpe (String)
  
#### Response
- **Status Codes**:
    - `200 OK`: Request was successful.
    - `400 Bad Request`: Invalid request.
    - `500 Internal Server Error`: Problem with Reactor or Server.
- **Schema**:
    ```json
     {
        "name": "WP1",
        "blown": false,
        "rpm": 1000,
        "setRpm": 1800,
        "maxRpm": 2000
     }    
    ```
- ℹ️ rpm, setRpm, maxRpm in U/min



### **3. GET /generator**
Liefert informationen über den Generator des Reaktors. Power wird in MW angegeben.

#### Request
- **HTTP Method**: <code style="color : greenyellow">GET</code>
- **Endpoint**: `/generator`

#### Response
- **Status Codes**:
    - `200 OK`: Request was successful.
    - `400 Bad Request`: Invalid request.
    - `500 Internal Server Error`: Problem with Reactor or Server.
- **Schema**:
    ```json
        {
          "name": "generator",
          "blown": false,
          "power": 700     
        }      
    ```
- ℹ️ power in MW



### **4. GET /condenser**
Liefert informationen über den Kondensator des Reaktors.

#### Request
- **HTTP Method**: <code style="color : greenyellow">GET</code>
- **Endpoint**: `/condenser`

#### Response
- **Status Codes**:
    - `200 OK`: Request was successful.
    - `400 Bad Request`: Invalid request.
    - `500 Internal Server Error`: Problem with Reactor or Server.
- **Schema**:
    ```json
        {
          "name": "condenser",
          "blown": false,
          "waterLevel": 0,
          "pressure":  100     
        }      
    ```
- ℹ️ waterLevel in mm, 
- ℹ️ pressure in bar

### **5. GET /reactor**
Liefert den aktuellen Zustand des Reaktors und die Position der Kontrollstäbe.

#### Request
- **HTTP Method**: <code style="color : greenyellow">GET</code>
- **Endpoint**: `/reactor`

#### Response
- **Status Codes**:
  - `200 OK`: Request was successful.
  - `500 Internal Server Error`: Problem with Reactor or Server.

- **Schema**:
    ```json
        {
          "pressure":  10,
          "waterLevel": 0,
          "operational": false,
          "intact": true,
          "rodPosition": 100
        }      
    ```

- ℹ️ pressure in bar,
- ℹ️ waterLevel in mm,
- ℹ️ rodPosition in %

### **6. GET /health**
Liefert Informationen zum Zustand der Komponenten des Reaktors.

#### Request
- **HTTP Method**: <code style="color : greenyellow">GET</code>
- **Endpoint**: `/health`

#### Response
- **Status Codes**:
  - `200 OK`: Request was successful.
  - `500 Internal Server Error`: Problem with Reactor or Server.
  
- **Schema**:
    ```json
        {
          [{
            "name": "Component ID",
            "broken": false 
          }]     
        }      
    ```