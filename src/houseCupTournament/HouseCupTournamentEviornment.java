 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package houseCupTournament;

import audio.AudioPlayer;
import environment.Environment;
import environment.LocationValidatorIntf;
import grid.Grid;
import images.ResourceTools;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author Shayla
 */
class HouseCupTournamentEviornment extends Environment implements GridDrawData, LocationValidatorIntf {

    Grid grid;
    private HarryPotter harryPotter;
    private Score score;
    private int level;
    private final int HEAD_POSITION = 0;

    private final int SLOW_SPEED = 3;
    private final int MEDIUM_SPEED = 2;
    private final int HIGH_SPEED = 1;

    private int slowSpeed = SLOW_SPEED;
    private int mediumSpeed = MEDIUM_SPEED;
    private int highSpeed = HIGH_SPEED;
    private int moveDelayCounter = 0;

    private ArrayList<GridObject> gridObjects;
    private Image segmentImage;

    private Image guitar;
    private Image musicNote;
    private GameState gameState = GameState.PLAYING;

    public HouseCupTournamentEviornment() {
    }

    @Override
    public void initializeEnvironment() {
        score = new Score();
        score.setPosition(new Point(10, 50));
        guitar = ResourceTools.loadImageFromResource("resources/guitar.jpeg");
        musicNote = ResourceTools.loadImageFromResource("resources/MusicNote.png");

//        this.setBackground(ResourceTools.loadImageFromResource("resources/DragonChallenge.jpg").getScaledInstance(900, 525, Image.SCALE_FAST));
        setLevel(1);
        grid = new Grid(35, 20, 25, 25, new Point(10, 20), Color.RED);

        harryPotter = new HarryPotter();
        harryPotter.setDirection(Direction.DOWN);
        harryPotter.setDrawData(this);
        harryPotter.setLocationValidator(this);

        ArrayList<Point> body = new ArrayList<>();
        body.add(new Point(3, 1));
        body.add(new Point(3, 2));
        body.add(new Point(2, 2));
        body.add(new Point(2, 3));

        harryPotter.setBody(body);

        gridObjects = new ArrayList<>();
//        gridObjects.add(new GridObject(GridObjectType.MUSIC_NOTE, getRandomPoint()));
        gridObjects.add(new GridObject(GridObjectType.GUITAR, getRandomPoint()));
        AudioPlayer.play("/resources/AccioGuitar.wav");

    }

    public void timerTaskHandler() {
        if (harryPotter != null) {
            //if counter > = limit then reset counter and move snake
            //else increment counter
            if (this.moveDelayCounter >= this.slowSpeed) {
                moveDelayCounter = 0;
                harryPotter.move();
            } else {
                moveDelayCounter++;
            }

        }

    }

    @Override
    public void keyPressedHandler(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_N) {
            grid.setShowCellCoordinates(!grid.getShowCellCoordinates());

        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            harryPotter.setDirection(Direction.UP);

        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            harryPotter.setDirection(Direction.DOWN);

        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            harryPotter.setDirection(Direction.LEFT);

        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            harryPotter.setDirection(Direction.RIGHT);
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            harryPotter.togglePaused();
        } else if (e.getKeyCode() == KeyEvent.VK_G) {
            harryPotter.grow(2);
        } else if (e.getKeyCode() == KeyEvent.VK_M) {
            AudioPlayer.play("/resources/HeyDragon.wav");
        } else if (e.getKeyCode() == KeyEvent.VK_1) {
            setLevel(1);
        } else if (e.getKeyCode() == KeyEvent.VK_2) {
            setLevel(2);
        } else if (e.getKeyCode() == KeyEvent.VK_3) {
            setLevel(3);
        }

    }

    @Override
    public void keyReleasedHandler(KeyEvent e) {

    }

    @Override
    public void environmentMouseClicked(MouseEvent e) {
    }

    @Override
    public void paintEnvironment(Graphics graphics) {
        switch (gameState) {
            case MENU:

                break;
            case PLAYING:

                if (grid != null) {
//            grid.paintComponent(graphics);
                }

                if (harryPotter != null) {
                    harryPotter.draw(graphics);
                }

                if (gridObjects != null) {
                    for (GridObject gridObject : gridObjects) {
                        if (gridObject.getType() == GridObjectType.GUITAR) {
                            graphics.drawImage(guitar, grid.getCellSystemCoordinate(gridObject.getLocation()).x,
                                    grid.getCellSystemCoordinate(gridObject.getLocation()).y,
                                    getCellWidth(), getCellHeight(), this);

                        } else if (gridObject.getType() == GridObjectType.MUSIC_NOTE) {
                            graphics.drawImage(musicNote, grid.getCellSystemCoordinate(gridObject.getLocation()).x,
                                    grid.getCellSystemCoordinate(gridObject.getLocation()).y,
                                    getCellWidth(), getCellHeight(), this);

                        }

                    }
                }
                
                if (score != null) {
                    score.draw(graphics);
                }

                break;

            case OVER:

        }
    }

    public Point getRandomPoint() {
//        return new Point((int) (grid.getRows() * Math.random()), (int) (grid.getColumns() * Math.random()));
        return new Point((int) (grid.getColumns() * Math.random()), (int) (grid.getRows() * Math.random()));
    }

