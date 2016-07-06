# Pre-work - *To Do, Or Not To Do?*

**To Do, Or Not To Do?** is an android app that allows building a todo list and basic todo items management functionality including adding new items, 
editing and deleting an existing item, and even marking an item as complete.

Submitted by: **Yoav Gray**

Time spent: **13** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo items (and display within listview item)
* [x] Add support for selecting the priority of each todo item (and display in listview item)
* [x] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items

The following **optional** features are not YET implemented:

* [ ] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [ ] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [x] Add a 'Complete' check box for each item to mark item as completed.
* [x] Set time and date for task is through a Dialog showing DatePicker and TimePicker

## Video Walkthrough 

Here's a walkthrough of implemented user stories:<br>
<img src="http://i.imgur.com/h8KISOl.gif" title="Video Walkthrough" alt="Video Walkthrough"/>

GIF created with [<a href="https://github.com/colinkeenan/silentcast">SilentCast</a>](https://github.com/colinkeenan/silentcast) on [Ubuntu 14.04].

## Notes

A big challenge I encountered while building the app is understanding how I should successfully create
an interaction between the ListView's TextView and CheckBox. Also, I couldn't understand, why OnItemClickListener
did not fire after clicking the ListView item once I added the checkbox, but I managed to work around it with a lot
of help from StackOverflow!

I am submitting my work, but will continue working on extending the app while experiencing with more Android features!
