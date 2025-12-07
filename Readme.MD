# 🚕 RideShare Backend

A mini **Ride Sharing Backend API** built with **Spring Boot**, **MongoDB**, and **JWT Authentication**.  
Users can request rides, and drivers can accept & complete them.

---

## ✅ Tech Stack
- Spring Boot
- Spring Security
- JWT
- MongoDB
- Spring Data Mongo
- BCrypt

---

## 🔐 Auth Flow
Register → Login → Get JWT → Send JWT in every request  
`Authorization: Bearer <token>`

---

## 📌 API Endpoints

### Auth
- POST `/api/auth/register`
- POST `/api/auth/login`

### User
- POST `/api/v1/rides` → Create Ride
- GET `/api/v1/user/rides` → My Rides

### Driver
- GET `/api/v1/driver/rides/requests` → Get all Pending Rides
- POST `/api/v1/driver/rides/{id}/accept` → Accept Ride

### Common
- POST `/api/v1/rides/{id}/complete` → Complete a Ride

---

## ✅ Roles
- ROLE_USER → Request & view rides
- ROLE_DRIVER → Accept & complete rides

---

## ⚙️ Run
1. Start MongoDB
2. Run Spring Boot app
3. Test with Postman / Curl

---
Built as a **learning + portfolio backend project**.