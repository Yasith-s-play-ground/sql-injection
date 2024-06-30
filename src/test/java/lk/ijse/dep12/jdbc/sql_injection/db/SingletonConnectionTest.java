package lk.ijse.dep12.jdbc.sql_injection.db;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class SingletonConnectionTest {

    @Test
    void getInstance() throws Exception {
        SingletonConnection instance1 = SingletonConnection.getInstance();
        SingletonConnection instance2 = SingletonConnection.getInstance();
        SingletonConnection instance3 = SingletonConnection.getInstance();

        assertEquals(instance1, instance2);
        assertEquals(instance1, instance3);
    }

    @Test
    void getConnection() throws Exception {
        Connection connection1 = SingletonConnection.getInstance().getConnection();
        Connection connection2 = SingletonConnection.getInstance().getConnection();
        Connection connection3 = SingletonConnection.getInstance().getConnection();

        assertEquals(connection1, connection2);
        assertEquals(connection1, connection3);
    }

}