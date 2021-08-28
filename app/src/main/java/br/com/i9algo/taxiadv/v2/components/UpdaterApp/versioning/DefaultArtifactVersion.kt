package br.com.i9algo.taxiadv.v2.components.UpdaterApp.versioning

import java.util.*
import java.util.regex.Pattern

/**
 * Default implementation of artifact versioning.
 *
 * @author [Brett Porter](mailto:brett@apache.org)
 */
class DefaultArtifactVersion(version: String) : ArtifactVersion {

    override var majorVersion: Int? = 0

    override var minorVersion: Int? = null

    override var incrementalVersion: Int? = null

    override var buildNumber: Int? = null

    override var qualifier: String? = null

    private var comparable: ComparableVersion? = null

    init {
        parseVersion(version)
    }

    override fun hashCode(): Int {
        return 11 + comparable!!.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        return if (other !is ArtifactVersion) {
            false
        } else compareTo((other as ArtifactVersion?)!!) == 0

    }

    override fun compareTo(other: ArtifactVersion): Int {
        return if (other is DefaultArtifactVersion) {
            this.comparable!!.compareTo(other.comparable!!)
        } else {
            compareTo(DefaultArtifactVersion(other.toString()))
        }
    }

    fun getMajorVersion(): Int {
        return if (majorVersion != null) majorVersion!! else 0
    }

    fun getMinorVersion(): Int {
        return if (minorVersion != null) minorVersion!! else 0
    }

    fun getIncrementalVersion(): Int {
        return if (incrementalVersion != null) incrementalVersion!! else 0
    }

    fun getBuildNumber(): Int {
        return if (buildNumber != null) buildNumber!! else 0
    }

    override fun parseVersion(version: String) {
        comparable = ComparableVersion(version)

        val index = version.indexOf("-")

        val part1: String
        var part2: String? = null

        if (index < 0) {
            part1 = version
        } else {
            part1 = version.substring(0, index)
            part2 = version.substring(index + 1)
        }

        if (part2 != null) {
            try {
                if (part2.length == 1 || !part2.startsWith("0")) {
                    buildNumber = Integer.valueOf(part2)
                } else {
                    qualifier = part2
                }
            } catch (e: NumberFormatException) {
                qualifier = part2
            }

        }

        if (!part1.contains(".") && !part1.startsWith("0")) {
            try {
                majorVersion = Integer.valueOf(part1)
            } catch (e: NumberFormatException) {
                // qualifier is the whole version, including "-"
                qualifier = version
                buildNumber = null
            }

        } else {
            var fallback = false

            val tok = StringTokenizer(part1, ".")
            try {
                majorVersion = getNextIntegerToken(tok)
                if (tok.hasMoreTokens()) {
                    minorVersion = getNextIntegerToken(tok)
                }
                if (tok.hasMoreTokens()) {
                    incrementalVersion = getNextIntegerToken(tok)
                }
                if (tok.hasMoreTokens()) {
                    qualifier = tok.nextToken()
                    fallback = Pattern.compile("\\d+").matcher(qualifier!!).matches()
                }

                // string tokenzier won't detect these and ignores them
                if (part1.contains("..") || part1.startsWith(".") || part1.endsWith(".")) {
                    fallback = true
                }
            } catch (e: NumberFormatException) {
                fallback = true
            }

            if (fallback) {
                // qualifier is the whole version, including "-"
                qualifier = version
                majorVersion = null
                minorVersion = null
                incrementalVersion = null
                buildNumber = null
            }
        }
    }

    private fun getNextIntegerToken(tok: StringTokenizer): Int {
        try {
            val s = tok.nextToken()
            if (s.length > 1 && s.startsWith("0")) {
                throw NumberFormatException("Number part has a leading 0: '$s'")
            }
            return Integer.valueOf(s)
        } catch (e: NoSuchElementException) {
            throw NumberFormatException("Number is invalid")
        }

    }

    override fun toString(): String {
        return comparable!!.toString()
    }
}