# To-Do List Manager

A simple desktop application built with Java Swing that allows users to manage their daily tasks with persistent storage using H2 database.

## Project Summary

This is a beginner-friendly GUI-based To-Do List application that demonstrates core Java programming concepts including Object-Oriented Programming, GUI development with Swing, and basic database operations. Users can add, view, remove, and mark tasks as completed, with all data being persistently stored in an H2 database file.

## Directory Structure

```
ToDoListApp/
├── src/
│   ├── model/
│   │   └── Task.java                 # Data model class for tasks
│   ├── app/
│   │   └── ToDoListApp.java          # Main application class with GUI and database logic
├── lib/
│   └── h2-2.2.224.jar              # SQLite JDBC driver
├── db/
│   └── todo.db                      # SQLite database file (created automatically)
├── README.md
├── LICENSE
└── .gitignore
```

## Demo

*Demo content will be added here*

## How to Run the Project

### Prerequisites
1. **Java Development Kit (JDK)**: Ensure you have JDK 8 or higher installed
2. **IDE**: Any Java IDE (Eclipse, IntelliJ IDEA, VS Code, etc.)

### Setup Instructions

1. **Clone or Download the Project**
   - Download the project files to your local machine
   - Ensure the `h2-2.2.224.jar` file is present in the `lib/` directory

### Running the Application

1. **Method 1: Using IDE**
   - Open `ToDoListApp.java` in your IDE
   - Right-click on the file and select "Run" or click the Run button
   - The application window will open

2. **Method 2: Command Line**
   ```bash
   # Navigate to the project directory
   cd ToDoListApp
   
   # Compile the Java files (include SQLite JAR in classpath)
   javac -cp "lib/h2-2.2.224.jar" -d . src/model/Task.java src/app/ToDoListApp.java
   
   # Run the application
   java -cp ".:lib/h2-2.2.224.jar" app.ToDoListApp
   ```

3. **Method 3: Using JAR File**
   ```bash
   # Compile and create executable JAR
   javac -cp "lib/h2-2.2.224.jar" -d . src/model/Task.java src/app/ToDoListApp.java
   jar cfe ToDoListApp.jar app.ToDoListApp -C . app -C . model -C lib h2-2.2.224.jar
   
   # Run the JAR
   java -jar ToDoListApp.jar
   ```

### Using the Application

1. **Adding Tasks**: Type a task description in the text field and click "Add Task"
2. **Viewing Tasks**: All tasks appear in the list below, showing completion status
3. **Removing Tasks**: Select a task from the list and click "Remove Task"
4. **Marking Complete**: Select a task and click "Mark as Completed"
5. **Persistence**: All tasks are automatically saved to the SQLite database and will persist between application sessions


