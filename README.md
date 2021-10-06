# android-test

Logic of app:- 
1. At the start of the application, an API call is made to get the list of dogs.
2. For Search, First it will go through the data that is shown to user to check if any name contains the keyword. 
3. If no result is found from step [2] it would call the search api with query param to get the list of items. 

At the start of the app internet should be connected in order to fetch the data, otherwise internet error message shown to user. This application supports both the orientation. The test case coverage for ViewModel/Business logic is over ~85%.

- [x] MVVM: Used MVVM with ViewModel and LiveData to maintain updated state of UI during configuration change. ViewModel has used UDF and ViewState to maintain a single point of communication rather than exposing multiple livedata objects.
- [x] Kotlin: Used Kotlin for business, view, and test cases 100%.
- [x] RxJava : To fetch the response from API.
- [x] Retrofit : Called API with query params.
- [x] Dagger : Dependency injector.
- [x] Mockito : Tested ViewModel since it is handling business logic to call api etc. The code coverage for ViewModel is 94% line coverage%.

Note: If I had more time I could have done the better job in UI. I could have made this application connection aware.

What things I will do before going to PROD:- 
1. More alerting and logging to see the user behaviour. 
2. A/B testing with alternative UI. Fallback UI if API is down or error cases. 
3. It depends if API provide pagination capability or not.
4. Use ROOM to store data as data might become big in PROD.
5. Add e2e test cases.   



Test Report 
https://github.com/Jaggrat-Singh/dog-app/blob/main/report.png

UI
https://github.com/Jaggrat-Singh/dog-app/blob/main/details.png 
