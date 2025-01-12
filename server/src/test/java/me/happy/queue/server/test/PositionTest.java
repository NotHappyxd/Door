package me.happy.queue.server.test;

import me.happy.door.commons.lane.Lane;
import me.happy.door.commons.queue.Queue;
import me.happy.door.commons.queue.QueuePlayer;
import me.happy.door.commons.server.DoorServer;
import me.happy.door.server.queue.QueueManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionTest {

    @Test
    public void testPriorityPosition() {
        Queue queue = new Queue("test");

        Lane lane = new Lane("abc", "test.abc", 1);
        Lane priorityLane = new Lane("def", "test.def", 2);

        DoorServer server = new DoorServer();

        QueuePlayer normalPlayer = new QueuePlayer(UUID.randomUUID(), lane, false, System.currentTimeMillis(), server);
        QueuePlayer priorityPlayer = new QueuePlayer(UUID.randomUUID(), priorityLane, false, System.currentTimeMillis(), server);

        queue.getPlayers().add(normalPlayer);
        queue.getPlayers().add(priorityPlayer);

        assertEquals(0, new ArrayList<>(queue.getPlayers()).indexOf(priorityPlayer));
        assertEquals(1, new ArrayList<>(queue.getPlayers()).indexOf(normalPlayer));
    }

    @Test
    public void testSamePriorityPosition() {
        Queue queue = new Queue("test");

        Lane lane = new Lane("abc", "test.abc", 1);

        DoorServer server = new DoorServer();

        QueuePlayer normalPlayer = new QueuePlayer(UUID.randomUUID(), lane, false, System.currentTimeMillis(), server);
        QueuePlayer beforePlayer = new QueuePlayer(UUID.randomUUID(), lane, false, System.currentTimeMillis(), server);

        normalPlayer.getInsertedAtMap().put(queue, System.currentTimeMillis());
        beforePlayer.getInsertedAtMap().put(queue, System.currentTimeMillis() - 1000);

        queue.getPlayers().add(normalPlayer);
        queue.getPlayers().add(beforePlayer);

        assertEquals(0, new ArrayList<>(queue.getPlayers()).indexOf(beforePlayer));
        assertEquals(1, new ArrayList<>(queue.getPlayers()).indexOf(normalPlayer));
    }
}
