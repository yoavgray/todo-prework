# Pre-work - *To Do, Or Not To Do?*

**To Do, Or Not To Do?** is an android app that allows building a todo list and basic todo items management functionality including adding new items, 
editing and deleting an existing item, and even marking an item as complete.

Submitted by: **Yoav Gray**

Time spent: **8** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [ ] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [ ] Add support for completion due dates for todo items (and display within listview item)
* [ ] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [ ] Add support for selecting the priority of each todo item (and display in listview item)
* [ ] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [ ] Add a 'Complete' check box for each item to mark item as completed.

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/link/to/your/gif/file.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

A big challenge I encountered while building the app is understanding how I should successfully create
an interaction between the ListView's TextView and CheckBox. Also, I couldn't understand, why OnItemClickListener
did not fire after clicking the ListView item once I added the checkbox, but I managed to work around it with a lot
of help from StackOverflow!

I am submitting my work, but will continue working on extending the app while experiencing with more Android features!
