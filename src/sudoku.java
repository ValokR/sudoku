import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by neil on 7/5/17.
 */
public class sudoku {

    static File myFile;
    //create 2d array that represents the 16x16 world
    public cell[][] world_array = new cell[15][15];
    ArrayList<String> world_constraints;


    public static void main(String[] args) throws IOException {

        JFrame frame = new JFrame("file_selector");
        file_selector fs = new file_selector();
        frame.setContentPane(fs.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        myFile = fs.getFile();

        while (myFile != null) {
            sudoku my_puzzle = new sudoku();
            my_puzzle.solve_puzzle();
        }
    }

    public void solve_puzzle() throws IOException {
        file_to_array(myFile);
        initialize_constraints();
        System.out.println(myFile.getName());

        if (assignment_complete() == true) {
            print_solution();
        } else {
            cell mrv_cell = mrv_hueristic();
            //get remaining possible values
            ArrayList<String> remaining_values = remaining_values(mrv_cell);

            for (String value : remaining_values) {
                cell temp_cell = mrv_cell;
                temp_cell.value = value;
                if (assignment_valid(temp_cell.row, temp_cell.col, temp_cell)) {
                    mrv_cell.value = value;
                    mrv_cell.attempted_assignments++;
                    ArrayList<pairing> inferences_list = inferences(mrv_cell);
                    if (inferences_list == null) {
                        undo_inferences(inferences_list);
                    } else {
                        solve_puzzle();
                    }
                }
                mrv_cell.value = null;
            }
        }
    }

    public void print_solution() {

        int num_assignments = 0;

        for (cell[] array1 : world_array) {
            System.out.println();
            for (cell value : array1) {
                System.out.print(value.value);
            }
        }

        for (cell[] array1 : world_array) {
            for (cell value : array1) {
                num_assignments += value.attempted_assignments;
            }
        }
        System.out.println("There was a total of " + num_assignments + " value assignments attempted");

    }

    public void initialize_constraints() {
        for (cell[] array1 : world_array) {
            for (cell value : array1) {
                inferences(value);
            }
        }
    }

    //check to see if all variables have been assigned a value
    public boolean assignment_complete() {
        for (cell[] array1 : world_array) {
            for (cell value : array1) {
                if (value.value == null) ;
                {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList remaining_values(cell cell) {
        ArrayList<String> remaining_values = null;
        for (int i = 0; i < cell.constraints.size(); i++) {
            if (!cell.constraints.contains(world_constraints.get(i))) {
                remaining_values.add(world_constraints.get(i));
            }
        }
        return remaining_values;
    }

    //check to see if assignment of variable is valid i.e. no other occurences in line or square
    public boolean assignment_valid(int row, int col, cell cell) {
        //check for duplicate values in rows
        for (int i = 0; i < 15; i++) {
            if (cell.value == world_array[row][i].value && cell != world_array[row][i]) {
                return false;
            }
        }


        //check for duplicate values in cols
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
    public ArrayList inferences(cell cell) {

        int inference_row = cell.row;
        int inference_col = cell.col;
        String inference_val = cell.value;
        ArrayList<pairing> inferences_list = null;

        //remove from all cells in row.  Remove assigned value from domain
        for (int i = 0; i <= 15; i++) {
            if (!world_array[inference_row][i].constraints.contains(inference_val)) {
                world_array[inference_row][i].constraints.add(inference_val);
                pairing my_pairing = new pairing();
                my_pairing.value = inference_val;
                my_pairing.cell = world_array[inference_row][i];
                inferences_list.add(my_pairing);
            }
        }

        //remove from all cells in col.  Remove assigned value from domain
        for (int i = 0; i <= 15; i++) {
            if (!world_array[i][inference_col].constraints.contains(inference_val)) {
                world_array[i][inference_col].constraints.add(inference_val);
                pairing my_pairing = new pairing();
                my_pairing.value = inference_val;
                my_pairing.cell = world_array[i][inference_col];
                inferences_list.add(my_pairing);
            }
        }

        //remove from all cells in box.  Remove assigned value from domain
        int row_offset = (inference_row / 4) * 4;
        int col_offset = (inference_col / 4) * 4;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!world_array[row_offset + i][col_offset + j].constraints.contains(inference_val)) {
                    world_array[row_offset + i][col_offset + j].constraints.add(inference_val);
                    pairing my_pairing = new pairing();
                    my_pairing.value = inference_val;
                    my_pairing.cell = world_array[row_offset + i][col_offset + j];
                    inferences_list.add(my_pairing);
                }
            }
        }

        //if any cell has 16 constraints, meaning it has no legal remaining values, set inferences_list to null and return
        for (cell[] array1 : world_array) {
            for (cell value : array1) {
                if (value.constraints.size() == 16) {
                    inferences_list = null;
                }
            }
        }
        return inferences_list;
    }

    public void undo_inferences(ArrayList inferences_list) {
        ArrayList<pairing> undo_list = inferences_list;
        for (pairing pairing : undo_list) {
            pairing.cell.constraints.remove(pairing.value);
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

                    //add value to world_constraints if not already a member of that arraylist
                    if (!world_constraints.contains(value) && value != "-") {
                        world_constraints.add(value);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //class to contain all info for each variable
    public class cell {

        String value = null;
        int col;
        int row;
        ArrayList<String> constraints;
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

    class pairing {
        cell cell;
        String value;
    }
}