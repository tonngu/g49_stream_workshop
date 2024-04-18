package se.lexicon.vxo.service;


import org.junit.jupiter.api.Test;
import se.lexicon.vxo.model.Gender;
import se.lexicon.vxo.model.Person;
import se.lexicon.vxo.model.PersonDto;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Your task is to make all tests pass (except task1 because its non-testable).
 * However, you have to solve each task by using a java.util.Stream or any of its variance.
 * You also need to use lambda expressions as implementation to functional interfaces.
 * (No Anonymous Inner Classes or Class implementation of functional interfaces)
 */
public class StreamExercise {

    private static List<Person> people = People.INSTANCE.getPeople();

    /**
     * Turn integers into a stream then use forEach as a terminal operation to print out the numbers
     */
    @Test
    public void task1() {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        //integers.stream().forEach(System.out::println);  //1. using method reference

        integers.stream().forEach(integer -> System.out.println(integer));  //2. using lambda

    }

    /**
     * Turning people into a Stream count all members
     */
    @Test
    public void task2() {
        long amount = 0;

        //TODO:Write code here
        amount = people.stream().count();
        assertEquals(10000, amount);
    }

    /**
     * Count all people that has Andersson as lastName.
     */
    @Test
    public void task3() {
        long amount = 0;
        int expected = 90;

        //TODO:Write code here
        amount = people.stream().filter(person -> person.getLastName().equals("Andersson")).count();
        assertEquals(expected, amount);
    }

    /**
     * Extract a list of all female
     */
    @Test
    public void task4() {
        int expectedSize = 4988;
        List<Person> females = null;

        //TODO:Write code here
        females = people.stream().filter(person -> person.getGender() == Gender.FEMALE).collect(Collectors.toList());

        assertNotNull(females);
        assertEquals(expectedSize, females.size());
    }

    /**
     * Extract a TreeSet with all birthDates
     */
    @Test
    public void task5() {
        int expectedSize = 8882;
        Set<LocalDate> dates = null;

        //TODO:Write code here

        dates = people.stream().
                map(Person::getDateOfBirth)
                .collect(Collectors.toCollection(TreeSet::new));


        assertNotNull(dates);
        assertTrue(dates instanceof TreeSet);
        assertEquals(expectedSize, dates.size());
    }

    /**
     * Extract an array of all people named "Erik"
     */
    @Test
    public void task6() {
        int expectedLength = 3;

        Person[] result = null;

        //TODO:Write code here
        result = people.stream().filter(person -> person.getFirstName().equals("Erik")).toArray(Person[]::new);

        assertNotNull(result);
        assertEquals(expectedLength, result.length);
    }

