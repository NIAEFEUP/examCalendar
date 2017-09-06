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

Rename the file `example-config.json` inside `src/web/backend/` to `config.json` and change the credentials inside to match your MySQL configuration.


## Usage

Run `npm start` to start the backend server of the web interface. 
Set up a web server at the folder `src/web/frontend/`. After that, access your web server with your browser.
