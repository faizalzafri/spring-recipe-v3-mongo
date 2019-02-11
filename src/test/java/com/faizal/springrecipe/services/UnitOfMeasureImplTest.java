package com.faizal.springrecipe.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.faizal.springrecipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.faizal.springrecipe.domain.UnitOfMeasure;
import com.faizal.springrecipe.repositories.UnitOfMeasureRepository;

public class UnitOfMeasureImplTest {

	UnitOfMeasureService unitOfMeasureService;

	UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();

	@Mock
	UnitOfMeasureRepository unitOfMeasureRepository;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureRepository,
				unitOfMeasureToUnitOfMeasureCommand);
	}

	@Test
	public void testFindAll() {

		Set<UnitOfMeasure> setUom = new HashSet<>();
		UnitOfMeasure uom1 = new UnitOfMeasure();
		uom1.setId("1");
		setUom.add(uom1);

		UnitOfMeasure uom2 = new UnitOfMeasure();
		uom1.setId("1");
		setUom.add(uom2);

		when(unitOfMeasureRepository.findAll()).thenReturn(setUom);

		assertEquals(2, unitOfMeasureService.findAll().size());
		verify(unitOfMeasureRepository, times(1)).findAll();

	}

}
