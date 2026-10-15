package battlemarked

// TODO: replace placeholder text with the real in-game traits (send screenshots).

private val TierNumerals = listOf("I", "II", "III")

private fun placeholderSlots(name: String) = TierNumerals.map { tier ->
    Slot(List(3) { i -> Trait("$name $tier-${i + 1}", "TODO: description") })
}

/** Attribute skills are the same whether the attribute is primary or secondary. */
val AttributeSlots: Map<Attribute, List<Slot>> =
    Attribute.entries.associateWith { placeholderSlots(it.name) } + mapOf(
        Attribute.Strength to listOf(
            Slot(listOf(
                Trait("Martial Might", "Melee Attacks deal +1 physical damage."),
                Trait("Carry Capacity", "Grants +1 extra card slot."),
                Trait("Potent Brew", "Potion of Strength now grants +2 extra physical damage."),
            )),
            Slot(listOf(
                Trait("Knockback", "Melee Attacks push the target back by +1 tile."),
                Trait("Warborn Surge", "Melee Attacks now have 10% chance to apply Battle Cry to allies within range 2."),
                Trait("Throw", "Grants the Throw card."),
            )),
            Slot(listOf(
                Trait("Crushing Blow", "Gain 10% chance for Melee Attacks to stop enemies from moving for 2 turns."),
                Trait("Taunt", "Grants the Taunt card."),
                Trait("Ground Pound", "Grants the Ground Pound card."),
            )),
        ),
        Attribute.Constitution to listOf(
            Slot(listOf(
                Trait("Healthy", "Grants +3 extra hit points."),
                Trait("Resilient", "Take -1 damage from attacks while at 5 hit points or less."),
                Trait("Iron Gut", "Take 33% less damage from poison attacks."),
            )),
            Slot(listOf(
                Trait("Healthy", "Grants +3 extra hit points."),
                Trait("Thick Skinned", "Take -1 physical damage from melee attacks."),
                Trait("Fast Healer", "Potions of Healing now heal +4 hit points."),
            )),
            Slot(listOf(
                Trait("Juggernaut", "Heal 2 when killing an enemy."),
                Trait("Battle Hardened", "Take -1 physical damage from melee attacks. You are immune to counter attacks."),
                Trait("Endure", "Grants the Endure card."),
            )),
        ),
        Attribute.Wisdom to listOf(
            Slot(listOf(
                Trait("Eagle Eyed", "Grants +2 vision range."),
                Trait("Fortified Action", "One More Thing now grants +2 magic armor."),
                Trait("Unflinching Focus", "Take -1 spell damage while at, or above 80% hit points."),
            )),
            Slot(listOf(
                Trait("Combat Medic", "Reviving allies now heals them +5 hit points."),
                Trait("Combat Magic", "Reviving allies makes them deal +4 damage with Spell cards for 1 turn."),
                Trait("Prepared Spirit", "Grants +1 extra card slot."),
            )),
            Slot(listOf(
                Trait("Honed Instinct", "Melee Attacks now have 10% chance to hit 1 extra time."),
                Trait("Insightful Timing", "Grants 50% chance to take 50% physical damage."),
                // Shown with the same name and text as tier II's Combat Magic in the source screenshot.
                Trait("Combat Magic", "Reviving allies makes them deal +4 damage with Spell cards for 1 turn."),
            )),
        ),
        Attribute.Dexterity to listOf(
            Slot(listOf(
                Trait("Reposition", "Grants the Reposition card."),
                Trait("Dodge", "Grants 10% chance to take no damage from any attack."),
                Trait("Pocketed Trick", "Grants +1 extra card slot."),
            )),
            Slot(listOf(
                Trait("Pocketed Trick", "Grants +1 extra card slot."),
                Trait("Walk Speed", "Grants +1 move range."),
                Trait("Leap", "Grants the Leap card."),
            )),
            Slot(listOf(
                Trait("Disarm", "Grants the Disarm card."),
                Trait("Double Tap", "Melee Attacks now have 10% chance to hit 1 extra time."),
                Trait("Speedy Brew", "Potion of Speed now grants +2 extra move range."),
            )),
        ),
        Attribute.Intelligence to listOf(
            Slot(listOf(
                Trait("Magic Dart", "Grants the Magic Dart card."),
                Trait("Elemental Resistance", "Take -1 elemental damage."),
                Trait("Douse", "Grants the Douse card."),
            )),
            Slot(listOf(
                Trait("Alchemist's Inferno", "Alchemist's Fire now deals +4 fire damage."),
                Trait("Deflect Magic", "Grants +10% chance to deflect Spell Projectiles."),
                Trait("Ruinous Brew", "Potion of Magic now grants +2 extra spell damage."),
            )),
            Slot(listOf(
                Trait("Magic Expert", "Spell cards now have +5% chance to do +10 damage."),
                Trait("Deflect Magic", "Grants +10% chance to deflect Spell Projectiles."),
                Trait("Expert Spellcaster", "Deal +2 damage with Spell cards."),
            )),
        ),
        Attribute.Charisma to listOf(
            Slot(listOf(
                Trait("Magical Words", "Spell cards now deal +1 damage."),
                Trait("Intimidating Stare", "Enemies have +10% chance to miss you with Melee Attacks."),
                Trait("Refreshing Presence", "Antitoxin also applies to your nearest ally within 5 tiles."),
            )),
            Slot(listOf(
                Trait("Opportunity Banish", "Banish now applies Haste for 1 turn."),
                Trait("Charismatic Might", "Grants +1 extra card slot."),
                Trait("Unshakable Presence", "Grants 15% chance to deflect incoming projectiles."),
            )),
            Slot(listOf(
                Trait("Distracting Strikes", "Melee Attacks now have 20% chance to Weaken the target."),
                Trait("Inspiring Speech", "Melee Attacks now have 10% chance to apply Inspired to allies within range 2."),
                Trait("Heartening Brew", "Potions of Healing now heal you +3 hit points."),
            )),
        ),
    )


