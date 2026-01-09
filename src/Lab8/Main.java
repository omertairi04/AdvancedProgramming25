package Lab8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.*;


enum ActionType {
    JOIN_GAME,
    LEAVE_GAME,
    ATTACK
}


class Player {
    private final String id;
    private int score;

    public Player(String id) {
        this.id = id;
        this.score = 0;
    }

    // TODO: Implement addScore function
    public synchronized void addScore(int score) {
        this.score += score;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", score=" + score +
                '}';
    }
}


class PlayerAction {
    private final String playerId;
    private final ActionType action;

    public PlayerAction(String playerId, ActionType action) {
        this.playerId = playerId;
        this.action = action;
    }

    public String getPlayerId() {
        return playerId;
    }

    public ActionType getActionType() {
        return action;
    }

    public int getProcessingTime() {
        switch (action) {
            case JOIN_GAME:
                return 20;
            case LEAVE_GAME:
                return 30;
            case ATTACK:
                return 5;
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return "PlayerAction{" +
                "playerId='" + playerId + '\'' +
                ", action=" + action +
                '}';
    }
}

class RoomAction {
    final String roomId;
    final PlayerAction action;

    RoomAction(String roomId, PlayerAction action) {
        this.roomId = roomId;
        this.action = action;
    }
}


class GameRoom {
    public final String roomId;
    public final Map<String, Player> players = new ConcurrentHashMap<>();

    private final BlockingQueue<PlayerAction> actionQueue =
            new LinkedBlockingQueue<>();

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    public volatile boolean running = true;

    public GameRoom(String roomId) {
        this.roomId = roomId;
        startProcessor();
    }