//<editor-fold defaultstate="collapsed" desc="GridDrawData Interface">
    @Override
    public int getCellHeight() {
        return grid.getCellHeight();

    }

    @Override
    public int getCellWidth() {
        return grid.getCellWidth();
    }

    @Override
    public Point getCellSystemCoordinate(Point cellCoordinate) {
        return grid.getCellSystemCoordinate(cellCoordinate);
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="LocationValidatorIntf">
    @Override
    public Point validateLocation(Point point) {

        harryPotter.selfHit();
        if (point.x >= this.grid.getColumns()) {
            point.x = 0;
        } else if (point.x < 0) {
            point.x = this.grid.getColumns() - 1;
        }
        if (point.y >= this.grid.getRows()) {
            point.y = 0;
        } else if (point.y < 0) {
            point.y = this.grid.getRows() - 1;
        }

        //check if the snake hit a GridObhect, then take appropriate action
        //Guitar sound
        //Music notes grow snake
        //look at all the locations stored in the gridObject
        //for each compare it to the head location stored
        //in the point parameter
        int newNotes = 0;
//                    
///       for (int i = 0; i < newNotes; i++) {
//            gridObjects.add(new GridObject(GridObjectType.MUSIC_NOTE, getRandomPoint()));
//        }
// 
        for (GridObject object : gridObjects) {
            if (object.getLocation().equals(point)) {
                System.out.println("Hit = " + object.getType());
                HarryPotter HarryPotter = null;
//                object.getLocation().x = -200;
                if (object.getType() == GridObjectType.GUITAR) {
//                    object.setLocation(this.getRandomPoint());
                    object.getLocation().move(1000000000, 1000000000);
                    AudioPlayer.play("/resources/HeyDragon.wav");
                    newNotes++;
                } else if (object.getType() == GridObjectType.MUSIC_NOTE) {
                    harryPotter.grow(1);
                    object.setLocation(this.getRandomPoint());
                    this.score.addToValue(5);

                }
//                  

            }
        }

        for (int i = 0; i < newNotes; i++) {
            gridObjects.add(new GridObject(GridObjectType.MUSIC_NOTE, getRandomPoint()));

        }

        // check if the snake hit itself!!!!!
        return point;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {

        if (this.level != level) {
            if (level == 1) {
                this.setBackground(ResourceTools.loadImageFromResource("resources/DragonChallenge.jpg").getScaledInstance(900, 525, Image.SCALE_FAST));

            } else if (level == 2) {
                this.setBackground(ResourceTools.loadImageFromResource("resources/BallroomFloor.jpeg").getScaledInstance(900, 525, Image.SCALE_FAST));
                AudioPlayer.play("/resources/GrangerDanger.wav");

                if (harryPotter != null) {
//if counter > = limit then reset counter and move snake
//else increment counter
                    if (this.moveDelayCounter >= this.mediumSpeed) {
                        moveDelayCounter = 0;
                        harryPotter.move();
                    } else {
                        moveDelayCounter++;
                    }

                }

            } else if (level == 3) {
                this.setBackground(ResourceTools.loadImageFromResource("resources/Graveyard.jpeg").getScaledInstance(900, 525, Image.SCALE_FAST));
                AudioPlayer.play("/resources/DanceAgain.wav");

                if (harryPotter != null) {
                    //if counter > = limit then reset counter and move snake
                    //else increment counter
                    if (this.moveDelayCounter >= this.highSpeed) {
                        moveDelayCounter = 0;
                        harryPotter.move();
                    } else {
                        moveDelayCounter++;
                    }

                }
            }
        }

        this.level = level;

    }

    private GridObjectType getClass(HarryPotter harryPotter) {
        this.harryPotter = HarryPotter.java;
        return null;

    }

    /**
     * @return the GameState
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * @param GameState the GameState to set
     */
    public void setGameState(GameState GameState) {
        this.gameState = GameState;
    }

}

//</editor-fold>

