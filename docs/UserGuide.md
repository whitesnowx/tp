---
layout: page
title: User Guide
---

StaffConnect (SC) is a **desktop app for managing contacts of Professors and Tutors, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, SC can get your contact management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `11` or above installed in your Computer.

2. Download the latest `StaffConnect.jar` from [here](https://github.com/AY2324S2-CS2103-F08-3/tp/).

3. Copy the file to the folder you want to use as the _home folder_ for your StaffConnect application.

4. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar StaffConnect.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>__(The data in preview image below may not match with the sample data provided)__<br>
   ![Ui](images/Ui.png)

5. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

    * `list` : Lists all contacts.

    * `add n/John Doe p/98765432 e/johnd@example.com m/CS2103 f/Computing v/John street, block 123, #01-01` : Adds a contact named `John Doe` to the contacts list.

    * `delete 3` : Deletes the 3rd contact shown in the current list.

    * `clear` : Deletes all contacts.

    * `exit` : Exits the app.

6. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------


## Navigability

### Mouse and Keyboard controls

Before we get started StaffConnect offers a unique suite of UI controls for users to customise their own unique experience!

1. Clicking any items on the left panel will allow you to select the person contact to display.
<br>Alternatively, clicking anywhere in the list panel then using your arrow keys to navigate and hitting enter to select would give the same result.
<br>![Region to select the person](images/personPanelRegion.png)

2. There is a divider that is draggable up and down to hide and show details on the right side and to customise the look of your application.
<br>![Region to select the divider](images/detailsDividerRegion.png)

3. Each of the 3 display panes of information they are able to pan in all four directions to view the content:
    - For mouse pad users, dragging around with two fingers the around would pan around the pane.
    - For mouse users, __[mouse wheel]__ will scroll up and down while __[shift + mouse wheel]__ will scroll left and right.
    - __[Left click + drag]__ would pan around in the details pane as well.
    - Keyboard arrow keys are able to pan around as well, but the scroll speed may differ on different systems.
    - There are scroll bars at the vertical and horizontal dividers of the window pane, dragging them in the respective direction will pan around as well.
    

<Br><Br> 
### Alternative UI arrangements
<br>![first alternative ui](images/firstAlternative.png)
<br>
<br>![second alternative ui](images/secondAlternative.png)

**Intended Limitations**
1. The divider position is not controllable by keyboard input, hence the only way to customise the look is mostly by mouse input.
2. Pane switching by keyboard input, like a terminal is not supported.
3. Font sizes does not automatically resize in this application, scroll bars will appear in smaller window variants of this application to help with the viewing of details.
4. The UI will do a soft reset on its divider position every time the application is relaunched, as this is to allow users who wish to fall back to the default layout settings.


--------------------------------------------------------------------------------------------------------------------


## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a person to the contacts.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL m/MODULE f/FACULTY v/VENUE [t/TAG]…​ [a/AVAILABILITY]…​`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A person can have any number of tags and availabilities (including 0)
</div>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com f/Computing v/John street, block 123, #01-01 m/CS2103`
* `add n/Chihiro Ogino m/GEC1024 e/spirited@example.com f/FASS v/The Spirit Realm p/20122001 a/tues 21:00 22:00 a/wednesday 00:00 23:59`
* `add n/Betsy Crowe t/friend m/CS2103T e/betsycrowe@example.com f/Computing v/Newgate Prison p/1234567 t/criminal a/monday 11:00 12:00 a/wednesday 14:00 16:00`

![Effects of an add command](images/AfterAddCommand.png)

### Listing all persons : `list`

Shows a list of all persons in the contacts.

Format: `list`

### Editing a person : `edit`

Edits an existing person in the contacts.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [m/MODULE] [f/FACULTY] [v/VENUE] [t/TAG]…​ [a/AVAILABILITY]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
  specifying any tags after it.
* When editing availabilities, the existing availabilities of the person will be removed i.e adding of availabilities is not cumulative.
* You can remove all the person’s availabilities by typing `a/` without
  specifying any availabilities after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 3 m/GEA1000` Edits the module of the 3rd person to be `GEA1000`. 
* `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.
    <br>**Before editing the second person:** <br>
    ![Before editing the second person](images/BeforeEditCommand.png)
    <br>
    <br>**After editing the second person:** <br>
    ![After editing the second person](images/AfterEditCommand.png)

### Filtering persons: `filter`

Filters persons whose module, faculty, tags or availabilities match the given filtering criteria.

format: `filter [m/MODULE] [f/FACULTY] [t/TAG]… [a/AVAILABILITY]…`

* At least one of the optional fields must be provided.
* Only module, faculty, tags and availabilities can be filtered.
* The filter only accepts a single module to filter from.
* The filter only accepts a single faculty to filter from.
* The filter only accepts valid values for faculty. e.g `faculty of business` is an invalid faculty value.
* The filter accepts single or multiple tags to filter from.
* The filter accepts single or multiple availabilities to filter from.
* The filter only accepts whole values for the available filtering criteria. e.g `mon` does not match `mon 12:00 13:00`, `cs` does not match `CS2030S`
* The filter is case-insensitive for the available filtering criteria. e.g `tUTOR` will match `tutor`, `cs2100` will match `CS2100`
* Persons matching all fields will be returned (i.e. `AND` search).
  e.g. `filter m/CS2030S f/Computing` will return `Charlotte Oliveiro`

Examples:
* `filter m/CS1101S` returns `Alex Yeoh`
* `filter f/Computing t/professor` returns `Alex Yeoh`, `Charlotte Oliveiro`, `David Li` and `Roy Balakrishman`
* `filter t/tutor` returns `Bernice Yu`, `Irfan Ibrahim`<br>
  ![result for 'filter t/tutor'](images/filterTutorTagResult.png)

### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find li` returns `David Li`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Sorting persons by attribute: `sort`

Sorts the list of persons based on specified attribute.

Format: `sort [ATTRIBUTE]`

* By default, sorting is done in alphanumeric order.
* The order of character priority would be letters (A-Z), numbers (0-9), special characters (!@#$%^&*).
* The capitalisation of the letters do not affect their priority such that `Aaron` will have same priority as `aaron`.
* For attribute with exact same values, the tie-breaker is determined by their added order.
* For sorting of multiple attributes, the weightage will be determined by the order in which it was entered. E.g `sort m/ p/ v/` will sort by contact by their module, among those with equal modules would then be sorted by their phone number and similarly for venue.
* `[ATTRIBUTE]` is to be noted by their prefix. e.g `name` will be `n/`.
* `s/` sorts contacts by person with the earliest meeting
* `meet/` sorts contacts by person with the earliest meeting, followed by alphanumeric order of meeting description

Examples:
* `sort m/ p/` returns person by ascending module codes followed by ascending phone numbers `CS2000 80000000`, `CS2000 90000000`, `CS3000 80000000`followed by `CS3000 90000000`
* `sort n/` returns person by ascending names `Alex`, `Bernice` followed by `Charlotte`
* `sort p/` returns person by ascending phone numbers `87438807`, `91031282` followed by `92492021`<br>
  ![result for 'sort p/'](images/sortByPhoneNumberResult.png)

### Adding a meeting to a person: `meeting-add`

Add a meeting to a person based on specified description and date.

Format: `meeting-add INDEX d/DESCRIPTION s/DATETIME`

* Adds a meeting to the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3,…​ and tally within range index of the displayed list.
* Both of the fields must be provided and valid values.
* A valid `DESCRIPTION` of the meeting can only contain latin alphanumeric characters.
* A valid `DATETIME` of the meeting can only contain valid date and 24 hour time values with a single space to separate the date and time. Multiple formats are allowed.
    * Date Formats
        - Separators: `-`
            1. `d-M-yyyy`
            2. `dd-M-yyyy`
            3. `d-MM-yyyy`
            4. `dd-MM-yyyy`
            5. `yyyy-M-d`
            6. `yyyy-MM-d`
            7. `yyyy-MM-dd`
        - Separators: `/`
            1. `d/M/yyyy`
            2. `dd/M/yyyy`
            3. `d/MM/yyyy`
            4. `dd/MM/yyyy`
            5. `yyyy/M/d`
            6. `yyyy/M/dd`
            7. `yyyy/MM/d`
            8. `yyyy/MM/dd`
    * Time Formats
        1. `HH:mm`
        2. `H:mm`
        3. `HHmm`
* Duplicate meetings with the same `DESCRIPTION` and `DATETIME` in the same person is not allowed.

Examples:
* `meeting-add 1 d/Meet for finals preparation s/12/04/2024 18:00` adds a meeting to the first person with the description of `Meet for finals preparation` and the date and time of `12/04/2024 18:00`
* `meeting-add 1 d/CS2100 Consultation s/11/06/2024 08:00` adds a meeting to the first person with the description of `CS2100 Consultation` and the date and time of `11/06/2024 08:00`
* `meeting-add 2 d/Meet for practical exam s/20/04/2024 15:00` adds a meeting to the second person with the description of `Meet for practical exam` and the date and time of `20/04/2024 15:00`

<br>**Result for add meeting:** <br> `meeting-add 1 d/Meet for finals preparation s/12/04/2024 18:00`<br>
  ![result for 'meeting-add 1 d/Meet for finals preparation s/12/04/2024 18:00'](images/addMeetingResult.png)

### Deleting a meeting from a person: `meeting-delete`

<div markdown="block" class="alert alert-danger">:warning: **Caution:**
There will be no further prompt after entering the command to delete a meeting from a person. This action is irreversible and the meeting information to be deleted cannot be retrieved afterwards.
</div>

Deletes a meeting from a person based on specified meeting index.

Format: `meeting-delete INDEX i/MEETING-INDEX `

* Deletes the  meeting at specified `MEETING-INDEX` from the person at specified `INDEX`. 
* The index refers to the index number shown in the displayed person list. 
* The index **must be a positive integer** 1, 2, 3,…​ and tally within range index of the displayed person list.
* The meeting-index refers to the index number shown in the displayed meeting list.
* The index **must be a positive integer** 1, 2, 3,…​ and tally within range index of the displayed meeting list.
* The meeting from the person must exist before it can be deleted otherwise an error will be displayed.
Examples:
* The following commands assumes that meetings have been added prior to the command. Otherwise, an error will be thrown. <br> **(Refer to the section above on how to add a meeting)**
  * `list` followed by `meeting-delete 1 i/1` deletes the 1st meeting from the 1st person in the contacts.
  * `find Bernice Yu` followed by `meeting-delete 1 i/2` deletes the 1st meeting form the 1st person in the results of the `find` command.
      
<br>**Results for delete meeting:**<br>
The following command was applied:  `find Bernice Yu` followed by `meeting-delete 1 i/2`.
<br> __(Disclaimer: The content shown in the examples may not match what you have added to your own meetings within the contact book).__
<br><br> **After `find Bernice Yu`:**<br>
    ![result for before `find Bernice Yu` followed by `meeting-delete 1 i/2`](images/meetingDeleteResultBefore.png)
<br><br> **After `meeting-delete 1 i/2`:**<br>
    ![result for after `find Bernice Yu` followed by `meeting-delete 1 i/2`](images/meetingDeleteResultAfter1.png)
<br><br> **After `meeting-delete 1 i/1`:**<br>
    ![result for after `find Bernice Yu` followed by `meeting-delete 1 i/2`](images/meetingDeleteResultAfter2.png)

### Deleting a person : `delete`

<div markdown="block" class="alert alert-danger">:warning: **Caution:**
There will be no further prompt after entering the command to delete a person in the staff book. This action is irreversible and the person to be deleted cannot be retrieved afterwards.
</div>

Deletes the specified person from the contacts.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the contacts.
* `sort p/` followed by `delete 1` deletes the 1st person in the contacts in the results of the `sort` command, which should be the person with the smallest phone number.
* `find Bernice Yu` followed by `delete 1` deletes the 1st person in the results of the `find` command.
  <br>**Before deletion:**<br>
  ![All persons listed](images/BeforeDeleteCommand1.png)
  <br>**First find Bernice:**<br>
  ![Result of finding Bernice](images/BeforeDeleteCommand2.png)
  <br>**Then delete Bernice:**<br>
  ![Result of deletion](images/AfterDeleteCommand1.png)
  <br>**List all persons and Bernice is deleted:**<br>
  ![Result of deletion](images/AfterDeleteCommand2.png)
### Setting a person as favourite: `fav`

Sets the specified person from the contacts as favourite.

Format: `fav INDEX`

* Sets the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `fav 2` sets the 2nd person as favourite in the contacts.
* `sort p/` followed by `fav 1` sets the 1st person as favourite in the contacts in the results of the `sort` command, which should be the person with the smallest phone number.
* `find Betsy` followed by `fav 1` sets the 1st person as favourite in the results of the `find` command.
![Result of fav command](images/AfterFavCommand.png)

### Removes a person as favourite: `unfav`

Removes the specified person from the contacts as favourite.

Format: `unfav INDEX`

* Removes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `unfav 2` removes the 2nd person as favourite in the contacts.
* `sort p/` followed by `fav 1` removes the 1st person as favourite in the contacts in the results of the `sort` command, which should be the person with the smallest phone number.
* `find Betsy` followed by `unfav 1` removes the 1st person as favourite in the results of the `find` command.

### Refresh and clear all outdated meetings: `refresh`

<div markdown="block" class="alert alert-danger">:warning: **Caution:**
This may result in possible data loss. e.g. Meetings of a person may be deleted.
There will be no further prompt after entering the command to refresh meetings of all persons. This action is irreversible and the meeting information that may be deleted cannot be retrieved afterwards.
</div>

Deletes all meetings that start before the very moment the user types in the command and enters.

Format: `refresh`

* The deleted meetings will be explicitly printed again to the use, specifying content of the meeting and who "owns" the meeting.
* If no meetings are deleted, there will no error thrown. Instead, a prompt will be given to user that no meeting is deleted.

Examples:
* If there is a meeting `Avengers Assemble` that happened in `31/08/1939 12:00`, when the user types in `refresh`, it will be deleted.
* If there is a meeting `French Revolution` that happened in `14/07/1789 12:00`, when the user types in `refresh`, it will be deleted.
* If there is a meeting `Future Meeting` that will happen in `31/12/2999 12:00`, when the user types in `refresh`, it will not be deleted.
  <br>**Before:**<br>
  ![Before refreshing](images/BeforeRefreshCommand.png)
  <br>
  ![After refreshing](images/AfterRefreshCommand.png)
  <br>**After:**<br>

**Known limitations:**
Refresh is only used when the user decides to remove clutter in the staff book, and wants to remove outdated meetings. 
This process is not done automatically as sometimes the user would like to retain old meetings for bookkeeping purposes.

### Selecting the detailed contact to display: `select`

Selects the person identified by the index number used in the displayed person list for display.

Format: `select INDEX`

* Select the person and loads its contact with meeting details for display at the specified **INDEX**
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `select 2`. <br> Selects the 2nd person in the staff book.
* `filter t/tutor` followed by `select 1` <br> Selects the 1st person in the results of the `filter` command.
* `sort p/` followed by `select 1` <br> Selects the 1st person in the results of the `sort` command, which should be the person with the smallest phone number.

### Clearing all entries : `clear`

<div markdown="block" class="alert alert-danger">:warning: **Caution:**
There will be no further prompt after entering the command to clear all persons from the staff book. This action is irreversible and the staff book cannot be retrieved afterwards.
</div>

Clears all entries from the staff book.

Format: `clear`

![Effects of clear command](images/AfterClearCommand.png)

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

StaffConnect data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

StaffConnect data are saved automatically as a JSON file `[JAR file location]/data/staffconnect.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, StaffConnect will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the StaffConnect to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous StaffConnect home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.

--------------------------------------------------------------------------------------------------------------------

## Attribute summary

Attribute | Prefix | Restrictions | Examples
----------|--------|--------------|---------------------
Name[^1] | n/ | Case-sensitive.<br>Only alphanumeric characters allowed. | `alex yeoh`, `Bernice Yu`, `test1`
Phone Number[^1] | p/ | Numeric digits only, no special characters, at least 3 digits long. | `123`, `88888888, 12345678`
Email[^1] | e/ | Valid email of the format `local-part@domain`.<br>1. `local-part` should only contain alphanumeric characters and the special characters `+_.-`<br>2. `local-part` may not start or end with any special characters.<br>3. `local-part` must be followed by exactly one `@` and then a `domain` name.<br>4. `domain` must be made up of at least 2 `domain` labels separated by periods.<br>5. Each `domain` name must be at least 2 alphanumeric characters long.<br>6. Each `domain` name must start and end with alphanumeric characters.<br>7. Each `domain` name can only consist of alphanumeric characters, separated by hyphens, if any. | `e@123.com`, `hello@h-h.com`, `one+two@h-h.hh`, `hello@e-h.e-hh`
Module[^1] | m/ | Case-insensitive.<br>Valid module consisting of 2-4 letters, followed by exactly 4 numeric digits, with a suffix that is at most 2 characters long. | `gess1025`, `hsi1000`, `cs2103t`
Faculty[^1] | f/ | Case-insensitive.<br>Restricted set of values (see table `Valid faculty values` below). | `soc`, `biz`, `School of Business`
Venue[^1] | v/ | Only alphanumeric characters allowed. | `belobog avenue`, `COM4-02-33`, `LT21`, `Kent Ridge Vale, Tulip Street, #12-34`
Tag | t/ | Only alphanumeric characters allowed.<br>Person can have any number of tags. | `tutor`, `professor`, `BestProf`, `Number1TA`
Availability | a/ | Valid format of `day start-time end-time`.<br>Person can have any number of availabilities.<br>1. `day` should be a valid day of week: `Monday`, `mon`, `Tuesday`, `tues`, `Wednesday`, `wed`, `Thursday`, `thurs`, `Friday`, `fri`, `Saturday`, `sat`, `Sunday`, `sun`.<br>2. `day` is case-insensitive.<br>3. `start-time` and `end-time` should be in the time format of `HH:mm` where `HH` is in 24 hours and `mm` are minutes 00-60. | `mon 13:00 14:00`, `monday 13:00 14:00`, `tues 14:00 21:00`
Meeting Description | d/ | Only alphanumeric characters allowed. | `Meet for finals`, `Midterm revision`
Meeting Start Time | s/ | Valid date and time format.<br>1. Valid date formats: `yyyy-MM-dd`, `yyyy-M-d`, `dd-MM-yyyy`, `yyyy-MM-d`, `d-MM-yyyy`, `d-M-yyyy`, `dd-M-yyyy`, `d/MM/yyyy`, `d-M-yyyy`, `dd-M-yyyy`, `dd/MM/yyyy`, `yyyy/MM/dd`, `yyyy/MM/d`, `yyyy/M/dd`,  `yyyy/M/d`<br>2. Valid time formats: `HH:mm`, `H:mm`, `HHmm` | `2002-11-02 19:00`, `1-12-2022 9:00`, `2024/1/1 0000`

[^1]: Mandatory when adding a person into the staff book, as these are important information for students to know when/where to consult their professors/TAs.

Faculty | Other names
--------|------
Faculty of Arts of Social Sciences | Arts and Social Sciences, FASS
Business School | Business, Biz School, Biz
School of Computing | Computing, SoC
School of Continuing and Lifelong Education | Continuing and Lifelong Education, SCALE
Faculty of Dentistry | Dentistry
School of Design and Environment | Design and Environment, SDE
Duke-NUS Medical School | Duke-NUS
Faculty of Engineering, Engineering | FoE
Integrative Sciences and Engineering | ISEP
Faculty of Law | Law
Yong Loo Lin School of Medicine | Medicine
Yong Siew Toh Conservatory of Music | Music, TST Conservatory of Music
Saw Swee Hock School of Public Health | Public Health
Lee Kuan Yew School of Public Policy | Public Policy, LKY School of Public Policy
Faculty of Science | Science, FoS
University Scholars Programme | USP
Yale-NUS College | Yale-NUS

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE_NUMBER e/EMAIL m/MODULE f/FACULTY v/VENUE [t/TAG]…​ [a/AVAILABILITY]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com m/CS2103 f/Computing v/123, Clementi Rd, 1234665 t/friend t/colleague a/monday 14:00 16:00`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [m/MODULE] [f/FACULTY] [v/VENUE] [t/TAG]…​ [a/AVAILABILITY]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Filter** | `filter [m/MODULE] [f/FACULTY] [t/TAG]… [a/AVAILABILITY]…`<br> e.g., `filter m/CS2100 t/friends`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**Sort** | `sort [n/] [p/] [m/] [f/] [v/] [s/] [meet/]...`<br> e.g., `sort n/ p/ m/`
**Add Meeting** | `meeting-add INDEX d/DESCRIPTION s/DATETIME`<br> e.g., `meeting-add 1 d/Meet for finals preparation s/12/04/2024 18:00`
**Delete Meeting** | `meeting-delete INDEX i/MEETING-INDEX`<br> e.g., `meeting-delete 1 i/1 `
**Set as Favourite** | `fav INDEX`<br> e.g., `fav 3`
**Remove as Favourite** | `unfav INDEX`<br> e.g., `unfav 3`
**Refresh** | `refresh` <br> e.g., `refresh`
**Select** | `select INDEX`<br> e.g., `select 3`
**List** | `list`
**Help** | `help`
