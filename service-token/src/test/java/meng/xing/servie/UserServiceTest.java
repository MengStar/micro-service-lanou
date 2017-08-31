package meng.xing.servie;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;




@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    public void getLastPasswordResetByUsername() throws Exception {
        Assert.assertNotEquals(userService.getLastPasswordResetByUsername("admin"), "null");
    }

    @Test
    public void getCorrectPasswordByUsername() throws Exception {
        Assert.assertEquals(userService.getCorrectPasswordByUsername("admin"), "admin");
    }


}