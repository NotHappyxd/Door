package me.happy.door.commons.queue

import java.util.Comparator

class QueuePlayerComparator(private val queue: Queue) : Comparator<QueuePlayer> {
    override fun compare(o1: QueuePlayer, o2: QueuePlayer): Int {
        if (o1.lane.name == o2.lane.name) {
            if (o1.getInsertedOn(queue) == o2.getInsertedOn(queue))
                return 0
            else if (o1.getInsertedOn(queue)!! > o2.getInsertedOn(queue)!!) return 1
                return -1
        }

        return if (o1.lane.priority > o2.lane.priority) -1 else 1
    }
}