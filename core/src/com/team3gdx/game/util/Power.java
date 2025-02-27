package com.team3gdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.team3gdx.game.entity.Cook;
import com.team3gdx.game.screen.GameScreen;
import java.util.Stack;
import static com.team3gdx.game.screen.GameScreen.orderCards;

//this whole class done by pranshu dhungana
public class Power {
    public static int use;
    //in charge of handling different states of power ups
    private static String currentPower="None";
    private static Integer speedmultiplier = 2;
    private static Stack<PowerUnit> powerStack = new Stack<>();
    private static int max=3;
    private Power() {}

    private static void init(){}

    /**
     * pranshu dhungana
     * changes the speed of the cooks
     * @return
     */

    public static boolean Speed(){
        // we are now using speed
        PowerUnit temp= powerStack.peek();
        powerStack.pop(); //now its being used
        cookspeed(Float.valueOf(speedmultiplier));
        use=1; //stops continous usage
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                //Reset stats and current power after timer ends
                cookspeed(1/Float.valueOf(speedmultiplier)); //resets speed by dividing multiplier
                speedmultiplier = 2;
                System.out.println("Speed should be set invisible now" + currentPower);
                temp.setVisible(false);
                temp.dispose();
                currentPower = "None";
                use=1;

            }
        }, 15);
        return true;

    } //improves w

    /**
     * method to change speed depending on multiplier supplied
     * @param Multiplier
     * @return
     */
    private static boolean cookspeed(Float Multiplier){
        for(Cook cook:GameScreen.cooks){
            cook.setSpeed(Multiplier);
        }
        return true;
    }


    /**
     * The set a timer for using the instant generation power up. The main implementation done with the ingredient class
     * its just for setting flag and disposing of the ui and updating power stack.
     * @return
     */
    public static boolean recipe_complete() {
        PowerUnit temp= powerStack.peek();
        powerStack.pop(); //now its being used
        use=1; //stops continous usage
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                // Reset stats and current power after timer ends
                System.out.println("Instant should be set invisible now" + currentPower);
                temp.setVisible(false);
                temp.dispose();
                currentPower = "None";
                use=1;
            }
        }, 15);
        return true;
    } //instant generation , just handles the timer the rest is handled by ingredient poweruse check method

    /**
     * this method removes the orders on queue, it sets up the ui and gets rid of jacket potato
     * @return
     */
    public static boolean wipe() {
        PowerUnit temp= powerStack.peek();
        orderCards.clear();
        powerStack.pop();
        temp.setVisible(false);
        temp.dispose();
        return true;
    }        //wipes out current orders

    /**
     * gets current power
     * @return power
     */
    public static String getCurrentPower() {
        return currentPower;
    }

    /**
     * getter method returns the powerunit in case its needed
     * @return powerstack
     */
    public static PowerUnit getCurrentUnit(){
        return powerStack.pop();
    }

    /**
     * set flags and checks for powers and uses the top of the powerstack to set current power and call the relevant methods
     * @return boolean true if power is used , false otherwise
     */
    public static boolean usePower() {
        if (!isPowerEmpty()) {
            if (powerStack.peek().getPower() == 1) {
                currentPower = "Speed";
                Power.Speed();
                use=0;
                return true;
            } else if (powerStack.peek().getPower() == 2) {
                currentPower = "Instant"; // removes   order from place if there is an order there
                Power.recipe_complete();
                use=0;
                return true;
            } else if (powerStack.peek().getPower() == 3) {
                currentPower = "Wipe"; // wipes out an order
                Power.wipe();
                use=0;
                return true;
            }
        }
        return false;
    }

    /**
     * used to add power into stack, and sets up the ui generation
     * @param pow
     * @param batch
     */
    public static void addPower(Integer pow , SpriteBatch batch){
        Float y=820F;
        if(!isPowerFull() && isPowerEmpty()){
            //add texture to it
            Texture powerTexture= new  Texture(Gdx.files.internal("uielements/power"+pow+".png"));
            //new power unit with alligning
            Float x=200F;
            PowerUnit powerUnit = new PowerUnit(pow ,powerTexture , x,y);
            powerUnit.setVisible(true);
            powerStack.push(powerUnit); // push onto stack

        }
        else if(!isPowerFull() && !isPowerEmpty()){
            Float x= powerStack.peek().getX()+100F;
            Texture powerTexture= new  Texture(Gdx.files.internal("uielements/power"+pow+".png"));
            PowerUnit powerUnit = new PowerUnit(pow ,powerTexture , x,y);
            powerUnit.setVisible(true);
            powerStack.push(powerUnit); // push onto stack
            //render powerup
        }
    }

    /**
     * getter method for whole power stack
     * @return powerStack
     */
    public static Stack<PowerUnit> getPowerStack() {
        return powerStack;
    }

    /**
     * checks if there is powers in stack
     * @return
     */

    public static boolean isPowerEmpty(){
        return powerStack.isEmpty();
    }

    /**
     * returns power stack size
     * @return powerstack size
     */
    public static Integer powerSize(){
        return powerStack.size();
    }

    /**
     * used to check if maximum amount of power is used
     * @return boolean indicating if stack is full or not
     */

    public static boolean isPowerFull(){return powerStack.size()==max;}
}

