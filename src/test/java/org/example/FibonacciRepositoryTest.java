package org.example;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FibonacciRepositoryTest extends PostgresTestContainerInitializer {
    @Autowired
    FibonacciRepository repository;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    EntityManager entityManager;
    private FibonacciNumber fiboObject;
    private final int indexForTest = 4;

    @BeforeEach
    public void testData() {
        FibonacciNumber fiboNumber = new FibonacciNumber();
        fiboNumber.setIndex(indexForTest);
        repository.save(fiboNumber);
        entityManager.flush();
        entityManager.detach(fiboNumber);
        fiboObject = fiboNumber;
    }

    @Test
    @DisplayName("Add new number on database")     //1, 1, 2, 3, 5, 8, 13, 21, 34, 55
    public void givenNewNumber_whenAddOnDatabase_thenSavedOnDatabase() {
        List<FibonacciNumber> actual = jdbcTemplate.query(
                "SELECT * FROM fibonacci_number WHERE index = " + indexForTest,
                (rs, rowNum) ->
                        new FibonacciNumber(rs.getInt("index"), rs.getInt("value"))
        );

        FibonacciNumber checkFiboNumber = actual.stream()
                .filter(fibonacciNumber -> fibonacciNumber.getIndex() == indexForTest)
                .findAny()
                .get();

        assertEquals(fiboObject.getIndex(), checkFiboNumber.getIndex());
        assertEquals(1, actual.size());
    }

    @Test
    @DisplayName("Get number by index")
    public void givenIndex_whenSearchOnDatabase_thenGetFiboNumber() {
        List<FibonacciNumber> actual = jdbcTemplate.query(
                "SELECT * FROM fibonacci_number WHERE index = " + indexForTest,
                (rs, rowNum) ->
                        new FibonacciNumber(rs.getInt("index"), rs.getInt("value"))
        );

        FibonacciNumber checkFiboNumber = actual.stream()
                .filter(fibonacciNumber -> fibonacciNumber.getIndex() == indexForTest)
                .findAny()
                .get();

        assertEquals(fiboObject.getValue(), checkFiboNumber.getValue());
        assertEquals(1, actual.size());
    }

    @Test
    @DisplayName("Add re-number on database")     //1, 1, 2, 3, 5, 8, 13, 21, 34, 55
    public void givenReIndex_whenAddOnDatabase_thenSavedOnDatabase() {
        Executable executable = () -> {
            FibonacciNumber fiboObject1 = new FibonacciNumber();
            fiboObject1.setIndex(indexForTest);
            repository.save(fiboObject1);
            entityManager.flush();
            FibonacciNumber fiboObject2 = new FibonacciNumber();
            fiboObject2.setIndex(indexForTest);
            repository.save(fiboObject2);
            entityManager.flush();
            entityManager.detach(fiboObject1);
            entityManager.detach(fiboObject2);

            List<FibonacciNumber> actual = jdbcTemplate.query(
                    "SELECT * FROM fibonacci_number WHERE index = 4",
                    (rs, rowNum) ->
                            new FibonacciNumber(rs.getInt("index"), rs.getInt("value"))
            );
            FibonacciNumber checkFiboNumber = actual.stream()
                    .filter(fibonacciNumber -> fibonacciNumber.getIndex() == 4)
                    .findAny()
                    .get();
            assertEquals(1, actual.size());
            assertEquals(3, checkFiboNumber.getValue());

        };
        assertThrows(Exception.class, executable);
    }
}
