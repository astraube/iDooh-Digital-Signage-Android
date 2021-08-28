package br.com.i9algo.taxiadv.v2.components.UpdaterApp.versioning

/**
 * Describes an artifact version in terms of its components, converts it to/from a string and
 * compares two versions.
 *
 * @author [Brett Porter](mailto:brett@apache.org)
 */
interface ArtifactVersion : Comparable<ArtifactVersion> {
    val majorVersion: Int?

    val minorVersion: Int?

    val incrementalVersion: Int?

    val buildNumber: Int?

    var qualifier: String?

    fun parseVersion(version: String)
}