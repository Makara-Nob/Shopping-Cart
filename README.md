# Shopping Cart Project

## Project Description

This backend application manages core e-commerce functionalities such as shopping cart operations, product management, and order processing. It also integrates with PostgreSQL and implements secure authentication using JWT and Spring Security.

## Features
- **Product Management**: CRUD operations for products.
- **Cart Operations**: Add, update, or remove items from the cart.
- **JWT Authentication**: Secure login and user session management.
- **Order Processing**: Handle user orders and checkout process.

## Technologies Used
- Java (Spring Boot)
- PostgreSQL
- JWT for authentication
- Docker (for containerization)

## Setup Instructions
1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/shopping-cart-backend.git
    cd shopping-cart-backend
    ```

2. Set up environment variables in `.env` file:
    ```dotenv
    POSTGRES_DB_NAME=shopping_cart
    POSTGRES_USER=your_user
    POSTGRES_PASS=your_password
    JWT_SECRET=your_jwt_secret_key
    ```

3. Build and run the application:
    ```bash
    mvn spring-boot:run
    ```

4. Access the application at `http://localhost:5000`.

## API Documentation
- http://localhost:5000/api/v1/swagger-ui/index.html

