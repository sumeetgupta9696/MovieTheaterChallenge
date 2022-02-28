package service;

import exception.SeatReservationException;
import theater.MovieTheater;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MovieTheaterSeatingService {
    private MovieTheater theater;

    public MovieTheaterSeatingService() {
        theater = new MovieTheater();
    }

    public void runService(String inputFilePath, String outputFilePath) throws IOException {
        processInputFile(inputFilePath);
        writeToOutputFile(outputFilePath);
    }

    public void processInputFile(String inputFilePath) throws IOException {
        System.out.println("CURRENT PATH: " + System.getProperty("user.dir"));
        List<String> lines = Files.readAllLines(Paths.get(inputFilePath));
        for (String line : lines) {
            String[] tokens = line.split(" ");
            String reservationId = tokens[0];
            int numSeats = Integer.parseInt(tokens[1]);
            allocateSeats(reservationId, numSeats);
        }
    }

    public void writeToOutputFile(String outputFilePath) throws IOException {
        File file = new File(outputFilePath);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(theater.getSeatingAssignments().toString());
        } finally {
            if (writer != null) writer.close();
        }
    }

    private void allocateSeats(String reservationId, int numSeats) {
        try {
            theater.allocateSeats(reservationId, numSeats);
        } catch (SeatReservationException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
