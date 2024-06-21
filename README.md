# family_cookbook_app
CS4520 Final Project (Ousman Jobe & Axel Boly)

Overview:

Our Remote Family Cookbook App is designed to allow multiple users to maintain a list of family recipes. Users can add new recipes with descriptions, ingredients, instructions, and photos. The app supports user authentication via Firebase and uses the Room API for local data persistence. The app also supports light and dark themes.

How to Run:

- Clone the repository and open the project in Android Studio.
- Configure Firebase with your project credentials.
- Sync the project with Gradle files.
- Run the app on an emulator or physical device.
- Sign it with username: SmithFamily, password: test1234

Features:

User Authentication: Users can log in or register using Firebase Authentication.
Recipe Management: Add new recipes with ingredients, instructions, and photos; Edit and delete existing recipes
Favorites: Mark recipes as favorites for quick access



#Activities:

1. (Main Activity) User Authentication Activity

Login or register using Firebase Authentication.
Redirects to the Homepage on successful authentication.

2. Homepage Activity

Displays a RecyclerView of favorite recipes with the recipe's name and duration.
"All Recipes" button to navigate to the Recipe List Screen.
"Profile" button to navigate to the Profile Screen.

3. Recipe List Activity

Allows adding new recipes through an alert dialog.
Displays all added recipes in a RecyclerView.
"Home" button to navigate back to the Homepage.
"Profile" button to navigate to the Profile Screen.

4. Recipe Detail Activity

Displays detailed information of the selected recipe.
Option to take and upload a photo of the recipe.
"Favorite" button to add/remove the recipe from favorites.
"Remove" button to delete the recipe.
"Return" button to navigate back to the Recipe List Screen.

5. Profile Activity

Displays the username.
"Preferences" button to navigate to the Preferences Screen.
"Logout" button to log out the user.
"Delete Account" button to delete the user's account after confirmation.
"Home" button to navigate to the Homepage.
"All Recipes" button to navigate to the Recipe List Screen.

6. Preferences Activity

Allows toggling between light and dark themes.
"Return" button to navigate back to the Profile Screen.
