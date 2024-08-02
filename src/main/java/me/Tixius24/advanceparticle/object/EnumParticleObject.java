package me.Tixius24.advanceparticle.object;

import me.Tixius24.advanceparticle.AdvanceParticle;

public enum EnumParticleObject {

	EXPLOSION_NORMAL("explode","EXPLOSION_NORMAL", "poof", true, 0.1f, 0.7f, 0.1f, 0.1f, 3),
	EXPLOSION_LARGE("largeexplode","EXPLOSION_LARGE", "explosion", true, 0.02f, 0.5f, 0.02f, 0.02f, 1),
	EXPLOSION_HUGE("hugeexplosion","EXPLOSION_HUGE", "explosion_emitter", true, 0.01f, 0.3f, 0.01f, 0.01f, 1),
	FIREWORK_SPARK("fireworksSpark","FIREWORKS_SPARK", "firework", true, 0.2f, 0.7f, 0.2f, 0.2f, 6),
	WATER_SPLASH("splash","WATER_SPLASH", "splash", true, 0.4f, 0.8f, 0.4f, 0f, 40),
	WATER_WAKE("wake", "WATER_WAKE", "fishing", true, 0.4f, 0.8f, 0.4f, 0f, 40),
	DEPTH_SUSPEND("depthsuspend","SUSPENDED_DEPTH", "mycelium", true, 0.4f, 0.7f, 0.4f, 0.4f, 50),
	CRIT("crit","CRIT", "crit", true, 0.4f, 0.7f, 0.4f, 0.4f, 15),
	CRIT_MAGIC("-----","CRIT_MAGIC", "enchanted_hit", true, 0.4f, 0.7f, 0.4f, 0.4f, 15),
	SMOKE_NORMAL("smoke","SMOKE_NORMAL", "smoke", true, 0.2f, 0.7f, 0.2f, 0.2f, 15),
	SMOKE_LARGE("largesmoke","SMOKE_LARGE", "large_smoke", true, 0.1f, 0.7f, 0.1f, 0.1f, 5),
	SPELL("spell","SPELL", "effect", true, 0.3f, 0.7f, 0.3f, 0.3f, 10),
	SPELL_INSTANT("instantSpell","SPELL_INSTANT", "instant_effect", true, 0.3f, 0.7f, 0.3f, 0.3f, 10),
	SPELL_MOB("mobSpell","SPELL_MOB", "entity_effect", true, 0.1f, 0.7f, 0.1f, 0.1f, 25),
	SPELL_MOB_AMBIENT("mobSpellAmbient","SPELL_MOB_AMBIENT", "ambient_entity_effect", true, 0.3f, 0.7f, 0.3f, 0.3f, 50),
	SPELL_WITCH("witchMagic","SPELL_WITCH", "witch", true, 0.2f, 0.7f, 0.2f, 0.2f, 20),
	DRIP_WATER("dripWater","DRIP_WATER", "dripping_water", true, 0.4f, 0.7f, 0.4f, 0f, 15),
	DRIP_LAVA("dripLava","DRIP_LAVA", "dripping_lava", true, 0.4f, 0.7f, 0.4f, 0f, 15),
	VILLAGER_ANGRY("angryVillager","VILLAGER_ANGRY", "angry_villager", true, 0.4f, 0.7f, 0.4f, 0.4f, 3),
	VILLAGER_HAPPY("happyVillager","VILLAGER_HAPPY", "happy_villager", true, 0.4f, 0.7f, 0.4f, 0.4f, 10),
	TOW_AURA("townaura","TOWN_AURA", "mycelium", true, 0.8f, 0.7f, 0.8f, 0.8f, 60),
	NOTE("note","NOTE", "note", true, 0.5f, 0.7f, 0.5f, 0.5f, 25),
	PORTAL("portal","PORTAL", "portal", true, 0.3f, 0.7f, 0.3f, 0.3f, 80),
	ENCHANT_TABLE("enchantmenttable","ENCHANTMENT_TABLE", "enchant", true, 0.5f, 0.7f, 0.5f, 0, 30),
	FLAME("flame","FLAME", "flame", true, 0.5f, 0.7f, 0.5f, 0f, 10),
	LAVA("lava","LAVA", "lava", true, 0.3f, 0.7f, 0.3f, 0f, 3),
	FOOTSTEP("footstep","FOOTSTEP", "-----", true, 0.3f, 0.7f, 0.3f, 0f, 3),
	CLOUD("cloud","CLOUD", "cloud", true, 0.1f, 0.7f, 0.1f, 0.1f, 5),
	REDSTONE("reddust","REDSTONE", "-----", true, 0.2f, 0.7f, 0.2f, 5.0f, 10),
	SNOWBALL("snowballpoof","SNOWBALL", "item_snowball", true, 0.5f, 0.8f, 0.5f, 0.5f, 20),
	SNOW_SHOVEL("snowshovel","SNOW_SHOVEL", "spit", true, 0.1f, 0.6f, 0.1f, 0.1f, 20),
	SLIME("slime","SLIME", "item_slime", true, 0.3f, 0.8f, 0.3f, 0.3f, 12),
	HEART("heart","HEART", "heart", true, 0.3f, 0.7f, 0.3f, 0.3f, 4),
	WATER_DROP("-----","WATER_DROP", "rain", true, 0.3f, 0.8f, 0.3f, 0.3f, 12),
	DRAGON_BREATH("-----","DRAGON_BREATH", "dragon_breath", true, 0.1f, 0.2f, 0.1f, 0.05f, 5),
	END_ROD("-----","END_ROD", "end_rod", true, 0.1f, 0.4f, 0.1f, 0.2f, 8),
	DAMAGE_INDICATOR("-----","DAMAGE_INDICATOR", "damage_indicator", true, 0.3f, 0.7f, 0.3f, 0.3f, 4),
	SWEEP_ATTACK("-----","SWEEP_ATTACK", "sweep_attack", true, 0.3f, 0.7f, 0.3f, 0.3f, 4);

	private String a_5;
	private String b_8;
	private String c_13;
	private boolean d;
	private float e;
	private float f;
	private float g;
	private float h;
	private int i;
	private int j = AdvanceParticle.getInstance().getVersionNumber();

	private EnumParticleObject(String arg1, String arg2, String arg3, boolean bol, float arg4, float arg5, float arg6, float speed, int count) {
		a_5 = arg1;
		b_8 = arg2;
		c_13 = arg3;
		d = bol;
		e = arg4;
		f = arg5;
		g = arg6;
		h = speed;
		i = count;
	}
	
	public String get() {
		if (j < 8) {
			return a_5;
		} 
		
		if (j >= 8 && j < 13) {
			return b_8; 
		}

		return c_13;
	}
	
	public boolean getBoolean() {
		return d;
	}

	public float OffSetX() {
		return e;
	}

	public float OffSetY() {
		return f;
	}

	public float OffSetZ() {
		return g;
	}

	public float getSpeed() {
		return h;
	}

	public int getCount() {
		return i;
	}

}