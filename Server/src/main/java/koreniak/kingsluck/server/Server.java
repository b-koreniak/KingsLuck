package koreniak.kingsluck.server;

import koreniak.kingsluck.core.leader.Leader;
import koreniak.kingsluck.core.message.Message;
import koreniak.kingsluck.core.message.MessageType;
import koreniak.kingsluck.core.net.SessionI;
import koreniak.kingsluck.core.net.SessionPropertyType;
import koreniak.kingsluck.core.unit.Unit;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private BeanFactory beanFactory = new GenericXmlApplicationContext("game/game-bean.xml");

    private int nextRoomId;

    private Map<Integer, Room> rooms = new ConcurrentHashMap<>();

    public void handleMessage(SessionI session, Message message) throws IOException, EncodeException {
        Integer roomId = (Integer) session.getProperty(SessionPropertyType.ROOM_ID);

        Room room;

        if (rooms.containsKey(roomId)) {
            room = rooms.get(roomId);
        } else {
            Optional<Room> emptyRoom = rooms.values().stream()
                    .filter(filteredRoom -> !filteredRoom.isFilled())
                    .findFirst();

            String playerNickname = (String) message.getObject();

            if (emptyRoom.isPresent()) {
                room = emptyRoom.get();

                room.addPlayer(playerNickname, session);
            } else {
                room = new Room();

                room.addPlayer(playerNickname, session);

                rooms.put(nextRoomId, room);
                session.addProperty(SessionPropertyType.ROOM_ID, nextRoomId);

                nextRoomId++;
            }
        }

        switch (message.getType()) {
            case CONNECTION_REQUEST: {
                session.sendMessage(new Message(MessageType.CONNECTION_ESTABLISHED));
                break;
            }
            case START_GAME_REQUEST: {
                if (room.isFilled()) {
                    Leader leaderFirst = beanFactory.getBean("humanCommander", Leader.class);
                    Leader leaderSecond = beanFactory.getBean("humanCommander", Leader.class);

                    room.initializeGameManager(leaderFirst, leaderSecond);

                    room.getPlayerSessionFirst().getValue().sendMessage(
                            new Message(new Object[] { leaderFirst, leaderSecond }, MessageType.START_GAME_RESPONSE));
                    room.getPlayerSessionSecond().getValue().sendMessage(
                            new Message(new Object[] { leaderSecond, leaderFirst }, MessageType.START_GAME_RESPONSE));
                }
                break;
            }
            case PUT_UNIT_ON_FIELD_REQUEST: {
                Object[] objects = (Object[]) message.getObject();

                int row = (int) objects[0];
                int column = (int) objects[1];
                Unit unit = (Unit) objects[2];

                Message roomMessage = room.getPutUnitOnFieldMessage(row, column, unit);
                room.sendBroadcastMessage(roomMessage);
                break;
            }
            case DUEL_ENEMY_UNIT: {
                Object[] objects = (Object[]) message.getObject();

                int targetRow = (int) objects[0];
                int targetColumn = (int) objects[1];
                Unit unitAttacker = (Unit) objects[2];

                Message roomMessage = room.getDuelEnemyUnitMessage(targetRow, targetColumn, unitAttacker);
                room.sendBroadcastMessage(roomMessage);
                break;
            }
            case SKIP_ROUND: {
                Message roomMessage = room.getSkipRoundMessage();
                room.sendBroadcastMessage(roomMessage);
                break;
            }
        }
    }

    public void handleSessionClose(SessionI session) throws IOException, EncodeException {
        Integer roomId = (Integer) session.getProperty(SessionPropertyType.ROOM_ID);

        if (rooms.containsKey(roomId)) {
            Room room = rooms.get(roomId);

            if (room.isFilled()) {
                String leavedPlayerNickname;

                if (room.getPlayerSessionFirst().getValue().getProperty(SessionPropertyType.SESSION_ID)
                        .equals(session.getProperty(SessionPropertyType.SESSION_ID))) {
                    leavedPlayerNickname = room.getPlayerSessionFirst().getKey();
                } else {
                    leavedPlayerNickname = room.getPlayerSessionSecond().getKey();
                }

                room.sendBroadcastMessage(new Message(leavedPlayerNickname, MessageType.LEAVED_GAME));
            }

            rooms.remove(roomId);
        }

        session.sendMessage(new Message(MessageType.CONNECTION_LOST));
    }
}
