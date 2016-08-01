# Pre-work - *To Do? Or Not To Do?*

**To Do? Or Not To Do?** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item, and even marking an item as complete.

Submitted by: **Yoav Gray**

Time spent: **28** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improved style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Added support for completion due dates for todo items (and display within listview item)
* [x] Added support for selecting the priority of each todo item (and display in listview item)
* [x] Used a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [x] Tweaked the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [x] Added a 'Complete' check box for each item to mark item as completed
* [x] Set time and date for task is through a Dialog showing DatePicker and TimePicker
* [x] Added support for Spanish from the OptionsMenu and Like/Dislike button to send a feedback email
* [x] Added an AlertDialog before deleting task from list
* [x] Used text and color to follow Android Material Design guidelines
* [x] Added notifications and sound when task is due
* [x] Added the AndroidViewAnimations to create cool animations when changing priorities on todo item

## Video Walkthrough 

Here's a walkthrough of implemented user stories:<br>
<img src="http://i.imgur.com/SVgjwjN.gif" title="Video Walkthrough" alt="Video Walkthrough"/>

GIF created with [<a href="https://github.com/colinkeenan/silentcast">SilentCast</a>](https://github.com/colinkeenan/silentcast) on [Ubuntu 14.04].

## Notes

A big challenge I encountered while building the app is understanding how I should successfully 
create an interaction between the ListView's TextView and CheckBox. Also, I couldn't understand, 
why OnItemClickListener did not fire after clicking the ListView item once I added the checkbox, but I managed to work around it with a lot
of help from StackOverflow!

The application is now submitted and I hope you like it and the additional features. I spent a lot
of time on it and learned a lot while doing it, so thanks for pushing me!

Yoav Gray
