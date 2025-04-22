# 🚀 Gowl - Play with Friends (Backend)

Spring Boot 3.4.4 backend for a social football prediction game. Compete in leagues by forecasting match results - no betting, just pure skill!

---

## 📌 Features
- **🏆 League Management** - Private/public leagues with custom rules
- **🔮 Match Predictions** - Forecast results before deadlines
- **🎯 Smart Scoring** - Risk-adjusted points (higher odds = more points)
- **📊 Live Leaderboards** - Redis-cached rankings
- **🔔 Real-time Alerts** - WebSocket notifications

---

## 🛠️ Tech Stack
| Category       | Technologies                          |
|----------------|---------------------------------------|
| **Core**       | Java 21, Spring Boot 3.4.4           |
| **Database**   | PostgreSQL 15                         |
| **Security**   | JWT, Spring Security, OAuth2-ready    |
| **Integrations**| OddsAPI, WebSocket, Redis             |
| **DevOps**     | Docker, Maven, GitHub Actions         |

---

## 🚀 Quick Start

### 1. Prerequisites
- Java 21 JDK
- PostgreSQL 15+
- Redis 7+ (optional for caching)



### 2. Setup
```bash
git clone https://github.com/Pmolinav/gowl.git
cd gowl
```

---
## 🌐 API Reference

### **🔐 Authentication**
| Endpoint                | Method | Request Body                          | Description                     |
|-------------------------|--------|---------------------------------------|---------------------------------|
| `/auth/register`        | POST   | `{ email, password, username }`      | Register new user               |
| `/auth/login`           | POST   | `{ email, password }`                | Authenticate user (returns JWT) |

### **🏆 League Management**
| Endpoint                     | Method | Parameters                           | Description                     |
|------------------------------|--------|--------------------------------------|---------------------------------|
| `/leagues`                   | POST   | `{ name, isPublic, accessCode }`    | Create new league               |
| `/leagues/{id}/join`         | POST   | `{ accessCode }`                    | Join existing league            |
| `/leagues/public`            | GET    | `?page=1&size=20`                   | Browse public leagues           |

### **🔮 Predictions**
| Endpoint                     | Method | Request Body                        | Description                     |
|------------------------------|--------|--------------------------------------|---------------------------------|
| `/matchdays/current`         | GET    | -                                   | Active matchday with matches    |
| `/predictions`               | POST   | `{ matchId, outcome }`              | Submit prediction               |
| `/predictions/history`       | GET    | `?leagueId={id}`                    | User's prediction history       |

---

## 📡 Response & Error Handling

### **✅ Successful Response**
```json
{
  "status": "success",
  "data": {
    "id": "league_123",
    "name": "Premier League Fans"
  },
  "metadata": {
    "timestamp": "2024-03-16T10:30:00Z",
    "version": "1.0"
  }
}
```

### **📌 Request Headers**
```http
Authorization: Bearer {your_jwt_token}
Content-Type: application/json
```

## ⚠️ Common Errors

### **HTTP Error Codes**
| Status Code | Error Code                   | Description                           | Recommended Action                  |
|-------------|------------------------------|---------------------------------------|-------------------------------------|
| `400`       | `MISSING_REQUIRED_FIELD`     | Required field not provided           | Check request body for missing data |
| `401`       | `INVALID_CREDENTIALS`        | Email/password incorrect              | Verify credentials and retry        |
| `403`       | `LEAGUE_JOIN_PERMISSION`     | User lacks league join permissions    | Request valid access code           |
| `404`       | `USER_NOT_FOUND`             | User ID doesn't exist                 | Check provided user ID              |
| `409`       | `DUPLICATE_LEAGUE_NAME`      | League name already taken             | Choose a unique league name         |
| `422`       | `INVALID_PREDICTION_RANGE`   | Prediction confidence out of bounds   | Use values between 1-100            |
| `429`       | `RATE_LIMIT_EXCEEDED`        | Too many requests (1000/day limit)    | Wait 24 hours or contact support    |
| `500`       | `EXTERNAL_API_FAILURE`       | OddsAPI service unavailable           | Retry in 5 minutes                  |

### **Business Logic Errors**
| Error Code                  | HTTP Status | Trigger Condition                    |
|-----------------------------|-------------|---------------------------------------|
| `PREDICTION_LOCKED`         | `423`       | Attempt to modify locked prediction   |
| `MATCHDAY_IN_PROGRESS`      | `409`       | Action conflicts with active matchday |
| `INSUFFICIENT_PRIVILEGES`   | `403`       | User lacks ADMIN/CREATOR role         |
| `LEAGUE_FULL`               | `410`       | League reached max participants       |

### **Sample Error Response**
```json
{
  "status": "error",
  "error": {
    "code": "LEAGUE_FULL",
    "message": "League 'Premier League Fans' has reached maximum capacity (50/50 players)",
    "details": {
      "leagueId": "league_123",
      "maxCapacity": 50
    }
  },
  "metadata": {
    "timestamp": "2024-03-16T14:25:30Z"
  }
}