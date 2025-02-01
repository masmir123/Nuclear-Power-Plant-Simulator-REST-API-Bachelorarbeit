# Control


---
## Endpunkte

### **1. PUT /valve/{id}**
Steuere Ventile des Reaktors durch ein oder ausschalten. Ventile werden über den Pfadparameter `id` identifiziert.

#### Request
- **HTTP Method**:  <code style="color : lightskyblue">PUT</code>
- **Endpoint**: `/valve/{id}`
- **Path Parameter**:
    - `id`: ID des Ventils (String)
- **Query Parameter**:

    | Parameter  | Typ       | Erforderlich | Beschreibung              |  
    |------------|-----------|--------------|---------------------------|
    | `activate` | `boolean` | Ja           | Setze Zustand des Ventils |

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
Steuere Pumpen des Reaktors durch Ändern der Rotationsgeschwindigkeit. Pumpen werden über den Pfadparameter `id` identifiziert.

#### Request
- **HTTP Method**: <code style="color :  lightskyblue">PUT</code>
- **Endpoint**: `/pump/{id}`
- **Path Parameter**:
    - `id`: ID der Pumpe (String)
- **Query Parameter**:

  | Parameter | Typ   | Erforderlich | Beschreibung        |  
  |-----------|-------|--------------|---------------------|
  | `setRpm`  | `int` | Ja           | Setze RPM der Pumpe |

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


### **3. PUT /rods**
Steuere die Konzentration der Kontrollstäbe.

#### Request
- **HTTP Method**:  <code style="color :  lightskyblue">PUT</code>
- **Endpoint**: `/rods`
- **Query Parameter**:

  | Parameter | Typ   | Erforderlich | Beschreibung                                                            |  
  |-----------|-------|--------------|-------------------------------------------------------------------------|
  | `setRod`  | `int` | Ja           | Prozentuale Zufuhr der Kontrollstäbe einstellen. Wertebereich `[0,100]` |
- 
#### Response
- **Status Codes**:
    - `200 OK`: Request was successful.
    - `400 Bad Request`: Invalid request.
    - `500 Internal Server Error`: Problem with Server.
- **Schema**:
    ```json
        {
          "pressure":  10,
          "waterLevel": 0,
          "operational": false,
          "intact": true,
          "rodPosition": 100,
          "overheated": false,
          "restheat": 0
        }      
    ```