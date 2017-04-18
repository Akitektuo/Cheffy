# Cheffy

**Installing app**

I recommend using Android Studio.

`Project from Version Control -> Git -> 
Enter the link: https://github.com/Akitektuo/Cheffy.git -> Clone`

Now you can see and edit the code. If you want to see the app you need an Android device.
After you have connected it via USB cable and enabled debugging, use `Shift + F10` command
to run the program and then select the target device.

If you do not see the connected device try checking the developer options on your phone.
If you do not see them, go to `Settings -> About (Phone) -> Software information (if exists) ->
 Tap 10 times on Build number (to be sure) -> Go back to Settings -> Developer options -> 
 Make sure it is On -> USB debugging`

**Using the application**

The first screen is a list, to populate it (you need to be connected to a network),
swipe down and if the server is on you will see all the recipes from server.
You can click one to see a detailed picture, how much it can take to prepare it, a list of
 all ingredients and quantities and the steps with detailed description. Long pressing an ingredient
 will show all the recipes that can be made with that ingredient.
 
**What is so special about this project?**

Using my previous knowledge, I used SQL for database management and personalised list items.
This project brought teamwork and the use of new technologies. I cooperated with Ioan Sirian
(Site: https://ioansiran.github.io/) to populate the local database with new information from
the server that he managed. For the list of recipes is used a RecyclerView for better performance
and a library for the drop down animation and one for the circular images.