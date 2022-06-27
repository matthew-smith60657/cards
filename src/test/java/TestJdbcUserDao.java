import com.projects.model.User;
import com.projects.dao.JdbcUserDao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
@FixMethodOrder(MethodSorters.JVM)
public class TestJdbcUserDao {

    private final User TEST_USER_1 = new User(1, "dummy1", "mypwd", 1, 1);
    private final User TEST_USER_2 = new User(2, "dummy2", "otherpwd", 2, 2);
    private final User TEST_USER_3 = new User(3, "dummy3", "anotherpwd", 3, 3);
    private final String DB_NAME = "user_table_testing";
    private SingleConnectionDataSource adminConnectionDataSource;
    private SingleConnectionDataSource singleConnectionDataSource;
    private JdbcTemplate adminJdbcTemplate;
    private JdbcUserDao dao;

    @Before
    public void setup() throws SQLException {
        adminConnectionDataSource = new SingleConnectionDataSource();
        adminConnectionDataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
        adminConnectionDataSource.setUsername("postgres");
        adminConnectionDataSource.setPassword("postgres1");

        adminJdbcTemplate = new JdbcTemplate(adminConnectionDataSource);
        adminJdbcTemplate.update("DROP DATABASE IF EXISTS \"" + DB_NAME + "\";");
        adminJdbcTemplate.update("CREATE DATABASE \"" + DB_NAME + "\";");

        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
        singleConnectionDataSource.setUsername("postgres");
        singleConnectionDataSource.setPassword("postgres1");
        singleConnectionDataSource.setAutoCommit(false);

        ScriptUtils.executeSqlScript(singleConnectionDataSource.getConnection(), new ClassPathResource("test-data.sql"));

        dao = new JdbcUserDao(singleConnectionDataSource);
    }

    @After
    public void rollback() throws SQLException {
        singleConnectionDataSource.getConnection().rollback();
    }

    @Test
    public void full_user_list_is_retrievable() {
        List<User> userList = dao.getAllUserNames();
        Assert.assertEquals("User List should be size 2", 2, userList.size());
    }

    @Test
    public void users_one_and_two_are_retrievable() {
        User user = dao.getUser(1);
        assertUserEqual(TEST_USER_1, user);

        user = dao.getUser(2);
        assertUserEqual(TEST_USER_2, user);
    }

    @Test
    public void users_one_and_two_password_validate() {
        User user = dao.getUser(1);
        Assert.assertTrue(user.validatePassword("mypwd"));

        user = dao.getUser(2);
        Assert.assertTrue(user.validatePassword("otherpwd"));
    }

    @Test
    public void update_user_name() {
        User user = dao.getUser(1);
        user.setName("hamburger");

        dao.updateUser(user);

        user = dao.getUser(1);
        Assert.assertEquals("hamburger", user.getName());
    }

    @Test
    public void create_new_user() {
        int newId = dao.createUser(TEST_USER_3.getName(), "anotherpwd");

        User user = dao.getUser(newId);
        Assert.assertEquals(TEST_USER_3.getName(), user.getName());
        Assert.assertEquals(TEST_USER_3.getUserId(), user.getUserId());
    }

    public void assertUserEqual(User user1, User user2) {
        Assert.assertEquals(user1.getUserId(), user2.getUserId());
        Assert.assertEquals(user1.getName(), user2.getName());
        Assert.assertEquals(user1.getWarId(), user2.getWarId());
        Assert.assertEquals(user1.getGoFishId(), user2.getGoFishId());
    }
}
