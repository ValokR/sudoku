import javax.swing.*;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by neil on 7/5/17.
 */
public class sudoku extends file_selector {

    //create 2d array that represents the 16x16 world
    cell[][] world_array = new cell[15][15];

    public static void main(String[] args) {
        JFrame frame = new JFrame("file_selector");
        frame.setContentPane(new file_selector().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        file.file_to_array();
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
        cell mrv_cell = new cell();
        int mrv = 0;
        for (cell[] array1 : world_array) {
            for (cell value : array1) {
                if (value.constraints.size() > mrv) {
                    mrv = value.constraints.size();
                    mrv_cell = value;
                }
            }
        }
        return mrv_cell;
    }

    //make inferences based on value assignment to a cell
    public void inferences(cell cell) {
        int inference_row = cell.row;
        int inference_col = cell.col;
        String inference_val = cell.value;

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

    public void file_to_array(File file) throws IOException {
        try {
            String line;
            InputStream in_stream = new FileInputStream(file);
            InputStreamReader in_stream_reader = new InputStreamReader(in_stream, Charset.defaultCharset());
            BufferedReader buffered_reader = new BufferedReader(in_stream_reader);

            int line_count = 0;
            while ((line = buffered_reader.readLine()) != null) ;
            {
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    String value = Character.toString(c);
                    cell in_cell = new cell(value, i, line_count);
                    world_array[line_count][i] = in_cell;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    //@param - a cell
    //@return - array with all cells that share a domain with input cell
    // public array shared_space(cell cell) {
//
    //}

    //class to contain all info for each variable
    public class cell {

        String value;
        int col;
        int row;
        ArrayList constraints;
        int attempted_assignments;


        //default constructor
        public cell() {
            constraints = null;
            attempted_assignments = 0;
        }

        //constructor
        public cell(String value, int col, int row) {
            this.value = value;
            this.col = col;
            this.row = row;
            constraints = null;
            attempted_assignments = 0;
        }
    }
}