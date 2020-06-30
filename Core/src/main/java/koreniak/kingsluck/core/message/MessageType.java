package koreniak.kingsluck.core.message;

public enum MessageType {
    DEFAULT,

    CONNECTION_LOST,

    START_GAME, END_GAME,

    TRANSFER_TURN, SKIP_ROUND, PUT_UNIT_ON_FIELD, DUEL_ENEMY_UNIT,

    ROUND_VICTORY, ROUND_DRAW,

    GAME_VICTORY, GAME_DRAW,

    LEADER_INFO,

    MESSAGE_QUEUE,

    LEAVED_GAME,

    GAME_STATE;
}
