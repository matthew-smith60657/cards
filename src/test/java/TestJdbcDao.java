import com.projects.dao.JdbcWarDao;
import com.projects.model.User;
import com.projects.dao.JdbcUserDao;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.SQLException;
import java.util.List;

@FixMethodOrder(MethodSorters.JVM)
public class TestJdbcDao {

    private final User TEST_USER_1 = new User(1, "dummy1", "mypwd", 1, 1, 3, 2, 50, 250);
    private final User TEST_USER_1_VICTORY_300 = new User(1, "dummy1", "mypwd", 1, 1, 4, 3, 50, 300);
    private final User TEST_USER_1_LOSS = new User(1, "dummy1", "mypwd", 1, 1, 4, 2, 50, 250);
    private final User TEST_USER_2 = new User(2, "dummy2", "otherpwd", 2, 2, 2, 0, 0, 0);
    private final User TEST_USER_2_VICTORY_300 = new User(2, "dummy2", "otherpwd", 2, 2, 3, 1, 300, 300);
    private final User TEST_USER_2_LOSS = new User(2, "dummy2", "otherpwd", 2, 2, 3, 0, 0, 0);
    private final User TEST_USER_3 = new User(3, "dummy3", "anotherpwd", 3, 3);
    private final String DB_NAME = "user_table_testing";
    private SingleConnectionDataSource adminConnectionDataSource;
    private SingleConnectionDataSource singleConnectionDataSource;
    private JdbcTemplate adminJdbcTemplate;
    private JdbcUserDao dao;
    private JdbcWarDao warDao;

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
        warDao = new JdbcWarDao(singleConnectionDataSource);
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
    public void users_one_and_two_have_war_records() {
        User user = warDao.getWar(dao.getUser(1));
        assertWarEqual(TEST_USER_1, user);

        user = warDao.getWar(dao.getUser(2));
        assertWarEqual(TEST_USER_2, user);
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
    public void update_war_record() {
        User user = dao.getUser(1);
        user = warDao.getWar(user);

        user.warCompleted(true, 300);
        assertWarEqual(TEST_USER_1_VICTORY_300, user);

        user = dao.getUser(1);
        user = warDao.getWar(user);

        user.warCompleted(false, 300);
        assertWarEqual(TEST_USER_1_LOSS, user);

        user = dao.getUser(2);
        user = warDao.getWar(user);

        user.warCompleted(true, 300);
        assertWarEqual(TEST_USER_2_VICTORY_300, user);

        user = dao.getUser(2);
        user = warDao.getWar(user);

        user.warCompleted(false, 300);
        assertWarEqual(TEST_USER_2_LOSS, user);
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
    public void assertWarEqual(User user1, User user2) {
        Assert.assertEquals(user1.getPlayedWar(), user1.getPlayedWar());
        Assert.assertEquals(user1.getWonWar(), user2.getWonWar());
        Assert.assertEquals(user1.getFastestWarWin(), user2.getFastestWarWin());
        Assert.assertEquals(user1.getSlowestWarWin(), user2.getSlowestWarWin());
    }
}
