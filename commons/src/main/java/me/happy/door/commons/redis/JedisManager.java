package me.happy.door.commons.redis;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.Map;

public class JedisManager {

    private static JedisPool pool;
    private static final Map<String, Class<? extends Packet>> packetClassMap = new HashMap<>();
    private static final Gson gson = new Gson();
    private static String password;

    public static void connect(String host, int port, String password) {
        pool = new JedisPool(host, port);
        JedisManager.password = password;

        Jedis jedis = pool.getResource();
        if (!password.isEmpty())
            jedis.auth(password);

        new Thread(() -> jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                String[] args = message.split("\\|\\|\\|");

                Class<? extends Packet> clazz = packetClassMap.get(args[0]);

                if (clazz == null) {
                    System.out.println("Tried to handle an unregistered packet. Message:\n" + message);
                    return;
                }


                Packet packet = gson.fromJson(StringUtils.join(args, "|||", 1, args.length), clazz);

                packet.onReceive();
            }
        }, "queue")).start();
    }

    public static void registerPacket(Packet packet) {
        packetClassMap.put(packet.getIdentifier(), packet.getClass());
    }

    public static void writePacket(Packet packet) {
        writePacket("queue", packet);
    }

    public static void writePacket(String channel, Packet packet) {
        if (pool == null) {
            packet.onSend();
            packet.onReceive();
            return;
        }

        packet.onSend();

        try (Jedis jedis = pool.getResource()) {
            if (!password.isEmpty())
                jedis.auth(password);
            jedis.publish(channel, packet.getIdentifier() + "|||" + gson.toJson(packet));
        }
    }

    public static JedisPool getPool() {
        return pool;
    }
}