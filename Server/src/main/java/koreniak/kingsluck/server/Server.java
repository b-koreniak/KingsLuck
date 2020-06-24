package koreniak.kingsluck.server;

import koreniak.kingsluck.core.leader.Leader;
import koreniak.kingsluck.core.message.Message;
import koreniak.kingsluck.core.net.SessionI;
import koreniak.kingsluck.core.net.SessionPropertyType;
import koreniak.kingsluck.core.unit.Unit;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private Map<Integer, Room> rooms = new ConcurrentHashMap<>();

    public void handleMessage(SessionI session, Message message) {
        Integer roomId = (Integer) session.getProperty(SessionPropertyType.ROOM_ID);

        Room room = getRoom(session, message, roomId);

        switch (message.getType()) {
            case PUT_UNIT_ON_FIELD: {
                List<Object> objects = (List) message.getObject();

                int row = (int) objects.get(0);
                int column = (int) objects.get(1);
                Unit unit = (Unit) objects.get(2);

                room.putUnitOnField(row, column, unit);
                break;
            }
            case DUEL_ENEMY_UNIT: {
                List<Object> objects = (List) message.getObject();

                int targetRow = (int) objects.get(0);
                int targetColumn = (int) objects.get(1);
                Unit unitAttacker = (Unit) objects.get(2);

                room.duelEnemyUnit(targetRow, targetColumn, unitAttacker);
                break;
            }
            case SKIP_ROUND: {
                room.skipRound();
                break;
            }
        }
    }

    public void handleSessionClose(SessionI session) {
        Integer roomId = (Integer) session.getProperty(SessionPropertyType.ROOM_ID);

        if (roomId != null) {
            rooms.get(roomId).handleLeaveGame(session);
        }
    }

    private int nextRoomId;

    private Room getRoom(SessionI session, Message message, Integer roomId) {
        Room room;
        if (roomId != null) {
            room = rooms.get(roomId);
        } else {
            Optional<Room> emptyRoom = rooms.values().stream()
                    .filter(filteredRoom -> !filteredRoom.isFilled())
                    .findFirst();

            Leader leader = (Leader) message.getObject();

            if (emptyRoom.isPresent()) {
                room = emptyRoom.get();

                room.addLeader(leader, session);

                if (room.isFilled()) {
                    room.startGame();
                }
            } else {
                room = new Room();

                room.addLeader(leader, session);

                rooms.put(nextRoomId, room);
                session.addProperty(SessionPropertyType.ROOM_ID, nextRoomId);

                nextRoomId++;
            }
        }
        return room;
    }
}
