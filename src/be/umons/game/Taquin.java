package be.umons.game;

import be.umons.graph.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import static be.umons.game.Action.*;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static javax.swing.SwingUtilities.invokeLater;

public class Taquin extends JFrame implements ActionListener {

    private static final String PATH = "Chemin parcouru";
    private static final String QUIT = "Quitter";
    private static final String GAME = "Jeu";
    private static final String NEW = "Nouveau";;
    
    private static final int BUTTON_SIZE = 80;
    private static final int COTE = 3;
    private static final int LOG_SIZE = 1000;
    private static final int LOG_ROTATION_COUNT = 1;

    private Node actualNode, nextNode;
    private JButton boardVector[];
    private JMenuBar barre_menu;
    private int emptySquare, borderUp, borderDown, borderLeft, borderRight;
    private Action currentAction;
    
    public Taquin() {
        super("Taquin");
        layoutInit();
        boardInit();
        do5Moves();
        actualNode = new Node(toString());
    }

    private void play(int clickedSquare) {
        currentAction = null;
        if (frontierAction(clickedSquare)) {
            updateLayout(boardVector,clickedSquare);
            updateFrontier(clickedSquare);
            emptySquare = clickedSquare;
            nextNode = new Node(toString());
            nextNode.updateGraph(actualNode, currentAction);
            try {
                actualNode = nextNode.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    private void showTraveledPath(Node actualNode) {
        System.out.println("showTraveledPath : ");
        if(actualNode.isRoot()) return;
        System.out.println("game sequence ["+actualNode.getTraveledPath()+"]");
        System.out.println(actualNode.toString());
        showTraveledPath(actualNode.getParent());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case NEW:
                new Taquin();
                break;

            case QUIT:
                System.exit(0);
                break;

            case PATH:
                showTraveledPath(actualNode);
                break;

            default:
                play(parseInt(e.getActionCommand()));
                break;
        }
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        List<JButton> vecteurPlateau = asList(boardVector);
        for (JButton bouton : vecteurPlateau)
            if (bouton != null) response.append(bouton.getText());
        return response.toString();
    }

    private void do5Moves() {
        Integer[] actions = new Integer[] {8,5,2,1,4};
        List<Integer> actionsMelangeJeu = asList(actions);
        for (Integer action : actionsMelangeJeu)
            if (frontierAction(action)) {
                updateLayout(boardVector,action);
                updateFrontier(action);
                emptySquare = action;
            }
    }

    public static void main(String[] args)	{
        Handler handler;
        try {
            handler = new FileHandler("actions.log", LOG_SIZE, LOG_ROTATION_COUNT);
            Logger.getLogger("").addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        invokeLater(new Runnable() {public void run() {new Taquin(); }});
    }

    private void boardInit() {
        if (emptySquare != 0 && emptySquare != 9) {
            boardVector[emptySquare].setVisible(true);
            boardVector[emptySquare].setText(boardVector[9].getText());
        }
        emptySquare = 9;
        borderUp = 6;
        borderLeft = 8;
        borderRight = borderDown = 0;
        boardVector[9].setVisible(false);
    }

    private void updateFrontier(int clickedSquare) {
        borderDown = clickedSquare + COTE;
        borderUp = clickedSquare - COTE;

        if (leftBorderLimit(clickedSquare)) borderLeft = 0;
        else borderLeft = clickedSquare - 1;

        if (rightBorderLimit(clickedSquare)) borderRight = 0;
        else borderRight = clickedSquare + 1;
    }

    private boolean leftBorderLimit(int clickedSquare) {
        return clickedSquare == 1 || clickedSquare == 4 || clickedSquare == 7;
    }

    private boolean rightBorderLimit(int clickedSquare) {
        return clickedSquare == 3 || clickedSquare == 6 || clickedSquare == 9;
    }

    private boolean frontierAction(int clickedSquare) {
        if (clickedSquare == borderDown) {
            currentAction = DOWN;
            return true;
        }
        if (clickedSquare == borderUp) {
            currentAction = UP;
            return true;
        }
        if (clickedSquare == borderLeft) {
            currentAction = LEFT;
            return true;
        }
        if (clickedSquare == borderRight) {
            currentAction = RIGHT;
            return true;
        }

        return false;
    }

    private void layoutInit() {
        setSize(250, 296);
        setLocation(100, 100);
        setVisible(true);
        getContentPane().setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boardVector = new JButton[10];
        int j = 0;
        for (int i = 1; i < boardVector.length; i++) {

            boardVector[i] = new JButton(String.valueOf(i));
            boardVector[i].addActionListener(this);
            boardVector[i].setActionCommand(String.valueOf(i));

            if ((i - 1) == COTE || (i - 1) == COTE*2 ) j++;

            boardVector[i].setBounds(((i - 1) % COTE) * BUTTON_SIZE, j * BUTTON_SIZE, BUTTON_SIZE, BUTTON_SIZE);
            getContentPane().add(boardVector[i]);
            barre_menu = new JMenuBar();
            JMenu menuGame = new JMenu(GAME);
            
            JMenuItem submenuNew = new JMenuItem(NEW);
            JMenuItem submenuPath = new JMenuItem(PATH);
            JMenuItem submenuQuit = new JMenuItem(QUIT);

            submenuNew.addActionListener(this);
            submenuPath.addActionListener(this);
            submenuQuit.addActionListener(this);

            menuGame.add(submenuNew);
            menuGame.add(submenuPath);
            menuGame.add(submenuQuit);
            barre_menu.add(menuGame);

            setJMenuBar(barre_menu);
        }
    }

    private void updateLayout(JButton[] boutons, int clickedSquare) {
        boutons[clickedSquare].setVisible(false);
        boutons[emptySquare].setText(boutons[clickedSquare].getText());
        boutons[emptySquare].setVisible(true);
    }

}
