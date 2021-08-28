package br.com.i9algo.taxiadv.v2.components.UpdaterApp.versioning

import java.math.BigInteger
import java.util.*

/**
 * Generic implementation of version comparison.
 *
 *
 *
 * Features:
 *
 *  * mixing of '`-`' (dash) and '`.`' (dot) separators,
 *  * transition between characters and digits also constitutes a separator:
 * `1.0alpha1 => [1, 0, alpha, 1]`
 *  * unlimited number of version components,
 *  * version components in the text can be digits or strings,
 *  * strings are checked for well-known qualifiers and the qualifier ordering is used for version ordering.
 * Well-known qualifiers (case insensitive) are:
 *  * `alpha` or `a`
 *  * `beta` or `b`
 *  * `milestone` or `m`
 *  * `rc` or `cr`
 *  * `snapshot`
 *  * `(the empty string)` or `ga` or `final`
 *  * `sp`
 *
 * Unknown qualifiers are considered after known qualifiers, with lexical order (always case insensitive),
 *
 *  * a dash usually precedes a qualifier, and is always less important than something preceded with a dot.
 *
 *
 * @author [Kenney Westerhof](mailto:kenney@apache.org)
 * @author [Hervé Boutemy](mailto:hboutemy@apache.org)
 * @see ["Versioning" on Maven Wiki](https://cwiki.apache.org/confluence/display/MAVENOLD/Versioning)
 */
internal class ComparableVersion(version: String) : Comparable<ComparableVersion> {
    private var value: String = ""

    private var canonical: String = ""

    private var items: ListItem? = null

    private interface Item {

        val type: Int

        val isNull: Boolean

        operator fun compareTo(item: Item?): Int

        companion object {
            val INTEGER_ITEM = 0
            val STRING_ITEM = 1
            val LIST_ITEM = 2
        }
    }

    /**
     * Represents a numeric item in the version item list.
     */
    private class IntegerItem : Item {

        private val value: BigInteger

        override val type: Int
            get() = ComparableVersion.Item.INTEGER_ITEM

        override val isNull: Boolean
            get() = BIG_INTEGER_ZERO == value

        private constructor() {
            this.value = BIG_INTEGER_ZERO
        }

        constructor(str: String) {
            this.value = BigInteger(str)
        }

        override fun compareTo(item: Item?): Int {
            if (item == null) {
                return if (BIG_INTEGER_ZERO == value) 0 else 1 // 1.0 == 1, 1.1 > 1
            }

            when (item.type) {
                ComparableVersion.Item.INTEGER_ITEM -> return value.compareTo((item as IntegerItem).value)

                ComparableVersion.Item.STRING_ITEM -> return 1 // 1.1 > 1-sp

                ComparableVersion.Item.LIST_ITEM -> return 1 // 1.1 > 1-1

                else -> throw RuntimeException("invalid item: " + item.javaClass)
            }
        }

        override fun toString(): String {
            return value.toString()
        }

        companion object {
            private val BIG_INTEGER_ZERO = BigInteger("0")

            val ZERO = IntegerItem()
        }
    }

    /**
     * Represents a string in the version item list, usually a qualifier.
     */
    private class StringItem(value: String, followedByDigit: Boolean) : Item {

        private val value: String

        override val type: Int
            get() = ComparableVersion.Item.STRING_ITEM

        override val isNull: Boolean
            get() = comparableQualifier(value).compareTo(RELEASE_VERSION_INDEX) == 0

        init {
            var value = value
            if (followedByDigit && value.length == 1) {
                // a1 = alpha-1, b1 = beta-1, m1 = milestone-1
                when (value[0]) {
                    'a' -> value = "alpha"
                    'b' -> value = "beta"
                    'm' -> value = "milestone"
                }
            }
            this.value = ALIASES.getProperty(value, value)
        }

        override fun compareTo(item: Item?): Int {
            if (item == null) {
                // 1-rc < 1, 1-ga > 1
                return comparableQualifier(value).compareTo(RELEASE_VERSION_INDEX)
            }
            when (item.type) {
                ComparableVersion.Item.INTEGER_ITEM -> return -1 // 1.any < 1.1 ?

                ComparableVersion.Item.STRING_ITEM -> return comparableQualifier(value).compareTo(comparableQualifier((item as StringItem).value))

                ComparableVersion.Item.LIST_ITEM -> return -1 // 1.any < 1-1

                else -> throw RuntimeException("invalid item: " + item.javaClass)
            }
        }

        override fun toString(): String {
            return value
        }

