import exception.SeatReservationException;
import org.junit.Before;
import org.junit.Test;
import service.MovieTheaterSeatingService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class MovieTheaterServiceTests {
    private MovieTheaterSeatingService service;

    @Before
    public void setUp() {
        service = new MovieTheaterSeatingService();
    }

    @Test
    public void testRunInvalidInputPath() {
        String fileInputPath = "";
        Throwable exception = assertThrows(InvalidPathException.class,
                () -> service.runService(fileInputPath, ""));
        assertEquals("Input file path is invalid.:" + " ", exception.getMessage());
    }

    @Test
    public void testRequestingZeroSeats() throws IOException {
        String fileInputPath = "src/test/data/ZeroSeatsInput.txt";
        String fileOutputPath = "src/test/data/ZeroSeatsOutput.txt";
        service.runService(fileInputPath, fileOutputPath);
        String expected = "";
        Path path = Paths.get(fileOutputPath);
        String contents = "";
        try {
            contents = Files.readString(path, StandardCharsets.ISO_8859_1);
        } catch (IOException ex) {}
        assertEquals(expected, contents);
    }

    @Test
    public void testRequestingAllSeats() throws IOException {
        String fileInputPath = "src/test/data/AllSeatsInput.txt";
        String fileOutputPath = "src/test/data/AllSeatsOutput.txt";
        service.runService(fileInputPath, fileOutputPath);
        String expectedOutput = "R001 ";
        for (int start = 'J'; start >= 'A'; start--) {
            for (int seat = 1; seat <= 20; seat++) {
                expectedOutput = expectedOutput + ((char)start + String.valueOf(seat)) + ",";
            }
        }
        Path path = Paths.get(fileOutputPath);
        String contents = null;
        try {
            contents = Files.readString(path, StandardCharsets.ISO_8859_1);
        } catch (IOException ex) {}
        expectedOutput = expectedOutput.substring(0, expectedOutput.length() - 1) + "\n";
        assertEquals(expectedOutput, contents);
    }

    @Test
    public void testRequestingMoreThanAllSeats() throws IOException {
        String fileInputPath = "src/test/data/MoreThanAllSeatsInput.txt";
        String fileOutputPath = "src/test/data/MoreThanAllSeatsOutput.txt";
        service.runService(fileInputPath, fileOutputPath);
        String expectedOutput = "";
        Path path = Paths.get(fileOutputPath);
        String contents = null;
        try {
            contents = Files.readString(path, StandardCharsets.ISO_8859_1);
        } catch (IOException ex) {}
        assertEquals(expectedOutput, contents);
    }

    @Test
    public void testNormalReservation() throws IOException {
        String fileInputPath = "src/test/data/NormalReservationInput.txt";
        String fileOutputPath = "src/test/data/NormalReservationOutput.txt";
        service.runService(fileInputPath, fileOutputPath);
        String second = "R002 I1,I2,I3,I4,I5,I6,I7,I8,I9,I10,I11,I12,I13,I14,I15,I16\n";
        String expectedOutput = "R001 J1,J2,J3,J4,J5" + "\n" + second;
        Path path = Paths.get(fileOutputPath);
        String contents = null;
        try {
            contents = Files.readString(path, StandardCharsets.ISO_8859_1);
        } catch (IOException ex) {}
        assertEquals(expectedOutput, contents);
    }
}
