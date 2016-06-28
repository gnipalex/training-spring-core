package ua.epam.spring.hometask.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.service.AuditoriumService;

public class DefaultAuditoriumServiceIntegrationTest extends AbstractServiceIntegrationTest {
	
	private static final String TEST_AUDITORIUM_1_CODE = "test-auditorium-1";
	
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
	
	@Test
    public void shouldReturnTestAuditoriumByCode() {
	    Auditorium auditorium = auditoriumService.getByCode(TEST_AUDITORIUM_1_CODE);
	    assertThat(auditorium).isNotNull();
	}
	
}
