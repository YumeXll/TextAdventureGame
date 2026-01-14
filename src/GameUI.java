import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class GameUI {

    private final GameEngine engine = new GameEngine();

    private JFrame frame;
    private JTextArea textArea;
    private JPanel optionsPanel;
    private JLabel statusLabel;

    public GameUI() {
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    private void createAndShowGUI() {
        frame = new JFrame("Adventure Game - Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 450);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(0, 1, 6, 6));

        statusLabel = new JLabel("HP: 0 | Weapon: - | Potions: 0");

        JPanel rightPanel = new JPanel(new BorderLayout(8,8));
        rightPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        rightPanel.add(optionsPanel, BorderLayout.SOUTH);

        frame.getContentPane().setLayout(new BorderLayout(8,8));
        frame.getContentPane().add(statusLabel, BorderLayout.NORTH);
        frame.getContentPane().add(rightPanel, BorderLayout.CENTER);

        // Prompt for name and start
        String name = JOptionPane.showInputDialog(frame, "Enter your name:", "Welcome", JOptionPane.PLAIN_MESSAGE);
        if (name == null) name = "Adventurer";
        engine.reset(name);

        refreshUI();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void refreshUI() {
        SwingUtilities.invokeLater(() -> {
            StringBuilder sb = new StringBuilder();
            sb.append(engine.getSceneText()).append("\n\n");
            // additional dynamic info for some scenes
            if (engine.getCurrentScene() == GameEngine.Scene.FIGHT) {
                sb.append("Your HP: ").append(engine.getPlayerHP()).append(" | ");
                sb.append("Weapon: ").append(engine.getWeapon()).append(" | ");
                sb.append("Potions: ").append(engine.getPotions()).append("\n");
            }
            textArea.setText(sb.toString());

            // status
            statusLabel.setText(String.format("HP: %d    Weapon: %s    Potions: %d", engine.getPlayerHP(), engine.getWeapon(), engine.getPotions()));

            // options
            optionsPanel.removeAll();
            List<String> opts = engine.getOptions();
            for (int i = 0; i < opts.size(); i++) {
                String label = opts.get(i);
                JButton b = new JButton(label);
                final int idx = i;
                b.addActionListener((ActionEvent e) -> {
                    engine.chooseOption(idx);
                    // If scene is ENDING and option chosen is Exit, close app
                    if (engine.getCurrentScene() == GameEngine.Scene.ENDING && label.toLowerCase().contains("exit")) {
                        frame.dispose();
                        return;
                    }
                    refreshUI();
                });
                optionsPanel.add(b);
            }

            optionsPanel.revalidate();
            optionsPanel.repaint();
        });
    }

    public static void main(String[] args) {
        new GameUI();
    }
}
