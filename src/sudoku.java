import javax.swing.JFileChooser;
import java.io.File;
import java.util.*;

/**
 * Created by neil on 7/5/17.
 */
public class sudoku {

    //create 2d array that represents the 16x16 world
   cell[][] world_array = new cell[15][15];

    public static void main(String[] args) {

    }

    //check to see if all variables have been assigned a value
    public boolean assignment_complete() {
        for (cell[] array1: world_array) {
            for (cell value: array1) {
                if (value.value == null) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    //check to see if assignment of variable is valid i.e. no other occurences in line or square
    public boolean assignment_valid(int row, int col, cell cell) {
        //check for duplicate values in rows
        for (int i = 0; i < 15; i++) {
            if (cell.value == world_array[row][i].value && cell != world_array[row][i]) {
                return false;
            }
        }

        //check for duplicat values in cols
        for (int i = 0; i < 15; i++) {
            if (cell.value == world_array[i][col].value && cell != world_array[i][col]) {
                return false;
            }
        }

        //offset for boxes.
        int row_offset = (row / 4) * 4;
        int col_offset = (col / 4) * 4;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (cell.value == world_array[row_offset + i][col_offset + j].value && cell != world_array[row_offset + i][col_offset + j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public cell select_unassigned_variable() {
        //TODO find variable with minimum remaining possible values
    }

    //class to contain all info for each variable
    public class cell {

        String value;
        List constraints;
        int num_of_constraints;
        int attempted_assignments;

        //default constructor
        public cell() {
            value = "";
            constraints = null;
            num_of_constraints = constraints.size();
            attempted_assignments = 0;
        }
    }
}
