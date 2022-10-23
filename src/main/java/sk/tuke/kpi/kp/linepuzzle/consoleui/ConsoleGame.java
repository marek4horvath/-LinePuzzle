package sk.tuke.kpi.kp.linepuzzle.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.kpi.kp.linepuzzle.core.*;
import sk.tuke.kpi.kp.linepuzzle.entity.Comment;
import sk.tuke.kpi.kp.linepuzzle.entity.Score;
import sk.tuke.kpi.kp.linepuzzle.service.*;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConsoleGame extends Game {
    private final Scanner scanner = new Scanner(System.in);
    private static final Pattern COMMANT_PATTERN = Pattern.compile("([1-5]) ([1-5]) ([1-5]) ([1-5])");

    private GameState state;
    private boolean isProblem = false;

    // private ScoreService scoreService = new ScoreServiceJDBC();
   //  private CommentService commentService = new CommentServiceJDBC();
    // private RatingService ratingService = new RatingServiceJDBC();
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;

    private String confirmation;
    private LinkedList<Level> levels;

    public ConsoleGame(LinkedList<Level> levels) {
        super(levels);
        this.levels = levels;
        System.out.print(SettingBoard.RED);
        System.out.print(GAME_NAME);
        System.out.print(SettingBoard.RESET);
        System.out.println();
       /* scoreService.reset();
        commentService.reset();
        ratingService.reset();*/
    }

    @Override
    protected Player createNewPlayer(Board board) {
        System.out.print(SettingBoard.CYAN);
        System.out.print("Enter a player name: ");
        System.out.print(SettingBoard.RESET);
        final String playerName = scanner.nextLine();
        return new Player(playerName, board);
    }

    @Override
    public void render() {
        printRating();
        printTopScores();
        System.out.print(SettingBoard.Purple);
        System.out.println(String.format("%s %s", "Level:", currentLevel.getLevelName()));
        System.out.print(SettingBoard.RESET);
        System.out.println();
        renderBoard(this.getBoard());
        renderBoard(getPlayer().getBoard());
        processInput();
    }

    @Override
    public void renderDone(Long time, Integer points) {
        if (time == null) {
            System.out.print(SettingBoard.Purple);
            System.out.println("It not exist next levels. Good bay.");
            System.out.print(SettingBoard.RESET);
            comment();
            raiting();
            printComment();
            printRating();
            printTopScores();
            return;
        }
        renderBoard(this.getBoard());
        renderBoard(getPlayer().getBoard());
        System.out.print(SettingBoard.GREEN);
        System.out.println("Victory, your time is " + time + " s. Went you play next level enter Y or no N.");
        System.out.print(SettingBoard.RESET);
        confirmation = scanner.nextLine().toUpperCase();
        renderPointsByStars(points);

        if ("Y".equals(confirmation)) {
            this.setPlaying();
        } else if ("N".equals(confirmation)) {
            comment();
            raiting();
            printRating();
            printComment();
            printTopScores();
        } else {
            System.err.print("You have entered an invalid command !!!");
            renderDone(time, points);
        }

    }

    private void renderPointsByStars(Integer points) {
        String starts = "";
        switch (points) {
            case 30:
                starts = " *** ";
                break;
            case 20:
                starts = " ** ";
                break;
            case 10:
                starts = " * ";
                break;
            case 0:
                starts = "You haven't received a single star";
                break;
        }
        if (points >= 10) {
            System.out.print(SettingBoard.YELLOW);
        } else {
            System.out.print(SettingBoard.RED);
        }
        System.out.println(starts);
        System.out.print(SettingBoard.RESET);
    }

    public void renderBoard(Board board) {
        final Character[][] consoleBoard = new Character[board.getWidth() * 2][board.getHeight() * 2];
        Node node;
        for (int row = 0; row < board.getWidth(); row++) {
            for (int col = 0; col < board.getHeight(); col++) {
                node = board.getNode(row, col);
                if (node.getState() == NodeState.ACTIV) {
                    consoleBoard[row * 2][col * 2] = SettingBoard.NODE_ACTIV;
                    consoleBoard[row * 2][col * 2 + 1] = SettingBoard.SPACE;
                } else {
                    consoleBoard[row * 2][col * 2] = SettingBoard.NODE;
                    consoleBoard[row * 2][col * 2 + 1] = SettingBoard.SPACE;
                }
            }
            for (int col = 0; col < board.getHeight(); col++) {
                consoleBoard[row * 2 + 1][col * 2] = SettingBoard.SPACE;
                consoleBoard[row * 2 + 1][col * 2 + 1] = SettingBoard.SPACE;
            }
        }
        addGraphToConsoleBoard(consoleBoard, board);
        printConsoleBoard(consoleBoard);
    }

    private void printConsoleBoard(Character[][] consoleBoard) {
        System.out.print(String.format("%c", SettingBoard.SPACE));
        System.out.print(String.format("%c", SettingBoard.SPACE));
        for (int col = 1; col < consoleBoard.length; col++) {
            if (col <= 5) {
                System.out.print(SettingBoard.RED);
                System.out.print(col);
                System.out.print(String.format("%c", SettingBoard.SPACE));
                System.out.print(SettingBoard.RESET);
            }
        }
        System.out.println();
        for (int row = 0; row < consoleBoard.length; row++) {
            if (row % 2 == 0) {
                if (row + 1 == 1) {
                    System.out.print(SettingBoard.RED);
                    System.out.print(row + 1);
                    System.out.print(String.format("%c", SettingBoard.SPACE));
                } else if (row + 1 == 3) {
                    System.out.print(SettingBoard.RED);
                    System.out.print(row);
                    System.out.print(String.format("%c", SettingBoard.SPACE));
                } else if (row + 1 == 5) {
                    System.out.print(SettingBoard.RED);
                    System.out.print(row - 1);
                    System.out.print(String.format("%c", SettingBoard.SPACE));
                } else if (row + 1 == 7) {
                    System.out.print(SettingBoard.RED);
                    System.out.print(row - 2);
                    System.out.print(String.format("%c", SettingBoard.SPACE));
                } else if (row + 1 == 9) {
                    System.out.print(SettingBoard.RED);
                    System.out.print(row - 3);
                    System.out.print(String.format("%c", SettingBoard.SPACE));
                }
                System.out.print(SettingBoard.RESET);
            } else {
                System.out.print(String.format("%c", SettingBoard.SPACE));
                System.out.print(String.format("%c", SettingBoard.SPACE));
            }
            for (int col = 0; col < consoleBoard[row].length; col++) {
                if (consoleBoard[row][col] == SettingBoard.NODE_ACTIV) {
                    System.out.print(SettingBoard.GREEN);
                    System.out.printf(consoleBoard[row][col] + "");
                    System.out.print(SettingBoard.RESET);
                } else {
                    System.out.printf(consoleBoard[row][col] + "");
                }
            }
            System.out.println();
        }
    }


    private void addGraphToConsoleBoard(Character[][] consoleBoard, Board board) {
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
                            this.isProblem = false;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            this.isProblem = true;
                        }
                }
                if (node.getPositionY() == nextNode.getPositionY()) {
                    if (node.getState() == NodeState.ACTIV && nextNode.getState() == NodeState.ACTIV)
                        try {
                            verticalLines(node.getPositionX(), node.getPositionY(), nextNode.getPositionX(), nextNode.getPositionY(), consoleBoard);
                            this.isProblem = false;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            this.isProblem = true;
                        }
                }
                if ((node.getPositionX() > nextNode.getPositionX() && node.getPositionY() > nextNode.getPositionY()) || (node.getPositionX() < nextNode.getPositionX() && node.getPositionY() < nextNode.getPositionY())) {//
                    if (node.getState() == NodeState.ACTIV && nextNode.getState() == NodeState.ACTIV)
                        try {
                            leftLines(node.getPositionX(), node.getPositionY(), nextNode.getPositionX(), nextNode.getPositionY(), consoleBoard);
                            this.isProblem = false;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            this.isProblem = true;
                        }
                }
                if ((node.getPositionY() <= nextNode.getPositionY() && node.getPositionX() > nextNode.getPositionX()) || (node.getPositionY() >= nextNode.getPositionY() && node.getPositionX() < nextNode.getPositionX())) {
                    if (node.getState() == NodeState.ACTIV && nextNode.getState() == NodeState.ACTIV) {
                        try {
                            rightLine(node.getPositionX(), node.getPositionY(), nextNode.getPositionX(), nextNode.getPositionY(), consoleBoard);
                            this.isProblem = false;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            this.isProblem = true;
                        }
                    }
                }
            }
            idx++;
        }
    }

    private void rightLine(int nodeX, int nodeY, int nextNodeX, int nextNodeY, Character[][] consoleBoard) {
        if (nodeX < nextNodeX) {
            if (nextNodeY != nodeY) {
                int i = (nodeX * 2) + 1;
                int j = (nodeY * 2) - 1;
                do {
                    consoleBoard[i][j] = '/';
                    i++;
                    j--;
                } while (consoleBoard[i][j] != SettingBoard.NODE_ACTIV);//
            }
        } else if (nodeX > nextNodeX) {
            if (nextNodeY != nodeY) {
                int i = (nodeX * 2) - 1;
                int j = (nodeY * 2) + 1;
                do {
                    consoleBoard[i][j] = '/';
                    i--;
                    j++;
                } while (consoleBoard[i][j] != SettingBoard.NODE_ACTIV);
            }
        }

    }

    private void leftLines(int nodeX, int nodeY, int nextNodeX, int nextNodeY, Character[][] consoleBoard) {
        if (nodeX > nextNodeX) {
            int i = (nodeX * 2) - 1;
            int j = (nodeY * 2) - 1;
            do {
                consoleBoard[i][j] = '\\';
                i--;
                j--;
            } while (consoleBoard[i][j] != SettingBoard.NODE_ACTIV);
        } else if (nodeX < nextNodeX) {
            int i = (nodeX * 2) + 1;
            int j = (nodeY * 2) + 1;
            do {
                consoleBoard[i][j] = '\\';
                i++;
                j++;

                if (consoleBoard.length == 10) {
                    throw new ArrayIndexOutOfBoundsException("You have entered incorrect coordinates");
                }
            } while (consoleBoard[i][j] != SettingBoard.NODE_ACTIV);

        }
    }

    private void horizontalLines(int nodeX, int nodeY, int nextNodeX, int nextNodeY, Character[][] consoleBoard) {
        if (nodeY < nextNodeY) {
            int i = (nodeY * 2) + 1;
            do {
                consoleBoard[nodeX * 2][i] = '-';
                i++;
            } while (nextNodeY != i / 2);

        } else if (nodeY > nextNodeY) {
            int i = (nodeY * 2) - 1;
            do {
                consoleBoard[nodeX * 2][i] = '-';
                i--;
            } while (nextNodeY != (i + 1) / 2);
        }
    }

    private void verticalLines(int nodeX, int nodeY, int nextNodeX, int nextNodeY, Character[][] consoleBoard) {
        if (nodeX < nextNodeX) {
            int i = (nodeX * 2) + 1;
            do {
                consoleBoard[i][nodeY * 2] = '|';
                i++;
            } while (nextNodeX != i / 2);
        } else if (nodeX > nextNodeX) {
            int i = (nodeX * 2) - 1;
            do {
                consoleBoard[i][nodeY * 2] = '|';
                i--;
            } while (nextNodeX != (i + 1) / 2);
        }
    }

    private void processInput() {
        if (isProblem) {
            System.out.print(SettingBoard.YELLOW);
            System.out.println("You have entered incorrect coordinates, take a step back with b");
            System.out.print(SettingBoard.RESET);
            isProblem = false;
        }

        isProblem = false;
        System.out.println();
        System.out.print(SettingBoard.CYAN);
        System.out.print("Enter the command (coordinate: X, Y of the active node and coordinate of the inactive node: X, Y | exit: x | back: b): ");
        System.out.print(SettingBoard.RESET);
        String line = scanner.nextLine().toUpperCase();

        if ("X".equals(line)) {
            state = GameState.TOGIVEUP;
            return;
        }
        if ("B".equals(line)) {
            getPlayer().back();
            return;
        }

        Matcher matcher = COMMANT_PATTERN.matcher(line);

        if (matcher.matches()) {
            int row = Integer.parseInt(matcher.group(1)) - 1;
            int column = Integer.parseInt(matcher.group(2)) - 1;
            int row1 = Integer.parseInt(matcher.group(3)) - 1;
            int column1 = Integer.parseInt(matcher.group(4)) - 1;
            try {
                getPlayer().splitNodesByPositions(row, column, row1, column1);
            } catch (NoSuchElementException e) {
                System.out.print(SettingBoard.RED);
                System.out.println("You have entered incorrect coordinates");
                System.out.print(SettingBoard.RESET);
                processInput();
            }
        } else {
            System.err.print("You have entered an invalid command !!!");
            System.out.println();
            System.out.println();
            System.out.println();
            isProblem = true;
        }
    }

    @Override
    public GameState exitGame() {
        return state;
    }

    @Override
    public boolean isProblem() {
        return isProblem;
    }

    @Override
    public void printTopScores() {
        List<Score> scores = scoreService.getTopScores(GAME_NAME);
        if (!scores.isEmpty()) {
            System.out.format("---------------------------------------------------------------------------------%n");
            System.out.println("Scores");
            System.out.format("---------------------------------------------------------------------------------%n");
            System.out.println();
            String leftAlignFormat = "| %-4d            | %-15s | %-4d     | %-15s          |%n";
            System.out.print(SettingBoard.BLUE);
            System.out.format("+-----------------+-----------------+----------+--------------------------------+%n");
            System.out.format("| ID              |  Player name    | Points   | Playedon                       |%n");
            System.out.format("+-----------------+-----------------+----------+--------------------------------+%n");
            int id = 1;
            for (Score score : scores) {
                System.out.format(leftAlignFormat, id, score.getPlayer(), score.getPoints(), score.getPlayedon());
                System.out.format("+-----------------+-----------------+----------+--------------------------------+%n");
                id++;
            }
            System.out.println(SettingBoard.RESET);
        }
    }

    @Override
    public void printComment() {
        List<Comment> comments = commentService.getComments(GAME_NAME);
        if (!comments.isEmpty()) {
            int id = 1;
            System.out.format("--------------------------------------------------------------------%n");
            System.out.println("Comments");
            System.out.format("--------------------------------------------------------------------%n");
            System.out.println();
            String leftAlignFormat = "| %-4d            | %-15s | %-15s    | %-15s                | %-15s        |%n";
            System.out.print(SettingBoard.BLUE);
            System.out.format("+-----------------+-----------------+--------------------+--------------------------------+--------------------------------+%n");
            System.out.format("| ID              |  Player name    | Game               | Comment                        |  CommentedOn                   |%n");
            System.out.format("+-----------------+-----------------+--------------------+--------------------------------+--------------------------------+%n");
            for (Comment comment : comments) {
                System.out.format(leftAlignFormat, id, comment.getPlayer(), comment.getGame(), comment.getComment(), comment.getCommentedOn());
                System.out.format("----------------------------------------------------------------------------------------------------------------------------%n");
                id++;
            }
            System.out.print(SettingBoard.RESET);
        }
    }

    private void printRating() {
        int rating_avg = ratingService.getAverageRating(GAME_NAME);
        System.out.println("\n------------------------------");
        System.out.print("Rating Average: ");
        System.out.println(rating_avg);
        System.out.println("------------------------------");
    }

    private void comment() {
        System.out.println();
        System.out.print(SettingBoard.CYAN);
        System.out.print("Enter a comment ");
        System.out.print(SettingBoard.RESET);
        writeComment(scanner.nextLine());
    }

    private void raiting() {
        System.out.println();
        System.out.print(SettingBoard.CYAN);
        System.out.print("Enter a raiting ");

        int rating = -1;
        while (rating < 0 || rating > 5) {
            System.out.print(SettingBoard.CYAN);
            System.out.println("Pleas rating (0-5) for game " + GAME_NAME + ":");
            try {
                rating = this.scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.next();
            }
            if (rating < 0 || rating > 5) {
                System.out.println("\n------------------------------");
                System.out.print(SettingBoard.RED);
                System.out.println("Invalid rating  (Min: 0) - (Max: 5)");
            }
            System.out.print(SettingBoard.RESET);
        }
        writeRating(rating);
    }


}