const { app, BrowserWindow } = require('electron');
const path = require('path');
const express = require('express');

let mainWindow;
let server;

function createWindow() {
  // 1) Start a local Express server to serve your static files (search.html, results.json, etc.)
  const expressApp = express();
  // Serve all files from current folder
  expressApp.use(express.static(path.join(__dirname)));

  // Listen on some random port (e.g., 3000)
  server = expressApp.listen(3000, () => {
    console.log('Local server running at http://localhost:3000');
  });

  // 2) Create the Electron browser window
  mainWindow = new BrowserWindow({
    width: 1024,
    height: 768,
    title: 'Octopus Browser',
    // Optional: remove the default menu
    // autoHideMenuBar: true,
    // Optional: allow Node integration if needed
    webPreferences: {
      nodeIntegration: false,
      contextIsolation: true,
    },
  });

  // 3) Load your `search.html` from the local server
  mainWindow.loadURL('http://localhost:3000/search.html');

  // Emitted when the window is closed
  mainWindow.on('closed', function () {
    mainWindow = null;
    // Stop the local server when the window closes
    server.close();
  });
}

// This method will be called when Electron has finished initialization
app.whenReady().then(createWindow);

// Quit when all windows are closed
app.on('window-all-closed', function () {
  // On macOS, it's common for apps to stay open until the user quits explicitly
  if (process.platform !== 'darwin') {
    app.quit();
  }
});

app.on('activate', function () {
  // On macOS, re-create a window if the dock icon is clicked
  if (BrowserWindow.getAllWindows().length === 0) createWindow();
});
