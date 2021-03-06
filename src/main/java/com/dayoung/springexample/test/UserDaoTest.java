package com.dayoung.springexample.test;

import com.dayoung.springexample.bean.User;
import com.dayoung.springexample.dao.UserDao;
import com.dayoung.springexample.service.DAOFactory;
import com.dayoung.springexample.service.SimpleConnectionMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@DirtiesContext
public class UserDaoTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserDao dao;

    User user1;
    User user2;
    User user3;

    @Before
    public void setup(){
        user1 = new User( "22222", "김희진");
        user2 = new User("33333", "양희진");
        user3 = new User("44444", "박정아");

        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:oracle:thin:@localhost:1521:xe", "devuser", "devuser", true
        );
        dao.setDataSource(dataSource);

    }


    @Test
    public void addAndGet() throws SQLException{
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        dao.add(user2);

        assertThat(dao.getCount(), is(2));

        User userGet1 = dao.get(user1.getEmpNo());
        User userGet2 = dao.get(user2.getEmpNo());

        assertThat(userGet1.getUserName(), is(user1.getUserName()));
        assertThat(userGet1.getEmpNo(), is(user1.getEmpNo()));

        assertThat(userGet2.getUserName(), is(user2.getUserName()));
        assertThat(userGet2.getEmpNo(), is(user2.getEmpNo()));
    }

    @Test
    public void count() throws SQLException{


        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException{
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_empNo");
    }
}
