package me.Tixius24;

public enum ParticleManager {

	EXPLOSION_NORMAL("explode","EXPLOSION_NORMAL", "poof"),
	EXPLOSION_LARGE("largeexplode","EXPLOSION_LARGE", "explosion"),
	FIREWORK_SPARK("fireworksSpark","FIREWORKS_SPARK", "firework"),
	WATER_SPLASH("splash","WATER_SPLASH", "splash"),
	WATER_WAKE("wake", "WATER_WAKE", "fishing"),
	DEPTH_SUSPEND("depthsuspend","SUSPENDED_DEPTH", "mycelium"),
	CRIT("crit","CRIT", "crit"),
	SMOKE_NORMAL("smoke","SMOKE_NORMAL", "smoke"),
	SMOKE_LARGE("largesmoke","SMOKE_LARGE", "large_smoke"),
	SPELL("spell","SPELL", "effect"),
	SPELL_INSTANT("instantSpell","SPELL_INSTANT", "instant_effect"),
	SPELL_MOB("mobSpell","SPELL_MOB", "entity_effect"),
	SPELL_MOB_AMBIENT("mobSpellAmbient","SPELL_MOB_AMBIENT", "ambient_entity_effect"),
	SPELL_WITCH("witchMagic","SPELL_WITCH", "witch"),
	DRIP_WATER("dripWater","DRIP_WATER", "dripping_water"),
	DRIP_LAVA("dripLava","DRIP_LAVA", "dripping_lava"),
	VILLAGER_ANGRY("angryVillager","VILLAGER_ANGRY", "angry_villager"),
	VILLAGER_HAPPY("happyVillager","VILLAGER_HAPPY", "happy_villager"),
	TOW_AURA("townaura","TOWN_AURA", "mycelium"),
	NOTE("note","NOTE", "note"),
	PORTAL("portal","PORTAL", "portal"),
	ENCHANT_TABLE("enchantmenttable","ENCHANTMENT_TABLE", "enchant"),
	FLAME("flame","FLAME", "flame"), 
	LAVA("lava","LAVA", "lava"),
	CLOUD("cloud","CLOUD", "cloud"),
	SNOWBALL("snowballpoof","SNOWBALL", "item_snowball"),
	SLIME("slime","SLIME", "item_slime"), 
	HEART("heart","HEART", "heart"),
	BARRIER("barrier","BARRIER", "barrier");

	private String Particle1_5_x;
	private String Particle1_8_x;
	private String Particle1_13_x;
	private AdvanceParticles plugin;

	private ParticleManager(String particle5, String particle8, String particle13) {
		Particle1_5_x = particle5;
		Particle1_8_x = particle8;
		Particle1_13_x = particle13;
		plugin = AdvanceParticles.getInstance();
	}

	public String get() {
		if (plugin.getVersionNumger() < 8) {
			return Particle1_5_x;
		} else if (plugin.getVersionNumger() >= 8 && plugin.getVersionNumger() < 13) {
			return Particle1_8_x; 
		}

		return Particle1_13_x;
	}

}