        companion object {
            private val QUALIFIERS = arrayOf("alpha", "beta", "milestone", "rc", "snapshot", "", "sp")

            private val _QUALIFIERS = Arrays.asList(*QUALIFIERS)

            private val ALIASES = Properties()

            init {
                ALIASES["ga"] = ""
                ALIASES["final"] = ""
                ALIASES["cr"] = "rc"
            }

            /**
             * A comparable value for the empty-string qualifier. This one is used to determine if a given qualifier makes
             * the version older than one without a qualifier, or more recent.
             */
            private val RELEASE_VERSION_INDEX = _QUALIFIERS.indexOf("").toString()

            /**
             * Returns a comparable value for a qualifier.
             *
             *
             * This method takes into account the ordering of known qualifiers then unknown qualifiers with lexical
             * ordering.
             *
             *
             * just returning an Integer with the index here is faster, but requires a lot of if/then/else to check for -1
             * or QUALIFIERS.size and then resort to lexical ordering. Most comparisons are decided by the first character,
             * so this is still fast. If more characters are needed then it requires a lexical sort anyway.
             *
             * @param qualifier
             * @return an equivalent value that can be used with lexical comparison
             */
            fun comparableQualifier(qualifier: String): String {
                val i = _QUALIFIERS.indexOf(qualifier)

                return if (i == -1) _QUALIFIERS.size.toString() + "-" + qualifier else i.toString()
            }
        }
    }

    /**
     * Represents a version list item. This class is used both for the global item list and for sub-lists (which start
     * with '-(number)' in the version specification).
     */
    private class ListItem : ArrayList<Item>(), Item {
        override val type: Int
            get() = ComparableVersion.Item.LIST_ITEM

        override val isNull: Boolean
            get() = size == 0

        internal fun normalize() {
            val iterator = listIterator(size)
            while (iterator.hasPrevious()) {
                val item = iterator.previous()
                if (item.isNull) {
                    iterator.remove() // remove null trailing items: 0, "", empty list
                } else {
                    break
                }
            }
        }

        override fun compareTo(item: Item?): Int {
            if (item == null) {
                if (size == 0) {
                    return 0 // 1-0 = 1- (normalize) = 1
                }
                val first = get(0)
                return first.compareTo(null)
            }
            when (item.type) {
                ComparableVersion.Item.INTEGER_ITEM -> return -1 // 1-1 < 1.0.x

                ComparableVersion.Item.STRING_ITEM -> return 1 // 1-1 > 1-sp

                ComparableVersion.Item.LIST_ITEM -> {
                    val left = iterator()
                    val right = (item as ListItem).iterator()

                    while (left.hasNext() || right.hasNext()) {
                        val l = if (left.hasNext()) left.next() else null
                        val r = if (right.hasNext()) right.next() else null

                        // if this is shorter, then invert the compare and mul with -1
                        val result = l?.compareTo(r) ?: if (r == null) 0 else -1 * r.compareTo(l)

                        if (result != 0) {
                            return result
                        }
                    }

                    return 0
                }

                else -> throw RuntimeException("invalid item: " + item.javaClass)
            }
        }

        override fun toString(): String {
            val buffer = StringBuilder("(")
            val iter = iterator()
            while (iter.hasNext()) {
                buffer.append(iter.next())
                if (iter.hasNext()) {
                    buffer.append(',')
                }
            }
            buffer.append(')')
            return buffer.toString()
        }
    }

    init {
        parseVersion(version)
    }

    fun parseVersion(version: String) {
        var v = version
        this.value = version

        items = ListItem()

        v = v.toLowerCase(Locale.ENGLISH)

        var list: ListItem = items as ListItem

        val stack = Stack<Item>()
        stack.push(list)

        var isDigit = false

        var startIndex = 0

        for (i in 0 until v.length) {
            val c = v[i]

            if (c == '.') {
                if (i == startIndex) {
                    list.add(IntegerItem.ZERO)
                } else {
                    list.add(parseItem(isDigit, v.substring(startIndex, i)))
                }
                startIndex = i + 1
            } else if (c == '-') {
                if (i == startIndex) {
                    list.add(IntegerItem.ZERO)
                } else {
                    list.add(parseItem(isDigit, v.substring(startIndex, i)))
                }
                startIndex = i + 1

                if (isDigit) {
                    list.normalize() // 1.0-* = 1-*

                    if (i + 1 < v.length && Character.isDigit(v[i + 1])) {
                        // new ListItem only if previous were digits and new char is a digit,
                        // ie need to differentiate only 1.1 from 1-1
                        list = ListItem()
                        list.add(list)

                        stack.push(list)
                    }
                }
            } else if (Character.isDigit(c)) {
                if (!isDigit && i > startIndex) {
                    list.add(StringItem(v.substring(startIndex, i), true))
                    startIndex = i
                }

                isDigit = true
            } else {
                if (isDigit && i > startIndex) {
                    list.add(parseItem(true, v.substring(startIndex, i)))
                    startIndex = i
                }

                isDigit = false
            }
        }

        if (v.length > startIndex) {
            list.add(parseItem(isDigit, v.substring(startIndex)))
        }

        while (!stack.isEmpty()) {
            list = stack.pop() as ListItem
            list.normalize()
        }

        canonical = items!!.toString()
    }

    private fun parseItem(isDigit: Boolean, buf: String): Item {
        return if (isDigit) IntegerItem(buf) else StringItem(buf, false)
    }

    override fun compareTo(o: ComparableVersion): Int {
        return items!!.compareTo(o.items)
    }

    override fun toString(): String {
        return value
    }

    override fun equals(o: Any?): Boolean {
        return o is ComparableVersion && canonical == o.canonical
    }

    override fun hashCode(): Int {
        return canonical!!.hashCode()
    }
}
