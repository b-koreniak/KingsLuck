package koreniak.kingsluck.server.manager;

import koreniak.kingsluck.core.unit.Unit;

public class DuelManager {
    public static Unit duel(Unit attacker, Unit target) {
        while (target.getEfficiency().getCurrentValue() > 0 && attacker.getEfficiency().getCurrentValue() > 0) {
            attack(attacker, target);
            attack(target, attacker);
        }

        if (target.getEfficiency().getCurrentValue() <= 0) {
            return target;
        }

        if (attacker.getEfficiency().getCurrentValue() <= 0) {
            return attacker;
        }

        return null;
    }

    public static Unit attack(Unit attacker, Unit target) {
        int attackerDamage = attacker.getEfficiency().getCurrentValue();
        target.getEfficiency().dec(attackerDamage);

        if (target.getEfficiency().getCurrentValue() <= 0) {
            return target;
        }

        if (attacker.getEfficiency().getCurrentValue() <= 0) {
            return attacker;
        }

        return null;
    }
}
