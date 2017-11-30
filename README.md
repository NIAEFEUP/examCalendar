# Exam Calendar
This is an exam calendar scheduler for the University of Porto to make it easy to schedule multiple exams with constraints.

![Example calendar page](https://cloud.githubusercontent.com/assets/3010353/18812132/e0c64488-82c1-11e6-9cf7-b285cef7f9c3.png)

## Current status
- Backend is currently crashing when a calendar is attempted to be created. Steps to reproduce:
  - Run 'Usage' steps
  - Login with FEUP credentials (without '@fe.up.pt')
  - Create new calendar
  - Import example files
  - Expect crash

## Features
- Login (SiFEUP credentials)
- Import the following data from an Excel file:
 - Curricular units and their students
 - Rooms
 - Professors of each topic
- Manual exam scheduling
- Automatic exam scheduling
- Custom constraints
- Tweakable optimization configurations

## Technologies

* MySQL
* NodeJS
* Octaplanner
* Java

## Setup

1. Run "git clone https://github.com/NIAEFEUP/examCalendar.git" to download the repository.
2. In a terminal, move to `src/web/backend/` and run `npm install` to install dependencies.
3. Create a MySQL local server with the setup present in the SQL files in '/database'.
4. Rename the file `example-config.json` inside `src/web/backend/` to `config.json` and change the credentials inside to match your MySQL configuration.
5. Add your FEUP email to the 'users' table (required for local testing).

## Usage

To completely setup the project, several systems must be brought up:

- Scheduler backend (src/scheduler):
  - Move to 'src/scheduler/' and run 'mvn package' to compile the scheduler and generate the '.jar' files.
  - Run 'mvn exec:java' to run the scheduler.
- WebApp backend (src/web/backend):
  - Run `npm start` at system root to start the backend server of the web interface.
  - This will use port 8081 to run the backend
- WebApp frontend (src/web/frontend):
  - Set up a web server (like [XAMPP](https://www.apachefriends.org/download.html)) at the frontend root dir. The frontend should then be accessible through localhost in a web browser.
  - Web app users must be registered in the 'users' table in the database.
