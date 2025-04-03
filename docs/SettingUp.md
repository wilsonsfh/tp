---
layout: default.md
title: "Setting up and getting started"
pageNav: 3
---

# Setting up and getting started

<page-nav-print />

---

## 🖥️ Setting up the project on your computer

<box type="warning" seamless>
<strong>Caution:</strong> Follow the steps below precisely. Setup may fail if instructions are skipped or altered.
</box>

1. **Fork** this repository on GitHub, then **clone** your fork to your computer.

2. If you’re using IntelliJ IDEA (highly recommended):

   1. **Configure the JDK**  
      Use [this guide](https://se-education.org/guides/tutorials/intellijJdk.html) to set up **JDK 17** in IntelliJ.

   2. **Import as a Gradle Project**  
      Follow [this guide](https://se-education.org/guides/tutorials/intellijImportGradleProject.html).  
      <box type="warning" seamless>
      ⚠️ Note: Importing a Gradle project is slightly different from a normal Java project.
      </box>

   3. **Verify the Setup**
      - Run the app using `MainApp.java` (entry point: `seedu.address.Main`)
      - Run the tests: `./gradlew test` or open the `Testing.md` for guidance
      - You should see the GUI and be able to enter commands like `list`, `add`, or `task`

---

## 🛠️ Before Writing Code

### 1. Configure the Coding Style

- Follow [this guide](https://se-education.org/guides/tutorials/intellijCodeStyle.html) to align with the project’s Java formatting rules.

<box type="tip" seamless>
💡 Optionally, install Checkstyle in IntelliJ and configure it using [this guide](https://se-education.org/guides/tutorials/checkstyle.html) for real-time style checking.
</box>

---

### 2. CI/CD Setup (Auto-configured)

TeamScape uses GitHub Actions for CI. Configuration files are in `.github/workflows`.  
CI will automatically run on each push or pull request. No extra setup is required.

---

### 3. Learn the Design

Once setup is complete, we recommend familiarizing yourself with TeamScape's overall design:

📖 See: [Developer Guide → Architecture](DeveloperGuide.md#architecture)

---

### 4. Do the Tutorials

These tutorials will help you understand how TeamScape (originally AB3) works:

- [Tracing code](https://se-education.org/guides/tutorials/ab3TracingCode.html)
- [Adding a new command](https://se-education.org/guides/tutorials/ab3AddRemark.html)
- [Removing fields](https://se-education.org/guides/tutorials/ab3RemovingFields.html)

> 🔄 You can adapt these tutorials to your custom commands like `task`, `mark`, or `setduedate`.

---
