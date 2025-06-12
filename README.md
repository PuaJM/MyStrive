```
# MyStrive - A Java Web Application with Docker Compose

## Overview

MyStrive is a Java web application designed for **[Describe the main purpose of your application here, e.g., "streamlining personal goal tracking and task management," "a simple student information system," "a basic e-commerce platform"].** It leverages Apache Tomcat as the servlet container and MySQL as the database, all orchestrated using Docker Compose for easy setup and consistent deployment across different environments.

This repository contains the full NetBeans project, including the compiled `.war` file, along with the necessary Dockerfiles and a Docker Compose configuration to get the application and its database running quickly.

## Features

* **[List a few key features of your application, e.g.:]**
    * User authentication and profile management
    * Creation, tracking, and completion of personal goals
    * Milestone management for each goal
    * Categorization of goals for better organization
    * Data persistence via a MySQL database
    * Web-based interface accessible via any modern browser

## Technologies Used

* **Backend Language:** Java (JDK 11)
* **Web Server/Servlet Container:** Apache Tomcat 9
* **Database:** MySQL 8
* **Build Tool:** NetBeans IDE (generates `.war` files)
* **Containerization:** Docker, Docker Compose

## Getting Started

Follow these instructions to set up and run the MyStrive application locally using Docker Compose.

### Prerequisites

* **Docker Desktop:** Ensure Docker Desktop is installed and running on your Windows machine. It provides the Docker Engine and Docker Compose. Download from [https://www.docker.com/products/docker-desktop/](https://www.docker.com/products/docker-desktop/).
* **Git:** (If you plan to clone this repository via the command line).

### Setup Instructions

1.  **Clone the Repository (if you haven't already):**
    If you haven't cloned the repository, do so using Git:
    ```bash
    git clone [https://github.com/PuaJM/MyStrive.git](https://github.com/PuaJM/MyStrive.git)
    cd MyStrive
    ```
    If you downloaded a zip file, extract it and navigate into the `MyStrive` folder.

2.  **Verify WAR File Presence:**
    The `MyStrive.war` file, which is the compiled Java web application, should be located in the `dist/` directory within this project's root.
    Expected Path: `MyStrive/dist/MyStrive.war`
    If this file is missing or you've made changes to the Java code and need to rebuild it, open the `MyStrive` project in NetBeans and perform a "Clean and Build" operation.

3.  **Navigate to the Docker Configuration Directory:**
    All Docker-related files (Dockerfiles, Docker Compose) are located in the `docker/` subdirectory.
    ```bash
    cd docker
    ```

4.  **Clean Up Previous Docker Runs (Recommended for First Time / Fresh Start):**
    This command stops and removes any previously running containers, associated networks, volumes, and *all* images built by this project. This ensures a completely clean environment for your new deployment.
    ```bash
    docker-compose down -v --rmi all
    ```

5.  **Build Docker Images:**
    This command builds the Docker images for both your `db` (MySQL) and `web` (Tomcat with MyStrive app) services. The `--no-cache` flag ensures that the images are built from scratch, reflecting any recent changes.
    ```bash
    docker-compose build --no-cache
    ```
    *You should see output indicating successful building of both `db` and `web` images, including the `COPY` step for `MyStrive.war`.*

6.  **Start the Application and Database Services:**
    This command brings up both the web application and the database services. Logs from both containers will be streamed directly to your terminal.
    ```bash
    docker-compose up
    ```
    * **Monitor the logs carefully.** Wait for both the `db` (MySQL) and `web` (Tomcat) services to indicate that they are ready. Look for messages such as:
        * `db-1 | ... MySQL Community Server - GPL. (ready for connections)`
        * `web-1 | ... Deploying web application archive [/usr/local/tomcat/webapps/ROOT.war]`
        * `web-1 | ... Deployment of web application archive [...] has finished in [X] ms`
        * `web-1 | ... Server startup in [Y] milliseconds`
    * *Note:* The initial database setup might take a few moments.

### Accessing the Application

Once both services are running and their logs indicate successful startup and deployment, open your web browser and navigate to:

[http://localhost:8080/](http://localhost:8080/)

You should now see your MyStrive Java web application.

### Stopping the Application

To stop the running Docker containers without removing the images or database volume (for quick restarts later):

1.  In the terminal where `docker-compose up` is running, press `Ctrl+C`.
2.  Then, run:
    ```bash
    docker-compose down
    ```
    If you want to remove everything (containers, networks, images, and volumes), use `docker-compose down -v --rmi all`.

## Project Structure

```
MyStrive/
├── dist/
│   └── MyStrive.war         # Compiled Java web application archive
├── docker/
│   ├── Dockerfile.web       # Dockerfile for the Tomcat/Java web app image
│   ├── Dockerfile.db        # Dockerfile for the MySQL database image
│   ├── docker-compose.yml   # Docker Compose configuration for services
│   └── init.sql             # SQL script to initialize the MySQL database schema and data
├── src/                     # Your NetBeans Java source code (e.g., .java files)
├── web/                     # Web content for NetBeans (e.g., .jsp, HTML, CSS, JS files)
├── nbproject/               # NetBeans IDE project configuration files
├── README.md                # This README file
└── .gitignore               # Git ignore file for version control
```

## Database Configuration

The Java application is configured to connect to the MySQL database service (`db`) using the following parameters within the Docker network:

* **Host:** `db`
* **Internal Port:** `3306`
* **External Host Port (optional for local access):** `3307` (mapped to 3306 in container)
* **Database Name:** `mystrive_db`
* **Application Username:** `mystrive_user`
* **Application Password:** `mystrive_password`
* **MySQL Root Password (for DB management):** `@StrongPassword1234` (as set in `docker-compose.yml` and `Dockerfile.db`)

Ensure your Java application's database connection code (e.g., in `DBConnection.java`) aligns with these settings.


