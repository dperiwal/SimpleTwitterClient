SimpleTwitterClient
===================
By Damodar Periwal

<b>Simple Twitter Client With Fragments – Part 2</b>

<b>The following user stories have been completed:</b>

- Includes all required user stories from Week 3 Twitter Client
- User can switch between Timeline and Mention views using tabs. 
    - User can view their home timeline tweets.
    - User can view the recent mentions of their username.
    - User can scroll to bottom of either of these lists and new tweets will load ("infinite scroll")
- User can navigate to view their own profile 
    - User can see picture, tagline, # of followers, # of following, and tweets on their profile.
- User can click on the profile image in any tweet to see another user's profile. 
    - User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
    - Profile view should include that user's timeline

<b>The following advanced user stories are implemented:</b>
- Advanced: Robust error handling, check if internet is available, handle error cases, network failures
- Advanced:  Uses JDX ORM for persistence in local SQLite database. JDX ORM is a product of Software Tree and is currently under beta. The information about older tweets is automatically deleted from time-to-time when a threshold number of objects are detected.
- Advanced: When a network request is sent, user sees an indeterminate progress indicator
- Advanced: User can "reply" to any tweet on their home timeline 
    - The user that wrote the original tweet is automatically "@" replied in compose
- Advanced: User can click on a tweet to be taken to a "detail view" of that tweet 
    - Advanced: User can take reweet actions on a tweet
- Advanced: Improve the user interface and theme the app to feel twitter branded


<b>Third-party libraries:</b>
The following third party libraries are used in this project under their respective licenses:

     
     android-async-http-1.4.3.jar
     android-support_v4.jar
     codepath-oauth-0.3.0.jar
     codepath-utils.jar
     JDXAndroid.jar
     scribe-codepath.jar
     sqldroid.jar
     universal-image-loader-1.8.4.jar
     
     
     

Walkthrough of all user stories:

![Animated Walkthrough](DamodarSimpleTwitterClient2.gif "Animation that shows the working of the app in an emulator")

GIF created with [LiceCap](http://www.cockos.com/licecap/).




<b>The following information is for Part 1 submission.</b>

<b>The following user stories have been completed:</b>

- User can sign in to Twitter using OAuth login
- User can view the tweets from their home timeline 
    - User should be displayed the username, name, and body for each tweet
    - User should be displayed the relative timestamp for each tweet "8m", "7h"
    - User can view more tweets as they scroll with infinite pagination
    - Optional: Links in tweets are clickable and will launch the web browser (see autolink)
-	 User can compose a new tweet 
    - 	User can click a “Compose” icon in the Action Bar on the top right
    - 	User can then enter a new tweet and post this to twitter
    - 	User is taken back to home timeline with new tweet visible in timeline
    - 	Optional: User can see a counter with total number of characters left for tweet

<b>The following advanced optional user stories have also been completed:</b>
- <b>Advanced:</b> User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh)
-	<b>Advanced:</b> User can open the twitter app offline and see last loaded tweets 
    - Tweets are persisted into sqlite and can be displayed from the local DB
    - This has been implemented but there are some bugs in using ActiveAndroid. Some records seem to be missing. Could not get time to investigate further.
- <b>Advanced:</b> User can tap a tweet to display a "detailed" view of that tweet
-	<b>Advanced:</b> User can select "reply" from detail view to respond to a tweet
-	<b>Advanced:</b> Improve the user interface and theme the app to feel "twitter branded"
- <b>Bonus:</b> User can see embedded image media within the tweet detail view


<b>Third-party libraries:</b>
The following third party libraries are used in this project under their respective licenses:

     ActiveAndroid-3.1-adcdda4c58.jar
     android-async-http-1.4.6.jar
     codepath-oauth-0.3.0.jar
     codepath-utils.jar
     scribe-codepath.jar
     universal-image-loader-1.8.4.jar
     
     
     

Walkthrough of all user stories:

![Animated Walkthrough](DamodarSimpleTwitterClient.gif "Animation that shows the working of the app in an emulator")

GIF created with [LiceCap](http://www.cockos.com/licecap/).
