package DAO;

import Model.Response;
import Model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends DAO<User> {

    public UserDao(Connection conn) {
        super(conn);
    }

    @Override
    public Response create(User o) throws SQLException {
        return null;
    }

    @Override
    public Response Update(User o) throws SQLException {
        return null;
    }

    @Override
    public Response delete(int id) throws SQLException {
        return null;
    }

    @Override
    public Response<User> Read(int id) throws SQLException {
        return null;
    }

    @Override
    public Response<User> ReadAll() throws SQLException {
        return null;
    }


}
