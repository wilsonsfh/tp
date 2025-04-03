---
layout: default.md
title: "Documentation guide"
pageNav: 3
---

# Documentation Guide

* We use [**MarkBind**](https://markbind.org/) to manage documentation.
* The `docs/` folder contains the source files for the documentation website.

**Style guidance:**

* Follow the [**_Google developer documentation style guide_**](https://developers.google.com/style).
* Also relevant is the [_se-edu/guides **Markdown coding standard**_](https://se-education.org/guides/conventions/markdown.html).

**Converting to PDF**

* See the guide [_se-edu/guides **Saving web documents as PDF files**_](https://se-education.org/guides/tutorials/savingPdf.html).

---

## 📦 Feature: Task Management

### 1. Task Model

Each `Person` object contains a list of `Task` objects via composition.

- **Class**: `seedu.address.model.task.Task`
- **Fields**:
  - `description`: String
  - `status`: Enum (`YET_TO_START`, `IN_PROGRESS`, `COMPLETED`)
  - `dueDate`: LocalDateTime

---

### 2. Task Commands and Parsers

| Command        | Command Class             | Parser Class                    |
|----------------|---------------------------|---------------------------------|
| `task`         | `TaskCommand`             | `TaskCommandParser`             |
| `deltask`      | `DeleteTaskCommand`       | `DeleteTaskCommandParser`       |
| `mark`         | `TaskStatusCommand`       | `TaskStatusCommandParser`       |
| `listtasks`    | `ListMemberTasksCommand`  | `ListMemberTasksCommandParser`  |
| `setduedate`   | `SetDueDateCommand`       | `SetDueDateCommandParser`       |

---

### 3. Execution Flow

When the user inputs a command like `task 1 t/Finish draft`:

1. `LogicManager` receives the input and calls `AddressBookParser`.
2. `AddressBookParser` identifies it as a task command and delegates to `TaskCommandParser`.
3. The parser extracts arguments and constructs a `TaskCommand`.
4. `LogicManager` executes the command, which updates the `Model` and triggers a UI refresh.

---

### 4. Class Diagram

<include src="diagrams/TaskModelClassDiagram.puml" />

This diagram shows the relationship between `Person`, `Task`, and `TaskStatus`.

---

### 5. Sequence Diagram

<include src="diagrams/TaskCommandSequenceDiagram.puml" />

This diagram shows how a `task` command is parsed and executed from UI input through Logic and into the Model.

---
