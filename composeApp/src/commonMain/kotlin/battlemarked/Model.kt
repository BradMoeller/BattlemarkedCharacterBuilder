package battlemarked

enum class Attribute { Strength, Dexterity, Constitution, Intelligence, Wisdom, Charisma }

data class Trait(val name: String, val description: String)

/** A slot (one tier of one column) offers a choice between a few traits. */
data class Slot(val options: List<Trait>)

data class HeroClass(
    val name: String,
    val primary: Attribute,
    val secondary: Attribute,
    /** Tiers I..III, each with its choices. */
    val classSlots: List<Slot>,
    /** Hidden from the UI when false (e.g. classes whose data isn't available yet). */
    val available: Boolean = true,
)

enum class ColumnKind(val label: String) { Class("Class"), Primary("Primary"), Secondary("Secondary") }

/** One of the three trait columns of a hero: three tier slots. */
data class TraitColumn(val kind: ColumnKind, val title: String, val slots: List<Slot>)

/** [secondary] defaults to the class's own but the player may change it; the primary is fixed. */
fun HeroClass.columns(secondary: Attribute = this.secondary): List<TraitColumn> = listOf(
    TraitColumn(ColumnKind.Class, name, classSlots),
    TraitColumn(ColumnKind.Primary, primary.name, AttributeSlots.getValue(primary)),
    TraitColumn(ColumnKind.Secondary, secondary.name, AttributeSlots.getValue(secondary)),
)

/** Chosen option index per (column, tier). Missing = nothing picked. */
data class Build(val picks: Map<Pair<ColumnKind, Int>, Int> = emptyMap()) {
    fun pick(kind: ColumnKind, tier: Int): Int? = picks[kind to tier]

    /** Selecting the already-chosen option clears the slot. */
    /** Drops picks in one column, e.g. when its attribute changes. */
    fun clear(kind: ColumnKind) = Build(picks.filterKeys { it.first != kind })

    fun toggle(kind: ColumnKind, tier: Int, option: Int): Build {
        val key = kind to tier
        return Build(if (picks[key] == option) picks - key else picks + (key to option))
    }
}
