# Tapped Out - Sports Event Management Platform

![TappedOut Logo](TappedOut_Full-logo.png)

> Copyright © 2025 Tapped Out. All rights reserved

## 📕 Table of contents

- [Project Overview](#🔎-project-overview)

- [Database Structure](#⛁-database-structure)

    - [Diagrams](#diagrams)

    - [Key Tables](#key-tables)

- [Backend Implementation](#☕︎-backend-implementation)

    - [Dependencies](#dependencies)

    - [Configuration](#configuration)

- [Frontend Implementation](#⚛-frontend-implementation)

- [Getting Started](#getting-started)

- [License](#license)

## 🔎 Project Overview

Tapped Out is a comprehensive platform for organizing combat sports events. The system provides:

- Event creation and management.

- Competitor registration.

- Category organization by age, weight, gender...

- Result tracking and reporting.

- User management for organizers and competitiors.

The technical stack includes:

- **Backend**: Spring Boot (Java) v.3.4.5

- **Frontend**: React with Vite

- **Database**: MySQL

## ⛁ Database Structure

The database is designed to support complex event management with multiple sports, categories, user types, results and users, and the relations stablished between this models.

### Diagrams

The following are Entity-Relation Diagrams showing the relations between tables.

Diagram manually created at [UMLetino](https://www.umletino.com/umletino.html)

![ER Diagram](Diagrams/TappedOut_ER-Diagram-01.png)

Diagram automatically created at [DBDiagram](https://dbdiagram.io/home) prompting the database creation script.

![ER Diagram](Diagrams/TappedOut_ER-Diagram.png)

### Key Tables

- **Users**: Stores all the information with different types (organizers, competitors)

- **Sports**: Defines different ombat sports supported.

- **Categories**: Organizes competitors by age, weight, gender and skill level.

- **Events**: Central table for all tournament information.

- **Inscriptions**: Tracks competitor registrations at different events.

- **Results**: Records tournament outcomes.

## ☕︎ Backend Implementation

### Dependencies

### Configuration

## ⚛ Frontend Implementation

## Getting Started

## License