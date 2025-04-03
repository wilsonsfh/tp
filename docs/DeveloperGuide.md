<!--
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
-->

# TeamScape Developer Guide

## Table of Contents  
- [Acknowledgements](#acknowledgements)  
- [Setting up, getting started](#setting-up-getting-started)  
- [Design](#design)  
  - [Architecture](#architecture)  
  - [UI component](#ui-component)  
  - [Logic component](#logic-component)  
  - [Model component](#model-component)  
  - [Storage component](#storage-component)  
  - [Common classes](#common-classes)  
- [Implementation](#implementation)  
  - [Proposed Undo/redo feature](#proposed-undoredo-feature)  
  - [Implementation of Task Feature](#Implementation-of-Task-Feature)  
- [Documentation, logging, testing, configuration, dev-ops](#documentation-logging-testing-configuration-dev-ops)  
- [Appendix: Requirements](#appendix-requirements)  
  - [Product scope](#product-scope)  
  - [User stories](#user-stories)  
  - [Use cases](#use-cases)  
  - [Non-Functional Requirements](#non-functional-requirements)  
  - [Glossary](#glossary)  
- [Appendix: Instructions for manual testing](#appendix-instructions-for-manual-testing)  
  - [Launch and shutdown](#launch-and-shutdown)  
  - [Deleting a person](#deleting-a-person)  
  - [Saving data](#saving-data)  

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

[_AB3_](docs\_markbind\layouts\default.md)

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S2-CS2103-F09-4/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S2-CS2103-F09-4/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, `TaskCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

For example, the `AddressBookParser` will route `task` commands to `TaskCommandParser`, which parses the input and creates a `TaskCommand` object. The `TaskCommand` class implements the `Command` interface and defines the execution logic for adding tasks to a person.

The `TaskStatus` enum provides a standardized way to represent the state of a task, namely `YET_TO_START`, `IN_PROGRESS`, and `COMPLETED`.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S2-CS2103-F09-4/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S2-CS2103-F09-4/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### Implementation of Task Feature

#### Overview
The task feature allows users to assign and manage tasks for individual team members. Each `Person` object can contain multiple `Task` instances, each having a description, status, and optional due date.

#### Task Status Enum
We use an `enum TaskStatus` to define task states:
- `YET_TO_START`
- `IN_PROGRESS`
- `COMPLETED`

#### Commands
| Command        | Description                                   |
|----------------|-----------------------------------------------|
| `task`         | Adds a task to a person                       |
| `deltask`      | Deletes a task from a person                  |
| `setduedate`   | Sets a due date for a specific task           |
| `mark`         | Updates the task’s status                     |
| `listtasks`    | Lists tasks for a specific person             |
| `report`       | Generates a task completion summary           |

#### Key Implementation Notes
- Tasks are embedded inside the `Person` model.
- `JsonAdaptedPerson` serializes/deserializes the `tasks` list.
- Tasks are displayed in the UI when `listtasks` is used.
- All task-related commands extend `Command` and use dedicated parsers.

#### Task Structure

<puml src="diagrams/tracing/TaskModelClassDiagram.puml" />

#### Task Command Execution Flow

<puml src="diagrams/tracing/TaskCommandSequenceDiagram.puml" />

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a small team
* need efficient way to track and manage team members' progress and status
* does not require online syncing functionality
* does not require multi-user interactions
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:

* help users to categorize members by position, grade, skills, and department for easy tracking
* help users to assign tasks, break them into subtasks, and track completion as a percentage
* generate overall statistics for individual members and teams, offering insights into progress and efficiency


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​      | I want to …​                                                                                 | So that I can…​                                                                  |
|----------|--------------|----------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------|
| `* * *`  | team manager | add a member, his/her position, department, roles                                            | start tracking my team member progress.                                          |
| `* * *`  | team manager | remove inactive team members                                                                 | keep the team database clean and up-to-date.                                     |
| `* * *`  | team manager | add a task/subtask under a member                                                            | ensure clarity responsibilities.                                                 |
| `* * *`  | team manager | be able to mark the task as completed/in-progress/yet-to-start                               | check the status of my tasks.                                                    |
| `* * *`  | team manager | set due dates for the tasks                                                                  | know what time each task need to be completed by.                                |
| `* * *`  | team manager | receive a summary of task statuses                                                           | stay informed.                                                                   |
| `* * *`  | team manager | quickly search for members’ contacts by name                                                 | call them easily if there is an urgency.                                         |
| `* * *`  | user         | access interactive tutorials or help pop-ups                                                 | quickly learn how to use the app’s features effectively.                         |
| `* *`    | team manager | identify gaps in skills required for the Task and the members/departments assigned to it     | assign more people to the Task to fill up the skill gap.                         |
| `* *`    | team manager | search for tasks sorted by their priority levels                                             | know which tasks need immediate attention.                                       |
| `* *`    | team manager | search for tasks by their assigned deadline                                                  | search for tasks by their assigned deadline.                                     |
| `* *`    | team manager | filter team members by their availability (sorting by number of tasks)                       | assign tasks only to those who are not overloaded.                               |
| `* *`    | team manager | edit the position, department, roles of my team members                                      | easily organise my team structure when there is a change.                        |
| `* *`    | team manager | add skills required for a task                                                               | know which members/department to assign the task to.                             |
| `* *`    | team manager | set priority levels for tasks                                                                | I knows which task need immediate attention and which task can be handled later. |
| `* *`    | team manager | set task dependencies (e.g. Task B can only start after Task A is completed)                 | know if the task have been completed in the right order.                         |
| `* *`    | team manager | see the task completion percentages (no. of subtasks completed out of total no. of subtasks) | easily gauge progress and identify any bottlenecks.                              |
| `* *`    | team manager | bulk import member details via a CSV file                                                    | save time when adding multiple members.                                          |
| `* *`    | team manager | generate a report showing the team's overall task completion rate                            | present it to stakeholders.                                                      |
| `* *`    | team manager | color-code tasks by priority level                                                           | easily differentiate between critical and low-priority tasks.                    |
| `* *`    | team manager | set reminders for upcoming deadlines                                                         | ensure my team stays on track and meets important deadlines.                     |
| `* *`    | team manager | receive notifications when a task is overdue                                                 | address delays immediately.                                                      |
| `* *`    | team manager | quickly search for team members by their progress completion state                           | identify and help those who are lagging behind.                                  |
| `*`      | team manager | add a task/subtask to a department                                                           | mass assign tasks to members in that department.                                 |
| `*`      | team manager | create subtasks for a  task                                                                  | break down large tasks into smaller, manageable pieces.                          |
| `*`      | team manager | track the hours worked by each member on tasks                                               | analyze productivity and effort.                                                 |
| `*`      | team manager | see statistics comparing the progress of different departments/members                       | identify high-performing department/member.                                      |
| `*`      | user         | customize themes and layouts                                                                 | the app feels personal and meets my visual preferences.                          |
| `*`      | user         | take an interactive app tour                                                                 | learn features without external help.                                            |
| `*`      | user         | undo accidental deletions                                                                    | critical data isn’t lost.                                                        |
| `*`      | user         | enable keyboard shortcuts                                                                    | frequent actions (e.g., Ctrl+S to save) are faster.                              |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `manager`, unless specified otherwise)

**Use case: UC1 List members**

**MSS**

1.  Manager requests to list members
2.  AddressBook shows a list of members

**Extensions**

* 2a. The list is empty.

  Use case ends.

**Use case: UC2 List tasks**

**MSS**

1.  Manager requests to list tasks.
2.  AddressBook shows a list of tasks.

**Extensions**

* 2a. The task list is empty.

  Use case ends.

**Use case: UC3 Delete a member**

**MSS**

1.  Manager <ins>list members (UC1)</ins>.
2.  Manager requests to delete a specific member in the list
3.  AddressBook deletes the member

    Use case ends.

**Extensions**

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

**Use case: UC4 Edit a member**

**MSS**

1.  Manager <ins>list members (UC1)</ins>.
2.  Manager requests to edit a specific member in the list
3.  AddressBook edits the member

    Use case ends.

**Extensions**

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

* 3b. Edit command received invalid inputs.

    * 3b1. AddressBook shows an error message

      Use case resumes at step 2.

**Use case: UC5 Add a person**

**MSS**

1.  Manager requests to add a member to the list
2.  AddressBook adds the member

    Use case ends.

**Extensions**

* 2a. The details provided are invalid.

    * 2a1. AddressBook shows an error message.

      Use case resumes at step 1.

**Use case: UC6 Add a task under a person**

**MSS**

1.  Manager <ins>list members (UC1)</ins>.
2.  Manager <ins>list tasks (UC2)</ins>.
3.  Manager requests to add a task under a member.
4.  AddressBook adds a task under a member.

    Use case ends.

**Extensions**

* 4a. The given index for either task is invalid.

    * 4a1. AddressBook shows an error message.

      Use case resumes at step 2.

* 4b. The given index for either member is invalid.

    * 4b1. AddressBook shows an error message.

      Use case resumes at step 2.

**Use case: UC7 Changes a task under a person**

**MSS**

1.  Manager <ins>list members (UC1)</ins>.
2.  Manager <ins>list tasks (UC2)</ins>.
3.  Manager requests to change a task under a member.
4.  AddressBook changes a task under a member.

    Use case ends.

**Extensions**

* 4a. The given index for either task is invalid.

    * 4a1. AddressBook shows an error message.

      Use case resumes at step 2.

* 4b. The given index for either member is invalid.

    * 4b1. AddressBook shows an error message.

      Use case resumes at step 2.

**Use case: UC8 Create a task**

**MSS**

1. Manager requests to create a task.
2. AddressBook creates a task.

   Use case ends.

**Extensions**

* 2a. Invalid task input.

    * 2a1. AddressBook shows an error message.

      Use case ends.

**Use case: UC9 Set a due date for a task**

**MSS**

1. Manager <ins>list tasks (UC2)</ins>.
2. Manager requests to set a due date for a task.
3. AddressBook set a due date for a task.

   Use case ends.

**Extensions**

* 3a. The given task index is invalid.

    * 4a1. AddressBook shows an error message.

      Use case resumes at step 2.

* 3b. The due date format is invalid.

    * 4b1. AddressBook shows an error message.

      Use case resumes at step 2.

**Use case: UC10 Mark the task as completed, in progress, or yet to start**

**MSS**

1.  Manager <ins>list tasks (UC2)</ins>.
2.  Manager requests to set a due date for a task.
3.  AddressBook set a due date for a task.

    Use case ends.

**Extensions**

* 3a. The given task index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

* 3b. The due date format is invalid.

    * 4b1. AddressBook shows an error message.

      Use case resumes at step 2.

**Use case: UC11 Find and return members’ contact by name**

**MSS**

1.  Manager requests to find the member by name.
2.  AddressBook finds the member.

    Use case ends.

**Extensions**

* 2a. Empty member list
    * 2a1. AddressBook shows an error message.

      Use case ends.

* 2b. No member of the name found.
    * 2b1. AddressBook shows an error message.

      Use case ends.

**Use case: UC12 Generate task status report**

**MSS**

1.  Manager requests to generate tasks report.
2.  AddressBook generate tasks report and show.

    Use case ends.

**Extensions**

* 2a. Empty task list
    * 2a1. AddressBook shows an error message.

      Use case ends.

**Use case: UC13 Use of help command**

**MSS**

1.  Manager requests to get help.
2.  AddressBook prompts manager.
3.  Manager input a prompt.
4.  AddressBook shows user guide.
    Steps 3 and 4 are repeated until Manager requests to exit help mode.
5.  User requests to exit help mode.
6.  AddressBook exits help mode.

    Use case ends.

**Extensions**

* 4a. Invalid input from user.
    * 4a1. AddressBook shows an error message.

      Use case resumes at step 2.

* *a. At any time, manager chooses to exit help mode
    * *a1. AddressBook exits help mode.

      Use case ends.

*{More to be added}*

### Non-Functional Requirements
*Based on the AB3 brown-field project*
1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Should not require a persistent internet connection and be able to use the whole system fully offline and locally.
5. Should minimize data loss to 1-minute worth of user input for resilience in unexpected shutdowns.
6. A user needs to confirm when handling duplicate entries instead of allowing silent overwrites.
7. Should work on both 32-bit and 64-bit operating systems for compatibility inclusion.
8. Should be compatible with Windows, macOS, and Linux for cross-platform usage.
9. Should follow a modular code structure, allowing for future enhancements and maintainability.
10. Should complete all CRUD operations (Add, Remove, Update) within 200 milliseconds.
11. Should retrieve and display a list of 1000 members within 1.5 second.
12. Should generate task status reports within a maximum of 3 seconds.
15. Should prevent accidental data loss by implementing confirmation prompts for deletions.
16. Should support automatic backups at periodic intervals or before performing bulk actions.
17. Should process command inputs in a case-insensitive manner to improve usability.
18. Should provide explicit and user-friendly error messages for troubleshooting.
19. Should provide a user manual detailing all available commands.
20. Should document code using JavaDoc for maintainability by future developers.
21. Should allow data recovery from the last saved state in case of crashes.
22. Should consume less than 200MB of memory during normal operations.
23. Should generate reports in a structured format, ensuring usability with spreadsheet applications.
24. Should include unit tests covering at least 80% of core functionalities.
25. Should encrypt or obfuscate sensitive data if required.
26. Should ensure that commands follow a consistent pattern to reduce user confusion.
27. Should return task search results within 1 second.

### Glossary

    - **CLI**: Command Line Interface. A text-based way to interact with the app (e.g., `add n/John`).
    - **Mainstream OS**: Windows, Linux, Unix, macOS.
    - **Private contact detail**: A contact detail not meant to be shared (e.g., personal phone number).
    - **Task**: A unit of work assigned to a team member, which can have subtasks and deadlines.
    - **Subtask**: A smaller component of a task (e.g., "Design UI mockups" as part of "Develop login feature").
    - **Model**: The component managing data (e.g., storing team members and tasks).
    - **UI**: User Interface. The visual part of the app (e.g., buttons, lists).
    - **Storage**: Component that saves/loads data to/from the hard drive.
    - **API**: Application Programming Interface. How components interact (e.g., `Logic.java` defines the API for executing commands).
    - **JSON**: A file format used to store data (e.g., team members are saved in `addressbook.json`).



--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   2. Double-click the jar file or run file with `java -jar teamscape.jar`(recommended) <br> Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Adding a person

**Prerequisites:**
- List all persons using the `list` command.
- Ensure there are no persons with the same details you're about to add.

1. Test case: `add n/John Doe p/98765432 e/johnd@example.com tele/@john pos/student a/John street, block 123, #01-01` 
<br>
**Expected**:
   - New contact is added to the list.
   - Details of the added contact shown in the status message.
   - Contact list updated with the new person.

2. Test case: `add n/John Doe p/98765432` (missing required fields)
<br>
**Expected**:
   - No person is added. 
   - Error details shown in the status message.
   - Member list not updated.

3. Other incorrect add commands to try:
- `add n/John Doe` (missing required fields)
- `add p/98765432 e/johnd@example.com` (missing name)
- `add n/John Doe p/invalid e/invalid` (invalid formats)
<br>
  **Expected**: Similar to previous error case.

### Adding a task to a member

**Prerequisites:**
- Have at least one person in the address book.
- View this person using `list` or `find` command.

1. Test case: `task 1 task/Prepare report, 2025-10-10 10:00, in progress` <br>
**Expected**:
   - Task is added to the first person's task list. 
   - Status message shows task was added. 

2. Test case: `task 1 task/Book venue` (minimal task) <br>
**Expected**:
   - Task is added with default "yet to start" status. 
   - Status message confirms addition.

3. Test case: `task 0 task/Invalid task` <br>
**Expected**:
   - No task added. 
   - Error shown for invalid index.

### Editing a person

**Prerequisites:**
- Have at least one person in the address book.
- View this person using `list` command.

1. Test case: `edit 1 p/91234567 e/johndoe@example.com` <br>
**Expected**:
   - First person's phone and email are updated. 
   - Status message shows changes.

2. Test case: `edit 1 t/` (clearing tags) <br>
**Expected**:
   - All tags removed from first person. 
   - Status message confirms changes.

3. Test case: `edit 0 n/Invalid` <br>
**Expected**:
   - No changes made. 
   - Error shown for invalid index.

### Setting due date for a task

**Prerequisites:**
- Have at least one person with at least one task.

1. Test case: `setduedate 1 taskint/1 due/2026-02-28 23:59` <br>
**Expected**:
   - Due date set for first task of first person. 
   - Status message confirms change.

2. Test case: `setduedate 1 taskint/1 due/2020-01-01 00:00` (past date) <br>
**Expected**:
   - No changes made. 
   - Error message shown.

3. Other incorrect commands to try: `setduedate 1 taskint/1 due/2025-10-10`, `setduedate 1 /taskint 1 due/2025-10-10 23:59` <br>
**Expected**:
   - Error message similar to that of point 2. 

### Listing tasks assigned to a member

**Prerequisites:**
- Have at least one person with tasks.

1. Test case: `listtasks 1`<br>
**Expected**:
   - Tasks for first person displayed. 
   - Status bar shows command success.

2. Test case: `listtasks 0`<br>
**Expected**:
   - No tasks shown.
   - Error about invalid index.

### Deleting a task under a member

**Prerequisites:**
- Have at least one person with at least one task.

1. Test case: `deltask 1 1` <br>
**Expected**:
   - First task of first person deleted. 
   - Status message confirms deletion.

2. Test case: `deltask 1 0` <br>
**Expected**:
   - No task deleted. 
   - Error about invalid index.

### Updating status for a task

**Prerequisites:**
- Have at least one person with at least one task.

1. Test case: `mark 1 1 completed` <br>
**Expected**:
   - First task of first person marked as completed. 
   - Status message confirms change.

2. Test case: `mark 1 1 invalid-status` <br>
**Expected**:
   - No changes made. 
   - Error about invalid status.

### Finding persons

**Prerequisites:**
- Have several persons with different names, tags, and tasks.

1. Test case: `find n/ john` <br>
**Expected**:
   - Persons with "john" in name displayed. 
   - Status shows number of matches.

2. Test case: `find t/ friend` <br>
**Expected**:
   - Persons with "friend" tag displayed.

3. Test case: `find task/ report` <br>
**Expected**:
   - Persons with tasks containing "report" displayed.

4. Test case: `find n/ john t/ friend` <br>
**Expected**:
   - Persons matching both name and tag criteria displayed.

### Generating Task Status Report

**Prerequisites:**
- Have several persons with tasks in different statuses.

1. Test case: `report` <br>
**Expected**:
   - Summary report displayed showing tasks grouped by status.
   - Status bar shows command success.

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   2. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   3. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   4. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.
--------------------------------------------------------------------------------------------------------------------

## **Appendix: Effort**
While this project is a spin-off from AB3, the project was harder due to the introduction of new functions and the purpose of helping team manager to manage tasks. Hence, we needed to create more entities (Classes) to encapsulate attributes and relationships between entity interactions. 

Major challenges encountered includes collaboration between team members especially when there are frequent merge conflicts, introduction of new classes and commands which requires deep understanding of AB3 dependencies, and consensus about certain feature design/UI within the team.

As per our implementation, we achieved a brand new Command Line Interface (CLI) application which not only helps users to manage contacts and their information, our target audience, team leaders, can also reap the benefits of task management. 

Using of app, managers can assign multiple tasks under a specific member, check the tasks information such as due date and status under the member, remove task, and generate a holistic task report to check progress and status. 

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Planned Enhancements**
_coming soon after PE-D_.
