package com.pmolinav;

import com.pmolinav.systemtests.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
public class SpringContextTest {

    @Test
    public void whenSpringContextIsBootstrapped_thenNoExceptions() {
    }
}
