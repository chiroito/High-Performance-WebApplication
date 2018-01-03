package system.sql;

import domain.User;
import domain.dao.UserDao;
import domain.dao.exception.DataStoreException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoSql implements UserDao {

    private final Connection con;

    public UserDaoSql(Connection con) {
        this.con = con;
    }

    // TODO: 本来なら存在チェックを含めるが、このサンプルで伝える目的ではないため省略
    @Override
    public User find(int userId) throws DataStoreException {

        // DBに格納されたオブジェクトを取得
        try (PreparedStatement userStmt = createUserStatement(con, userId); ResultSet userRS = userStmt.executeQuery()) {

            userRS.next();

            // ドメインオブジェクトの生成
            User user = new User(userId, userRS.getString("NAME"));

            return user;

        } catch (SQLException e) {
            throw new DataStoreException("データベースに問題が発生しました", e);
        }
    }


    private static final String USER_SQL = "SELECT * FROM user_t WHERE user_id = ?";

    private PreparedStatement createUserStatement(Connection con, int userId) throws SQLException {

        PreparedStatement userStmt = con.prepareStatement(USER_SQL);
        userStmt.setInt(1, userId);

        return userStmt;
    }
}
