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
     *
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

    public void printSeat(Character rowId, String reservationId, LinkedList<Integer> row) {
        String seatNumber = String.valueOf(SEATS_IN_ROW - row.size() + 1);
        if (printedId) {
            seatingAssignments.append(rowId + seatNumber + ",");
        } else {
            seatingAssignments.append(reservationId + " " + rowId + seatNumber + ",");
        }
    }

    public void removeBuffer(LinkedList<Integer> row) {
        // Remove buffer seats
        int bufferSeats = 0;
        while (!row.isEmpty() && bufferSeats++ < SEAT_BUFFER) {
            row.removeFirst();
        }
    }

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


    public void allocateSeats(String reservationId, int numSeats) throws SeatReservationException {
        if (numSeats > availableSeats) {
            throw new SeatReservationException("Not enough seats available to make reservation.");
        }

        printedId = false;
        boolean foundRow = findRow(reservationId, numSeats);

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
