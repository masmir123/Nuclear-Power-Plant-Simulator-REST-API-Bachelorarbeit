# Simulation


---
## Endpunkte

### **1. GET /valve/{id}**
Liefert informationen über bestehende Ventile des Reaktors. Ventile werden über den Pfadparameter `id` identifiziert.

#### Request
- **HTTP Method**: <code style="color : greenyellow">GET</code>
- **Endpoint**: `/valve/{id}`
- **Path Parameter**:
    - `id`: ID des Ventils
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
    - `id`: ID der Pumpe
  
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
          "pressure":  10,     
        }      
    ```


### **5. GET /reactor**
Liefert die IDs der jeweiligen komponenten des Reaktors, welche über die API angesprochen werden können.

#### Request
- **HTTP Method**: <code style="color : greenyellow">GET</code>
- **Endpoint**: `/components`

#### Response
- **Status Codes**:
    - `200 OK`: Request was successful.
    - `500 Internal Server Error`: Problem with Reactor or Server.
- **Schema**:
    ```json
        {
          "components": [{
            "name": "WV1",
            "blown": false,
            "interactable": true 
          }]     
        }      
    ```


### **6. GET /health**
Liefert die IDs der jeweiligen komponenten des Reaktors, welche über die API angesprochen werden können.

#### Request
- **HTTP Method**: <code style="color : greenyellow">GET</code>
- **Endpoint**: `/components`

#### Response
- **Status Codes**:
    - `200 OK`: Request was successful.
    - `500 Internal Server Error`: Problem with Reactor or Server.
- **Schema**:
    ```json
        {
          "components": [{
            "name": "WV1",
            "blown": false,
            "interactable": true 
          }]     
        }      
    ```