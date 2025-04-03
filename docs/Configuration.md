---
layout: default.md
title: "Configuration guide"
---

# Configuration Guide

This guide explains how to configure TeamScape's runtime behavior through the `config.json` file.  
The file is created automatically in the root directory on first launch and contains settings like GUI window size, log file location, and the path to the data file.

---

## 📂 Location of the Config File

The `config.json` file is created in the same folder where the app is launched.

- For end users: The file will appear in the same folder as the `.jar` file.
- For developers: When running with Gradle, it will be generated in the root project directory.

---

## ⚙️ Configuration Fields

The following fields can be modified in `config.json`:

| Field                 | Type    | Description                                                                 |
|----------------------|---------|-----------------------------------------------------------------------------|
| `appTitle`            | String  | The title of the application window                                         |
| `logLevel`            | String  | Logging verbosity (`INFO`, `WARNING`, `SEVERE`, etc.)                       |
| `userPrefsFilePath`   | String  | Path to the user preferences file (GUI layout, window size, etc.)          |
| `addressBookFilePath` | String  | Path to the main data file (e.g., `data/addressbook.json`)                 |

---

## 📝 Editing the Configuration

You can manually edit the `config.json` file **before launching the app**.  
Make sure you use proper JSON syntax.

**Example:**

```json
{
  "appTitle": "TeamScape",
  "logLevel": "INFO",
  "userPrefsFilePath": "preferences/userprefs.json",
  "addressBookFilePath": "data/addressbook.json"
}
