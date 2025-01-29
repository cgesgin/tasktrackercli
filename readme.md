# 🎯 Overview
This project is created to contribute to the Roadmap.sh challenge. It is a simple console application that helps you manage and track tasks, implemented using Java.
For more details about the challenge and how to contribute, check the link: Roadmap.sh Task Tracker Challenge.

     https://roadmap.sh/projects/task-tracker

## 🔍 Features

- ✅ Add, update and delete tasks
- ✅ Mark tasks by status (TODO, IN_PROGRESS, DONE)
- ✅ List all tasks
- ✅ Filter tasks by status
- ✅ Track creation and update dates
- ✅ Persistent storage in JSON file

## 💾 Data Storage

All tasks are stored in a `tasks.json` file in JSON format. Each task contains:
- ID
- Description
- Status
- Creation date
- Last update date

## 🛠️ Technical Details

The project uses:
- Java 19
- Maven for build management(3.9.9 )
- Jackson for JSON processing
- Command-line interface for interaction


# 🏃How to Run

* Clone the repository:
  
        git clone https://github.com/cgesgin/tasktrackercli.git

* Navigate to the project directory
  
        cd tasktrackercli

* build the project:
  
        mvn clean install

* Run the application:
  
        mvn exec:java

# 📘 Usage Example

After running the application, enter the “help” command to display a list of available commands and their descriptions.