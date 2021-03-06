package me.rabrg.rat.node;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.utilities.impl.Condition;
import org.dreambot.api.wrappers.interactive.NPC;

public final class AttackNode extends Node {

	private NPC monster;

	public AttackNode(final MethodContext ctx) {
		super(ctx);
	}

	@Override
	public boolean validate() {
		return !ctx.getLocalPlayer().isInCombat() && (monster = ctx.getNpcs().closest(filter)) != null;
	}

	@Override
	public int execute() {
		if (monster.distance() > 10)
			ctx.getWalking().walk(monster);
		else if (!monster.isOnScreen())
			ctx.getCamera().rotateToEntity(monster);
		else
			if (monster.interact("Attack")) {
				MethodProvider.sleepUntil(new Condition() {
					@Override
					public boolean verify() {
						return ctx.getLocalPlayer().isInCombat();
					}
					
				}, Calculations.random(750, 2500));
				if (Calculations.random(1, 5) >= 4) {
					MethodProvider.sleep(750, 1250);
					ctx.getMouse().moveMouseOutsideScreen();
				}
			}
		return Calculations.random(550, 1250);
	}

	private final Filter<NPC> filter = new Filter<NPC>(){
		@Override
		public boolean match(final NPC n) {
			if(n == null || n.getActions() == null || n.getActions().length <= 0)
				return false;
			if(n.getName() == null || (!n.getName().equals("Goblin")/* && !n.getName().equals("Minotaur")*/))
				return false;
			return true;
		}
	};

	@Override
	public String getName() {
		return "Attacking target";
	}
}
