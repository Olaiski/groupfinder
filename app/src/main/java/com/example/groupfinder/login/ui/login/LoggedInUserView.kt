package com.example.groupfinder.login.ui.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val name: String, val email: String, val location: String
    //... other data fields that may be accessible to the UI
)