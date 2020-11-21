package com.example.groupfinder.util


/**
 * Sammenkobler navnet, hvis navnet er for langt returner bare fornavn.
 * @param s1 Forename
 * @param s2 Surname
 * @param maxLength Max length of the concatenated name
 *
 */
fun concatName(s1: String, s2: String, maxLength: Int) : String? {
    return if (s1.length + s2.length >= maxLength) s1 else "$s1 $s2"
}