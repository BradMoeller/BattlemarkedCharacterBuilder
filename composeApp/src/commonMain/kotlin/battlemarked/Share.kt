package battlemarked

/** Browser/platform hooks used for sharing; implemented per target. */
expect fun readLocationHash(): String
expect fun replaceLocationHash(hash: String)
expect fun currentUrl(): String
expect fun copyToClipboard(text: String)

enum class View { Builder, Overview }

/** Everything that goes into a shared link. */
data class AppState(
    val heroClass: HeroClass,
    val secondary: Attribute,
    val build: Build,
    val view: View = View.Builder,
    /** Class or attribute name shown in the overview. */
    val overview: String = heroClass.name,
)

private val Kinds = ColumnKind.entries

fun AppState.toHash(): String {
    val picks = buildString {
        for (kind in Kinds) for (tier in 0..2) append(build.pick(kind, tier)?.toString() ?: "-")
    }
    return "c=${heroClass.name}&s=${secondary.name}&b=$picks&v=${view.name}&o=$overview"
}

fun parseAppState(hash: String): AppState? {
    val params = hash.removePrefix("#").split('&').mapNotNull {
        val i = it.indexOf('=')
        if (i < 0) null else it.substring(0, i) to it.substring(i + 1)
    }.toMap()
    val heroClass = AvailableClasses.firstOrNull { it.name == params["c"] } ?: return null
    val secondary = Attribute.entries.firstOrNull { it.name == params["s"] }
        ?.takeIf { it != heroClass.primary } ?: heroClass.secondary
    val columns = heroClass.columns(secondary)
    val raw = params["b"].orEmpty()
    val picks = buildMap {
        Kinds.forEachIndexed { k, kind ->
            for (tier in 0..2) {
                val option = raw.getOrNull(k * 3 + tier)?.digitToIntOrNull() ?: continue
                if (option < columns[k].slots[tier].options.size) put(kind to tier, option)
            }
        }
    }
    val view = View.entries.firstOrNull { it.name == params["v"] } ?: View.Builder
    val overview = params["o"]?.takeIf { o ->
        AvailableClasses.any { it.name == o } || Attribute.entries.any { it.name == o }
    } ?: heroClass.name
    return AppState(heroClass, secondary, Build(picks), view, overview)
}
