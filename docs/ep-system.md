# System


---
## Endpunkte

### **1. GET /status**
Liefert informationen über Server und laufende Simulation.

#### Request
- **HTTP Method**: <code style="color : greenyellow">GET</code>
- **Endpoint**: `/status`

#### Response
- **Status Codes**:
    - `200 OK`: Request was successful.
    - `500 Internal Server Error`: Problem with Reactor or Server.
- **Schema**:
    ```json
     {
        "isRunning": true,
        "runningSince": "2023-10-01T12:00:00Z"
     }    
    ```
#### Example
```shell
curl -X GET \
     -H "Authorization: Basic <user:password>" \
     -H "Content-Type: application/json" \
     http://localhost:8080/api/system/status
```


### **2. GET /components**
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


### **3. POST /restart**
Startet die Reaktorsimulation mit gegebenenfalls neuen Startparametern neu.

#### Request
- **HTTP Method**: <code style="color : <code style="color : yellow">POST</code>
- **Endpoint**: `/restart`

#### Response
- **Status Codes**:
  - `200 OK`: Request was successful.
  - `500 Internal Server Error`: Problem with Server.
- **Schema**:
    ```json
        {}      
    ```



### **4 PUT /emergencyShutdown**
Bereite Simulation auf Notabschaltübungsszenario vor. Dieses Szenario setzt die Pumpe WP1 außer Kraft.

#### Request
- **HTTP Method**: <code style="color :  lightskyblue">PUT</code>
- **Endpoint**: `/emergencyShutdown`

#### Response
- **Status Codes**:
  - `200 OK`: Request was successful.
  - `500 Internal Server Error`: Problem with Server.
- **Schema**:
    ```json
        {}      
    ```