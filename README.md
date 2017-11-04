# Exam Calendar
This is an exam calendar scheduler for the University of Porto to make it easy to schedule multiple exams with constraints.

![Example calendar page](https://cloud.githubusercontent.com/assets/3010353/18812132/e0c64488-82c1-11e6-9cf7-b285cef7f9c3.png)

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

## Requirements

* MySQL
* NodeJS

## Setup

Clone this repo to your desktop and run `npm install` on folder `src/web/backend/` to install all the dependencies.

Create a MySQL local server with the setup present in the SQL files in /database.

Rename the file `example-config.json` inside `src/web/backend/` to `config.json` and change the credentials inside to match your MySQL configuration.


## Usage

To completely setup the project, several systems must be brought up:

- Scheduler backend (src/scheduler):
  - TODO
- WebApp backend (src/web/backend):
  - Run `npm start` at system root to start the backend server of the web interface.
  - This will use port 8081 to run the backend
- WebApp frontend (src/web/frontend):
  - Set up a web server (like [XAMPP](https://www.apachefriends.org/download.html)) at the frontend root dir. The frontend should then be accessible through localhost in a web browser.
  - Web app users must be registered in the 'users' table in the database.
