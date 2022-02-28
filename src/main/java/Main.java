import service.MovieTheaterSeatingService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputFilePath = args[0];
        String outputFilePath = args[1];
        MovieTheaterSeatingService movieService = new MovieTheaterSeatingService();
        System.out.println("inputFilePath: " + inputFilePath);
        System.out.println("outputFilePath: " + outputFilePath);
        movieService.runService(inputFilePath, outputFilePath);
    }
}
