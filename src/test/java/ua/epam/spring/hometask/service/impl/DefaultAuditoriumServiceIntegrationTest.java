package ua.epam.spring.hometask.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.annotation.Resource;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.service.AuditoriumService;
import ua.epam.spring.hometask.service.UserService;

public class DefaultAuditoriumServiceIntegrationTest extends AbstractServiceIntegrationTest {

	@Resource 
	private UserService userService;
	
	@Resource
	private AuditoriumService auditoriumService;
	
	@Before
	public void before() {
		
	}
	
	@Test
	public void shouldReturnAuditoriums() {
		Set<Auditorium> auditoriums = auditoriumService.getAll();
		assertThat(auditoriums).isNotEmpty();
	}

}
