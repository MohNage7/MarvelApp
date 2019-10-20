# MarvelApp

Marvel application that is built with MVVM , Dagger2 , LiveData and Room.
Using this application, users will be able to browse through the Marvel library of characters.

## Preview 

<img src="https://github.com/MohNage7/MarvelApp/blob/master/screenshots/device-2019-10-20-104946.png"  width="241" height="500" /> <img src="https://github.com/MohNage7/MarvelApp/blob/master/screenshots/device-2019-10-20-105043.png"   width="241" height="500" />
<img src="https://github.com/MohNage7/MarvelApp/blob/master/screenshots/device-2019-10-20-105111.png"  width="241" height="500" /><img src="https://github.com/MohNage7/MarvelApp/blob/master/screenshots/device-2019-10-20-105126.png"  width="241" height="500" />
<img src="https://github.com/MohNage7/MarvelApp/blob/master/screenshots/device-2019-10-20-105211.png"  width="241" height="500" /><img src="https://github.com/MohNage7/MarvelApp/blob/master/screenshots/device-2019-10-20-105250.png"  width="241" height="500" />


### Project Overview
In this project we will consume Marvel APIs to build a simple app that have the following features

* Fetch Characters endlessly
* Show every character details in separate screen
* Cache data offline. 
* Search for characters.

### App architecture 
The following diagram shows how all the modules will interact with one another.

<img src="https://github.com/MohNage7/SimpleMovies/blob/master/images/simple_movies_diagram.png"  width=600 height=524  />

Each component depends only on the component one level below it. For example, activities and fragments depend only on a view model. 
The repository is the only class that depends on multiple other classes; on a persistent data model and a remote backend data source.