val HeroClasses: List<HeroClass> = listOf(
    HeroClass(
        name = "Bard",
        primary = Attribute.Charisma,
        secondary = Attribute.Dexterity,
        classSlots = listOf(
            Slot(listOf(
                Trait("Vicious Shout", "Vicious Mockery now has 30% chance to affect another enemy within 1 range."),
                Trait("Amplified Inspiration", "Bardic Inspiration now grants +1 damage and costs 0 AP."),
                Trait("Combat Medic", "Reviving allies now heals them +5 hit points."),
            )),
            Slot(listOf(
                Trait("Ruinous Shatter", "Shatter now deals +2 thunder damage."),
                Trait("Protective Inspiration", "Bardic Inspiration now grants +2 magic armor."),
                Trait("Inspired", "+20% chance to be granted Bardic Inspiration when taking damage."),
            )),
            Slot(listOf(
                Trait("Unrelenting Whispers", "Dissonant Whispers now deals +4 psychic damage."),
                Trait("Ballad of Protection", "Song of Rest now grants +5 magic armor."),
                Trait("Swifter Secrets", "Magical Secrets now grants you haste."),
            )),
        ),
    ),
    HeroClass(
        "Paladin", Attribute.Strength, Attribute.Constitution,
        classSlots = listOf(
            Slot(listOf(
                Trait("Oath of Vengeance", "Divine Smite now deals +1 radiant damage."),
                Trait("Oath of Devotion", "Grants 20% chance to take no damage from melee attacks."),
                Trait("Oath of the Ancients", "Lay on Hands now grants Haste."),
            )),
            Slot(listOf(
                Trait("Radiant Cascade", "Divine Smite now releases bouncing energy that deals 1 radiant damage to nearby foes."),
                Trait("True Bond", "Warding Bond now makes you and your target take only 40% damage when they are hit."),
                Trait("Hands of a Healer", "Lay on Hands now heals +4 hit points."),
            )),
            Slot(listOf(
                Trait("Guardian of Faith", "Grants the Guardian of Faith card."),
                Trait("Flame Strike", "Grants the Flame Strike card."),
                Trait("Sanctuary of Swiftness", "Sanctuary now heals 4 hit points and applies Haste."),
            )),
        ),
    ),
    HeroClass(
        "Rogue", Attribute.Dexterity, Attribute.Constitution,
        classSlots = listOf(
            Slot(listOf(
                Trait("Hellish Resistance", "Take -1 fire damage."),
                Trait("Surprise Attack", "Deal +2 physical damage while you have Stealth."),
                Trait("Poisoner's Kit", "Grants the Blade Poison card."),
            )),
            Slot(listOf(
                Trait("Dagger Shards", "Throw Dagger splinters into shards on hit, dealing 2 physical damage to 1 nearby enemy."),
                Trait("Retreat", "10% chance to be granted Stealth when you take damage."),
                Trait("Toxic Toss", "Gas Bomb now deals +2 poison damage and has +2 range."),
            )),
            Slot(listOf(
                Trait("Infernal Flame", "Melee attacks now have 10% chance to deal +2 fire damage."),
                Trait("Killer's Momentum", "Blink Strike now returns to your hand if it kills an enemy."),
                Trait("Toxic Opportunist", "Your attacks that deal poison damage now deal +2 poison damage."),
            )),
        ),
    ),
    HeroClass(
        "Sorcerer", Attribute.Charisma, Attribute.Intelligence,
        classSlots = listOf(
            Slot(listOf(
                Trait("Freezing Ray", "Ray of Frost now has 50% chance to inflict Frozen."),
                Trait("Blazing Bolt", "Fire Bolt now deals +2 fire damage."),
                Trait("Prismatic Well", "Receive Prismatic Spray more often."),
            )),
            Slot(listOf(
                Trait("Longer Step", "Misty Step now has +2 range."),
                Trait("Collateral Flames", "Fireball now deals +2 fire damage and creates more fire tiles."),
                Trait("Freezing Cone", "Cone of Cold now has +30% chance to freeze the target."),
            )),
            Slot(listOf(
                Trait("Longest Step", "Misty Step now has +4 range."),
                Trait("Flames of Chaos", "Fireball now pushes enemies 5 tiles in a random direction."),
                Trait("Master Sorcerer", "Sorcerer cards now have 70% chance to deal +2 damage."),
            )),
        ),
    ),
    HeroClass(
        "Ranger", Attribute.Dexterity, Attribute.Wisdom,
        classSlots = listOf(
            Slot(listOf(
                Trait("Extra Attack", "Arrow now has 10% chance to hit 1 extra time."),
                Trait("Canine Spirit", "Primal Companion now has +3 extra hit points and deals +1 damage."),
                Trait("Potent Mark", "Hunter's Mark now returns to your hand if you eliminate the hunted target."),
            )),
            Slot(listOf(
                Trait("Explosive Fire Arrow", "Fire Arrow now deals +2 fire damage and creates an area of fire tiles."),
                Trait("Devastating Barrage", "Conjure Barrage now deals +2 damage with each attack."),
                Trait("Rodent Ringleader", "Horn of Rat Reinforcements now creates +2 rats."),
            )),
            Slot(listOf(
                Trait("Mithral Arrow", "Grants the Mithral Arrow card."),
                Trait("Vicious Brambles", "Ensnaring Arrow now deals +3 physical damage."),
                Trait("Quick Dodge", "The first time you take damage each turn, gain 25% chance to automatically move 2 tiles away from your attacker."),
            )),
        ),
    ),
    HeroClass(
        "Fighter", Attribute.Strength, Attribute.Constitution,
        classSlots = listOf(
            Slot(listOf(
                Trait("Extra Attack", "Melee attacks now have 10% chance to hit 1 extra time."),
                Trait("Third Wind", "Second Wind now heals +3 hit points."),
                Trait("Fear and Fury", "Lunging Attack now has 10% chance to inflict Frightened."),
            )),
            Slot(listOf(
                Trait("Whirlwind", "Sweeping Attack now deals +2 physical damage and pushes enemies +1 tile."),
                Trait("Shove", "Grants the Shove card."),
                Trait("Bait and Twist", "Bait and Switch now deals 1 physical damage."),
            )),
            Slot(listOf(
                Trait("Relentless Surge", "Action Surge now grants +1 AP."),
                Trait("Fortified Armor", "Grants +2 extra armor."),
                Trait("Fire Charge", "Lunging Attack now deals +2 fire damage and creates an area of fire tiles around you."),
            )),
        ),
    ),
    HeroClass(
        "Warlock", Attribute.Charisma, Attribute.Constitution,
        classSlots = listOf(
            Slot(listOf(
                Trait("Lingering Gaze", "Eyebite can now target up to 2 creatures each turn."),
                Trait("Eldritch Missile", "Eldritch Blast now fires 2 projectiles but deals -2 damage per projectile."),
                Trait("Hexplosion", "When you kill a target with Hex they explode, dealing necrotic damage to adjacent targets."),
            )),
            Slot(listOf(
                Trait("Hold Monsters", "Hold Monster will bounce to up to 2 additional creatures."),
                Trait("Big Bane", "Bane can target up to 5 enemies and has a larger area."),
                Trait("Hungry Fingers", "Vampiric Touch now deals +3 necrotic damage."),
            )),
            Slot(listOf(
                Trait("Eldritch Overload", "Eldritch Blast has a 30% chance to fire an additional projectile."),
                Trait("Spreading Chill", "Chill Touch now has 30% chance to deal +3 damage and bounce to another target."),
                Trait("Dark Reach", "Arms of Hadar now has +2 range."),
            )),
        ),
    ),
    // TODO: Druid is hidden until its real skills and default attributes are added (attributes below are a guess).
    HeroClass("Druid", Attribute.Wisdom, Attribute.Constitution, placeholderSlots("Druid"), available = false),
)

/** Classes shown in the UI. */
val AvailableClasses: List<HeroClass> = HeroClasses.filter { it.available }
