package mars.mips.instructions.customlangs;
import mars.simulator.*;
import mars.mips.hardware.*;
import mars.mips.instructions.syscalls.*;
import mars.*;
import mars.util.*;
import java.util.*;
import java.io.*;
import mars.mips.instructions.*;
import java.util.Random;

public class Football extends CustomAssembly {
    private int down = 1;
    private int yardsToGo = 10;
    @Override
    public String getName(){
        return "Football";
    }

    @Override
    public String getDescription(){
        return "Football program that covers basic aspects of the game. " +
                "Has offensive, defensive, and special team calls. Can simulate different scenarios.\n";
    }

    @Override
    protected void populate(){

        instructionList.add(
                new BasicInstruction("audi",
                        "Prints The QB Called An Audible",
                        BasicInstructionFormat.R_FORMAT,
                        "000000 00000 00000 00000 00000 000001",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                SystemIO.printString("The QB Called An Audible\n");
                            }
                        }));

        instructionList.add(
                new BasicInstruction("blitz $t3",
                        "Loads -3 into $t3 and prints They Send A Blitz!",
                        BasicInstructionFormat.R_FORMAT,
                        "011111 00000 fffff 00000 00000 000000",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                int[] operands = statement.getOperands();
                                int blitz = -3;
                                RegisterFile.updateRegister(operands[0], blitz);
                                SystemIO.printString("They Send A Blitz!\n");

                            }
                        }));
        instructionList.add(
                new BasicInstruction("cointoss",
                        "Displays register purposes and 'starts' the game",
                        BasicInstructionFormat.R_FORMAT,
                        "111111 00000 00000 00000 00000 000000",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                SystemIO.printString("$t1 = yard number\n" +
                                        "$t3 = Loaded yards to add to $t1 (sp, mp, blitz, etc)\n" +
                                        "$t5 = Team 1 score\n" +
                                        "$t6 = Team 2 score\n\n" +
                                        "Let The Game Begin! \n");
                                down = 1;
                                yardsToGo = 10;

                            }
                        }));
        instructionList.add(
                new BasicInstruction("defended $t3",
                        "Prints various calls for a defended play and current down",
                        BasicInstructionFormat.R_FORMAT,
                        "000000 00000 fffff 00000 00000 000010",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                int[] operands = statement.getOperands();
                                int call = RegisterFile.getValue(operands[0]);
                                switch (call) {
                                    case -3:
                                        SystemIO.printString("The Blitz Is Picked Up!\n");
                                        break;
                                    case 3:
                                        SystemIO.printString("The Run Is Stopped At The Line Of Scrimmage!\n");
                                        down++;
                                        break;
                                    default:
                                        SystemIO.printString("The Pass Is Broken Up!\n");
                                        down++;
                                        break;
                                }
                                if (down == 5) {
                                    SystemIO.printString("Turnover\n");
                                    down = 1;
                                    yardsToGo = 10;
                                }
                                else if (call == -3) {
                                    SystemIO.printString("Let's See How This Plays Out \n");
                                }
                                else {
                                    switch (down) {
                                        case 1:
                                            SystemIO.printString("1st Down\n");
                                            break;
                                        case 2:
                                            SystemIO.printString("2nd Down\n");
                                            break;
                                        case 3:
                                            SystemIO.printString("3rd Down\n");
                                            break;
                                        case 4:
                                            SystemIO.printString("4th Down\n");
                                            break;
                                    }
                                }
                            }
                        }));
        instructionList.add(
                new BasicInstruction("fieldgoal $t5",
                        "Adds 3 to score register and prints Field Goal Is Good!",
                        BasicInstructionFormat.R_FORMAT,
                        "010000 00000 fffff 00000 00000 000011",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                int[] operands = statement.getOperands();
                                int currPoints = RegisterFile.getValue(operands[0]);
                                int points = currPoints + 3;
                                RegisterFile.updateRegister(operands[0], points);
                                SystemIO.printString("Field Goal Is Good!\n");
                            }
                        }));
        instructionList.add(
                new BasicInstruction("hike",
                        "Prints Ready, Set, Hike!",
                        BasicInstructionFormat.R_FORMAT,
                        "000000 00000 00000 00000 00000 000100",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                SystemIO.printString("Ready, Set, Hike!\n");
                            }
                        }));
        instructionList.add(
                new BasicInstruction("intercep $t1",
                        "Loads 50 into $t1 and prints Interception!",
                        BasicInstructionFormat.R_FORMAT,
                        "000000 00000 fffff 00000 00000 000101",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                int[] operands = statement.getOperands();
                                SystemIO.printString("Interception!\n");
                                down = 1;
                                yardsToGo = 10;
                                RegisterFile.updateRegister(operands[0], 50);
                            }
                        }));
        instructionList.add(
                new BasicInstruction("kickoff $t1",
                        "Loads 100 into $t1, allows for choice between tback and kickreturn, and prints Kickoff!",
                        BasicInstructionFormat.R_FORMAT,
                        "000000 00000 fffff 00000 00000 000110",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                SystemIO.printString("Kickoff!\n");
                            }
                        }));
        instructionList.add(
                new BasicInstruction("kickreturn $t1",
                        "Sets the value in $t1 to 30 and prints He Returns It To The 30",
                        BasicInstructionFormat.R_FORMAT,
                        "111000 00000 fffff 00000 00000 011001",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                int[] operands = statement.getOperands();
                                int yard = 30;
                                RegisterFile.updateRegister(operands[0], yard);
                                SystemIO.printString("He Returns It To The 30\n");
                                down = 1;
                                yardsToGo = 10;
                            }
                        }));
        instructionList.add(
                new BasicInstruction("lp $t3",
                        "Loads 15 into $t3 and prints Long Pass To The Receiver",
                        BasicInstructionFormat.R_FORMAT,
                        "000100 00000 fffff 00000 00000 001111",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                int[] operands = statement.getOperands();
                                int yard = 15;
                                RegisterFile.updateRegister(operands[0], yard);
                                SystemIO.printString("Long Pass To The Receiver\n");
                            }
                        }));
        instructionList.add(
                new BasicInstruction("man",
                        "Prints The Defense Is Showing Man Coverage",
                        BasicInstructionFormat.R_FORMAT,
                        "000000 00000 00000 00000 00000 000111",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                SystemIO.printString("The Defense Is Showing Man Coverage\n");
                            }
                        }));
        instructionList.add(
                new BasicInstruction("mp $t3",
                        "Loads 8 into $t3 and prints Medium Pass To The Receiver",
                        BasicInstructionFormat.R_FORMAT,
                        "000010 00000 fffff 00000 00000 001000",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                int[] operands = statement.getOperands();
                                int yard = 8;
                                RegisterFile.updateRegister(operands[0], yard);
                                SystemIO.printString("Medium Pass To The Receiver\n");
                            }
                        }));
        instructionList.add(
                new BasicInstruction("pat $t5",
                        "Adds 1 to score register and prints PAT Is Good",
                        BasicInstructionFormat.R_FORMAT,
                        "001000 00000 fffff 00000 00000 000001",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                int[] operands = statement.getOperands();
                                int currScore = RegisterFile.getValue(operands[0]);
                                RegisterFile.updateRegister(operands[0], currScore + 1);
                                SystemIO.printString("PAT Is Good\n");
                            }
                        }));
        instructionList.add(
                new BasicInstruction("punt",
                        "Prints Punt!",
                        BasicInstructionFormat.R_FORMAT,
                        "000000 00000 00000 00000 00000 001001",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                SystemIO.printString("Punt!\n");
                            }
                        }));
        instructionList.add(
                new BasicInstruction("puntreturn $t1",
                        "Sets the value in $t1 to 40 and prints Ball Spotted At The 40",
                        BasicInstructionFormat.R_FORMAT,
                        "111100 00000 fffff 00000 00000 011001",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                int[] operands = statement.getOperands();
                                int yard = 40;
                                RegisterFile.updateRegister(operands[0], yard);
                                SystemIO.printString("Ball Spotted At The 40\n");
                                down = 1;
                                yardsToGo = 10;
                            }
                        }));
        instructionList.add(
                new BasicInstruction("run $t3",
                        "Loads 3 into $t3 and prints It's A Handoff",
                        BasicInstructionFormat.R_FORMAT,
                        "000011 01011 fffff 00000 00000 000011",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                int[] operands = statement.getOperands();
                                int yard = 3;
                                RegisterFile.updateRegister(operands[0], yard);
                                SystemIO.printString("It's A Handoff\n");
                            }
                        }));
        instructionList.add(
                new BasicInstruction("scoreboard $t5, $t6",
                        "Prints score registers",
                        BasicInstructionFormat.R_FORMAT,
                        "000000 sssss fffff 00000 00000 011110",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                int[] operands = statement.getOperands();
                                int homeTeam = RegisterFile.getValue(operands[0]);
                                int awayTeam = RegisterFile.getValue(operands[1]);
                                SystemIO.printString(homeTeam + " : " + awayTeam + "\n");
                            }
                        }));
        instructionList.add(
                new BasicInstruction("sp $t3",
                        "Loads 5 into $t3 and prints Short Pass To The Receiver",
                        BasicInstructionFormat.R_FORMAT,
                        "000001 00000 fffff 00000 00000 000101",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                int[] operands = statement.getOperands();
                                int yard = 5;
                                RegisterFile.updateRegister(operands[0], yard);
                                SystemIO.printString("Short Pass To The Receiver\n");
                            }
                        }));
        instructionList.add(
                new BasicInstruction("success $t3, $t1",
                        "Adds value in $t3 to $t1 and prints various calls for a successful play",
                        BasicInstructionFormat.R_FORMAT,
                        "000000 sssss fffff 00000 00000 011111",
                        new SimulationCode() {

                            public void simulate(ProgramStatement statement) throws ProcessingException {
                                int[] operands = statement.getOperands();
                                int prevPlay = RegisterFile.getValue(operands[0]);
                                int currYards = RegisterFile.getValue(operands[1]);
                                int newYards = prevPlay + currYards;
                                RegisterFile.updateRegister(operands[1], newYards);
                                down++;
                                yardsToGo -= prevPlay;
                                int call = prevPlay;
                                switch (call) {
                                    case -3:
                                        SystemIO.printString("They Get The Tackle In The Backfield!\n");
                                        break;
                                    case 3:
                                        SystemIO.printString("He Gets About 3 Yards On That Run\n");
                                        break;
                                    default:
                                        SystemIO.printString("What A Catch!\n");
                                        break;
                                }
                                if (yardsToGo <= 0) {
                                    SystemIO.printString("First Down!\n");
                                    down = 1;
                                    yardsToGo = 10;
                                }
                                else if (down == 5) {
                                    SystemIO.printString("Turnover\n");
                                    down = 1;
                                } else {
                                    switch (down) {
                                        case 1:
                                            SystemIO.printString("1st Down\n");
                                            break;
                                        case 2:
                                            SystemIO.printString("2nd Down\n");
                                            break;
                                        case 3:
                                            SystemIO.printString("3rd Down\n");
                                            break;
                                        case 4:
                                            SystemIO.printString("4th Down\n");
                                            break;
                                    }
                                }
                            }
                        }));
        instructionList.add(
                new BasicInstruction("tback $t1",
                        "Sets $t1 to 25 if the value in $t1 >= 100 and prints It's A Touchback",
                        BasicInstructionFormat.R_FORMAT,
                        "111110 00000 fffff 00000 00000 011001",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                int[] operands = statement.getOperands();
                                int yardage = RegisterFile.getValue(operands[0]);
                                if (yardage >= 100) {
                                    RegisterFile.updateRegister(operands[0], 25);
                                    SystemIO.printString("It's A Touchback\n");
                                    down = 1;
                                    yardsToGo = 10;
                                }
                            }
                        }));
        instructionList.add(
                new BasicInstruction("tdown $t5, $t1",
                        "Adds 6 to score register if $t1 >= 100 and prints TOUCHDOWN!",
                        BasicInstructionFormat.R_FORMAT,
                        "011110 sssss fffff 00000 00000 000000",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                int[] operands = statement.getOperands();
                                int currPoints = RegisterFile.getValue(operands[0]);
                                int points = currPoints + 6;
                                if (RegisterFile.getValue(operands[1]) >= 100) {
                                    RegisterFile.updateRegister(operands[0], points);
                                    SystemIO.printString("TOUCHDOWN!\n");
                                }
                                else {
                                    SystemIO.printString("They're Driving To The Endzone\n");
                                }
                            }
                        }));
        instructionList.add(
                new BasicInstruction("truck",
                        "Prints BOOM!",
                        BasicInstructionFormat.R_FORMAT,
                        "000000 00000 00000 00000 00000 001100",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                SystemIO.printString("BOOM!\n");
                            }
                        }));

        instructionList.add(
                new BasicInstruction("yardline $t1",
                        "Prints $t1",
                        BasicInstructionFormat.R_FORMAT,
                        "000000 00000 fffff 00000 00000 001101",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                int[] operands = statement.getOperands();
                                SystemIO.printString("Yard Line: " + RegisterFile.getValue(operands[0]) + "\n");
                            }
                        }));
        instructionList.add(
                new BasicInstruction("zone",
                        "Prints The Defense Is Showing Zone Coverage",
                        BasicInstructionFormat.R_FORMAT,
                        "000000 00000 00000 00000 00000 001110",
                        new SimulationCode()
                        {

                            public void simulate(ProgramStatement statement) throws ProcessingException
                            {
                                SystemIO.printString("The Defense Is Showing Zone Coverage\n");
                            }
                        }));
    }

}
