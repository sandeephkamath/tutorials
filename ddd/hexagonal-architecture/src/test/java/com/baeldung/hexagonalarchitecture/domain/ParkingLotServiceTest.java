package com.baeldung.hexagonalarchitecture.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baeldung.hexagonalarchitecture.port.ParkingLotPersistencePort;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParkingLotServiceTest {

    @InjectMocks
    private ParkingLotService parkingLotService;

    @Mock
    private ParkingLotPersistencePort parkingLotPersistencePort;

    private static final Car CAR = new Car("123", "Tesla", "Black");

    @Test
    public void givenParkingLotPresentAndNotFull_whenParked_thenParkCarAndSave() {
        ObjectId parkingLotId = new ObjectId();
        ParkingLot parkingLot = ParkingLot.builder()
            .id(parkingLotId)
            .capacity(1)
            .build();
        when(parkingLotPersistencePort.findById(parkingLotId)).thenReturn(parkingLot);

        parkingLotService.park(parkingLotId, CAR);

        assertThat(parkingLot.isFull()).isTrue();
        verify(parkingLotPersistencePort, times(1)).save(parkingLot);
    }

    @Test
    public void givenParkingLotANdCarPresent_whenUnParked_thenUnParkAndSave() {
        ObjectId parkingLotId = new ObjectId();
        ParkingLot parkingLot = ParkingLot.builder()
            .id(parkingLotId)
            .capacity(1)
            .build();
        when(parkingLotPersistencePort.findById(parkingLotId)).thenReturn(parkingLot);
        parkingLotService.park(parkingLotId, CAR);
        parkingLotService.unPark(parkingLotId, CAR);
        assertThat(parkingLot.isFull()).isFalse();
        verify(parkingLotPersistencePort, times(2)).save(parkingLot);
    }

    @Test
    public void givenCapacity_thenCreateAndSaveParkingLotOfGivenCapacity() {
        int capacity = 5;
        ParkingLot parkingLot = ParkingLot.builder()
            .capacity(capacity)
            .build();

        parkingLotService.create(capacity);

        verify(parkingLotPersistencePort, times(1)).save(parkingLot);
    }

    @Test
    public void givenParkingLotExists_whenQueried_thenReturnFromPersistence() {
        ObjectId parkingLotId = new ObjectId();
        ParkingLot parkingLot = ParkingLot.builder()
            .id(parkingLotId)
            .capacity(1)
            .build();

        when(parkingLotPersistencePort.findById(parkingLotId)).thenReturn(parkingLot);

        assertThat(parkingLotService.get(parkingLotId)).isEqualTo(parkingLot);
    }

}