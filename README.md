# User List View
## Overview
This component represents a user list view in the application. It displays a grid containing information about users, such as their usernames, names, creation dates, and modification dates.

## Functionality
* Displays a grid of users retrieved from the database.
* Allows customization of displayed columns:
  * The "Created" column shows the creation date of each user in the user's local time zone.
  * The "Modified" column shows the modification date of each user in the user's local time zone.
  * The "Password" column is hidden for security reasons.
  * The "Locale" column is hidden as it's not relevant for display.
  * The "Groups" column is hidden as it's not relevant for display.
## Implementation Details
* Utilizes the Vaadin Flow framework for building web applications.
* Retrieves user data from the UserRepository using Spring Data JPA.
* Uses Hibernate to initialize lazy-loaded associations (in this case, the "groups" association) to avoid LazyInitializationExceptions.
* Formats the creation and modification dates of users using the user's local time zone obtained from the client's browser.
* Renders the creation and modification dates using a custom text renderer with a specified date format ("dd/MM/yyyy HH:mm").
## Usage
#### This component can be integrated into any Vaadin Flow-based application requiring a user list view. Simply inject the UserRepository dependency into the constructor and call the initUI() method to initialize the UI components.

      private final UserListImpl userList;

    public UserListView(UserListImpl userList) {
        this.userList = userList;

        add(userList.initUI());

    }