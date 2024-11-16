# Control


---
## Endpunkte

### **1. PUT /valve/{id}**
Steuere Ventile des Reaktors durch ein oder ausschalten. Ventile werden über den Pfadparameter `id` identifiziert.

#### Request
- **HTTP Method**:  <code style="color : lightskyblue">PUT</code>
- **Endpoint**: `/valve/{id}`
- **Path Parameter**:
    - `id`: ID des Ventils
- **Query Parameter**:

    | Parameter  | Type      | Required | Description            |  
    |------------|-----------|----------|------------------------|
    | `activate` | `boolean` | Yes      | Set state of the valve |

#### Response
- **Status Codes**:
    - `200 OK`: Request was successful.
    - `400 Bad Request`: Invalid request.
    - `500 Internal Server Error`: Problem with Reactor or Server.
- **Schema**:
    ```json
     {
        "name": "WV1",
        "status": true
     }    
    ```

### **2. PUT /pump/{id}**
Steuere Pumpen des Reaktors durch ein oder ausschalten. Pumpen werden über den Pfadparameter `id` identifiziert.

#### Request
- **HTTP Method**: <code style="color :  lightskyblue">PUT</code>
- **Endpoint**: `/pump/{id}`
- **Path Parameter**:
    - `id`: ID der Pumpe
- **Query Parameter**:

  | Parameter | Type  | Required | Description                    |  
  |-----------|-------|----------|--------------------------------|
  | `setRpm`  | `int` | Yes      | Set rotation speed of the pump |

#### Response
- **Status Codes**:
    - `200 OK`: Request was successful.
    - `400 Bad Request`: Invalid request.
    - `500 Internal Server Error`: Problem with Reactor or Server.
- **Schema**:
    ```json
     {
        "name": "WP1",
        "setRpm": 1800
     }    
    ```


### **3. POST /rods**
Steuere die Konzentration der Kontrollstäbe.

#### Request
- **HTTP Method**:  <code style="color :  lightskyblue">PUT</code>
- **Endpoint**: `/rods`
- **Query Parameter**:

  | Parameter | Type  | Required | Description                                      |  
  |-----------|-------|----------|--------------------------------------------------|
  | `setRod`  | `int` | Yes      | Set percentage of rod exposure. Values `[0,100]` |
- 
#### Response
- **Status Codes**:
    - `200 OK`: Request was successful.
    - `400 Bad Request`: Invalid request.
    - `500 Internal Server Error`: Problem with Server.
- **Schema**:
    ```json
        {}      
    ```