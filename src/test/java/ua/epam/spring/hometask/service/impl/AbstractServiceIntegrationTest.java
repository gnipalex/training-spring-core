package ua.epam.spring.hometask.service.impl;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-application-context.xml")
public class AbstractServiceIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
	
}