    // TODO: Implement startProcessor
    private void startProcessor() {
        Thread processorThread = new Thread(() -> {
            // Continue while room is active OR while there are still actions to process
            while (running && !actionQueue.isEmpty()) {
                try {
                    // Wait up to 100ms for an action from the queue
                    PlayerAction action = actionQueue.poll(100, TimeUnit.MILLISECONDS);

                    if (action != null) {
                        // Process the action if one was retrieved
                        processAction(action);
                    }
                    // If action is null, timeout occurred - loop continues to check conditions

                } catch (InterruptedException e) {
                    // Thread was interrupted, restore interrupt status and exit
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "ActionProcessor-" + roomId); // Optional: give thread a descriptive name

        processorThread.start();
    }

    public void submitAction(PlayerAction action) {
        System.out.println("[" + roomId + "] RECEIVED: " + action);
        actionQueue.offer(action);
    }

    private void processAction(PlayerAction action) {
        try {
            Thread.sleep(action.getProcessingTime());
            if (action.getProcessingTime() == 20 && !players.containsKey(action.getPlayerId())) {
                players.put(action.getPlayerId(), new Player(action.getPlayerId()));
                System.out.println("Player " + action.getPlayerId() + " joined");
            } else if (action.getProcessingTime() == 30) {
                players.remove(action.getPlayerId());
                System.out.println("Player " + action.getPlayerId() + " left the game");
            } else if (action.getProcessingTime() == 5) {
                if (players.containsKey(action.getPlayerId())) {
                    Player p = players.get(action.getPlayerId());
                    p.addScore(10);
                } else {
                    System.out.println("Player " + action.getPlayerId() + " not in game");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        switch (action.getActionType()) {
            case JOIN_GAME:
                players.putIfAbsent(
                        action.getPlayerId(),
                        new Player(action.getPlayerId())
                );
                System.out.println("[" + roomId + "] JOIN: "
                        + action.getPlayerId());
                break;

            case LEAVE_GAME:
                if (players.remove(action.getPlayerId()) != null) {
                    System.out.println("[" + roomId + "] LEAVE: "
                            + action.getPlayerId());
                } else {
                    System.out.println("[" + roomId
                            + "] LEAVE IGNORED (not in room): "
                            + action.getPlayerId());
                }
                break;

            case ATTACK:
                Player p = players.get(action.getPlayerId());
                if (p == null) {
                    System.out.println("[" + roomId
                            + "] ATTACK IGNORED (not in room): "
                            + action.getPlayerId());
                } else {
                    p.addScore(10);
                    System.out.println("[" + roomId + "] ATTACK: " + p);
                }
                break;
        }
    }

    public void shutdown() {
        // Set the running flag to false to signal shutdown
        running = false;

        // Initiate graceful shutdown of the executor
        executor.shutdown();

        try {
            // Wait up to 5 seconds for existing tasks to complete
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                // If tasks didn't finish in time, force shutdown
                executor.shutdownNow();

                // Wait a bit more for tasks to respond to cancellation
                if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                    System.err.println("[" + roomId + "] Executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            // If interrupted, force shutdown immediately
            executor.shutdownNow();
            // Restore interrupt status
            Thread.currentThread().interrupt();
        }

        System.out.println("[" + roomId + "] FINAL PLAYERS:");
        players.values().forEach(p ->
                System.out.println("  " + p));
    }
}


class GameServer {

    private final BlockingQueue<RoomAction> inputQueue =
            new LinkedBlockingQueue<>();

    private final ConcurrentHashMap<String, GameRoom> rooms =
            new ConcurrentHashMap<>();

    private final ExecutorService dispatcher =
            Executors.newSingleThreadExecutor();

    private volatile boolean running = true;

    public GameServer() {
        startDispatcher();
    }

    private void startDispatcher() {
        dispatcher.submit(() -> {
            // Continue while server is running OR while there are actions to process
            while (running || !inputQueue.isEmpty()) {
                try {
                    // Wait up to 100ms for an action from the main queue
                    RoomAction roomAction = inputQueue.poll(100, TimeUnit.MILLISECONDS);

                    if (roomAction != null) {
                        String roomId = roomAction.roomId;
                        PlayerAction action = roomAction.action;

                        // Find existing room or create a new one (thread-safe)
                        GameRoom room = rooms.computeIfAbsent(roomId,
                                id -> new GameRoom(id));

                        // Forward the action to the appropriate room
                        room.submitAction(action);
                    }
                    // If roomAction is null, timeout occurred - loop continues

                } catch (InterruptedException e) {
                    // Thread was interrupted, restore interrupt status and exit
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    public void submit(String roomId, PlayerAction action) {
        inputQueue.offer(new RoomAction(roomId, action));
    }

    public void shutdown() {
        // Signal shutdown to dispatcher thread
        running = false;

        // Shutdown the dispatcher executor
        dispatcher.shutdown();

        try {
            // Wait up to 5 seconds for dispatcher to finish processing remaining actions
            if (!dispatcher.awaitTermination(5, TimeUnit.SECONDS)) {
                // Force shutdown if grace period expires
                dispatcher.shutdownNow();

                // Wait a bit more for cancellation to complete
                if (!dispatcher.awaitTermination(2, TimeUnit.SECONDS)) {
                    System.err.println("Dispatcher did not terminate");
                }
            }
        } catch (InterruptedException e) {
            // If interrupted, force shutdown immediately
            dispatcher.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Shutdown all active game rooms
        rooms.values().forEach(GameRoom::shutdown);

        // Optional: Clear the rooms map
        rooms.clear();
    }
}


public class Main {

    public static void main(String[] args) throws IOException {

        GameServer server = new GameServer();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        String line;
        while ((line = reader.readLine()) != null && !line.isBlank()) {

            final String input = line.trim();

            try {
                String[] parts = input.split(",");
                if (parts.length != 3) {
                    System.err.println("Invalid input: " + input);
                    return;
                }

                String roomId = parts[0].trim();
                String playerId = parts[1].trim();
                ActionType actionType =
                        ActionType.valueOf(parts[2].trim());

                PlayerAction action =
                        new PlayerAction(playerId, actionType);

                server.submit(roomId, action);

            } catch (Exception e) {
                System.err.println(
                        "Failed to process line: " + input
                );
                e.printStackTrace();
            }
        }

        reader.close();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        server.shutdown();

        System.out.println("Game server stopped.");
    }
}
