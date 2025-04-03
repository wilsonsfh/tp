---
layout: default.md
title: "Logging guide"
---

# Logging Guide

This guide explains how TeamScape uses logging to monitor runtime behavior and help with debugging.

---

## 📋 Overview

TeamScape uses the `java.util.logging` (JUL) package for logging, with a custom `LogsCenter` utility to manage loggers across the codebase.

Log messages are output to both:
- The **console**
- A **log file** (`logs/teamscape.log` by default)

The logging level (e.g., `INFO`, `WARNING`, `SEVERE`) can be configured via [`config.json`](Configuration.md#configuration-fields).

---

## 🧰 Usage

### ✅ How to get a logger

For any class, you can create a logger like this:

```java
private static final Logger logger = LogsCenter.getLogger(YourClassName.class);
