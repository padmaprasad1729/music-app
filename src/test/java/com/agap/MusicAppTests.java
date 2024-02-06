package com.agap;

import com.agap.MusicApp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MusicApp.class)
public class MusicAppTests {

    @Test
    void contextLoads() {
    }
}