    /**
     * Find a person that has id of 5436
     */
    @Test
    public void task7() {
        Person expected = new Person(5436, "Tea", "Håkansson", LocalDate.parse("1968-01-25"), Gender.FEMALE);

        Optional<Person> optional = null;

        //TODO:Write code here
        optional = people.stream().filter(person -> person.getPersonId() == 5436).findFirst();

        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());
    }

    /**
     * Using min() define a comparator that extracts the oldest person i the list as an Optional
     */
    @Test
    public void task8() {
        LocalDate expectedBirthDate = LocalDate.parse("1910-01-02");

        Optional<Person> optional = null;

        //TODO:Write code here
        optional = people.stream().min(Comparator.comparing(Person::getDateOfBirth));

        assertNotNull(optional);
        assertEquals(expectedBirthDate, optional.get().getDateOfBirth());
    }

    /**
     * Map each person born before 1920-01-01 into a PersonDto object then extract to a List
     */
    @Test
    public void task9() {
        int expectedSize = 892;
        LocalDate date = LocalDate.parse("1920-01-01");

        List<PersonDto> dtoList = null;

        //TODO:Write code here
        dtoList = people.stream()
                .filter(person -> person.getDateOfBirth().isBefore(date))
                .map(person -> new PersonDto(person.getPersonId(), person.getFirstName() + " " + person.getLastName()))
                .collect(Collectors.toList());

        assertNotNull(dtoList);
        assertEquals(expectedSize, dtoList.size());
    }

    /**
     * In a Stream Filter out one person with id 5914 from people and take the birthdate and build a string from data that the date contains then
     * return the string.
     */
    @Test
    public void task10() {
        String expected = "WEDNESDAY 19 DECEMBER 2012";
        int personId = 5914;

        Optional<String> optional = null;

        //TODO:Write code here
        optional = people.stream()
                .filter(person -> person.getPersonId() == 5914)
                .map(person -> {
                    LocalDate birthDate = person.getDateOfBirth();
                    String dayOfWeek = birthDate.getDayOfWeek().toString();
                    int dayOfMonth = birthDate.getDayOfMonth();
                    String month = birthDate.getMonth().toString();
                    int year = birthDate.getYear();
                    return dayOfWeek.toUpperCase() + " " + dayOfMonth + " " + month.toUpperCase() + " " + year;
                })
                .findFirst();

        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());
    }

    /**
     * Get average age of all People by turning people into a stream and use defined ToIntFunction personToAge
     * changing type of stream to an IntStream.
     */
    @Test
    public void task11() {
        ToIntFunction<Person> personToAge =
                person -> Period.between(person.getDateOfBirth(), LocalDate.parse("2019-12-20")).getYears();
        double expected = 54.42;
        double averageAge = 0;

        //TODO:Write code here
        averageAge = people.stream().
                mapToInt(personToAge)
                .average().getAsDouble();

        // This is suggesting me to use orElseThrow  because getAsDouble() can result in null error


        assertTrue(averageAge > 0);
        assertEquals(expected, averageAge, .01);
    }

    /**
     * Extract from people a sorted string array of all firstNames that are palindromes. No duplicates
     */
    @Test
    public void task12() {
        String[] expected = {"Ada", "Ana", "Anna", "Ava", "Aya", "Bob", "Ebbe", "Efe", "Eje", "Elle", "Hannah", "Maram", "Natan", "Otto"};

        String[] result = null;

        //TODO:Write code here
        result = people.stream()
                .filter(person -> new StringBuilder(person.getFirstName()).reverse().toString().equalsIgnoreCase(person.getFirstName()))
                .map(Person::getFirstName)
                .distinct()
                .sorted()
                .toArray(String[]::new);

        assertNotNull(result);
        assertArrayEquals(expected, result);
    }

    /**
     * Extract from people a map where each key is a last name with a value containing a list of all that has that lastName
     */
    @Test
    public void task13() {
        int expectedSize = 107;
        Map<String, List<Person>> personMap = null;

        //TODO:Write code here
        personMap = people.stream().collect(Collectors.groupingBy(Person::getLastName));

        assertNotNull(personMap);
        assertEquals(expectedSize, personMap.size());
    }

    /**
     * Create a calendar using Stream.iterate of year 2020. Extract to a LocalDate array
     */
    @Test
    public void task14() {
        LocalDate[] _2020_dates = null;

        //TODO:Write code here
        LocalDate date = LocalDate.of(2020, 01, 01);
        _2020_dates = Stream.iterate(date, d -> d.isBefore(LocalDate.of(2021, 01, 01)), d -> d.plusDays(1))
                .toArray(LocalDate[]::new);

        //Stream.iterate(a, b, c) takes a seed 'a' and continually produces an output according to some logic 'c'
        //until hasNext on the condition 'b' fails and returns false
        //'a' and 'c' must be of the same data type while 'b' not necessarily so
        assertNotNull(_2020_dates);
        assertEquals(366, _2020_dates.length);
        assertEquals(LocalDate.parse("2020-01-01"), _2020_dates[0]);
        assertEquals(LocalDate.parse("2020-12-31"), _2020_dates[_2020_dates.length - 1]);
    }

}
