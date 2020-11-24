package com.example.groupfinder.util

import android.content.Context
import com.example.groupfinder.group.GroupFragment
import com.example.groupfinder.userprofile.UserProfileFragment
import com.example.groupfinder.group.GroupFinderFragment

/**
 * [PreferenceProvider] er en klasse som lagrer data om brukeren som er innlogget, og "Clear" data funksjonen fjerner det.
 *  Blir brukt for queries igjennom applikasjonen, og når man klikker logg ut.
 *
 * @author Anders Olai Pedersen - 225280
 */
class PreferenceProvider(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("myPref",0)

    /**
     * Epost, blir brukt i queries.
     */
    fun putEmailPreference(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getEmailPreference(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    /**
     * StudentId, blir brukt i queries.
     */
    fun putIdPreference(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getIdPreference(key: String): Int? {
        return sharedPreferences.getInt(key, 0)
    }

    /**
     *  Synlighet (boolean) for knapp i [GroupFragment], setter synlighet basert på hvilket fragment som åpner den.
     *  (Hvis man åpner gruppen under [UserProfileFragment] skal knappene sjules (pga. man er medlem av den),
     *  hvis søker etter gruppe i [GroupFinderFragment]skal disse vises).
     *
     *  Dette er kanskje ikke den mest optimale måten å håndtere dette problemet på..
      */
    fun putShowButton(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getShowButton(key: String): Boolean? {
        return sharedPreferences.getBoolean(key, false)
    }

    // Clear shared preference
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

}