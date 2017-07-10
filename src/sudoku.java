import javax.swing.JFileChooser;
import java.io.File;
import java.util.*;

/**
 * Created by neil on 7/5/17.
 */
public class sudoku {

    //create 2d array that represents the 16x16 world
   cell[][] world_array = new cell[15][15];


    //class to contain all info for each variable
    public class cell {

        String value;
        List constraints;
        int num_of_constraints;
        int attempted_assignments;
        int cell_col;
        int cell_row;

        //default constructor
        public cell() {
            value = "";
            constraints = null;
            num_of_constraints = constraints.size();
            attempted_assignments = 0;
        }
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
    
    //find cell with most constraints / minimum remaining number of values in its domain
    public cell mrv_hueristic() {
        cell mrv_cell = new cell;
        int mrv = 0;
        for (cell[] array1: world_array) {            
            for (cell value: array1) {
                if (cell.constraints.size() > mrv) {
                    mrv = cell.constraints.size();
                    mrv_cell = cell;
                }
            }
        }
        return cell;
    }    
    
    //make inferences based on value assignment to a cell
    public void inferences(cell cell) {
        inference_row = cell.row;
        inference_col = cell.col;
        inference_val = cell.value;
        
        //remove from all cells in row.  Remove assigned value from domain
        for (int i = 0; i <= 15; i++) {
            world_array[inference_row][i].constraints.remove(inference_val);
        }
        
        //remove from all cells in col.  Remove assigned value from domain
        for (int i = 0; i <= 15; i++) {
            world_array[i][inference_col].constraints.remove(inference_val);   
        }
        
        //remove from all cells in box.  Remove assigned value from domain
        int row_offset = (inference_row / 4) * 4;        
        int col_offset = (inference_col / 4) * 4;
        for (int i = 0; i < 4; i++) {            
            for (int j = 0; j < 4; j++) {
                world_array[row_offset + i][col_offset + j].constraints.remove(inference_val);
            }
        }
        
    }
    
    //@param - a cell
    //@return - array with all cells that share a domain with input cell
    public arrray shared_space(cell cell) {
            
    }

    public static void main(String[] args) {

    }
}



As you're reading in data from text file, assign row and col values to each cell.
int row_reset = 0;
while (reader.hasNextLine()) { 
    String[] = nextLine(); 
    for (int i = 0;, i < String[].length; i++) {  
        world_array[row_reset][i] = value;  
        world_array[row_reset][i].row = row_reset;  
        world_array[row_reset][i].col = i; 
    } 
    row_reset++;
}
