package me.Tixius24.object;

public enum ParticleObject {

	EXPLOSION_NORMAL(true, 0.1f, 0.7f, 0.1f, 0.1f, 3),
	EXPLOSION_LARGE(true, 0.02f, 0.5f, 0.02f, 0.02f, 1),
	FIREWORK_SPARK(true, 0.2f, 0.7f, 0.2f, 0.2f, 6),
	WATER_SPLASH(true, 0.4f, 0.8f, 0.4f, 0f, 40),
	WATER_WAKE(true, 0.4f, 0.8f, 0.4f, 0f, 40),
	DEPTH_SUSPEND(true, 0.4f, 0.7f, 0.4f, 0.4f, 50),
	CRIT(true, 0.4f, 0.7f, 0.4f, 0.4f, 15),
	SMOKE_NORMAL(true, 0.2f, 0.7f, 0.2f, 0.2f, 15),
	SMOKE_LARGE(true, 0.1f, 0.7f, 0.1f, 0.1f, 5),
	SPELL(true, 0.3f, 0.7f, 0.3f, 0.3f, 10),
	SPELL_INSTANT(true, 0.3f, 0.7f, 0.3f, 0.3f, 10),
	SPELL_MOB(true, 0.1f, 0.7f, 0.1f, 0.1f, 25),
	SPELL_MOB_AMBIENT(true, 0.3f, 0.7f, 0.3f, 0.3f, 50),
	SPELL_WITCH(true, 0.2f, 0.7f, 0.2f, 0.2f, 20),
	DRIP_WATER(true, 0.4f, 0.7f, 0.4f, 0f, 15),
	DRIP_LAVA(true, 0.4f, 0.7f, 0.4f, 0f, 15),
	VILLAGER_ANGRY(true, 0.4f, 0.7f, 0.4f, 0.4f, 3),
	VILLAGER_HAPPY(true, 0.4f, 0.7f, 0.4f, 0.4f, 10),
	TOW_AURA(true, 0.8f, 0.7f, 0.8f, 0.8f, 60),
	NOTE(true, 0.5f, 0.7f, 0.5f, 0.5f, 25),
	PORTAL(true, 0.3f, 0.7f, 0.3f, 0.3f, 80),
	ENCHANT_TABLE(true, 0.5f, 0.7f, 0.5f, 0, 30),
	FLAME(true, 0.5f, 0.7f, 0.5f, 0f, 10),
	LAVA(true, 0.3f, 0.7f, 0.3f, 0f, 3),
	CLOUD(true, 0.1f, 0.7f, 0.1f, 0.1f, 5),
	SNOWBALL(true, 0.5f, 0.8f, 0.5f, 0.5f, 20),
	SLIME(true, 0.3f, 0.8f, 0.3f, 0.3f, 12),
	HEART(true, 0.3f, 0.7f, 0.3f, 0.3f, 4),
	BARRIER(true, 0.2f, 0.7f, 0.2f, 0.2f, 2);

	private boolean a;
	private float b;
	private float c;
	private float d;
	private float e;
	private int f;

	private ParticleObject(boolean bol, float arg1, float arg2, float arg3, float arg4, int count) {
		a = bol;
		b = arg1;
		c = arg2;
		d = arg3;
		e = arg4;
		f = count;
	}

	public boolean getBoolean() {
		return a;
	}

	public float getOffSet1() {
		return b;
	}

	public float getOffSet2() {
		return c;
	}

	public float getOffSet3() {
		return d;
	}

	public float getOffSet4() {
		return e;
	}

	public int getCount() {
		return f;
	}

}