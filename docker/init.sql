-- init.sql
-- This script creates the database and tables for the MyStrive application.
-- It will be executed automatically when the MySQL Docker container starts.

-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS mystrive_db;

-- Use the database
USE mystrive_db;

-- Create a dedicated user for the application (recommended over using root directly)
-- Replace 'mystrive_user' and 'mystrive_password' with your desired credentials.
CREATE USER IF NOT EXISTS 'mystrive_user'@'%' IDENTIFIED WITH mysql_native_password BY 'mystrive_password';
GRANT ALL PRIVILEGES ON mystrive_db.* TO 'mystrive_user'@'%';
FLUSH PRIVILEGES;

-- Table: users
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: categories
CREATE TABLE IF NOT EXISTS categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    category_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Table: goals
CREATE TABLE IF NOT EXISTS goals (
    goal_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    category_id INT,
    goal_description TEXT NOT NULL,
    target_date DATE,
    status VARCHAR(50) NOT NULL DEFAULT 'Not Started',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE SET NULL
);

-- Table: milestones
CREATE TABLE IF NOT EXISTS milestones (
    milestone_id INT PRIMARY KEY AUTO_INCREMENT,
    goal_id INT NOT NULL,
    milestone_description TEXT NOT NULL,
    due_date DATE,
    status VARCHAR(50) NOT NULL DEFAULT 'Not Started',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (goal_id) REFERENCES goals(goal_id) ON DELETE CASCADE
);