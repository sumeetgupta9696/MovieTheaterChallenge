package theater;

import exception.SeatReservationException;

import java.util.*;

public class MovieTheater {
    private Map<Character, LinkedList<Integer>> seatsInRow = new HashMap<>();
    private List<Character> rowIds = new ArrayList<>();
    private final int ROWS = 10;
    private final int SEATS_IN_ROW = 20;
    private final int SEAT_BUFFER = 3;
    private Character frontRow = 'A';
    private int availableSeats;
    private StringBuilder seatingAssignments;
    private boolean printedId = false;

    /**
     * Constructor to build movie theater object
     */
    public MovieTheater() {
        for (int i = 'A'; i < frontRow + ROWS; i++) {
            rowIds.add((char)(i));
        }
        for (char rowId : rowIds) {
            seatsInRow.put(rowId, new LinkedList<>());
            LinkedList<Integer> rowList = seatsInRow.get(rowId);
            for (int seat = 0; seat < SEATS_IN_ROW; seat++) {
                rowList.add(seat + 1);
            }
        }
        seatingAssignments = new StringBuilder();
        availableSeats = ROWS * SEATS_IN_ROW;
    }

    /**
     * Function to find a row with numSeats
     * @param reservationId the id of the reservation
     * @param numSeats the number of seats that need to be allocated
     * @return returns whether a row was found
     */
    public boolean findRow(String reservationId, int numSeats) {
        boolean foundRow = false;
        LinkedList<Integer> chosenRow = null;

        for (int row = ROWS - 1; row >= 0; row--) {
            Character rowId = rowIds.get(row);
            chosenRow = seatsInRow.get(rowId);
            if (chosenRow.size() >= numSeats) {
                int seatsRemoved = 0;
                while (seatsRemoved++ < numSeats) {
                    printSeat(rowId, reservationId, chosenRow);
                    printedId = true;
                    chosenRow.removeFirst();
                }
                foundRow = true;
                break;
            }
        }

        if (foundRow) {
            removeBuffer(chosenRow);    // Remove buffer seats
        }

        return foundRow;
    }

    /**
     * Function that adds seat for a specific reservation to final output
     * @param rowId the row we are reserving
     * @param reservationId the id of the reservation
     * @param row the LinkedList that stores the row
     */
    public void printSeat(Character rowId, String reservationId, LinkedList<Integer> row) {
        String seatNumber = String.valueOf(SEATS_IN_ROW - row.size() + 1);
        if (printedId) {
            seatingAssignments.append(rowId + seatNumber + ",");
        } else {
            seatingAssignments.append(reservationId + " " + rowId + seatNumber + ",");
        }
    }

    /**
     * Removes seats from row to account for buffer
     * @param row the LinkedList that stores the row
     */
    public void removeBuffer(LinkedList<Integer> row) {
        // Remove buffer seats
        int bufferSeats = 0;
        while (!row.isEmpty() && bufferSeats++ < SEAT_BUFFER) {
            row.removeFirst();
        }
    }

    /**
     * Finds any available seat starting from the back row
     * @param reservationId the id of the reservation
     * @param numSeats the number of seats that need to be allocated
     */
    public void findAnySeats(String reservationId, int numSeats) {
        int seatsLeft = numSeats; // Seats left to allocate

        // Consider rows with most available seats first

        List<Character> largestRows = new ArrayList<>();

        for (char rowId : rowIds) {
            largestRows.add(rowId);
        }

        Collections.sort(largestRows, Comparator.comparingInt(id -> (seatsInRow.get(id).size())));

        // Allocate seats wherever possible starting from the back
        for (int row = ROWS - 1; row >= 0; row--) {
            Character rowId = largestRows.get(row);
            LinkedList<Integer> currentRow = seatsInRow.get(rowId);
            while (seatsLeft > 0 && !currentRow.isEmpty()) {
                printSeat(rowId, reservationId, currentRow);
                printedId = true;
                currentRow.removeFirst();
                seatsLeft--;
            }
        }
    }


    /**
     * Parent function that allocates seats for a reservation
     * @param reservationId the id of the reservation
     * @param numSeats the number of seats that need to be allocated
     * @throws SeatReservationException
     */
    public void allocateSeats(String reservationId, int numSeats) throws SeatReservationException {
        if (numSeats > availableSeats) {
            throw new SeatReservationException("Not enough seats available to make reservation.");
        } else if (numSeats == 0) {
            throw new SeatReservationException("Can't reserve zero seats. Please try again with a number greater" +
                    " than zero.");
        }

        printedId = false;
        boolean foundRow = false;
        if (numSeats <= SEATS_IN_ROW) {
            foundRow = findRow(reservationId, numSeats);
        }

        if (!foundRow) {
            findAnySeats(reservationId, numSeats);
        }

        availableSeats -= numSeats;
        seatingAssignments.setLength(seatingAssignments.length() - 1);
        seatingAssignments.append("\n");
    }

    public StringBuilder getSeatingAssignments() {
        return seatingAssignments;
    }
}
