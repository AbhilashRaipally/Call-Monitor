# Call-Monitor
App that can listen to incoming and outgoing calls and record the numbers

https://android-developers.googleblog.com/2013/05/handling-phone-call-requests-right-way.html

Android 10 changes how call intents are handled. The NEW_OUTGOING_CALL broadcast is deprecated and is replaced with the CallRedirectionService API. The CallRedirectionService API provides interfaces for you to modify outgoing calls made by the Android platform.
Apps that perform call screening should use the CallScreeningService API.

This app uses the new [`RoleManager`][role-manager] in Android Q to become the system [`CallScreeningService`][call-screening-service]. 
This service type has been available since Android 7.0 (Nougat), but was only usable if the app was the current default dialer app.

https://zoransasko.medium.com/detecting-and-rejecting-incoming-phone-calls-on-android-9e0cff04ef20

[role-manager]: https://developer.android.com/reference/android/app/role/RoleManager
[call-screening-service]: https://developer.android.com/reference/android/telecom/CallScreeningService

