/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryborrowingsystem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Fine {

    private int overdueDays;
    private boolean isDamaged;
    private int totalFine;

    // Constructor
    public Fine(LocalDate dueDate, LocalDate returnDate, boolean isDamaged) {
        this.overdueDays = (int) ChronoUnit.DAYS.between(dueDate, returnDate);
        this.isDamaged = isDamaged;
        this.totalFine = 0;
    }

    // Method to calculate fine (no printing)
    public void calculateFine() {
        int finePerDay = 100;  // ₦100 per day late
        int damageFine = 500;  // ₦500 if damaged

        if (overdueDays > 0) {
            totalFine += overdueDays * finePerDay;
        }
        if (isDamaged) {
            totalFine += damageFine;
        }
    }

    // Getter for total fine
    public int getTotalFine() {
        return totalFine;
    }

    // Optional getters if you want to use them in the main class
    public int getOverdueDays() {
        return overdueDays;
    }

    public boolean isDamaged() {
        return isDamaged;
    }
}
