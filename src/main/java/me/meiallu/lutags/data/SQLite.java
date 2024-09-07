package me.meiallu.lutags.data;

import me.meiallu.lutags.LuTags;
import me.meiallu.lutags.util.Util;
import org.bukkit.Bukkit;

import java.sql.*;

public class SQLite implements Storage {

    private final Connection connection;

    @Override
    public void writeTag(String uuid, String tag) {
        try {
            PreparedStatement statement = connection.prepareStatement(LuTags.sql.sql_write);

            statement.setString(1, uuid);
            statement.setString(2, tag == null ? Tag.getPlayerTag(uuid).name : tag);
            statement.setString(3, Medal.getPlayerMedal(uuid).name);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void writeMedal(String uuid, String medal) {
        try {
            PreparedStatement statement = connection.prepareStatement(LuTags.sql.sql_write);

            statement.setString(1, uuid);
            statement.setString(2, Tag.getPlayerTag(uuid).name);
            statement.setString(3, medal == null ? Medal.getPlayerMedal(uuid).name : medal);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public String readTag(String uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement(LuTags.sql.sql_read_tag);

            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return resultSet.getString("tag");
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return null;
    }

    @Override
    public String readMedal(String uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement(LuTags.sql.sql_read_medal);

            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return resultSet.getString("medal");
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return null;
    }

    public SQLite() {
        try {
            Util.log("Connecting to SQLite database...");

            String clazz = LuTags.sql.database.get("sqlite").get("clazz");
            String url = LuTags.sql.database.get("sqlite").get("url");

            Class.forName(clazz);

            connection = DriverManager.getConnection(url);
            connection.createStatement().execute(LuTags.sql.sql_table_creation);

            Util.log("Successfully connected and setup!");
        } catch (SQLException | ClassNotFoundException exception) {
            Bukkit.shutdown();
            throw new RuntimeException(exception);
        }
    }
}
