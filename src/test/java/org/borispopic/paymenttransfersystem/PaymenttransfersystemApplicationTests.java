package org.borispopic.paymenttransfersystem;

import org.borispopic.paymenttransfersystem.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
class PaymenttransfersystemApplicationTests {

    @Test
    void contextLoads() {
    }
}