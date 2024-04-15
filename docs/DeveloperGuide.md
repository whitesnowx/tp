---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This developer guide is adapted from [AB-3 Developer Guide](https://se-education.org/addressbook-level3/DeveloperGuide.html).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2324S2-CS2103-F08-3/tp/blob/master/src/main/java/staffconnect/Main.java) and [`MainApp`](https://github.com/AY2324S2-CS2103-F08-3/tp/blob/master/src/main/java/staffconnect/MainApp.java)) is in charge of the app launch and shut down.
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

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2324S2-CS2103-F08-3/tp/blob/master/src/main/java/staffconnect/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI interface is implemented by `UiManager`.

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2324S2-CS2103-F08-3/tp/blob/master/src/main/java/staffconnect/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2324S2-CS2103-F08-3/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to data in `Model` component so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UIManager` relies on the `Logic` component to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model` component.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2324S2-CS2103-F08-3/tp/blob/master/src/main/java/staffconnect/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `LogicManager` is called upon to execute a command, it is passed to an `StaffConnectParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` component when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `LogicManger`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `StaffConnectParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `StaffConnectParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2324S2-CS2103-F08-3/tp/blob/master/src/main/java/staffconnect/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the staff book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` component represents data entities of the domain, they should make sense on their own without depending on other components)
* stores the meeting book data i.e., all `Meeting` objects (which are contained in a `UniqueMeetingList` object) in each `Person` object.
<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `StaffBook`, which `Person` references. This allows `StaffBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2324S2-CS2103-F08-3/tp/blob/master/src/main/java/staffconnect/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both staff book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `StaffBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `staffconnect.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Edit feature

#### How the feature is implemented

The sequence diagram below shows how the edit command `edit 1 p/ 12345678` goes through the `Logic` component.

![Interactions Inside the Logic Component for the `edit 1 p/ 12345678` Command](images/EditSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `EditCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

1. When the user issues the command `edit 1 p/ 12345678`, `LogicManager` is called upon to execute the command, it is passed to the `StaffConnectParser` object which creates a `EditCommandParser` to parse the arguments for the edit command.
2. The parsing of `EditCommandParser` results in a new `EditCommand` initialized by an integer `index` and a `EditPersonDescriptor`. The datails will be explained later.
3. When the `EditCommand` is executed, it creates a new `Person` object according to the `EditPersonDescriptor` passed to it, and replaces the old `Person` object with the new one.
4. The command communicates with the `Model` component when it is executed. More specifically, it calls the `updateFilteredPersonList()` method using a `Predicate` object which simply evaluates to true for all `Person`. The intension is that no `Person` will be filtered out in an edit command.
5.  The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `LogicManager`, to show in the `UI` component the success message that the `Person` at the given index is updated with the new information.

The below sequence diagram goes into more detail on how the command is parsed in `EditCommandParser`.

![Interactions Inside EditCommandParser for the `parse("1 p/ 12345678")` Command](images/EditSequenceDiagram-Parser.png)

1. The string is checked to see if if contains tags. If it does, call the corresponding setter method in `EditPersonDescriptor` object. For tags and availabiliies, all values will be updated.
2. If no field is updated, throw a `ParseException` to indicate that no field is updated.
3. The `EditPersonDescriptor` is used to construct an `EditCommand` object, where `EditCommand` object calls `createEditedPerson()` method using the `EditPersonDescriptor` as an argument.

The below activity diagram illustrates the process when a user executes a edit command.

<img src="images/EditActivityDiagram.png" width="250" />

#### Why edit is implemented this way

The command calls `SetPerson()` method in `Model` component and then refresh the list of `Person` objects.
Below are some explanations for some implementation details.

Check if `editPersonDescriptor.isAnyFieldEdited()`:
This is to make sure at least one field is modified, or the command will not have any impact on the `Model` component.

Call `model.updateFilteredPersonList())` with a `Predicate` that always evaluates to true:
This is to refresh the list of `Person` in `Model` component.

### Find feature

#### How the feature is implemented

The sequence diagram below explains how the find command `find Alex` goes through the `Logic` component.

![Interactions Inside the Logic Component for the `find Alex` Command](images/FindSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `FindCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

1. When user types in `find Alex`, it is passed to `StaffConnectParser`.
2. `StaffconnectParser` then creates a `FindCommandParser` that will parse `Alex` to create a `FindCommand` which utilizes a predicate judge whether `Alex` is contained in the person's name.
3. In `FindCommand`, `ModelManager` executes `updateFilteredPersonList()` method using the predicate mentioned above.
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `LogicManager`, to show in the `UI` component the number of persons listed with `Alex` in the name.

The below sequence diagram goes into more detail on how the command is parsed in `EditCommandParser`.

![Interactions Inside FindCommandParser for the `parse("f/Computing")` Command](images/FindSequenceDiagram-Parser.png)

1. Within `FindCommandParser`, the command string is first trimmed and checked whether it is empty, then splitted into an string array by space characters.
2. `FindCommandParser` then constructs a predicate to test whether the names of `Person` contain any one of the strings in the array mentioned above. This predicate is passed as an argument for the constructor of `FindCommand`.

The below activity diagram illustrates the process when a user executes a find command.

<img src="images/FindActivityDiagram.png" width="250" />

#### Why find is implemented this way

The main operation for the find feature is the `updateFilteredPersonList(Predicate<Person> predicate)` method in the `Model` component.
Below are some explanations for the special considerations in the implementation.

`FindCommmandParser` parsing the `Predicate` objects:
This is to prevent `FindCommand` from taking on more responsibilities (Separation of Concerns).

### Filter feature

#### How the feature is implemented

The sequence diagram below shows how the filter command `filter f/Computing` goes through the `Logic` component.

![Interactions Inside the Logic Component for the `filter f/Computing` Command](images/FilterSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `FilterCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

1. When the user issues the command `filter f/Computing`, `LogicManager` is called upon to execute the command, it is passed to the `StaffConnectParser` object which creates a `FilterCommandParser` to parse the arguments for the filter command.
2. This results in a `FilterCommand` object, which then creates a `Predicate` object.
3. The command communicates with the `Model` component when it is executed. More specifically, it calls the `updateFilteredPersonList()` method using the `Predicate` object created earlier as the argument. Note that although it is shown as a single step in the diagram (for simplicity), in the code it takes several.
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `LogicManager`, to show in the `UI` component the number of persons listed with the `Faculty` value of "Computing".

The below sequence diagram goes into more detail on how the command is parsed in `FilterCommandParser`.

![Interactions Inside FilterCommandParser for the `parse("f/Computing")` Command](images/FilterSequenceDiagram-Parser.png)

Within `FilterCommandParser`, the filtering criteria is parsed into `PersonHasModulePredicate`, `PersonHasFacultyPredicate`, `PersonHasTagsPredicate`, `PersonHasAvailabilitiesPredicate` objects, which extend from `Predicate`.
These `Predicate` objects are then used to construct a `FilterCommand` object, where `FilterCommand` creates its own `Predicate` object by self-calling the `setPersonPredicate()` method, where the `Predicate` produced is to be used as the argument for the `updateFilteredPersonList()` method.

The below activity diagram illustrates the process when a user executes a filter command.

<img src="images/FilterActivityDiagram.png" width="450" />

#### Why filter is implemented this way

The main operation for the filter feature is the `updateFilteredPersonList(Predicate<Person> predicate)` method in the `Model` component.
The following are some explanations for decisions made in the implementation of the filter feature.

Need for multiple `Predicate` objects:
This is to keep in view for when other commands or enhancements may need the separate attribute predicates.

`FilterCommmandParser` parsing the `Predicate` objects:
This is to prevent `FilterCommand` from taking on more responsibilities (Separation of Concerns).

`FilterCommand` having `setPersonPredicate()` method:
This is so that `FilterCommand` has the required argument of type `Predicate<Person>` to be used in the `updateFilteredPersonList()` method. Since the `Predicate<Person>` object is created by chaining the multiple predicates, no parsing is involved to create this `Predicate`.

### Sort feature

##### How the feature is implemented

The sort mechanism is facilitated by JavaFX's `SortedList` within ModelManager, `SortCommand` and `SortCommandParser`. `SortCommandParser` extends the types of command parsers in StaffBookParser, and returns a `SortCommand` to be executed by the LogicManager. This execution also updates the `SortedList` in  Model via ModelManager. Additionally, it implements the following operations:

* `SortCommandParser#parse()`  — Parses user input to identify the attribute to be sorted
* `ModelManager#updateSortedPersonList()` — Update the comparator used by SortedList resulting in the data being sorted accordingly

Given below is an example usage scenario and how the sort mechanism behaves at each step.

1. The user enters **“sort n/”** to sort the list by their name.

2. The `LogicManager` takes this command text and calls `StaffBookParser.parseCommand("sort n/")` and identifies the sort command. It then creates a new instance of `SortCommandParser` to `parse(“n/”)` on the attribute.

3. `SortCommandParser.parse(“n/”)` then constructs a SortCommand with the appropriate attribute comparator, `NameComparator`.

4. The `SortCommand` is returned to Logic manager which calls on its `execute()` to return a `CommandResult()`. During its execution, `ModelManager.updateSortedPersonList(NameComparator)` is invoked which updates the model to show the list of persons being sorted by name.

The sequence diagram for executing a **"sort n/"** is shown below:

<img src="images/SortSequenceDiagram.png" width="850" />


The following activity diagram summarizes what happens when a user executes a new sort command:

<img src="images/SortActivityDiagram.png" width="450" />

#### Why sort is implemented this way

The main operation for the sort feature is the `updateSortedPersonList(Comparator<Person> comparator)` method in the `Model` component.
The following are some explanations for decisions made in the implementation of the sort feature.

Need for multiple `Comparator` objects:
This is to keep in view for when other commands or enhancements may need the separate attribute predicates.

Need for `MultiComparator` object:
This is to map the 1 or more comparator objects and act as a layer of abstraction where `SortCommmand` does need to know how many attributes are used in sorting.

`SortCommmandParser` parsing the `Comparator` objects:
This is to prevent `SortCommand` from taking on more responsibilities (Separation of Concerns).

#### What designs were considered
**Aspect: Determining order of sorting of attribute(s):**

* **Current Design:** Get sorting order of attribute(s) from user input.
    * Pros: More functionality and more suited to the user's needs.
    * Cons: Harder to implement and guide user to use, may have more leeway for error.

* **Alternative 1:** Use a configured comparator for each attribute in ascending order.
    * Pros: Controlled and more simple for user.
    * Cons: Less flexibility and unable to do more advance sorting such as multiple attributes. We must implement a comparator for each attribute used for sorting.

**Aspect: Number of Attribute:**

* **Current Design:** 1 or more attribute per sort.
    * Pros: More functionality, more advanced view of contacts.
    * Cons: Harder to implement, order of prefix affects priority of attribute and have to specify to user.

* **Alternative 1:** Only 1 attribute per sort.
    * Pros: Easy to implement, controlled and less likely to be used incorrectly. This increase ease of use for users.
    * Cons: Limited sorting and lesser functionality.

### Meeting feature

Meeting is feature that allows the user to keep track of any events they may have with the particular contact. It contains the description of the meeting event with the date and time it would occur.

#### How the feature is implemented

Meeting contains two attributes ```MeetingDescription``` and ```MeetingDateTime``` class. ```MeetingDescription```
is used to handle any valid description of the meeting with only alphanumeric values, while the ```MeetingDateTime```
is used to handle any valid date time values. Each of this meeting are stored in a list data class ```MeetingList``` that
contains each of the meetings related to each other stored in an ```ObservableList```. The ``` MeetingManager ``` is
used to manage any operations that require viewing or sorting of meetings from the ```MeetingList``` class.

The operations for adding and deleting meeting are handled by `AddMeetingCommand` and `DeleteMeetingCommand`, which are supported by `AddMeetingCommandParser` and `DeleteMeetingCommandParser` respectively.

1. The user enters `meeting-add 2 d/Finals s/20/04/2024 15:00` to add a meeting or `meeting-delete 1 i/1` to delete a meeting.
2. `Logic Manager` receives the user input which is parsed by `StaffConnectParser`.
3. After splitting the user input into `commandWord` and `arguments` based on the regex pattern of the user input, the `StaffConnectParser` invokes the `AddMeetingCommandParser`or`DeleteMeetingCommandParser` based on the `commandWord`. Calling the method `parse` with `arguments` as the method arguments, and getting supported by parsing methods from `ParsedUtil`. 
4. `AddMeetingCommand` or `DeleteMeetingCommand` is created with the parsed values.
5. `Logic Manager` executes the `AddMeetingCommand` or `DeleteMeetingCommand`, which handles adding/removing meeting from the `Person` respectively and updates the model with the new information.

Below is the sequence diagram for parsing inputs with  `AddMeetingCommandParser` executing `meeting-add 2 d/Finals s/20/04/2024 15:00`:
<br>![AddMeetingCommandParser Sequence Diagram](images/AddMeetingParserSequenceDiagram.png)
<br>Below in the in-depth reference of how `AddMeetingCommandParser` utilise `ParseUtil` to parse the arguments:
<br>![Add Parser Reference Diagram](images/AddParserRefrenceDiagram.png)
<br> Similarly, the sequence diagram for parsing inputs with `DeleteMeetingCommandParser` executing `meeting-delete 1 i/1`:
<br>![DeleteMeetingCommandParser Sequence Diagram](images/DeleteMeetingParserSequenceDiagram.png)
<br><br>
After parsing, the commands are executed by the logic manager as show below. (Execute in the diagrams below comes form the logic manager)
<br> Below is the sequence diagram for adding meeting with  `AddMeetingCommand`:
<br>![AddMeetingCommand Sequence Diagram](images/AddMeetingSequenceDiagram.png)
<br> Similarly the sequence diagram for deleting meeting with `DeleteMeetingCommand`:
<br>![DeleteMeetingCommand Sequence Diagram](images/DeleteMeetingSequenceDiagram.png)
<br> Below is the sequence diagram of how both `AddMeetingCommand` and `DeleteMeetingCommand` copies the selected person from the model for editing:
<br>![Copy selectedPerson](images/MeetingCopyPerson.png)

#### What designs were considered

**Aspect: How the meetings are stored :**

* **Current Design:** Store meetings in an ObservableList.
    * Pros: Better segregation of the OOP functionalities, and good integration with the UI ListView.
    * Cons: Larger code complexity.

* **Alternative 1:** Store meetings in a Set.
    * Pros: Easier implementation.
    * Cons: There is an efficiency gap as each element has to be placed into a list before it can be shown to the UI ListView.

### Fav/unfav feature

The feature enables us to sets/remove a particular contact using an index as favourite.

#### How the feature is implemented

The Fav/Unfav feature is implemented via the `FavCommand` and `UnfavCommand`, which is supported by the `FavCommandParser` and `UnfavCommandParser` respectively.
The `FavCommandParser` and `UnfavCommandParser` implements the `Parser` interface.

1. `LogicManager` receives the user input which is parsed by the `StaffConnectParser`.
2. After splitting the user input into `commandWord` and `arguments` based on the regex pattern of the user input, the `StaffConnectParser` invokes the `FavCommandParser`/`UnfavCommandParser` based on the `commandWord`, calling the method `parse` with `arguments` as the method arguments
3. `FavCommandParser`/`UnfavCommandParser` takes in the `args` string and parse it into with the static `ParserUtil#parseIndex(args)` function. If the `INDEX` format is invalid, a `ParseException` will be thrown.
4. `FavCommandParser`/`UnfavCommandParser` then creates the `FavCommand`/`UnfavCommand` and returns it.
5. The `LogicManager` executes the `FavCommand`/`UnfavCommand`, which creates a `Person` with the `Favourite` attribute set as `true`/`false` respectively and updates the model with this new `Person`.

The following sequence diagram shows how the `fav` command works:

![Fav Command Sequence Diagram](images/FavSequenceDiagram.png)

Similarly, how the `unfav` command works is shown below:

![Unfav Command Sequence Diagram](images/UnfavSequenceDiagram.png)

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedStaffBook`. It extends `StaffBook` with an undo/redo history, stored internally as an `staffBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedStaffBook#commit()` — Saves the current staff book state in its history.
* `VersionedStaffBook#undo()` — Restores the previous staff book state from its history.
* `VersionedStaffBook#redo()` — Restores a previously undone staff book state from its history.

These operations are exposed in the `Model` interface as `Model#commitStaffBook()`, `Model#undoStaffBook()` and `Model#redoStaffBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedStaffBook` will be initialized with the initial staff book state, and the `currentStatePointer` pointing to that single staff book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the staff book. The `delete` command calls `Model#commitStaffBook()`, causing the modified state of the staff book after the `delete 5` command executes to be saved in the `staffBookStateList`, and the `currentStatePointer` is shifted to the newly inserted staff book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitStaffBook()`, causing another modified staff book state to be saved into the `staffBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitStaffBook()`, so the staff book state will not be saved into the `staffBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoStaffBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous staff book state, and restores the staff book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial StaffBook state, then there are no previous StaffBook states to restore. The `undo` command uses `Model#canUndoStaffBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoStaffBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the staff book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `staffBookStateList.size() - 1`, pointing to the latest staff book state, then there are no undone StaffBook states to restore. The `redo` command uses `Model#canRedoStaffBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the staff book, such as `list`, will usually not call `Model#commitStaffBook()`, `Model#undoStaffBook()` or `Model#redoStaffBook()`. Thus, the `staffBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitStaffBook()`. Since the `currentStatePointer` is not pointing at the end of the `staffBookStateList`, all staff book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire staff book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.


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

Bob is a 22 year old NUS SOC student who often struggles with finding details about his professors' and tutors' consultation hours.
He has difficulty identifying his professors and changing tutors, and prefers certain professors and tutors but often misplaces their contact information as such information can be hard to find online.
He also sometimes forgets that he has scheduled consultations with a professor or tutor, but this is not a big problem as he can always arrange for another consultation.

**Value proposition**:

StaffConnect offers convenience and efficiency for a forgetful tech-savvy student.
StaffConnect allows users to easily identify and connect with educators by providing visual cues alongside their contact details, supported by an easy-to-use filtering system.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                     | I want to …​                    | So that I can…​                                                         |
| -------- | ------------------------------------------ | ------------------------------ | ---------------------------------------------------------------------- |
| `* * *`  | sociable user                              | save a professor's/tutor's name, phone number, faculty, consultation venue, module, email, tag(s) and availabilities in one line | save time when adding each professor/tutor                    |
| `* * *`  | clumsy user                                | edit a professor's/tutor's name, phone number, faculty, consultation venue, module, email, tag(s) and availabilities in one line | save time when editing multiple attributes of a professor/tutor                    |
| `* * *`  | disorganised student                       | store a professor's/tutor's name       | recall how to address the professor/tutor                                    |
| `* * *`  | disorganised student                       | store the module a professor/tutor is teaching  | contact the professor/tutor who teach a module which I am currently taking       |
| `* * *`  | forgetful user                             | store a professor's/tutor's faculty    | see the faculty that a professor/tutor belongs to                            |
| `* * *`  | student who get lost easily                | view the consultation venues of my professors/tutors | search for their consultation venues easily                |
| `* * *`  | disorganised student                       | store a professor's/tutor's availabilities | schedule meetings to meet my professor/tutor for consultation            |
| `* * *`  | organised user                             | delete a staff book entry | remove outdated or redundant entries of professors/tutors that I will not contact anymore                  |
| `* * *`  | slow reader                                | filter through staff book entries by their name | not waste time in finding a specific professor/tutor and access their information easily |
| `* * *`  | slow reader                                | filter through staff book entries by their availability | not waste time in finding a specific professor/tutor and access their information easily |
| `* * *`  | slow reader                                | filter through staff book entries by their module | not waste time in finding a specific professor/tutor and access their information easily |
| `* * *`  | slow reader                                | filter through staff book entries by their faculty | not waste time in finding a specific professor/tutor and access their information easily |
| `* * *`  | slow reader                             | filter through staff book entries by their tag | not waste time in finding a specific professor/tutor and access their information easily |
| `* * *`  | slow reader                             | sort staff book entries by name | not waste time in finding a specific professor/tutor and access their information easily |
| `* * *`  | slow reader                             | sort staff book entries by phone number | not waste time in finding a specific professor/tutor and access their information easily |
| `* * *`  | slow reader                             | sort staff book entries module | not waste time in finding a specific professor/tutor and access their information easily |
| `* * *`  | slow reader                             | sort staff book entries faculty | not waste time in finding a specific professor/tutor and access their information easily |
| `* * *`  | slow reader                             | sort staff book entries' consultation venues | not waste time in finding a specific professor/tutor and access their information easily |
| `* *`  | slow reader                             | sort staff book entries' meeting times | not waste time in finding a specific meeting and access their information easily |
| `* *` | time-conscious user | save a specific professor as "favourite" | have quick access to the professors/tutors I frequent the most for consultations |
| `* *` | time-conscious user | remove a specific professor as "favourite" | remove outdated professors/tutors that I do not frequent for consultations anymore |
| `* *` | easily-distracted user | record my scheduled meeting agenda and start time with professors/tutors | see which professor/tutor I have set up to meet with |
| `* *` | organised user | delete my scheduled meeting agenda and start time with professors/tutors | remove outdated or redundant entries of meetings that have passed or cancelled                  |
| `* *` | time-conscious user | clear my outdated meetings with professors/tutors | save time by removing outdated meetings with one command |
| `* *` | proficient typer | select a professor/tutor to see their contact details with a command | use the app with my more proficient method of typing instead of using other input devices (i.e. mouse) |

### Use cases

(For all use cases below, the **System** is the `StaffConnect` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Add a person**

**MSS**

1. StaffConnect shows a list of persons
2. User requests to add a new person

   Use case ends.

**Extensions**

* 1a. The list is empty.

  Use case resumes at step 2.

**Use case: Filter the list**

**MSS**

1. StaffConnect shows a list of persons
2. User requests to filter the list by a specific attribute
3. StaffConnect shows a filtered list of persons

   Use case ends.

**Extensions**

* 1a. The list is empty.

  Use case ends.

* 2a. The list is empty.

  Use case ends.

**Use case: Sort the list**

**MSS**

1. StaffConnect shows a list of persons
2. User requests to sort the list by a specific attribute
3. StaffConnect shows a sorted list of persons

   Use case ends.

**Extensions**

* 1a. The list is empty.

  Use case ends.

**Use case: Delete a person**

**MSS**

1. StaffConnect shows a list of persons
2. User requests to delete a specific person in the list
3. StaffConnect deletes the person

   Use case ends.

**Extensions**

* 1a. The list is empty.

  Use case ends.

* 2a. The given index is invalid.

    * 2a1. StaffConnect shows an error message.

      Use case resumes at step 1.

**Use case: Edit a person**

**MSS**

1. StaffConnect shows a list of persons
2. User requests to edit a specific person in the list
3. StaffConnect edits the person

   Use case ends.

**Extensions**

* 1a. The list is empty.

  Use case ends.

* 2a. The given index is invalid.

    * 2a1. StaffConnect shows an error message.

      Use case resumes at step 1.


**Use case: Add a meeting**

**MSS**

1. StaffConnect shows a list of persons
2. User requests to add a meeting to the specific person in the list
3. StaffConnect adds the meeting to the person with the provided details

   Use case ends.

**Extensions**

* 1a. The list is empty.

  Use case ends.

* 2a. The given index is invalid.

    * 2a1. StaffConnect shows an error message.

      Use case resumes at step 1.

* 3a. The given details for meeting is invalid.
    * 3a1. StaffConnect shows an error message.
      Use case resumes at step 1.


**Use case: Delete a meeting**

**Precondition:** The intended meeting to delete exists and has been added before.

**MSS**

1. StaffConnect shows a list of persons
2. User requests to delete a meeting of a specific person in the list
3. StaffConnect deletes the specified meeting

   Use case ends.

**Extensions**

* 1a. The list is empty.

  Use case ends.

* 2a. The given index for person is invalid.

    * 2a1. StaffConnect shows an error message.

      Use case resumes at step 1.

* 2a. The given index for meeting is invalid.
    * 2a1. StaffConnect shows an error message.
      Use case resumes at step 1.


### Non-Functional Requirements

1. The app should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. The app should be able to respond to a user's prompt within 2 seconds.
3. The app should not exceed using 1GB of RAM while it is operating.
4. The app should work on both 32-bit and 64-bit environments.
5. The app should be able to store up to 1000 persons without affecting the response time of 2 seconds.
6. The app should be able to store up to a total of 1000 meetings across all persons without affecting the response time of 2 seconds.
7. The app should only be able to read and write in the generated `[JAR file location]/data/staffconnect.json` file.
8. The app should be usable by a student who is familiar with CLI interfaces.
9. The app should be up-to-date with the latest NUS faculty names.
10. The data stored in the app should not change unless the user has modified the data through usage of the app with user-issued commands, or the `[JAR file location]/data/staffconnect.json` file has been modified with valid values.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS, with versions that support Java 11
* **Person**: A professor or tutor (i.e. Teaching Assistant)
* **Attribute**: A useful piece of information belonging to a `Person`. e.g `Venue` is the consultation venue to consult a `Person`
* **Staff Book**: Name for the list containing `Person` objects
* **Contacts' Information**: All `Persons` in the staff book
* **Error Message**: A prompt printed to the user that the program execution cannot run normally and specifies the most possible cause
* **MSS**: Main Success Scenario, a sequence of steps to reach the end of a use case

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

### Saving data

1. Dealing with missing/corrupted data files

   1. Prerequisites: Ensure that the `[JAR file location]/data/staffconnect.json` file is generated by running the JAR file of the app at least once.

   1. Test case: No modifications to data file after it has been generated.<br>
      In the image below shows the contents of the untouched data file:
      ![Before Corrupt Data File](images/beforeCorruptDataFile.png)

      Expected: The app should show a list of 6 persons
      ![Before Corrupt Data File Result](images/beforeCorruptDataFileResult.png)

   1. Test case: Invalid modification to data file.<br>
      Modify the `Favourite` attribute value to `Not avourite` (an invalid value) in the data file:
      ![After Corrupt Data File](images/afterCorruptDataFile.png)

      Expected: The app should show an empty list (no persons)
      ![After Corrupt Data File Result](images/afterCorruptDataFileResult.png)

   1. Test case: Valid modification to data file.<br>
      Before, `Alex Yeoh` has the module `CS1101S` in the untouched data file as seen in `Test case: No modifications to data file after it has been generated`.

      Modify the `Module` attribute value to `CS2030S` (a valid value) in the data file:
      ![After Valid Modification To Data File](images/afterValidModificationToDataFile.png)

      Expected: The app should show `Alex Yeoh` with the module `CS2030S`:
      ![After Valid Modification To Data File Result](images/afterValidModificationToDataFileResult.png)
