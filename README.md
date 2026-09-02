# InstantOps — Live Vehicle Service Operations

InstantOps is a production-style operations dashboard for a vehicle service business. It provides database-backed booking analytics, booking search/filtering/pagination, mechanic and customer views, booking status updates, and live status propagation with Server-Sent Events.

## Tech Stack

- Frontend: React, TypeScript, Vite, Tailwind CSS, custom accessible UI components
- Charts: lightweight SVG-based charts
- Backend: Java 21, Spring Boot 3.3, Spring Web, Spring Data JPA, Bean Validation
- Database: PostgreSQL
- API documentation: Swagger/OpenAPI
- Real-time: Server-Sent Events (SSE)
- Deployment target: Vercel (frontend), AWS Free Tier (backend), GitHub (source)

## Architecture

React frontend
↓
REST API + SSE
↓
Spring Boot service layer
↓
Spring Data JPA repositories
↓
PostgreSQL

The frontend never hardcodes dashboard business data. KPI values, charts, bookings, mechanics and customers are retrieved from the backend.

## Local Setup

### Backend

Requirements: Java 21, Maven, PostgreSQL.

Create a PostgreSQL database named `instantops`, then configure these environment variables:

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASS`

Never commit database credentials to Git.



```powershell
$env:DB_PASS="your password"
```

Run:

```bash
cd backend
mvn spring-boot:run
```

Backend: `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui.html`

### Frontend

Requirements: Node.js 20+.

```bash
cd frontend
npm install
npm run dev
```

Frontend: `http://localhost:5173`

For local development, `/api` is proxied to `http://localhost:8080` by Vite. For production, set `VITE_API_URL` to the deployed backend API base URL.

## Main APIs

- `GET /api/dashboard`
- `GET /api/bookings` — server-side search/filter/sort/pagination
- `GET /api/bookings/{id}`
- `PATCH /api/bookings/{id}/status`
- `GET /api/mechanics`
- `GET /api/mechanics/{id}`
- `GET /api/customers`
- `GET /api/customers/{id}`
- `GET /api/services`
- `GET /api/events` — SSE live event stream

Bookings support server-side pagination, search, status filtering, mechanic filtering, service filtering and sorting.

## Live Updates

The frontend subscribes to `/api/events`. When a booking status is changed successfully, Spring publishes a domain event after the transaction commits and broadcasts it to connected SSE clients. Connected pages refresh their database-backed data without a full browser reload.

## Seed Data

The backend seeds realistic relational data when the database is empty:

- 750 bookings
- 100 customers
- 150 vehicles
- 25 mechanics
- 10 service offerings
- Six booking statuses


## Engineering Notes

- JPA entities are not exposed directly through REST APIs; DTOs are used.
- Booking list operations use server-side pagination and dynamic specifications.
- Status transitions are validated in the service layer.
- Global exception handling returns consistent API error responses.
- Database-backed aggregation is used for dashboard metrics and analytics.
- SSE uses connection cleanup and heartbeat handling.


