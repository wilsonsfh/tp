---
layout: default.md
title: "Testing guide"
pageNav: 3
---

# 🧪 Testing Guide

<page-nav-print />

---

## ✅ Running Tests

There are two ways to run the test suite:

### Method 1: Using IntelliJ (JUnit Runner)

- To run all tests:  
  Right-click on `src/test/java` → `Run 'All Tests'`
- To run specific tests:  
  Right-click on a test **class**, **method**, or **package** → `Run 'XYZ'`

### Method 2: Using Gradle

- On Windows: `gradlew clean test`
- On macOS/Linux: `./gradlew clean test`

<box type="info" seamless>
💡 Need help with Gradle?  
Check the [Gradle Tutorial](https://se-education.org/guides/tutorials/gradle.html) from se-edu.
</box>

---

## 🧩 Types of Tests in TeamScape

We use a combination of the following test types:

### 1. **Unit Tests**
- Test individual classes and methods.
- Example: `StringUtilTest`, `TaskTest`, `TaskStatusCommandTest`

### 2. **Integration Tests**
- Check how components interact when combined.
- Example: `StorageManagerTest`, `JsonAdaptedPersonTest`

### 3. **Hybrid (Unit + Integration) Tests**
- Simulate full command flows from parsing to execution.
- Example: `LogicManagerTest`, `CommandSystemTest`

---

## 🧪 Task Feature Test Cases

### ✅ Command Parsing

File: `TaskCommandParserTest.java`

Covers:
- Valid task input with all fields
- Missing fields (e.g., description only)
- Invalid field values (e.g., bad date format, invalid status)

Example test:
```java
assertParseSuccess(parser, "1 t/Submit report d/2025-10-10 10:00 s/in progress", expectedCommand);
