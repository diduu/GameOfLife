import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class App extends JFrame {

    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_DEPTH = 60;

    private static boolean isRunning = false;

    private Board board;
    private BoardView boardView;

    Timer timer;
    TimerTask timerTask;


    public App() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    public App(int depth, int width) {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be positive.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        setTitle("Game of Life");

        board = new Board(depth, width);
        boardView = new BoardView(width, depth);
        boardView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int[] cell = boardView.getCell(e.getX(), e.getY());
                board.setValueAt(cell[1], cell[0]);
                boardView.draw(board);
            }
        });

        start(10);

        JPanel root = new JPanel(new BorderLayout());
        root.add(boardView);
        root.add(footer(), BorderLayout.SOUTH);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null); // sets the app in the middle of the screen
        setVisible(true);
        boardView.draw(board);
    }

    public void start(int speed) {
        int turnPerSec = speed == 0 ? speed+1 : speed;

        if (timer != null) {
            timerTask.cancel();
            timer.cancel();
        }

        timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(isRunning)
                    nextTurn();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000/turnPerSec);
    }

    public void simulate(int numTurns) {
        for(int step = 0; step < numTurns; step++) {
            nextTurn();
        }
    }

    public void nextTurn() {
        board.nextTurn();
        boardView.draw(board);
    }

    public JPanel footer() {
        JPanel footer = new JPanel();
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clear());

        JButton runButton = new JButton("Run");
        runButton.addActionListener(e -> runStop(runButton));

        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
        sliderPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider slider = new JSlider(0, 10, 10);
   
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(5);
        slider.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent event){
                start(slider.getValue());
            }
        });

        sliderPanel.add(new JLabel("Running Speed"));
        sliderPanel.add(slider);

        footer.add(clearButton);
        footer.add(runButton);
        footer.add(sliderPanel);


        return footer;
    }

    private void runStop(JButton button) {
        if (isRunning) {
            button.setText("Run");
            isRunning = false;
        } else {
            button.setText("Stop");
            isRunning = true;
        }
    }

    private void clear() {
        board.clear();
        boardView.draw(board);
    }
}