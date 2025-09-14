# M2I Assurance

M2I Assurance is a full-stack web application for managing insurance clients, contracts, and categories. It provides a secure system for admins and clients to manage their data, view dashboards with basic statistics, and perform CRUD operations through a user-friendly interface.


## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Setup Instructions](#setup-instructions)

## Features

- Role-based access control (Admin, Client)
- CRUD operations for users, insurance contracts, and categories
- Dashboard with key statistics (e.g., number of clients, contracts by category)
- Dynamic server-rendered UI with Thymeleaf
- Data persistence using Spring Data MongoDB
- Clean architecture using the controller-service-repository pattern

## Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Security
- Spring Data MongoDB
- Thymeleaf
- TailwindCSS

### Tools & Libraries
- Maven
- MongoDB
- Spring Web
- Lombok

## Getting Started

### Prerequisites

- Java 21
- Maven
- MongoDB 

### Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/taoufiqallali/m2i-assurance.git
   cd m2i-assurance

2. Configure MongoDB connection in:
   ```bash
   spring.data.mongodb.uri=mongodb://localhost:27017/m2i_assurance

3. Run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run

4. Open the browser and navigate to:
   ```bash
   http://localhost:8080
