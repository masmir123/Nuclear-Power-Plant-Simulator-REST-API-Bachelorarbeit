# REST API Spezifikation: Reaktor-Leitwarte

## Inhalt
1. [Allgemeines](#allgemeines)
2. [Endpunkte](#endpunkte)
   1. [System `system/`](#system-system)
   2. [Controller `control/`](#controller-control)
   3. [Simulation `simulation/`](#simulation-simulation)
3. [Response Codes](#response-codes)

## Allgemeines

Die Reaktor-Leitwarte REST API stellt Informationen sowie Steuerungselemente zur 
Reaktorsimulation via HTTP Requests zur Verfügung und ermöglicht vollständige
Bedienung der Reaktorsimulation durch externe Clients. Zusätzlich kann die Simulation über die API gesteuert werden.

### Voraussetzungen
- Die API antwortet auf Anfragen mit einer Nachricht in `JSON`-Format.
Fehlermeldungen, sowie sonstige Systeminformationen werden ebenfalls in `JSON` formatiert.
Anfragen müssen mit dem Header `Content-Type: application/json` gesendet werden.
- Die API kommuniziert nur mit einem Client, welcher validen Zugang zu den Endpunkten besitzt (Basic-Authentication).
- Zugriff auf die Endpunkte erfolgt über die URL `http://localhost:8080/api/` gefolgt von dem jeweiligen Endpunkt.

### Generische Fehlermeldung (4xx)

Wenn kein valider Zugang zum Endpunkt besteht, die Eingabe inkorrekt ist oder sonstige Fehler auftreten, wird folgende Fehlermeldung zurückgegeben:

```json
{
  "error": "Error Type",
  "message": "Specific Error Message"
}
```

## Endpunkte

Die API stellt zwei Endpunkte zur Verfügung, welche für die **Steuerung** und **Überwachung** der Reaktorsimulation verantwortlich sind.
Zusätzlich existiert ein weiterer Endpunkt, zur **Interaktion** mit verschiedenen Serverressourcen.

Eine Auflistung der Endpunkte ist in der folgenden Liste zu finden. Jeweilige Details zu den Endpunkten sind in den
entsprechenden Abschnitten einzusehen.

### System: system/

Regelt die Interaktion mit dem Server und liefert Informationen über den Server.
Unter `http://localhost:8080/api/system/` erreichbar.
Siehe [System Endpoint](ep-control.md) für weitere Informationen.


| Request-Type                                  | Endpunkt           | Verwendung                                                     |
|-----------------------------------------------|--------------------|----------------------------------------------------------------|
| <code style="color : greenyellow">GET</code>  | /status            | Liefert Informationen über Server                              |
| <code style="color : greenyellow">GET</code>  | /components        | Liefert eine Liste aller Komponenten IDs der Reaktorsimulation |
| <code style="color : yellow">POST</code>      | /restart           | Startet Reaktorsimulation neu                                  |
| <code style="color : lightskyblue">PUT</code> | /emergencyShutdown | Leite Emergency Shutdown Szenario ein                          |
| <code style="color : lightskyblue">PUT</code> | /initialState      | Setze Simulation auf initialen Zustand                         |
| <code style="color : lightskyblue">PUT</code> | /normalShutdown    | Leite 'Herunterfahren' Szenario ein                            |


### Controller: control/

Regelt die Steuerung der Reaktorsimulation.
Alle Endpunkte sind unter `http://localhost:8080/api/control/` erreichbar.
Siehe [Control Endpoint](ep-system.md) für weitere Informationen.

| Request-Type                                    | Endpunkt      | Verwendung                  |
|-------------------------------------------------|---------------|-----------------------------|
| <code style="color :  lightskyblue">PUT</code>  | /valve/{`id`} | Steuerung der Ventile       |
| <code style="color :  lightskyblue">PUT</code>  | /pump/{`id`}  | Steuerung der Pumpen        |
| <code style="color :  lightskyblue">PUT</code>  | /rods         | Steuerung der Kontrollstäbe |

### Simulation: simulation/

Regelt die Interaktion mit Simulationsvariablen.
Alle Endpunkte sind unter `http://localhost:8080/api/simulation/` erreichbar.
Siehe [Simulation Endpoint](ep-simulation.md) für weitere Informationen.

| Request-Type                                 | Endpunkt      | Verwendung                                       |
|----------------------------------------------|---------------|--------------------------------------------------|
| <code style="color : greenyellow">GET</code> | /valve/{`id`} | Pumpen Informationen                             |
| <code style="color : greenyellow">GET</code> | /pump/{`id`}  | Ventil Informationen                             |
| <code style="color : greenyellow">GET</code> | /generator    | Generator Informationen                          |
| <code style="color : greenyellow">GET</code> | /condenser    | Kondensator Informationen                        |
| <code style="color : greenyellow">GET</code> | /reactor      | Reaktor Informationen                            |
| <code style="color : greenyellow">GET</code> | /health       | Liefert Informationen zum Zustand der Komponente |

## Response Codes

Eine Übersicht der möglichen Response-Codes und deren Bedeutung im Kontext der API.

Alle Endpunkte unterstützen die folgenden Response-Codes:

| Code | Bedeutung                                                                                        |
|------|--------------------------------------------------------------------------------------------------|
| 200  | OK - Die Anfrage wurde erfolgreich ausgeführt                                                    |
| 400  | Bad Request - Die Anfrage ist fehlerhaft oder unvollständig. Details im Fehlercode               |
| 404  | Not Found - Der Endpunkt existiert nicht.                                                        |
| 500  | Internal Server Error - Ein interner Fehler ist aufgetreten.                                     |