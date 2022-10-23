package sk.tuke.kpi.kp.linepuzzle.server.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.kpi.kp.linepuzzle.consoleui.SettingBoard;
import sk.tuke.kpi.kp.linepuzzle.core.*;
import sk.tuke.kpi.kp.linepuzzle.entity.Score;
import sk.tuke.kpi.kp.linepuzzle.service.ScoreService;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("/linepuzzle")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class LinePuzzleController {
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private UserController userController;

    private Board board;
    private Player player;
    private LinkedList<Level> levels;
    private Level actualLevel;
    private GameState state=GameState.DONE;;
    private boolean isProblem = false;
    private Long startedTime;
    private Long endedTime;
    private int activNodey;
    private int activNodex;
    private int inactivNodey;
    private int inactivNodex;
    private String win;
    private int points = -1;
    private String namePlayer;
    private long deff;

    @PostConstruct
    public void postconsturct() {
        setLevelsAndBorderd();
        if (!isWin() && userController.isLogged()) {
            startedTime = System.currentTimeMillis();
        } else {
            startedTime = (long) 0;
        }
    }

    @RequestMapping
    public String linePuzzle(@RequestParam(required = false) String row, @RequestParam(required = false) String col, @RequestParam(required = false) String back, @RequestParam(required = false) String newGame, Model model) throws InterruptedException, IOException, ServletException {
        isProblem = false;
        back(back);
        setGraphLines(row, col);
        newGame(newGame);
        fillModel(model);
        if(!userController.isLogged() && state == GameState.DONE){
            setLevelsAndBorderd();
            startedTime = startedTime-(long)5;
            state=GameState.PLAYING;
            namePlayer=null;
        }
        return "linepuzzle";
    }

    private void setGraphLines(String row, String col) {
        if (row != null && col != null) {
            Node node = player.getBoard().getNode(Integer.parseInt(row) - 1, Integer.parseInt(col) - 1);
            if (node.getState() == NodeState.ACTIV) {
                activNodey = Integer.parseInt(row);
                activNodex = Integer.parseInt(col);
            } else if (node.getState() == NodeState.INACTIV) {
                inactivNodey = Integer.parseInt(row);
                inactivNodex = Integer.parseInt(col);
            }
            if (activNodex != 0 && activNodey != 0 && inactivNodex != 0 && inactivNodey != 0) {
                try {
                    player.splitNodesByPositions(activNodex - 1, activNodey - 1, inactivNodex - 1, inactivNodey - 1);
                    isProblem = false;
                } catch (NoSuchElementException e) {
                    isProblem = true;
                }
                activNodex = 0;
                activNodey = 0;
                inactivNodex = 0;
                inactivNodey = 0;
            }
        }
        if (isWin()) {
            if (levels.isEmpty()) {
                win = "vihral si";
                score();
            } else {
                actualLevel = levels.pollFirst();
                board = new Board(actualLevel.getSizeX(), actualLevel.getSizeY(), actualLevel.getGameGraph());
                player = new Player(getPlayerName(), new Board(actualLevel.getSizeX(), actualLevel.getSizeY(), actualLevel.getPlayerGraph()));
            }
        }

    }

    public void back(String back) {
        if (back != null) {
            if (!isWin())
                player.back();
        }
    }

    public void newGame(String newGame) {
        if (newGame != null) {
            points = -1;
            activNodex = 0;
            activNodey = 0;
            inactivNodex = 0;
            inactivNodey = 0;
            setLevelsAndBorderd();
        }
    }

    private void score() {
        if (isWin()) {
            endedTime = System.currentTimeMillis();
            long time = endedTime - startedTime;
            deff = TimeUnit.MILLISECONDS.toSeconds(time);
            points = getPoints(deff);
            if (userController.isLogged()) {
                writeScore(points);
            }
        } else {
            endedTime = (long) 0;
        }
    }

    private int getPoints(Long timeSecund) {
        if (timeSecund <= 10 && timeSecund != 0) {
            return 30;
        } else if (timeSecund <= 20 && timeSecund >= 10) {
            return 20;
        } else if (timeSecund <= 30 && timeSecund >= 20) {
            return 10;
        } else {
            return 0;
        }
    }

    public boolean isWin() {
        final Graph playerGraph = player.getBoard().getGraph();
        final Graph gameGraph = board.getGraph();
        return gameGraph.isEquals(playerGraph);
    }

    public void writeScore(int points) {
        scoreService.addScore(new Score("Line Puzzle", this.player.getName(), points, new Date()));
    }

    private void fillModel(Model model) {
        if (points != -1) {
            model.addAttribute("scores", points);
        }
    }

    public String getNameLevel() {
        return actualLevel.getLevelName();
    }

    public String getPlayerName() {
        return player.getName();
    }

    //funkcia ktora vrati hracju tabulu do web-oveho prostredia
    public String getHtmlFromBoard() {
        return getHtmlBoarad(board);
    }

    //funkcia ktora vrati hracovu tabulu do web-oveho prostredia
    public String getHtmlForPlayer() {
        return getHtmlBoarad(player.getBoard());
    }

    //funkcia na renderovanie board pre web-ove prostredie
    private void renderBoardHtml(Board board, Character[][] consoleBoard) {
        Node node;
        for (int row = 0; row < board.getWidth(); row++) {
            for (int col = 0; col < board.getHeight(); col++) {
                node = board.getNode(row, col);
                if (node.getState() == NodeState.ACTIV) {
                    consoleBoard[row * 2][col * 2] = 'a';
                    consoleBoard[row * 2][col * 2 + 1] = SettingBoard.SPACE;
                } else {
                    consoleBoard[row * 2][col * 2] = 'n';
                    consoleBoard[row * 2][col * 2 + 1] = SettingBoard.SPACE;
                }
            }
            for (int col = 0; col < board.getHeight(); col++) {
                consoleBoard[row * 2 + 1][col * 2] = SettingBoard.SPACE;
                consoleBoard[row * 2 + 1][col * 2 + 1] = SettingBoard.SPACE;
            }
        }
    }

    private String getHtmlBoarad(final Board board) {
        final Character[][] consoleBoard = new Character[board.getWidth() * 2][board.getHeight() * 2];
        renderBoardHtml(board, consoleBoard);
        addGraphToWebBoard(consoleBoard, board);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<table  class=\"playerBoard\"\n");
        for (int row = 0; row < consoleBoard.length; row++) {
            stringBuffer.append("<tr>\n");
            for (int col = 0; col < consoleBoard[row].length; col++) {
                stringBuffer.append("<td>");
                if (consoleBoard[row][col] != ' ') {//consoleBoard[row][col] == 'a' || consoleBoard[row][col] == 'n' &&
                    if (consoleBoard[row][col] == 'r') {
                        stringBuffer.append("<hr style=\"width: 50px;transform: rotate(-41deg);\" width=\"50%\" size=\"10\" color=\"Red\" align=\"left\">");
                    } else if (consoleBoard[row][col] == 'l') {
                        stringBuffer.append("<hr style=\"width: 50px;transform: rotate(-138deg);\" width=\"50%\" size=\"10\" color=\"Red\" align=\"left\">");
                    } else if (consoleBoard[row][col] == 'h') {
                        stringBuffer.append("<hr style=\"width: 50px;transform: rotate(-180deg);\" width=\"50%\" size=\"10\" color=\"Red\" align=\"left\">");
                    } else if (consoleBoard[row][col] == 'v') {
                        stringBuffer.append("<hr style=\"width: 41px;transform: rotate(-270deg);\" width=\"50%\" size=\"10\" color=\"Red\" align=\"left\">");
                    } else {
                        int a = row;
                        int b = col;
                        if (a == 0) {
                            a = a + 1;
                        } else if (a == 4) {
                            a = a - 1;
                        } else if (a == 6) {
                            a = a - 2;
                        } else if (a == 8) {
                            a = a - 3;
                        }
                        if (b == 0) {
                            b = b + 1;
                        } else if (b == 4) {
                            b = b - 1;
                        } else if (b == 6) {
                            b = b - 2;
                        } else if (b == 8) {
                            b = b - 3;
                        }
                        if (!isWin())
                            stringBuffer.append(String.format("<a href='/linepuzzle?row=%d&col=%d'>\n", a, b));
                        stringBuffer.append("<img src='/images/" + consoleBoard[row][col] + ".svg' style=\"width: 32px;height: 32px; margin: auto; display: block;\">");
                        if (!isWin())
                            stringBuffer.append("</a>\n");
                    }
                } else {
                    stringBuffer.append("<img src='/images/" + consoleBoard[row][col] + ".svg' style=\"width: 32px;height: 32px; margin: auto; visibility: hidden;\">");
                }
                stringBuffer.append("</td>\n");
            }
            stringBuffer.append("</tr>\n");
        }
        stringBuffer.append("</table>\n");
        return stringBuffer.toString();
    }

    //funkcia na pridavanie grafu do bordra
    private void addGraphToWebBoard(Character[][] consoleBoard, Board board) {
        if (consoleBoard == null || board == null)
            throw new NullPointerException("Illegal setting ConsoleGame");
        int idx = 0;
        for (Node node : board.getGraph().getNodes()) {
            if (idx < board.getGraph().getNodes().size() - 1) {//-1
                final Node nextNode = board.getGraph().getNodes().get(idx + 1);//+1
                if (node == null || nextNode == null)
                    throw new NullPointerException("Illegal setting ConsoleGame");
                if (node.getPositionX() == nextNode.getPositionX()) {
                    if (node.getState() == NodeState.ACTIV && nextNode.getState() == NodeState.ACTIV)
                        try {
                            horizontalLines(node.getPositionX(), node.getPositionY(), nextNode.getPositionX(), nextNode.getPositionY(), consoleBoard);
                            isProblem = false;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            isProblem = true;
                        }
                }
                if (node.getPositionY() == nextNode.getPositionY()) {
                    if (node.getState() == NodeState.ACTIV && nextNode.getState() == NodeState.ACTIV)
                        try {
                            verticalLines(node.getPositionX(), node.getPositionY(), nextNode.getPositionX(), nextNode.getPositionY(), consoleBoard);
                            isProblem = false;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            isProblem = true;
                        }
                }
                if ((node.getPositionX() > nextNode.getPositionX() && node.getPositionY() > nextNode.getPositionY()) || (node.getPositionX() < nextNode.getPositionX() && node.getPositionY() < nextNode.getPositionY())) {//
                    if (node.getState() == NodeState.ACTIV && nextNode.getState() == NodeState.ACTIV)
                        try {
                            leftLines(node.getPositionX(), node.getPositionY(), nextNode.getPositionX(), nextNode.getPositionY(), consoleBoard);
                            isProblem = false;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            isProblem = true;
                        }
                }
                if ((node.getPositionY() <= nextNode.getPositionY() && node.getPositionX() > nextNode.getPositionX()) || (node.getPositionY() >= nextNode.getPositionY() && node.getPositionX() < nextNode.getPositionX())) {
                    if (node.getState() == NodeState.ACTIV && nextNode.getState() == NodeState.ACTIV) {
                        try {
                            rightLine(node.getPositionX(), node.getPositionY(), nextNode.getPositionX(), nextNode.getPositionY(), consoleBoard);
                            isProblem = false;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            isProblem = true;
                        }
                    }
                }
            }
            idx++;
        }
    }

    //funkcie na overenie ciar
    private void rightLine(int nodeX, int nodeY, int nextNodeX, int nextNodeY, Character[][] consoleBoard) {
        if (nodeX < nextNodeX) {
            if (nextNodeY != nodeY) {
                int i = (nodeX * 2) + 1;
                int j = (nodeY * 2) - 1;
                do {
                    consoleBoard[i][j] = 'r';
                    i++;
                    j--;
                } while (consoleBoard[i][j] != 'a');//
            }
        } else if (nodeX > nextNodeX) {
            if (nextNodeY != nodeY) {
                int i = (nodeX * 2) - 1;
                int j = (nodeY * 2) + 1;
                do {
                    consoleBoard[i][j] = 'r';
                    i--;
                    j++;
                } while (consoleBoard[i][j] != 'a');
            }
        }

    }

    private void leftLines(int nodeX, int nodeY, int nextNodeX, int nextNodeY, Character[][] consoleBoard) {
        if (nodeX > nextNodeX) {
            int i = (nodeX * 2) - 1;
            int j = (nodeY * 2) - 1;
            do {
                consoleBoard[i][j] = 'l';
                i--;
                j--;
            } while (consoleBoard[i][j] != 'a');
        } else if (nodeX < nextNodeX) {
            int i = (nodeX * 2) + 1;
            int j = (nodeY * 2) + 1;
            do {
                consoleBoard[i][j] = 'l';
                i++;
                j++;

                if (consoleBoard.length == 10) {
                    throw new ArrayIndexOutOfBoundsException("You have entered incorrect coordinates");
                }
            } while (consoleBoard[i][j] != 'a');

        }
    }

    private void horizontalLines(int nodeX, int nodeY, int nextNodeX, int nextNodeY, Character[][] consoleBoard) {
        if (nodeY < nextNodeY) {
            int i = (nodeY * 2) + 1;
            do {
                consoleBoard[nodeX * 2][i] = 'h';
                i++;
            } while (nextNodeY != i / 2);

        } else if (nodeY > nextNodeY) {
            int i = (nodeY * 2) - 1;
            do {
                consoleBoard[nodeX * 2][i] = 'h';
                i--;
            } while (nextNodeY != (i + 1) / 2);
        }
    }

    private void verticalLines(int nodeX, int nodeY, int nextNodeX, int nextNodeY, Character[][] consoleBoard) {
        if (nodeX < nextNodeX) {
            int i = (nodeX * 2) + 1;
            do {
                consoleBoard[i][nodeY * 2] = 'v';
                i++;
            } while (nextNodeX != i / 2);
        } else if (nodeX > nextNodeX) {
            int i = (nodeX * 2) - 1;
            do {
                consoleBoard[i][nodeY * 2] = 'v';
                i--;
            } while (nextNodeX != (i + 1) / 2);
        }
    }
    ///

    public boolean isProblem() {
        return isProblem;
    }

    private void setLevelsAndBorderd() {
        levels = new LibraryLevels();
        actualLevel = levels.pollFirst();
        board = new Board(actualLevel.getSizeX(), actualLevel.getSizeY(), actualLevel.getGameGraph());
        player = new Player(getNamePlayer(), new Board(actualLevel.getSizeX(), actualLevel.getSizeY(), actualLevel.getPlayerGraph()));// levels.get(1).getPlayerGraph()

    }

    public String getNamePlayer() {
        if (userController.isLogged()) {
            namePlayer = userController.getLoggedUser().getLogin();
        }
        return namePlayer;
    }

    public String getGenerationRandomGuest() {
        Random rand = new Random();
        int number = rand.nextInt(100000);
        return "Guest" + String.valueOf(number);
    }

}
