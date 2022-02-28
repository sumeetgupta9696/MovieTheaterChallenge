package service;

import exception.SeatReservationException;
import theater.MovieTheater;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.List;

public class MovieTheaterSeatingService {
    private MovieTheater theater;

    public MovieTheaterSeatingService() {
        theater = new MovieTheater();
    }


    /**
     * Function to run service for given file paths.
     * @param inputFilePath input file path to read from
     * @param outputFilePath output file path to write to
     * @throws IOException
     * @throws InvalidPathException
     */
    public void runService(String inputFilePath, String outputFilePath) throws IOException, InvalidPathException {
        if (inputFilePath.isEmpty()) {
            throw new InvalidPathException(inputFilePath, "Input file path is invalid.");
        }
        processInputFile(inputFilePath);
        writeToOutputFile(outputFilePath);
        if (!outputFilePath.isEmpty()) {
            System.out.println("Output written to file: " + outputFilePath);
        } else {
            System.out.println("Output written to file: MovieTheaterSeating/output.txt");
        }
    }

    /**
     * Function to process given input file path
     * @param inputFilePath input file path to read from
     * @throws IOException
     */
    public void processInputFile(String inputFilePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(inputFilePath));
        for (String line : lines) {
            String[] tokens = line.split(" ");
            String reservationId = tokens[0];
            int numSeats = Integer.parseInt(tokens[1]);
            allocateSeats(reservationId, numSeats);
        }
    }

    /**
     * Function to write final seating arrangement
     * @param outputFilePath output file path to write to
     * @throws IOException
     */
    public void writeToOutputFile(String outputFilePath) throws IOException {
        if (outputFilePath.isEmpty()) {
            outputFilePath = "output.txt";
        }
        File file = new File(outputFilePath);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(theater.getSeatingAssignments().toString());
        } finally {
            if (writer != null) writer.close();
        }
    }

    /**
     * Function to allocate numSeats for a specific reservation
     * @param reservationId The id of the reservation
     * @param numSeats The number of seats that need to be allocated
     */
    private void allocateSeats(String reservationId, int numSeats) {
        try {
            theater.allocateSeats(reservationId, numSeats);
        } catch (SeatReservationException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
