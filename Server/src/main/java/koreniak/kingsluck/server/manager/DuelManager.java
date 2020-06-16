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
        int attackerDamage = attacker.getEfficiency().getCurrentValue() * getDamageModifier(attacker, target);
        target.getEfficiency().dec(attackerDamage);

        if (target.getEfficiency().getCurrentValue() <= 0) {
            return target;
        }

        if (attacker.getEfficiency().getCurrentValue() <= 0) {
            return attacker;
        }

        return null;
    }

    private static int getDamageModifier(Unit attacker, Unit target) {
        int damageModifier = 1;

        switch (attacker.getUnitType()) {
            case SWORDSMAN: {
                switch (target.getUnitType()) {
                    case SPEARMAN: {
                        damageModifier = 2;
                        break;
                    }
                }
                break;
            }
            case SPEARMAN: {
                switch (target.getUnitType()) {
                    case CAVALRY: {
                        damageModifier = 2;
                        break;
                    }
                }
                break;
            }
            case CAVALRY: {
                switch (target.getUnitType()) {
                    case ARCHER: {
                        damageModifier = 2;
                        break;
                    }
                }
                break;
            }
            case ARCHER: {
                switch (target.getUnitType()) {
                    case SWORDSMAN: {
                        damageModifier = 2;
                        break;
                    }
                }
                break;
            }
        }

        return damageModifier;
    }
}
