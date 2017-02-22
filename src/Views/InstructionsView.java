package Views;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 16/02/17.
 */
public class InstructionsView extends JPanel {
    private ArrayList<JLabel> instructionLabels;

    public InstructionsView() {
        instructionLabels = new ArrayList<>();
    }

    public InstructionsView(List<String> instructions) {
        for(String instruction : instructions) {
            int index = instructionLabels.size();
            instructionLabels.add(new JLabel(String.valueOf(index+1) + instruction));
        }

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (JLabel instruction : instructionLabels) {
            this.add(instruction);
        }
    }

    public void addInstruction(String instruction) {
        int index = instructionLabels.size();
        instructionLabels.add(new JLabel(index + 1 + instruction));
        this.add(instructionLabels.get(index));
    }

}
